package MainPackage;

import GUI.*;
import smtp.*;


public class MainClass {

	// 임시로 만든 메일의 정보들이 들어간 데이터입니다. (차후 데이터의 형태를 알게되면 그에 맞게 수정될 예정입니다.)
	public static Chat_List chat_list = new Chat_List();		
	
	private static smtp.MainProcessor main_processor = new MainProcessor(chat_list);
	
	// GUI 불러오기 (자세한 사항은 Main_Frame.java 참고)
	private static GUI.Main_Window main_window = new Main_Window(main_processor, chat_list);
	
	
	public static void main(String[] args) {
		 System.out.println("MongoDB 연결이 성공적으로 설정되었습니다.");
		main_processor.testing();
	}
}
