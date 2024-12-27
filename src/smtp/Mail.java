package smtp;


/**
 * Mail 클래스에 사용자의 비밀번호가 들어가는건 좀 이상한듯
 */
public class Mail {
    private String from;
    private String to;
    private String subject;
    private String body;
    private String password;

    public Mail(String from, String to, String subject, String body,String password) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.password = password;

    }
    
    public void setMail(String from, String to, String subject, String body,String password) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.password = password;

    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getPassword() {
        return password;
    }
}
