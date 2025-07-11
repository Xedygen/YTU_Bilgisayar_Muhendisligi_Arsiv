package yazilim;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import yazilim.classes.Dealer;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class DealerLoginPage {
	private JFrame frame;
	private JTextField emailField;
	private JTextField passwordField;
	private static Connection conn;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/YazilimMuhProje", "postgres", "12345");
					DealerLoginPage window = new DealerLoginPage(conn);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public DealerLoginPage() throws SQLException {
		conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/YazilimMuhProje", "postgres", "12345");
		initialize();
	}

	/**
	 * Create the application.
	 */
	public DealerLoginPage(Connection parent_conn) {
		conn = parent_conn;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Bayi Giriş Sayfası");
		frame.setBounds(100, 100, 373, 293);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		emailField = new JTextField();
		emailField.setBounds(20, 60, 150, 25);
		frame.getContentPane().add(emailField);
		emailField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		passwordField.setBounds(20, 120, 150, 25);
		frame.getContentPane().add(passwordField);

		JLabel emailLabel = new JLabel("E-Posta:");
		emailLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		emailLabel.setBounds(20, 40, 80, 15);
		frame.getContentPane().add(emailLabel);

		JLabel passwordLabel = new JLabel("Şifre:");
		passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		passwordLabel.setBounds(20, 100, 100, 15);
		frame.getContentPane().add(passwordLabel);

		JButton btnNewButton = new JButton("Giriş Yap");
		btnNewButton.setFocusable(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = emailField.getText();
				String password = passwordField.getText();
				String query = "SELECT login_dealer(?,?);";
				try {
					PreparedStatement statement = conn.prepareStatement(query);
					statement.setString(1, email);
					statement.setString(2, password);

					ResultSet r = statement.executeQuery();
					r.next();
					if (r.getBoolean(1)) {
						query = "SELECT id FROM dealer WHERE email = ?;";
						PreparedStatement p = conn.prepareStatement(query);
						p.clearParameters();
						p.setString(1, emailField.getText());
						r = p.executeQuery();
						r.next();
						
						Dealer dealer = new Dealer(r.getInt(1), emailField.getText());
						
						DealerMainPage dealer_main_page = new DealerMainPage(dealer, conn);
						dealer_main_page.showFrame();
						frame.setVisible(false);
					} else {
						lblNewLabel.setVisible(true);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton.setBounds(20, 165, 130, 30);
		frame.getContentPane().add(btnNewButton);

		JButton returnButton = new JButton("Geri Dön");
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartPage startPage = new StartPage(conn);
				startPage.showFrame();
				frame.setVisible(false);
			}
		});
		returnButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		returnButton.setBounds(175, 165, 100, 30);
		frame.getContentPane().add(returnButton);
		returnButton.setFocusable(false);

		lblNewLabel = new JLabel("Hatalı e-posta veya şifre. Lütfen tekrar deneyiniz.");
		lblNewLabel.setVisible(false);
		lblNewLabel.setForeground(new Color(255, 0, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(20, 199, 325, 44);
		frame.getContentPane().add(lblNewLabel);
	}

	public void showFrame() {
		frame.setVisible(true);
	}
}