package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import LogicaNegocio.LogicaNegocio;
import domain.*;

public class ConsultarFacturas_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textAreaResultados;
	private DefaultComboBoxModel<Integer> numSocioBoxModel = new DefaultComboBoxModel<Integer>();
	private JComboBox<Integer> comboBox = new JComboBox<Integer>();

	// Simulación de la lógica (¡Sustituye esto por tu clase real!)
	private LogicaNegocio logicaNegocio = new LogicaNegocio();

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				ConsultarFacturas_GUI frame = new ConsultarFacturas_GUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public ConsultarFacturas_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lblNumeroSocio = new JLabel("Numero socio:");
		lblNumeroSocio.setBounds(20, 20, 120, 20);
		contentPane.add(lblNumeroSocio);

		JButton botonConsultar = new JButton("Consultar Facturas");
		botonConsultar.setBounds(380, 17, 180, 30);
		contentPane.add(botonConsultar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 68, 540, 285);
		contentPane.add(scrollPane);
		
				textAreaResultados = new JTextArea();
				scrollPane.setViewportView(textAreaResultados);
				textAreaResultados.setEditable(false);
				textAreaResultados.setLineWrap(true);
				textAreaResultados.setWrapStyleWord(true);
		
		numSocioBoxModel.removeAllElements();
		for (Integer unekoa : logicaNegocio.getNumeroSocios()) {
			numSocioBoxModel.addElement(unekoa);
		}
		comboBox.setBounds(147, 20, 150, 21);
		contentPane.add(comboBox);
		comboBox.setModel(numSocioBoxModel);
		comboBox.setSelectedIndex(0);

		// Acción del botón
		botonConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					textAreaResultados.setText("");
					int numSocio = (Integer) comboBox.getSelectedItem();
					List<Factura> listaFacturas = logicaNegocio.getFacturasNoPagadas(numSocio);
					if (listaFacturas.isEmpty()) {
						JOptionPane.showMessageDialog(ConsultarFacturas_GUI.this, 
								"No hay facturas pendientes de pago.", "Mensaje", 
								JOptionPane.INFORMATION_MESSAGE);
						return;
					} else {
						for (Factura factura : listaFacturas) {
							textAreaResultados.append(factura.toString() + "\n");
						}
					}
				} catch (NumberFormatException ex1) {
					JOptionPane.showMessageDialog(ConsultarFacturas_GUI.this, "La intensidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
	}
}