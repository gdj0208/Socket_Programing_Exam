package smtp;

import java.util.Scanner;

/**
 * 실제 매일을 구성함
 * UserAgent가 sendMailToMailServer를 호출하면 메일을 MailClient의 메일큐에 보냄
 * 그러면 MailClient가 큐에서 메일을 꺼내서 메일 전송
 */
public class UserAgent {
    private MailClient mailClient;
    private User user;
    private Mail mail;




    public UserAgent(MailClient mailClient, User user) {//UserAgent를 구성하고 메일을 만듦
        this.mailClient = mailClient;
        this.user=user;
        //this.makeMail(new Mail());
    }

    /*
     public void makeMail(Mail mail){//메일을 만듦
        Scanner sc = new Scanner(System.in);

        System.out.println("메일 작성");

        System.out.print("받는 사람 메일: ");
        String to = sc.nextLine();

        String from = user.getEmail();

        System.out.print("메일 제목 작성: ");
        String subject = sc.nextLine();

        System.out.print("메일 내용 작성: ");
        String body = sc.nextLine();

        mail.setMail(from, to, subject, body,user.getPassword());
        this.mail = mail;
    }
     */
    
    /*
    public void makeMail(String receiver, String title, String content){//메일을 만듦
       this.mail = new Mail(user.getEmail(), receiver, title, content, user.getPassword());
       mail.setMail(user.getEmail(), receiver, title, content, user.getPassword());
        
    } 
     */
    

    public void sendMailToMailServer(String receiver, String title, String content) {
    	//this.makeMail(new Mail());
    	this.mail = new Mail(user.getEmail(), receiver, title, content, user.getPassword());
    	
        mailClient.queueMail(mail); // 메일을 서버의 전송 큐로 전송
        //System.out.println("Mail queued for delivery.");
    }
}
