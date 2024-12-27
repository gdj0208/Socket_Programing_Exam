package MainPackage;

import java.util.ArrayList;

public class Chat_List {
	public class ChatMassage {
		private int index;
		private String from;
		private String to;
		private String title;
		private String message;
		private boolean is_receiver;
		
		public ChatMassage(String from, String to, String title, String message, boolean is_receiver) {
			this.from = from;
			this.to = to;
			this.title = title;
			this.message = message;
			this.is_receiver = is_receiver;
		}
		
		public int get_index() { return this.index; }
		public String get_from() { return this.from; }
		public String get_to() { return this.to; }
		public String get_title() { return this.title; }
		public String get_text() { return this.message; }
		public boolean get_receiver_or_sender() { return this.is_receiver; }
	}
	
	public ArrayList<ChatMassage> lists = new ArrayList<ChatMassage>();
	
	public void add_new_massage(String from, String to, String title, String message, boolean is_receiver) {
		lists.add(new ChatMassage(from, to, title, message, is_receiver));
	}
	
	public void clear() {
		lists.clear();
	}
	
	public void add_basic_List() {
		 lists.add(new ChatMassage("from_1", "to_1", "title_1", "lalalalalala", true));
		 lists.add(new ChatMassage("from_2", "to_2", "title_2", "lalalalalala", true));
		 lists.add(new ChatMassage("from_3", "to_3", "title_3", "lalalalalala", true));
		 lists.add(new ChatMassage("from_4", "to_4", "title_4", "lalalalalala", true));
		 lists.add(new ChatMassage("from_5", "to_5", "title_5", "lalalalalala", true));
		 lists.add(new ChatMassage("from_6", "to_6", "title_6", "lalalalalala", true));
		 lists.add(new ChatMassage("from_7", "to_7", "title_7", "lalalalalala", true));
		 lists.add(new ChatMassage("from_8", "to_8", "title_8", "lalalalalala", true));
		 lists.add(new ChatMassage("from_9", "to_9", "title_9", "lalalalalala", true));
		/*
		 */
		
	}
}
