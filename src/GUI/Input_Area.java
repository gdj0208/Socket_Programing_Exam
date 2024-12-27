package GUI;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class Input_Area extends JPanel {
	public JTextField Input_Field;		// 입력 창 
	public JButton Send_Button;			// 보내기 버튼
	
	public Input_Area() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(10, 20, 20, 20));

		init_all_elements();	// 클래스 내 요소(입력 창과 보내기 버튼)의 정보들 초기
		add_all_elements();		// 클래스 내에 요소(입력 창과 보내기 버튼)들을 넣기 
	}

	private void init_all_elements() {
		Input_Field = new JTextField(15);
		Send_Button = new JButton("Send");
	}
	
	private void add_all_elements() {
		this.add(Input_Field, BorderLayout.CENTER);
		this.add(Send_Button, BorderLayout.EAST);
	}
}
