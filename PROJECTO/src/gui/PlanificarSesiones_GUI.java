package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.*;

import LogicaNegocio.LogicaNegocio;
import domain.*;

public class PlanificarSesiones_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textNombreSala;
	private JTextArea textAreaResultados;

	// Simulación de la lógica (¡Sustituye esto por tu clase real!)
	private LogicaNegocio logicaNegocio = new LogicaNegocio();
	private JTextField textNombreActividad;
	private JTextField textGradoExigencia;
	private JTextField textDia;
	private JTextField textHoraInicio;
	private JTextField textHoraFinal;
	private JTextField textMes;
	private JTextField textAño;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				PlanificarSesiones_GUI frame = new PlanificarSesiones_GUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public PlanificarSesiones_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lblNombreSala = new JLabel("Nombre Sala:");
		lblNombreSala.setBounds(20, 20, 120, 20);
		contentPane.add(lblNombreSala);

		textNombreSala = new JTextField();
		textNombreSala.setBounds(150, 20, 150, 25);
		contentPane.add(textNombreSala);

		JButton botonConsultar = new JButton("Consultar Sesiones");
		botonConsultar.setBounds(380, 17, 180, 30);
		contentPane.add(botonConsultar);

		textAreaResultados = new JTextArea();
		textAreaResultados.setEditable(false);
		textAreaResultados.setLineWrap(true);
		textAreaResultados.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(textAreaResultados);
		scrollPane.setBounds(20, 55, 540, 119);
		contentPane.add(scrollPane);
		
		JLabel lblNombreActividad = new JLabel("Nombre Actividad:");
		lblNombreActividad.setBounds(20, 184, 120, 20);
		contentPane.add(lblNombreActividad);
		
		JLabel lblGradoExigencia = new JLabel("Grado Exigencia:");
		lblGradoExigencia.setBounds(20, 214, 120, 20);
		contentPane.add(lblGradoExigencia);
		
		JLabel lblDia = new JLabel("Dia:");
		lblDia.setBounds(410, 311, 38, 20);
		contentPane.add(lblDia);
		
		JLabel lblHoraInicio = new JLabel("Hora Inicio: (int)");
		lblHoraInicio.setBounds(20, 244, 120, 20);
		contentPane.add(lblHoraInicio);
		
		JLabel lblHoraFinal = new JLabel("Hora Final: (int)");
		lblHoraFinal.setBounds(20, 274, 120, 20);
		contentPane.add(lblHoraFinal);
		
		textNombreActividad = new JTextField();
		textNombreActividad.setBounds(150, 184, 150, 25);
		contentPane.add(textNombreActividad);
		
		textGradoExigencia = new JTextField();
		textGradoExigencia.setBounds(150, 215, 150, 25);
		contentPane.add(textGradoExigencia);
		
		textDia = new JTextField();
		textDia.setBounds(410, 328, 150, 25);
		contentPane.add(textDia);
		
		textHoraInicio = new JTextField();
		textHoraInicio.setBounds(150, 242, 150, 25);
		contentPane.add(textHoraInicio);
		
		textHoraFinal = new JTextField();
		textHoraFinal.setBounds(150, 272, 150, 25);
		contentPane.add(textHoraFinal);
		
		JButton btnAñadirSesion = new JButton("Añadir Sesion");
		btnAñadirSesion.setBounds(363, 184, 180, 110);
		contentPane.add(btnAñadirSesion);
		
		textMes = new JTextField();
		textMes.setBounds(220, 328, 150, 25);
		contentPane.add(textMes);
		textMes.setColumns(10);
		
		textAño = new JTextField();
		textAño.setColumns(10);
		textAño.setBounds(10, 328, 150, 25);
		contentPane.add(textAño);
		
		JLabel lblMes = new JLabel("Mes:");
		lblMes.setBounds(220, 315, 45, 13);
		contentPane.add(lblMes);
		
		JLabel lblAño = new JLabel("Año:");
		lblAño.setBounds(10, 315, 45, 13);
		contentPane.add(lblAño);
		
		lblDia.setVisible(false);
		lblGradoExigencia.setVisible(false);
		lblHoraFinal.setVisible(false);
		lblHoraInicio.setVisible(false);
		lblNombreActividad.setVisible(false);
		lblAño.setVisible(false);
		lblMes.setVisible(false);
		btnAñadirSesion.setVisible(false);
		textDia.setVisible(false);
		textGradoExigencia.setVisible(false);
		textHoraFinal.setVisible(false);
		textHoraInicio.setVisible(false);
		textNombreActividad.setVisible(false);
		textAño.setVisible(false);
		textMes.setVisible(false);

		// Acción del botón
		botonConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textAreaResultados.setText("");
				if (textNombreSala.getText().isEmpty()) {
					JOptionPane.showMessageDialog(
	                		PlanificarSesiones_GUI.this,
	                    "Debes rellenar todos los campos",
	                    "ERROR",
	                    JOptionPane.ERROR_MESSAGE
	                );
	                return;
				}
				
				if (!logicaNegocio.existeSala(textNombreSala.getText())) {
					JOptionPane.showMessageDialog(
	                		PlanificarSesiones_GUI.this,
	                    "La sala indicada no existe",
	                    "ERROR",
	                    JOptionPane.ERROR_MESSAGE
	                );
	                return;
				}
				
				List<Sesion> listaSesiones = logicaNegocio.getSesionesDeSala(textNombreSala.getText());
				if (listaSesiones.isEmpty()) {
					JOptionPane.showMessageDialog(
	                		PlanificarSesiones_GUI.this, 
	                		"No hay sesiones en la sala","ERROR",
	                		JOptionPane.ERROR_MESSAGE);
				} else {
					for (Sesion sesion : listaSesiones) {
						textAreaResultados.append(sesion.getActividad().getName() + " " + sesion.getActividad().getGradoExigencia() + ": " + sesion.toString() + "\n\n");
					}
					
					lblDia.setVisible(true);
					lblGradoExigencia.setVisible(true);
					lblHoraFinal.setVisible(true);
					lblHoraInicio.setVisible(true);
					lblNombreActividad.setVisible(true);
					lblAño.setVisible(true);
					lblMes.setVisible(true);
					btnAñadirSesion.setVisible(true);
					textDia.setVisible(true);
					textGradoExigencia.setVisible(true);
					textHoraFinal.setVisible(true);
					textHoraInicio.setVisible(true);
					textNombreActividad.setVisible(true);	
					textAño.setVisible(true);
					textMes.setVisible(true);	
				}
				btnAñadirSesion.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e1) {
						try {
							textAreaResultados.setText("");
							if (textAño.getText().isEmpty() || textMes.getText().isEmpty() || textDia.getText().isEmpty() || textNombreActividad.getText().isEmpty() ||
									textGradoExigencia.getText().isEmpty() || textHoraInicio.getText().isEmpty() || textHoraFinal.getText().isEmpty()) {
								JOptionPane.showMessageDialog(
				                		PlanificarSesiones_GUI.this,
				                    "Debes rellenar todos los campos",
				                    "ERROR",
				                    JOptionPane.ERROR_MESSAGE
				                );
				                return;
							}
							//Control de errores, actividad
							int gradoExigencia = Integer.parseInt(textGradoExigencia.getText());
							if (1 > gradoExigencia || gradoExigencia > 5) {
								//Grado exigencia debe estar entre 1 y 5
								JOptionPane.showMessageDialog(PlanificarSesiones_GUI.this, "Grado exigencia debe estar entre 1 y 5.", "Error", JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							if(!logicaNegocio.existeActividad(textNombreActividad.getText(), gradoExigencia)) {
								JOptionPane.showMessageDialog(PlanificarSesiones_GUI.this, "No existe la actividad insertada.", "Error", JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							int año = Integer.parseInt(textAño.getText());
							int mes = Integer.parseInt(textMes.getText());
							int dia = Integer.parseInt(textDia.getText());
							int hIni = Integer.parseInt(textHoraInicio.getText());
							int hFin = Integer.parseInt(textHoraFinal.getText());
							
							//Control de errores, horas
							if(hIni < 7 || hIni > 21) {
								JOptionPane.showMessageDialog(PlanificarSesiones_GUI.this, "La hora de inicio debe estar entre 7 y 21.", "ERROR", JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							if(hFin > 22 || hFin < 8) {
								JOptionPane.showMessageDialog(PlanificarSesiones_GUI.this, "La hora de final debe estar entre 8 y 22.", "ERROR", JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							if (hIni >= hFin || hFin-hIni!=1) {
								JOptionPane.showMessageDialog(PlanificarSesiones_GUI.this, "La hora de final debe ser mayor que la hora de inicio y las sesiones deben ser de una hora.", "ERROR", JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							//Control de errores, fecha
							if(1 > mes || mes > 12 || dia < 1 || dia > 30 || año < 2025) {
								JOptionPane.showMessageDialog(PlanificarSesiones_GUI.this, "Ponme la fecha bien porfavor.", "ERROR", JOptionPane.ERROR_MESSAGE);
								return;		
							}
							
							//No compruebo porque llegados a este punto no hay error 
							LocalDate localDia = LocalDate.of(año, mes, dia);
							LocalTime horaIni = LocalTime.of(hIni, 0);
							LocalTime horaFinal = LocalTime.of(hFin, 0);
				
							switch (logicaNegocio.addSesionSala(textNombreSala.getText(), textNombreActividad.getText(), gradoExigencia , localDia , horaIni, horaFinal)) {
							case 0: //Caso bueno
								JOptionPane.showMessageDialog(PlanificarSesiones_GUI.this, "Sesion añadida correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
								break;
							case -1: //Caso lleno
								JOptionPane.showMessageDialog(PlanificarSesiones_GUI.this, "Sala llena.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
								break;							
							case -2: //Caso ocupado
								JOptionPane.showMessageDialog(PlanificarSesiones_GUI.this, "Sala ocupada ese dia a esa hora.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
								break;
							}
							return;
						} catch (NumberFormatException ex1) {
							JOptionPane.showMessageDialog(PlanificarSesiones_GUI.this, "El grado de exigencia, el dia, el mes, el año y las horas deben ser numeros enteros.", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
			}
		});
	}
}