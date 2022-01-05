import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Amazon{
	
	public static JPanel pnlM = new JPanel(null);
	public static JFrame frame = new JFrame();
	public static JTextField txt = new JTextField();

	public void TabAmazon() throws Exception{ 
		String[] categ = {"All", "Discs", "Toys", "Clothes", "Books"};
		txt.setBounds(280, 10, 385, 43);
		HelpSwing helpSwing = new HelpSwing();
		Account ac = new Account("","USD");
		Font font = new Font(txt.getFont().getName(), txt.getFont().getStyle(), 16);
		txt.setFont(font);
		JLabel home = lbl(0, 0, 112, 63, helpSwing.pageHome());
		JLabel account = new JLabel();
		account.setBounds(780, 10, 120, 63);
		account.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				account.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			public void mouseClicked(MouseEvent e) {
				try {
					txt.setText("");
					ac.IsLogin();
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
					if(txt.getText().equals("") != true) {
						Search sc = new Search(txt.getText(), ac.getCurrency(), box, ac.getUserName());
						setFrame(sc.getPanel());
					}
					else
						System.out.println("enter text");
				}
				catch(Exception exp) {
				}
			}
			
		});
		JLabel changeCurrency = lbl(150, 0, 50, 63, PageChangeCurrency(ac));
		pnlM.add(home);
		pnlM.add(box);
		pnlM.add(txt);
		pnlM.add(search);
		pnlM.add(account);
		pnlM.add(changeCurrency);
		pnlM.add(helpSwing.account());
		pnlM.setLayout(null);
		pnlM.setBounds(0, 0, 1000, 100);
		pnlM.setOpaque(false);
		setFrame(helpSwing.pageHome());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, 1000, 400);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setLayout(null);
		frame.setVisible(true);
	}
	
	/*public void setFrame(JFrame frame) {
		this.frame = frame;
	}*/
	
	public JPanel PageChangeCurrency(Account ac) {
		JPanel pnl = new JPanel(null);
		pnl.setOpaque(false);
		String[] curren = {"USD","EUR","ILS","GPB","CAD"};
		JComboBox<String> box = new JComboBox<String>(curren);
		box.setBounds(400, 150, 100, 80);
		box.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		});
		JButton btn = new JButton("Change Currency");
		btn.setBounds(550, 150, 100, 80);
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ac.setCurrency(box.getSelectedItem().toString());
				Message("Changed Currency");
			}
			
		});
		pnl.add(box);
		pnl.add(btn);
		pnl.setBounds(0, 100, 1000, 300);
		return pnl;
	}
	
	public void setFrame(JPanel pnl) {
		JPanel pnl2 = new JPanel();
		pnl2.add(pnlM);
		pnl2.add(pnl);
		pnl2.setLayout(null);
		pnl2.setBounds(0, 0, 1000, 400);
		frame.setContentPane(pnl2);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.revalidate();
		frame.repaint();
	}
	
	public JLabel lbl(int x, int y, int w, int h, JPanel jp) {
		JLabel lb = new JLabel();
		lb.setBounds(x, y, w, h);
		lb.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				lb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			public void mouseClicked(MouseEvent e) {
				txt.setText("");
				setFrame(jp);
			}
			
		});
		return lb;
	}
	
	public void Message(String str) {
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
