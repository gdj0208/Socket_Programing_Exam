package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

//메일 작성을 위한 창을 정의하는 클래스
public class Mail_Writter_Window extends JFrame {
	
	// 받는 사람과 제목을 다루는 내부 클래스 
	class Receiver_Info extends JPanel {
		private JLabel receiver_label;	
		private JLabel title_label;
		public JTextField receiver_field;	// 수신자 정보 입력 플드  
		public JTextField title_field;		// 제목 입력 필드 
		
		
		public Receiver_Info() {
			this.setLayout(new GridLayout(2,2));
			
			receiver_label = new JLabel("받는 사람");	receiver_field = new JTextField();
			title_label = new JLabel("제목");			title_field = new JTextField();
			
			
			this.add(receiver_label);	this.add(receiver_field);
			this.add(title_label);		this.add(title_field);
		}
	}
	
	
	
	private int width = 400;	// 프레임 넓이 
	private int height = 600;	// 프레임 높이 
	
	private Main_Window main_window;         	// 메인 윈도우의 참조를 통해 로그인 상태를 확인 및 변경
	  
	private JTextArea text_area;			// 메일 내용 입력 필드
	private JScrollPane scroll_text_area;	// 스크롤 가능한 텍스트 영역
	private JButton send_button;			// 보내기 버튼
	private Receiver_Info receiver_info;	// 받는 사람과 제목 정보를 입력받는 패널
	
	private String receiver;	// 받는 사람 
	private String title;		// 제목 
	private String contents;	// 메일 내용 
	
	
	public Mail_Writter_Window(Main_Window main_window) {
		this.main_window = main_window;
		
		// 제목 설정 
		setTitle("메일 작성함");				
				
		
		// 화면 사이즈 설정 
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
				

        // 모든 컴포넌트 초기화 및 추가
		init_all_elements();
		add_all_elements();
		
		
		// 프레임 보이게 하기 
		setVisible(true);
	}
	
	//public void main() {
	//	SwingUtilities.invokeLater(() -> { new Mail_Writter_Window(); });	// Main_Frame 생성 및 표기 
	//}


	// 모든 컴포넌트를 초기화하는 메서드
	private void init_all_elements() {
		receiver_info = new Receiver_Info();							// 받는 사람 및 제목 패널 초기화
		
		text_area = new JTextArea();									// 메일 내용 입력 필드 초기화
		scroll_text_area = new JScrollPane(text_area);					// 텍스트 영역을 스크롤 가능하게 감싸기
		
		send_button = new JButton("보내기");								// 보내기 버튼 생성
		send_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {				
            	receiver = receiver_info.receiver_field.getText();		// 받는 사람 
            	title = receiver_info.title_field.getText();			// 제목 
            	contents = text_area.getText();							// 내용 
            	
            	/*--------------------------------------------------------------------------------------------*/
    		    //여기에 메일 전송 프로세스 추가 
    		            	 
    		    main_window.main_processor.send(receiver, title, contents);
    		    
            	/*--------------------------------------------------------------------------------------------*/

                dispose(); // 창 닫기 (메일을 보냈음을 의미)
            }
        });
	}
	
	private void add_all_elements() {
		this.add(receiver_info, BorderLayout.NORTH);
		this.add(scroll_text_area, BorderLayout.CENTER);
		this.add(send_button, BorderLayout.SOUTH);
	}
	
}
