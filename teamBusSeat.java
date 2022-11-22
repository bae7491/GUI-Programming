package team;

import java.awt.*;
import javax.swing.*;

class loginView extends JFrame {
	private static final long serialVersionUID = 1L;

// 로그인 창 뷰 클래스.
	Container c = getContentPane();

	JPanel[] panel = new JPanel[3]; // panel[0] = 아이디 입력 공간. / panel[1] = 비밀번호 입력 공간. / panel[2] = 로그인, 회원가입 버튼 공간.
	JPanel[] pPanel = new JPanel[2]; // pPanel[0] = 아이디 공간. / pPanel[1] = 비밀번호 공간.
	JLabel[] la = new JLabel[2]; // 아이디, 비밀번호 Label.
	JTextField[] tf = new JTextField[2]; // 아이디, 비밀번호 TextField.
	JButton[] btn = new JButton[2]; // 로그인, 회원가입 Button.

	loginView() {
		setTitle("버스 좌석 예매");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(280, 150);

		newComponents(); // 클래스를 구성할 멤버들을 설정하는 메소드.
		setComponents(); // 멤버등릐 추가 설정을 추가하는 메소드.
		addComponents(); // 구성된 멤버들을 Panel에 부착하는 메소드.

		setVisible(true);
	}

	void newComponents() { // 클래스를 구성할 멤버들을 설정하는 메소드.
		String[] laString = { "ID", "PW" }; // Label에 들어갈 텍스트.
		String[] btnString = { "로그인", "회원가입" }; // Button에 들어갈 텍스트.

		for (int i = 0; i < panel.length; i++) {
			panel[i] = new JPanel(); // Panel 3개 생성.

			if (i < 2) { // Panel을 제외한 나머지는 2개이므로,
				pPanel[i] = new JPanel(new FlowLayout()); // pPanel 2개 생성.
				la[i] = new JLabel(laString[i]); // Label 2개 생성. / Label 안에 텍스트 추가.
				tf[i] = new JTextField(20); // TextField 2개 생성. / 길이를 25로 지정.
				btn[i] = new JButton(btnString[i]); // Button 2개 생성. / Button 안에 텍스트 추가.
			}		
		}
	}

	void setComponents() { // 멤버등릐 추가 설정을 추가하는 메소드.
		c.setLayout(new GridLayout(3, 1)); // c를 GridLayout으로 3행 1열로 설정.
		
		for (int i = 0; i < pPanel.length; i++) { // pPanel.length = 2.
			pPanel[i].setLayout(new FlowLayout(FlowLayout.CENTER));
		}
		
		for (int i = 0; i < la.length; i++) { // la.length = 2.
			la[i].setHorizontalAlignment(JLabel.CENTER); // Label의 텍스트 위치를 중앙으로 위치하게 함.
		}
	}

	void addComponents() { // 구성된 멤버들을 Panel에 부착하는 메소드.
		for (int i = 0; i < pPanel.length; i++) { // pPanel.length = 2.
			pPanel[i].add(la[i]); // pPanel에 Label 추가.
			pPanel[i].add(tf[i]); // pPanel에 TextField 추가.

			panel[i].add(pPanel[i]); // 위에 입력한 정보등를 Panel에 추가.
		}

		for (int i = 0; i < btn.length; i++) { // btn.length = 2.
			panel[2].add(btn[i]); // Panel[2]에 로그인, 회원가입 Button 추가.
		}

		for (int i = 0; i < panel.length; i++) { // panel.length = 3.
			c.add(panel[i]); // Label, TextField, Button이 있는 Panel을 c에 부착.
		}
	}

}

public class teamBusSeat {
	loginView l;

	public teamBusSeat() {
		l = new loginView();
	}

	public static void main(String[] args) {
		new teamBusSeat();
	}

}
