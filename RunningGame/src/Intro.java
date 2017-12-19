import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Intro extends JFrame {
	JFrame intro;
	JButton button1;
	JButton button2;
	Image normal_img;
	
	public Intro() {
		normal_img = new ImageIcon("background.png").getImage();
		
		this.setTitle(" !~ Run & Run ~! ");
		this.setLayout(null);
		button1 = new JButton(" [ 시 작 ] ");
		button2 = new JButton(" [ 종 료 ] ");
		this.setBounds(500, 100, 915, 650);
		button1.setBounds(350, 200, 200, 100);
		button2.setBounds(350, 350, 200, 100);
		button1.addActionListener(new MyActionListener());
		button2.addActionListener(new MyActionListener());
		add(button1);
		add(button2);
		
		
		
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			try {
				if(ae.getSource() == button1) {
					if(button1.getText().equals(" [ 시 작 ] ")) {
						dispose();
						Game gg = new Game();
						gg.setVisible(true);
					}
				}
				else if(ae.getSource() == button2) {
					System.exit(1);
				}
					
			} catch(Exception e) { }
			
		}
	}
	
	public void paint(Graphics g) { 
		g.drawImage(normal_img, 0, 0, 915, 650, this);
	}


}
