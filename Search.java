import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
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
import javax.swing.SwingConstants;

public class Search {

	private ArrayList<Item> items;
	private ArrayList<Integer> num;
	private ArrayList<String> name;
	
	public Search(String str, JComboBox<String> box){
		try {
			this.items = new ArrayList<Item>();
			this.num = new ArrayList<Integer>();
			this.name = createArr(str);
			String catg = box.getSelectedItem().toString();
			if(catg.equals("All") != true)
				check(catg);
			else
				if(catg.equals("All")) {
					for(int i = 1;i < box.getItemCount(); i++)
						check(box.getItemAt(i));
					check("Other");
				}
		}
		catch(SQLException exp) {
		}
	}
	
	private JButton ButtonAdd(int ind) {
		JButton btn = new JButton("Add");
		btn.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(Market.userName.equals("") != true) {
					if(Market.userName.equals(Search.this.items.get(ind).getOwner()) != true)
						try {
							addToCart(ind);
						}
						catch(SQLException exp) {
						}
					else
						Market.Message("Unable to add, item owned by you");
				}
				else
					Market.Message("Login to add");
			}
			
		});
		return btn;
	}
	
	private void addToCart(int ind) throws SQLException{
		Item item = this.items.get(ind);
	    String catg = item.getCategory();
	    String name = item.getName();
	    BigDecimal price = item.getPrice();
	    String cur = item.getCurrency();
	    String owner = item.getOwner();
	  	int quantity = item.getQuantity();
	  	int amount = item.getSelectedAmount();
	  	int yourAmount = item.getYourAmount();
	  	yourAmount = yourAmount + amount;
	  	if(yourAmount > quantity) {
	  		Market.Message("You selcted More Than Allowed = " + yourAmount + ", Limit= " + quantity);
	  	}
	  	else {
			this.items.get(ind).setYouAmount(yourAmount);
			if(HelpSQL.UpdateUserCart(Market.userName, catg, name, price, cur, quantity, yourAmount, owner) == 0) {
				HelpSQL.InsertUserCart(Market.userName, catg, name, price, cur, quantity, yourAmount, owner);
			}
			Market.Message("Added, your amount= " + yourAmount + ", Amount= " + amount + ", Limit= " + quantity);
	  	}
	}
	
	public JPanel getPanel() {
		JPanel jp = new JPanel(null);
		if(this.items.size() == 0) {
			JLabel lb = new JLabel("No Results", SwingConstants.CENTER);
			lb.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			Font font = new Font(lb.getFont().getName(), lb.getFont().getStyle(), 16);
			lb.setFont(font);
			lb.setBounds(300, 0, 200, 100);
			jp.add(lb);
			jp.setOpaque(false);
			return jp;
		}
		if(Market.userName.equals("") != true)
			try {
				HelpSQL.setAmountList(this.items, Market.userName);	
			}
			catch(SQLException exp) {}
		jp.add(HelpSwing.getItemsPanel(this.items,"All",new JPanel()));
		jp.setOpaque(false);
		return jp;
	}
	
	
	private void check(String category) throws SQLException{
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(HelpSQL.prop.getProperty("url"),
					HelpSQL.prop.getProperty("user"), HelpSQL.prop.getProperty("pass"));
			String str = "SELECT * FROM " + category;
			stm = con.createStatement();
			rs = stm.executeQuery(str);
			int count = 0;
			while(rs.next()) {
				count = countSim(rs.getString("itemName"));
				if(count >= 1) {
					if(this.num.size() == 10)
						replace(count, rs, category);
					else {
						BigDecimal price = rs.getBigDecimal("itemPrice");
						String currency = rs.getString("itemCurrency");
						String name = rs.getString("itemName");
						int quantity = rs.getInt("itemQuantity");
						String owner = rs.getString("itemOwner"); 
						Item item = new Item(category, name, price, currency, quantity, 0, 1, quantity, owner);
						item.setButton(ButtonAdd(this.items.size()));
						this.items.add(item);
						this.num.add(count);
					}	
				}
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
	}
	
	private void replace(int n, ResultSet rs, String category) throws SQLException{
		int ind = -1;
		int smal = n;
		for(int i = 0;i < this.num.size(); i++) {
			if(this.num.get(i) < smal) {
				ind = i;
			}
		}
		if(ind != -1) {
			BigDecimal price = rs.getBigDecimal("itemPrice");
			String currency = rs.getString("itemCurrency");
			String name = rs.getString("itemName");
			int quantity = rs.getInt("itemQuantity");
			String owner = rs.getString("itemOwner");
			Item item = new Item(category, name, price, currency, quantity, 0, 1, quantity, owner);
			item.setButton(ButtonAdd(ind));
			this.items.set(ind, item);
			this.num.set(ind, n);
		}
	}
	
	private int countSim(String str) {
		ArrayList<String> newArr = createArr(str);
		int k = 0;
		for(int i = 0;i < newArr.size(); i++)
			for(int j = 0; j < this.name.size(); j++)
				if(newArr.get(i).equalsIgnoreCase(this.name.get(j)))
					k++;
		return k;
	}
	
	private ArrayList<String> createArr(String name){
		ArrayList<String> arr = new ArrayList<String>();
		int c = 0;
		boolean b = true;
		for(int i = 0;i < name.length(); i++) {
			if(i == name.length()-1) {
				arr.add(name.substring(c));
				break;
			}
			if(name.charAt(i) == ' ' || name.charAt(i) == ',' || name.charAt(i) == '.') {
				if(b == true) {
					arr.add(name.substring(c, i));
					b = false;
				}
				c = i + 1;
			}
			else
				b = true;
		}
		return arr;
	}
}
