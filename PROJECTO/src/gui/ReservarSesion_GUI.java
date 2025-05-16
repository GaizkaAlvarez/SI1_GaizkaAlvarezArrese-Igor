package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import LogicaNegocio.LogicaNegocio;
import domain.*;

public class ReservarSesion_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNombreActividad;
	private JTextField txtGradoExigencia;
	private JTextArea textAreaResultados;
	
	private DefaultComboBoxModel<Integer> numSocioBoxModel = new DefaultComboBoxModel<Integer>();
	private JComboBox<Integer> comboBox = new JComboBox<Integer>();

	private LogicaNegocio logicaNegocio = new LogicaNegocio();

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				ReservarSesion_GUI frame = new ReservarSesion_GUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public ReservarSesion_GUI() {
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

		JButton botonConsultar = new JButton("Reservar sesion");
		botonConsultar.setBounds(350, 55, 180, 30);
		contentPane.add(botonConsultar);

		textAreaResultados = new JTextArea();
		textAreaResultados.setEditable(false);
		textAreaResultados.setLineWrap(true);
		textAreaResultados.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(textAreaResultados);
		scrollPane.setBounds(20, 128, 540, 225);
		contentPane.add(scrollPane);
		
		JLabel lblNumeroSocio = new JLabel("Numero Socio");
		lblNumeroSocio.setBounds(20, 95, 120, 20);
		contentPane.add(lblNumeroSocio);
		
		numSocioBoxModel.removeAllElements();
		for (Integer unekoa : logicaNegocio.getNumeroSocios()) {
			numSocioBoxModel.addElement(unekoa);
		}
		comboBox.setBounds(150, 95, 150, 21);
		contentPane.add(comboBox);
		comboBox.setModel(numSocioBoxModel);
		comboBox.setSelectedIndex(0);

		// Acción del botón
		botonConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textAreaResultados.setText("");
				String nombre = txtNombreActividad.getText();
				String intensidadTexto = txtNombreActividad.getText();
				if (intensidadTexto.isEmpty() || nombre.isEmpty()) {
					JOptionPane.showMessageDialog(
	                		ReservarSesion_GUI.this,
	                    "Debes rellenar todos los campos",
	                    "ERROR",
	                    JOptionPane.ERROR_MESSAGE
	                );
	                return;
				}
				try {
					//Control de errores, actividad
					int gradoExigencia = Integer.parseInt(txtGradoExigencia.getText());
					if (1 > gradoExigencia || gradoExigencia > 5) {
						//Grado exigencia debe estar entre 1 y 5
						JOptionPane.showMessageDialog(ReservarSesion_GUI.this, "Grado exigencia debe estar entre 1 y 5.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(!logicaNegocio.existeActividad(txtNombreActividad.getText(), gradoExigencia)) {
						JOptionPane.showMessageDialog(ReservarSesion_GUI.this, "No existe la actividad insertada.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					
					List<Sesion> sesiones = logicaNegocio.getSesiones(nombre, gradoExigencia);

					if (sesiones.isEmpty()) {
						JOptionPane.showMessageDialog(ReservarSesion_GUI.this, "No se encontraron sesiones.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
						return;
						//textAreaResultados.setText("No se encontraron sesiones.");
					} else {
						for (Sesion s : sesiones) {
							textAreaResultados.append(s.toString() + "\n");
						}
						
						String idSesionTexto = JOptionPane.showInputDialog(ReservarSesion_GUI.this, "Escribe el id de la sesión que quieres reservar:");					
						try {
							int numSocio = (Integer)comboBox.getSelectedItem();
							int idSesion = Integer.parseInt(idSesionTexto);
							//Control de errores, sesion
							boolean esta = false;
							for (Sesion sesion : sesiones) {
								if (sesion.getIdSesion() == idSesion) {
									esta = true;
									break;
								}
							}
							if (!esta) {
								JOptionPane.showMessageDialog(ReservarSesion_GUI.this, "Indique una sesion que este en la lista desplegada.", "Error", JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							
							int n = logicaNegocio.reservarSesion(numSocio, idSesion);	
							if (n == -1) {
								JOptionPane.showMessageDialog(ReservarSesion_GUI.this, "La sesion esta llena, se te ha añadido a la lista de espera.", "Error", JOptionPane.ERROR_MESSAGE);
								return;
							} else if (n == -2) {
								JOptionPane.showMessageDialog(ReservarSesion_GUI.this, "Numero maximo de reservas alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
								return;
							} else if (n == -3) {
								JOptionPane.showMessageDialog(ReservarSesion_GUI.this, "El socio ya tiene una reserva de esta sesion", "Error", JOptionPane.ERROR_MESSAGE);
								return;
							}
							else {
								JOptionPane.showMessageDialog(ReservarSesion_GUI.this, "El id de tu reserva es " + n, "Mensaje", JOptionPane.INFORMATION_MESSAGE);
								return;
							}	
						} catch (NumberFormatException e2) {
							JOptionPane.showMessageDialog(ReservarSesion_GUI.this, "Numero de socio y el identificador de la sesion deben ser numeros.", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}	
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(ReservarSesion_GUI.this, "La intensidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
	}
}
