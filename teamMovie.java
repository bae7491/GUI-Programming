package team;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

// 영화 관리 데이터 모델.
class MovieModel {
	String title;
	String director;
	String genre;
	String time;
	String actor;
	String rating;
	String country;

	MovieModel(String title, String director, String genre, String time, String actor, String rating, String country) {
		this.title = title;
		this.director = director;
		this.genre = genre;
		this.time = time;
		this.actor = actor;
		this.rating = rating;
		this.country = country;
	}
}

// 실질적으로 보이는 Frame에 대한 설정.
class teamMovieView extends JFrame {
	private static final long serialVersionUID = 1L;

	// ContentPane과 Panel들을 선언.
	Container c = getContentPane();
	JPanel p1, p2, p3;
	JPanel pp1, pp2, pp3;

	// teamMovie 변수 선언.
	JLabel title = new JLabel("제목");
	JTextField ttf = new JTextField(25); // TitleTextField.
	JLabel[] label = new JLabel[6];
	JTextField[] tf = new JTextField[3];
	JComboBox<String> combG = new JComboBox<String>(); // 장르 (Genre)
	JComboBox<String> combT = new JComboBox<String>(); // 상영시간 (Time)
	JComboBox<String> combR = new JComboBox<String>(); // 등급 (Rating)
	JComboBox<String> combC = new JComboBox<String>(); // 나라 (Country)
	JButton[] button = new JButton[5];

	// Table 기본 설정.
	String colName[] = { "제목", "감독", "장르", "시간", "주연", "연령 등급", "나라" };
	DefaultTableModel model = new DefaultTableModel(colName, 0) {
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int i, int c) {
			return false;
		}
	};

	JTable table = new JTable(model);
	DefaultTableCellRenderer ca = new DefaultTableCellRenderer();

	public teamMovieView() {
		
		// Frame의 기본설정을 함.
		setTitle("영화 관리 프로그램");
		setSize(615, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Frame에 Panel을 넣기위해 선언한 메소드.
		newComponents();
		setComponents();
		addComponents();

		setVisible(true);
	}
	
	// 패널을 새로 만들때 필요한 구성을 선언.
	void newComponents() {

		String[] lName = { "감독", "장르", "시간", "주연", "등급", "나라" }; // 라벨 배열.
		String[] combGName = { "전체", "액션", "코미디", "드라마", "멜로/로맨스", "공포/스릴러", "SF/판타지", "애니메이션" }; // 장르 리스트 배열.
		String[] combTName = { "전체", "60분 이하", "61~90분", "91~120분", "121~150분", "151~180분", "180분 초과" }; // 상영 시간 리스트 배열.																							
		String[] combRName = { "전체", "12세 관람가", "15세 관람가", "19세 관람가" }; // 등급 리스트 배열.
		String[] combCName = { "전체", "국내", "동양", "서양" }; // 나라 리스트 배열.

		String[] bName = { "삽입", "수정", "삭제", "초기화", "영화 검색" }; // 기능을 추가한 버튼들.

		// TextField 배치.
		for (int i = 0; i < tf.length; i++) {
			tf[i] = new JTextField(30);
			tf[i].setText("");
		}

		// Button 배치.
		for (int i = 0; i < bName.length; i++) {
			button[i] = new JButton(bName[i]);
		}

		// 등급 ComboBox 배치.
		for (int i = 0; i < combRName.length; i++) {
			combR.addItem(combRName[i]);
		}

		// 시간 ComboBox 배치.
		for (int i = 0; i < combTName.length; i++) {
			combT.addItem(combTName[i]);
		}

		// 나라 ComboBox 배치.
		for (int i = 0; i < combCName.length; i++) {
			combC.addItem(combCName[i]);
		}

		// Label 배치.
		for (int i = 0; i < lName.length; i++) {
			label[i] = new JLabel(lName[i]);
		}

		// 장르 ComboBox 배치.
		for (int i = 0; i < combGName.length; i++) {
			combG.addItem(combGName[i]);
		}
	}
	
	// 패널 내부 설정들.
	void setComponents() { 

		// 라벨들의 세부 설정.
		for (int i = 0; i < 6; i++) {
			label[i].setHorizontalAlignment(JLabel.CENTER);
			label[i].setFont(new Font("고딕체", Font.BOLD, 18));
		}

		// 제목을 입력할 Label과 TextField의 세부 설정.
		title.setFont(new Font("고딕체", Font.BOLD, 30));
		ttf.setFont(new Font("", Font.ITALIC, 20));
		// 색깔 변경.
		Color color = new Color(200, 220, 235);
		ttf.setBackground(color);

		// Label 내부 설정.
		for (int i = 0; i < label.length; i++) {
			label[i].setHorizontalAlignment(JLabel.CENTER);
		}
		
	}
	
	// 기능들을 패널에 붙임.
	void addComponents() { 

		// Label, TextField, ComboBox, Button을 추가한 p1, p2, p3를 합칠 패널.
		pp1 = new JPanel();
		pp1.setLayout(new BorderLayout());

		// 제목 tf가 있는 패널. (pp1의 북쪽 배치.)
		p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));

		
		p1.add(title);
		p1.add(ttf);

		// tf들, list들이 있는 패널. (pp1의 중앙 배치.)
		p2 = new JPanel();
		p2.setLayout(new GridLayout(2, 3, 10, 5));

		// 감독 텍스트필드를 Panel에 추가.
		p2.add(label[0]); 
		p2.add(tf[0]);

		// 장르 콤보박스를 Panel에 추가.
		p2.add(label[1]); 
		p2.add(combG);

		// 시간 콤보박스를 Panel에 추가.
		p2.add(label[2]); 
		p2.add(combT);

		// 주연 텍스트필드를 Panel에 추가.
		p2.add(label[3]); 
		p2.add(tf[1]);

		// 등급 콤보박스를 Panel에 추가.
		p2.add(label[4]); 
		p2.add(combR);

		// 나라 콤보박스를 Panel에 추가.
		p2.add(label[5]); 
		p2.add(combC);

		// btn들이 있는 패널. (pp1의 남쪽 배치.)
		p3 = new JPanel();
		p3.setLayout(new FlowLayout());

		p3.add(button[0]); // 삽입
		p3.add(button[1]); // 수정
		p3.add(button[2]); // 삭제
		p3.add(button[3]); // 초기화.

		// Table가 추가될 패널.
		pp2 = new JPanel();
		pp2.setLayout(new FlowLayout());
		pp2.add(new JScrollPane(table));

		// Table의 크기를 설정 및 수정 불가.
		table.setPreferredScrollableViewportSize(new Dimension(550, 118));
		table.getTableHeader().setReorderingAllowed(false);

		// Table의 열 길이 설정.
		table.getColumnModel().getColumn(0).setPreferredWidth(70);
		table.getColumnModel().getColumn(1).setPreferredWidth(70);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		table.getColumnModel().getColumn(3).setPreferredWidth(60);
		table.getColumnModel().getColumn(4).setPreferredWidth(70);
		table.getColumnModel().getColumn(5).setPreferredWidth(50);
		table.getColumnModel().getColumn(6).setPreferredWidth(10);

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
		tcm.getColumn(5).setCellRenderer(ca);
		tcm.getColumn(6).setCellRenderer(ca);

		// 검색 btn이 추가될 패널.
		pp3 = new JPanel();
		pp3.setLayout(new BorderLayout());

		pp3.add(button[4]);

		pp1.add(p1, BorderLayout.NORTH);
		pp1.add(p2, BorderLayout.CENTER);
		pp1.add(p3, BorderLayout.SOUTH);

		c.add(pp1, BorderLayout.NORTH); // 필드들과 버튼.
		c.add(pp2, BorderLayout.CENTER); // JTable.
		c.add(pp3, BorderLayout.SOUTH); // 검색 버튼.
	}
}

// 영화 검색 창의 역할을 수행할 Frame.
class searchView extends JFrame {
	private static final long serialVersionUID = 1L;

	// searchView의 변수 선언.
	Container c = getContentPane();
	JPanel p1, p2;
	JComboBox<String> combMovie;
	String[] movie = { "제목", "감독", "장르", "시간", "주연", "등급", "나라" };
	JTextField tf;
	JButton btn[] = new JButton[2];
	String[] btnString = { "검색", "취소" };

	// searchView의 기본 설정.
	public searchView() {
		setTitle("영화 검색");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 100);

		// List와 검색어 TextField 추가.
		p1 = new JPanel(new FlowLayout()); 
		combMovie = new JComboBox<String>(movie);

		// CombBox에 툴팁 Text를 추가.
		combMovie.setToolTipText("검색하고 싶은 분류를 선택하세요.");
		p1.add(combMovie);

		// 검색어를 입력할 TextField 설정.
		tf = new JTextField(20);
		tf.setToolTipText("검색하고 싶은 내용을 입력하세요.");
		p1.add(tf);

		// 검색 button.
		p2 = new JPanel(new FlowLayout()); 

		for (int i = 0; i < btn.length; i++) {
			btn[i] = new JButton(btnString[i]);
			p2.add(btn[i]);
		}

		c.add(p1, BorderLayout.NORTH);
		c.add(p2, BorderLayout.SOUTH);

		setVisible(true);
	}
}

// DB와 연동해서 각 기능들을 수행할 Class.
public class teamMovie {
	MovieModel m;
	teamMovieView v;
	searchView search;

	MovieHandler handler = new MovieHandler();

	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	// TextField, Button, Table, 창에 Handler 추가.
	teamMovie() {
		v = new teamMovieView();

		v.ttf.addActionListener(handler);

		for (int i = 0; i < v.tf.length; i++) {
			v.tf[i].addActionListener(handler);
		}

		for (int i = 0; i < v.button.length; i++) {
			v.button[i].addActionListener(handler);
		}

		startButton(); // Button의 초기 설정 메소드.

		v.table.addMouseListener(new MouseHandler());
		v.addWindowListener(new WindowHandler());
	}

	// 입력받은 데이터의 값을 가져오는 메소드.
	void viewToModel() {
		m = new MovieModel(v.ttf.getText(), v.tf[0].getText(), v.combG.getSelectedItem().toString(),
				v.combT.getSelectedItem().toString(), v.tf[1].getText(), v.combR.getSelectedItem().toString(),
				v.combC.getSelectedItem().toString());
	}

	// TextField와 ComboBox의 내용을 초기화 하는 메소드.
	void resetField() {
		v.ttf.setText("");
		v.tf[0].setText("");
		v.tf[1].setText("");
		v.combG.setSelectedIndex(0);
		v.combT.setSelectedIndex(0);
		v.combR.setSelectedIndex(0);
		v.combC.setSelectedIndex(0);
	}

	// 마우스가 클릭되는 이벤트를 처리할 MouseHandler.
	class MouseHandler extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			int row = v.table.getSelectedRow();

			TableModel data = v.table.getModel();

			// Table에서 마우스로 클릭한 행의 각 셀들을 불러옴.
			String movieTitle = (String) data.getValueAt(row, 0);
			String movieDirector = (String) data.getValueAt(row, 1);
			String movieGenre = (String) data.getValueAt(row, 2);
			String movieTime = (String) data.getValueAt(row, 3);
			String movieActor = (String) data.getValueAt(row, 4);
			String movieRating = (String) data.getValueAt(row, 5);
			String movieCountry = (String) data.getValueAt(row, 6);

			// 불러온 셀 값들을 맞는 위치의 TextField나 ComboBox를 변경.
			v.ttf.setText(movieTitle);
			v.tf[0].setText(movieDirector);
			v.tf[1].setText(movieActor);
			v.combG.setSelectedItem(movieGenre);
			v.combT.setSelectedItem(movieTime);
			v.combR.setSelectedItem(movieRating);
			v.combC.setSelectedItem(movieCountry);

			v.button[0].setEnabled(false);
			v.button[1].setEnabled(true);
			v.button[2].setEnabled(true);
		}
	}

	// 마우스를 이용한 액션 이벤트를 처리할 ActionLiseter. (MovieHandler)
	class MovieHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == v.ttf) { // TitleTextField.
				v.tf[0].requestFocus();
			} else if (e.getSource() == v.tf[0]) { // 감독 TextField.
				v.tf[1].requestFocus();
			} else if (e.getSource() == v.button[0]) { // 삽입.
				if (v.ttf.getText().equals("") || v.tf[0].getText().equals("") || v.tf[1].getText().equals("")) { // TextField의 내용들이 비어있는 값이라면,
					JOptionPane.showMessageDialog(null, "빈 공간이 있습니다! 공간을 채워주세요!", "경고", JOptionPane.ERROR_MESSAGE); // 빈공간을 채워달라는 경고창 실행.
				} else { // 모두 채워졌다면,
					addMovie(); // DB에 삽입하고, Table에 출력.
					resetField(); // TextField, ComboBox 값을 초기 상태로 돌림.
				}
			} else if (e.getSource() == v.button[1]) { // 수정.
				updateMovie(); // DB의 내용을 수정하고, Table에 출력.
				resetField(); // TextField, ComboBox 값을 초기 상태로 돌림.
			} else if (e.getSource() == v.button[2]) { // 삭제.
				deleteMovie(); // 선택된 내용을 토대로, DB에서 내용을 삭제.
				resetField(); // TextField, ComboBox 값을 초기 상태로 돌림.
			} else if (e.getSource() == v.button[3]) { // 초기화.
				resetField(); // TextField, ComboBox 값을 초기 상태로 돌림.

				v.model.setNumRows(0); // DB의 0번 행부터 불러옴.

				// Button의 기능이 수행될지 정함.
				v.button[0].setEnabled(true); // Button 입력이 가능.
				v.button[1].setEnabled(false); // Button 입력이 불가능.
				v.button[2].setEnabled(false); // Button 입력이 불가능.

				getMovie(); // DB의 내용을 불러옴.
			} else if (e.getSource() == v.button[4]) { // 영화 검색.
				searchView(); // 영화 검색 창 실행 메소드.
				resetField(); // TextField, ComboBox 값을 초기 상태로 돌림.
			} else if (e.getSource() == search.btn[0]) { // 영화 검색 창의 검색.
				searchMovie(); // 영화 검색 기능을 수행할 메소드.
			} else if (e.getSource() == search.btn[1]) { // 영화 검색 창의 취소.
				search.setVisible(false); // 취소 버튼 클릭 시, search 창 종료.
				resetField(); // TextField, ComboBox 값을 초기 상태로 돌림.
			}
		}

	}

	// 창 실행 시 실행하게 해줄 WindowListener. (WindowHandler)
	class WindowHandler extends WindowAdapter {
		public void windowOpened(WindowEvent e) {
			getMovie(); // DB의 내용을 가져오는 메소드.
		}
	}

	// DB의 내용을 가져오는 메소드.
	public void getMovie() {
		makeConnection(); // DB에 연결하는 메소드.
		
		String sql = ""; // SQL문 초기화.
		sql = "SELECT * FROM movie";

		try {
			rs = stmt.executeQuery(sql);

			// 다음 줄이 없을 때 까지 반복.
			while (rs.next()) {
				// Table의 행에 데이터 추가.
				v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"), rs.getString("genre"),
						rs.getString("time"), rs.getString("actor"), rs.getString("rating"), rs.getString("country") });
			}

			startButton(); // Button의 초기 설정 메소드.
		} catch (SQLException sqle) {
			System.out.println("getData: SQL Error");
			disConnection(); // DB 연결 종료 메소드.
		}
	}

	// 영화 검색 창 실행 메소드.
	public void searchView() {
		search = new searchView();
		
		// 영화 검색 창의 Button에 ActionHandler를 추가.
		for (int i = 0; i < search.btn.length; i++) {
			search.btn[i].addActionListener(handler);
		}
	}

	// 영화 검색 기능을 수행할 메소드.
	public void searchMovie() {
		makeConnection(); // DB에 연결하는 메소드.
		String sql = "SELECT * FROM movie "; // SQL문 초기화.

		try {
			// ComboBox에서 선택한 아이템을 문자열로 변경해서 SQL 문 뒤에 연결할 수 있게 switch 구문을 추가.
			switch (search.combMovie.getSelectedItem().toString()) {
			case "제목": // ComboBox에서 선택한 아이템이 '제목'이라면,
				sql += "WHERE title = '" + search.tf.getText() + "'"; // 먼저 입력된 SQL문 뒤에 추가로 붙임.
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0); // Table의 행을 처음(0)으로 변경.

				while (rs.next()) {
					// Table의 행에 데이터 추가.
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				// 영화 검색 창 종료.
				search.setVisible(false);
				break;

			case "감독": // ComboBox에서 선택한 아이템이 '감독'이라면,
				sql += "WHERE director = '" + search.tf.getText() + "'"; // 먼저 입력된 SQL문 뒤에 추가로 붙임.
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0); // Table의 행을 처음(0)으로 변경.

				while (rs.next()) {
					// Table의 행에 데이터 추가.
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				// 영화 검색 창 종료.
				search.setVisible(false);
				break;

			case "장르": // ComboBox에서 선택한 아이템이 '장르'이라면,
				sql += "WHERE genre = '" + search.tf.getText() + "'"; // 먼저 입력된 SQL문 뒤에 추가로 붙임.
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0); // Table의 행을 처음(0)으로 변경.

				while (rs.next()) {
					// Table의 행에 데이터 추가.
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				// 영화 검색 창 종료.
				search.setVisible(false);
				break;

			case "시간": // ComboBox에서 선택한 아이템이 '시간'이라면,
				sql += "WHERE time = '" + search.tf.getText() + "'"; // 먼저 입력된 SQL문 뒤에 추가로 붙임.
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0); // Table의 행을 처음(0)으로 변경.

				while (rs.next()) {
					// Table의 행에 데이터 추가.
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				// 영화 검색 창 종료.
				search.setVisible(false);
				break;

			case "주연": // ComboBox에서 선택한 아이템이 '주연'이라면,
				sql += "WHERE actor = '" + search.tf.getText() + "'"; // 먼저 입력된 SQL문 뒤에 추가로 붙임.
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0); // Table의 행을 처음(0)으로 변경.

				while (rs.next()) {
					// Table의 행에 데이터 추가.
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				// 영화 검색 창 종료.
				search.setVisible(false);
				break;

			case "등급": // ComboBox에서 선택한 아이템이 '등급'이라면,
				sql += "WHERE rating = '" + search.tf.getText() + "'"; // 먼저 입력된 SQL문 뒤에 추가로 붙임.
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0); // Table의 행을 처음(0)으로 변경.

				while (rs.next()) {
					// Table의 행에 데이터 추가.
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				// 영화 검색 창 종료.
				search.setVisible(false);
				break;

			case "나라": // ComboBox에서 선택한 아이템이 '나라'이라면,
				sql += "WHERE country = '" + search.tf.getText() + "'"; // 먼저 입력된 SQL문 뒤에 추가로 붙임.
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0); // Table의 행을 처음(0)으로 변경.

				while (rs.next()) {
					// Table의 행에 데이터 추가.
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				// 영화 검색 창 종료.
				search.setVisible(false);
				break;

			}
		} catch (SQLException sqle) {
			System.out.println("SQL Error");
			disConnection(); // DB 연결 종료 메소드.
		}
	}

	// Table에서 선택된 행의 영화 데이터를 Table에 추가하는 메소드.
	public void addMovie() {
		makeConnection(); // DB에 연결하는 메소드.
		viewToModel(); // 입력받은 데이터의 값을 가져오는 메소드.
		try {
			if (!v.ttf.getText().equals("")) { // 제목 TextField의 값을 가져왔는데 값이 비었다면,
				String s = ""; // SQL문 초기화.
				
				//SQL 문에 삽입 SQL문 추가.
				s = "INSERT INTO movie (title, director, genre, time, actor, rating, country) values ";
				s += "('" + m.title + "', '" + m.director + "', '" + m.genre + "', '" + m.time + "', '" + m.actor
						+ "', '" + m.rating + "', '" + m.country + "')";
				System.out.println(s);
				
				stmt.executeUpdate(s);
				v.model.setNumRows(0); // Table의 행을 처음(0)으로 변경.
				getMovie();
			} else {
				System.out.println("삽입할 문장이 없습니다.");
			}
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}

		disConnection(); // DB 연결 종료 메소드.
	}

	// Table에서 선택된 행의 영화 데이터를 수정해 Table에 출력하는 메소드.
	public void updateMovie() {
		viewToModel(); // 입력받은 데이터의 값을 가져오는 메소드.
		makeConnection(); // DB에 연결하는 메소드.

		try {
			String s = ""; // SQL문 초기화.
			
			//SQL 문에 삽입 SQL문 추가.
			s = "UPDATE movie set director = '" + m.director + "', genre = '" + m.genre + "', time = '" + m.time
					+ "', actor = '" + m.actor + "', rating = '" + m.rating + "', country = '" + m.country
					+ "' WHERE title = '" + m.title + "'";
			System.out.println(s);
			
			stmt.executeUpdate(s);
			v.model.setNumRows(0); // Table의 행을 처음(0)으로 변경.
			getMovie();
		} catch (SQLException sqle) {
			System.out.println("isExist : SQL Error");
			disConnection(); // DB 연결 종료 메소드.
		}
	}

	// Table에서 선택된 행의 영화 데이터를 삭제해 Table에 출력하는 메소드.
	public void deleteMovie() {
		viewToModel(); // 입력받은 데이터의 값을 가져오는 메소드.
		int isDelete = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", null, JOptionPane.YES_NO_OPTION); // 삭제 Button 클릭 시, 삭제 경고 창 실행.
		System.out.println("DELETE : " + isDelete);

		if (isDelete == JOptionPane.YES_OPTION) { // YES_OPTION 선택 시,
			makeConnection(); // DB에 연결하는 메소드.
			String sql = ""; // SQL문 초기화.
			sql = "DELETE FROM movie WHERE title = '" + m.title + "'";
			System.out.println(sql);
			try {
				stmt.executeUpdate(sql);
				v.model.setNumRows(0); // Table의 행을 처음(0)으로 변경.
				getMovie(); // DB의 내용을 가져오는 메소드.
			} catch (SQLException sqle) {
				System.out.println("isExist : DELETE SQL Error");
			}

			disConnection(); // DB 연결 종료 메소드.
		}
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

	// Button의 초기 설정 메소드.
	public void startButton() {
		v.button[0].setEnabled(true);
		v.button[1].setEnabled(false);
		v.button[2].setEnabled(false);
	}

	// 메인.
	public static void main(String[] args) {
		new teamMovie();
	}
}
