package smtp;

import java.util.Scanner;
import MainPackage.Chat_List;

/**
 * MailReceiver : 메일 받아오기
 * MailSender : 메일 전송하기
 */
public class MainProcessor {
    private MailClient mailClient;
    private User user;
    public UserAgent userAgent1;
    
    Chat_List chat_list;
    String email;
    String password;

	public MainProcessor(Chat_List chat_list) {
		this.chat_list = chat_list;
	}
	
	public void testing() { System.out.print("testing"); }
	
	public boolean init_user_and_agent(String email, String password) {
		
		this.mailClient = new MailClient();
		this.email = email;
		this.password = password;
		
		user = new User(email, password);
		// 로그인 성공 여부를 확인하고 MongoDB에 사용자 이메일 저장
		// 로그인을 성공적으로 한 경우 참 반환, 실패시 거짓 반
		boolean loginSuccess = log_in(email, password);
        

        return loginSuccess;
	}
	
	private boolean log_in(String email, String password) {
		boolean success_log_in = false;
		
		if(!email.contains("gmail")&&!email.contains("naver")){
            System.out.println("Login Fail");
            success_log_in = false;
        }
        try {
            if (email.contains("gmail")) {
            	success_log_in = mailClient.loginAuth("smtp.gmail.com",email,password);
            }

            if (email.contains("naver")) {
            	success_log_in = mailClient.loginAuth("smtp.naver.com",email,password);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        
        return success_log_in;
	}
	
	public void erase_user() { 
		this.user = null;
		this.userAgent1 = null;
		this.mailClient = null;
	}
	
	// userAgent1이 할당되지 않았으면 오류 발생 가능 
	// 하지만 여기서는 할당되었다는 가정 하에 코드를 작성하겠다.
	public void send(String receiver, String title, String content) { 
		userAgent1 = new UserAgent(mailClient, user);
		userAgent1.sendMailToMailServer(receiver, title, content); 
	}//MailClient에 메일 전송
	
	
	public void read() {
		if(email.contains("gmail")) { (new MailReceiverFromGmail(email,password, chat_list)).receiveMail(); }
        if(email.contains("naver")) { (new MailReceiverFromNaver(email,password, chat_list)).receiveMail(); }
        System.out.println("check");
	}
	
	
	// 참고용 코드 실제로는 안 쓰일 예정 
	/*
	 public void run(String[] args) {

        //MailClient mailClient = new MailClient();

        Scanner sc = new Scanner(System.in);

       System.out.println("사용할 메일 계정 입력(gamil은 설정한 앱 비밀번호 사용해야 함)");
       System.out.print("email : ");
       String email = sc.nextLine();
       System.out.print("password : ");
       String password = sc.nextLine();


        while(true) {
            System.out.print("메일 읽기 read 입력,메일 보내기 send 입력 끝내기 exit 입력: ");
            String input = sc.nextLine();
            
            if(input.equals("exit")){ break; }

            if (input.equals("read"))//메일 읽기
                (new MailReceiver(email,password)).receiveMail();


            //메일 보내기
            //UserAgent에서 메일을 작성하고 sendMailToMailServer()을 통해 메일 전송
             
            if (input.equals("send")) {
                User user = new User(email, password);
                UserAgent userAgent1 = new UserAgent(mailClient, user);//메일 작성
                //userAgent1.sendMailToMailServer();//MailClient에 메일 전송
            }
        }


    	}
	 */
    
}