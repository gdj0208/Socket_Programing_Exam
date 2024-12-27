package smtp;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.Base64;


/**
 * 인자로 받은 메일정보를 기반으로 서버와 연결,인증,전송
 * sendCommand()함수는 연결된 서버로 메세지를 보내고 그에 대한 서버의 응답을 받아옴,둘다 콘솔에 출력
 */
public class MailSender {
    String smtpServer;
    private Mail mail;
    int port = 587;//TLS는 587포트 사용

    public MailSender(Mail mail) throws IOException {//메일 정보를 기반으로 연결할 smtp 서버 설정
        this.mail = mail;
        if(mail.getFrom().contains("gmail"))//mail이 gmail 계정이면 구글 메일 서버와 연결
            this.smtpServer = "smtp.gmail.com";
        if(mail.getFrom().contains("naver"))//mail이 naver계정이면 네이버 메일 서버와 연결
            this.smtpServer = "smtp.naver.com";
    }

    public void sendMail(){
        try {
            // 일반 소켓 생성 후 연결
            Socket socket = new Socket(smtpServer, port);
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
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(socket, smtpServer, port, true);
            sslSocket.startHandshake(); // TLS 핸드셰이크 시작

            // SSL 소켓으로 새로 연결된 읽기/쓰기 스트림 설정
            reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

            // 다시 HELO 명령어 전송 (TLS 세션이 새로 시작됨)
            sendCommand(writer, reader, "HELO " + smtpServer);

            //연결한 메일 서버로부터 작성자의 메일주소,비밀번호 인증 (AUTH LOGIN)
            sendCommand(writer, reader, "AUTH LOGIN");
            sendCommand(writer, reader, Base64.getEncoder().encodeToString(mail.getFrom().getBytes()));
            sendCommand(writer, reader, Base64.getEncoder().encodeToString(mail.getPassword().getBytes()));

            //문법에 맞게 메일 헤더 전송
            sendCommand(writer, reader, "MAIL FROM:<" + mail.getFrom() + ">");
            sendCommand(writer, reader, "RCPT TO:<" + mail.getTo() + ">");
            sendCommand(writer, reader, "DATA");

            //메일 body 전송
            writer.write("Subject: " + mail.getSubject() + "\r\n");
            writer.write("To: " + mail.getTo() + "\r\n");
            writer.write("From: " + mail.getFrom() + "\r\n");
            writer.write("\r\n" + mail.getBody() + "\r\n.\r\n");
            writer.flush();
            System.out.println("Server: " + reader.readLine());

            // 연결 종료
            sendCommand(writer, reader, "QUIT");

            writer.close();
            reader.close();
            sslSocket.close();
            socket.close();
        } catch (Exception e) {
            System.out.print(e.getMessage());
            e.printStackTrace();
        }
    }


    // SMTP 명령어 전송 헬퍼 메서드,명령어를 보내고 서버의 응답을 받아와서 출력한다
    private static void sendCommand(BufferedWriter writer, BufferedReader reader, String command) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
        System.out.println("Client: " + command);
        System.out.println("Server: " + reader.readLine());
    }
}

