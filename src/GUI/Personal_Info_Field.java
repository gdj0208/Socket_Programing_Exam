package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;

/**
 * Personal_Info_Field 클래스는 이메일 로그인과 로그아웃을 위한 UI 패널을 생성합니다.
 * 이 패널은 사용자의 로그인 상태에 따라 다른 UI 요소를 표시합니다.
 */

public class Personal_Info_Field extends JPanel{

	private Main_Window main_window;         	// 메인 윈도우의 참조를 통해 로그인 상태를 확인 및 변경
    private JTextField SMTP_Server_Info;     	// SMTP 서버 입력 필드
    private JTextField email_input;          	// 이메일 입력 필드
    private JPasswordField password;         	// 비밀번호 입력 필드
    private JButton Log_in;                  	// 로그인 버튼
    private JButton Log_out;                 	// 로그아웃 버튼
    private JButton write_email;             	// 메일 쓰기 버튼
    private String server_string, email_string; // 로그인 후 표시할 서버 정보와 이메일 정보
	
	
	public Personal_Info_Field(Main_Window main_window) {
		this.main_window = main_window;
		
		// 이 클래스의 배치 방법과 여백 설정
		this.setLayout(new GridLayout(3,2,5,5));
		this.setBackground(Color.white);
		this.setBorder(new EmptyBorder(20, 20, 10, 20));
		
        // 초기 UI 상태 업데이트 (로그인 여부에 따라 다른 UI 표시)
		update_panel();	
	}
	
	private void make_unlogged_pannel() {
		// 기존 요소들 제거 
        this.removeAll();
        
        
        // UI 요소 초기화
		SMTP_Server_Info = new JTextField(10);
		email_input = new JTextField(10);
		password = new JPasswordField(10);
		Log_in = new JButton("로그인");

		
		// UI 요소 패널에 추가
		//this.add(new JLabel("서버 "));	this.add(SMTP_Server_Info);
		this.add(new JLabel("이메일 "));	this.add(email_input);
		this.add(new JLabel("PW"));		this.add(password);
		this.add(new JLabel());			this.add(Log_in);
		
		
		// 로그인 버튼 액션 리스너 
		Log_in.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	 /*--------------------------------------------------------------------------------------------*/
            	 //여기에 로그인시 이루어지는 프로세스를 작성해야 합니다.
            	 
            	email_string = email_input.getText();
            	
            	// 로그인을 성공적으로 한 경우 참, 실패시 거짓 반
            	if(main_window.main_processor.init_user_and_agent(email_string, new String(password.getPassword()))) {
            		main_window.log_in_out_update(true);	// 로그인 상태로 변경
            	}
            	else {JOptionPane.showMessageDialog(Personal_Info_Field.this, "로그인에 실패했습니다!","Login Failed", JOptionPane.ERROR_MESSAGE);}
            	//System.out.println(server_string);
            	  
            	 /*--------------------------------------------------------------------------------------------*/
            	
        		//update_panel(); // 패널 업데이트
            }
        });

		// 패널 갱신 
        revalidate();
        repaint();
	}
	private void make_logged_pannel() {
		// 기존 요소 제거 
        this.removeAll();

        
        // UI 요소 초기화
        server_string = "서버정보";
        //email_string = "이메일";
		Log_out = new JButton("로그아웃");
		write_email = new JButton("메일 쓰기");

		
		// UI 요소 패널에 추가
		//this.add(new JLabel("서버 "));	this.add(new JLabel(server_string));
		this.add(new JLabel("이메일 "));	this.add(new JLabel(email_string));
		this.add(new JLabel("PW "));	this.add(new JLabel("**********"));
		this.add(Log_out);				this.add(write_email);
		

		// 로그아웃 버튼 액션리스너 
		Log_out.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	/*--------------------------------------------------------------------------------------------*/
            	//여기에 로그아웃시 이루어지는 프로세스를 작성해야 합니다.
            	 
            	main_window.main_processor.erase_user();
            	
            	/*--------------------------------------------------------------------------------------------*/
            	 

        		main_window.log_in_out_update(false); // 로그인 상태로 변경
            }
        });
		
		// 메일 작성 버튼 액션리스너 
		write_email.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mail_Writter_Window mail_writter_window = new Mail_Writter_Window(main_window);	
			}
		});
				
		// 패널 갱신 
        revalidate();
        repaint();
	}

	public void update_panel() {
        if (main_window.logged_in) { make_logged_pannel(); } 
        else { make_unlogged_pannel(); }
    }
}
