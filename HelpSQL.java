import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelpSQL {
	
	public void CreateUserCart(String user_name, String user_pass) throws Exception{
		Connection con = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/Amazon","root","Itay1015");
		String str = "insert into Users (userName, userPassword)"
				+ "values (?, ?)";
		PreparedStatement stm = con.prepareStatement(str);
		stm.setString(1, user_name);
		stm.setString(2, user_pass);
		stm.executeUpdate();
		stm.close();
		Statement st = con.createStatement();
		String strin = "create table user_" + user_name + "_cart "
				+ "(itemCategory varchar(50) not null, " + 
				"itemName varchar(100) not null, " + 
				"itemPrice decimal(8,2) not null, " + 
				"itemCurrency varchar(10) not null, " + 
				"itemQuantity int not null, " + 
				"amountBuy int not null, " + 
				"itemOwner varchar(16) not null" + 
				")";
		st.executeUpdate(strin);
		st.close();
		con.close();
	}
	
	public int InsertUserCart(String userName, String catg, String name, BigDecimal dm, String cur, int quan, int amount, String owner){
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
			String str = "INSERT into user_" + userName + "_cart (itemCategory, itemName, itemPrice, itemCurrency, itemQuantity, amountBuy, itemOwner)"
					+ " values(?,?,?,?,?,?,?)";
			PreparedStatement pstm = con.prepareStatement(str);
			pstm.setString(1, catg);
			pstm.setString(2, name);
			pstm.setBigDecimal(3, dm);
			pstm.setString(4, cur);
			pstm.setInt(5, quan);
			pstm.setInt(6, amount);
			pstm.setString(7, owner);
			int i = pstm.executeUpdate();
			pstm.close();
			con.close();
			return i;
		}
		catch(Exception exp) {
			return 0;
		}
	}
	
	public int RemoveUserCart(String userName, String catg, String name, BigDecimal dm, String cur, String user) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
			String st = "delete from user_" + userName + "_cart where itemCategory=? and itemName=? and itemPrice=? and itemCurrency=? and itemOwner=?";
			PreparedStatement ps = con.prepareStatement(st);
			ps.setString(1, catg);
			ps.setString(2, name);
			ps.setBigDecimal(3, dm);
			ps.setString(4, cur);
			ps.setString(5, user);
			int i = ps.executeUpdate();
			ps.close();
			con.close();
			return i;
		}
		catch(Exception exp) {
			return 0;
		}
	}
	
	public int UpdateUserCart(String userName, String catg, String name, BigDecimal dm, String cur, int quan, int amount, String owner) {
		try {
			if(amount == 0) {
				return RemoveUserCart(userName, catg, name, dm, cur, owner);
			}
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
			String str = "update user_" + userName + "_cart set itemQuantity=?, amountBuy=? where itemCategory=? and itemName=? and itemPrice=? and itemCurrency=? and itemOwner=?";
			PreparedStatement ps = con.prepareStatement(str);
			ps.setInt(1, quan);
			ps.setInt(2, amount);
			ps.setString(3, catg);
			ps.setString(4, name);
			ps.setBigDecimal(5, dm);
			ps.setString(6, cur);
			ps.setString(7, owner);
			int i = ps.executeUpdate();
			System.out.println(i);
			ps.close();
			con.close();
			return i;
		}
		catch(Exception exp) {
			return 0;
		}
	}
	
	public int InsertToDetabase(String catg, String name, BigDecimal dm, String cur, int quantity, String user) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
			String str = "INSERT into " + catg + " (itemName, itemPrice, itemCurrency, itemQuantity, itemOwner)"
					+ " values(?, ?, ?, ?, ?)";
			PreparedStatement pstm = con.prepareStatement(str);
			pstm.setString(1, name);
			pstm.setBigDecimal(2, dm);
			pstm.setString(3, cur);
			pstm.setInt(4, quantity);
			pstm.setString(5, user);
			int i = pstm.executeUpdate();
			pstm.close();
			con.close();
			return i;
		}
		catch(Exception exp) {
			return 0;
		}
	}
	
	public int RemoveFromDetabase(String catg, String name, BigDecimal dm, String cur, String user) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
			String str = "delete from " + catg + " where itemName=? and itemPrice=? and itemCurrency=? and itemOwner=?";
			PreparedStatement ps = con.prepareStatement(str);
			ps.setString(1, name);
			ps.setBigDecimal(2, dm);
			ps.setString(3, cur);
			ps.setString(4, user);
			int i = ps.executeUpdate();
			ps.close();
			con.close();
			return i;
		}
		catch(Exception exp) {
			return 0;
		}
	}
	
	public int UpdateToDetabase(String catg, String name, BigDecimal dm, String cur, int quan, String user) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
			String str = "update " + catg + " set itemQuantity=itemQuantity+? where itemName=? and itemPrice=? and itemCurrency=? and itemOwner=?";
			PreparedStatement ps = con.prepareStatement(str);
			ps.setInt(1, quan);
			ps.setString(2, name);
			ps.setBigDecimal(3, dm);
			ps.setString(4, cur);
			ps.setString(5, user);
			int i = ps.executeUpdate();
			ps.close();
			con.close();
			return i;
		}
		catch(Exception exp) {
			return 0;
		}
	}
	
	public int getAmount(String user, String catg, String name, BigDecimal price, String cur, String owner) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
			String st = "select amountBuy from user_" + user + "_cart where itemCategory=? and itemName=? and itemPrice=? and itemCurrency=? and itemOwner=?";
			PreparedStatement ps = con.prepareStatement(st);
			ps.setString(1, catg);
			ps.setString(2, name);
			ps.setBigDecimal(3, price);
			ps.setString(4, cur);
			ps.setString(5, user);
			ResultSet rs = ps.executeQuery();
			int i = 0;
			if(rs.next())
				i = rs.getInt(1);
			ps.close();
			rs.close();
			con.close();
			return i;
		}
		catch(Exception exp) {
			return 0;
		}
	}
	
	
	public boolean UserExist(String userName) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
			String str = "select userPassword from Users where userName=?";
			PreparedStatement stm = con.prepareStatement(str);
			stm.setString(1, userName);
			ResultSet rs = stm.executeQuery();
			boolean b = false;
			if(rs.next())
				b = true;
			rs.close();
			stm.close();
			con.close();
			return b;
		}
		catch(Exception exp) {
			return false;
		}
	}
	
}
