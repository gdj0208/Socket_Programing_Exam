package smtp;

import java.util.Base64;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import static java.lang.Thread.sleep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * 사용자가 작성한 메일을 큐에 저장하고 큐에 있는 메일을 전송함
 * 멀티쓰레드로 processMailQueue()를 실행시킬거 아니면 굳이 큐에 넣고 다시 뺴서 보내는 과정을 할 필요 없을듯 합니다
 *
 */
public class MailClient {
    private Queue<Mail> mailQueue=null;

    public MailClient() { this.mailQueue = new LinkedList<>(); }

    // 메일을 전송 큐에 추가
    public void queueMail(Mail mail) {
    	mailQueue.offer(mail);//메일 큐에 메일 넣음
        processMailQueue();//큐에 넣고 바로 전송,마찬가지로 단일 쓰레드이기 때문에 불필요한 과정이 됨
        System.out.println("Mail from " + mail.getFrom() + " to " + mail.getTo());

    }

    // 큐에 있는 메일을 전송하는 메서드
    private void processMailQueue() {
        try {
            while (!mailQueue.isEmpty()) {//큐가 빌때까지 계속 메일을 전송
                Mail mail = mailQueue.poll();
                try { sendMailToRecipient(mail); }
                catch (Exception e) { System.out.println(e.getMessage()); }
            }
        }
        catch (Exception e){ System.out.println(e.getMessage()); }

    }

    // 메일을 수신자에게 전송하는 메서드
    private void sendMailToRecipient(Mail mail) {//MailSender를 이용해서 실제로 매일 전송
        System.out.println("Sending mail to " + mail.getTo());
        System.out.println("From: " + mail.getFrom());
        System.out.println("Subject: " + mail.getSubject());
        System.out.println("Body: " + mail.getBody());
        System.out.println("Mail sent successfully!\n");
        try{
            (new MailSender(mail)).sendMail();//MailSender에 mail을 넣어서 sendMail함수 실행
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    // 처음 로그인 할때 서버로 부터 인증을 받기위한 함수
    public boolean loginAuth(String smtpServer,String email,String password) throws Exception{

        Socket socket = new Socket(smtpServer, 587);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        //첫 소켓 연결에 대한 서버 응답 확인
        System.out.println("Server: " + reader.readLine());

        // HELO 명령어로 SMTP 기능 확인
        sendCommand(writer, reader, "HELO " + smtpServer);

        // STARTTLS 명령어 전송해서 TLS로 업그레이드
        sendCommand(writer, reader, "STARTTLS");


        //TLS로 업그레이드에 대해 서버로 부터 확인 받으면 SSL 소켓을 사용해 TLS 핸드셰이크 수행
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(socket, smtpServer, 587, true);
        sslSocket.startHandshake(); // TLS 핸드셰이크 시작

        // SSL 소켓으로 새로 연결된 읽기/쓰기 스트림 설정
        reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

        // 다시 HELO 명령어 전송 (TLS 세션이 새로 시작됨)
        sendCommand(writer, reader, "HELO " + smtpServer);


        /**
         * 연결한 메일 서버로부터 작성자의 메일주소,비밀번호 인증 (AUTH LOGIN)
         * sendCommand()로그인 실패시 false를 반환한다
         * 반환값에 따라 로그인 처리
         */
        sendCommand(writer, reader, "AUTH LOGIN");
        boolean isSuccess = sendCommand(writer, reader, Base64.getEncoder().encodeToString(email.getBytes()));
        isSuccess = sendCommand(writer, reader, Base64.getEncoder().encodeToString(password.getBytes()));
        
        //sendCommand 가 false 반환하면 로그인 실패,오류 던짐
        if(!isSuccess){
            sendCommand(writer, reader, "QUIT");
            writer.close();
            reader.close();
            sslSocket.close();
            socket.close();
            throw new Exception("Login Fail");
            
        }

        sendCommand(writer, reader, "QUIT");

        writer.close();
        reader.close();
        sslSocket.close();
        socket.close();
        
        return isSuccess;
    }

    //서버로 명령어를 보내고 응답을 받는 함수
    private static boolean sendCommand(BufferedWriter writer, BufferedReader reader, String command) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
        System.out.println("Client: " + command);

        String response = reader.readLine();
        System.out.println("Server: " + response);
        if(response.contains("not accepted")){//서버의 응답에 not accepted가 포함되면 로그인 실패임
            return false;
        }
        else{
            return true;
        }


    }
}
