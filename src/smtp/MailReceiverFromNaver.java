package smtp;

import MainPackage.Chat_List;
import MainPackage.Chat_List.ChatMassage;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Naver pop서버와 통신해서 받은메일을 보는 클래스
 * 주석은 MailReceiverFromGmail 참고
 */
public class MailReceiverFromNaver {
    private String POP3_SERVER = "pop.naver.com";
    private static final int POP3_PORT = 995;
    
    Chat_List chat_list;
    String email;
    String password;

    String mailCount;
    boolean isBodyStart = false;
    String encodingType;
    String contentType;

    public MailReceiverFromNaver(String email, String password, Chat_List chat_list) {
        this.email = email;
        this.password = password;
        this.chat_list = chat_list;
    }

    public void receiveMail() {
        try {
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(POP3_SERVER, POP3_PORT);
            sslSocket.startHandshake();

            BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

            System.out.println("Server: " + reader.readLine());

            sendCommand(writer, reader, "USER " + email);
            sendCommand(writer, reader, "PASS " + password);
            sendCommand(writer, reader, "STAT");

            int mailID = Integer.parseInt(mailCount);
            int cnt = 0;
            
            // 여기서 메일 읽어옵니다. 
            for (int i = mailID; i >= Math.max(1, mailID - 5); i--) {
                System.out.println("----------------------------------------------------------------");
                sendCommandToReadMail(writer, reader, "RETR " + i);
                System.out.println("-------- --------------------------------------------------------\n");
                isBodyStart = false;
                Thread.sleep(500);
                
                cnt++;
            } 
            System.out.println("mail : " + Integer.toString(cnt));

            //chat_list.add_basic_List();
            
            sendCommand(writer, reader, "QUIT");

            writer.close();
            reader.close();
            sslSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //서버에 명령어를 전달( RETR 명령어 제외)하고 응답을 받아오는 함수
    private void sendCommand(BufferedWriter writer, BufferedReader reader, String command) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
        System.out.println("Client: " + command);

        String response;
        while ((response = reader.readLine()) != null) {
            System.out.println("Server: " + response);
            if (response.startsWith("+OK") || response.equals(".")) {
                break;
            }
        }
        if (command.contains("STAT")) {
            String[] parts = response.split(" ");
            mailCount = parts[1];
        }
    }

    //서버로 RETR 명령어를 전송할때 마다 호출되는 함수,여기서 받는 응답은 메일 내용임
    private void sendCommandToReadMail(BufferedWriter writer, BufferedReader reader, String command) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
        System.out.println("Client: " + command);

        
        // 변경 내용 
    	String date = null;
    	String from = null;
    	String to = null;
    	String title = null;
    	String finalBody = null;
    	
        String response;
        StringBuilder contentBody = new StringBuilder();


        //이 while문에서 받는 응답은 받은 메일 정보임
        while ((response = reader.readLine()) != null) {
            //서버 응답이 공백이면 body내용 시작임
            if (response.isEmpty()) {
                isBodyStart = true;
            }


            /**
             * 응답의 인코딩 방식을 알아냄
             */
            if (response.contains("Content-Type: text/plain")) {
                contentType = "text/plain";
                encodingType = null;
            } else if (response.contains("Content-Type: text/html")) {
                contentType = "text/html";
                encodingType = null;
            }
            //응답으로부터 인코딩 방식을 알아냄
            if (response.contains("Content-Transfer-Encoding:")) {
                encodingType = response.split(" ")[1];
            }

            /**
             * 바디 내용이면 인코딩된 타입에 맞게 디코딩해줌
             * encodingType==null이면 디코딩 안함
             */
            if (isBodyStart) {
                try {
                    if (encodingType != null && encodingType.equals("base64")) {
                        contentBody.append(decodeBase64(response));
                    } else if (encodingType != null && encodingType.equals("quoted-printable")) {
                        contentBody.append(decodeQuotedPrintable(response, StandardCharsets.UTF_8));
                    } else {
                        contentBody.append(response).append("\n");
                    }
                } catch (Exception e) {
                    contentBody.append(response);
                    System.out.println("Server : " + response);
                }
            }

            /**
             * 응답이 바디가 아니면 원하는 정보만 파싱해서 출력함
             * Date,From,To,Subject만 출력한다.적절히 잘라서 출력해줌,경우에 따라 디코딩 필요
             */
            if(!isBodyStart) {
            	
                if (response.startsWith("Date:")) {
                    System.out.println("Server : " + response);
                    
                	// 변경내용 
                    date = response;
                }
                if (response.startsWith("From:")) {
                    if(response.contains("?")){
                        from = response.split("\\?")[3];
                        byte[] decodedBytes = Base64.getDecoder().decode(from);
                        String decodedFrom = new String(decodedBytes, StandardCharsets.UTF_8);
                        System.out.println("Server : From :" + decodedFrom);
                        from = decodedFrom;
                    }
                    else if(response.contains("<")){
                        String[] temp = response.split("<");
                        if (temp.length > 1) { // 배열 길이를 확인하여 안전하게 접근

                            // 변경내용
                            from = temp[1].replace(">", ""); // 닫는 꺽쇠 '>'도 제거

                            System.out.println("Server : From : " + from);
                        }
                    }
                    else {
                        System.out.println("Server : From : " + response.substring(5).trim()); // "From: " 이후의 내용을 출력
                        from = response;
                    }
                }
                if (response.startsWith("To:")) {
                    if (response.contains("<")) { // "<"가 포함된 경우만 처리
                        String[] temp = response.split("<");
                        
                    	// 변경내용 
                        to = temp[1].replace(">", ""); // 닫는 꺽쇠 '>'도 제거
                        
                        System.out.println("Server : To : " + to);
                    } else {
                        System.out.println("Server : " + response);
                        to = response;
                    }
                }

                if (response.startsWith("Subject:")) {
                    try {
                        if (response.contains("?")) {//Subject에 "?"이 포함되면 디코딩 해줌
                            String[] temp = response.split("\\?");
                            String encodedSubject = temp[3];
                            byte[] decodedBytes = Base64.getDecoder().decode(encodedSubject);
                            String decodedSubject = new String(decodedBytes, StandardCharsets.UTF_8);
                            System.out.println("Server : Subject : " + decodedSubject);
                            
                        	// 변경내용 
                            title = decodedSubject;
                        } else {
                            System.out.println("Server : " + response);
                            title = response;
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
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }


    //Quoted-printable로 인코딩된 문자열을 디코딩함
    public static String decodeQuotedPrintable(String input, Charset charset) {
        StringBuilder output = new StringBuilder();
        int length = input.length();

        for (int i = 0; i < length; i++) {
            char currentChar = input.charAt(i);

            if (currentChar == '=' && i + 2 < length) {
                char hex1 = input.charAt(i + 1);
                char hex2 = input.charAt(i + 2);

                try {
                    int decodedByte = Integer.parseInt("" + hex1 + hex2, 16);
                    output.append((char) decodedByte);
                    i += 2;
                } catch (NumberFormatException e) {
                    output.append(currentChar);
                }
            } else {
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

