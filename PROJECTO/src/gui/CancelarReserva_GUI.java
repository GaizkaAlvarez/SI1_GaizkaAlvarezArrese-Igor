package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import LogicaNegocio.LogicaNegocio;

public class CancelarReserva_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultComboBoxModel<Integer> numSocioBoxModel = new DefaultComboBoxModel<Integer>();
	private JComboBox<Integer> comboBox = new JComboBox<Integer>();
	private LogicaNegocio logicaNegocio = new LogicaNegocio();
	private JTextField textField;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				CancelarReserva_GUI frame = new CancelarReserva_GUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public CancelarReserva_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lblNumeroSocio = new JLabel("Numero Socio:");
		lblNumeroSocio.setBounds(20, 20, 120, 20);
		contentPane.add(lblNumeroSocio);

		JLabel lblIdReserva = new JLabel("Id Reserva:");
		lblIdReserva.setBounds(20, 60, 120, 20);
		contentPane.add(lblIdReserva);

		JButton botonCancelar = new JButton("Cancelar reserva");
		botonCancelar.setBounds(321, 20, 180, 63);
		contentPane.add(botonCancelar);
		
		numSocioBoxModel.removeAllElements();
		for (Integer unekoa : logicaNegocio.getNumeroSocios()) {
			numSocioBoxModel.addElement(unekoa);
		}
		comboBox.setBounds(150, 20, 150, 20);
		contentPane.add(comboBox);
		comboBox.setModel(numSocioBoxModel);
		comboBox.setSelectedIndex(0);
		
		textField = new JTextField();
		textField.setBounds(150, 61, 150, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		// Acción del botón
		botonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (textField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(CancelarReserva_GUI.this, "Rellene todos los campos", "ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					int numSocio = (Integer) comboBox.getSelectedItem();
					int idReserva = (Integer) Integer.parseInt(textField.getText());
					
					//El socio tiene esa reserva?
					if (!logicaNegocio.tieneReservaSocio(numSocio, idReserva)) {
						JOptionPane.showMessageDialog(
								CancelarReserva_GUI.this, 
								"El socio no tiene esa reserva", "Error", 
								JOptionPane.ERROR_MESSAGE
						);
						return;
					}	
					
					logicaNegocio.cancelarReserva(idReserva, numSocio);
					
					JOptionPane.showMessageDialog(CancelarReserva_GUI.this, "La reserva se ha cancelado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
					return;
				} catch (NumberFormatException ex1) {
					JOptionPane.showMessageDialog(CancelarReserva_GUI.this, "El numero de socio y el id de la reserva deben ser numeros", "ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
	}
}
