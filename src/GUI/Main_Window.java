package GUI;

import java.io.*;
import javax.swing.*;

import MainPackage.*;
import smtp.MainProcessor;

import java.awt.*;

/*
 */

public class Main_Window extends JFrame{
	private int width = 400;	// 프레임 넓이 
	private int height = 600;	// 프레임 높이 
	public static boolean logged_in = false;	// 로그인 여부
	
	public String email;
	public static String password;

	
	Chat_List chat_list;		
	private Personal_Info_Field peron_info_field = new Personal_Info_Field(this);		
	private Mail_List_Feild mail_list_field = new Mail_List_Feild(this);	
	public smtp.MainProcessor main_processor;			
	
	//public static void main() {
		// SwingUtilities.invokeLater(() -> { new Main_Window(); });	// Main_Frame 생성 및 표기 
	//}
	
	// 프레임 생성
	// main 클래스에서 이 Main_Frame을 호출하는 것 만으로도 화면은 생성됩니다.
	public Main_Window(MainProcessor main_processor, Chat_List chat_list) {
		
		this.main_processor = main_processor;
		this.chat_list = chat_list;
		
		// 제목 설정 
		setTitle("Socket_Programing_Project");				
		
		// 화면 사이즈 설정 
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		this.add(peron_info_field, BorderLayout.NORTH);
		this.add(mail_list_field.scroll_pane, BorderLayout.CENTER);
		
		// 프레임 보이게 하기 
		setVisible(true);									
	}
	
	public void log_in_out_update(boolean is_log_in) {
		logged_in = is_log_in;
		
		chat_list.clear();
		
		peron_info_field.update_panel();
		mail_list_field.update_mail_list();
	}
}
