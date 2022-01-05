import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Account{
	
	private String newCur;
	private String name;
	private HelpSQL help = new HelpSQL();
	private HelpSwing helpSwing = new HelpSwing();
	private Amazon amazon;
	
	public Account(String name, String newCur) {
		this.name = "";
		this.newCur = newCur;
		this.amazon = new Amazon();
	}
	
	public String getUserName() {
		return this.name;
	}
	
	public void setCurrency(String newCur) {
		this.newCur = newCur;
	}
	
	public String getCurrency() {
		return this.newCur;
	}
	
	public void IsLogin() throws Exception{
		if(this.name.equals(""))
			this.amazon.setFrame(TabLogin());
		else
			this.amazon.setFrame(TabAccount());
	}
	
	private JPanel TabAccount() throws Exception{
		JLabel jb1 = this.amazon.lbl(0, 0, 400, 200, TabCart());
		jb1.setText("See Your Cart");
		JLabel jb2 = this.amazon.lbl(410, 0, 500, 200, TabAddItem());
		jb2.setText("Add New Item");
		Font font = new Font(jb1.getFont().getName(), jb1.getFont().getStyle(), 16);
		jb1.setFont(font);
		jb2.setFont(font);
		JPanel jp = new JPanel(null);
		jp.add(jb1);
		jp.add(jb2);
		jp.setBounds(0, 100, 1000, 300);
		jp.setOpaque(false);
		return jp;
	}
	
	private JPanel TabLogin() {
		JLabel lbl1 = new JLabel("User Name :");
		lbl1.setBounds(10, 50, 100, 30);
		lbl1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JTextField user_name = new JTextField();
		user_name.setBounds(110, 50, 130, 30);
		JLabel lbl2 = new JLabel("User Password :");
		lbl2.setBounds(10, 90, 100, 30);
		lbl2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JTextField user_pass = new JTextField();
		user_pass.setBounds(110, 90, 130, 30);
		JButton btn = new JButton("enter");
		btn.setBounds(130, 140, 100, 100);
		JPanel jp = new JPanel(null);
		JLabel newlbl = this.amazon.lbl(300, 0, 100, 200, TabCreateAccount());
		newlbl.setText("create new : ");
		newlbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jp.add(lbl1);
		jp.add(user_name);
		jp.add(lbl2);
		jp.add(user_pass);
		jp.add(newlbl);
		jp.add(btn);
		jp.setBounds(0,100,1000,300);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
				if(user_name.getText().equals("") != true && user_pass.getText().equals("") != true) {
					try {
						Connection con = DriverManager.getConnection(
								"jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
						String str = "select userPassword from Users where userName=?";
						PreparedStatement stm = con.prepareStatement(str);
						stm.setString(1, user_name.getText());
						ResultSet rs = stm.executeQuery();
						boolean b = false;
						if(rs.next())
							if(rs.getString("userPassword").equals(user_pass.getText())) {
								Account.this.name = user_name.getText();
								b = true;
							}
						rs.close();
						stm.close();
						con.close();
						if(b == true)
							Account.this.amazon.setFrame(TabAccount());
						else
							Account.this.amazon.Message("UserName or Password Incorrect");
					}
					catch(Exception exp) {
					}
				}
				else
					Account.this.amazon.Message("1 or more information is missing");
			}
		});
		jp.setOpaque(false);
		return jp;
	}
	
	private JPanel TabCreateAccount() {
		JLabel lbl1 = new JLabel("User Name :");
		lbl1.setBounds(10, 10, 150, 30);
		lbl1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JTextField user_name = new JTextField();
		user_name.setBounds(160, 10, 150, 30);
		JLabel lbl2 = new JLabel("User Password :");
		lbl2.setBounds(10, 40, 150, 30);
		lbl2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JTextField user_pass = new JTextField();
		user_pass.setBounds(160, 40, 150, 30);
		JLabel lbl3 = new JLabel("Confirm User Password :");
		lbl3.setBounds(10, 70, 150, 30);
		lbl3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JTextField user_pass_confirm = new JTextField();
		user_pass_confirm.setBounds(160, 70, 150, 30);
		JButton btn = new JButton("enter");
		btn.setBounds(100, 120, 100, 100);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
				try {
					if(Account.this.help.UserExist(user_name.getText()) != true) {
						if(user_pass.getText().equals(user_pass_confirm.getText())) {
							Account.this.name = user_name.getText();
							Account.this.help.CreateUserCart(user_name.getText(), user_pass.getText());
							Account.this.amazon.setFrame(TabAccount());
							Account.this.amazon.Message("User Created");
						}
						else
							Account.this.amazon.Message("Confirm Password not matching Password");
					}
					else
						Account.this.amazon.Message("UserName taken");
				}
				catch(Exception exp) {
					
				}
			}
		});
		JPanel jp = new JPanel(null);
		jp.add(lbl1);
		jp.add(user_name);
		jp.add(lbl2);
		jp.add(user_pass);
		jp.add(lbl3);
		jp.add(user_pass_confirm);
		jp.add(btn);
		jp.setBounds(0,100,1000,300);
		jp.setOpaque(false);
		return jp;
	}
	
	private boolean updateCart(JPanel jp) {
		boolean b = false;
		for(int i = 0;i < jp.getComponentCount()-3; i++) {
		  String name = ((JLabel)((JPanel)jp.getComponent(i)).getComponent(0)).getName();
		  String cur = ((JLabel)((JPanel)jp.getComponent(i)).getComponent(1)).getName();
		  BigDecimal dm = new BigDecimal(((JLabel)((JPanel)jp.getComponent(i)).getComponent(2)).getName());
		  String user = ((JLabel)((JPanel)jp.getComponent(i)).getComponent(4)).getName();
		  String catg = ((JLabel)((JPanel)jp.getComponent(i)).getComponent(5)).getName();
		  JSpinner spin =((JSpinner)((JPanel)jp.getComponent(i)).getComponent(6));
		  JTextField txt = (JTextField)(((JSpinner.DefaultEditor) spin.getEditor()).getTextField());
		  if(this.help.UpdateUserCart(this.name, catg, name, dm, cur, Integer.parseInt(spin.getName()), Integer.parseInt(txt.getText()), user)!=0)
			  b = true;
		}
		System.out.println(b);
		return b;
	}
	
	private JButton createButton(String user, String catg, String name, BigDecimal price, String cur, String owner) {
		JButton btn = new JButton("Remove");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Account.this.help.RemoveUserCart(user,catg,name,price,cur, owner);
					Account.this.amazon.Message("Removed");
					Account.this.amazon.setFrame(TabCart());
				}
				catch(Exception exp) {
				}
			}
			
		});
		return btn;
	}
	
	private void BuyCart(JPanel jp) {
		for(int i = 0;i < jp.getComponentCount()-3; i++) {
			  String name = ((JLabel)((JPanel)jp.getComponent(i)).getComponent(0)).getName();
			  String cur = ((JLabel)((JPanel)jp.getComponent(i)).getComponent(1)).getName();
			  BigDecimal dm = new BigDecimal(((JLabel)((JPanel)jp.getComponent(i)).getComponent(2)).getName());
			  String user = ((JLabel)((JPanel)jp.getComponent(i)).getComponent(4)).getName();
			  String catg =((JLabel)((JPanel)jp.getComponent(i)).getComponent(5)).getName();
			  JSpinner spin =((JSpinner)((JPanel)jp.getComponent(i)).getComponent(6));
			  JTextField txt = (JTextField)(((JSpinner.DefaultEditor) spin.getEditor()).getTextField());
			  int num = Integer.parseInt(spin.getName()) - Integer.parseInt(txt.getName());
			  this.help.RemoveUserCart(this.name, catg, name, dm, cur, user);
			  if(num <= 0) {
				  this.help.RemoveFromDetabase(catg, name, dm, cur, user);
				  try {
					  Connection con = DriverManager.getConnection(
							 "jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
					  String str = "select userName from Users";
					  Statement stm = con.createStatement();
					  ResultSet rs = stm.executeQuery(str);
					  while(rs.next())
						  this.help.RemoveUserCart(rs.getString(1), catg, name, dm, cur, user);
					  stm.close();
					  rs.close();
					  con.close();
				  }
				  catch(Exception exp) {
				  }
			  }
			  else {
				  this.help.UpdateToDetabase(catg, name, dm, cur, num, user);
				  try {
					  Connection con = DriverManager.getConnection(
							 "jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
					  String str = "select userName from Users";
					  Statement stm = con.createStatement();
					  ResultSet rs = stm.executeQuery(str);
					  while(rs.next())
						  this.help.UpdateUserCart(rs.getString(1), catg, name, dm, cur, num, checkDiff(rs.getString(1), catg, name, dm, cur, user, num), user);
					  stm.close();
					  rs.close();
					  con.close();
				  }
				  catch(Exception exp) {
				  } 
			  }
			}
		}
	
	public int checkDiff(String userName, String catg, String name, BigDecimal dm, String cur, String user, int quan) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
			String st = "select amountBuy from user_" + userName + "_cart where itemCategory=? and itemName=? and itemPrice=? and itemCurrency=? and itemOwner=?";
			PreparedStatement ps = con.prepareStatement(st);
			ps.setString(1, catg);
			ps.setString(2, name);
			ps.setBigDecimal(3, dm);
			ps.setString(4, cur);
			ps.setString(5, user);
			ResultSet rs = ps.executeQuery();
			int k = 0;
			if(rs.next())
				k = Math.min(quan, rs.getInt(1));
			ps.close();
			rs.close();
			con.close();
			return k;
		}
		catch(Exception exp) {
			return 0;
		}
	}
	
	
	public JPanel TabAddItem() {
		JTextField txt1 = new JTextField();
		txt1.setBounds(80, 100, 100, 50);
		String[] listCurrency = {"USD","EUR","ILS","GPB","CAD"};
		String[] listCategory = {"All", "Discs", "Toys", "Clothes", "Books"};
		JComboBox<String> box1 = new JComboBox<String>(listCategory);
		box1.setBounds(260, 100, 100, 50);
		JComboBox<String> box2 = new JComboBox<String>(listCurrency);
		box2.setSelectedItem(this.newCur);
		box2.setBounds(260, 150, 100, 50);
		JTextField txt3 = new JTextField();
		txt3.setBounds(80, 200, 100, 50);
		((AbstractDocument) txt3.getDocument()).setDocumentFilter(new DocumentFilter() {
			
		    @Override
		    public void replace(DocumentFilter.FilterBypass fb, int offset,
		                        int length, String text, AttributeSet attrs)
		            throws BadLocationException {
		    	String str = text;
		    	String newStr ="";
		    	for(int i = 0;i < str.length(); i++)
		    		if(isNum(str.charAt(i)+"")) {
		    			newStr = newStr + str.charAt(i);
		    			if(Integer.parseInt(txt3.getText()+newStr) >= 10000)
		    		    	break;
		    		}
    			if(newStr.equals("") != true && Integer.parseInt(txt3.getText()+newStr) <= 10000)
    				fb.replace(offset, length, newStr, attrs);
		    }
		    
        	public boolean isNum(String str) {
        		try {
        			Integer.parseInt(str);
        			return true;
        		}
        		catch(Exception exp) {
        			return false;
        		}
        	}
        });
		
		JLabel lb1 = new JLabel("Name");
		lb1.setBounds(0, 100, 80, 50);
		lb1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel lb2 = new JLabel("Category");
		lb2.setBounds(180, 100, 80, 50);
		lb2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel lb3 = new JLabel("Price");
		lb3.setBounds(0, 150, 80, 50);
		lb3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JTextField txt2 = new JTextField();
		((AbstractDocument) txt2.getDocument()).setDocumentFilter(new DocumentFilter() {
			
		    @Override
		    public void replace(DocumentFilter.FilterBypass fb, int offset,
		                        int length, String text, AttributeSet attrs)
		            throws BadLocationException {
		    	String str = text;
		    	String newStr ="";
		    	boolean b = isDot(txt2.getText());
		    	for(int i = 0;i < str.length(); i++)
		    		if(isNum(str.charAt(i)+"") || (str.charAt(i) == '.' && b == false)) {
		    			b = true;
		    			newStr = newStr + str.charAt(i);
		    			if(txt2.getText().length()+newStr.length() >= 9)
		    		    	break;
		    		}
    			if(txt2.getText().length()+newStr.length() <= 9)
    				fb.replace(offset, length, newStr, attrs);
		    }
		    
		    public boolean isDot(String str) {
		    	for(int i = 0;i < str.length(); i++)
		    		if(str.charAt(i) == '.')
		    			return true;
		    	return false;
		    }
		    
        	public boolean isNum(String str) {
        		try {
        			Integer.parseInt(str);
        			return true;
        		}
        		catch(Exception exp) {
        			return false;
        		}
        	}
        });
		txt2.setBounds(80, 150, 100, 50);
		JLabel lb4 = new JLabel("Currency");
		lb4.setBounds(180, 150, 80, 50);
		lb4.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel lb5 = new JLabel("Quantity");
		lb5.setBounds(0, 200, 80, 50);
		lb5.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		String userName = this.name;
		JButton btn = new JButton("Add");
		btn.setBounds(0, 250, 80, 50);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
				try {
					if(txt1.getText().equals("") != true && txt2.getText().equals("") != true && txt3.getText().equals("") != true) {
						String catg = box1.getSelectedItem().toString();
						if(catg.equals("All"))
							catg = "Other";
						BigDecimal bd = new BigDecimal(txt2.getText()).setScale(2, RoundingMode.HALF_UP);
						int m = Integer.parseInt(txt3.getText());
						if(Account.this.help.UpdateToDetabase(catg, txt1.getText(), bd, box2.getSelectedItem().toString(), m, userName) == 0) {
							Account.this.help.InsertToDetabase(catg, txt1.getText(), bd, box2.getSelectedItem().toString(), m, userName);
							Account.this.amazon.Message("Added New Item");
						}
						else
							Account.this.amazon.Message("Updated Existing Item");
						}
					else
						Account.this.amazon.Message("1 or more information is missing");
				}
				catch(Exception exp) {
				}
			}
		});
		JPanel pnl = new JPanel();
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
		pnl.setOpaque(false);
		pnl.setLayout(null);
		pnl.setBounds(0, 0, 1000, 400);
		return pnl;
	}
	
	public JPanel TabCart() throws Exception{
		Connection con = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/Amazon","root","Itay1015"); 
		String str = "SELECT * from user_" + this.name + "_cart";
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery(str);
		JPanel jp = new JPanel(null);
		int x = 0, y = 0;
		BigDecimal totalPrice = new BigDecimal(0);
		while(rs.next()) {
			String catg = rs.getString("itemCategory");
			String name = rs.getString("itemName");
			BigDecimal price = rs.getBigDecimal("itemPrice");
			String currency = rs.getString("itemCurrency");
			int quan = rs.getInt("itemQuantity");
			int amount = rs.getInt("amountBuy");
			String owner = rs.getString("itemOwner");
			BigDecimal am = new BigDecimal(amount);
			totalPrice = totalPrice.add(this.helpSwing.values(price, currency, this.newCur).multiply(am));
			JPanel jp1 = this.helpSwing.createLabel(catg, name, price, currency, quan, amount, owner, createButton(this.name, catg, name, price, currency, owner), this.newCur, 0);
			jp1.setLocation(x,y);
			if(x + 154 > 1000) {
				x = 0;
				y = y + 200;
			}
			else
				x = x + 154;
			jp.add(jp1);
		}
		JLabel jb1 = new JLabel(this.newCur + " " + totalPrice);
		Font font = new Font(jb1.getFont().getName(), jb1.getFont().getStyle(), 16);
		jb1.setFont(font);
		jb1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jb1.setOpaque(false);
		jb1.setBounds(120, 200, 250, 50);
		JButton btn = new JButton("Buy");
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					BuyCart(jp);
					Account.this.amazon.setFrame(TabCart());
					Account.this.amazon.Message("Bought For " + jb1.getText());
				}
				catch(Exception exp) {
				}
				
			}
		});
		btn.setBounds(0, 200, 70, 50);
		btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btn.setOpaque(false);
		JButton btn1 = new JButton("Update Cart");
		btn1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					updateCart(jp);
					Account.this.amazon.setFrame(TabCart());
					Account.this.amazon.Message("Updated");
				}
				catch(Exception exp) {
				}
				
			}
		});
		btn1.setBounds(500, 200, 70, 50);
		btn1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btn1.setOpaque(false);
		jp.add(btn);
		jp.add(btn1);
		jp.add(jb1);
		stm.close();
		rs.close();
		con.close();
		jp.setOpaque(false);
		jp.setBounds(0, 100, 1000, 300);
		return jp;
	}
	
}