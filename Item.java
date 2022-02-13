import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class Item {
	
	private String name;
	private String category;
	private BigDecimal price;
	private BigDecimal newPrice;
	private String currency;
	private int amount;
	private int quantity;
	private String owner;
	private JButton btn;
	private int start;
	private int end;
	private JSpinner spin;
	
	public Item(String category, String name, BigDecimal price, String currency, int quantity, int amount, int start, int end, String owner) {
		this.category = category;
		this.name = name;
		this.price = price;
		this.currency = currency;
		this.quantity = quantity;
		this.amount = amount;
		this.owner = owner;
		this.start = start;
		this.end = end;
		int amoun = this.amount;
		if(this.end==10001) {
			this.end--;
			amoun = 1;
		}
		else if(this.amount == 0)
			amoun = 1; 
		this.newPrice = HelpSwing.values(price,this.currency,Market.newCurrency).multiply(new BigDecimal(amoun));
		System.out.println(this.amount + " " + this.quantity);
	}
	
	
	public void setButton(JButton btn) {
		this.btn = btn;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public BigDecimal getPrice() {
		return this.price;
	}
	
	public BigDecimal getNewPrice() {
		return this.newPrice;
	}
	
	public String getCurrency() {
		return this.currency;
	}
	
	public int getYourAmount() {
		return this.amount;
	}
	
	public int getSelectedAmount() {
		return (int) this.spin.getValue();
	}
	
	public void setYouAmount(int amount) {
		this.amount = amount;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
	
	public String getOwner() {
		return this.owner;
	}
	
	public JPanel getPanel() {
		return createLabel();
	}
	
	private JPanel createLabel() {
		JLabel jb1 = createLbl(152,16,this.name);
		JLabel jb2 = new JLabel(Market.newCurrency);
		JLabel jb3 = new JLabel(""+this.newPrice);
		JLabel jb4 = new JLabel("Owner: ");
		JLabel jb5 = new JLabel(this.owner);
		JLabel jb6 = new JLabel(this.category);
		jb2.setVerticalAlignment(JLabel.TOP);
		jb2.setLocation(0, jb1.getHeight());
		jb2.setSize(30,jb2.getPreferredSize().height);
		jb3.setVerticalAlignment(JLabel.TOP);
		jb3.setLocation(jb2.getWidth(), jb1.getHeight());
		jb3.setSize(152-jb2.getWidth(),jb2.getPreferredSize().height);
		jb4.setVerticalAlignment(JLabel.TOP);
		jb4.setLocation(jb1.getX(), jb2.getY()+jb2.getHeight());
		jb4.setSize(jb4.getPreferredSize().width, jb4.getPreferredSize().height);
		jb5.setVerticalAlignment(JLabel.TOP);
		jb5.setLocation(jb4.getWidth()+jb4.getX(), jb2.getY()+jb2.getHeight());
		jb5.setSize(152-jb4.getWidth(), jb4.getHeight());
		jb6.setVerticalAlignment(JLabel.TOP);
		jb6.setLocation(jb1.getX(), jb4.getHeight()+jb4.getY());
		jb6.setSize(152,jb6.getPreferredSize().height);
		String[] arr = {"1","2"};
		JComboBox<String> box = new JComboBox<String>(arr);
		box.setBounds(jb1.getWidth()/2,jb1.getHeight(), jb1.getWidth()/2, jb2.getHeight());
		int start = this.start;
		if(start == 0)
			start = this.amount;
		SpinnerModel sm = new SpinnerNumberModel(start,this.start,this.end,1);
		this.spin = new JSpinner(sm);
		this.spin.setEditor(new JSpinner.DefaultEditor(this.spin));
		this.spin.setBounds(jb1.getWidth()-60, jb1.getHeight()+10, 60, 15);
		this.btn.setBounds(jb1.getX(),jb6.getY()+jb6.getHeight(),152,30);
		JLabel jb = new JLabel("Max: " + this.quantity);
		jb.setBounds(this.spin.getX()-10,this.spin.getY()+this.spin.getHeight(),100,15);
		JPanel jp = new JPanel(null);
		jp.add(jb1);
		jp.add(jb2);
		jp.add(jb3);
		jp.add(jb4);
		jp.add(jb5);
		jp.add(jb6);
		jp.add(this.spin);
		jp.add(jb);
		jp.add(this.btn);
		jp.setBounds(0, 0, jb1.getWidth(), btn.getY()+btn.getHeight());
		jp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jp.setOpaque(false);
		return jp;
	}
	
	private JLabel createLbl(int w, int f, String str) {
		JLabel lb = new JLabel("s");
		FontMetrics fm = lb.getFontMetrics(lb.getFont());;
		int c = 0;
		int sum = 0;
		String newStr = "";
		int h = 1;
		boolean b = true;
		for(int i = 0;i < str.length();i++) {
			if(i == str.length()-1) {
				if(sum + fm.stringWidth(str.substring(c)) > w)
					h++;
				break;
			}
			if(str.charAt(i) == ' ') {
				if(b == false) {
					if(sum + fm.stringWidth(str.substring(c, i)) > w) {
						newStr = str.substring(c, i) + " ";
						sum = fm.stringWidth(newStr);
						h++;
					}
					else {
						newStr = newStr + str.substring(c, i) + " ";
						sum = sum + fm.stringWidth(newStr);
					}
					b = true;
				}
				c = i+1;
			}
			else
				b = false;
		}
		StringBuilder sb1 = new StringBuilder(str.length());
		sb1.append("<html>" + str + "</html>");
		lb.setText(sb1.toString());
		lb.setVisible(true);
		lb.setVerticalAlignment(JLabel.TOP);
		lb.setLocation(0,0);
		lb.setSize(new Dimension(w, lb.getPreferredSize().height*h));
		return lb;
	}
	
}
