package GUI;

import java.io.*;
import javax.swing.*;
import java.awt.*;

/*
 모든 UI 관련 클래스들은 GUI 패키지에 다 들어가 있습니다.
 GUI클래스의 UI는 다음과 같습니다.
 +-----------------------+
 |		Main_Frame		 |	
 |+---------------------+|	
 ||	 Text_Area_Field   	||	
 ||(대화 내역을 표시하는 공간)	||
 ||						||	
 ||						||	600
 ||						||
 ||						||
 |+---------------------+|
 |+---------------------+|		+----------------+------+
 ||		Input_Area	----++---->	|	Input_Field	 |button|
 |+---------------------+|		+----------------+------+
 +-----------------------+
 			400
 
 1. 메인 프레임 내부에는 Text_Area_Field와 Input_Area가 있습니다.
 2. Text_Area_Field는 대화 내용들을 보여줍니다. 
 	카카오톡에서 대화 내용을 보여주는 창에 해당됩니다.
 3. Input_Area는 보낼 내용을 포함하는 Text_Field와 Text_Filed에 적어놓은 내용들을 보내는 Button으로 구성되어있습니다.
 */

public class Main_Frame extends JFrame{
	
	private Text_Area_Field show_field = new Text_Area_Field();		// 대화 내용을 보여주는 창 
	private Input_Area input_field = new Input_Area();				// 보내고 싶은 대화를 보내는 창 
	
	private int width = 400;	// 프레임 넓이 
	private int height = 600;	// 프레임 높이 

	public static void main() {
		SwingUtilities.invokeLater(() -> { new Main_Frame(); });	// Main_Frame 생성 및 표기 
	}
	
	// 프레임 생성
	// main 클래스에서 이 Main_Frame을 호출하는 것 만으로도 화면은 생성됩니다.
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
