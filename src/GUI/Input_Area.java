package GUI;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Input_Area extends JPanel {
	public JTextField Input_Field;
	public JButton Send_Button;
	
	public Input_Area() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(10, 20, 20, 20));
		
		init_all_elements();
		add_all_elements();
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
