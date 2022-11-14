package team;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.print.attribute.standard.JobName;
import javax.swing.*;

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

class teamMovieView extends JFrame {
	private static final long serialVersionUID = 1L;

	Container c = getContentPane();
	JPanel p1, p2, p3, p4, p5;
	JPanel pp1, pp2, pp3;

	JLabel title = new JLabel("제목");
	JTextField ttf = new JTextField(46);
	JLabel[] label = new JLabel[6];
	JTextField[] tf = new JTextField[3];
	JComboBox<String> combG = new JComboBox<String>(); // 장르 Genre
	JComboBox<String> combT = new JComboBox<String>(); // 상영시간 Time
	JComboBox<String> combR = new JComboBox<String>(); // 등급 Rating
	JComboBox<String> combC = new JComboBox<String>(); // 나라 Country

	JButton[] button = new JButton[5];

	JTextArea ta = new JTextArea(10, 54);
	// 10, 54

	public teamMovieView() {

		setTitle("영화 ");
		setSize(570, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		newComponents();
		setComponents();
		addComponents();

		setVisible(true);
	}

	void newComponents() {

		String[] lName = { "감독", "장르", "시간", "주연", "등급", "나라" }; // 라벨 배열.
		String[] tName = { "", "", "" };
		String[] combGName = { "전체", "액션", "코미디", "드라마", "멜로", "공포/스릴러", "SF/판타지", "애니메이션" }; // 장르 리스트 배열.
		String[] combTName = { "전체", "1시간 이내", "1시간30분 이내", "2시간 이내", "2시간30분 이내" }; // 상영 시간 리스트 배열.
		String[] combRName = { "전체", "12세 관람가", "15세 관람가", "19세 관람가" }; // 등급 리스트 배열.
		String[] combCName = { "전체", "국내", "동양", "서양" }; // 나라 리스트 배열.

		String[] bName = { "삽입", "수정", "삭제", "초기화", "영화 검색"};

		for (int i = 0; i < tf.length; i++) {
			tf[i] = new JTextField(30);
		}

		for (int i = 0; i < bName.length; i++) {
			button[i] = new JButton(bName[i]);
		}

		for (int i = 0; i < combRName.length - 1; i++) {
			combR.addItem(combRName[i]);
		}

		for (int i = 0; i < combTName.length; i++) {
			combT.addItem(combTName[i]);
		}

		for (int i = 0; i < combCName.length; i++) {
			combC.addItem(combCName[i]);
		}

		for (int i = 0; i < lName.length; i++) {
			label[i] = new JLabel(lName[i]);
		}

		for (int i = 0; i < combGName.length; i++) {
			combG.addItem(combGName[i]);
		}
	}

	void setComponents() { // 패널 내부 설정들.

		for (int i = 0; i < label.length; i++) {
			label[i].setHorizontalAlignment(JLabel.CENTER);
		}
		
		ttf.setBackground(Color.YELLOW);
		
	}

	void addComponents() { // 내부설정패널을 외부 패널에 붙임.
		
		pp1 = new JPanel();
		pp1.setLayout(new BorderLayout());

		// 제목 tf가 있는 패널. (pp1의 북쪽 배치.)
		p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));

		p1.add(title); // 제목
		p1.add(ttf); // ㄴ title tf

		// tf들, list들이 있는 패널. (pp1의 중앙 배치.)
		p2 = new JPanel();
		p2.setLayout(new GridLayout(2, 3));

		p2.add(label[0]); // 감독
		p2.add(tf[0]); // ㄴ tf

		p2.add(label[1]); // 장르
		p2.add(combG); // ㄴ 드롭다운 리스트

		p2.add(label[2]); // 시간
		p2.add(combT); // ㄴ 드롭다운 리스트

		p2.add(label[3]); // 주연
		p2.add(tf[1]); // ㄴ tf

		p2.add(label[4]); // 등급
		p2.add(combR); // ㄴ 드롭다운 리스트

		p2.add(label[5]); // 나라
		p2.add(combC); // ㄴ 드롭다운 리스트

		// btn들이 있는 패널. (pp1의 남쪽 배치.)
		p3 = new JPanel();
		p3.setLayout(new FlowLayout());

		p3.add(button[0]); // 삽입
		p3.add(button[1]); // 수정
		p3.add(button[2]); // 삭제
		p3.add(button[3]); // 초기화.

		// TextArea가 추가될 패널.
		pp2 = new JPanel();
		pp2.setLayout(new FlowLayout());

		pp2.add(new JScrollPane(ta));

		// 검색 btn이 추가될 패널.
		pp3 = new JPanel();
		pp3.setLayout(new BorderLayout());

		pp3.add(button[4]);

		pp1.add(p1, BorderLayout.NORTH);
		pp1.add(p2, BorderLayout.CENTER);
		pp1.add(p3, BorderLayout.SOUTH);

		c.add(pp1, BorderLayout.NORTH); // 필드들과 버튼.
		c.add(pp2, BorderLayout.CENTER); // 텍스트에리어.
		c.add(pp3, BorderLayout.SOUTH); // 검색 버튼.
	}
}

class searchView extends JFrame {
	private static final long serialVersionUID = 1L;

	Container c = getContentPane();
	JPanel p1, p2;
	JComboBox<String> combmovie;
	String[] movie = { "제목", "감독", "장르", "시간", "주연", "등급", "나라" };
	JTextField tf;
	JButton btn = new JButton("검색");

	public searchView() {
		setTitle("검색");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 100);

		p1 = new JPanel(new FlowLayout()); // List와 검색어 TextField 추가.
		combmovie = new JComboBox<String>(movie);

		combmovie.setToolTipText("검색하고 싶은 분류를 선택하세요.");
		p1.add(combmovie);

		tf = new JTextField(20);
		tf.setToolTipText("검색하고 싶은 내용을 입력하세요.");
		p1.add(tf);

		p2 = new JPanel(new FlowLayout()); // 검색 btn.
		p2.add(btn);

		c.add(p1, BorderLayout.NORTH);
		c.add(p2, BorderLayout.SOUTH);

		setVisible(true);
	}
}

public class teamMovie {
	MovieModel m;
	teamMovieView v;
	searchView search;
	MovieHandler handler = new MovieHandler();

	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	teamMovie() {
		v = new teamMovieView();

		v.ttf.addActionListener(handler);

		for (int i = 0; i < v.tf.length; i++) {
			v.tf[i].addActionListener(handler);
			if (i < 3) {
				v.button[i].addActionListener(handler);
			}
		}
		v.button[3].addActionListener(handler);
		v.button[4].addActionListener(handler);
		v.addWindowListener(new WindowHandler());
	}

	void viewToModel() {
		m = new MovieModel(v.ttf.getText(), v.tf[0].getText(), v.combG.getSelectedItem().toString(),
				v.combT.getSelectedItem().toString(), v.tf[1].getText(), v.combR.getSelectedItem().toString(),
				v.combC.getSelectedItem().toString());
	}

	// 전각문자 개수를 세주는 메서드.
	private static int getKorCnt(String kor) {
		int cnt = 0;
		for (int i = 0; i < kor.length(); i++) {
			if (kor.charAt(i) >= '가' && kor.charAt(i) <= '힣') {
				cnt++;
			}
		}
		return cnt;
	}

	// 전각문자의 개수만큼 문자열 길이를 빼주는 메서드.
	public static String convert(String word, int size) {
		String formatter = String.format("%%%ds", size - getKorCnt(word));
		return String.format(formatter, word);
	}

	void addTextArea() {
		String s;
		
		s = "  |";
		s += convert(m.title, 13);
		s += "\t|";
		s += convert(m.director, 13);
		s += "\t|";
		s += convert(m.genre, 13);
		s += "\t|";
		s += convert(m.time, 13);
		s += "\t|";
		s += convert(m.actor, 13);
		s += "\t|";
		s += convert(m.rating, 14);
		s += "\t|";
		s += convert(m.country, 4);
		s += "|    \n";
		
		
		System.out.println(s);
		v.ta.append(s);
	}

	class MovieHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == v.ttf) {
				v.tf[0].requestFocus();
			} else if (e.getSource() == v.tf[0]) {
				v.tf[1].requestFocus();
			} else if (e.getSource() == v.button[0]) { // 삽입.
				addMovie();
			} else if (e.getSource() == v.button[1]) { // 수정.
				updateMovie();
			} else if (e.getSource() == v.button[2]) { // 삭제.
				deleteMovie();
			} else if (e.getSource() == v.button[3]) { // 초기화.
				v.ttf.setText("");
				for (int i = 0; i < v.tf.length; i++) {
					v.tf[i].setText("");
				}
				v.combG.setSelectedIndex(0);
				v.combT.setSelectedIndex(0);
				v.combR.setSelectedIndex(0);
				v.combC.setSelectedIndex(0);
				getMovie();
			} else if (e.getSource() == v.button[4]) {
				searchView();
			} else if (e.getSource() == search.btn) {
				searchMovie();
			}
		}

	}

	class WindowHandler extends WindowAdapter {
		public void windowOpened(WindowEvent e) {
			getMovie();
		}
	}

	public void getMovie() {
		makeConnection();
		String sql = "";
		sql = "SELECT * FROM movie";
		v.ta.setText("");

		try {
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				m = new MovieModel(rs.getString("title"), rs.getString("director"), rs.getString("genre"),
						rs.getString("time"), rs.getString("actor"), rs.getString("rating"), rs.getString("country"));
				addTextArea();
			}
		} catch (SQLException sqle) {
			System.out.println("getData: SQL Error");
			disConnection();
		}
	}

	public void searchView() {
		search = new searchView();
		search.btn.addActionListener(handler);
	}

	public void searchMovie() {
		makeConnection();
		v.ta.setText("");
		String sql = "SELECT * FROM movie ";

		try {
			switch (search.combmovie.getSelectedItem().toString()) {
			case "제목":
				sql += "WHERE title = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				while (rs.next()) {
					m = new MovieModel(rs.getString("title"), rs.getString("director"), rs.getString("genre"),
							rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country"));
					addTextArea();
				}
				search.setVisible(false);
				break;

			case "감독":
				sql += "WHERE director = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				while (rs.next()) {
					m = new MovieModel(rs.getString("title"), rs.getString("director"), rs.getString("genre"),
							rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country"));
					addTextArea();
				}
				search.setVisible(false);
				break;

			case "장르":
				sql += "WHERE genre = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				while (rs.next()) {
					m = new MovieModel(rs.getString("title"), rs.getString("director"), rs.getString("genre"),
							rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country"));
					addTextArea();
				}
				search.setVisible(false);
				break;

			case "시간":
				sql += "WHERE time = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				while (rs.next()) {
					m = new MovieModel(rs.getString("title"), rs.getString("director"), rs.getString("genre"),
							rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country"));
					addTextArea();
				}
				search.setVisible(false);
				break;

			case "주연":
				sql += "WHERE actor = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				while (rs.next()) {
					m = new MovieModel(rs.getString("title"), rs.getString("director"), rs.getString("genre"),
							rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country"));
					addTextArea();
				}
				search.setVisible(false);
				break;

			case "등급":
				sql += "WHERE rating = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				while (rs.next()) {
					m = new MovieModel(rs.getString("title"), rs.getString("director"), rs.getString("genre"),
							rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country"));
					addTextArea();
				}
				search.setVisible(false);
				break;

			case "나라":
				sql += "WHERE country = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				while (rs.next()) {
					m = new MovieModel(rs.getString("title"), rs.getString("director"), rs.getString("genre"),
							rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country"));
					addTextArea();
				}
				search.setVisible(false);
				break;

			}
		} catch (SQLException sqle) {
			System.out.println("SQL Error");
			disConnection();
		}
	}

	public void addMovie() {
		makeConnection();
		viewToModel();
		try {
			String s = "";
			s = "INSERT INTO movie (title, director, genre, time, actor, rating, country) values ";
			s += "('" + m.title + "', '" + m.director + "', '" + m.genre + "', '" + m.time + "', '" + m.actor + "', '"
					+ m.rating + "', '" + m.country + "')";
			System.out.println(s);
			stmt.executeUpdate(s);
			getMovie();
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}

		disConnection();
	}

	public void updateMovie() {
		viewToModel();
		makeConnection();

		try {
			String s = "";
			s = "UPDATE movie set director = '" + m.director + "', genre = '" + m.genre + "', time = '" + m.time
					+ "', actor = '" + m.actor + "', rating = '" + m.rating + "', country = '" + m.country
					+ "' WHERE title = '" + m.title + "'";
			System.out.println(s);
			stmt.executeUpdate(s);
			getMovie();
		} catch (SQLException sqle) {
			System.out.println("isExist : SQL Error");
			disConnection();
		}
	}

	public void deleteMovie() {
		viewToModel();
		int isDelete = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", null, JOptionPane.YES_NO_OPTION);
		System.out.println("DELETE : " + isDelete);

		if (isDelete == JOptionPane.YES_OPTION) {
			makeConnection();
			String sql = "";
			sql = "DELETE FROM movie WHERE title = '" + m.title + "'";
			System.out.println(sql);
			try {
				stmt.executeUpdate(sql);
				getMovie();
			} catch (SQLException sqle) {
				System.out.println("isExist : DELETE SQL Error");
			}

			disConnection();
		}
	}

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
		new teamMovie();
	}
}
