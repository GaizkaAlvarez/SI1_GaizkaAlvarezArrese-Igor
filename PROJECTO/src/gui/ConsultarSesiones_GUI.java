package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import LogicaNegocio.LogicaNegocio;
import domain.*;

public class ConsultarSesiones_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNombreActividad;
	private JTextField txtGradoExigencia;
	private JTextArea textAreaResultados;

	// Simulación de la lógica (¡Sustituye esto por tu clase real!)
	private LogicaNegocio logicaNegocio = new LogicaNegocio();

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				ConsultarSesiones_GUI frame = new ConsultarSesiones_GUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public ConsultarSesiones_GUI() {
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

		JButton botonConsultar = new JButton("Consultar sesiones");
		botonConsultar.setBounds(320, 40, 180, 30);
		contentPane.add(botonConsultar);

		textAreaResultados = new JTextArea();
		textAreaResultados.setEditable(false);
		textAreaResultados.setLineWrap(true);
		textAreaResultados.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(textAreaResultados);
		scrollPane.setBounds(20, 110, 540, 220);
		contentPane.add(scrollPane);

		// Acción del botón
		botonConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textAreaResultados.setText("");
				String nombre = txtNombreActividad.getText();
				String intensidadTexto = txtGradoExigencia.getText();
				 // Validamos si algún campo está vacío
	            if (intensidadTexto.isEmpty() || nombre.isEmpty()) {
	                JOptionPane.showMessageDialog(
	                		ConsultarSesiones_GUI.this,
	                    "Debes rellenar todos los campos",
	                    "ERROR",
	                    JOptionPane.ERROR_MESSAGE
	                );
	                return;
	            }
				try {
					//Control de errores, actividad
					int gradoExigencia = Integer.parseInt(intensidadTexto);
					if (1 > gradoExigencia || gradoExigencia > 5) {
						//Grado exigencia debe estar entre 1 y 5
						JOptionPane.showMessageDialog(ConsultarSesiones_GUI.this, "Grado exigencia debe estar entre 1 y 5.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(!logicaNegocio.existeActividad(txtNombreActividad.getText(), gradoExigencia)) {
						JOptionPane.showMessageDialog(ConsultarSesiones_GUI.this, "No existe la actividad insertada.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					List<Sesion> sesiones = logicaNegocio.getSesiones(nombre, gradoExigencia);

					textAreaResultados.setText("");

					if (sesiones.isEmpty()) {
						textAreaResultados.setText("No se encontraron sesiones.");
					} else {
						for (Sesion s : sesiones) {
							textAreaResultados.append(s.toString() + "\n");
						}
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(ConsultarSesiones_GUI.this, "La intensidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
	}
}
