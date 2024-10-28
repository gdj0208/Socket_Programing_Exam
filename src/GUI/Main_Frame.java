package GUI;

import java.io.*;
import javax.swing.*;
import java.awt.*;


public class Main_Frame extends JFrame{
	
	private Text_Area_Field show_field = new Text_Area_Field();
	private Input_Area input_field = new Input_Area();
	
	private int width = 400;	// 프레임 넓이 
	private int height = 600;	// 프레임 높이 

	public static void main() {
		// Main_Frame 생성 및 표기 
		SwingUtilities.invokeLater(() -> { new Main_Frame(); });
	}
	
	// 프레임 생성
	public Main_Frame() {
		
		// 제목 설정 
		setTitle("Socket_Programing_Project");				
		
		// 화면 사이즈 설정 
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		this.add(show_field, BorderLayout.CENTER);
		this.add(input_field, BorderLayout.SOUTH);
		
		// 프레임 보이게 하기 
		setVisible(true);									
	}
	
}
