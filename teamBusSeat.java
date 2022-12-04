package team;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import team.FirstUserBusView.ResPanel;

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
	static String id = "";

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
		sql += "SELECT * FROM member ";

		try {
			sql += "WHERE MID = '" + tf[0].getText() + "'";
			System.out.println(sql);
			t.rs = t.stmt.executeQuery(sql);

			if (t.rs.isBeforeFirst() == false) {
				JOptionPane.showMessageDialog(null, "로그인에 실패했습니다!\n값을 제대로 입력해주세요!", "로그인 오류",
						JOptionPane.ERROR_MESSAGE); // 로그인 실패 창 출력.
			} else {
				while (t.rs.next()) {
					if (tf[1].getText().equals(t.rs.getString("PW"))) {
						JOptionPane.showMessageDialog(null, "로그인 성공!", "로그인 성공", JOptionPane.PLAIN_MESSAGE); // 로그인 성공 창 출력.

						loginId();
						new FirstUserBusView(); // 유저 기본 창으로 이동.

						setVisible(false);
					} else { // 비밀번호가 틀리다면,
						JOptionPane.showMessageDialog(null, "아이디 혹은 비밀번호를 잘못 입력했습니다.\n입력하신 내용을 다시 확인해주세요.", "로그인 오류",
								JOptionPane.ERROR_MESSAGE); // 로그인 실패 창 출력.
					}
				}
			}
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		t.disConnection(); // DB 연결 종료 메소드.

	}

	void adminLogin() { // 관리자 로그인.
		if (tf[1].getText().equals("ADMIN")) { // 만약 비밀번호가 ADMIN이 입력된다면,
			JOptionPane.showMessageDialog(null, "관리자 로그인 성공!", "로그인 성공", JOptionPane.PLAIN_MESSAGE); // 로그인 성공 창 출력.
			new FirstAdminBusView(); // 관리자 기본 창으로 이동.

			setVisible(false);
		} else { // 비밀번호가 틀리다면,
			JOptionPane.showMessageDialog(null, "아이디 혹은 비밀번호를 잘못 입력했습니다.\n입력하신 내용을 다시 확인해주세요.", "로그인 오류",
					JOptionPane.ERROR_MESSAGE); // 로그인 실패 창 출력.
		}
	}

	String loginId() { // 로그인 한 ID를 저장.
		id = tf[0].getText();

		return id;
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
				dupCheck(); // DB에서 ID 중복 체크 메소드.
			} else if (e.getSource() == btn[1]) { // 회원가입 Button 클릭 시,
				phoneCheck(); // 전화번호 중복체크를 별도로 실행하는 메소드.
				signUpCheck(); // 회원가입 체크 메소드.
			} else if (e.getSource() == btn[2]) { // 취소 Button 클릭 시,
				int isCancel = JOptionPane.showConfirmDialog(null, "회원 가입을 취소하시겠습니까?", null, JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE); // 취소 다이얼로그.

				if (isCancel == JOptionPane.YES_OPTION) { // YES_OPTION 선택 시,
					setVisible(false); // 창 닫기.
				}
			}
		}

	}

	void dupCheck() { // DB에서 ID 중복 체크 메소드.
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
				JOptionPane.showMessageDialog(null, "값이 비어있습니다.\n값을 입력해주세요!", "ID 중복 체크", JOptionPane.ERROR_MESSAGE); // 에러 메세지 출력.
			} else { // 가장 처음의 데이터가 true (있다면),
				while (t.rs.next()) { // DB에서 행을 반복하면서,
					System.out.println(t.rs.getString("MID"));
					System.out.println(tf[0].getText().toString());

					if (tf[0].getText().equals(t.rs.getString("MID"))) { // 중복하는 ID가 DB에 있으면,
						JOptionPane.showMessageDialog(null, "이미 존재하는 ID입니다!", "ID 중복 체크", JOptionPane.ERROR_MESSAGE); // 중복 체크 실패.

						// ID를 제외한 나머지 TextField, ComboBox 비활성화.
						for (int i = 1; i < tf.length; i++) {
							tf[i].setEnabled(false);
						}

						monthCombG.setEnabled(false);

						for (int i = 0; i < rbtn.length; i++) {
							rbtn[i].setEnabled(false);
						}

						tf[0].setText(""); // ID TextField 초기화.
					}
				}
			}
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		t.disConnection(); // DB 연결 종료 메소드.
	}

	void signUpCheck() { // 회원가입 체크.
		if (!tf[1].getText().equals(tf[2].getText())) { // tf[1]과 tf[2]가 다르다면,
			JOptionPane.showMessageDialog(null, "PW가 일치하지 않습니다!", "PW 오류", JOptionPane.ERROR_MESSAGE); // PW 일치하지 않음.
		} else { // 회원가입에 문제가 없다면, (ID 중복 아님, PW 일치.)
			t.makeConnection();
			String sql = "";

			try {
				if (rbtn[0].isSelected() == true) { // 성별에서 남자가 선택됐다면,
					sql += "INSERT INTO member (MID, PW, name, birth, gender, phone) values ";
					sql += "('" + tf[0].getText() + "', '" + tf[1].getText() + "', '" + tf[3].getText() + "', '" // ID, PW, 이름.
							+ tf[5].getText() + "-" + monthCombG.getSelectedItem() + "-" + tf[6].getText() + "', '남', '" // 생년월일, 성별.
							+ tf[4].getText() + "');"; // 전화번호.
					if (tf[0].getText().equals("") || tf[1].getText().equals("") || tf[3].getText().equals("")
							|| tf[4].getText().equals("") || tf[5].getText().equals("") || tf[6].getText().equals("")) { // 만약 채우지 않은 값이 있다면,
						JOptionPane.showMessageDialog(null, "입력하지 않은 값이 존재합니다!\n값을 입력해주세요!", "회원가입 오류",
								JOptionPane.ERROR_MESSAGE);
					} else { // 모든 값이 채워졌다면,
						System.out.println(sql);
						t.stmt.executeUpdate(sql); // sql문을 DB에 추가.

						JOptionPane.showMessageDialog(null, "회원가입 완료했습니다!", "회원가입 완료", JOptionPane.PLAIN_MESSAGE);
						setVisible(false); // 창 종료.
					}
				} else if (rbtn[1].isSelected() == true) { // 성별에서 여자가 선택됐다면,
					sql += "INSERT INTO member (MID, PW, name, birth, gender, phone) values ";
					sql += "('" + tf[0].getText() + "', '" + tf[1].getText() + "', '" + tf[3].getText() + "', '" // ID, PW, 이름.
							+ tf[5].getText() + "-" + monthCombG.getSelectedItem() + "-" + tf[6].getText() + "', '여', '" // 생년월일, 성별.
							+ tf[4].getText() + "')"; // 전화번호.

					if (tf[0].getText().equals("") || tf[1].getText().equals("") || tf[3].getText().equals("")
							|| tf[4].getText().equals("") || tf[5].getText().equals("") || tf[6].getText().equals("")) { // 만약 채우지 않은 값이 있다면,
						JOptionPane.showMessageDialog(null, "입력하지 않은 값이 존재합니다!\n값을 입력해주세요!", "회원가입 오류",
								JOptionPane.ERROR_MESSAGE);
					} else { // 모든 값이 채워졌다면,
						System.out.println(sql);
						t.stmt.executeUpdate(sql); // sql문을 DB에 추가.

						JOptionPane.showMessageDialog(null, "회원가입 완료했습니다!", "회원가입 완료", JOptionPane.PLAIN_MESSAGE);
						setVisible(false); // 창 종료.
					}
				}
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
			t.disConnection(); // DB 연결 종료 메소드.
		}

	}

	void phoneCheck() { // 전화번호는 중복값이 있으면 안되므로, 전화번호 중복체크를 별도로 실행하는 메소드.
		if (!tf[4].getText().equals("")) { // 전화번호 TextField의 값에 값이 들어있다면,
			t.makeConnection();
			String sql = "";
			sql += "SELECT * FROM member ";

			try {
				sql += "WHERE phone = '" + tf[4].getText() + "'";
				t.rs = t.stmt.executeQuery(sql);

				// sql문에 따른 DB 테이블의 끝까지, 중복되는 전화번호가 있으면, 경고창 출력.
				while (t.rs.next()) {
					if (t.rs.getString("phone").equals(tf[4].getText())) {
						JOptionPane.showMessageDialog(null, "이미 존재하는 전화번호입니다!", "회원가입 오류", JOptionPane.ERROR_MESSAGE);
					}
				}
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
			t.disConnection(); // DB 연결 종료 메소드.
		}
	}
}

class FirstUserBusView extends JFrame { // 유저 로그인 성공 시 나올 창.
	private static final long serialVersionUID = 1L;

	TeamBusSeat t = new TeamBusSeat(); // DB가 있는 메인 클래스 호출.
	NormalBusSeatView normalSeat;
	PremiumBusSeatView premiumSeat;

	Container c = getContentPane();
	JTabbedPane pane = createTabbedPane(); // 탭팬 만들기

	int row; // 테이블의 열을 찾음.

	FirstUserBusView() { // 유저 로그인 창의 생성자.
		setTitle("버스 좌석 예약");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		System.out.println(LoginView.id); // LoginView 클래스에서 로그인 했던 ID 값 가져오는 법.

		c.add(pane, BorderLayout.CENTER);
		setSize(650, 700);

		setResizable(false); // container의 크기 변경 막기.
		setLocationRelativeTo(null); // container를 중앙에 배치.

		setVisible(true);
	}

	private JTabbedPane createTabbedPane() { // 유저 Frame안에 들어갈 Pane들.
		JTabbedPane pane = new JTabbedPane();
		pane.addTab("버스 조회 및 예매", new ResPanel()); // 버스 조회 및 버스 예매 탭.
		pane.addTab("예매 취소", new ResCancelPanel()); // 버스 예매 취소 탭.
		pane.addTab("로그아웃", new LogoutUserPanel()); // 로그아웃 탭.
		return pane;
	}

	class ResPanel extends JPanel { // 버스 조회 및 버스 예매 탭.
		private static final long serialVersionUID = 1L;

		JLabel[] label = new JLabel[4];
		JComboBox<String> combS = new JComboBox<String>(); // 출발지 콤보박스(start)
		JComboBox<String> combF = new JComboBox<String>(); // 도착지 콤보박스(finish)
		JComboBox<String> combR = new JComboBox<String>(); // 소요시간 콤보박스(retime)
		JButton[] button = new JButton[3]; // 조회, 예약하기, 초기화 버튼.
		SpinnerDateModel dateModel = new SpinnerDateModel();
		JSpinner startT = new JSpinner(dateModel); // 출발일 스피너(startT)
		int index; // 출발 시간의 Index.
		static int busNumber; // bus 테이블의 busN에 해당.
		ArrayList<Integer> busNList = new ArrayList<>();
		static String busID; // bus 테이블의 busID에 해당.
		ArrayList<String> busIdList = new ArrayList<>();

		// 테이블 기본 설정
		String colName[] = { "출발시간", "소요시간", "등급", "잔여석", "총좌석" };
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
					"12시", "13시", "14시", "15시", "16시", "17시", "18시", "19시", "20시", "21시", "22시", "23시" }; // 시간 리스트 배열
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
			table.getColumnModel().getColumn(4).setPreferredWidth(60);

			// 렌더러의 가로정렬을 CENTER로
			ca.setHorizontalAlignment(SwingConstants.CENTER);

			// 정렬할 테이블의 컬럼모델을 가져옴
			TableColumnModel tcm = table.getColumnModel();

			// 모든 열에 ca 설정을 추가.
			tcm.getColumn(0).setCellRenderer(ca);
			tcm.getColumn(1).setCellRenderer(ca);
			tcm.getColumn(2).setCellRenderer(ca);
			tcm.getColumn(3).setCellRenderer(ca);
			tcm.getColumn(4).setCellRenderer(ca);
		}

		void setComponents() {
			startT.setEditor(new JSpinner.DateEditor(startT, "yyyy-MM-dd")); // Spinner에 나오는 날짜 타입 지정.

			button[0].addActionListener(new resAction()); // 조회 button에 ActionListener 추가.
			button[1].addActionListener(new resAction()); // 예매하기 button에 ActionListener 추가.
			button[2].addActionListener(new resAction());
			combR.addActionListener(new resAction()); // 소요시간 ComboBox에 ActionListener 추가.

			table.addMouseListener(new resMouseAction()); // Table에 MouseListener 추가.

			button[1].setEnabled(false); // 예매 확인 Button 막기.
			button[2].setEnabled(false); // 초기화 Button 막기.

			table.getTableHeader().setResizingAllowed(false); // 열 크기 조절 불가.
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

		class resAction implements ActionListener { // 버스 조회 창에 사용될 ActionListener.

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == button[0]) { // 조회버튼 클릭 시,
					busSearch();
					resetField();
				} else if (e.getSource() == button[1]) { // 예매하기 버튼 클릭 시,
					new ReCheckPanel();
				} else if (e.getSource() == button[2]) { // 초기화 버튼 클릭 시,
					resetSearchView(); // 초기화면으로 초기화하는 메소드.
					button[1].setEnabled(false);
				}
			}

		}

		class resMouseAction extends MouseAdapter { // 테이블에서 행을 클릭했을 때,
			public void mouseClicked(MouseEvent e) {
				row = table.getSelectedRow(); // 테이블에서 마우스로 눌린 행을 찾음.
				busNumber = busNList.get(row); // 클릭한 행의 차량 번호를 찾음.
				busID = busIdList.get(row); // 클릭한 행의 busID를 찾음.

				button[1].setEnabled(true); // 예매 button 나타나기.
			}

		}

		void resetList() { // 버스 번호, busID를 저장한 List들을 리셋하는 메소드.
			busNList.removeAll(busNList); // List내의 busID들을 지움.
			busIdList.removeAll(busIdList); // List내의 차량 번호들을 지움.
		}

		class ReCheckPanel extends JFrame { // 선택한 사항이 맞는지 나타나게 할 창.
			private static final long serialVersionUID = 1L;

			JLabel[] la = new JLabel[7]; // 출발지, 도착지, 등급, 소요시간, 출발 시간, 출발 날짜, "맞습니까?" Label.
			JTextField[] tf = new JTextField[6]; // 출발지, 도착지, 등급, 소요시간, 출발 시간, 출발 날짜 TextField.
			JButton[] btn = new JButton[2]; // Yes, No Button.
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			ReCheckPanel() {
				setTitle("선택하신 버스가 맞습니까?");
				setSize(500, 250);

				setLayout(null);

				newComponents();
				setComponents();
				addComponents();

				setVisible(true);
			}

			void newComponents() {
				String[] laString = { "출발지", "도착지", "등급", "출발 날짜", "출발 시간", "소요 시간", "위 정보가 맞습니까?" };
				String[] btnString = { "YES", "NO" };
				row = table.getSelectedRow(); // 테이블에서 선택한 행.

				for (int i = 0; i < la.length; i++) {
					la[i] = new JLabel(laString[i]);
				}

				for (int i = 0; i < tf.length; i++) {
					tf[i] = new JTextField();
				}

				// 출발지
				tf[0].setText(combS.getSelectedItem().toString());

				// 도착지
				tf[1].setText(combF.getSelectedItem().toString());

				// 등급
				tf[2].setText(table.getValueAt(row, 2).toString());

				// 출발 날짜
				tf[3].setText(dateFormat.format(dateModel.getValue()).toString());

				// 출발 시간
				tf[4].setText(table.getValueAt(row, 0).toString());

				// 소요 시간
				tf[5].setText(table.getValueAt(row, 1).toString());

				for (int i = 0; i < btn.length; i++) {
					btn[i] = new JButton(btnString[i]);
				}
			}

			void setComponents() {
				setLayout(null);

				// 각 Label, TextField, Button의 좌표 지정.
				// 출발지
				la[0].setBounds(30, 15, 50, 30);
				tf[0].setBounds(90, 20, 40, 20);

				// 도착지
				la[1].setBounds(190, 15, 50, 30);
				tf[1].setBounds(250, 20, 40, 20);

				// 등급
				la[2].setBounds(350, 15, 50, 30);
				tf[2].setBounds(420, 20, 40, 20);

				// 출발 날짜
				la[3].setBounds(30, 45, 70, 30);
				tf[3].setBounds(90, 50, 80, 20);

				// 출발 시간
				la[4].setBounds(190, 45, 70, 30);
				tf[4].setBounds(250, 50, 70, 20);

				// 소요 시간
				la[5].setBounds(350, 45, 70, 30);
				tf[5].setBounds(420, 50, 40, 20);

				// 맞습니까? 라벨
				la[6].setFont(new Font("Aharoni", Font.BOLD, 30));
				la[6].setBounds(95, 60, 300, 100);

				// Yes 버튼
				btn[0].setBounds(140, 150, 60, 30);

				// No 버튼
				btn[1].setBounds(280, 150, 60, 30);

				// TextField 설정.
				for (int i = 0; i < tf.length; i++) {
					tf[i].setHorizontalAlignment(JTextField.CENTER); // TextField 안의 내용을 중앙으로 정렬.
					tf[i].setEditable(false); // 편집 불가.
					tf[i].setBackground(Color.WHITE); // 백그라운드 색을 하얀색으로 지정.
				}

				for (int i = 0; i < btn.length; i++) {
					btn[i].addActionListener(new reCheckAciton());
				}

				setResizable(false); // container의 크기 변경 막기.
				setLocationRelativeTo(null); // container를 중앙에 배치.
			}

			void addComponents() {
				for (int i = 0; i < tf.length; i++) {
					add(la[i]);
					add(tf[i]);
				}

				add(la[6]);

				for (int i = 0; i < btn.length; i++) {
					add(btn[i]);
				}
			}

			class reCheckAciton implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) { // Button에 추가할 ActionListener.
					if (e.getSource() == btn[0]) { // Yes 버튼 클릭 시,
						System.out.println(table.getValueAt(row, 2).toString());
						if (table.getValueAt(row, 2).toString().equals("우등")) {
							new PremiumBusSeatView();
							setVisible(false);
						} else if (table.getValueAt(row, 2).toString().equals("일반")) {
							new NormalBusSeatView(); // 버스 좌석 선택 창.
							setVisible(false);
						}
					} else if (e.getSource() == btn[1]) { // No 버튼 클릭 시,
						setVisible(false);
					}
				}

			}
		}

		void busSearch() { // 원하는 (입력한) 조건에 맞게 버스를 조회하는 메소드.
			t.makeConnection();

			// 해당하는 날짜의 버스들을 조회.
			String sql = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			try {
				index = combR.getSelectedIndex(); // 출발 시간 index 값 가져오기. (ex. 0시 - 0, 18시 - 18.)
				resetList(); // List들의 내용을 리셋하는 메소드.

				System.out.println(index);

				model.setNumRows(0); // Table의 행을 처음(0)으로 변경.

				for (int i = index; i < 24; i++) { // 정한 시간부터 23시까지 반복.

					// 버스들을 조회하는 sql문.
					sql += "SELECT busID, busN, start, retime, rating, seatn, seatR, startT FROM bus ";
					sql += "WHERE date = '" + dateFormat.format(dateModel.getValue()) + "' ";
					sql += "AND start = '" + combS.getSelectedItem() + "' ";
					sql += "AND finish = '" + combF.getSelectedItem() + "' ";

					if (i < 10) { // 한자리 수 index 이면,
						sql += "AND startT LIKE '0" + i + "%'"; // 앞에 0을 붙임. (ex. 01시, 03시)
					} else { // 두자리 수 index 이면,
						// 
						sql += "AND startT LIKE '" + i + "%'";
					}

					t.rs = t.stmt.executeQuery(sql);
					System.out.println(sql);

					// 조회된 버스들을 추가.
					while (t.rs.next()) {
						model.addRow(new Object[] { t.rs.getString("startT"), t.rs.getString("retime"),
								t.rs.getString("rating"), t.rs.getString("seatR"), t.rs.getString("seatn") });
						busNList.add(t.rs.getInt("busN"));
						busIdList.add(t.rs.getString("busID"));
					}

					sql = ""; // sql문 초기화.
				}

				button[2].setEnabled(true); // 초기화 버튼 보이기.

				if (model.getRowCount() < 1) { // 테이블의 행 수가 0이면,
					// 선택한 날짜에 버스가 없다는 경고창 출력.
					button[2].setEnabled(false); // 초기화 버튼 안보이게 설정.
					JOptionPane.showMessageDialog(null, "선택하신 날짜에 해당하는 버스가 없습니다!", "버스 조회 오류",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
			t.disConnection(); // DB 연결 종료 메소드.
		}

		void resetSearchView() { // 초기화면으로 초기화하는 메소드.
			resetField(); // List들에 저장된 내용들을 리셋하는 메소드.

			model.setNumRows(0); // 테이블을 0번으로 초기화.
		}

		void resetField() { // 초기 화면의 내용 초기화하는 메소드.
			combS.setSelectedIndex(0);
			combF.setSelectedIndex(0);
			combR.setSelectedIndex(0);
		}
	}

	class ResCancelPanel extends JPanel { // 예매 취소 Panel.
		private static final long serialVersionUID = 1L;

		JButton rescBtn = new JButton(); // 예매 취소 Button.
		JButton checkBtn = new JButton(); // 예매 확인 BUtton.
		int row = 0;

		ArrayList<Integer> busNList = new ArrayList<>();
		ArrayList<String> busIdList = new ArrayList<>();
		String rating = "";
		ArrayList<String> ratingList = new ArrayList<>();
		String colDB = "";
		ArrayList<String> colList = new ArrayList<>();

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

		public ResCancelPanel() {
			addWindowListener(new cancelWindowAction());
			newComponents();
			setComponents();
			addComponents();
		}

		void newComponents() {
			rescBtn = new JButton("예매 취소"); // 예매 취소 Button.
			checkBtn = new JButton("예매 확인"); // 예매 확인 Button.

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
			tcm.getColumn(4).setCellRenderer(ca);
		}

		void setComponents() {
			setLayout(new FlowLayout());

			rescBtn.setEnabled(false); // 예매 취소 버튼 누르기 막기.
			rescBtn.addActionListener(new resCheckAction()); // 예매 취소 Button에 ActinListener 추가.

			checkBtn.addActionListener(new resCheckAction()); // 예매 확인 Button에 ActionListener 추가.

			table2.addMouseListener(new cancelMouseAction());
		}

		void addComponents() {
			add(new JScrollPane(table2)); // 테이블을 스크롤팬에 삽입
			add(checkBtn); // 예매 확인 버튼 붙이기
			add(rescBtn); // 예매 취소 버튼 붙이기
		}

		class resCheckAction implements ActionListener { // ActionListener.

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == rescBtn) { // 예매 취소 버튼을 누르면,
					cancelDB();
				}

				if (e.getSource() == checkBtn) { // 예매 확인 버튼을 누르면,
					loadDB();
				}
			}

		}

		class cancelMouseAction extends MouseAdapter { // MouseListener.
			public void mouseClicked(MouseEvent e) {
				row = table2.getSelectedRow();

				ResPanel.busNumber = busNList.get(row);
				ResPanel.busID = busIdList.get(row);
				rating = ratingList.get(row);
				colDB = colList.get(row);

				rescBtn.setEnabled(true); // 예매 취소 버튼 나타나기.
			}
		}

		class cancelWindowAction extends WindowAdapter { // 창이 켜지면 실행될 WindowListener.
			public void windowOpened(WindowEvent e) {
				loadDB(); // 데이터 베이스에서 값을 가져옴.
			}
		}

		void loadDB() { // DB에서 값을 가져와 테이블에 출력.
			t.makeConnection();
			String sql = "";

			try {
				model2.setNumRows(0); // 테이블의 행을 0으로 이동.
				resetList();

				// 로그인한 유저의 예약 정보를 찾아오는 sql문.
				sql += "SELECT seat.busID, seat.busN, start, finish, startT, rating, col "
						+ "FROM bus INNER JOIN seat On bus.busN = seat.busN WHERE MID = '" + LoginView.id + "'";

				t.rs = t.stmt.executeQuery(sql);
				System.out.println(sql);

				// 찾아온 정보를 테이블에 저장.
				while (t.rs.next()) {
					model2.addRow(new Object[] { t.rs.getString("start"), t.rs.getString("finish"),
							t.rs.getString("startT"), t.rs.getString("rating"), t.rs.getString("col") });
					busNList.add(t.rs.getInt("busN"));
					busIdList.add(t.rs.getString("busID"));
					ratingList.add(t.rs.getString("rating"));
					colList.add(t.rs.getString("col"));
				}

				sql = "";
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
			t.disConnection();
		}

		void cancelDB() { // 테이블의 내용을 DB에서 삭제.
			// 예매 취소 경고창.
			int isDelete = JOptionPane.showConfirmDialog(null, "예매를 취소하시겠습니까?", "좌석 예매 취소", JOptionPane.YES_NO_OPTION);

			if (isDelete == JOptionPane.YES_OPTION) { // 네라고 선택 시,
				t.makeConnection();
				String sql = "";
				System.out.println("busID : " + ResPanel.busID);
				System.out.println("busN : " + ResPanel.busNumber);

				try {
					// 예약을 취소하고자 하는 정보를 삭제하는 sql문.
					sql += "DELETE FROM seat WHERE busId = '" + ResPanel.busID + "' AND busN = '" + ResPanel.busNumber
							+ "' AND MID = '" + LoginView.id + "' AND col = '" + colDB + "'";
					t.stmt.executeUpdate(sql);

					if (rating.equals("우등")) { // 만약 등급이 우등이라면,
						System.out.println(rating);

						// 우등 버스의 잔여석을 다시 체크.
						premiumSeat = new PremiumBusSeatView();
						premiumSeat.firstPremiumGetSeat();
						premiumSeat.premiumPurchaseDB();
						premiumSeat.premiumRemainUpdate();
					} else if (rating.equals("일반")) { // 만약 등급이 일반이라면,
						System.out.println(rating);

						// 일반 버스의 잔여석을 다시 체크.
						normalSeat = new NormalBusSeatView();
						normalSeat.firstNormalGetSeat();
						normalSeat.normalPurchaseDB();
						normalSeat.normalRemainUpdate();
					}
					loadDB(); // DB에서 값을 가져와 테이블에 출력.
				} catch (SQLException sqle) {
					System.out.println(sqle.getMessage());
				}
				t.disConnection();
			}
		}

		void resetList() { // List들의 내용을 리셋하는 메소드.
			busNList.removeAll(busNList);
			busIdList.removeAll(busIdList);
			ratingList.removeAll(ratingList);
			colList.removeAll(colList);
		}

	}

	class LogoutUserPanel extends JPanel { // 사용자 로그아웃 Panel.
		private static final long serialVersionUID = 1L;

		JButton logoutBtn = new JButton("로그아웃");

		LogoutUserPanel() { // 사용자 로그아웃 창의 생성자.
			setLayout(null);
			logoutBtn.setBounds(210, 280, 200, 50);
			logoutBtn.addActionListener(new LogoutAction());
			logoutBtn.setFont(new Font("", Font.BOLD, 30));
			add(logoutBtn, BorderLayout.CENTER);
		}

		class LogoutAction implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == logoutBtn) { // 로그아웃 Button이 눌러지면,
					int isCancel = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?", "Confirm",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); // 로그아웃 경고 창.

					if (isCancel == JOptionPane.YES_OPTION) { // 로그아웃에 '네' Button 클릭 시,
						btnAction(); // Panel 비활성화 메소드.
						new LoginView(); // 로그인 창으로 돌아감.
					}
				}
			}

		}
	}

	private void btnAction() { // Panel 비활성화 메소드.
		setVisible(false); // Panel 비활성화.
	}
}

class FirstAdminBusView extends JFrame { // 관리자 로그인 성공 시 나올 창.
	private static final long serialVersionUID = 1L;

	Container c = getContentPane();
	JTabbedPane pane = createTabbedPane(); // 탭팬 만들기

	TeamBusSeat t = new TeamBusSeat();
	NormalBusSeatView normalSeat;
	PremiumBusSeatView premiumSeat;

	JTable userTable = new JTable();

	FirstAdminBusView() { // 관리자 창 초기화면 생성자.
		setTitle("버스 좌석 예매 (관리자)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(650, 700);

		c.add(pane, BorderLayout.CENTER);

		setResizable(false); // container의 크기 변경 막기.
		setLocationRelativeTo(null); // container를 중앙에 배치.

		setVisible(true);
	}

	private JTabbedPane createTabbedPane() { // 사용자 Frame안에 들어갈 Pane들.
		JTabbedPane pane = new JTabbedPane();
		pane.addTab("버스 관리", new BusMgtPanel()); // 버스 관리 창
		pane.addTab("고객 관리", new UserMgtPanel()); // 고객 관리 창
		pane.addTab("로그아웃", new LogoutAdminPanel());
		return pane;
	}

	class BusMgtPanel extends JPanel { // 버스 관리창
		private static final long serialVersionUID = 1L;

		JLabel[] label = new JLabel[8]; // 버스 관리창 라벨
		JTextField[] tf = new JTextField[8]; // 버스 관리창 텍스트필드
		JButton[] button = new JButton[5]; // 버스 관리창 버튼
		JScrollPane jscp = new JScrollPane();
		String busId;
		String busN;
		String start;
		String finish;
		String rating;
		String date;
		String startT;
		String retime;
		String busNumbers; // bus 테이블의 busN에 해당.
		ArrayList<String> busLists = new ArrayList<>();

		// 버스 정보테이블 기본 설정
		String busColName[] = { "차량 번호", "버스 번호", "출발지", "도착지", "소요시간", "등급", "좌석수", "잔여석", "날짜", "출발시간" }; // 버스 관리창 테이블 열 이름 배열

		DefaultTableModel busModel = new DefaultTableModel(busColName, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int i, int c) {
				return false;
			}
		};

		JTable busTable = new JTable(busModel);

		DefaultTableCellRenderer ca = new DefaultTableCellRenderer();

		BusMgtPanel() { // 버스 관리 창
			setLayout(new BorderLayout());
			newComponents();
			setComponents();
			addComponents();
		}

		void newComponents() {
			String[] lName = { "차량 번호", "버스 ID", "출발지", "도착지", "등급", "날짜", "출발시간", "소요시간" }; // 라벨 배열
			String[] bName = { "추가", "수정", "삭제", "초기화", "검색" }; // 버튼 배열

			// 라벨 생성
			for (int i = 0; i < lName.length; i++) {
				label[i] = new JLabel(lName[i]);
			}

			// 버튼 생성
			for (int i = 0; i < bName.length; i++) {
				button[i] = new JButton(bName[i]);
			}

			// 텍스트필드 생성
			for (int i = 0; i < tf.length; i++) {
				tf[i] = new JTextField(5);
			}

			// Table의 크기를 설정 및 수정 불가.
			busTable.setPreferredScrollableViewportSize(new Dimension(650, 118));
			busTable.getTableHeader().setReorderingAllowed(false);

			// Table의 열 길이 설정
			busTable.getColumnModel().getColumn(0).setPreferredWidth(70);
			busTable.getColumnModel().getColumn(1).setPreferredWidth(70);
			busTable.getColumnModel().getColumn(2).setPreferredWidth(60);
			busTable.getColumnModel().getColumn(3).setPreferredWidth(60);
			busTable.getColumnModel().getColumn(4).setPreferredWidth(60);
			busTable.getColumnModel().getColumn(5).setPreferredWidth(50);
			busTable.getColumnModel().getColumn(6).setPreferredWidth(50);
			busTable.getColumnModel().getColumn(7).setPreferredWidth(50);
			busTable.getColumnModel().getColumn(8).setPreferredWidth(80);
			busTable.getColumnModel().getColumn(9).setPreferredWidth(70);

			// 렌더러의 가로정렬을 CENTER로
			ca.setHorizontalAlignment(SwingConstants.CENTER);

			// 정렬할 테이블의 컬럼모델을 가져옴
			TableColumnModel tcm = busTable.getColumnModel();

			// 모든 열에 ca 설정을 추가
			tcm.getColumn(0).setCellRenderer(ca);
			tcm.getColumn(1).setCellRenderer(ca);
			tcm.getColumn(2).setCellRenderer(ca);
			tcm.getColumn(3).setCellRenderer(ca);
			tcm.getColumn(4).setCellRenderer(ca);
			tcm.getColumn(5).setCellRenderer(ca);
			tcm.getColumn(6).setCellRenderer(ca);
			tcm.getColumn(7).setCellRenderer(ca);
			tcm.getColumn(8).setCellRenderer(ca);
			tcm.getColumn(9).setCellRenderer(ca);

			jscp = new JScrollPane(busTable);
		}

		void setComponents() {
			setLayout(null);

			for (int i = 0; i < button.length; i++) {
				button[i].addActionListener(new busAction());
			}

			button[1].setEnabled(false); // 수정 button 막기
			button[2].setEnabled(false); // 삭제 button 막기

			busTable.getTableHeader().setResizingAllowed(false); // 열 크기 조절 불가.
			busTable.addMouseListener(new MouseHandler());

			// 각 Label, TextField, Table, Button의 좌표를 지정.
			// 차량 번호
			label[0].setBounds(35, 20, 70, 30);
			tf[0].setBounds(90, 25, 60, 20);

			// 버스 번호
			label[1].setBounds(160, 20, 50, 30);
			tf[1].setBounds(210, 25, 60, 20);

			// 출발지
			label[2].setBounds(300, 20, 50, 30);
			tf[2].setBounds(360, 25, 60, 20);

			// 도착지
			label[3].setBounds(450, 20, 50, 30);
			tf[3].setBounds(505, 25, 60, 20);

			// 등급
			label[4].setBounds(35, 50, 70, 30);
			tf[4].setBounds(90, 55, 60, 20);

			// 날짜
			label[5].setBounds(160, 50, 50, 30);
			tf[5].setBounds(210, 55, 80, 20);

			// 출발 시간.
			label[6].setBounds(300, 50, 50, 30);
			tf[6].setBounds(360, 55, 80, 20);

			// 소요시간
			label[7].setBounds(450, 50, 50, 30);
			tf[7].setBounds(505, 55, 80, 20);

			button[0].setBounds(125, 98, 80, 30); // 추가 버튼
			button[2].setBounds(275, 98, 80, 30); // 삭제 버튼
			button[3].setBounds(425, 98, 80, 30); // 초기화 버튼

			jscp.setBounds(35, 150, 550, 150); // 테이블

			button[4].setBounds(35, 315, 550, 30); // 검색 버튼

			setResizable(false); // container의 크기 변경 막기.
			setLocationRelativeTo(null); // container를 중앙에 배치.
		}

		void addComponents() {
			add(label[0]);
			add(tf[0]);
			add(label[1]);
			add(tf[1]);
			add(label[2]);
			add(tf[2]);
			add(label[3]);
			add(tf[3]);

			add(label[4]);
			add(tf[4]);
			add(label[5]);
			add(tf[5]);
			add(label[6]);
			add(tf[6]);
			add(label[7]);
			add(tf[7]);

			for (int i = 0; i < 4; i++) {
				add(button[i]);
			}

			add(jscp);

			add(button[4]);
		}

		void resetField() { // TextField들을 리셋하는 메소드.
			for (int i = 0; i < tf.length; i++) {
				tf[i].setText("");
			}
		}

		class MouseHandler extends MouseAdapter {
			public void mouseClicked(MouseEvent e) {
				int busMgtRow = busTable.getSelectedRow(); // 버스 관리 창의 행을 구함.

				TableModel busData = busTable.getModel();

				busId = (String) busData.getValueAt(busMgtRow, 0);
				busN = (String) busData.getValueAt(busMgtRow, 1);
				start = (String) busData.getValueAt(busMgtRow, 2);
				finish = (String) busData.getValueAt(busMgtRow, 3);
				rating = (String) busData.getValueAt(busMgtRow, 5);
				date = (String) busData.getValueAt(busMgtRow, 8);
				startT = (String) busData.getValueAt(busMgtRow, 9);
				retime = (String) busData.getValueAt(busMgtRow, 4);

				tf[0].setText(busId);
				tf[1].setText(busN);
				tf[2].setText(start);
				tf[3].setText(finish);
				tf[4].setText(rating);
				tf[5].setText(date);
				tf[6].setText(startT);
				tf[7].setText(retime);

				button[0].setEnabled(false);
				button[1].setEnabled(true);
				button[2].setEnabled(true);
			}
		}

		// 버튼 이벤트
		class busAction implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == button[0]) { // 추가 버튼.
					addBus(); // 버스 추가 메소드.
					button[0].setEnabled(true);
					button[2].setEnabled(false);
				} else if (e.getSource() == button[2]) { // 삭제 버튼.
					deleteBus(); // 버스 삭제 메소드.
					resetField(); // TextField들을 리셋하는 메소드.
					busModel.setNumRows(0);
					button[0].setEnabled(true);
					button[1].setEnabled(false);
					button[2].setEnabled(false);
				} else if (e.getSource() == button[3]) { // 초기화 버튼.
					resetField(); // TextField들을 리셋하는 메소드.
					busModel.setNumRows(0);
					button[0].setEnabled(true);
					button[1].setEnabled(false);
					button[2].setEnabled(false);
				} else if (e.getSource() == button[4]) { // 검색 버튼.
					new BusSearchView(); // 버스 검색 메소드.
				}

			}
		}

		boolean busDupCheck() {
			t.makeConnection();
			String sql = "";

			try {
				sql = "SELECT busN FROM bus;";

				t.rs = t.stmt.executeQuery(sql);
				while (t.rs.next()) {
					if (tf[1].getText().equals(t.rs.getString("busN"))) {
						JOptionPane.showMessageDialog(null, "이미 배치된 버스가 존재합니다!", "버스 추가 오류",
								JOptionPane.WARNING_MESSAGE);
						return false;
					}
				}
				return true;
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
			t.disConnection();
			return true;
		}

		void addBus() { // 버스 추가 메소드.
			t.makeConnection();
			String sql = "";

			try {
				int isDelete = JOptionPane.showConfirmDialog(null, "버스를 추가하시겠습니까?", "버스 추가", JOptionPane.YES_NO_OPTION);

				if (isDelete == JOptionPane.YES_OPTION) {
					if (busDupCheck() == true) {
						if (!(tf[0].getText().equals("") || tf[1].getText().equals("") || tf[2].getText().equals("")
								|| tf[3].getText().equals("") || tf[4].getText().equals("")
								|| tf[5].getText().equals("") || tf[6].getText().equals("")
								|| tf[7].getText().equals(""))) { // 비어있는 값이 없다면,
							if (tf[4].getText().equals("일반")) { // 일반 버스라면,
								// 일반 버스를 DB에 추가.
								sql += "INSERT INTO bus (busID, busN, start, finish, retime, rating, seatn, seatR, startT, date) values ";
								sql += "('" + tf[0].getText() + "', '" + tf[1].getText() + "', '" + tf[2].getText()
										+ "', '" + tf[3].getText() + "', '" + tf[7].getText() + "', '일반', '45', '45', '"
										+ tf[6].getText() + "', '" + tf[5].getText() + "');";

								System.out.println(sql);
								t.stmt.executeUpdate(sql);

								resetField(); // TextField들을 리셋하는 메소드.
								busModel.setNumRows(0);
								button[0].setEnabled(false);
								button[1].setEnabled(true);
								button[2].setEnabled(true);
							} else if (tf[4].getText().equals("우등")) { // 우등 버스라면,
								// 우등 버스를 DB에 추가.
								sql += "INSERT INTO bus (busID, busN, start, finish, retime, rating, seatn, seatR, startT, date) values ";
								sql += "('" + tf[0].getText() + "', '" + tf[1].getText() + "', '" + tf[2].getText()
										+ "', '" + tf[3].getText() + "', '" + tf[7].getText() + "', '우등', '28', '28', '"
										+ tf[6].getText() + "', '" + tf[5].getText() + "')";

								System.out.println(sql);
								t.stmt.executeUpdate(sql);

								resetField(); // TextField들을 리셋하는 메소드.
								busModel.setNumRows(0);
								button[0].setEnabled(false);
								button[1].setEnabled(true);
								button[2].setEnabled(true);
							}

						} else { // 공간이 비었으면, 경고문 출력.
							JOptionPane.showMessageDialog(null, "공간을 모두 채운다음 다시 시도해주세요!", "버스 추가 오류",
									JOptionPane.WARNING_MESSAGE);
						}
					}

				}
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
			t.disConnection();
		}

		void deleteBus() { // 버스 삭제 메소드.
			int isDelete = JOptionPane.showConfirmDialog(null, "버스를 삭제하시겠습니까?", "버스 삭제", JOptionPane.YES_NO_OPTION);

			if (isDelete == JOptionPane.YES_OPTION) { // 삭제한다고 하면,
				t.makeConnection();
				String sql = "";

				try {
					// 버스를 삭제하는 sql문.
					sql = "DELETE b, s FROM bus b LEFT JOIN seat s ON b.busID = s.busID AND b.busN = s.busN "
							+ "WHERE b.busID = '" + busId + "' AND b.busN = '" + busN + "'";
					System.out.println(sql);
					t.stmt.executeUpdate(sql);
					busModel.setNumRows(0);
				} catch (SQLException sqle) {
					System.out.println(sqle.getMessage());
				}
				t.disConnection();
			}

		}

		// 버스 관리 검색 창의 역할을 수행할 JFrame
		class BusSearchView extends JFrame { // 버스 관리 검색 창의 역할을 수행할 JFrame
			private static final long serialVersionUID = 1L;

			// busSearchView의 변수 선언
			Container c = getContentPane();
			JPanel p1, p2; // p1에는 콤보박스와 텍스트필드, p2에는 버튼들 배치됨
			JComboBox<String> combBus; // 콤보박스 (bus 테이블의 애트리뷰트명 문자열이 들어감)
			String[] bus = { "출발지", "도착지", "버스 번호", "차량 번호", "등급", "날짜", "출발 시간", "소요 시간" }; // combBus에 들어가는 문자열
			JTextField busTf; // 검색어를 입력받는 텍스트필드
			JButton btn[] = new JButton[2];
			String[] btnString = { "검색", "취소" };

			// busSearchView의 기본 설정
			public BusSearchView() {
				setTitle("버스 정보 검색");
				setSize(320, 105);

				// 리스트와 검색어 텍스트필드 추가
				p1 = new JPanel(new FlowLayout());
				combBus = new JComboBox<String>(bus);

				// 콤보박스에 툴팁 텍스트를 추가
				combBus.setToolTipText("검색하고 싶은 분류를 선택하세요");
				p1.add(combBus);

				// 검색어를 입력할 텍스트필드 설정
				busTf = new JTextField(20);
				busTf.setToolTipText("검색하고 싶은 내용을 입력하세요.");
				p1.add(busTf);

				// 검색,취소 버튼
				p2 = new JPanel(new FlowLayout());

				for (int i = 0; i < btn.length; i++) {
					btn[i] = new JButton(btnString[i]);
					p2.add(btn[i]);
					btn[i].addActionListener(new busMgtAction());
				}

				c.add(p1, BorderLayout.NORTH);
				c.add(p2, BorderLayout.SOUTH);

				setResizable(false); // container의 크기 변경 막기.
				setLocationRelativeTo(null); // container를 중앙에 배치.

				setVisible(true);
			}

			class busMgtAction implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == btn[0]) { // 검색 버튼.
						busSearch(); // 버스를 검색.
					} else if (e.getSource() == btn[1]) { // 취소 버튼.
						setVisible(false);
					}
				}

			}

			void busSearch() { // 버스 검색 메소드.
				t.makeConnection();
				String sql = "";
				sql = "SELECT * FROM bus ";

				try {
					switch (combBus.getSelectedItem().toString()) { // 버스 조건에 맞게 검색하는 sql문.
					case "출발지":
						sql += "WHERE start LIKE '%" + busTf.getText() + "%' ORDER BY start";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						busModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								busModel.addRow(new Object[] { t.rs.getString("busID"), t.rs.getString("busN"),
										t.rs.getString("start"), t.rs.getString("finish"), t.rs.getString("retime"),
										t.rs.getString("rating"), t.rs.getString("seatn"), t.rs.getString("seatR"),
										t.rs.getString("date"), t.rs.getString("startT"), });
								busLists.add(t.rs.getString("busN"));
							}
						}

						setVisible(false);
						resetField();
						button[0].setEnabled(true);
						button[1].setEnabled(false);
						button[2].setEnabled(false);
						break;

					case "도착지":
						sql += "WHERE finish LIKE '%" + busTf.getText() + "%' ORDER BY finish";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						busModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								busModel.addRow(new Object[] { t.rs.getString("busID"), t.rs.getString("busN"),
										t.rs.getString("start"), t.rs.getString("finish"), t.rs.getString("retime"),
										t.rs.getString("rating"), t.rs.getString("seatn"), t.rs.getString("seatR"),
										t.rs.getString("date"), t.rs.getString("startT"), });
							}
						}

						setVisible(false);
						resetField();
						button[0].setEnabled(true);
						button[1].setEnabled(false);
						button[2].setEnabled(false);
						break;

					case "버스 번호":
						sql += "WHERE busN LIKE '%" + busTf.getText() + "%' ORDER BY busN";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						busModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								busModel.addRow(new Object[] { t.rs.getString("busID"), t.rs.getString("busN"),
										t.rs.getString("start"), t.rs.getString("finish"), t.rs.getString("retime"),
										t.rs.getString("rating"), t.rs.getString("seatn"), t.rs.getString("seatR"),
										t.rs.getString("date"), t.rs.getString("startT"), });
							}
						}

						setVisible(false);
						resetField();
						button[0].setEnabled(true);
						button[1].setEnabled(false);
						button[2].setEnabled(false);
						break;

					case "차량 번호":
						sql += "WHERE busID LIKE '%" + busTf.getText() + "%' ORDER BY busID";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						busModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								busModel.addRow(new Object[] { t.rs.getString("busID"), t.rs.getString("busN"),
										t.rs.getString("start"), t.rs.getString("finish"), t.rs.getString("retime"),
										t.rs.getString("rating"), t.rs.getString("seatn"), t.rs.getString("seatR"),
										t.rs.getString("date"), t.rs.getString("startT"), });
							}
						}

						setVisible(false);
						resetField();
						button[0].setEnabled(true);
						button[1].setEnabled(false);
						button[2].setEnabled(false);
						break;

					case "등급":
						sql += "WHERE rating LIKE '%" + busTf.getText() + "%' ORDER BY rating";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						busModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								busModel.addRow(new Object[] { t.rs.getString("busID"), t.rs.getString("busN"),
										t.rs.getString("start"), t.rs.getString("finish"), t.rs.getString("retime"),
										t.rs.getString("rating"), t.rs.getString("seatn"), t.rs.getString("seatR"),
										t.rs.getString("date"), t.rs.getString("startT"), });
							}
						}

						setVisible(false);
						resetField();
						button[0].setEnabled(true);
						button[1].setEnabled(false);
						button[2].setEnabled(false);
						break;

					case "날짜":
						sql += "WHERE date LIKE '%" + busTf.getText() + "%' ORDER BY date";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						busModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								busModel.addRow(new Object[] { t.rs.getString("busID"), t.rs.getString("busN"),
										t.rs.getString("start"), t.rs.getString("finish"), t.rs.getString("retime"),
										t.rs.getString("rating"), t.rs.getString("seatn"), t.rs.getString("seatR"),
										t.rs.getString("date"), t.rs.getString("startT"), });
							}
						}

						setVisible(false);
						resetField();
						button[0].setEnabled(true);
						button[1].setEnabled(false);
						button[2].setEnabled(false);
						break;

					case "출발 시간":
						sql += "WHERE startT LIKE '%" + busTf.getText() + "%' ORDER BY startT";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						busModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								busModel.addRow(new Object[] { t.rs.getString("busID"), t.rs.getString("busN"),
										t.rs.getString("start"), t.rs.getString("finish"), t.rs.getString("retime"),
										t.rs.getString("rating"), t.rs.getString("seatn"), t.rs.getString("seatR"),
										t.rs.getString("date"), t.rs.getString("startT"), });
							}
						}

						setVisible(false);
						resetField();
						button[0].setEnabled(true);
						button[1].setEnabled(false);
						button[2].setEnabled(false);
						break;

					case "소요 시간":
						sql += "WHERE retime LIKE '%" + busTf.getText() + "%' ORDER BY retime";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						busModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								busModel.addRow(new Object[] { t.rs.getString("busID"), t.rs.getString("busN"),
										t.rs.getString("start"), t.rs.getString("finish"), t.rs.getString("retime"),
										t.rs.getString("rating"), t.rs.getString("seatn"), t.rs.getString("seatR"),
										t.rs.getString("date"), t.rs.getString("startT"), });
							}
						}

						setVisible(false);
						resetField();
						button[0].setEnabled(true);
						button[1].setEnabled(false);
						button[2].setEnabled(false);
						break;
					}

				} catch (SQLException sqle) {
					System.out.println(sqle.getMessage());
				}
				t.disConnection();
			}

		}

	}

	// 고객 관리 창
	class UserMgtPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		JLabel[] label = new JLabel[6];
		JTextField[] tf = new JTextField[6];
		JButton[] button = new JButton[4];
		JScrollPane jscp = new JScrollPane();
		String userID;
		String userPW;
		String userBirth;
		String userName;
		String userGender;
		String userPhone;

		// 고객 정보 테이블 기본 설정
		String userColName[] = { "ID", "PW", "생년월일", "이름", "성별", "전화번호" };
		DefaultTableModel userModel = new DefaultTableModel(userColName, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int i, int c) {
				return false;
			}
		};

		JTable userTable = new JTable(userModel);

		DefaultTableCellRenderer ca = new DefaultTableCellRenderer();

		UserMgtPanel() { // 고객 관리 창
			newComponents();
			setComponents();
			addComponents();
		}

		void newComponents() {
			String[] lName = { "ID", "PW", "생년월일", "이름", "성별", "전화번호" }; // 라벨 배열
			String[] bName = { "수정", "삭제", "초기화", "검색" }; // 버튼 배열

			// 라벨 배치
			for (int i = 0; i < lName.length; i++) {
				label[i] = new JLabel(lName[i]);
			}

			// 버튼 배치
			for (int i = 0; i < bName.length; i++) {
				button[i] = new JButton(bName[i]);
			}

			// 텍스트필드 배치 
			for (int i = 0; i < tf.length; i++) {
				tf[i] = new JTextField(5);
			}

			// Table의 크기를 설정 및 수정 불가.
			userTable.setPreferredScrollableViewportSize(new Dimension(550, 118));
			userTable.getTableHeader().setReorderingAllowed(false);

			// Table의 열 길이 설정
			userTable.getColumnModel().getColumn(0).setPreferredWidth(60);
			userTable.getColumnModel().getColumn(1).setPreferredWidth(60);
			userTable.getColumnModel().getColumn(2).setPreferredWidth(50);
			userTable.getColumnModel().getColumn(3).setPreferredWidth(70);
			userTable.getColumnModel().getColumn(4).setPreferredWidth(50);
			userTable.getColumnModel().getColumn(5).setPreferredWidth(70);

			// 렌더러의 가로정렬을 CENTER로
			ca.setHorizontalAlignment(SwingConstants.CENTER);

			// 정렬할 테이블의 컬럼모델을 가져옴
			TableColumnModel tcm = userTable.getColumnModel();

			// 모든 열에 ca 설정을 추가
			tcm.getColumn(0).setCellRenderer(ca);
			tcm.getColumn(1).setCellRenderer(ca);
			tcm.getColumn(2).setCellRenderer(ca);
			tcm.getColumn(3).setCellRenderer(ca);
			tcm.getColumn(4).setCellRenderer(ca);
			tcm.getColumn(5).setCellRenderer(ca);

			jscp = new JScrollPane(userTable);
		}

		void setComponents() {
			setLayout(null);

			for (int i = 0; i < button.length; i++) {
				button[i].addActionListener(new userMgtAction());
			}

			// 각 Label, TextField, Table, BUtton의 좌표 지정.
			// ID.
			label[0].setBounds(80, 20, 50, 30);
			tf[0].setBounds(110, 25, 90, 20);

			// PW
			label[1].setBounds(240, 20, 50, 30);
			tf[1].setBounds(270, 25, 90, 20);

			// 생년월일
			label[2].setBounds(400, 20, 50, 30);
			tf[2].setBounds(460, 25, 90, 20);

			// 이름
			label[3].setBounds(80, 50, 50, 30);
			tf[3].setBounds(110, 55, 90, 20);

			// 성별
			label[4].setBounds(240, 50, 50, 30);
			tf[4].setBounds(270, 55, 90, 20);

			// 전화번호
			label[5].setBounds(400, 50, 50, 30);
			tf[5].setBounds(460, 55, 90, 20);

			button[0].setBounds(175, 98, 80, 30); // 추가 버튼
			button[1].setBounds(275, 98, 80, 30); // 수정 버튼
			button[2].setBounds(375, 98, 80, 30); // 삭제 버튼

			jscp.setBounds(35, 150, 550, 150); // 테이블

			button[3].setBounds(35, 315, 550, 30); // 검색 버튼

			button[0].setEnabled(false); // 수정 button 막기
			button[1].setEnabled(false); // 삭제 button 막기

			userTable.getTableHeader().setResizingAllowed(false); // 열 크기 조절 불가.

			setResizable(false); // container의 크기 변경 막기.
			setLocationRelativeTo(null); // container를 중앙에 배치.

			addWindowListener(new WindowHandler());
			userTable.addMouseListener(new MouseHandler());
		}

		void addComponents() {
			add(label[0]);
			add(tf[0]);

			add(label[1]);
			add(tf[1]);

			add(label[2]);
			add(tf[2]);

			add(label[3]);
			add(tf[3]);

			add(label[4]);
			add(tf[4]);

			add(label[5]);
			add(tf[5]);

			for (int i = 0; i < 3; i++) {
				add(button[i]);
			}

			add(jscp);
			add(button[3]);

		}

		// 수정, 삭제, 초기화, 검색 버튼 이벤트 
		class userMgtAction implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == button[0]) { // 수정 버튼.
					updateUserInfo(); // 고객 정보 수정 메소드.

					resetUserInfoView(); // 초기화 버튼 누르면 필드를 리셋하는 메소드.
					for (int i = 0; i < 2; i++) {
						button[i].setEnabled(false);
					}
				} else if (e.getSource() == button[1]) { // 삭제 버튼.
					deleteUserInfo(); // 고객 정보 삭제 메소드.

					resetUserInfoView(); // 초기화 버튼 누르면 필드를 리셋하는 메소드.
					for (int i = 0; i < 2; i++) {
						button[i].setEnabled(false);
					}
				} else if (e.getSource() == button[2]) { // 초기화 버튼.
					resetUserInfoView(); // 초기화 버튼 누르면 필드를 리셋하는 메소드.
					userModel.setNumRows(0); // DB의 0번 행부터 불러옴.
					UserViewInfo(); // DB의 내용을 가져오는 메소드.

					for (int i = 0; i < 2; i++) {
						button[i].setEnabled(false);
					}
				} else if (e.getSource() == button[3]) { // 검색 버튼.
					new UserSearchView(); // 고객 검색 창.
					resetUserInfoView();

				}
			}

		}

		// 마우스가 클릭되는 이벤트를 처리할 MouseHandler.
		class MouseHandler extends MouseAdapter {

			@Override
			public void mouseClicked(MouseEvent e) {
				int row = userTable.getSelectedRow();

				TableModel data = userTable.getModel();

				// Table에서 마우스로 클릭한 행의 각 셀들을 불러옴.
				userID = (String) data.getValueAt(row, 0);
				userPW = (String) data.getValueAt(row, 1);
				userBirth = (String) data.getValueAt(row, 2);
				userName = (String) data.getValueAt(row, 3);
				userGender = (String) data.getValueAt(row, 4);
				userPhone = (String) data.getValueAt(row, 5);

				// 불러온 셀 값들을 맞는 위치의 TextField나 ComboBox를 변경.
				tf[0].setText(userID);
				tf[1].setText(userPW);
				tf[2].setText(userBirth);
				tf[3].setText(userName);
				tf[4].setText(userGender);
				tf[5].setText(userPhone);

				for (int i = 0; i < 3; i++) {
					button[i].setEnabled(true);
				}
			}
		}

		// 창 실행 시 실행하게 해줄 WindowListener. (WindowHandler)
		class WindowHandler extends WindowAdapter {
			public void windowOpened(WindowEvent e) {
				UserViewInfo(); // DB의 내용을 가져오는 메소드.
			}
		}

		void resetUserInfoView() { // 초기화 버튼 누르면 필드를 리셋하는 메소드.
			tf[0].setText("");
			tf[1].setText("");
			tf[2].setText("");
			tf[3].setText("");
			tf[4].setText("");
			tf[5].setText("");
		}

		void UserViewInfo() { // 버튼 기능 수행 후, DB의 내용을 다시 불러오는 메소드.
			t.makeConnection(); // DB에 연결하는 메소드.

			String sql = ""; // SQL문 초기화.
			sql = "SELECT * FROM member";

			try {
				t.rs = t.stmt.executeQuery(sql);

				// 다음 줄이 없을 때 까지 반복.
				while (t.rs.next()) {
					userModel.addRow(new Object[] { t.rs.getString("MID"), t.rs.getString("PW"), t.rs.getString("name"),
							t.rs.getString("birth"), t.rs.getString("gender"), t.rs.getString("phone") });
				}
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
			t.disConnection(); // DB 연결 종료 메소드.
		}

		void updateUserInfo() { // 고객 정보 수정 메소드.
			t.makeConnection();
			String sql = "";

			try {
				sql = "UPDATE member set MID = '" + tf[0].getText() + "', PW = '" + tf[1].getText() + "', name = '"
						+ tf[2].getText() + "', birth = '" + tf[3].getText() + "', gender = '" + tf[4].getText()
						+ "', phone = '" + tf[5].getText() + "'";
				System.out.println(sql);
				t.stmt.executeUpdate(sql);

				userModel.setNumRows(0);
				UserViewInfo(); // 버튼 기능 수행 후, DB의 내용을 다시 불러오는 메소드.
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
			t.disConnection(); // DB 연결 종료 메소드.
		}

		void deleteUserInfo() { // 고객 정보 삭제 메소드.
			int isDelete = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", null, JOptionPane.YES_NO_OPTION);
			// 삭제 Button 클릭 시, 삭제 경고 창 실행.
			System.out.println("DELETE : " + isDelete);

			if (isDelete == JOptionPane.YES_OPTION) { // YES_OPTION 선택 시,
				t.makeConnection(); // DB에 연결하는 메소드.
				String sql = ""; // SQL문 초기화.

				try {
					sql = "DELETE member, seat FROM member LEFT JOIN seat ON member.MID = seat.MID ";
					sql += "WHERE member.MID = '" + userID + "'";
					System.out.println(sql);
					t.stmt.executeUpdate(sql);

					reCheckNormal(); // 고객의 삭제된 좌석만큼 모든 일반 버스 좌석을 초기화하는 메소드.
					reCheckPremium(); // 고객의 삭제된 좌석만큼 모든 우등 버스 좌석을 초기화하는 메소드.

					userModel.setNumRows(0); // Table의 행을 처음(0)으로 변경.
					UserViewInfo(); // DB의 내용을 가져오는 메소드.
				} catch (SQLException sqle) {
					System.out.println(sqle.getMessage());
				}
				t.disConnection(); // DB 연결 종료 메소드.
			}
		}

		void reCheckNormal() { // 고객의 삭제된 좌석만큼 모든 일반 버스 좌석을 초기화하는 메소드.
			t.makeConnection();
			String sql = "";

			try {
				sql += "SELECT * FROM bus";
				t.rs = t.stmt.executeQuery(sql);

				normalSeat = new NormalBusSeatView();

				while (t.rs.next()) {
					if (t.rs.getString("rating").equals("일반")) {
						ResPanel.busID = t.rs.getString("busID");
						ResPanel.busNumber = t.rs.getInt("busN");

						normalSeat.firstNormalGetSeat();
						normalSeat.normalPurchaseDB();
						normalSeat.normalRemainUpdate();
					}
				}
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
			t.disConnection(); // DB 연결 종료 메소드.
		}

		void reCheckPremium() { // 고객의 삭제된 좌석만큼 모든 우등 버스 좌석을 초기화하는 메소드.
			t.makeConnection();
			String sql = "";

			try {
				sql += "SELECT * FROM bus";
				t.rs = t.stmt.executeQuery(sql);

				premiumSeat = new PremiumBusSeatView();

				while (t.rs.next()) {
					if (t.rs.getString("rating").equals("우등")) {
						ResPanel.busID = t.rs.getString("busID");
						ResPanel.busNumber = t.rs.getInt("busN");

						premiumSeat.firstPremiumGetSeat();
						premiumSeat.premiumPurchaseDB();
						premiumSeat.premiumRemainUpdate();
					}

				}
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
			t.disConnection(); // DB 연결 종료 메소드.
		}

		//고객 정보 검색 창의 역할을 수행할 Frame.
		class UserSearchView extends JFrame {
			private static final long serialVersionUID = 1L;

			// userSearchView의 변수 선언
			Container c = getContentPane();
			JPanel p1, p2;
			JComboBox<String> combUser;
			String[] userInfo = { "ID", "PW", "생년월일", "이름", "성별", "전화번호" };
			JTextField tf;
			JButton btn[] = new JButton[2];
			String[] btnString = { "검색", "취소" };

			// userSearchView의 기본 설정
			UserSearchView() {
				setTitle("고객 정보 검색");
				setSize(320, 105);

				// 리스트와 검색어 텍스트필드 추가
				p1 = new JPanel(new FlowLayout());
				combUser = new JComboBox<String>(userInfo);

				// 콤보박스에 툴팁텍스트를 추가
				combUser.setToolTipText("검색하고 싶은 분류를 선택하세요");
				p1.add(combUser);

				// 검색어를 입력할 텍스트필드 설정
				tf = new JTextField(20);
				tf.setToolTipText("검색하고 싶은 내용을 입력하세요.");
				p1.add(tf);

				// 검색 버튼
				p2 = new JPanel(new FlowLayout());

				for (int i = 0; i < btn.length; i++) {
					btn[i] = new JButton(btnString[i]);
					btn[i].addActionListener(new userSearchAction()); // 버튼에 Listener 추가.
					p2.add(btn[i]);
				}

				c.add(p1, BorderLayout.NORTH);
				c.add(p2, BorderLayout.SOUTH);

				setResizable(false); // container의 크기 변경 막기.
				setLocationRelativeTo(null); // container를 중앙에 배치.

				setVisible(true);
			}

			class userSearchAction implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == btn[0]) { // 검색 버튼 클릭 시,
						searchUserInfo(); // 사용자 정보 검색 메소드.
					} else if (e.getSource() == btn[1]) { // 창에서 돌아가기 버튼 클릭 시,

						setVisible(false); // 창이 닫힘.
					}
				}

			}

			void searchUserInfo() { // 사용자 정보 검색 메소드.
				t.makeConnection();
				String sql = "SELECT * FROM member ";

				try {
					switch (combUser.getSelectedItem().toString()) { // 사용자 조건에 맞게 검색하는 sql문.
					case "ID":
						sql += "WHERE MID LIKE '%" + tf.getText() + "%'";

						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						userModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								userModel.addRow(new Object[] { t.rs.getString("MID"), t.rs.getString("PW"),
										t.rs.getString("name"), t.rs.getString("birth"), t.rs.getString("gender"),
										t.rs.getString("phone") });
							}
						}

						setVisible(false);
						break;

					case "PW":
						sql += "WHERE PW LIKE '%" + tf.getText() + "%'";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						userModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								userModel.addRow(new Object[] { t.rs.getString("MID"), t.rs.getString("PW"),
										t.rs.getString("name"), t.rs.getString("birth"), t.rs.getString("gender"),
										t.rs.getString("phone") });
							}
						}

						setVisible(false);
						break;

					case "생년월일":
						sql += "WHERE birth LIKE '%" + tf.getText() + "%'";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						userModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								userModel.addRow(new Object[] { t.rs.getString("MID"), t.rs.getString("PW"),
										t.rs.getString("name"), t.rs.getString("birth"), t.rs.getString("gender"),
										t.rs.getString("phone") });
							}
						}

						setVisible(false);
						break;

					case "이름":
						sql += "WHERE name LIKE '%" + tf.getText() + "%'";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						userModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								userModel.addRow(new Object[] { t.rs.getString("MID"), t.rs.getString("PW"),
										t.rs.getString("name"), t.rs.getString("birth"), t.rs.getString("gender"),
										t.rs.getString("phone") });
							}
						}

						setVisible(false);
						break;

					case "성별":
						sql += "WHERE gender LIKE '%" + tf.getText() + "%'";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						userModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								userModel.addRow(new Object[] { t.rs.getString("MID"), t.rs.getString("PW"),
										t.rs.getString("name"), t.rs.getString("birth"), t.rs.getString("gender"),
										t.rs.getString("phone") });
							}
						}

						setVisible(false);
						break;

					case "전화번호":
						sql += "WHERE phone LIKE '%" + tf.getText() + "%'";
						System.out.println(sql);
						t.rs = t.stmt.executeQuery(sql);

						userModel.setNumRows(0);

						if (t.rs.isBeforeFirst() == false) {
							JOptionPane.showMessageDialog(null, "검색에 실패했습니다!\n값을 다시 확인해주세요!", "검색 오류",
									JOptionPane.ERROR_MESSAGE);
						} else {
							while (t.rs.next()) {
								userModel.addRow(new Object[] { t.rs.getString("MID"), t.rs.getString("PW"),
										t.rs.getString("name"), t.rs.getString("birth"), t.rs.getString("gender"),
										t.rs.getString("phone") });
							}
						}

						setVisible(false);
						break;
					}
				} catch (SQLException sqle) {
					System.out.println(sqle.getMessage());
				}
				t.disConnection(); // DB 연결 종료 메소드.
			}
		}

	}

	class LogoutAdminPanel extends JPanel { // 로그아웃 창.
		private static final long serialVersionUID = 1L;

		JButton logoutBtn = new JButton("로그아웃");

		LogoutAdminPanel() { // 로그아웃 창 생성자.
			setLayout(null);
			logoutBtn.setBounds(210, 280, 200, 50);
			logoutBtn.addActionListener(new LogoutAdminAction());
			logoutBtn.setFont(new Font("", Font.BOLD, 30));
			add(logoutBtn, BorderLayout.CENTER);
		}

		class LogoutAdminAction implements ActionListener { // 로그아웃 창에 적용할 ActionListener.

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == logoutBtn) { // 로그아웃 Button 클릭 시,
					int isCancel = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?", "Confirm",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); // 로그아웃 경고창 실행.

					if (isCancel == JOptionPane.YES_OPTION) { // 로그아웃 경고창에서 YES 클릭시,
						btnAction(); // 로그아웃 Button 클릭 시, 창 종료 메소드.
						new LoginView(); // 로그인 창으로 돌아감.
					}
				}
			}

		}
	}

	private void btnAction() { // 로그아웃 Button 클릭 시, 창 종료 메소드.
		setVisible(false);
	}
}

class NormalBusSeatView extends JFrame { // 버스 좌석 선택 창.
	private static final long serialVersionUID = 1L;

	TeamBusSeat t = new TeamBusSeat();

	JPanel[] pPanel = new JPanel[5]; // Panel에 추가하기 전에 먼저 합칠 Panel.
	JPanel[] panel = new JPanel[5]; // pPanel들을 추가할 Panel.
	JLabel[] la = new JLabel[3]; // 출발지, >, 목적지 Label.
	JButton[][] seatBtn1 = new JButton[10][2]; // 서쪽 좌석 Panel에 붙일 Button. (2차원)
	JButton[][] seatBtn2 = new JButton[10][2]; // 동쪽 좌석 Panel에 붙일 Button. (2차원)
	JButton[] seatBtn3 = new JButton[5]; // 남쪽 좌석 Panel에 붙일 Button.
	JButton[] btn = new JButton[3]; // 결제, 초기화, 취소 Button.
	JLabel[] infoLa = new JLabel[5]; // 일반, 청소년, 어린이, 노인 Label.
	JLabel[] priceLa = new JLabel[2]; // 가격 Label.
	JTextField priceTf = new JTextField(10); // 가격 입력가능한 TextField.
	int personCount; // 사람 명 수.
	int priceSum = 0; // 총 가격.
	int btnCount = 1; // 눌러진 버튼의 수.
	ArrayList<String> normalColList = new ArrayList<>();
	int sqlRow = 0;
	int seatR;

	String[] desLa = { "출발지", "  >  ", "목적지" }; // 북쪽에 배치할 Label에 들어갈 문자열.
	String[] seat = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }; // 좌석 열에 해당하는 문자열.
	String[] personLa = { "일반", "청소년", "어린이", "노인" }; // 승객 정보에 해당하는 문자열.
	String[] priceString = { "금액", "원" }; // 금액 정보에 들어갈 문자열.
	String[] btnString = { "결제하기", "초기화", "취소하기" }; // Button에 들어갈 문자열.

	// JSpinner.
	SpinnerNumberModel[] genPerson = new SpinnerNumberModel[4]; // 일반, 청소년, 어린이, 노인 으로 총 4개.
	JSpinner[] spinnerInteger = new JSpinner[4]; // 각 Spinner 들은 Int형으로 만듦.

	NormalBusSeatView() { // BusSeat 생성자.
		setTitle("버스 좌석 예매");
		setSize(515, 550);

		addWindowListener(new FirstNormalWindow());

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
			pPanel[i] = new JPanel(new GridLayout(10, 2, 5, 5)); // GridLayout으로 설정.
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

		for (int i = 0; i < seatBtn3.length; i++) { // 남쪽 (마지막 행) 좌석 초기화.
			seatBtn3[i] = new JButton(seat[10] + Integer.toString(i + 1));
			pPanel[2].add(seatBtn3[i]);
		}

		// 동쪽 - 정보.
		// 사람 정보.
		pPanel[3] = new JPanel(new GridLayout(4, 2, 20, 30));

		for (int i = 0; i < personLa.length; i++) { // personLa.length = 4.
			infoLa[i] = new JLabel(personLa[i]); // 정보에 맞는 Label 초기화.
			genPerson[i] = new SpinnerNumberModel(0, 0, 45, 1); // Spinner를 초기값 0, 최소값 0, 최댓값 45, 변경값 1로 초기화.
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
		panel[4] = new JPanel(new GridLayout(1, 3, 10, 10));

		for (int i = 0; i < btn.length; i++) { // btn.length = 3.
			btn[i] = new JButton(btnString[i]); // 결제, 취소 Button 생성.
		}
	}

	void setComponents() { // 멤버들의 추가 설정을 추가하는 메소드.
		setLayout(null);

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

		priceTf.setHorizontalAlignment(JTextField.RIGHT); // 가격 TextField를 오른쪽 정렬.
		priceTf.setEditable(false);
		priceTf.setBackground(Color.white);

		// 패널들의 좌표.
		panel[0].setBounds(45, 5, 400, 20); // ( 출발지 -> 도착지) 라벨.
		pPanel[0].setBounds(8, 30, 105, 390); // 좌석의 서쪽.
		pPanel[1].setBounds(173, 30, 105, 390); // 좌석의 동쪽.
		pPanel[2].setBounds(8, 421, 270, 36); // 좌석의 남쪽.
		panel[3].setBounds(290, 50, 200, 370); // 사람 정보, 금액 정보
		panel[4].setBounds(110, 470, 280, 40); // 결제, 초기화, 취소 버튼

		setResizable(false); // container의 크기 변경 막기.
		setLocationRelativeTo(null); // container를 중앙에 배치.
	}

	void addComponents() { // 구성된 멤버들을 Panel에 부착하는 메소드.
		// 북쪽 - Label.
		for (int i = 0; i < desLa.length; i++) { // desLa.length = 3.
			panel[0].add(la[i]);
		}

		add(panel[0]); // ( 출발지 -> 도착지) 라벨.

		// 서쪽 - 좌석.
		add(pPanel[0]); // 좌석의 서쪽.
		add(pPanel[1]); // 좌석의 동쪽.
		add(pPanel[2]); // 좌석의 남쪽.

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

		panel[3].add(pPanel[3]);
		panel[3].add(pPanel[4]);

		add(panel[3]); // 사람 정보, 금액 정보

		// 남쪽 - Button.
		for (int i = 0; i < btn.length; i++) {
			panel[4].add(btn[i]);
		}

		add(panel[4]); // 결제, 취소 버튼
	}

	class FirstNormalWindow extends WindowAdapter {
		public void windowOpened(WindowEvent e) {
			firstNormalGetSeat(); // 처음 좌석 확인.
		}
	}

	class BusSeatAction implements ActionListener { // Action을 추가해줄 ActionListener.

		@Override
		public void actionPerformed(ActionEvent e) { // 금액 TextField에 ActionListener 추가.
			for (int i = 0; i < 10; i++) { // 서쪽 Button들에 ActionListener 추가.
				for (int j = 0; j < 2; j++) {
					if (e.getSource() == seatBtn1[i][j]) {
						if (seatBtn1[i][j].getBackground().equals(Color.red)) { // 빨간색이면,
							seatBtn1[i][j].setBackground(null); // 기본 색상으로 변경.
							normalColList.remove(seat[i] + Integer.toString(j + 1));
							btnCount--;
						} else { // 기본 색상이라면,
							if (maxCount() == true) {
								seatBtn1[i][j].setBackground(Color.red); // 빨간색으로 변경.
								normalColList.add(seat[i] + Integer.toString(j + 1));
								btnCount++;
							} else {
								JOptionPane.showMessageDialog(null, "사람 수를 다시 확인해주세요!", "선택 오류",
										JOptionPane.WARNING_MESSAGE);
							}
						}
					}
				}
			}

			for (int i = 0; i < 10; i++) { // 동쪽 Button들에 ActionListener 추가.
				for (int j = 0; j < 2; j++) {
					if (e.getSource() == seatBtn2[i][j]) {
						if (seatBtn2[i][j].getBackground().equals(Color.red)) { // 빨간색이면,
							seatBtn2[i][j].setBackground(null); // 기본 색상으로 변경.
							normalColList.remove(seat[i] + Integer.toString(j + 3));
							btnCount--;
						} else { // 기본 색상이라면,
							if (maxCount() == true) {
								seatBtn2[i][j].setBackground(Color.red); // 빨간색으로 변경.
								normalColList.add(seat[i] + Integer.toString(j + 3));
								btnCount++;
							} else {
								JOptionPane.showMessageDialog(null, "사람 수를 다시 확인해주세요!", "선택 오류",
										JOptionPane.WARNING_MESSAGE);
							}
						}

					}
				}

			}

			for (int i = 0; i < seatBtn3.length; i++) { // 남쪽 Button들에 ActionListener 추가.
				if (e.getSource() == seatBtn3[i]) {
					if (seatBtn3[i].getBackground().equals(Color.red)) { // 빨간색이면,
						seatBtn3[i].setBackground(null); // 기본 색상으로 변경.
						normalColList.remove(seat[10] + Integer.toString(i + 1));
						btnCount--;
					} else { // 기본 색상이라면,
						if (maxCount() == true) {
							seatBtn3[i].setBackground(Color.red); // 빨간색으로 변경.
							normalColList.add(seat[10] + Integer.toString(i + 1));
							btnCount++;
						} else {
							JOptionPane.showMessageDialog(null, "사람 수를 다시 확인해주세요!", "선택 오류",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}

			if (e.getSource() == btn[0]) { // 결제하기 Buttondp ActionListener 추가.
				if ((btnCount == personCount + 1) && (btnCount == 1 && personCount == 0)) { // 0이라면,
					JOptionPane.showMessageDialog(null, "선택된 좌석이 없습니다!", "결제 오류", JOptionPane.WARNING_MESSAGE);
				} else if (btnCount == personCount + 1) {// 수가 같다면,
					int result = JOptionPane.showConfirmDialog(null, "결제하시겠습니까?", "결제", JOptionPane.YES_NO_OPTION);

					if (result == JOptionPane.YES_OPTION) {
						JOptionPane.showMessageDialog(null, "결제가 완료되었습니다!", "결제 오류", JOptionPane.PLAIN_MESSAGE);
						normalPurchaseDB(); // 결제한 내역을 DB에 저장.
						normalRemainUpdate();
					}
				} else { // 그 외,
					JOptionPane.showMessageDialog(null, "좌석과 사람 수가 다릅니다!", "결제 오류", JOptionPane.WARNING_MESSAGE);
				}
			}

			if (e.getSource() == btn[1]) { // 초기화 Button에 ActionListener 추가.
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 2; j++) {
						if (seatBtn1[i][j].isEnabled() == true && seatBtn1[i][j].getBackground().equals(Color.red)) { // 빨간색이면,
							seatBtn1[i][j].setBackground(null); // 기본 색상으로 변경.
						}
					}
				}

				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 2; j++) {
						if (seatBtn2[i][j].isEnabled() == true && seatBtn2[i][j].getBackground().equals(Color.red)) { // 빨간색이면,
							seatBtn2[i][j].setBackground(null); // 기본 색상으로 변경.
						}
					}
				}

				for (int i = 0; i < seatBtn3.length; i++) {
					if (seatBtn3[i].isEnabled() == true && seatBtn3[i].getBackground().equals(Color.red)) { // 빨간색이면,
						seatBtn3[i].setBackground(null); // 기본 색상으로 변경.
					}
				}

				for (int i = 0; i < spinnerInteger.length; i++) {
					spinnerInteger[i].setValue(0);
				}

				btnCount = 1;
			}

			if (e.getSource() == btn[2]) { // 취소 Button에 ActionListener 추가.
				setVisible(false);
			}
		}

	}

	class BusSeatChange implements ChangeListener { // 스피너 바뀔 때마다 값을 불러옴. 금액 계산파트 추가.

		@Override
		public void stateChanged(ChangeEvent e) {
			int[] count = new int[4];
			int[] price = new int[4];
			personCount = 0;
			priceSum = 0;

			// 사람 수 계산.
			for (int i = 0; i < count.length; i++) {
				count[i] = (int) spinnerInteger[i].getValue();
			}

			for (int i = 0; i < count.length; i++) {
				personCount += count[i];
			}

			System.out.println("personCount : " + personCount);

			if (personCount < 46) { // 스피너 총 합이 45까지
				// 0번 = 일반(4만원), 1번 = 청소년(3만원), 2번 = 어린이(2만원), 3번 = 노인(3만원)
				price[0] = count[0] * 20000;
				price[1] = count[1] * 15000;
				price[2] = count[2] * 10000;
				price[3] = count[3] * 15000;

				// 총 가격 계산.
				for (int i = 0; i < price.length; i++) {
					priceSum += price[i];
				}

				priceTf.setText(Integer.toString(priceSum)); // 총 가격을 TextField에 출력.
			} else { // 스피너 총 합이 46부터
				JOptionPane.showMessageDialog(null, "최대 인원 수를 넘었습니다!", "선택 오류", JOptionPane.WARNING_MESSAGE);
				if (e.getSource() == spinnerInteger[0]) {
					if (personCount > 45) {
						spinnerInteger[0].setValue((int) spinnerInteger[0].getValue() - 1);
					}
				} else if (e.getSource() == spinnerInteger[1]) {
					if (personCount > 45) {
						spinnerInteger[1].setValue((int) spinnerInteger[1].getValue() - 1);
					}
				} else if (e.getSource() == spinnerInteger[2]) {
					if (personCount > 45) {
						spinnerInteger[2].setValue((int) spinnerInteger[2].getValue() - 1);
					}
				} else if (e.getSource() == spinnerInteger[3]) {
					if (personCount > 45) {
						spinnerInteger[3].setValue((int) spinnerInteger[3].getValue() - 1);
					}
				}
			}
		}

	}

	void firstNormalGetSeat() { // 처음 좌석 확인.
		t.makeConnection();
		String sql = "";

		sql += "SELECT col FROM seat ";

		try {
			sql += "WHERE busID = '" + ResPanel.busID + "' AND busN = '" + ResPanel.busNumber + "'";
			t.rs = t.stmt.executeQuery(sql);

			System.out.println(sql);

			while (t.rs.next()) {
				System.out.println(t.rs.getString("col"));

				for (int i = 0; i < 10; i++) { // 서쪽 좌석.
					for (int j = 0; j < 2; j++) {
						if (seatBtn1[i][j].getText().equals(t.rs.getString("col"))) {
							seatBtn1[i][j].setEnabled(false);
							seatBtn1[i][j].setBackground(Color.RED);
						}
					}
				}

				for (int i = 0; i < 10; i++) { // 동쪽 좌석.
					for (int j = 0; j < 2; j++) {
						if (seatBtn2[i][j].getText().equals(t.rs.getString("col"))) {
							seatBtn2[i][j].setEnabled(false);
							seatBtn2[i][j].setBackground(Color.RED);
						}
					}
				}

				for (int i = 0; i < seatBtn3.length; i++) { // 남쪽 좌석.
					if (seatBtn3[i].getText().equals(t.rs.getString("col"))) {
						seatBtn3[i].setEnabled(false);
						seatBtn3[i].setBackground(Color.RED);
					}
				}
				seatR++;
			}

			sql = "";
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		t.disConnection();
	}

	boolean maxCount() { // 버튼은 사람의 수를 넘길 수 없다.
		System.out.println("btnCount : " + btnCount);
		System.out.println("personCount : " + personCount);

		if (btnCount <= personCount) { // 버튼의 수가 사람 수보다 적다면
			return true;
		} else {
			return false;
		}
	}

	void normalPurchaseDB() { // 결제한 내역을 DB(seat)에 저장.
		t.makeConnection();

		String sql = "";

		System.out.println("버스 번호 : " + ResPanel.busNumber);
		System.out.println("버스 ID : " + ResPanel.busID);
		for (int i = 0; i < normalColList.size(); i++) {
			System.out.println("열 번호 : " + normalColList.get(0));
		}
		System.out.println("아이디 : " + LoginView.id);

		try {
			for (int i = 0; i < normalColList.size(); i++) {
				sql = "INSERT INTO seat (busID, busN, col, MID) values ";
				sql += "('" + ResPanel.busID + "', '" + ResPanel.busNumber + "', '" + normalColList.get(i) + "', '"
						+ LoginView.id + "')";
				System.out.println(sql);

				t.stmt.executeUpdate(sql);

				sql = "";
				seatR++;
			}

			setVisible(false);
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		t.disConnection();
	}

	void normalRemainUpdate() { // 잔여석 업데이트.
		t.makeConnection();
		String sql = "";

		try {
			sql = "UPDATE bus set seatR = '" + (45 - seatR) + "' WHERE busID = '" + ResPanel.busID + "'AND busN = '"
					+ ResPanel.busNumber + "'";

			t.stmt.execute(sql);
			seatR = 0;
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		t.disConnection();
	}
}

class PremiumBusSeatView extends JFrame { // 우등 버스 좌석 선택 창.
	private static final long serialVersionUID = 1L;

	TeamBusSeat t = new TeamBusSeat();

	JPanel[] pPanel = new JPanel[5]; // Panel에 추가하기 전에 먼저 합칠 Panel.
	JPanel[] panel = new JPanel[5]; // pPanel들을 추가할 Panel.
	JLabel[] la = new JLabel[3]; // 출발지, >, 목적지 Label.
	JButton[][] seatBtn1 = new JButton[8][2]; // 서쪽 좌석 Panel에 붙일 Button. (2차원)
	JButton[][] seatBtn2 = new JButton[8][1]; // 동쪽 좌석 Panel에 붙일 Button. (2차원)
	JButton[] seatBtn3 = new JButton[4]; // 남쪽 좌석 Panel에 붙일 Button.
	JButton[] btn = new JButton[3]; // 결제, 초기화, 취소 Button.
	JLabel[] infoLa = new JLabel[5]; // 일반, 청소년, 어린이, 노인 Label.
	JLabel[] priceLa = new JLabel[2]; // 가격 Label.
	JTextField priceTf = new JTextField(10); // 가격 입력가능한 TextField.
	int personCount; // 사람 명 수.
	int priceSum = 0; // 총 가격.
	int btnCount = 1; // 눌러진 버튼의 수.
	ArrayList<String> premiumColList = new ArrayList<>();
	int sqlRow;
	int seatR; // 잔여석.

	String[] desLa = { "출발지", "  >  ", "목적지" }; // 북쪽에 배치할 Label에 들어갈 문자열.
	String[] seat = { "A", "B", "C", "D", "E", "F", "G", "H", "I" }; // 좌석 열에 해당하는 문자열.
	String[] personLa = { "일반", "청소년", "어린이", "노인" }; // 승객 정보에 해당하는 문자열.
	String[] priceString = { "금액", "원" }; // 금액 정보에 들어갈 문자열.
	String[] btnString = { "결제하기", "초기화", "취소하기" }; // Button에 들어갈 문자열.

	// JSpinner.
	SpinnerNumberModel[] genPerson = new SpinnerNumberModel[4]; // 일반, 청소년, 어린이, 노인 으로 총 4개.
	JSpinner[] spinnerInteger = new JSpinner[4]; // 각 Spinner 들은 Int형으로 만듦.

	PremiumBusSeatView() { // BusSeat 생성자.
		setTitle("버스 좌석 예매");
		setSize(500, 500);

		addWindowListener(new FirstPremiumWindow());

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

		// 복도를 사이에 두고, 양 옆으로 배치할 Panel.
		pPanel[0] = new JPanel(new GridLayout(8, 2, 5, 5)); // GridLayout으로 설정.
		pPanel[1] = new JPanel(new GridLayout(8, 1, 5, 5)); // GridLayout으로 설정.

		pPanel[2] = new JPanel(new GridLayout(1, 1, 5, 5));

		for (int i = 0; i < 8; i++) { // 서쪽 좌석 초기화.
			for (int j = 0; j < 2; j++) {
				seatBtn1[i][j] = new JButton(seat[i] + Integer.toString(j + 1));
				pPanel[0].add(seatBtn1[i][j]);
			}
		}

		for (int i = 0; i < 8; i++) { // 동쪽 좌석 초기화.
			for (int j = 0; j < 1; j++) {
				seatBtn2[i][j] = new JButton(seat[i] + Integer.toString(j + 3));
				pPanel[1].add(seatBtn2[i][j]);
			}
		}

		for (int i = 0; i < seatBtn3.length; i++) { // 남쪽 (마지막 행) 좌석 초기화.
			seatBtn3[i] = new JButton(seat[8] + Integer.toString(i + 1));
			pPanel[2].add(seatBtn3[i]);
		}

		// 동쪽 - 정보.
		// 사람 정보.
		pPanel[3] = new JPanel(new GridLayout(4, 2, 20, 30));

		for (int i = 0; i < personLa.length; i++) { // personLa.length = 4.
			infoLa[i] = new JLabel(personLa[i]); // 정보에 맞는 Label 초기화.
			genPerson[i] = new SpinnerNumberModel(0, 0, 28, 1); // Spinner를 초기값 0, 최소값 0, 최댓값 28, 변경값 1로 초기화.
			spinnerInteger[i] = new JSpinner(genPerson[i]); // JSpinner 초기화.
		}

		// 금액 정보.
		pPanel[4] = new JPanel(new FlowLayout());

		for (int i = 0; i < priceString.length; i++) { // priceString.length = 2.
			priceLa[i] = new JLabel(priceString[i]);
		}

		priceTf = new JTextField(10); // 금액 입력 공간을 10으로 초기화.

		panel[3] = new JPanel(new GridLayout(2, 1, 20, 10));

		// 남쪽 - Button.
		panel[4] = new JPanel(new GridLayout(1, 3, 10, 10));

		for (int i = 0; i < btn.length; i++) { // btn.length = 3.
			btn[i] = new JButton(btnString[i]); // 결제, 취소 Button 생성.
		}
	}

	void setComponents() { // 멤버들의 추가 설정을 추가하는 메소드.
		setLayout(null);

		for (int i = 0; i < personLa.length; i++) { // personLa.length = 4.
			infoLa[i].setHorizontalAlignment(JLabel.CENTER); // 사람 정보 Label들을 중앙에 정렬.
		}

		for (int i = 0; i < 8; i++) { // 서쪽 좌석 Panel안의 Button에 ActionListener 추가.
			for (int j = 0; j < 2; j++) {
				seatBtn1[i][j].addActionListener(new BusSeatAction());
			}
		}

		for (int i = 0; i < 8; i++) { // 동쪽 좌석 Panel안의 Button에 ActionListener 추가.
			for (int j = 0; j < 1; j++) {
				seatBtn2[i][j].addActionListener(new BusSeatAction());
			}
		}

		for (int i = 0; i < 4; i++) { // 남쪽 좌석 Panel안의 Button에 ActionListener 추가.
			seatBtn3[i].addActionListener(new BusSeatAction());
		}

		for (int i = 0; i < btn.length; i++) { // 결제, 초기화, 취소 Button에 ActionListener 추가.
			btn[i].addActionListener(new BusSeatAction());
		}

		for (int i = 0; i < personLa.length; i++) {
			spinnerInteger[i].addChangeListener(new BusSeatChange());
		}

		priceTf.setHorizontalAlignment(JTextField.RIGHT); // 가격 TextField를 오른쪽 정렬.
		priceTf.setEditable(false);
		priceTf.setBackground(Color.white);

		// 패널들의 좌표.
		panel[0].setBounds(45, 5, 400, 20); // ( 출발지 -> 도착지) 라벨.
		pPanel[0].setBounds(10, 30, 110, 345); // 좌석의 서쪽.
		pPanel[1].setBounds(183, 30, 53, 345); // 좌석의 동쪽.
		pPanel[2].setBounds(8, 378, 230, 36); // 좌석의 남쪽.
		panel[3].setBounds(270, 50, 200, 370); // 사람 정보, 금액 정보
		panel[4].setBounds(90, 420, 300, 30); // 결제, 취소 버튼

		setResizable(false); // container의 크기 변경 막기.
		setLocationRelativeTo(null); // container를 중앙에 배치.
	}

	void addComponents() { // 구성된 멤버들을 Panel에 부착하는 메소드.
		// 북쪽 - Label.
		for (int i = 0; i < desLa.length; i++) { // desLa.length = 3.
			panel[0].add(la[i]);
		}

		add(panel[0]); // ( 출발지 -> 도착지) 라벨.

		// 서쪽 - 좌석.
		add(pPanel[0]); // 좌석의 서쪽.
		add(pPanel[1]); // 좌석의 동쪽.
		add(pPanel[2]); // 좌석의 남쪽.

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

		panel[3].add(pPanel[3]);
		panel[3].add(pPanel[4]);

		add(panel[3]); // 사람 정보, 금액 정보

		// 남쪽 - Button.
		for (int i = 0; i < btn.length; i++) {
			panel[4].add(btn[i]);
		}

		add(panel[4]); // 결제, 취소 버튼
	}

	class FirstPremiumWindow extends WindowAdapter {
		public void windowOpened(WindowEvent e) {
			firstPremiumGetSeat(); // 처음 좌석 확인.
		}
	}

	class BusSeatAction implements ActionListener { // Action을 추가해줄 ActionListener.

		@Override
		public void actionPerformed(ActionEvent e) {
			// 서쪽 Button들에 ActionListener 추가.
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 2; j++) {
					if (e.getSource() == seatBtn1[i][j]) {
						if (seatBtn1[i][j].getBackground().equals(Color.red)) { // 빨간색이면,
							seatBtn1[i][j].setBackground(null); // 기본 색상으로 변경.
							premiumColList.remove(seat[i] + Integer.toString(j + 1));
							btnCount--;
						} else { // 기본 색상이라면,
							if (maxCount() == true) {
								seatBtn1[i][j].setBackground(Color.red); // 빨간색으로 변경.
								premiumColList.add(seat[i] + Integer.toString(j + 1));
								btnCount++;
							} else {
								JOptionPane.showMessageDialog(null, "사람 수를 다시 확인해주세요!", "선택 오류",
										JOptionPane.WARNING_MESSAGE);
							}
						}
					}
				}
			}

			// 동쪽 Button들에 ActionListener 추가.
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 1; j++) {
					if (e.getSource() == seatBtn2[i][j]) {
						if (seatBtn2[i][j].getBackground().equals(Color.red)) { // 빨간색이면,
							seatBtn2[i][j].setBackground(null); // 기본 색상으로 변경.
							premiumColList.remove(seat[i] + Integer.toString(j + 3));
							btnCount--;
						} else { // 기본 색상이라면,
							if (maxCount() == true) {
								seatBtn2[i][j].setBackground(Color.red); // 빨간색으로 변경.
								premiumColList.add(seat[i] + Integer.toString(j + 3));
								btnCount++;
							} else {
								JOptionPane.showMessageDialog(null, "사람 수를 다시 확인해주세요!", "선택 오류",
										JOptionPane.WARNING_MESSAGE);
							}
						}

					}
				}

			}

			// 남쪽 Button들에 ActionListener 추가.
			for (int i = 0; i < seatBtn3.length; i++) {
				if (e.getSource() == seatBtn3[i]) {
					if (seatBtn3[i].getBackground().equals(Color.red)) { // 빨간색이면,
						seatBtn3[i].setBackground(null); // 기본 색상으로 변경.
						premiumColList.remove(seat[8] + Integer.toString(i + 1));
						btnCount--;
					} else { // 기본 색상이라면,
						if (maxCount() == true) {
							seatBtn3[i].setBackground(Color.red); // 빨간색으로 변경.
							premiumColList.add(seat[8] + Integer.toString(i + 1));
							btnCount++;
						} else {
							JOptionPane.showMessageDialog(null, "사람 수를 다시 확인해주세요!", "선택 오류",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}

			if (e.getSource() == btn[0]) { // 결제하기 Button에 ActionListener 추가.
				if ((btnCount == personCount + 1) && (btnCount == 1 && personCount == 0)) { // 0이라면,
					JOptionPane.showMessageDialog(null, "선택된 좌석이 없습니다!", "결제 오류", JOptionPane.WARNING_MESSAGE);
				} else if (btnCount == personCount + 1) {// 수가 같다면,
					int result = JOptionPane.showConfirmDialog(null, "결제하시겠습니까?", "결제", JOptionPane.YES_NO_OPTION);

					if (result == JOptionPane.YES_OPTION) {
						JOptionPane.showMessageDialog(null, "결제가 완료되었습니다!", "결제 오류", JOptionPane.PLAIN_MESSAGE);
						premiumPurchaseDB(); // 결제한 내역을 DB에 저장.
						premiumRemainUpdate();
					}
				} else { // 그 외,
					JOptionPane.showMessageDialog(null, "좌석과 사람 수가 다릅니다!", "결제 오류", JOptionPane.WARNING_MESSAGE);
				}
			}

			if (e.getSource() == btn[1]) { // 초기화 Button에 ActionListener 추가.
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 2; j++) {
						if (seatBtn1[i][j].isEnabled() == true && seatBtn1[i][j].getBackground().equals(Color.red)) { // 예매 가능한 곳이 빨간색이면,
							seatBtn1[i][j].setBackground(null); // 기본 색상으로 변경.
						}
					}
				}

				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 1; j++) {
						if (seatBtn2[i][j].isEnabled() == true && seatBtn2[i][j].getBackground().equals(Color.red)) { // 예매 가능한 곳이 빨간색이면,
							seatBtn2[i][j].setBackground(null); // 기본 색상으로 변경.
						}
					}
				}

				for (int i = 0; i < seatBtn3.length; i++) {
					if (seatBtn3[i].isEnabled() == true && seatBtn3[i].getBackground().equals(Color.red)) { // 예매 가능한 곳이 빨간색이면,
						seatBtn3[i].setBackground(null); // 기본 색상으로 변경.
					}
				}

				for (int i = 0; i < spinnerInteger.length; i++) {
					spinnerInteger[i].setValue(0);
				}

				btnCount = 1;
			}

			if (e.getSource() == btn[2]) { // 취소 Button에 ActionListener 추가.
				setVisible(false); // 창 종료.
			}
		}

	}

	class BusSeatChange implements ChangeListener { // 스피너 바뀔 때마다 값을 불러옴. 금액 계산파트 추가.

		@Override
		public void stateChanged(ChangeEvent e) {
			int[] count = new int[4];
			int[] price = new int[4];
			personCount = 0;
			priceSum = 0;

			// 사람 수 계산.
			for (int i = 0; i < count.length; i++) {
				count[i] = (int) spinnerInteger[i].getValue();
			}

			for (int i = 0; i < count.length; i++) {
				personCount += count[i];
			}

			System.out.println("count : " + personCount);

			if (personCount < 29) { // 스피너 총 합이 28까지
				// 0번 = 일반(4만원), 1번 = 청소년(3만원), 2번 = 어린이(2만원), 3번 = 노인(3만원)
				price[0] = count[0] * 40000;
				price[1] = count[1] * 30000;
				price[2] = count[2] * 20000;
				price[3] = count[3] * 30000;

				// 총 가격 계산.
				for (int i = 0; i < price.length; i++) {
					priceSum += price[i];
				}

				priceTf.setText(Integer.toString(priceSum)); // 총 가격을 TextField에 출력.
			} else { // 스피너 총 합이 28부터
				JOptionPane.showMessageDialog(null, "최대 인원 수를 넘었습니다!", "선택 오류", JOptionPane.WARNING_MESSAGE);
				if (e.getSource() == spinnerInteger[0]) {
					if (personCount > 28) {
						spinnerInteger[0].setValue((int) spinnerInteger[0].getValue() - 1);
					}
				} else if (e.getSource() == spinnerInteger[1]) {
					if (personCount > 28) {
						spinnerInteger[1].setValue((int) spinnerInteger[1].getValue() - 1);
					}
				} else if (e.getSource() == spinnerInteger[2]) {
					if (personCount > 28) {
						spinnerInteger[2].setValue((int) spinnerInteger[2].getValue() - 1);
					}
				} else if (e.getSource() == spinnerInteger[3]) {
					if (personCount > 28) {
						spinnerInteger[3].setValue((int) spinnerInteger[3].getValue() - 1);
					}
				}
			}
		}

	}

	void firstPremiumGetSeat() { // 처음 좌석 확인.
		t.makeConnection();
		String sql = "";

		sql += "SELECT col FROM seat ";

		try {
			sql += "WHERE busID = '" + ResPanel.busID + "' AND busN = '" + ResPanel.busNumber + "'";
			t.rs = t.stmt.executeQuery(sql);

			System.out.println(sql);

			while (t.rs.next()) {
				System.out.println(t.rs.getString("col"));

				for (int i = 0; i < 8; i++) { // 서쪽 좌석.
					for (int j = 0; j < 2; j++) {
						if (seatBtn1[i][j].getText().equals(t.rs.getString("col"))) {
							seatBtn1[i][j].setEnabled(false);
							seatBtn1[i][j].setBackground(Color.RED);
						}
					}
				}

				for (int i = 0; i < 8; i++) { // 동쪽 좌석.
					for (int j = 0; j < 1; j++) {
						if (seatBtn2[i][j].getText().equals(t.rs.getString("col"))) {
							seatBtn2[i][j].setEnabled(false);
							seatBtn2[i][j].setBackground(Color.RED);
						}
					}
				}

				for (int i = 0; i < seatBtn3.length; i++) { // 남쪽 좌석.
					if (seatBtn3[i].getText().equals(t.rs.getString("col"))) {
						seatBtn3[i].setEnabled(false);
						seatBtn3[i].setBackground(Color.RED);
					}
				}
				seatR++;
			}

			sql = "";
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		t.disConnection();
	}

	boolean maxCount() { // 버튼은 사람의 수를 넘길 수 없다.
		System.out.println("btnCount : " + btnCount);
		System.out.println("personCount : " + personCount);

		if (btnCount <= personCount) { // 버튼의 수가 사람 수보다 적다면
			return true;
		} else {
			return false;
		}
	}

	void premiumPurchaseDB() { // 결제한 내역을 DB(seat)에 저장.
		t.makeConnection();

		String sql = "";

		System.out.println("버스 번호 : " + ResPanel.busNumber);
		System.out.println("버스 ID : " + ResPanel.busID);
		for (int i = 0; i < premiumColList.size(); i++) {
			System.out.println("열 번호 : " + premiumColList.get(0));
		}
		System.out.println("아이디 : " + LoginView.id);

		try {
			for (int i = 0; i < premiumColList.size(); i++) {
				sql = "INSERT INTO seat (busID, busN, col, MID) values ";
				sql += "('" + ResPanel.busID + "', '" + ResPanel.busNumber + "', '" + premiumColList.get(i) + "', '"
						+ LoginView.id + "')";
				System.out.println(sql);

				t.stmt.executeUpdate(sql);

				sql = "";
				seatR++;
			}

			setVisible(false);
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		t.disConnection();
	}

	void premiumRemainUpdate() { // 잔여석 업데이트.
		t.makeConnection();
		String sql = "";

		try {
			sql = "UPDATE bus set seatR = '" + (28 - seatR) + "' WHERE busID = '" + ResPanel.busID + "'AND busN = '"
					+ ResPanel.busNumber + "'";

			t.stmt.execute(sql);
			seatR = 0;
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		t.disConnection();
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
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/teambus", "root", "1234");
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
