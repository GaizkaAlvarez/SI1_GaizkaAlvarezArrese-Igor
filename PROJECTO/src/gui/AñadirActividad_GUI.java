package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


import LogicaNegocio.*;

public class AñadirActividad_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNombreActividad;
	private JTextField txtGradoExigencia;

	private LogicaNegocio logicaNegocio = new LogicaNegocio();
	private JTextField txtPrecio;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				AñadirActividad_GUI frame = new AñadirActividad_GUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public AñadirActividad_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lblNombreActividad = new JLabel("Nombre Actividad:");
		lblNombreActividad.setBounds(20, 20, 120, 20);
		contentPane.add(lblNombreActividad);

		txtNombreActividad = new JTextField();
		txtNombreActividad.setBounds(150, 20, 150, 25);
		contentPane.add(txtNombreActividad);

		JLabel lblGradoExigencia = new JLabel("Grado Exigencia:");
		lblGradoExigencia.setBounds(20, 60, 120, 20);
		contentPane.add(lblGradoExigencia);

		txtGradoExigencia = new JTextField();
		txtGradoExigencia.setBounds(150, 58, 150, 25);
		contentPane.add(txtGradoExigencia);

		JButton botonAñadir = new JButton("Añadir actividad");
		botonAñadir.setBounds(353, 20, 180, 104);
		contentPane.add(botonAñadir);
		
		JLabel lblPrecio = new JLabel("Precio:");
		lblPrecio.setBounds(20, 104, 120, 20);
		contentPane.add(lblPrecio);
		
		txtPrecio = new JTextField();
		txtPrecio.setBounds(150, 99, 150, 25);
		contentPane.add(txtPrecio);

		// Acción del botón
		botonAñadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Mirar caso vacio
					if (txtGradoExigencia.getText().isEmpty() || txtNombreActividad.getText().isEmpty() 
							|| txtPrecio.getText().isEmpty()) {
						JOptionPane.showMessageDialog(AñadirActividad_GUI.this, "Rellena todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					//Control de errores, gradoexigencia
					int gradoExigencia = Integer.parseInt(txtGradoExigencia.getText());
					if (1 > gradoExigencia || gradoExigencia > 5) {
						//Grado exigencia debe estar entre 1 y 5
						JOptionPane.showMessageDialog(AñadirActividad_GUI.this, "Grado exigencia debe estar entre 1 y 5.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					//Precio
					int precio = Integer.parseInt(txtPrecio.getText());
					if (precio < 1) {
						JOptionPane.showMessageDialog(AñadirActividad_GUI.this, "EL precio debe ser un numero entero positivo.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					//Llamada
					boolean b = logicaNegocio.addActividad(txtNombreActividad.getText(), gradoExigencia, precio);
					
					if (b) {
						JOptionPane.showMessageDialog(AñadirActividad_GUI.this, "No se ha añadido porque la actividad ya existe.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
						return;
						//Se añadio correctamente
					} else {
						JOptionPane.showMessageDialog(AñadirActividad_GUI.this, "Se añadio correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
						return;
						//No se añadio porque la actividad ya existe
					}
				} catch (NumberFormatException ex1) {
					//el grado exigencia y el precio deben ser numeros
					JOptionPane.showMessageDialog(AñadirActividad_GUI.this, "El grado exigencia y el precio deben ser numeros enteros positivos.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
	}
}
