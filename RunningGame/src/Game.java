import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

class Game extends JFrame implements Runnable {  
	int f_Width ; // �������� ���� ũ��
	int f_Height ; // �������� ���� ũ��
	int x, y; 
	int bx; // ��ü ��� ���� ��ũ�ѿ� ����
	int cnt; // ������� �߰� �����ϱ� ���� ���� 
	int index; // �÷��̾� �迭�� �ε��� ��
	int snow_speed; // ������� �̵� �ӵ�
	int game_Score; // ���� ���� ���
	int distance; // �޷��� �Ÿ�
	boolean check = false; // �浹 ������ ���� ����
	int pX, pY; // �÷��̾��� (x,y)
	
	Thread th; // ������ ���
	
	Image[] Player_img; //�÷��̾� �ִϸ��̼� ǥ���� ���� �̹����� �迭�� ����
	Image BackGround_img; // ���ȭ�� �̹���
	Image Snow_img; // ����� �̹���
	Image Ground_img; // ���� �̹���
	
	Image bufferImage; // �̹��� Ŭ������ ��ü ���� (���� ���۸�)
	Graphics buffer;   // �׷��Ƚ� Ŭ������ ��ü ���� (���� ���۸�)
	
	ArrayList Snow_List = new ArrayList(); // ������� ArrayList�� ����
	
	Snow sn; // ����� Ŭ������ ��ü ���� 
	
	public Game() {  // ������
		init(); // ������ �̹��� �� �ʱ�ȭ�� �͵��� �ҷ��´�.
		start(); // ������ ����
  
		setTitle(" !~ Run & Run ~! "); // �������� ����
		int f_xpos = 500;
		int f_ypos = 100;

		setBounds(f_xpos, f_ypos, f_Width, f_Height); 
		// ������� f_xpos, f_ypos ��ġ�� f_Width, f_Height ũ���� ��ũ�� ���� 
		setResizable(false); // ����ڰ� ���Ƿ� ��ũ���� ������ ������ ���ϰ� ��. 
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void init() { // ���� ������ �̹��� �ʱ�ȭ �޼ҵ�
//		x = 100;
//		y = 100; // 
		f_Width = 915; // �������� ���� ũ��
		f_Height = 650; // �������� ���� ũ��

		Snow_img = new ImageIcon("SnowMan.png").getImage();
		//�̹��� ����� ����� ImageIcon���� ����.

		Player_img = new Image[10];  // �ִϸ��̼ǿ� ���� ������ ���� Image �迭 ����
		
		for(int i=0; i <Player_img.length; i++) // �̹��� �迭�� �ִϸ��̼ǿ� ���ϵ��� ����
		{
			Player_img[i] = new ImageIcon("run " + i + ".png").getImage(); // �̹��� ũ�� : 512 x 256
		}
		
		BackGround_img = new ImageIcon("background.png").getImage(); 
		// ��ü ���ȭ�� �̹����� �ҷ��´�. �̹��� ũ�� : 3000 x 500
		
		Ground_img = new ImageIcon("Ground.png").getImage();
		// ��ü ���ȭ�� �Ʒ� ���� �̹����� �ҷ��´�. �̹��� ũ�� : 3000 x 376
		
		bx = 0;
		pX = 30;
		pY = 280;
		index = 0; 
		distance = 0;
		game_Score = 0; // ���� ���ھ� �ʱ�ȭ
		snow_speed = 10; // ������� �̵��ϴ� �ӵ� ����
	}
	
	public void start() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		th = new Thread(this); 
		th.start(); 
	}
	
	public void run() {  
		try{
			while(true) {
				SnowProcess(); // ������� SnowProcess() ����
				Thread.sleep(50); 
				repaint();
				 
				cnt++; // ������� �߰� �����ϴ� ������ ����� ���� �����尡 �������� �� ����
				distance++; // ȭ�鿡 ������ �Ÿ� ���� ������Ų�� 
				index++; // index ���� �������Ѽ� ĳ���� �̹��� �迭�ȿ� �̹����� �ϳ��� �����ְ��Ѵ�.
				
				if(index >= Player_img.length) // index ���� �迭�� ũ�⸦ ������
					index = 0; // �ٽ� 0���� �ʱ�ȭ��
				if(distance == 1000) {
					dispose();
				}
			}
		} catch (Exception e) { }
	}
	
	public void SnowProcess() {   
		for (int i = 0 ; i < Snow_List.size() ; ++i ) { 
			sn = (Snow)(Snow_List.get(i)); // List���� ����� ���� 
			sn.move(); // ������� �����̵��� move() ȣ��
			if(sn.x < -100) { 
				Snow_List.remove(i); // ������� x��ǥ�� ȭ���� ��� -100�� �Ǹ� ���� 
			}
			
			if(Crash(pX, pY, sn.x, sn.y, Player_img[0], Snow_img)) {
				// �÷��̾�� ������� �浹�� �����Ͽ� boolean���� ���� �޾� true�� �Ʒ��� ����. 

				Snow_List.remove(i); // ���� �����մϴ�.
				game_Score += 100; // ������ ������Ŵ 
			}
		}
		
		if ( cnt % 200 == 0 ) { // cnt�� 200�� �� ������  �ش� ��ǥ�� ������� ����
			sn = new Snow(f_Width + 100, 400, snow_speed); // ������� ���� �� �� �ӵ��� �߰��� ����
			Snow_List.add(sn);
			sn = new Snow(f_Width + 100, 450, snow_speed);
			Snow_List.add(sn);
		}
	}
	
	public void paint(Graphics g) { 
		bufferImage = createImage(f_Width, f_Height); // buffImgae�� size(f_Width, f_Height)�� Image ���� ����
		buffer = bufferImage.getGraphics(); // bufferImage�� Graphics ��ü�� ���� buffer�� ����
		update(g);
	}
	
	public void update(Graphics g) { // update(Graphics g)�� ȭ���� ����� paint(Graphics g)�� ȣ����.
		Draw_Background(); // ��� �̹��� �׸��� 
		Draw_Player(); // �÷��̾ �׸���
		Draw_Snow(); // ����� �׸���
		Draw_StatusText(); // ���� ǥ�� �ؽ�Ʈ�� �׸��� 
		g.drawImage(bufferImage, 0, 0, this); // bufferImage�� �׸�
	}
	
	public void Draw_Background() {
		buffer.clearRect(0, 0, f_Width, f_Height); // �������� ũ�⸸ŭ ȭ���� �����
		if ( bx > - 2000){ // bx��ǥ�� -2000 ���� Ŀ����
			buffer.drawImage(BackGround_img, bx, 0, this); // buffImgae�� ��ü�� ����� buffer�� ����ؼ� (bx, 0) ��ǥ�� �̹����� �׸���.
			buffer.drawImage(Ground_img, bx, 500, this); // ������ ���� ������� �׸���
			bx -= 1; // bx�� 0���� -1��ŭ ��� ���̹Ƿ� ����̹����� x��ǥ�� ��� �������� �̵��Ѵ�. ���� ��ü ����� õõ�� �������� �����̰� �ȴ�.
		}
		else {
			bx = 0; 
		}
	}
	
	public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2) { // �浹�� �Ǵ��ϴ� �޼ҵ�
		System.out.println(String.valueOf(x1+img1.getWidth(null)/2));
		System.out.println(String.valueOf(x2));
		System.out.println(y1);
		System.out.println(y2);
		//�̹����� ����, ���� ���� �ٷ� �޾� ���.
		if ( Math.abs( ( x1 + img1.getWidth(null) /2 -100) - ( x2 + img2.getWidth(null)/2-100))
			< ( img2.getWidth(null)/2-100 + img1.getWidth(null)/2 -100) && Math.abs( ( y1 + img1.getHeight(null)/2-30 )  
			- ( y2 + img2.getHeight(null)/2-30)) < ( img2.getHeight(null)/2-30 + img1.getHeight(null)/2 -30) ) {
			// ĳ���� �̹����� ���� �׷��� �κ��� ���� ũ�⺸�� �� �����Ƿ� ��� ���� �߰��� ���־� �浹�Ǵ� ��ġ�� ������.
//		if(x1+img1.getWidth(null)/ 2+50 >x2) {	
		check = true;
			//�� ���� true�� check�� true�� ����.
		}
		else { 
			check = false; // �ƴ� ��� false�� ����.
		}
		return check; // check ���� ��ȯ ����.
	}
	
	public void Draw_Snow() { // ������� �׸��� �޼ҵ�
		for (int i = 0 ; i < Snow_List.size() ; ++i ) {
			sn = (Snow)(Snow_List.get(i));
			buffer.drawImage(Snow_img, sn.x, sn.y, this);
		}
	}
	
	public void Draw_Player() { // ĳ���͸� �׸��� �޼ҵ�
		buffer.drawImage(Player_img[index], pX, pY, this);
		repaint();
	}
	
	public void Draw_StatusText() { // ���� üũ��  �ؽ�Ʈ�� �׸��� �޼ҵ�.
		buffer.setFont(new Font("Defualt", Font.BOLD, 20));
		buffer.setColor(Color.GREEN);
		// ��Ʈ ���� �� = �⺻��Ʈ, ����, ������ 20, �ʷϻ�
		buffer.drawString("�� �� : " + game_Score, 600, 70);
		// (600, 70)�� ������ ǥ��

		buffer.drawString("�޷��� �Ÿ� : " + distance + "m", 600, 90);
		
//		buffer.drawString("ȭ�鿡 ��Ÿ�� ������ ���� : " + Snow_List.size(), 600, 110); 
		// ������� ȭ�鿡 ��Ÿ�� ������ ���
	}
	} // �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�Game Ŭ����

class Snow { 
	int x;
	int y;
	int speed; // ������� �߰� �̵� �ӵ�
	Snow(int x, int y, int speed ) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		// ������� ��ü ������ �ӵ� ���� �߰��� ����.
	}
	
	public void move() { 
		x -= speed; // �̵��ӵ��� �߰��ؼ� ����
	}
}

