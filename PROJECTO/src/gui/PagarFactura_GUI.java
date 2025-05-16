package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import LogicaNegocio.LogicaNegocio;
import domain.*;

public class PagarFactura_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textAreaResultados;

	private DefaultComboBoxModel<Integer> numSocioBoxModel = new DefaultComboBoxModel<Integer>();
	private JComboBox<Integer> comboBox = new JComboBox<Integer>();
	private LogicaNegocio logicaNegocio = new LogicaNegocio();
	private JLabel lblFactura;
	private JTextField txtFactura;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				PagarFactura_GUI frame = new PagarFactura_GUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public PagarFactura_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JButton botonConsultar = new JButton("Ver facturas");
		botonConsultar.setBounds(361, 17, 180, 30);
		contentPane.add(botonConsultar);

		textAreaResultados = new JTextArea();
		textAreaResultados.setEditable(false);
		textAreaResultados.setLineWrap(true);
		textAreaResultados.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(textAreaResultados);
		scrollPane.setBounds(20, 57, 540, 146);
		contentPane.add(scrollPane);
		
		JLabel lblNumeroSocio = new JLabel("Numero Socio");
		lblNumeroSocio.setBounds(20, 22, 120, 20);
		contentPane.add(lblNumeroSocio);
		
		lblFactura = new JLabel("Id de la factura a pagar");
		lblFactura.setBounds(20, 226, 120, 20);
		contentPane.add(lblFactura);
		lblFactura.setVisible(false);
		
		txtFactura = new JTextField();
		txtFactura.setBounds(150, 227, 150, 25);
		contentPane.add(txtFactura);
		txtFactura.setVisible(false);
		
		JButton botonPagar = new JButton("Pagar factura");
		botonPagar.setBounds(361, 226, 180, 84);
		contentPane.add(botonPagar);
		botonPagar.setVisible(false);
		
		JLabel lblMetodoPago = new JLabel("Metodo de pago");
		lblMetodoPago.setBounds(20, 280, 120, 20);
		contentPane.add(lblMetodoPago);
		lblMetodoPago.setVisible(false);
		
		String[] metodos = { "Tarjeta de crédito", "Cuenta bancaria" };
		JList<String> listMetodos = new JList<String>(metodos);
		listMetodos.setBounds(150, 280, 150, 44);
		contentPane.add(listMetodos);
		listMetodos.setVisible(false);
		
		numSocioBoxModel.removeAllElements();
		for (Integer unekoa : logicaNegocio.getNumeroSocios()) {
			numSocioBoxModel.addElement(unekoa);
		}
		comboBox.setBounds(136, 22, 150, 21);
		contentPane.add(comboBox);
		comboBox.setModel(numSocioBoxModel);
		comboBox.setSelectedIndex(0);
		
		// Acción del botón
		botonConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textAreaResultados.setText("");
				try {
					int numSocio = (Integer) comboBox.getSelectedItem();
					List<Factura> facturasNoPagadas = logicaNegocio.getFacturasNoPagadas(numSocio);
					if (facturasNoPagadas.isEmpty()) {
						JOptionPane.showMessageDialog(PagarFactura_GUI.this, "No hay facturas no pagadas.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
						return;
					} else {
						for (Factura fi : facturasNoPagadas) {
							textAreaResultados.append(fi.toString() + "\n");
						}
						
						lblFactura.setVisible(true);
						lblMetodoPago.setVisible(true);
						txtFactura.setVisible(true);
						listMetodos.setVisible(true);
						botonPagar.setVisible(true);
					
						botonPagar.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e1) {
								try {
									if (txtFactura.getText().isEmpty()) {
										JOptionPane.showMessageDialog(
						                		PagarFactura_GUI.this,
						                    "Debes rellenar todos los campos",
						                    "ERROR",
						                    JOptionPane.ERROR_MESSAGE
						                );
						                return;
									}
									textAreaResultados.setText("");
									if (txtFactura.getText().isEmpty()) {
										JOptionPane.showMessageDialog(
						                		PagarFactura_GUI.this,
						                    "Debes rellenar todos los campos",
						                    "ERROR",
						                    JOptionPane.ERROR_MESSAGE
						                );
						                return;
									}
									int codigoFactura = Integer.parseInt(txtFactura.getText());
									
									//El socio contiene esa factura?
									boolean esta = false;
									for (Factura factura : facturasNoPagadas) {
										if (factura.getCodigoFactura() == codigoFactura) {
											esta = true;
											break;
										}
									}
									if(!esta) {
										JOptionPane.showMessageDialog(
						                		PagarFactura_GUI.this,
						                    "El socio no contiene esa factura", "ERROR",
						                    JOptionPane.ERROR_MESSAGE
						                );									
										return;
									}
									
									
									if (logicaNegocio.pagarFactura((String)listMetodos.getSelectedValue(), numSocio, codigoFactura)) {
										JOptionPane.showMessageDialog(PagarFactura_GUI.this, "El pago ha ido correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
										return;
									} else {
										JOptionPane.showMessageDialog(PagarFactura_GUI.this, "Erro en el pago.", "Error", JOptionPane.ERROR_MESSAGE);
										return;
									}
								} catch (NumberFormatException ex2) {
									JOptionPane.showMessageDialog(PagarFactura_GUI.this, "El codigo de la factura debe ser un numero.", "Error", JOptionPane.ERROR_MESSAGE);
									return;
								}
							}
						});
					}
				} catch (NumberFormatException ex1) {
					JOptionPane.showMessageDialog(PagarFactura_GUI.this, "El numero del socio debe ser un numero.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
			}
		});
	}
}

