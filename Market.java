import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Market{
	
	public static JPanel pnlM = new JPanel(null);
	public static JFrame frame = new JFrame();
	public static JTextField txt = new JTextField();
	public static String[] category = {"Discs", "Toys", "Clothes", "Books", "Other"};
	public static String userName = "";
	public static String newCurrency = "USD";

	public static void TabMarket() throws Exception{ 
		String[] categ = {"All", "Discs", "Toys", "Clothes", "Books"};
		Market.txt.setBounds(280, 10, 385, 43);
		Font font = new Font(txt.getFont().getName(), txt.getFont().getStyle(), 16);
		Market.txt.setFont(font);
		JLabel home = LabelTab(0, 0, 112, 63, "PageHome");
		JLabel account = new JLabel();
		account.setBounds(780, 10, 120, 63);
		account.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				account.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			public void mouseClicked(MouseEvent e) {
				try {
					Market.txt.setText("");
					Account.IsLogin();
				}
				catch(Exception exp) {
				}
			}
			
		});
		JComboBox<String> box = new JComboBox<String>(categ);
		box.setBounds(220, 10, 60, 43);
		box.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		});
		JLabel search = new JLabel();
		search.setBounds(665, 10, 45, 43);
		search.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				search.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			public void mouseClicked(MouseEvent e) {
				try {
					if(Market.txt.getText().equals("") != true) {
						Search sc = new Search(Market.txt.getText(), box);
						setFrame(sc.getPanel());
					}
					else
						Market.Message("enter text");
				}
				catch(Exception exp) {
				}
			}
			
		});
		JLabel changeCurrency = LabelTab(150, 0, 50, 63, "PageChangeCurrency");
		Market.pnlM.add(home);
		Market.pnlM.add(box);
		Market.pnlM.add(Market.txt);
		Market.pnlM.add(search);
		Market.pnlM.add(account);
		Market.pnlM.add(changeCurrency);
		Market.pnlM.add(new HelpSwing().account());
		Market.pnlM.setLayout(null);
		Market.pnlM.setBounds(0, 0, 1000, 100);
		Market.pnlM.setOpaque(false);
		setFrame(HelpSwing.PageHome());
		Market.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Market.frame.getContentPane().setBackground(Color.WHITE);
		Market.frame.setLayout(null);
		Market.frame.setVisible(true);
	}
	
	private static JPanel PageChangeCurrency() {
		JPanel pnl = new JPanel(null);
		pnl.setOpaque(false);
		String[] curren = {"USD","EUR","ILS","GPB","CAD"};
		JComboBox<String> box = new JComboBox<String>(curren);
		box.setBounds(400, 100, 100, 80);
		box.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		});
		JButton btn = new JButton("Change Currency");
		btn.setBounds(550, 100, 150, 80);
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Market.newCurrency = box.getSelectedItem().toString();
				Message("Changed Currency");
			}
			
		});
		pnl.add(box);
		pnl.add(btn);
		pnl.setBounds(0, 0, 1000, 400);
		return pnl;
	}
	
	public static void setFrame(JPanel pnl) {
		JPanel pnl2 = new JPanel(null);
		pnl2.add(Market.pnlM);
		pnl.setBounds(0,100,1000,300);
		pnl.setLayout(null);
		pnl.setOpaque(false);
		pnl2.add(pnl);
		pnl2.setBounds(0, 0, 1000, 400);
		Market.frame.setBounds(0,0,1015,400);
		Market.frame.setContentPane(pnl2);
		Market.frame.getContentPane().setBackground(Color.WHITE);
		Market.frame.revalidate();
		Market.frame.repaint();
	}
	
	public static JLabel LabelTab(int x, int y, int w, int h, String str) {
		JLabel lb = new JLabel();
		lb.setBounds(x, y, w, h);
		lb.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				lb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			public void mouseClicked(MouseEvent e) {
				Market.txt.setText("");
				try {
					if(str.equals("PageHome"))
						setFrame(HelpSwing.PageHome());
					if(str.equals("TabCreateAccount"))
						setFrame(Account.TabCreateAccount());
					if(str.equals("TabCart"))
						setFrame(Account.TabCart());
					if(str.equals("TabAddItem"))
						setFrame(Account.TabAddItem());
					if(str.equals("TabYourItems"))
						setFrame(Account.TabYourItems());
					if(str.equals("PageChangeCurrency"))
						setFrame(PageChangeCurrency());
				}
				catch(SQLException exp) {}
			}
			
		});
		return lb;
	}
	
	public static void Message(String str) {
		JDialog jd = new JDialog();
		JLabel jb = new JLabel(str);
		jd.add(jb);
		jd.setModal(false);
		jd.setSize(jd.getPreferredSize().width+20, 100);
		jd.setVisible(true);
		jd.setLocationRelativeTo(frame);
		Timer time = new Timer(3000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jd.setVisible(false);
				jd.dispose();
			}
		});
		time.start();
	}
	
}
