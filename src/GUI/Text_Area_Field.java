package GUI;

import MainPackage.Chat_List;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.*;

/*
 남은 해야 할 일 목록 
 1.<인덱스 번호(int), 사용자명(String), 대화내용(String)> 리스트 
 2. 위 리스트를 바탕으로 Text_Area_Field에 내용 넣기 
 3. 내용을 업데이트 하라는 명령을 내리면 해당 내용 업데이트 
 */

public class Text_Area_Field extends JPanel{

	private JTextArea text_area;			// 대화 내용 창 
	public JScrollPane scroll_text_area;	// 대화 내용 창 (스크롤이 가능) 
	
	
	public Text_Area_Field() {
		// 이 클래스의 배치 방법과 여백 설정
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(20, 20, 10, 20));
		
		init_all_elements();	// 클래스 내 요소(대화 내용 창)의 정보들 초기
		add_all_elements();		// 클래스 내에 요소(대화 내용 창)들을 넣기 
	}

	private void init_all_elements() {
		text_area = new JTextArea(15, 20);
		scroll_text_area = new JScrollPane(text_area);
	}
	
	private void add_all_elements() {
		this.add(scroll_text_area, BorderLayout.CENTER);
	}
}
