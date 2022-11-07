package database;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

class CustomerModel {
	String phone;
	String name;
	String email;
	String address;
	String birthDay;
	String createDay;
	String grade;
	int point;

	CustomerModel(String phone,String name,String email,String address,String birthDay,String createDay,String grade, int point){
		this.phone=phone;
		this.name=name;
		this.email=email;
		this.address=address;
		this.birthDay=birthDay;		
		this.createDay=createDay;		
		this.grade=grade;		
		this.point=point;		
	}
}

class CustomerView extends JFrame{
	private static final long serialVersionUID = 1L;
	JPanel[] pnl=new JPanel[3];
	JButton[] btn=new JButton[3];
	JLabel[] lbl=new JLabel[8];
	JTextField[] tf=new JTextField[8];
	JTextArea ta;

	CustomerView(){
		setTitle("고객 관리");
		setSize(750,350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newComponents();
		setComponents();
		addComponents();
		setVisible(true);
	}

	void newComponents(){
		String[] label={"전화번호","이름","e-mail","주소","생년월일","가입일","등급","누적 포인트"};
		String[] button={"추가","수정","삭제"};
		for(int i=0;i<tf.length;i++){
			lbl[i]=new JLabel(label[i]);
			tf[i]=new JTextField();
			if(i<3){
				btn[i]=new JButton(button[i]);
				pnl[i]=new JPanel();
			}
		}
		ta=new JTextArea();
	}

	void setComponents(){
		int[] colLength= {13,10,20,30,10,10,3,5};
		for(int i=0;i<8;i++) {
			tf[i].setColumns(colLength[i]);
		}
		tf[0].setBackground(Color.YELLOW);
		ta.setFont(new Font("굴림체",Font.PLAIN,12));
		//ta.setEditable(false);
		ta.setBackground(Color.lightGray);
		pnl[0].setLayout(new GridLayout(0, 4));
	}
	void addComponents(){
		for(int i=0;i<tf.length;i++){
			pnl[0].add(lbl[i]);
			pnl[0].add(tf[i]);
			if(i<3) {
				pnl[2].add(btn[i]);
			}
		}
		pnl[1].add(new JScrollPane(ta));
		add(pnl[0],BorderLayout.NORTH);
		add(new JScrollPane(ta),BorderLayout.CENTER);
		add(pnl[2],BorderLayout.SOUTH);
	}
}

public class Customer{
	CustomerModel m;
	CustomerView v;
	CustomerActionHandler handler=new CustomerActionHandler();

	SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
	String dTime=formatter.format(new Date());

	Connection con = null;
	Statement stmt = null;
	ResultSet rs=null;

	Customer(){
		v=new CustomerView();
		for(int i=0;i<v.tf.length;i++) {
		    v.tf[i].addActionListener(handler);
		    if(i<3) {
		    	v.btn[i].addActionListener(handler);
		    }
		}
		v.addWindowListener(new WindowHandler());
		disableButtons();
		clearTextFields(0);
	}

	void viewToModel(){
		m=new CustomerModel(v.tf[0].getText(),v.tf[1].getText(),v.tf[2].getText(),v.tf[3].getText(),
				v.tf[4].getText(),v.tf[5].getText(),v.tf[6].getText(),Integer.valueOf((v.tf[7].getText())));
	}

	void modelToView(){
		v.tf[0].setText(m.phone);
		v.tf[1].setText(m.name);
		v.tf[2].setText(m.email);
		v.tf[3].setText(m.address);
		v.tf[4].setText(m.birthDay);
		v.tf[5].setText(m.createDay);
		v.tf[6].setText(m.grade);
		v.tf[7].setText(Integer.toString(m.point));
	}

	void addTextArea(){
		String s;
		s=String.format("%-15s %-8s %-20s %-30s %10s %10s %2s %d\n",m.phone,m.name,
				m.email,m.address,m.birthDay,m.createDay,m.grade,m.point);
		v.ta.append(s);
	}

	class CustomerActionHandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==v.tf[0]){
				searchCustomer();
			}else if(e.getSource()==v.tf[1]) {
				v.tf[2].requestFocus();
			}else if(e.getSource()==v.tf[2]) {
				v.tf[3].requestFocus();
			}else if(e.getSource()==v.tf[3]) {
				v.tf[4].requestFocus();
			}else if(e.getSource()==v.tf[4]) {
				v.tf[5].requestFocus();
			}else if(e.getSource()==v.tf[5]) {
				v.tf[6].requestFocus();
			}else if(e.getSource()==v.tf[6]) {
				v.tf[7].requestFocus();
			}else if(e.getSource()==v.btn[0]){
				addCustomer();
			}else if(e.getSource()==v.btn[1]){
				updateCustomer();
			}else if(e.getSource()==v.btn[2]){
				deleteCustomer();
			}
			getCustomer();					
		}
	}

	class WindowHandler extends WindowAdapter{
		public void windowOpened(WindowEvent e) {
			getCustomer();
		}
	}

	public void getCustomer(){
		makeConnection();
		String sql="";
		sql="SELECT * FROM customer";
		v.ta.setText("");
		try{
			rs=stmt.executeQuery(sql);
			while(rs.next()){
				m=new CustomerModel(rs.getString("phone"),fromMySQL(rs.getString("name")),fromMySQL(rs.getString("email")),
						fromMySQL(rs.getString("address")),rs.getString("birthDay"),rs.getString("createDay"),rs.getString("grade"),rs.getInt("point"));
				addTextArea();
			}
		}catch(SQLException sqle){System.out.println("getData: SQL Error");}
		disConnection();
	}

	public void searchCustomer(){
		for(int i=0;i<v.tf.length;i++) {
			if(i>0) v.tf[i].setEditable(true);
		}
		v.tf[1].requestFocus();
	
		makeConnection();
		String sql="";
		sql="SELECT * FROM customer WHERE phone='"+v.tf[0].getText()+"'";
		System.out.println(sql);
		try{
			rs=stmt.executeQuery(sql);
			if(rs.next()){
				m=new CustomerModel(rs.getString("phone"),fromMySQL(rs.getString("name")),fromMySQL(rs.getString("email")),
						fromMySQL(rs.getString("address")),rs.getString("birthDay"),rs.getString("createDay"),rs.getString("grade"),rs.getInt("point"));
				modelToView();
				v.btn[0].setEnabled(false);
				v.btn[1].setEnabled(true);
				v.btn[2].setEnabled(true);
			}
			else{
				clearTextFields(1);
				v.btn[0].setEnabled(true);
				v.btn[1].setEnabled(false);
				v.btn[2].setEnabled(false);
			}
		}catch(SQLException sqle){System.out.println("isExist: SQL Error");}
		disConnection();
	}

	public void addCustomer(){
		makeConnection();
		viewToModel();
		try {
			String s="";
			s="INSERT INTO customer (phone,name,email,address,birthDay,createDay,grade,point) values ";
			s+="('"+m.phone+"','"+toMySQL(m.name)+"','"+m.email+"','"+toMySQL(m.address)+"','"+m.birthDay+"','"+m.createDay+
					"','"+m.grade+"',"+m.point+")";
			System.out.println(s);
			stmt.executeUpdate(s);

		}catch(SQLException sqle){System.out.println(sqle.getMessage());}
		disConnection();
		disableButtons();
		clearTextFields(0);
	}

	public void updateCustomer(){
		viewToModel();
		makeConnection();
		try {
			String s="";
			s="UPDATE customer SET name='"+toMySQL(m.name)+"',email='"+m.email+"',address='"+toMySQL(m.address)+"',birthDay='"+m.birthDay+
					"',createDay='"+m.createDay+"',grade='"+m.grade+"',point="+m.point+" WHERE phone='"+m.phone+"'";
			System.out.println(s);
			stmt.executeUpdate(s);

		}catch(SQLException sqle){System.out.println("isExist: SQL Error");}
		disConnection();
		disableButtons();
		clearTextFields(0);
	}

	public void deleteCustomer(){
		viewToModel();
		int isDelete=JOptionPane.showConfirmDialog(null,"삭제하시겠습니까 ?");
		System.out.println("DELETE: "+isDelete);
		if(isDelete==0) {
			makeConnection();
			String sql="";
			sql="DELETE FROM customer where phone='"+m.phone+"'";
			System.out.println(sql);
			try{
				stmt.executeUpdate(sql);
			}catch(SQLException sqle){System.out.println("isExist: DELETE SQL Error");}
			disConnection();
		}
		disableButtons();
		clearTextFields(0);
	}

	public void removeAllCustomer(){
		makeConnection();
		String sql="";
		sql="DELETE FROM customer";
		try{
			System.out.println(sql);
			stmt.executeUpdate(sql);
		}catch(SQLException sqle){System.out.println("isExist: remove ALL SQL Error");}
		disConnection();
		disableButtons();
	}

	public String toMySQL(String str){
		try{
			if (str != null)
				return new String(str.getBytes("KSC5601"), "8859_1");
			else
				return null;
		} catch (Exception e) {e.printStackTrace();return null;}
	}

	public String fromMySQL(String str){
		try{
			if (str != null)
				return new String(str.getBytes("8859_1"),"KSC5601");
			else
				return null;
		} catch (Exception e) {e.printStackTrace();return null;}
	}

	public Connection makeConnection(){
		String url="jdbc:mysql://localhost/shop_db?serverTimezone=Asia/Seoul";
		String id="root";
		String password="1234";
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("드라이브 적재 성공");
			con=DriverManager.getConnection(url, id, password);
			stmt=con.createStatement();
			System.out.println("데이터베이스 연결 성공");
		}catch(ClassNotFoundException e){
			System.out.println("드라이버를 찾을 수 없습니다");
			e.getStackTrace();
		}catch(SQLException e){
			System.out.println("연결에 실패하였습니다");			
			e.getStackTrace();
		}
		return con;
	}

	public void disConnection() {
		try{
			rs.close();
			stmt.close();
			con.close();
		}catch(SQLException e){System.out.println(e.getMessage());}
	}

	public void disableButtons() {
		v.btn[0].setEnabled(false);
		v.btn[1].setEnabled(false);
		v.btn[2].setEnabled(false);
	}

	public void clearTextFields(int n) {
		for(int i=n;i<v.tf.length;i++) {
			v.tf[i].setText("");
			if(n!=1 && i>0) v.tf[i].setEditable(false);
		}
		v.tf[5].setText(dTime);
	}
	
	public static void main(String[] args){
		new Customer();
	}	
}