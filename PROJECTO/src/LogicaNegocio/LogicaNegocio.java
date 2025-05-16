package LogicaNegocio;

import java.time.*;
import java.util.*;

import DataAcces.DataAccess;
import domain.Factura;

import domain.*;

public class LogicaNegocio {
	private DataAccess da;
	
	public LogicaNegocio() {
		da = new DataAccess();
	}
	
	//Se asume que existe la actividad
	public List<Sesion> getSesiones(String nombreActividad, int gradoExigencia){
		da.openDb();
		List<Sesion> list = da.getSesiones(nombreActividad, gradoExigencia);
		da.closeDb();
		return list;
	}
	
	//Se asume que el socio y la sesion existen 
	public int reservarSesion(int numSocio, int idSesion) {
		da.openDb();
		int respuesta = da.reservarSesion(numSocio, idSesion);
		da.closeDb();
		return respuesta;
	}
	
	public List<Factura> getFacturasNoPagadas(int numSocio){
		da.openDb();
		List<Factura> list = da.getFacturasNoPagadas(numSocio);
		da.closeDb();
		return list;
	}
	
	public boolean pagarFactura(String metodo, int numSocio, int codigoFactura) {
		da.openDb();
		boolean respuesta = da.pagarFactura(metodo, numSocio, codigoFactura);
		da.closeDb();
		return respuesta;
	}
	
	public boolean addActividad(String NombreActividad, int gradoExigencia, int precio) {
		da.openDb();
		boolean respuesta = da.addActividad2(NombreActividad, gradoExigencia, precio);
		da.closeDb();
		return respuesta;
	}
	
	public List<Sesion> getSesionesDeSala(String nombreSala){
		da.openDb();
		List<Sesion> list = da.getSesionesDeSala(nombreSala);
		da.closeDb();
		return list;
	}
	
	public int addSesionSala(String nombreSala, String nombreActividad, int gradoExigencia, LocalDate fecha, LocalTime horaIni, LocalTime horaFin) {// 0 bien, -1 llena, -2 ocupada
		da.openDb();
		int respuesta = da.addSesionSala2(nombreSala, nombreActividad, gradoExigencia, fecha, horaIni, horaFin);
		da.closeDb();
		return respuesta;
	}
	
	public boolean enviarFacturas() {
		da.openDb();
		boolean respuesta = da.enviarFacturas();
		da.closeDb();
		return respuesta;
	}
	
	public void cancelarReserva(int idReserva, int numSocio) {
		da.openDb();
		da.cancelarReserva(idReserva, numSocio);
		da.closeDb();
	}

	public List<Integer> getNumeroSocios() {
		da.openDb();
		List<Integer> list = da.getNumeroSocios();
		da.closeDb();
		return list;
	}

	public boolean existeSala(String text) {
		da.openDb();
		boolean respuesta = da.existeSala(text);
		da.closeDb();
		return respuesta;
	}

	public boolean existeActividad(String text, int gradoExigencia) {
		da.openDb();
		boolean respuesta = da.existeActividad(text, gradoExigencia);
		da.closeDb();
		return respuesta;
	}

	public boolean tieneReservaSocio(int numSocio, int idReserva) {
		da.openDb();
		boolean respuesta = da.tieneReservaSocio(numSocio, idReserva);
		da.closeDb();
		return respuesta;
	}
}
