package GUI;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.border.*;

public class Text_Area_Field extends JPanel{

	public JTextArea text_area;
	public JScrollPane scroll_text_area;
	
	public Text_Area_Field() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(20, 20, 10, 20));
		
		init_all_elements();
		add_all_elements();
	}

	private void init_all_elements() {
		text_area = new JTextArea(15, 20);
		scroll_text_area = new JScrollPane(text_area);
		
		//input_field = new JTextField(15);
	}
	
	private void add_all_elements() {
		this.add(scroll_text_area, BorderLayout.CENTER);
		//this.add(input_field, BorderLayout.SOUTH);
	}
}
