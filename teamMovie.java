package team;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

import com.mysql.cj.xdevapi.Table;

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

	String colName[] = { "제목", "감독", "장르", "시간", "주연", "등급", "나라" };
	DefaultTableModel model = new DefaultTableModel(colName, 0) {
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int i, int c) {
			return false;
		}
	};

	JTable table = new JTable(model);
	DefaultTableCellRenderer ca = new DefaultTableCellRenderer();

	public teamMovieView() {

		setTitle("영화 관리 프로그램");
		setSize(615, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		newComponents();
		setComponents();
		addComponents();

		setVisible(true);
	}

	void newComponents() {

		String[] lName = { "감독", "장르", "시간", "주연", "등급", "나라" }; // 라벨 배열.
		String[] combGName = { "전체", "액션", "코미디", "드라마", "멜로", "공포/스릴러", "SF/판타지", "애니메이션" }; // 장르 리스트 배열.
		String[] combTName = { "전체", "1시간 이내", "1시간30분 이내", "2시간 이내", "2시간30분 이내" }; // 상영 시간 리스트 배열.
		String[] combRName = { "전체", "12세 관람가", "15세 관람가", "19세 관람가" }; // 등급 리스트 배열.
		String[] combCName = { "전체", "국내", "동양", "서양" }; // 나라 리스트 배열.

		String[] bName = { "삽입", "수정", "삭제", "초기화", "영화 검색" };

		for (int i = 0; i < tf.length; i++) {
			tf[i] = new JTextField(30);
			tf[i].setText("");
		}

		for (int i = 0; i < bName.length; i++) {
			button[i] = new JButton(bName[i]);
		}

		for (int i = 0; i < combRName.length; i++) {
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

		// Label 내부 설정.
		for (int i = 0; i < label.length; i++) {
			label[i].setHorizontalAlignment(JLabel.CENTER);
		}

		ttf.setBackground(Color.YELLOW);

	}

	void addComponents() { // 기능들을 패널에 붙임.

		pp1 = new JPanel();
		pp1.setLayout(new BorderLayout());

		// 제목 tf가 있는 패널. (pp1의 북쪽 배치.)
		p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));

		p1.add(title); // 제목
		p1.add(ttf); // ㄴ title tf

		// tf들, list들이 있는 패널. (pp1의 중앙 배치.)
		p2 = new JPanel();
		p2.setLayout(new GridLayout(2, 3, 10, 5));

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

		// Table가 추가될 패널.
		pp2 = new JPanel();
		pp2.setLayout(new FlowLayout());
		pp2.add(new JScrollPane(table));

		table.setPreferredScrollableViewportSize(new Dimension(580, 133));
		table.getTableHeader().setReorderingAllowed(false);

		table.getColumnModel().getColumn(0).setPreferredWidth(70);
		table.getColumnModel().getColumn(1).setPreferredWidth(70);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		table.getColumnModel().getColumn(3).setPreferredWidth(60);
		table.getColumnModel().getColumn(4).setPreferredWidth(70);
		table.getColumnModel().getColumn(5).setPreferredWidth(50);
		table.getColumnModel().getColumn(6).setPreferredWidth(10);

		ca.setHorizontalAlignment(SwingConstants.CENTER); // 렌더러의 가로정렬을 CENTER로

		TableColumnModel tcm = table.getColumnModel(); // 정렬할 테이블의 컬럼모델을 가져옴

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
		c.add(pp2, BorderLayout.CENTER); // 텍스트에리어.
		c.add(pp3, BorderLayout.SOUTH); // 검색 버튼.
	}
}

class searchView extends JFrame {
	private static final long serialVersionUID = 1L;

	Container c = getContentPane();
	JPanel p1, p2;
	JComboBox<String> combMovie;
	String[] movie = { "제목", "감독", "장르", "시간", "주연", "등급", "나라" };
	JTextField tf;
	JButton btn[] = new JButton[2];
	String[] btnString = { "검색", "취소" };

	public searchView() {
		setTitle("영화 검색");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 100);

		p1 = new JPanel(new FlowLayout()); // List와 검색어 TextField 추가.
		combMovie = new JComboBox<String>(movie);

		combMovie.setToolTipText("검색하고 싶은 분류를 선택하세요.");
		p1.add(combMovie);

		tf = new JTextField(20);
		tf.setToolTipText("검색하고 싶은 내용을 입력하세요.");
		p1.add(tf);

		p2 = new JPanel(new FlowLayout()); // 검색 btn.

		for (int i = 0; i < btn.length; i++) {
			btn[i] = new JButton(btnString[i]);
			p2.add(btn[i]);
		}

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
		}

		for (int i = 0; i < v.button.length; i++) {
			v.button[i].addActionListener(handler);
		}

		startButton();

		v.table.addMouseListener(new MouseHandler());
		v.addWindowListener(new WindowHandler());
	}

	void viewToModel() {
		m = new MovieModel(v.ttf.getText(), v.tf[0].getText(), v.combG.getSelectedItem().toString(),
				v.combT.getSelectedItem().toString(), v.tf[1].getText(), v.combR.getSelectedItem().toString(),
				v.combC.getSelectedItem().toString());
	}
	
	void resetField() {
		v.ttf.setText("");
		v.tf[0].setText("");
		v.tf[1].setText("");
		v.combG.setSelectedIndex(0);
		v.combT.setSelectedIndex(0);
		v.combR.setSelectedIndex(0);
		v.combC.setSelectedIndex(0);
	}

	class MouseHandler extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			int row = v.table.getSelectedRow();

			TableModel data = v.table.getModel();

			String movieTitle = (String) data.getValueAt(row, 0);
			String movieDirector = (String) data.getValueAt(row, 1);
			String movieGenre = (String) data.getValueAt(row, 2);
			String movieTime = (String) data.getValueAt(row, 3);
			String movieActor = (String) data.getValueAt(row, 4);
			String movieRating = (String) data.getValueAt(row, 5);
			String movieCountry = (String) data.getValueAt(row, 6);

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

	class MovieHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == v.ttf) {
				v.tf[0].requestFocus();
			} else if (e.getSource() == v.tf[0]) {
				v.tf[1].requestFocus();
			} else if (e.getSource() == v.button[0]) { // 삽입.
				if (v.ttf.getText().equals("") || v.tf[0].getText().equals("") || v.tf[1].getText().equals("")) {
					JOptionPane.showMessageDialog(null, "빈 공간이 있습니다! 공간을 채워주세요!", "경고", JOptionPane.ERROR_MESSAGE);
				} else {
					addMovie();
					resetField();
				}
			} else if (e.getSource() == v.button[1]) { // 수정.
				updateMovie();
				resetField();
			} else if (e.getSource() == v.button[2]) { // 삭제.
				deleteMovie();
				resetField();
			} else if (e.getSource() == v.button[3]) { // 초기화.
				resetField();

				v.model.setNumRows(0);

				v.button[0].setEnabled(true);
				v.button[1].setEnabled(false);
				v.button[2].setEnabled(false);

				getMovie();
			} else if (e.getSource() == v.button[4]) {
				searchView();
				resetField();
			} else if (e.getSource() == search.btn[0]) {
				searchMovie();
				
			} else if (e.getSource() == search.btn[1]) {
				search.setVisible(false);
				resetField();
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

		try {
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"), rs.getString("genre"),
						rs.getString("time"), rs.getString("actor"), rs.getString("rating"), rs.getString("country") });
			}

			startButton();
		} catch (SQLException sqle) {
			System.out.println("getData: SQL Error");
			disConnection();
		}
	}

	public void searchView() {
		search = new searchView();
		for (int i = 0; i < search.btn.length; i++) {
			search.btn[i].addActionListener(handler);
		}
	}

	public void searchMovie() {
		makeConnection();
		String sql = "SELECT * FROM movie ";

		try {
			switch (search.combMovie.getSelectedItem().toString()) {
			case "제목":
				sql += "WHERE title = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0);

				while (rs.next()) {
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				search.setVisible(false);
				break;

			case "감독":
				sql += "WHERE director = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0);

				while (rs.next()) {
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				search.setVisible(false);
				break;

			case "장르":
				sql += "WHERE genre = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0);

				while (rs.next()) {
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				search.setVisible(false);
				break;

			case "시간":
				sql += "WHERE time = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0);

				while (rs.next()) {
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				search.setVisible(false);
				break;

			case "주연":
				sql += "WHERE actor = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0);

				while (rs.next()) {
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				search.setVisible(false);
				break;

			case "등급":
				sql += "WHERE rating = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0);

				while (rs.next()) {
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
				}

				search.setVisible(false);
				break;

			case "나라":
				sql += "WHERE country = '" + search.tf.getText() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				v.model.setNumRows(0);

				while (rs.next()) {
					v.model.addRow(new Object[] { rs.getString("title"), rs.getString("director"),
							rs.getString("genre"), rs.getString("time"), rs.getString("actor"), rs.getString("rating"),
							rs.getString("country") });
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
			if (!v.ttf.getText().equals("")) {
				String s = "";
				s = "INSERT INTO movie (title, director, genre, time, actor, rating, country) values ";
				s += "('" + m.title + "', '" + m.director + "', '" + m.genre + "', '" + m.time + "', '" + m.actor
						+ "', '" + m.rating + "', '" + m.country + "')";
				System.out.println(s);
				stmt.executeUpdate(s);
				v.model.setNumRows(0);
				getMovie();
			} else {
				System.out.println("삽입할 문장이 없습니다.");
			}
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
			v.model.setNumRows(0);
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
				v.model.setNumRows(0);
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

	public void startButton() {
		v.button[0].setEnabled(true);
		v.button[1].setEnabled(false);
		v.button[2].setEnabled(false);
	}

	public static void main(String[] args) {
		new teamMovie();
	}
}
