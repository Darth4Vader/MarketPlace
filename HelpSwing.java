import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class HelpSwing {
	
	public JPanel pageHome() throws Exception{
		JPanel pnl = new JPanel(null);
		pnl.setOpaque(false);
		pnl.setBounds(0, 100, 1000, 300);
		return pnl;
	}
	
	public JLabel account() throws Exception{
		String path = "C:\\Users\\Lenovo\\Downloads\\amazons.png";
		ImageIcon background = new ImageIcon(new ImageIcon(path).getImage());
	    JLabel jb = new JLabel();
	    jb.setBounds(0, 0, 999, 63);
	    jb.setIcon(background);
	    return jb;
	}
	
	public JPanel createLabel(String catg, String name, BigDecimal price, String cur, int quan, int amount, String owner, JButton btn, String newCur, int start) {
		JLabel jb1 = createLbl(152,16,name);
		jb1.setName(name);
		JLabel jb2 = new JLabel(newCur);
		jb2.setName(cur);
		JLabel jb3 = new JLabel(""+values(price,cur,newCur).multiply(new BigDecimal(amount)));
		jb3.setName(""+price);
		JLabel jb4 = new JLabel("Owner: ");
		JLabel jb5 = new JLabel(owner);
		jb5.setName(owner);
		JLabel jb6 = new JLabel(catg);
		jb6.setName(catg);
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
		SpinnerModel sm = new SpinnerNumberModel(amount,start,quan,1);
		JSpinner spin = new JSpinner(sm);
		spin.setName(""+quan);
		spin.setEditor(new JSpinner.DefaultEditor(spin));
		spin.setBounds(jb1.getWidth()-50, jb1.getHeight()+10, 50, 15);
		((JSpinner.DefaultEditor) spin.getEditor()).getTextField().setText(""+amount);
		((JSpinner.DefaultEditor) spin.getEditor()).getTextField().setName(""+amount);
		btn.setBounds(jb1.getX(),jb6.getY()+jb6.getHeight(),152,30);
		JPanel jp = new JPanel(null);
		jp.add(jb1);
		jp.add(jb2);
		jp.add(jb3);
		jp.add(jb4);
		jp.add(jb5);
		jp.add(jb6);
		jp.add(spin);
		jp.add(btn);
		jp.setBounds(0, 0, jb1.getWidth(), btn.getY()+btn.getHeight());
		jp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jp.setOpaque(false);
		return jp;
	}
	
	public JLabel createLbl(int w, int f, String str) {
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
	
	public BigDecimal values(BigDecimal amount, String og, String change) {
		BigDecimal to = new BigDecimal(1);
		if(og.equals("USD")) {
			if(change.equals("EUR"))
				to = new BigDecimal(0.88);
			else if(change.equals("GPB"))
				to = new BigDecimal(0.74);
			else if(change.equals("ILS"))
				to = new BigDecimal(3.10);
			else if(change.equals("CAD"))
				to = new BigDecimal(1.26);
		}
		else if(og.equals("EUR")) {
				if(change.equals("USD"))
					to = new BigDecimal(1.13);
				else if(change.equals("GPB"))
					to = new BigDecimal(0.84);
				else if(change.equals("ILS"))
					to = new BigDecimal(3.53);
				else if(change.equals("CAD"))
					to = new BigDecimal(1.43);
			}
		else if(og.equals("GPB")) {
				if(change.equals("USD"))
					to = new BigDecimal(1.35);
				else if(change.equals("EUR"))
					to = new BigDecimal(1.18);
				else if(change.equals("ILS"))
					to = new BigDecimal(4.20);
				else if(change.equals("CAD"))
					to = new BigDecimal(1.70);
			}
		else if(og.equals("ILS")) {
				if(change.equals("USD"))
					to = new BigDecimal(0.32);
				else if(change.equals("EUR"))
					to = new BigDecimal(0.28);
				else if(change.equals("GPB"))
					to = new BigDecimal(0.23);
				else if(change.equals("CAD"))
					to = new BigDecimal(0.40);
			}
		else if(og.equals("CAD")) {
				if(change.equals("USD"))
					to = new BigDecimal(0.79);
				else if(change.equals("EUR"))
					to = new BigDecimal(0.69);
				else if(change.equals("GPB"))
					to = new BigDecimal(0.58);
				else if(change.equals("ILS"))
					to = new BigDecimal(2.45);
			}
		return amount.multiply(to).setScale(2, RoundingMode.HALF_UP);
	}
}
