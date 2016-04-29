package userInterface;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JTextField;

import model.Document;
import model.Supplier;
import run.Runner;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import java.awt.Color;

public class SimpleForm {

	Runner runner = null;
	private JFrame frame;
	private JTextField txtSupplierPath;
	private JTextField txtInvoicePath;
	private JTextField txtInvoiceFileName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimpleForm window = new SimpleForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SimpleForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(
				new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("450px:grow"),},
			new RowSpec[] {
				RowSpec.decode("16px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		JLabel lblHeader = new JLabel("Xtracta Interview Test");
		lblHeader.setForeground(Color.RED);
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblHeader, "1, 1, fill, top");

		JLabel lblEnterSupplierFilefolder = new JLabel(
				"Enter Supplier File/folder location:");
		frame.getContentPane().add(lblEnterSupplierFilefolder, "1, 3");

		txtSupplierPath = new JTextField();
		txtSupplierPath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser filechooser = new JFileChooser();
				filechooser
						.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				filechooser.showOpenDialog(null);

				txtSupplierPath.setText(filechooser.getSelectedFile()
						.getAbsolutePath());
			}
		});
		frame.getContentPane().add(txtSupplierPath, "1, 5");
		txtSupplierPath.setColumns(10);

		JLabel lblEnterInvoiceFilefolder = new JLabel(
				"Enter Invoice File/Folder location:");
		frame.getContentPane().add(lblEnterInvoiceFilefolder, "1, 7");

		txtInvoicePath = new JTextField();
		txtInvoicePath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				JFileChooser filechooser = new JFileChooser();
				filechooser
						.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				filechooser.showOpenDialog(null);

				txtInvoicePath.setText(filechooser.getSelectedFile()
						.getAbsolutePath());

			}
		});
		frame.getContentPane().add(txtInvoicePath, "1, 9, fill, top");
		txtInvoicePath.setColumns(10);

		JButton btnLoadData = new JButton("Load Data Button");
		btnLoadData.setBackground(Color.WHITE);
		btnLoadData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runner = new Runner();
				runner.buildNecessaryIndices(txtInvoicePath.getText(),
						txtSupplierPath.getText());
				JOptionPane.showMessageDialog(null, "Data Loaded to index");
				btnLoadData.setEnabled(false);

			}
		});
		frame.getContentPane().add(btnLoadData, "1, 11");

		JLabel lblEnterInvoiceFile = new JLabel(
				"Enter invoice file name to find its supplier");
		frame.getContentPane().add(lblEnterInvoiceFile, "1, 13");

		txtInvoiceFileName = new JTextField();
		frame.getContentPane().add(txtInvoiceFileName, "1, 15, fill, default");
		txtInvoiceFileName.setColumns(10);

		JButton btnFindSupplier = new JButton("Find Supplier");
		btnFindSupplier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String invoiceFileName = txtInvoiceFileName.getText();
				if (runner == null) {
					JOptionPane.showMessageDialog(null,
							"Load Data First Please");
					return;
				}
				Document doc = runner.invoiceDocumentMap.get(invoiceFileName
						.toLowerCase());
				if (doc == null) {
					JOptionPane.showMessageDialog(null,
							"File not found in Index");
					return;
				}

				Supplier supplier = runner.findSupplier(doc);
				if (supplier == null) {
					JOptionPane.showMessageDialog(null,
							"Unable to find supplier Information");
					return;
				}

				JOptionPane.showMessageDialog(null, "The Supplier Name is : "
						+ supplier.getSupplierName()
						+ "\n The Supplier Id is : " + supplier.getSupplierId());

			}
		});
		frame.getContentPane().add(btnFindSupplier, "1, 17");
		
		JButton btnCloseApplication = new JButton("Close Application");
		btnCloseApplication.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		frame.getContentPane().add(btnCloseApplication, "1, 19");
	}

}
