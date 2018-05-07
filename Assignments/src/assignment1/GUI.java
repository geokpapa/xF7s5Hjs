package assignment1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.Color;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;

@SuppressWarnings("unused")
public class GUI {

	private JFrame frmAssignmentI;
	private JTextField txtUsername;
	private JPasswordField pwdPassword;
	private JTextField txtFileEQ;
	private JTextField txtFileSSH;
	private JTable tblYbus;
	private JLabel lblNumberOfBuses;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmAssignmentI.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAssignmentI = new JFrame();
		frmAssignmentI.setResizable(false);
		frmAssignmentI.setTitle("Assignment I - EH2745  CIM-XML to Bus-Branch Ybus Model  V1.0");
		frmAssignmentI.setBounds(0, -50, 654, 351);
		frmAssignmentI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.setBounds(10, 286, 628, 25);
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String eqFile = txtFileEQ.getText();
				String sshFile = txtFileSSH.getText();
				String user = txtUsername.getText();
				@SuppressWarnings("deprecation")
				String psswd = pwdPassword.getText();
				ArrayList<Ybus> ybus_list = assignment1.Assignment_I.execute(eqFile, sshFile, user, psswd); //Get ybus list.
				Integer N = assignment1.Assignment_I.getNumberOfBuses(); //Get number of buses.
				
				DefaultTableModel tableData = new DefaultTableModel();			
				String[] columnNames = {"From Bus","To Bus","R (p.u)","X (p.u)", "Gs (p.u)", "Bs (p.u)", "DevType","Device"};				
				for (int i = 0; i < columnNames.length; i++) {
					tableData.addColumn(columnNames[i]);
				}
				for (Ybus branch : ybus_list) {					
					String[] row = {branch.From, branch.To,
							branch.R.toString(), branch.X.toString(),
							branch.Gch.toString(), branch.Bch.toString(),
							branch.devType, branch.dev};
					tableData.addRow(row);
				}
				lblNumberOfBuses.setText("Number of Buses in the System: " + N.toString());
				tblYbus.setModel(tableData);
			}
		});
		frmAssignmentI.getContentPane().setLayout(null);
		frmAssignmentI.setLocationRelativeTo(null);
		frmAssignmentI.getContentPane().add(btnExecute);
		
		txtUsername = new JTextField();
		txtUsername.setText("root");
		txtUsername.setBounds(70, 11, 86, 20);
		frmAssignmentI.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		pwdPassword = new JPasswordField();
		pwdPassword.setText("xxxx");
		pwdPassword.setBounds(70, 38, 86, 20);
		frmAssignmentI.getContentPane().add(pwdPassword);
		
		txtFileEQ = new JTextField();
		txtFileEQ.setText("D:\\academia\\KTH\\EH2745\\Assignments\\xml\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml");
		txtFileEQ.setBounds(249, 11, 389, 20);
		frmAssignmentI.getContentPane().add(txtFileEQ);
		txtFileEQ.setColumns(10);
		
		txtFileSSH = new JTextField();
		txtFileSSH.setText("D:\\academia\\KTH\\EH2745\\Assignments\\xml\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml");
		txtFileSSH.setBounds(249, 38, 389, 20);
		frmAssignmentI.getContentPane().add(txtFileSSH);
		txtFileSSH.setColumns(10);
		
		JLabel lblEqFile = new JLabel("EQ File:");
		lblEqFile.setBounds(191, 16, 63, 17);
		frmAssignmentI.getContentPane().add(lblEqFile);
		
		JLabel lblSshFile = new JLabel("SSH File:");
		lblSshFile.setBounds(191, 41, 52, 14);
		frmAssignmentI.getContentPane().add(lblSshFile);
		
		JLabel lblDbUser = new JLabel("DB User:");
		lblDbUser.setBounds(10, 14, 52, 14);
		frmAssignmentI.getContentPane().add(lblDbUser);
		
		JLabel lblDbPsswd = new JLabel("DB Psswd:");
		lblDbPsswd.setBounds(10, 41, 63, 14);
		frmAssignmentI.getContentPane().add(lblDbPsswd);
		
		lblNumberOfBuses = new JLabel("Number of Buses in the System:");
		lblNumberOfBuses.setBounds(10, 71, 227, 14);
		frmAssignmentI.getContentPane().add(lblNumberOfBuses);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 96, 628, 179);
		frmAssignmentI.getContentPane().add(scrollPane);
		
		tblYbus = new JTable();
		tblYbus.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"From Bus", "To Bus", "R (p.u.)", "X (p.u)", "Gs (p.u)", "Bs (p.u)", "DevType", "Device"
			}
		));
		scrollPane.setViewportView(tblYbus);
	}
}
