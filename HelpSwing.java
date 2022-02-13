import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class HelpSwing {
	
	public static JPanel PageHome(){
		JPanel pnl = new JPanel(null);
		pnl.setOpaque(false);
		pnl.setBounds(0, 100, 1000, 300);
		return pnl;
	}
	
	public JLabel account() throws Exception{
		ImageIcon background = new ImageIcon(this.getClass().getResource("Tab.png"));
	    JLabel jb = new JLabel();
	    jb.setBounds(0, 0, 999, 63);
	    jb.setIcon(background);
	    return jb;
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
	
	public static JPanel getItemsPanel(ArrayList<Item> items, String st, JPanel jp2) {
		JPanel jp = new JPanel(null);
		int x = 0,y = 0;
		for(int i = 0;i < items.size(); i++) { 
			JPanel jp1 = items.get(i).getPanel();
			jp1.setLocation(x, y);
			jp.add(jp1);
			if(x + (154+154) > 1000) {
				x = 0;
				y = y + 100;
			}
			else
				x = x + 154;
		}
		String[] str = {"Relevance","Price: Low to High", "Price: High to Low", "Quantity: Low to High", "Quantity: High to Low"};
		JComboBox<String> box = new JComboBox<String>(str);
		box.setSelectedItem(st);
		box.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {
		        if(e.getStateChange()== ItemEvent.SELECTED) {
			    	if(e.getItem().toString().equals("Relevance")) {
				    	Market.setFrame(getItemsPanel(items, box.getSelectedItem().toString(),jp2));
			    	}
			    	else
			    		Market.setFrame(getItemsPanel(HelpAction.sortBy(e.getItem().toString(), items), box.getSelectedItem().toString(), jp2));
		        }
		    }
		});
		box.setBounds(750, 200, 250, 50);
		if(x != 0 || y != 0) {
			System.out.println("gone");
			jp.add(box);
		}
		if(jp2.getComponentCount() != 0) {
			System.out.println("take");
			jp.add(jp2);
		}
		jp.setBounds(0, 0, 1000, 300);
		jp.setOpaque(false);
		return jp;
	}
	
	public static JTextField TextFieldInt() {
		JTextField txt = new JTextField();
		((AbstractDocument) txt.getDocument()).setDocumentFilter(new DocumentFilter() {
			
		    @Override
		    public void replace(DocumentFilter.FilterBypass fb, int offset,
		                        int length, String text, AttributeSet attrs)
		            throws BadLocationException {
		    	String str = text;
		    	String newStr ="";
		    	for(int i = 0;i < str.length(); i++)
		    		if(isNum(str.charAt(i)+"")) {
		    			newStr = newStr + str.charAt(i);
		    			if(Integer.parseInt(txt.getText()+newStr) >= 10000)
		    		    	break;
		    		}
    			if(newStr.equals("") != true && Integer.parseInt(txt.getText()+newStr) <= 10000)
    				fb.replace(offset, length, newStr, attrs);
		    }
        });
		return txt;
	}
	
	public static JTextField TextFieldDecimal() {
		JTextField txt = new JTextField();
		((AbstractDocument) txt.getDocument()).setDocumentFilter(new DocumentFilter() {
			
		    @Override
		    public void replace(DocumentFilter.FilterBypass fb, int offset,
		                        int length, String text, AttributeSet attrs)
		            throws BadLocationException {
		    	String str = text;
		    	String newStr ="";
		    	boolean b = isDot(txt.getText());
		    	for(int i = 0;i < str.length(); i++)
		    		if(isNum(str.charAt(i)+"") || (str.charAt(i) == '.' && b == false)) {
		    			b = true;
		    			newStr = newStr + str.charAt(i);
		    			if(txt.getText().length()+newStr.length() >= 9)
		    		    	break;
		    		}
    			if(txt.getText().length()+newStr.length() <= 9)
    				fb.replace(offset, length, newStr, attrs);
		    }
		    
		    public boolean isDot(String str) {
		    	for(int i = 0;i < str.length(); i++)
		    		if(str.charAt(i) == '.')
		    			return true;
		    	return false;
		    }
        });
		return txt;
	}
	
	private static boolean isNum(String str) {
		try {
			Integer.parseInt(str);
			return true;
		}
		catch(Exception exp) {
			return false;
		}
	}
	
	public static BigDecimal values(BigDecimal amount, String og, String change) {
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
