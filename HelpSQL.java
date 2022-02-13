import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;


public class HelpSQL {
	
	public static Properties prop = new HelpSQL().loadProp();
	
	public Properties loadProp() {
		Properties prop = null;
		try(InputStream stream = this.getClass().getResourceAsStream("info.properties")){
			prop = new Properties();
			prop.load(stream);
		}
		catch(Exception exp) {}
		return prop;
	}
	
	public static void setAmountList(ArrayList<Item> items, String userName) throws SQLException{
		for(int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
		    String catg = item.getCategory();
		    String name = item.getName();
		    BigDecimal price = item.getPrice();
		    String cur = item.getCurrency();
		    String owner = item.getOwner();
		  	items.get(i).setYouAmount(getAmount(userName,catg,name,price,cur,owner));
		}
	}
	
	public static int getAmount(String userName, String catg, String name, BigDecimal price, String cur, String owner) throws SQLException{
		String url = prop.getProperty("url");
		String userN = prop.getProperty("user");
		String pass = prop.getProperty("pass");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int i = 0;
		try {
			con = DriverManager.getConnection(url,userN,pass); 
			String st = "select amountBuy from user_" + userName + "_cart where itemCategory=? and itemName=? and itemPrice=? and itemCurrency=? and itemOwner=?";
			ps = con.prepareStatement(st);
			ps.setString(1, catg);
			ps.setString(2, name);
			ps.setBigDecimal(3, price);
			ps.setString(4, cur);
			ps.setString(5, owner);
			rs = ps.executeQuery();
			if(rs.next())
				i = rs.getInt(1);
		}
		finally {
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
			if(con != null)
				con.close();
		}
		return i;
	}
	
	public static boolean PassEquals(String userName, String password) throws SQLException{
		String url = prop.getProperty("url");
		String userN = prop.getProperty("user");
		String pass = prop.getProperty("pass");
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		boolean b = false;
		try {
			con = DriverManager.getConnection(url,userN,pass); 
			String str = "select userPassword from Users where userName=?";
			pstm = con.prepareStatement(str);
			pstm.setString(1, userName);
			rs = pstm.executeQuery();
			if(rs.next())
				if(rs.getString("userPassword").equals(password))
					b = true;
		}
		finally {
			if(rs != null)
				rs.close();
			if(pstm != null)
				pstm.close();
			if(con != null)
				con.close();
		}
		return b;
	}
	
	public static void CreateUserCart(String userName, String userPass) throws SQLException{
		String url = prop.getProperty("url");
		String userN = prop.getProperty("user");
		String pass = prop.getProperty("pass");
		Connection con = null;
		PreparedStatement stm = null;
		Statement st = null;
		try {
			con = DriverManager.getConnection(url,userN,pass);
			String str = "insert into Users (userName, userPassword)"
					+ "values (?, ?)";
			stm = con.prepareStatement(str);
			stm.setString(1, userName);
			stm.setString(2, userPass);
			stm.executeUpdate();
			stm.close();
			st = con.createStatement();
			String strin = "create table user_" + userName + "_cart "
					+ "(itemCategory varchar(50) not null, " + 
					"itemName varchar(100) not null, " + 
					"itemPrice decimal(8,2) not null, " + 
					"itemCurrency varchar(10) not null, " + 
					"itemQuantity int not null, " + 
					"amountBuy int not null, " + 
					"itemOwner varchar(16) not null" + 
					")";
			st.executeUpdate(strin);
		}
		finally {
			if(stm != null)
				stm.close();
			if(st != null)
				st.close();
			if(con != null)
				con.close();
		}
	}
	
	public static int InsertUserCart(String userName, String category, String name, BigDecimal price, String currency, int quantity, int amount, String owner) throws SQLException{
		String url = prop.getProperty("url");
		String userN = prop.getProperty("user");
		String pass = prop.getProperty("pass");
		Connection con = null;
		PreparedStatement pstm = null;
		int i = 0;
		try {
			con = DriverManager.getConnection(url,userN,pass); 
			String str = "INSERT into user_" + userName + "_cart (itemCategory, itemName, itemPrice, itemCurrency, itemQuantity, amountBuy, itemOwner)"
					+ " values(?,?,?,?,?,?,?)";
			pstm = con.prepareStatement(str);
			pstm.setString(1, category);
			pstm.setString(2, name);
			pstm.setBigDecimal(3, price);
			pstm.setString(4, currency);
			pstm.setInt(5, quantity);
			pstm.setInt(6, amount);
			pstm.setString(7, owner);
			i = pstm.executeUpdate();
		}
		finally {
			if(pstm != null)
				pstm.close();
			if(con != null)
				con.close();
		}
		return i;
	}
	
	public static int RemoveUserCart(String userName, String category, String name, BigDecimal price, String currency, String owner) throws SQLException{
		String url = prop.getProperty("url");
		String userN = prop.getProperty("user");
		String pass = prop.getProperty("pass");
		Connection con = null;
		PreparedStatement ps = null;
		int i = 0;
		try {
			con = DriverManager.getConnection(url,userN,pass); 
			String st = "delete from user_" + userName + "_cart where itemCategory=? and itemName=? and itemPrice=? and itemCurrency=? and itemOwner=?";
			ps = con.prepareStatement(st);
			ps.setString(1, category);
			ps.setString(2, name);
			ps.setBigDecimal(3, price);
			ps.setString(4, currency);
			ps.setString(5, owner);
			i = ps.executeUpdate();
		}
		finally {
			if(ps != null)
				ps.close();
			if(con != null)
				con.close();
		}
		return i;
	}
	
	public static int UpdateUserCart(String userName, String category, String name, BigDecimal price, String currency, int quantity, int amount, String owner) throws SQLException{
		String url = prop.getProperty("url");
		String userN = prop.getProperty("user");
		String pass = prop.getProperty("pass");
		Connection con = null;
		PreparedStatement ps = null;
		int i = 0;
		try {
			if(amount == 0) {
				return RemoveUserCart(userName, category, name, price, currency, owner);
			}
			con = DriverManager.getConnection(url,userN,pass); 
			String str = "update user_" + userName + "_cart set itemQuantity=?, amountBuy=? where itemCategory=? and itemName=? and itemPrice=? and itemCurrency=? and itemOwner=?";
			ps = con.prepareStatement(str);
			ps.setInt(1, quantity);
			ps.setInt(2, amount);
			ps.setString(3, category);
			ps.setString(4, name);
			ps.setBigDecimal(5, price);
			ps.setString(6, currency);
			ps.setString(7, owner);
			i = ps.executeUpdate();
		}
		finally {
			if(ps != null)
				ps.close();
			if(con != null)
				con.close();
		}
		return i;
	}
	
	public static int InsertToDetabase(String catg, String name, BigDecimal dm, String cur, int quantity, String owner) throws SQLException{
		String url = prop.getProperty("url");
		String userN = prop.getProperty("user");
		String pass = prop.getProperty("pass");
		Connection con = null;
		PreparedStatement pstm = null;
		int i = 0;
		try {
			con = DriverManager.getConnection(url,userN,pass); 
			String str = "INSERT into " + catg + " (itemName, itemPrice, itemCurrency, itemQuantity, itemOwner)"
					+ " values(?, ?, ?, ?, ?)";
			pstm = con.prepareStatement(str);
			pstm.setString(1, name);
			pstm.setBigDecimal(2, dm);
			pstm.setString(3, cur);
			pstm.setInt(4, quantity);
			pstm.setString(5, owner);
			i = pstm.executeUpdate();
		}
		finally {
			if(pstm != null)
				pstm.close();
			if(con != null)
				con.close();
		}
		return i;
	}
	
	public static void RemoveFromDetabase(String category, String name, BigDecimal price, String currency, String owner) throws SQLException{
		String url = prop.getProperty("url");
		String userN = prop.getProperty("user");
		String pass = prop.getProperty("pass");
		Connection con = null;
		PreparedStatement ps = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(url,userN,pass); 
			String str = "delete from " + category + " where itemName=? and itemPrice=? and itemCurrency=? and itemOwner=?";
			ps = con.prepareStatement(str);
			ps.setString(1, name);
			ps.setBigDecimal(2, price);
			ps.setString(3, currency);
			ps.setString(4, owner);
			ps.executeUpdate();
			String str2 = "select userName from Users";
		    stm = con.createStatement();
		    rs = stm.executeQuery(str2);
		    while(rs.next())
		    	RemoveUserCart(rs.getString(1), category, name, price, currency, owner);
		}
		finally {
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
			if(stm != null)
				stm.close();
			if(con != null)
				con.close();
		}
	}
	
	public static int UpdateToDetabase(String category, String name, BigDecimal price, String currency, int quantity, String owner) throws SQLException{
		String url = prop.getProperty("url");
		String userN = prop.getProperty("user");
		String pass = prop.getProperty("pass");
		Connection con = null;
		PreparedStatement ps = null;
		Statement stm = null;
		ResultSet rs = null;
		int num, i;
		try {
			con = DriverManager.getConnection(url,userN,pass); 
			String str = "update " + category + " set itemQuantity=? where itemName=? and itemPrice=? and itemCurrency=? and itemOwner=?";
			ps = con.prepareStatement(str);
			ps.setInt(1, quantity);
			ps.setString(2, name);
			ps.setBigDecimal(3, price);
			ps.setString(4, currency);
			ps.setString(5, owner);
			i = ps.executeUpdate();
			String str2 = "select userName from Users";
			stm = con.createStatement();
			rs = stm.executeQuery(str2);
			while(rs.next()) {
				num = getAmount(rs.getString(1), category, name, price, currency, owner);
				num = Math.min(num, quantity);
				UpdateUserCart(rs.getString(1), category, name, price, currency, quantity, num, owner);
			}
		}
		finally {
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
			if(stm != null)
				stm.close();
			if(con != null)
				con.close();
		}
		return i;
	}
	
	public static boolean UserExist(String userName) throws SQLException{
		String url = prop.getProperty("url");
		String userN = prop.getProperty("user");
		String pass = prop.getProperty("pass");
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		boolean b = false;
		try {
			con = DriverManager.getConnection(url,userN,pass); 
			String str = "select userPassword from Users where userName=?";
			pstm = con.prepareStatement(str);
			pstm.setString(1, userName);
			rs = pstm.executeQuery();
			if(rs.next())
				b = true;
		}
		finally {
			if(rs != null)
				rs.close();
			if(pstm != null)
				pstm.close();
			if(con != null)
				con.close();
		}
		return b;
	}
	
}
