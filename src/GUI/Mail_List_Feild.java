package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import MainPackage.Chat_List;

/**
 * Mail_List_Field 클래스는 이메일 목록을 표시하는 UI 패널을 생성합니다.
 * 이 클래스는 사용자의 로그인 상태에 따라 메일 목록을 업데이트합니다.
 */
public class Mail_List_Feild {
	
	// 메일 한 칸 
	class Mail_Box extends JPanel{
		
		// 메일 송신자와 제목
		public JLabel user_name, title;		
		public JButton show_more;
		private boolean is_receiver;
		
		
		public Mail_Box(String user_name, String title, String contents, boolean is_receiver) {
			/*
			// Mail_Box 구조 
			+-------------------+-----------+
			|	From			|	자세히보기	|				
			|	Title			|			|
			+-------------------+-----------+
			*/
			
			JPanel main_info = new JPanel();
			main_info.setLayout(new BoxLayout(main_info, BoxLayout.Y_AXIS));
			main_info.setBackground(Color.white);
			main_info.setPreferredSize(new Dimension(300, 25));		
			
			
			this.setLayout(new BorderLayout());
			this.setBackground(Color.white);
			
			// 송신자와 제목 라벨 초기화
			this.is_receiver = is_receiver;
			
			if(this.is_receiver) { this.user_name = new JLabel("FROM : \n" + user_name); }
			else { this.user_name = new JLabel("TO : \n" + user_name ); }
			
			this.title = new JLabel("TITLE : " + title);
			
			this.show_more = new JButton("자세히 보기");
			
			
			// 메일 작성 버튼 액션리스너 
			show_more.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				Mail_Reader_Window mail_reader_window = new Mail_Reader_Window(user_name, title, contents);
			    //update_panel(); // 패널 업데이트
			    }
			});
			
			this.setPreferredSize(new Dimension(300, 50));
			

			// 송신자와 제목을 패널에 추가
			main_info.add(this.user_name);
			main_info.add(this.title);
			
			this.add(main_info, BorderLayout.CENTER);
			this.add(show_more, BorderLayout.EAST);
		}
	}
	
	private Main_Window main_window;	// 메인 윈도우 참
	private JPanel list_panel;			// 받은 메일들을 보여주는 패널 
	public JScrollPane scroll_pane;		// list_panel의 정보들을 담은 스크롤 가능한 패널 
	
	
	
	public Mail_List_Feild(Main_Window main_window) {
		this.main_window = main_window;		// main_window 참조 (로그인 여부 확인을 위해 불러옴)
		update_mail_list();					// 초기 메일 목록 업데이트
	}
	
	/*
     * 로그인 상태에 따라 메일 목록을 업데이트합니다.
     * 로그인 시 유저의 메일을 불러오고, 로그아웃 시 메일 목록을 비웁니다.
     */
	public void update_mail_list() {
		
		// 기존 list_panel의 모든 요소 제거
        if (list_panel != null) { list_panel.removeAll(); } 
        else {
            // list_panel 초기화 및 레이아웃 설정
            list_panel = new JPanel();
            list_panel.setLayout(new BoxLayout(list_panel, BoxLayout.Y_AXIS));
            set_scroll_pane(); // 스크롤 패널 설정
        }

        // 로그인한 경우 메일 목록을 설정
        if (main_window.logged_in) { 
        	main_window.main_processor.read();
        	set_list_panel();
        }

        // 변경 사항 반영
        list_panel.revalidate();
        list_panel.repaint();
        scroll_pane.revalidate();
        scroll_pane.repaint();
	}
	 
	private void set_list_panel() {
		// 로그인한 유저의 메일들을 불러옵니다.
		for(int i = 0; i < main_window.chat_list.lists.size(); i++) {
			list_panel.add(new Mail_Box(main_window.chat_list.lists.get(i).get_from(), 
										main_window.chat_list.lists.get(i).get_title(), 
										main_window.chat_list.lists.get(i).get_text(),
										main_window.chat_list.lists.get(i).get_receiver_or_sender() ));
		}
	}
	
	private void set_scroll_pane() {
		scroll_pane = new JScrollPane(list_panel);
		scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		scroll_pane.setBackground(Color.white);
		scroll_pane.setBorder(new EmptyBorder(10, 20, 20, 20));
	}
}
