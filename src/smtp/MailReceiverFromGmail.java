package smtp;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import MainPackage.Chat_List;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


/**
 * Gmail용 pop서버와 통신해서 메일 읽어옴
 * POP3 프로토콜 이용
 * sendCommandToReadMail()는 sendCommand()와 달리 pop서버로 부터 메일 내용을 가져오는 RETR 명령어에 대해서만 사용됨
 *
 */
public class MailReceiverFromGmail {
    private String POP3_SERVER="pop.gmail.com"; //pop서버의 주소
    private static final int POP3_PORT = 995; // SSL용 포트,네이버 구글 모두 같은 포트넘버 사용
    
    Chat_List chat_list;
    String email;
    String password;

    //pop서버에서 읽을 수 있는 메일의 개수를 저장할 변수
    String mailCount;

    //sendCommandToReadMail()에서 사용됨,서버로부터의 응답에서 필요한 부분부터 출력하기위해 사용함
    boolean isBodyStart =false;
    String encodingType;
    String contentType;


    public MailReceiverFromGmail(String email, String password, Chat_List chat_list) {//사용자의 계정을 통해서 연결할 pop서버 설정
        this.email = email;
        this.password = password;
        this.chat_list = chat_list;
    }

    public void receiveMail(){
        try {
            // SSL 소켓을 통한 POP3 서버 연결
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(POP3_SERVER, POP3_PORT);
            sslSocket.startHandshake();

            BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

            // 서버 초기 응답 확인
            System.out.println("Server: " + reader.readLine());

            // USER 명령으로 사용자 이름 전송
            sendCommand(writer, reader, "USER " + email);


            // PASS 명령으로 비밀번호 전송
            sendCommand(writer, reader, "PASS " + password);

            // STAT 명령으로 메일 개수와 전체 크기 확인
            sendCommand(writer, reader, "STAT");

            int mailID = Integer.parseInt(mailCount);//STAT명령어로 매일 개수 알아냄

            for(int i=mailID;i>=Math.max(1,mailID-5);i--){//최신메일 6개 읽아옴
                String s = Integer.toString(i);
                System.out.println("----------------------------------------------------------------");
            //ex) 첫 번째 메일을 확인 (RETR 1)
                sendCommandToReadMail(writer, reader, "RETR "+s);//RETR에 대해서는 sendCommandToReadMail호출
                System.out.println("----------------------------------------------------------------\n");
                isBodyStart =false;
                Thread.sleep(500);//굳이 필요 없음
            }

            // QUIT 명령으로 세션 종료
            sendCommand(writer, reader, "QUIT");

            // 자원 정리
            writer.close();
            reader.close();
            sslSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //서버에 명령어를 전달( RETR 명령어 제외)하고 응답을 받아오는 함수
    private void sendCommand(BufferedWriter writer, BufferedReader reader, String command) throws IOException {
        //명령어 전송
        writer.write(command + "\r\n");
        writer.flush();
        System.out.println("Client: " + command);

        //응답 받아오기
        String response;
        while ((response = reader.readLine()) != null) {//서버 응답이 끝날때 까지 계속 출력
            System.out.println("Server: " + response);
            if (response.startsWith("+OK") || response.equals(".")){
                    break; // 응답 완료 시 종료
                }
        }
        if(command.contains("STAT")){//STAT 명령어면 서버의 응답에서 읽을 수 있는 메일개수를 알아냄
            String[] parts = response.split(" ");
            mailCount = parts[1];
            System.out.println("mail count : " + mailCount.toString());
        }

    }



    //서버로 RETR 명령어를 전송할때 마다 호출되는 함수,여기서 받는 응답은 메일 내용임
    private void sendCommandToReadMail(BufferedWriter writer, BufferedReader reader, String command) throws IOException {
        //명령어 전송
        writer.write(command + "\r\n");
        writer.flush();
        System.out.println("Client: " + command);

        // 변경 내용 
    	String date = null;
    	String from = null;
    	String to = null;
    	String title = null;
    	String finalBody = null;
    	
        //응답받아오기,디코딩과정
        String response;//서버로부터 응답을 저장할 변수
        StringBuilder contentBody = new StringBuilder();//서버의 응답중 body내용만 저장할 번수
        
        System.out.println("check");

        //--------------------------------------------------------------------------
        //이 while문에서 받는 응답은 받은 메일 정보임
        while ((response = reader.readLine()) != null) {

            //서버 응답이 공백이면 body내용 시작임
            if(response.isEmpty()){
                isBodyStart =true;
            }
            /**
             * 응답의 인코딩 방식을 알아냄
             */
            if(response.contains("Content-Type: text/plain")){
                contentType = "text/plain";
                encodingType=null;
            }
            if(response.contains("Content-Type: text/html")){
                contentType = "text/html";
                encodingType=null;
            }
            if(response.contains("Content-Type: image/png")){
                contentType = "image/png";
                encodingType=null;
            }
            if(response.contains("Content-Transfer-Encoding:")){
                encodingType = response.split(" ")[1];
            }

            /**
             * 바디 내용이면 인코딩된 타입에 맞게 디코딩해줌
             * encodingType==null이면 디코딩 안함
             */
            if(isBodyStart){
                try {
                    if(encodingType==null){
                        System.out.println("Server : " + response);
                        contentBody.append(response);
                    }
                    else if(encodingType.equals("base64")) {
                        String decodedBody = decodeBase64(response);
                        contentBody.append(decodedBody);
                        System.out.println("Server : " + decodedBody);
                    }
                    else if (encodingType.equals("quoted-printable")) {
                        String decodedBody = decodeQuotedPrintable(response,StandardCharsets.UTF_8);
                        contentBody.append(decodedBody);
                        System.out.println("Server : " + decodedBody);
                    }
                }
                catch (Exception e){
                    contentBody.append(response);
                    System.out.println("Server : " + response);
                }
            }
            /**
             * 응답이 바디가 아니면 원하는 정보만 파싱해서 출력함
             * Date,From,To,Subject만 출력한다.적절히 잘라서 출력해줌,경우에 따라 디코딩 필요
             */
            if(!isBodyStart) {
                if (response.startsWith("Date:") || response.startsWith("To:")) {
                    System.out.println("Server : " + response);
                    
                    if(response.startsWith("Date:")) { date = response;}
                    if(response.startsWith("To:")) { to = response; }
                }
                if (response.startsWith("From:")) {
                    try {
                        String[] temp = response.split("<");
                        from = temp[1].replace(">", "");;
                        System.out.println("From : " + from);
                    }
                    catch (Exception e){
                        from = response;
                        System.out.println(response);
                    }

                }
                if (response.startsWith("Subject:")) {
                    try {
                        if (response.contains("?")) {
                            String[] temp = response.split("\\?");
                            String encodedSubject = temp[3];
                            byte[] decodedBytes = Base64.getDecoder().decode(encodedSubject);
                            String decodedSubject = new String(decodedBytes, StandardCharsets.UTF_8);
                            System.out.println("Server : Subject : " + decodedSubject);

                        	// 변경내용 
                            title = decodedSubject;
                            
                        } else {
                            title = response;
                            System.out.println("Server : " + response);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            if ( response.equals(".")){
                break; // 응답 완료 시 종료
            }
            
        }
        //--------------------------------------------------------------------------
        /**
         * 위의 while문에서 서버로 부터 검색을 요청한 메일(하나)에 대한 정보를 모두 읽고 원하는 정보를 변수에 저장함
         */
        //응답받은 바디의 html태그 제거,html태크는 응답의 타입에 따라 없을수도 있음
        finalBody = removeHtmlTags(contentBody.toString());

        System.out.println("Server (Body): " + finalBody);
        
        System.out.println("From : " + from);
        System.out.println("To : " + to);
        System.out.println("Title : " + title);
        System.out.println("Contents : " + finalBody);
        chat_list.add_new_massage(from, to, title, finalBody, true);
    }

    //base64로 인코딩된 문자열을 디코딩함
    public static String decodeBase64(String encodedText) {

        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
        String text = new String(decodedBytes, StandardCharsets.UTF_8);
        //return new String(decodedBytes);
        return text;
    }
    //Quoted-printable로 인코딩된 문자열을 디코딩함
    public static String decodeQuotedPrintable(String input,Charset charset) {
        StringBuilder output = new StringBuilder();
        int length = input.length();

        for (int i = 0; i < length; i++) {
            char currentChar = input.charAt(i);

            // '='로 시작하면 다음 두 문자를 16진수로 변환
            if (currentChar == '=' && i + 2 < length) {
                char hex1 = input.charAt(i + 1);
                char hex2 = input.charAt(i + 2);

                // 16진수로 변환하여 문자 추가
                try {
                    int decodedByte = Integer.parseInt("" + hex1 + hex2, 16);
                    output.append((char) decodedByte);
                    i += 2; // 인덱스를 두 칸 더 이동
                } catch (NumberFormatException e) {
                    // 형식이 잘못되었으면 '=' 그대로 추가
                    output.append(currentChar);
                }
            } else {
                // '='가 아니면 그대로 추가
                output.append(currentChar);
            }
        }
        /**
         * Quoted-printable 인코딩을 디코딩하면 특정 바이트 배열이 생성되는데, 이 배열을 문자 데이터로 변환할 때 데이터 손실 없이 사용하고 싶다면 ISO-8859-1을 이용하는 것이 적합합니다.
         * 예를 들어, UTF-8로 바로 변환할 경우 일부 바이트가 가변 길이로 변환될 수 있기 때문에 원본 데이터가 손상될 수 있지만, ISO-8859-1을 중간에 사용하면 이러한 위험이 없습니다
         */
        byte[] bytes = output.toString().getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, charset);

    }

    //문자열에서 태그 제거
    public static String removeHtmlTags(String htmlText) {
        htmlText = htmlText.replaceAll("(?s)<style.*?>.*?</style>", "");
        htmlText = htmlText.replaceAll("<[^>]*>", "");
        htmlText = htmlText.replaceAll("&nbsp;", " ").replaceAll("&gt;", ">").replaceAll("&lt;", "<");
        return htmlText.trim();
    }
}
