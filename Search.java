import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

public class Search {
	private ArrayList<JPanel> items;
	private ArrayList<Integer> num;
	private ArrayList<String> name;
	private String user;
	private String newCur;
	private HelpSQL help = new HelpSQL();
	private HelpSwing helpSwing = new HelpSwing();
	private Amazon amazon = new Amazon();
	
	public Search(String str, String newCurr, JComboBox<String> box, String user){
		try {
			this.items = new ArrayList<JPanel>();
			this.num = new ArrayList<Integer>();
			this.name = createArr(str);
			this.user = user;
			this.newCur = newCurr;
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
		catch(Exception exp) {
			
		}
	}
	
	private JButton createButton(String user, String catg, String name, BigDecimal price, String cur, String owner, int quan, int ind) {
		JButton btn = new JButton("Add");
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(user.equals("") != true) {
					addToCart(ind);
				}
				else
					Search.this.amazon.Message("Login to add");
			}
			
		});
		return btn;
	}
	
	private void addToCart(int ind) {
	    String catg = ((JLabel)(this.items.get(ind)).getComponent(5)).getName();
	    String name = ((JLabel)(this.items.get(ind)).getComponent(0)).getName();
	    BigDecimal price = new BigDecimal(((JLabel)(this.items.get(ind)).getComponent(2)).getName());
	    String cur = ((JLabel)(this.items.get(ind)).getComponent(1)).getName();
	    String owner = ((JLabel)(this.items.get(ind)).getComponent(4)).getName();
	    JSpinner spin =((JSpinner)(this.items.get(ind)).getComponent(6));
	    JTextField txt = (JTextField)(((JSpinner.DefaultEditor) spin.getEditor()).getTextField());
	  	int quantity = Integer.parseInt(spin.getName());
	  	int amount = Integer.parseInt(txt.getText());
	  	int yourAmount = this.help.getAmount(this.user, catg, name, price, cur, owner);
	  	if(amount + yourAmount > quantity) {
	  		this.amazon.Message("You selcted More Than Allowed, your amount= " + yourAmount + ", Limit= " + quantity);
	  		return;
	  	}
	  	yourAmount = yourAmount + amount;
		if(this.help.UpdateUserCart(this.user, catg, name, price, cur, quantity, yourAmount, owner) == 0) {
			this.help.InsertUserCart(this.user, catg, name, price, cur, quantity, yourAmount, owner);
		}
		this.amazon.Message("Added, your amount= " + yourAmount + ", Limit= " + quantity);
	}
	
	public JPanel getPanel() {
		JPanel jp = new JPanel(null);
		JPanel jp1;
		int x = 0, y = 0;
		for(int i = 0;i < this.items.size(); i++) { 
			jp1 = this.items.get(i);
			jp1.setLocation(x, y);
			System.out.println("fire");
			jp.add(jp1);
			if(x + 154 > 1000) {
				x = 0;
				y = y + 200;
			}
			else
				x = x + 154;
		}
		jp.setBounds(0, 100, 1000, 320);
		jp.setOpaque(false);
		return jp;
	}
	
	
	private void check(String catg) throws Exception{
		Connection con = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/Amazon","root","Itay1015");
		String s = "SELECT * FROM " + catg;
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery(s);
		int count = 0;
		while(rs.next()) {
			count = countSim(rs.getString("itemName"));
			if(count >= 1) {
				if(this.num.size() == 10)
					replace(count, rs, catg);
				else {
					BigDecimal bd = rs.getBigDecimal("itemPrice");
					String cur = rs.getString("itemCurrency");
					String name = rs.getString("itemName");
					int quan = rs.getInt("itemQuantity");
					String owner = rs.getString("itemOwner");
					this.items.add(this.helpSwing.createLabel(catg, name, bd, cur, quan, 1, owner, createButton(this.user, catg, name, bd, cur, owner, quan, this.items.size()), this.newCur, 1));
					this.num.add(count);
				}	
			}
		}
		rs.close();
		stm.close();
		con.close();
	}
	
	private void replace(int n, ResultSet rs, String catg) throws Exception{
		int ind = -1;
		int smal = n;
		for(int i = 0;i < this.num.size(); i++) {
			if(this.num.get(i) < smal) {
				ind = i;
			}
		}
		if(ind != -1) {
			BigDecimal bd = rs.getBigDecimal("itemPrice");
			String cur = rs.getString("itemCurrency");
			String name = rs.getString("itemName");
			int quan = rs.getInt("itemQuantity");
			String owner = rs.getString("itemOwner");
			this.items.set(ind, this.helpSwing.createLabel(catg, name, bd, cur, quan, 1, owner, createButton(this.user, catg, name, bd, cur, owner, quan, ind), this.newCur, 1));
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
