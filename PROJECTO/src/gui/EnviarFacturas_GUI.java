package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import LogicaNegocio.LogicaNegocio;


public class EnviarFacturas_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Simulación de la lógica (¡Sustituye esto por tu clase real!)
	private LogicaNegocio logicaNegocio = new LogicaNegocio();

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				EnviarFacturas_GUI frame = new EnviarFacturas_GUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public EnviarFacturas_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JButton btnEnviar = new JButton("Enviar Facturas");
		btnEnviar.setBounds(189, 140, 180, 82);
		contentPane.add(btnEnviar);

		// Acción del botón
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (logicaNegocio.enviarFacturas()) {
					JOptionPane.showMessageDialog(EnviarFacturas_GUI.this, "Facturas enviadas por correo correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
					return;
				} else {
					JOptionPane.showMessageDialog(EnviarFacturas_GUI.this, "Hoy no es lunes, vuelve a intertarlo cuando sea.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				} 
			}
		});
	}
}
