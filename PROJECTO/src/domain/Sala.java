package domain;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "sala")
public class Sala {
	@Id
	private String nombre;
	private int aforoMaximo;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private LinkedList<Sesion> listaSesiones; //Maximo 15 sesiones por limite horario
	
	public Sala() {
		super();
	}
	
	public Sala(String nombreString, int aforoMaximo) {
		this.nombre = nombreString;
		this.aforoMaximo = aforoMaximo;
		listaSesiones = new LinkedList<Sesion>();
	}
	public LinkedList<Sesion> getListaSesiones() {
		return listaSesiones;
	}
	public void setListaSesiones(LinkedList<Sesion> listaSesiones) {
		this.listaSesiones = listaSesiones;
	}
	public String getNombreString() {
		return nombre;
	}
	public void setNombreString(String nombreString) {
		this.nombre = nombreString;
	}
	public int getAforoMaximo() {
		return aforoMaximo;
	}
	public void setAforoMaximo(int aforoMaximo) {
		this.aforoMaximo = aforoMaximo;
	}
	
	public void addSesion(Sesion sesion) {
		this.listaSesiones.add(sesion);
	}
	
	public int sizeListaSesiones() {
		return this.listaSesiones.size();
	}
	
	public boolean isSalaLlena(LocalDate fecha) {
		int contador = 0;
		Date date = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
		for (Sesion sesion : listaSesiones) {
			if (sesion.getFecha().equals(date)) {
				contador++;
			}
		}
		if (contador==15) return true;
		else return false;
	}
}
