package team;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

// 로그인 창 뷰 클래스.
class LoginView extends JFrame {
	private static final long serialVersionUID = 1L;

	TeamBusSeat t = new TeamBusSeat(); // DB가 있는 메인 클래스 호출.

	Container c = getContentPane();
	JPanel[] panel = new JPanel[3]; // panel[0] = 아이디 입력 공간. / panel[1] = 비밀번호 입력 공간. / panel[2] = 로그인, 회원가입 버튼 공간.
	JPanel[] pPanel = new JPanel[2]; // pPanel[0] = 아이디 공간. / pPanel[1] = 비밀번호 공간.
	JLabel[] la = new JLabel[2]; // 아이디, 비밀번호 Label.
	JTextField[] tf = new JTextField[2]; // 아이디, 비밀번호 TextField.
	JButton[] btn = new JButton[2]; // 로그인, 회원가입 Button.

	LoginView() { // 로그인 창 생성자.
		setTitle("버스 좌석 예매");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(280, 150);

		newComponents(); // 클래스를 구성할 멤버들을 설정하는 메소드.
		setComponents(); // 멤버들의 추가 설정을 추가하는 메소드.
		addComponents(); // 구성된 멤버들을 Panel에 부착하는 메소드.

		setVisible(true);
	}

	void newComponents() { // 클래스를 구성할 멤버들을 설정하는 메소드.
		String[] laString = { " ID   ", "PW  " }; // Label에 들어갈 텍스트.
		String[] btnString = { "로그인", "회원가입" }; // Button에 들어갈 텍스트.

		for (int i = 0; i < panel.length; i++) {
			panel[i] = new JPanel(new FlowLayout()); // Panel 3개 생성.

			if (i < 2) { // Panel을 제외한 나머지는 2개이므로,
				pPanel[i] = new JPanel(new FlowLayout(10)); // pPanel 2개 생성.
				la[i] = new JLabel(laString[i]); // Label 2개 생성. / Label 안에 텍스트 추가.
				tf[i] = new JTextField(15); // TextField 2개 생성. / 길이를 25로 지정.
				btn[i] = new JButton(btnString[i]); // Button 2개 생성. / Button 안에 텍스트 추가.
			}
		}
	}

	void setComponents() { // 멤버들의 추가 설정을 추가하는 메소드.
		c.setLayout(new GridLayout(3, 1)); // c를 GridLayout으로 3행 1열로 설정.

		for (int i = 0; i < pPanel.length; i++) { // pPanel.length = 2.
			pPanel[i].setLayout(new FlowLayout(FlowLayout.CENTER));
		}

		for (int i = 0; i < la.length; i++) { // la.length = 2.
			la[i].setHorizontalAlignment(JLabel.CENTER); // Label의 텍스트 위치를 중앙에 위치하게 함.
		}

		for (int i = 0; i < tf.length; i++) { // ActionListener 추가.
			tf[i].addActionListener(new LoginAction()); // TextField에 ActionListener 추가.
			if (i < 2) // Button의 개수가 2개이므로,
				btn[i].addActionListener(new LoginAction()); // Button에 ActionListener 추가.
		}

		setResizable(false); // container의 크기 변경 막기.
		setLocationRelativeTo(null); // container를 중앙에 배치.
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

	class LoginAction implements ActionListener { // TextField, Button에 추가할 ActionListener.

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == tf[0]) { // TextField[0]에 ID 입력 후 엔터키 입력 시,
				tf[1].requestFocus(); // TextField[1]로 포커스 변경.
			} else if (e.getSource() == btn[0]) { // btn[0] (로그인 버튼) 클릭 시,
				if (tf[0].getText().equals("") || tf[1].getText().equals("")) { // 값이 비었다면,
					JOptionPane.showMessageDialog(null, "입력하지않은 값이 있습니다!", "로그인 오류", JOptionPane.ERROR_MESSAGE); // 빈 값이 존재.
				} else { // 값이 있으면, 로그인 성공.
					successLogin();
				}
			} else if (e.getSource() == btn[1]) { // btn[1] (회원가입 버튼) 클릭 시,
				new SignUpView(); // SignUpView 클래스에서 회원가입 창 생성 메소드 (생성자) 불러오기.
			}
		}

	}

	void successLogin() { // 로그인 성공 시, 유저, 관리자 구분.
		if (tf[0].getText().equals("ADMIN")) {
			adminLogin();
		} else {
			userLogin();
		}
	}

	void userLogin() { // 유저 로그인.
		t.makeConnection();
		String sql = "";
		sql += "SELECT * FROM teambus.member ";

		try {
			sql += "WHERE MID = '" + tf[0].getText() + "'";
			System.out.println(sql);
			t.rs = t.stmt.executeQuery(sql);

			if (t.rs.isBeforeFirst() == false) {
				JOptionPane.showMessageDialog(null, "로그인에 실패했습니다! 값을 제대로 입력해주세요!", "로그인 오류", JOptionPane.ERROR_MESSAGE); // 로그인 실패 창 출력.
			} else {
				while (t.rs.next()) {
					if (tf[1].getText().equals(t.rs.getString("PW"))) {
						JOptionPane.showMessageDialog(null, "로그인 성공!", "로그인 성공", JOptionPane.PLAIN_MESSAGE); // 로그인 성공 창 출력.
						new FirstUserBusView(); // 유저 기본 창으로 이동.

						setVisible(false);
					} else { // 비밀번호가 틀리다면,
						JOptionPane.showMessageDialog(null, "아이디 혹은 비밀번호를 잘못 입력했습니다.\n 입력하신 내용을 다시 확인해주세요.", "로그인 오류",
								JOptionPane.ERROR_MESSAGE); // 로그인 실패 창 출력.
					}
				}
			}
		} catch (SQLException sqle) {
			System.out.println("getData: SQL Error");
			t.disConnection(); // DB 연결 종료 메소드.
		}
	}

	void adminLogin() { // 관리자 로그인.
		if (tf[1].getText().equals("ADMIN")) { // 만약 비밀번호가 ADMIN이 입력된다면,
			JOptionPane.showMessageDialog(null, "관리자 로그인 성공!", "로그인 성공", JOptionPane.PLAIN_MESSAGE); // 로그인 성공 창 출력.
			new FirstAdminBusView(); // 관리자 기본 창으로 이동.

			setVisible(false);
		} else { // 비밀번호가 틀리다면,
			JOptionPane.showMessageDialog(null, "아이디 혹은 비밀번호를 잘못 입력했습니다.\n 입력하신 내용을 다시 확인해주세요.", "로그인 오류", JOptionPane.ERROR_MESSAGE); // 로그인 실패 창 출력.
		}
	}
}

// 회원가입 창 뷰 클래스.
class SignUpView extends JFrame {
	private static final long serialVersionUID = 1L;

	TeamBusSeat t = new TeamBusSeat(); // DB가 있는 메인 클래스 호출.

	Container c = getContentPane();
	JPanel[] panel = new JPanel[2]; // panel[0] : Label, TextField, ComboBox 등 추가. / panel[1] : Button 추가.
	JPanel[] pPanel = new JPanel[7]; // Label, TextField, ComboBox 등을 추가.
	JLabel[] la = new JLabel[7]; // ID, PW, 비밀번호 재확인, 이름, 생년월일, 성별, 전화번호 Label.
	JTextField[] tf = new JTextField[7]; // ID, PW, 비밀번호 재확인, 이름, 전화번호, 생년월일 중 년도, 일 TextField.
	JComboBox<String> monthCombG = new JComboBox<String>(); // 월 ComboBox.
	JRadioButton[] rbtn = new JRadioButton[2]; // 성별 (남, 여) RadioButton.
	ButtonGroup genderGroup = new ButtonGroup(); // RadioButton의 Group 객체.
	JButton[] btn = new JButton[3]; // 중복 확인, 가입, 취소 Button.

	SignUpView() { // 회원가입 창 생성자.
		setTitle("회원가입");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(235, 320);

		newComponents(); // 클래스를 구성할 멤버들을 설정하는 메소드.
		setComponents(); // 멤버들의 추가 설정을 추가하는 메소드.
		addComponents(); // 구성된 멤버들을 Panel에 부착하는 메소드.

		setVisible(true);
	}

	void newComponents() { // 클래스를 구성할 멤버들을 설정하는 메소드.
		String[] laString = { "ID", "PW          ", "재확인    ", "이름        ", "전화번호", "생년월일  ", "성별        " }; // Label에 추가할 문자열.
		String[] btnString = { "중복체크", "가입", "취소" }; // Button에 추가할 문자열.
		String[] monthCombString = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }; // 월 ComboBox에 추가할 문자열.
		String[] rbtnString = { "남", "여" }; // 성별 RadioButton에 추가할 문자열.

		for (int i = 0; i < pPanel.length; i++) { // pPanel.length = 8.
			pPanel[i] = new JPanel(new FlowLayout(10)); // Panel 생성.
			if (i < 7) // Label이 7개이기 때문.
				la[i] = new JLabel(laString[i]); // Label 생성. / Label에 문자열 추가.

			if (i > 0 && i < 5) // 년도를 제외한 5개 TextField이기 때문.
				tf[i] = new JTextField(15); // 년도를 제외한 나머지 TextField 생성. / 길이는 25로 지정.
			else if (i == 0) // ID TextField.
				tf[i] = new JTextField(10); // 길이는 10으로 지정.
			else if (i == 5 || i == 6) // 년도, 일 TextField.
				tf[i] = new JTextField(4); // 길이는 6으로 지정.

			if (i < 3) // Button이 3개이기 때문.
				btn[i] = new JButton(btnString[i]); // Button 생성.
			if (i < 2) { // RadioButton이 2개이기 때문.
				rbtn[i] = new JRadioButton(rbtnString[i]);
				genderGroup.add(rbtn[i]);
			}
		}

		for (int i = 0; i < monthCombString.length; i++) { // 월의 수만큼,
			monthCombG.addItem(monthCombString[i]); // 월 문자열을 ComboBox에 추가.
		}

		panel[0] = new JPanel(new GridLayout(7, 1)); // Panel[0]을 7행 1열의 GridLayout으로 지정.
		panel[1] = new JPanel(new FlowLayout()); // Button 2개를 FlowLayout으로 지정.
	}

	void setComponents() { // 멤버들의 추가 설정을 추가하는 메소드.
		for (int i = 0; i < pPanel.length; i++) { // pPanel.length = 8.
			pPanel[i].setLayout(new FlowLayout(FlowLayout.LEFT)); // pPanel을 왼쪽으로 정렬.
		}

		rbtn[0].setSelected(true); // 성별 RadioButton이 처음에 자동 선택.

		for (int i = 0; i < tf.length; i++) { // tf.length = 7.
			tf[i].addActionListener(new SignUpAction()); // TextField에 ActionListener 추가.
			if (i < 3) { // Button 0, 1, 2.
				btn[i].addActionListener(new SignUpAction()); // Button에 ActionListener 추가.
			}
		}

		// TextField, RadioButton, ComboBox 수정 불가능하게 설정.
		for (int i = 1; i < tf.length; i++) {
			tf[i].setEnabled(false);
		}

		monthCombG.setEnabled(false);

		for (int i = 0; i < rbtn.length; i++) {
			rbtn[i].setEnabled(false);
		}

		setResizable(false); // container의 크기 변경 막기.
		setLocationRelativeTo(null); // container를 중앙에 배치.
	}

	void addComponents() { // 구성된 멤버들을 Panel에 부착하는 메소드.
		for (int i = 0; i < pPanel.length; i++) { // ID, PW, 비밀번호 재확인, 이름, 전화번호의 Label, TextField를 pPanel에 추가.
			if (i == 0) { // ID pPanel.
				pPanel[0].add(la[0]);
				pPanel[0].add(tf[0]);
				pPanel[0].add(btn[0]);
			} else if (i < 5) { // 나머지 Label, TextField를 pPanel에 추가.
				pPanel[i].add(la[i]);
				pPanel[i].add(tf[i]);
			} else if (i == 5) { // 생년월일 pPanel.
				pPanel[5].add(la[5]);
				pPanel[5].add(tf[5]);
				pPanel[5].add(monthCombG);
				pPanel[5].add(tf[6]);
			} else if (i == 6) { // 성별 정보를 pPanel에 추가.
				pPanel[6].add(la[6]);
				for (int j = 0; j < 2; j++)
					pPanel[6].add(rbtn[j]);
			}
			panel[0].add(pPanel[i]); // pPanel들을 panel[0]에 지정.
		}

		for (int i = 0; i < btn.length; i++) { // btn.length = 3.
			panel[1].add(btn[1]); // panel[1]에 가입 Button 추가.
			panel[1].add(btn[2]); // panel[1]에 취소 Button 추가.
		}

		c.add(panel[0], BorderLayout.NORTH); // Label, TextField 등이 들어간 panel[0]을 c에 추가. 
		c.add(panel[1], BorderLayout.SOUTH); // Button이 들어간 panel[1]을 c에 추가.
	}

	class SignUpAction implements ActionListener { // TextField, Button에 추가할 ActionListener.

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btn[0]) { // 중복체크 Button 클릭 시, DB에서 같은 ID가 있는지 체크.
				dupCheck(); // DB에서 중복 체크.
			} else if (e.getSource() == btn[1]) { // 회원가입 Button 클릭 시,
				signUpCheck(); // 회원가입 체크.
			} else if (e.getSource() == btn[2]) { // 취소 Button 클릭 시,
				int isCancel = JOptionPane.showConfirmDialog(null, "회원 가입을 취소하시겠습니까?", null, JOptionPane.PLAIN_MESSAGE); // 취소 다이얼로그.

				if (isCancel == JOptionPane.YES_OPTION) { // YES_OPTION 선택 시,
					setVisible(false); // 창 닫기.
				}
			}
		}

	}

	void dupCheck() { // DB에서 중복 체크.
		t.makeConnection(); // DB 연결 메소드.
		String sql = "";
		sql += "SELECT * FROM teambus.member ";

		try {
			sql += "WHERE MID = '" + tf[0].getText() + "'";
			System.out.println(sql);
			t.rs = t.stmt.executeQuery(sql);

			if (t.rs.isBeforeFirst() == false && !tf[0].getText().equals("")) { // 가장 처음의 데이터가 false (없다면), 공백이 없다면,
				JOptionPane.showMessageDialog(null, "사용가능한 ID입니다!", "ID 중복 체크", JOptionPane.PLAIN_MESSAGE); // 중복 체크 통과.

				// TextField, RadioButton, ComboBox 수정 가능하게 바꿈.
				for (int i = 1; i < tf.length; i++) {
					tf[i].setEnabled(true);
				}
				monthCombG.setEnabled(true);

				for (int i = 0; i < rbtn.length; i++) {
					rbtn[i].setEnabled(true);
				}
			} else if (tf[0].getText().equals("")) { // 빈 값 존재.
				JOptionPane.showMessageDialog(null, "값이 비어있습니다. 값을 입력해주세요!", "ID 중복 체크", JOptionPane.ERROR_MESSAGE); // 에러 메세지 출력.
			} else { // 가장 처음의 데이터가 true (있다면),
				while (t.rs.next()) { // DB에서 행을 반복하면서,
					System.out.println(t.rs.getString("MID"));
					System.out.println(tf[0].getText().toString());

					if (tf[0].getText().equals(t.rs.getString("MID"))) { // 중복하는 ID가 DB에 있으면,
						JOptionPane.showMessageDialog(null, "이미 존재하는 ID입니다!", "ID 중복 체크", JOptionPane.ERROR_MESSAGE); // 중복 체크 실패.
						tf[0].setText("");
					}
				}
			}
		} catch (SQLException sqle) {
			System.out.println("getData: SQL Error");
			t.disConnection(); // DB 연결 종료 메소드.
		}
	}

	void signUpCheck() { // 회원가입 체크.
		if (!tf[1].getText().equals(tf[2].getText())) { // tf[1]과 tf[2]가 다르다면,
			JOptionPane.showMessageDialog(null, "PW가 일치하지 않습니다!", "PW 오류", JOptionPane.ERROR_MESSAGE); // PW 일치하지 않음.
		} else { // 회원가입에 문제가 없다면, (ID 중복 아님, PW 일치.)
			t.makeConnection();
			String sql = "";

			try {
				if (rbtn[0].isSelected() == true) { // 성별에서 남자가 선택됐다면,
					sql += "INSERT INTO teambus.member (MID, PW, name, birth, gender, phone) values ";
					sql += "('" + tf[0].getText() + "', '" + tf[1].getText() + "', '" + tf[3].getText() + "', '" // ID, PW, 이름.
							+ tf[5].getText() + "-" + monthCombG.getSelectedItem() + "-" + tf[6].getText() + "', '남', '" // 생년월일, 성별.
							+ tf[4].getText() + "');"; // 전화번호.
					if (tf[0].getText().equals("") || tf[1].getText().equals("") || tf[3].getText().equals("")
							|| tf[4].getText().equals("") || tf[5].getText().equals("") || tf[6].getText().equals("")) { // 만약 채우지 않은 값이 있다면,
						JOptionPane.showMessageDialog(null, "입력하지 않은 값이 존재합니다! 값을 입력해주세요!", "회원가입 오류",
								JOptionPane.ERROR_MESSAGE);
					} else { // 모든 값이 채워졌다면,
						System.out.println(sql);
						t.stmt.executeUpdate(sql); // sql문을 DB에 추가.

						JOptionPane.showMessageDialog(null, "회원가입 완료했습니다!", "회원가입 완료", JOptionPane.PLAIN_MESSAGE);
						setVisible(false); // 창 종료.
					}
				} else if (rbtn[1].isSelected() == true) { // 성별에서 여자가 선택됐다면,
					sql += "INSERT INTO teambus.member (MID, PW, name, birth, gender, phone) values ";
					sql += "('" + tf[0].getText() + "', '" + tf[1].getText() + "', '" + tf[3].getText() + "', '" // ID, PW, 이름.
							+ tf[5].getText() + "-" + monthCombG.getSelectedItem() + "-" + tf[6].getText() + "', '여', '" // 생년월일, 성별.
							+ tf[4].getText() + "')"; // 전화번호.

					if (tf[0].getText().equals("") || tf[1].getText().equals("") || tf[3].getText().equals("")
							|| tf[4].getText().equals("") || tf[5].getText().equals("") || tf[6].getText().equals("")) { // 만약 채우지 않은 값이 있다면,
						JOptionPane.showMessageDialog(null, "입력하지 않은 값이 존재합니다! 값을 입력해주세요!", "회원가입 오류",
								JOptionPane.ERROR_MESSAGE);
					} else { // 모든 값이 채워졌다면,
						System.out.println(sql);
						t.stmt.executeUpdate(sql); // sql문을 DB에 추가.

						JOptionPane.showMessageDialog(null, "회원가입 완료했습니다!", "회원가입 완료", JOptionPane.PLAIN_MESSAGE);
						setVisible(false); // 창 종료.
					}
				}
			} catch (SQLException sqle) {
				System.out.println("getData: SQL Error");
				t.disConnection(); // DB 연결 종료 메소드.
			}
		}

	}
}

class FirstUserBusView extends JFrame { // 유저 로그인 성공 시 나올 창.
	private static final long serialVersionUID = 1L;
	
	Container c = getContentPane();
	JTabbedPane pane = createTabbedPane(); // 탭팬 만들기 
	
	FirstUserBusView() {
		setTitle("버스 좌석 예약");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		c.add(pane, BorderLayout.CENTER);
		setSize(650, 700);

		setResizable(false); // container의 크기 변경 막기.
		setLocationRelativeTo(null); // container를 중앙에 배치.
		
		
		setVisible(true);
	}

	private JTabbedPane createTabbedPane() {
		JTabbedPane pane = new JTabbedPane();
		pane.addTab("예매", new ResPanel()); // 예매 탭
		pane.addTab("예매 확인", new resCheckPanel());
		pane.addTab("로그아웃", new LogoutUserPanel());
		return pane;
	}

	class ResPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		JLabel[] label = new JLabel[4];
		JComboBox<String> combS = new JComboBox<String>(); // 출발지 콤보박스(start)
		JComboBox<String> combF = new JComboBox<String>(); // 도착지 콤보박스(finish)
		JComboBox<String> combR = new JComboBox<String>(); // 소요시간 콤보박스(retime)
		JButton[] button = new JButton[3];
		SpinnerDateModel dateModel = new SpinnerDateModel();
		JSpinner startT = new JSpinner(dateModel); // 출발일 스피너(startT)

		// 테이블 기본 설정
		String colName[] = { "출발시간", "소요시간", "등급", "잔여석" };
		DefaultTableModel model = new DefaultTableModel(colName, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int i, int c) {
				return false;
			}
		};

		JTable table = new JTable(model);
		DefaultTableCellRenderer ca = new DefaultTableCellRenderer();

		ResPanel() { // 예매 탭.
			newComponents();
			setComponents();
			addComponents();
		}

		void newComponents() {
			String[] lName = { "출발지", "도착지", "출발일", "시간" }; // 라벨 배열
			String[] combSName = { "부산", "서울", "대구", "울산", "대전", "광주", "인천" }; // 출발지 리스트 배열
			String[] combFName = { "부산", "서울", "대구", "울산", "대전", "광주", "인천" }; // 도착지 리스트 배열
			String[] combRName = { "00시", "01시", "02시", "03시", "04시", "05시", "06시", "07시", "08시", "09시", "10시", "11시",
					"12시", "13시", "14시", "15시", "16시", "17시", "18시", "19시", "20시", "21시", "22시", "23시", "24시", }; // 시간 리스트 배열
			String[] bName = { "조회", "예매하기", "초기화" }; // 버튼 리스트 배열
			
			setLayout(new FlowLayout());
			
			// 출발지 콤보박스 배치 
			for (int i = 0; i < combSName.length; i++) {
				combS.addItem(combSName[i]);
			}
			// 도착지 콤보박스 배치
			for (int i = 0; i < combFName.length; i++) {
				combF.addItem(combFName[i]);
			}
			// 소요시간 콤보박스 배치
			for (int i = 0; i < combRName.length; i++) {
				combR.addItem(combRName[i]);
			}
			// 라벨 배치
			for (int i = 0; i < lName.length; i++) {
				label[i] = new JLabel(lName[i]);
			}
			// 버튼 배치
			for (int i = 0; i < bName.length; i++) {
				button[i] = new JButton(bName[i]);
			}
			
			// Table의 크기를 설정 및 수정 불가.
			table.setPreferredScrollableViewportSize(new Dimension(550, 118));
			table.getTableHeader().setReorderingAllowed(false);

			// Table의 열 길이 설정.
			table.getColumnModel().getColumn(0).setPreferredWidth(70);
			table.getColumnModel().getColumn(1).setPreferredWidth(70);
			table.getColumnModel().getColumn(2).setPreferredWidth(50);
			table.getColumnModel().getColumn(3).setPreferredWidth(60);
			// 렌더러의 가로정렬을 CENTER로
			ca.setHorizontalAlignment(SwingConstants.CENTER);

			// 정렬할 테이블의 컬럼모델을 가져옴
			TableColumnModel tcm = table.getColumnModel();

			// 모든 열에 ca 설정을 추가.
			tcm.getColumn(0).setCellRenderer(ca);
			tcm.getColumn(1).setCellRenderer(ca);
			tcm.getColumn(2).setCellRenderer(ca);
			tcm.getColumn(3).setCellRenderer(ca);
		}

		void setComponents() {
			startT.setEditor(new JSpinner.DateEditor(startT, "yyyy.MM.dd"));
			
			button[1].addActionListener(new resAction());
		}

		void addComponents() {
			add(label[0]);
			add(combS);
			add(label[1]);
			add(combF);
			add(label[2]);
			add(startT);
			add(label[3]);
			add(combR);
			add(button[0]);
			add(new JScrollPane(table));

			add(button[1]);
			add(button[2]);
		}

		class resAction implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == button[1]) {
					new BusSeatView();
				}
			}

		}
	}
	
	// 예매 확인 패널
	// 2번 패널
	class resCheckPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		JButton rescBtn = new JButton("예매 취소"); // 예매 취소 버튼
		// 테이블 기본 설정
		String colName2[] = { "출발지", "도착지", "출발시간", "등급", "좌석번호" };
		DefaultTableModel model2 = new DefaultTableModel(colName2, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int i, int c) {
				return false;
			}
		};

		JTable table2 = new JTable(model2);
		DefaultTableCellRenderer ca = new DefaultTableCellRenderer();

		public resCheckPanel() {
			setLayout(new FlowLayout());
			add(new JScrollPane(table2)); // 테이블을 스크롤팬에 삽입
			// Table의 크기를 설정 및 수정 불가.
			table2.setPreferredScrollableViewportSize(new Dimension(550, 118));
			table2.getTableHeader().setReorderingAllowed(false);

			// Table의 열 길이 설정.
			table2.getColumnModel().getColumn(0).setPreferredWidth(70);
			table2.getColumnModel().getColumn(1).setPreferredWidth(70);
			table2.getColumnModel().getColumn(2).setPreferredWidth(50);
			table2.getColumnModel().getColumn(3).setPreferredWidth(60);
			table2.getColumnModel().getColumn(4).setPreferredWidth(70);
			// 렌더러의 가로정렬을 CENTER로
			ca.setHorizontalAlignment(SwingConstants.CENTER);

			// 정렬할 테이블의 컬럼모델을 가져옴
			TableColumnModel tcm = table2.getColumnModel();

			// 모든 열에 ca 설정을 추가.
			tcm.getColumn(0).setCellRenderer(ca);
			tcm.getColumn(1).setCellRenderer(ca);
			tcm.getColumn(2).setCellRenderer(ca);
			tcm.getColumn(3).setCellRenderer(ca);

			add(rescBtn); // 예매 취소 버튼 붙이기

			// 예매 취소 버튼을 눌렀을 때 이벤트
			rescBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int cancle = JOptionPane.showConfirmDialog(null, "출발지 -> 도착지 출발시간 x 등급 x 좌석번호 x 취소하시겠습니까?",
							"Confirm", JOptionPane.YES_NO_OPTION);
					// 출발지 -> 도착지 출발시간 x시 등급 x 좌석번호 x  취소하시겠습니까?
					//사용자가 선택한 버튼에 따라 이벤트 발생
					if(cancle == JOptionPane.CLOSED_OPTION) {
						System.out.println("선택하지 않고 그냥 닫음"); // 팝업창을 선택하지 않고 닫으면 콘솔창에 다음과 같은 문자열 출력
					}
					else if(cancle == JOptionPane.YES_OPTION) { // 예 버튼을 클릭한 경우
					System.out.println("취소되었습니다.");
					}
					else { // 아니요 버튼을 클릭한 경우 
						System.out.println("취소되지 않았습니다.");
					}
				}
			});

		}
	}
	
	class LogoutUserPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		JButton btn = new JButton("로그아웃");
		
		LogoutUserPanel() {
			btn.addActionListener(new LogoutAction());
			add(btn, BorderLayout.CENTER);
		}
		
		class LogoutAction implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btn) {
					btnAction();
					
					new LoginView();
				}
			}

		}
	}
	
	private void btnAction() {
			setVisible(false);
		}
}

class FirstAdminBusView extends JFrame { // 관리자 로그인 성공 시 나올 창.
	private static final long serialVersionUID = 1L;
	
	Container c = getContentPane();
	JTabbedPane pane = createTabbedPane(); // 탭팬 만들기 

	FirstAdminBusView() {
		setTitle("버스 좌석 예매 (관리자)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(650, 700);
		
		c.add(pane, BorderLayout.CENTER);
		
		setResizable(false); // container의 크기 변경 막기.
		setLocationRelativeTo(null); // container를 중앙에 배치.
		
		setVisible(true);
	}
	
	private JTabbedPane createTabbedPane() {
		JTabbedPane pane = new JTabbedPane();
		pane.addTab("관리자", new AdminPanel()); // 예매 탭
		pane.addTab("로그아웃", new LogoutAdminPanel());
		return pane;
	}
	
	class AdminPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		AdminPanel() {
			setLayout(new FlowLayout());
		}
	}
	
	class LogoutAdminPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		JButton btn = new JButton("로그아웃");
		
		LogoutAdminPanel() {
			btn.addActionListener(new LogoutAdminAction());
			add(btn, BorderLayout.CENTER);
		}
		
		class LogoutAdminAction implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btn) {
					btnAction();
					
					new LoginView();
				}
			}

		}
	}
	private void btnAction() {
		setVisible(false);
	}
}

class BusSeatView extends JFrame { // 버스 좌석 선택 창.
	private static final long serialVersionUID = 1L;

	Container c = getContentPane();
	JPanel[] pPanel = new JPanel[5]; // Panel에 추가하기 전에 먼저 합칠 Panel.
	JPanel[] panel = new JPanel[5]; // pPanel들을 추가할 Panel.
	JLabel[] la = new JLabel[3]; // 출발지, >, 목적지 Label.
	JButton[][] seatBtn1 = new JButton[20][2]; // 서쪽 좌석 Panel에 붙일 Button. (2차원)
	JButton[][] seatBtn2 = new JButton[20][2]; // 동쪽 좌석 Panel에 붙일 Button. (2차원)
	JButton[] seatBtn3 = new JButton[5]; // 남쪽 좌석 Panel에 붙일 Button.
	JButton[] btn = new JButton[2]; // 결제, 취소 Button.
	JLabel[] infoLa = new JLabel[5]; // 일반, 청소년, 어린이, 노인 Label.
	JLabel[] priceLa = new JLabel[2]; // 가격 Label.
	JTextField priceTf = new JTextField(10); // 가격 입력가능한 TextField.

	String[] desLa = { "출발지", "  >  ", "목적지" }; // 북쪽에 배치할 Label에 들어갈 문자열.
	String[] seat = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }; // 좌석 열에 해당하는 문자열.
	String[] personLa = { "일반", "청소년", "어린이", "노인" }; // 승객 정보에 해당하는 문자열.
	String[] priceString = { "금액", "원" }; // 금액 정보에 들어갈 문자열.
	String[] btnString = { "결제하기", "뒤로가기" }; // Button에 들어갈 문자열.

	// JSpinner.
	SpinnerNumberModel[] genPerson = new SpinnerNumberModel[4]; // 일반, 청소년, 어린이, 노인 으로 총 4개.
	JSpinner[] spinnerInteger = new JSpinner[4]; // 각 Spinner 들은 Int형으로 만듦.

	BusSeatView() { // BusSeat 생성자.
		setTitle("버스 좌석 예매");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);

		newComponents(); // 클래스를 구성할 멤버들을 설정하는 메소드.
		setComponents(); // 멤버들의 추가 설정을 추가하는 메소드.
		addComponents(); // 구성된 멤버들을 Panel에 부착하는 메소드.

		setVisible(true);
	}

	void newComponents() { // 클래스를 구성할 멤버들을 설정하는 메소드.

		// 북쪽 - Label.
		panel[0] = new JPanel(new FlowLayout());

		for (int i = 0; i < desLa.length; i++) { // desLa.length = 3.
			la[i] = new JLabel(desLa[i]); // Label을 출발지, >, 목적지 Label로 초기화.
		}

		// 서쪽 - 좌석.
		for (int i = 0; i < 2; i++) {
			panel[i + 1] = new JPanel(new BorderLayout()); // BorderLayout으로 설정.
		}

		for (int i = 0; i < 2; i++) { // 복도를 사이에 두고, 양 옆으로 배치할 Panel.
			pPanel[i] = new JPanel(new GridLayout(11, 2, 5, 5)); // GridLayout으로 설정.
		}

		pPanel[2] = new JPanel(new GridLayout(1, 1, 5, 5));

		for (int i = 0; i < 10; i++) { // 서쪽 좌석 초기화.
			for (int j = 0; j < 2; j++) {
				seatBtn1[i][j] = new JButton(seat[i] + Integer.toString(j + 1));
				pPanel[0].add(seatBtn1[i][j]);
			}
		}

		for (int i = 0; i < 10; i++) { // 동쪽 좌석 초기화.
			for (int j = 0; j < 2; j++) {
				seatBtn2[i][j] = new JButton(seat[i] + Integer.toString(j + 3));
				pPanel[1].add(seatBtn2[i][j]);
			}
		}

		for (int i = 0; i < 5; i++) { // 남쪽 (마지막 행) 좌석 초기화.
			seatBtn3[i] = new JButton(seat[10] + Integer.toString(i + 1));
			pPanel[2].add(seatBtn3[i]);
		}

		// 동쪽 - 정보.
		// 사람 정보.
		pPanel[3] = new JPanel(new GridLayout(4, 2, 20, 30));

		for (int i = 0; i < personLa.length; i++) { // personLa.length = 4.
			infoLa[i] = new JLabel(personLa[i]); // 정보에 맞는 Label 초기화.
			genPerson[i] = new SpinnerNumberModel(0, 0, 100, 1); // Spinner를 초기값 0, 최소값 0, 최댓값 100, 변경값 1로 초기화.
			spinnerInteger[i] = new JSpinner(genPerson[i]); // JSpinner 초기화.
		}

		// 금액 정보.
		pPanel[4] = new JPanel(new FlowLayout());

		for (int i = 0; i < priceString.length; i++) { // priceString.length = 2.
			priceLa[i] = new JLabel(priceString[i]);
		}

		priceTf = new JTextField(10); // 금액 입력 공간을 10으로 초기화.

		panel[3] = new JPanel(new GridLayout(2, 1, 10, 30));

		// 남쪽 - Button.
		panel[4] = new JPanel(new FlowLayout());

		for (int i = 0; i < btn.length; i++) { // btn.length = 2.
			btn[i] = new JButton(btnString[i]); // 결제, 취소 Button 생성.
		}
	}

	void setComponents() { // 멤버들의 추가 설정을 추가하는 메소드.
		for (int i = 0; i < personLa.length; i++) { // personLa.length = 4.
			infoLa[i].setHorizontalAlignment(JLabel.CENTER); // 사람 정보 Label들을 중앙에 정렬.
		}

		for (int i = 0; i < 10; i++) { // 서쪽 좌석 Panel안의 Button에 ActionListener 추가.
			for (int j = 0; j < 2; j++) {
				seatBtn1[i][j].addActionListener(new BusSeatAction());
			}
		}

		for (int i = 0; i < 10; i++) { // 동쪽 좌석 Panel안의 Button에 ActionListener 추가.
			for (int j = 0; j < 2; j++) {
				seatBtn2[i][j].addActionListener(new BusSeatAction());
			}
		}

		for (int i = 0; i < seatBtn3.length; i++) { // 남쪽 좌석 Panel안의 Button에 ActionListener 추가.
			seatBtn3[i].addActionListener(new BusSeatAction());
		}

		for (int i = 0; i < btn.length; i++) { // 결제, 취소 Button에 ActionListener 추가.
			btn[i].addActionListener(new BusSeatAction());
		}

		for (int i = 0; i < personLa.length; i++) {
			spinnerInteger[i].addChangeListener(new BusSeatChange());
		}

		setResizable(false); // container의 크기 변경 막기.
		setLocationRelativeTo(null); // container를 중앙에 배치.
	}

	void addComponents() { // 구성된 멤버들을 Panel에 부착하는 메소드.
		// 북쪽 - Label.
		for (int i = 0; i < desLa.length; i++) { // desLa.length = 3.
			panel[0].add(la[i]);
		}

		// 서쪽 - 좌석.
		panel[1].add(pPanel[0], BorderLayout.WEST);
		panel[1].add(pPanel[1], BorderLayout.EAST);

		panel[2].add(panel[1], BorderLayout.CENTER);
		panel[2].add(pPanel[2], BorderLayout.SOUTH);

		// 동쪽 - 정보.
		// 사람 정보.
		for (int i = 0; i < 4; i++) {
			pPanel[3].add(infoLa[i]);
			pPanel[3].add(spinnerInteger[i]);
		}

		// 금액 정보.
		pPanel[4].add(priceLa[0]);
		pPanel[4].add(priceTf);
		pPanel[4].add(priceLa[1]);

		panel[3].add(pPanel[3], BorderLayout.CENTER);
		panel[3].add(pPanel[4], BorderLayout.SOUTH);

		// 남쪽 - Button.
		for (int i = 0; i < btn.length; i++) {
			panel[4].add(btn[i]);
		}

		// c에 다 붙임.
		add(panel[0], BorderLayout.NORTH);
		add(panel[2], BorderLayout.WEST);
		add(panel[3], BorderLayout.EAST);
		add(panel[4], BorderLayout.SOUTH);
	}

	class BusSeatAction implements ActionListener { // Action을 추가해줄 ActionListener.

		@Override
		public void actionPerformed(ActionEvent e) { // 금액 TextField에 ActionListener 추가.
			if (e.getSource() == priceTf) {
				System.out.println("d");
			}

			for (int i = 0; i < 10; i++) { // 서쪽 Button들에 ActionListener 추가.
				for (int j = 0; j < 2; j++) {
					if (e.getSource() == seatBtn1[i][j]) {
						System.out.println("a");
					}
				}
			}

			for (int i = 0; i < 10; i++) { // 동쪽 Button들에 ActionListener 추가.
				for (int j = 0; j < 2; j++) {
					if (e.getSource() == seatBtn2[i][j]) {
						System.out.println("b");
					}
				}

			}

			for (int i = 0; i < seatBtn3.length; i++) { // 남쪽 Button들에 ActionListener 추가.
				if (e.getSource() == seatBtn3[i]) {
					System.out.println("c");
				}
			}
			
			if (e.getSource() == btn[1]) { // 취소 Button에 ActionListener 추가.
				setVisible(false);
			}
		}

	}

	class BusSeatChange implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			for (int i = 0; i < personLa.length; i++) {
				System.out.println(spinnerInteger[i].getValue().toString());
			}
		}

	}
}

public class TeamBusSeat {
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	public TeamBusSeat() {

	}

	// DB에 연결하는 메소드.
	public Connection makeConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("드라이브 적재 성공");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_db", "root", "1234");
			stmt = con.createStatement();
			System.out.println("데이터베이스 연결 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버를 찾을 수 없습니다.");
			e.getStackTrace();
		} catch (SQLException e) {
			System.out.println("연걸에 실패하였습니다.");
			e.getStackTrace();
		}
		return con;
	}

	// DB 연결 종료 메소드.
	public void disConnection() {
		try {
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		new LoginView();
	}

}
