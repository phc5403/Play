import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

class Game extends JFrame implements Runnable {  
	int f_Width ; // 프레임의 가로 크기
	int f_Height ; // 프레임의 세로 크기
	int x, y; 
	int bx; // 전체 배경 가로 스크롤용 변수
	int cnt; // 눈사람을 추가 생산하기 위한 변수 
	int index; // 플레이어 배열의 인덱스 값
	int snow_speed; // 눈사람의 이동 속도
	int game_Score; // 게임 점수 계산
	int distance; // 달려온 거리
	boolean check = false; // 충돌 판정을 위한 변수
	int pX, pY; // 플레이어의 (x,y)
	
	Thread th; // 스레드 사용
	
	Image[] Player_img; //플레이어 애니메이션 표현을 위해 이미지를 배열로 받음
	Image BackGround_img; // 배경화면 이미지
	Image Snow_img; // 눈사람 이미지
	Image Ground_img; // 지형 이미지
	
	Image bufferImage; // 이미지 클래스의 객체 생성 (더블 버퍼링)
	Graphics buffer;   // 그래픽스 클래스의 객체 생성 (더블 버퍼링)
	
	ArrayList Snow_List = new ArrayList(); // 눈사람을 ArrayList에 저장
	
	Snow sn; // 눈사람 클래스의 객체 생성 
	
	public Game() {  // 생성자
		init(); // 변수나 이미지 값 초기화한 것들을 불러온다.
		start(); // 스레드 시작
  
		setTitle(" !~ Run & Run ~! "); // 프레임의 제목
		int f_xpos = 500;
		int f_ypos = 100;

		setBounds(f_xpos, f_ypos, f_Width, f_Height); 
		// 모니터의 f_xpos, f_ypos 위치에 f_Width, f_Height 크기의 스크린 설정 
		setResizable(false); // 사용자가 임의로 스크린의 사이즈 조절을 못하게 함. 
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void init() { // 각종 변수나 이미지 초기화 메소드
//		x = 100;
//		y = 100; // 
		f_Width = 915; // 프레임의 가로 크기
		f_Height = 650; // 프레임의 세로 크기

		Snow_img = new ImageIcon("SnowMan.png").getImage();
		//이미지 만드는 방식을 ImageIcon으로 변경.

		Player_img = new Image[10];  // 애니메이션용 파일 저장을 위해 Image 배열 생성
		
		for(int i=0; i <Player_img.length; i++) // 이미지 배열에 애니메이션용 파일들을 저장
		{
			Player_img[i] = new ImageIcon("run " + i + ".png").getImage(); // 이미지 크기 : 512 x 256
		}
		
		BackGround_img = new ImageIcon("background.png").getImage(); 
		// 전체 배경화면 이미지를 불러온다. 이미지 크기 : 3000 x 500
		
		Ground_img = new ImageIcon("Ground.png").getImage();
		// 전체 배경화면 아래 지형 이미지를 불러온다. 이미지 크기 : 3000 x 376
		
		bx = 0;
		pX = 30;
		pY = 280;
		index = 0; 
		distance = 0;
		game_Score = 0; // 게임 스코어 초기화
		snow_speed = 10; // 눈사람이 이동하는 속도 설정
	}
	
	public void start() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		th = new Thread(this); 
		th.start(); 
	}
	
	public void run() {  
		try{
			while(true) {
				SnowProcess(); // 스레드로 SnowProcess() 실행
				Thread.sleep(50); 
				repaint();
				 
				cnt++; // 눈사람을 추가 생산하는 조건을 만들기 위해 스레드가 돌때마다 값 증가
				distance++; // 화면에 나오는 거리 값을 증가시킨다 
				index++; // index 값을 증가시켜서 캐릭터 이미지 배열안에 이미지를 하나씩 보여주게한다.
				
				if(index >= Player_img.length) // index 값이 배열의 크기를 넘으면
					index = 0; // 다시 0으로 초기화함
				if(distance == 1000) {
					dispose();
				}
			}
		} catch (Exception e) { }
	}
	
	public void SnowProcess() {   
		for (int i = 0 ; i < Snow_List.size() ; ++i ) { 
			sn = (Snow)(Snow_List.get(i)); // List에서 눈사람 생성 
			sn.move(); // 눈사람이 움직이도록 move() 호출
			if(sn.x < -100) { 
				Snow_List.remove(i); // 눈사람의 x좌표가 화면을 벗어나 -100이 되면 삭제 
			}
			
			if(Crash(pX, pY, sn.x, sn.y, Player_img[0], Snow_img)) {
				// 플레이어와 눈사람의 충돌을 판정하여 boolean값을 리턴 받아 true면 아래를 실행. 

				Snow_List.remove(i); // 적을 제거합니다.
				game_Score += 100; // 점수를 증가시킴 
			}
		}
		
		if ( cnt % 200 == 0 ) { // cnt가 200이 될 때마다  해당 좌표에 눈사람을 생성
			sn = new Snow(f_Width + 100, 400, snow_speed); // 눈사람을 생성 할 때 속도를 추가로 받음
			Snow_List.add(sn);
			sn = new Snow(f_Width + 100, 450, snow_speed);
			Snow_List.add(sn);
		}
	}
	
	public void paint(Graphics g) { 
		bufferImage = createImage(f_Width, f_Height); // buffImgae에 size(f_Width, f_Height)의 Image 버퍼 생성
		buffer = bufferImage.getGraphics(); // bufferImage의 Graphics 객체를 얻어와 buffer에 저장
		update(g);
	}
	
	public void update(Graphics g) { // update(Graphics g)는 화면을 지우고 paint(Graphics g)를 호출함.
		Draw_Background(); // 배경 이미지 그리기 
		Draw_Player(); // 플레이어를 그리기
		Draw_Snow(); // 눈사람 그리기
		Draw_StatusText(); // 상태 표시 텍스트를 그리는 
		g.drawImage(bufferImage, 0, 0, this); // bufferImage를 그림
	}
	
	public void Draw_Background() {
		buffer.clearRect(0, 0, f_Width, f_Height); // 프레임의 크기만큼 화면을 지운다
		if ( bx > - 2000){ // bx좌표가 -2000 보다 커지면
			buffer.drawImage(BackGround_img, bx, 0, this); // buffImgae의 객체가 저장된 buffer를 사용해서 (bx, 0) 좌표에 이미지를 그린다.
			buffer.drawImage(Ground_img, bx, 500, this); // 지형도 같은 방식으로 그린다
			bx -= 1; // bx를 0에서 -1만큼 계속 줄이므로 배경이미지의 x좌표는 계속 좌측으로 이동한다. 따라서 전체 배경은 천천히 좌측으로 움직이게 된다.
		}
		else {
			bx = 0; 
		}
	}
	
	public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2) { // 충돌을 판단하는 메소드
		System.out.println(String.valueOf(x1+img1.getWidth(null)/2));
		System.out.println(String.valueOf(x2));
		System.out.println(y1);
		System.out.println(y2);
		//이미지의 넓이, 높이 값을 바로 받아 계산.
		if ( Math.abs( ( x1 + img1.getWidth(null) /2 -100) - ( x2 + img2.getWidth(null)/2-100))
			< ( img2.getWidth(null)/2-100 + img1.getWidth(null)/2 -100) && Math.abs( ( y1 + img1.getHeight(null)/2-30 )  
			- ( y2 + img2.getHeight(null)/2-30)) < ( img2.getHeight(null)/2-30 + img1.getHeight(null)/2 -30) ) {
			// 캐릭터 이미지의 실제 그려진 부분은 파일 크기보다 더 작으므로 상수 값을 추가로 빼주어 충돌되는 위치를 맞춰줌.
//		if(x1+img1.getWidth(null)/ 2+50 >x2) {	
		check = true;
			//위 값이 true면 check에 true를 전달.
		}
		else { 
			check = false; // 아닐 경우 false를 전달.
		}
		return check; // check 값을 반환 해줌.
	}
	
	public void Draw_Snow() { // 눈사람을 그리는 메소드
		for (int i = 0 ; i < Snow_List.size() ; ++i ) {
			sn = (Snow)(Snow_List.get(i));
			buffer.drawImage(Snow_img, sn.x, sn.y, this);
		}
	}
	
	public void Draw_Player() { // 캐릭터를 그리는 메소드
		buffer.drawImage(Player_img[index], pX, pY, this);
		repaint();
	}
	
	public void Draw_StatusText() { // 상태 체크용  텍스트를 그리는 메소드.
		buffer.setFont(new Font("Defualt", Font.BOLD, 20));
		buffer.setColor(Color.GREEN);
		// 폰트 설정 값 = 기본폰트, 굵게, 사이즈 20, 초록색
		buffer.drawString("점 수 : " + game_Score, 600, 70);
		// (600, 70)에 점수를 표시

		buffer.drawString("달려온 거리 : " + distance + "m", 600, 90);
		
//		buffer.drawString("화면에 나타난 동전의 개수 : " + Snow_List.size(), 600, 110); 
		// 눈사람이 화면에 나타난 개수를 출력
	}
	} // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡGame 클래스

class Snow { 
	int x;
	int y;
	int speed; // 눈사람의 추가 이동 속도
	Snow(int x, int y, int speed ) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		// 눈사람의 객체 생성시 속도 값을 추가로 받음.
	}
	
	public void move() { 
		x -= speed; // 이동속도를 추가해서 받음
	}
}

