package domain;

import java.time.*;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "reserva")
public class Reserva {
	public enum estadoReserva{
		HECHA, NO_HECHA;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idReserva;
    @Temporal(TemporalType.DATE)
	private Date fechaReservaDate;
	private estadoReserva estado;
	
	private Factura factura;
	private Socio socio;
	private Sesion sesion;
	
	public Reserva() {
		super();
		//idReserva = id++;
	}
	
	public Reserva(Sesion sesion) {
//		idReserva = id++;
		LocalDate fecha = LocalDate.of(1970, 1, 1);
		fechaReservaDate = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
		estado = estadoReserva.NO_HECHA;
		
		factura = null;
		socio = null;
		this.sesion= sesion; 
	}
	
	public int getIdReserva() {
		return idReserva;
	}
	public void setIdReserva(int idReserva) {
		this.idReserva = idReserva;
	}
	public Date getFechaReservaDate() {
		return fechaReservaDate;
	}
	public void setFechaReservaDate(Date fechaReservaDate) {
		this.fechaReservaDate = fechaReservaDate;
	}
	public estadoReserva getEstado() {
		return estado;
	}
	public void setEstado(estadoReserva estado) {
		this.estado = estado;
	}
	public Factura getFactura() {
		return this.factura;
	}
	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	public Socio getSocio() {
		return socio;
	}

	public void setSocio(Socio socio) {
		this.socio = socio;
	}

	public Sesion getSesion() {
		return sesion;
	}

	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}
	
	public void actualizarFecha() {
		this.fechaReservaDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public void removeFactura() {
		this.factura = null;
	}

	public void removeSocio() {
		this.socio = null;
	}
}
