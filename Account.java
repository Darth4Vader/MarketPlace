import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Account{
	
	private static JButton ButtonAccount(int x, int y, int w, int h) { 
		JButton btn = new JButton("Back to Account");
		btn.addActionListener(new ActionListener() { 
			
			public void actionPerformed(ActionEvent e) {
				try {
					Market.setFrame(TabAccount());
				}
				catch(SQLException exp) {
				}
				
			}
		});
		btn.setBounds(x, y, w, h);
		return btn;
	}
	
	private static JButton ButtonUpdate(ArrayList<Item> items, String str) {
		JButton btn = new JButton("Update Cart");
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					update(items, str);
					if(str.equals("Cart"))
						Market.setFrame(TabCart());
					else
						Market.setFrame(TabYourItems());
					Market.Message("Updated " + str);
				}
				catch(SQLException exp) {
				}
				
			}
		});
		return btn;
	}
	
	private static JButton ButtonRemoveAll(ArrayList<Item> items, String str) {
		JButton btn = new JButton("Remove All");
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					remove(items, str);
					if(str.equals("Cart"))
						Market.setFrame(TabCart());
					else
						Market.setFrame(TabYourItems());
					Market.Message("Removed All");
				}
				catch(SQLException exp) {
				}
				
			}
		});
		return btn;
	}
	
	private static JButton ButtonRemove(Item item, String str) {
		JButton btn = new JButton("Remove");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				    String name = item.getName();
				    String currency = item.getCurrency();
				    BigDecimal price = item.getPrice();
				    String owner = item.getOwner();
				    String category = item.getCategory();
				    if(str.equals("")) {
				    	HelpSQL.RemoveUserCart(Market.userName,category,name,price,currency, owner);
						Market.setFrame(TabCart());
				    }
				    else {
				    	HelpSQL.RemoveFromDetabase(category, name, price, currency, owner);
						Market.setFrame(TabYourItems());
				    }
					Market.Message("Removed");
				}
				catch(SQLException exp) {
				}
			}
			
		});
		return btn;
	}
	
	private static boolean update(ArrayList<Item> items, String str) throws SQLException{
		boolean b = false;
		for(int i = 0;i < items.size(); i++) {
		  String name = items.get(i).getName();
		  String currency = items.get(i).getCurrency();
		  BigDecimal price = items.get(i).getPrice();
		  String owner = items.get(i).getOwner();
		  String category = items.get(i).getCategory();
		  int quantity = items.get(i).getQuantity();
		  int amount = items.get(i).getSelectedAmount();
		  if(items.get(i).getYourAmount() != amount)
			  if(str.equals("Cart")) {
				  if(HelpSQL.UpdateUserCart(Market.userName, category, name, price, currency, quantity, amount, owner)!=0)
					  b = true;}
			  else {
				  if(HelpSQL.UpdateToDetabase(category, name, price, currency, amount, Market.userName)!=0)
					  b = true;}
		}
		return b;
	}
	
	private static void remove(ArrayList<Item> items, String str) throws SQLException{
		for(int i = 0;i < items.size(); i++) {
			
		  String name = items.get(i).getName();
		  String currency = items.get(i).getCurrency();
		  BigDecimal price = items.get(i).getPrice();
		  String owner = items.get(i).getOwner();
		  String category = items.get(i).getCategory();
		  if(str.equals("Cart"))
			  HelpSQL.RemoveUserCart(Market.userName, category, name, price, currency, owner);
		  else 
			  HelpSQL.RemoveFromDetabase(category, name, price, currency, Market.userName);
		}
	}
	
	
	private static void BuyCart(ArrayList<Item> items) throws SQLException{
		for(int i = 0;i < items.size(); i++) {
			
		    String name = items.get(i).getName();
			  String currency = items.get(i).getCurrency();
			  BigDecimal price = items.get(i).getPrice();
			  String owner = items.get(i).getOwner();
			  String category = items.get(i).getCategory();
			  int quantity = items.get(i).getQuantity();
			  int amount = items.get(i).getYourAmount();
		    int num = quantity - amount;
		  System.out.println(num);
		    HelpSQL.RemoveUserCart(Market.userName, category, name, price, currency, owner);
		  if(num <= 0)
			  HelpSQL.RemoveFromDetabase(category, name, price, currency, owner);
		  else
			  HelpSQL.UpdateToDetabase(category, name, price, currency, num, owner); 
			}
		}
	
	public static void IsLogin() throws SQLException{
		if(Market.userName.equals(""))
			Market.setFrame(TabLogin());
		else
			Market.setFrame(TabAccount());
	}
	
	public static JPanel TabAccount() throws SQLException{
		JLabel jb1 = Market.LabelTab(0, 0, 300, 200, "TabCart");
		jb1.setText("See Your Cart");
		jb1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel jb2 = Market.LabelTab(310, 0, 300, 200, "TabAddItem");
		jb2.setText("Add New Item");
		jb2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel jb3 = Market.LabelTab(620, 0, 300, 200, "TabYourItems");
		jb3.setText("Your Items");
		jb3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Font font = new Font(jb1.getFont().getName(), jb1.getFont().getStyle(), 16);
		jb1.setFont(font);
		jb2.setFont(font);
		jb3.setFont(font);
		JPanel jp = new JPanel(null);
		jp.add(jb1);
		jp.add(jb2);
		jp.add(jb3);
		return jp;
	}
	
	public static JPanel TabYourItems() throws SQLException{
		String url = HelpSQL.prop.getProperty("url");
		String userN = HelpSQL.prop.getProperty("user");
		String pass = HelpSQL.prop.getProperty("pass");
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		String str;
		String[] arr  = Market.category;
		ArrayList<Item> items = new ArrayList<Item>();
		try {
			con = DriverManager.getConnection(url, userN, pass);
			for(int i = 0;i < arr.length; i++) {
				try {
					str = "select * from " + arr[i];
					System.out.println(str);
					stm = con.createStatement();
					rs = stm.executeQuery(str);
					while(rs.next()) {
						if(rs.getString("itemOwner").equals(Market.userName)) {
							String name = rs.getString("itemName");
							BigDecimal price = rs.getBigDecimal("itemPrice");
							String currency = rs.getString("itemCurrency");
							int quantity = rs.getInt("itemQuantity");
							Item item = new Item(arr[i], name, price, currency, quantity, quantity, 0, 10001, Market.userName);
							item.setButton(ButtonRemove(item, "1"));
							items.add(item);
						}
					}
				}
				finally {
					if(rs != null)
						rs.close();
					if(stm != null)
						stm.close();
				}
			}
		}
		finally {
			if(con != null)
				con.close();
		}
		JButton btn2 = ButtonUpdate(items, "");
		btn2.setBounds(310, 0, 150, 50);
		JPanel jp = new JPanel(null);
		jp.setOpaque(false);
		if(items.size() == 0) {
			JLabel lb = new JLabel("0 Items Owned", SwingConstants.CENTER);
			lb.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			Font font = new Font(lb.getFont().getName(), lb.getFont().getStyle(), 16);
			lb.setFont(font);
			lb.setBounds(300, 0, 200, 100);
			jp.add(lb);
			return jp;
		}
		JButton btn = ButtonAccount(470, 0, 150, 50);
		JButton btn3 = ButtonRemoveAll(items, "");
		btn3.setBounds(0, 0, 150, 50);
		jp.add(btn);
		jp.add(btn2);
		jp.add(btn3);
		jp.setBounds(0, 200, 1000, 50);
		return HelpSwing.getItemsPanel(items, "Relevance", jp);
	}
	
	private static JPanel TabLogin() {
		JLabel lbl_userName = Label(10, 50, 100, 30, "User Name :");
		JTextField text_userName = new JTextField();
		text_userName.setBounds(110, 50, 130, 30);
		JLabel lbl_userPass = Label(10, 90, 100, 30, "User Password :");
		JTextField text_userPass = new JTextField();
		text_userPass.setBounds(110, 90, 130, 30);
		JButton btn = new JButton("enter");
		btn.setBounds(130, 140, 100, 100);
		JPanel jp = new JPanel(null);
		JLabel newlbl = Market.LabelTab(300, 0, 100, 200, "TabCreateAccount");
		newlbl.setText("create new : ");
		newlbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jp.add(lbl_userName);
		jp.add(text_userName);
		jp.add(lbl_userPass);
		jp.add(text_userPass);
		jp.add(newlbl);
		jp.add(btn);
		jp.setBounds(0,100,1000,300);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
				if(text_userName.getText().equals("") != true && text_userPass.getText().equals("") != true) {
					try {
						boolean b = HelpSQL.PassEquals(text_userName.getText(), text_userPass.getText());
						if(b == true) {
							Market.userName = text_userName.getText();
							Market.setFrame(TabAccount());
						}
						else
							Market.Message("UserName or Password Incorrect");
					}
					catch(Exception exp) {
					}
				}
				else
					Market.Message("1 or more information is missing");
			}
		});
		jp.setOpaque(false);
		return jp;
	}
	
	public static JPanel TabCreateAccount() {
		JLabel lbl_userName = Label(10, 10, 150, 30, "User Name :");
		JTextField text_userName = new JTextField();
		text_userName.setBounds(160, 10, 150, 30);
		JLabel lbl_userPass = Label(10, 40, 150, 30, "User Password :");
		JTextField text_userPass = new JTextField();
		text_userPass.setBounds(160, 40, 150, 30);
		JLabel lbl_userPassCon = Label(10, 70, 150, 30, "Confirm User Password :");
		JTextField text_userPassCon = new JTextField();
		text_userPassCon.setBounds(160, 70, 150, 30);
		JButton btn = new JButton("enter");
		btn.setBounds(100, 120, 100, 100);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
				try {
					if(HelpSQL.UserExist(text_userName.getText()) != true) {
						if(text_userPass.getText().equals(text_userPassCon.getText())) {
							Market.userName = text_userName.getText();
							HelpSQL.CreateUserCart(text_userName.getText(), text_userPass.getText());
							Market.setFrame(TabAccount());
							Market.Message("User Created");
						}
						else
							Market.Message("Confirm Password not matching Password");
					}
					else
						Market.Message("UserName taken");
				}
				catch(SQLException exp) {
				}
			}
		});
		JPanel jp = new JPanel(null);
		jp.add(lbl_userName);
		jp.add(text_userName);
		jp.add(lbl_userPass);
		jp.add(text_userPass);
		jp.add(lbl_userPassCon);
		jp.add(text_userPassCon);
		jp.add(btn);
		return jp;
	}
	
	public static JLabel Label(int x, int y, int w, int h, String str) {
		JLabel lb = new JLabel(str);
		lb.setBounds(x, y, w, h);
		lb.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		return lb;
	}
	
	public static JPanel TabAddItem() {
		JTextField txt1 = new JTextField();
		txt1.setBounds(80, 0, 100, 50);
		String[] listCurrency = {"USD","EUR","ILS","GPB","CAD"};
		String[] listCategory = {"All", "Discs", "Toys", "Clothes", "Books"};
		JComboBox<String> box1 = new JComboBox<String>(listCategory);
		box1.setBounds(260, 0, 100, 50);
		JComboBox<String> box2 = new JComboBox<String>(listCurrency);
		box2.setSelectedItem(Market.newCurrency);
		box2.setBounds(260, 50, 100, 50);
		JTextField txt3 = HelpSwing.TextFieldInt();
		txt3.setBounds(80, 100, 100, 50);
		JLabel lb1 = Label(0, 0, 80, 50, "Name");
		JLabel lb2 = Label(180, 0, 80, 50, "Category");
		JLabel lb3 = Label(0, 50, 80, 50, "Price");
		JTextField txt2 = HelpSwing.TextFieldDecimal();
		txt2.setBounds(80, 50, 100, 50);
		JLabel lb4 = Label(180, 50, 80, 50, "Currency");
		JLabel lb5 = Label(0, 100, 80, 50, "Quantity");
		JButton btn = new JButton("Add");
		btn.setBounds(0, 150, 80, 50);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
				try {
					if(txt1.getText().equals("") != true && txt2.getText().equals("") != true && txt3.getText().equals("") != true) {
						String catg = box1.getSelectedItem().toString();
						if(catg.equals("All"))
							catg = "Other";
						BigDecimal bd = new BigDecimal(txt2.getText()).setScale(2, RoundingMode.HALF_UP);
						int m = Integer.parseInt(txt3.getText());
						if(HelpSQL.UpdateToDetabase(catg, txt1.getText(), bd, box2.getSelectedItem().toString(), m, Market.userName) == 0) {
							HelpSQL.InsertToDetabase(catg, txt1.getText(), bd, box2.getSelectedItem().toString(), m, Market.userName);
							Market.Message("Added New Item");
						}
						else
							Market.Message("Updated Existing Item");
						}
					else
						Market.Message("1 or more information is missing");
				}
				catch(Exception exp) {
				}
			}
		});
		JButton btn2 = ButtonAccount(500, 150, 150, 50);
		btn2.setBounds(500, 150, 150, 50);
		JPanel pnl = new JPanel(null);
		pnl.add(txt1);	
		pnl.add(box1);
		pnl.add(txt2);
		pnl.add(box2);
		pnl.add(txt3);
		pnl.add(lb1);
		pnl.add(lb2);
		pnl.add(lb3);
		pnl.add(lb4);
		pnl.add(lb5);
		pnl.add(btn);
		pnl.add(btn2);
		pnl.setOpaque(false);
		pnl.setBounds(0, 0, 1000, 400);
		return pnl;
	}
	
	public static JPanel TabCart() throws SQLException{
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		BigDecimal totalPrice = new BigDecimal(0);
		ArrayList<Item> items = new ArrayList<Item>();
		try {
			con = DriverManager.getConnection(HelpSQL.prop.getProperty("url"),
					HelpSQL.prop.getProperty("user"),HelpSQL.prop.getProperty("pass")); 
			String str = "SELECT * from user_" + Market.userName + "_cart";
			stm = con.createStatement();
			rs = stm.executeQuery(str);
			while(rs.next()) {
				String catg = rs.getString("itemCategory");
				String name = rs.getString("itemName");
				BigDecimal price = rs.getBigDecimal("itemPrice");
				String currency = rs.getString("itemCurrency");
				int quantity = rs.getInt("itemQuantity");
				int amount = rs.getInt("amountBuy");
				String owner = rs.getString("itemOwner");
				BigDecimal am = new BigDecimal(amount);
				totalPrice = totalPrice.add(HelpSwing.values(price, currency, Market.newCurrency).multiply(am));
				Item item = new Item(catg, name, price, currency, quantity, amount, 0, quantity, owner);
				item.setButton(ButtonRemove(item, ""));
				items.add(item);
			}
		}
		finally {
			if(rs != null)
				rs.close();
			if(stm != null)
				stm.close();
			if(con != null)
				con.close();
		}
		String priceCart = Market.newCurrency + " " + totalPrice;
		if(totalPrice.equals(new BigDecimal(0)))
			priceCart = "Cart Empty";
		JPanel jp = new JPanel(null);
		JButton btn = new JButton("Buy");
		JLabel jb1 = Label(70, 0, 250, 50, priceCart);
		Font font = new Font(jb1.getFont().getName(), jb1.getFont().getStyle(), 16);
		jb1.setFont(font);
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					BuyCart(items);
					Market.setFrame(TabCart());
					if(jb1.getText().equals("Cart Empty") != true)
						Market.Message("Bought For " + jb1.getText());
					else
						Market.Message("Cart Empty");
				}
				catch(SQLException exp) {
				}
				
			}
		});
		btn.setBounds(0, 0, 70, 50);
		JButton btn1 = ButtonUpdate(items, "Cart");
		btn1.setBounds(320, 0, 150, 50);
		JButton btn2 = ButtonRemoveAll(items, "Cart");
		btn2.setBounds(470, 0, 150, 50);
		JButton btn3 = ButtonAccount(620, 0, 130, 50);
		jp.add(btn);
		jp.add(btn1);
		jp.add(btn2);
		jp.add(jb1);
		jp.add(btn2);
		jp.add(btn3);
		jp.setOpaque(false);
		jp.setBounds(0, 200, 1000, 50);
		return HelpSwing.getItemsPanel(items,"All",jp);
	}
	
}