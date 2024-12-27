package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

//메일 세부 내용을 보기 위한 창을 정의하는 클래스
public class Mail_Reader_Window extends JFrame {
	
	// 보낸 사람과 제목을 다루는 내부 클래스 
	class Sender_Info extends JPanel {
		private JLabel sender_label;	// "보낸 사람" 레이블
		private JLabel title_label;		// "제목" 레이블
		private JLabel sender_field;	// 보낸 사람 정보를 표시할 레이블
		private JLabel title_field;		// 제목 정보를 표시할 레이블

		public String sender;			// 보낸 사람 입력 필드 
		public String title;			// 제목 입력 필드 
		
		
		public Sender_Info(String sender, String title) {
			this.sender = sender;
			this.title = title;
			
			this.setLayout(new GridLayout(2,2));
			
			
			// 보낸 사람과 제목 정보를 레이블에 할당
			sender_label = new JLabel("상대방");		sender_field = new JLabel(this.sender);
			title_label = new JLabel("제목");			title_field = new JLabel(this.title);
			
			
			// 패널에 레이블 추가
			this.add(sender_label);		this.add(sender_field);
			this.add(title_label);		this.add(title_field);
		}
		
	}
	
	
	
	private int width = 400;	// 프레임 넓이 
	private int height = 600;	// 프레임 높이 

	private JTextArea text_area;			// 메일 내용 표시
	public JScrollPane scroll_text_area;	// 텍스트 영역 스크롤 가능
	private JButton close_button;			// 보내기 버튼
	public Sender_Info receiver_info;		// 보낸 사람과 제목 정보를 담은 패널
	
	public Mail_Reader_Window(String user_name, String title, String contents) {
		// 제목 설정 
		setTitle("메일");				
				
		// 화면 사이즈 설정 
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
				
		
		// 모든 컴포넌트 초기화 및 추가
		init_all_elements(user_name, title, contents);
		add_all_elements();
				
		
		// 프레임 보이게 하기 
		setVisible(true);
	}
	
	//public void main() {
	//	SwingUtilities.invokeLater(() -> { new Mail_Writter_Window(); });	// Main_Frame 생성 및 표기 
	//}

	// 컴포넌트 초기화 메서드: 보낸 사람, 제목, 메일 내용을 설정
	private void init_all_elements(String sender, String title, String contents) {
		receiver_info = new Sender_Info(sender, title);	// 보낸 사람과 제목 패널 초기화
		
		text_area = new JTextArea(contents);			// 메일 내용을 표시할 텍스트 영역 초기화
		text_area.setEditable(false); 					// 텍스트 영역 읽기 전용으로 설정
        
		scroll_text_area = new JScrollPane(text_area);	// 스크롤 가능하도록 텍스트 영역 감싸기
		

		close_button = new JButton("창 닫기");	
		close_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { dispose(); }	// 창 닫기
        });
	}
	
	private void add_all_elements() {
		this.add(receiver_info, BorderLayout.NORTH);
		this.add(scroll_text_area, BorderLayout.CENTER);
		this.add(close_button, BorderLayout.SOUTH);
	}
	
}