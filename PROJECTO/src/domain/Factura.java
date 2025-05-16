package domain;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "factura")
public class Factura {
	public enum estadoFactura1{
		PAGADA, NO_PAGADA;
	}
	public enum estadoFactura2{
		VISIBLE, NO_VISIBLE;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigoFactura;
	private double precio;
	@Temporal(TemporalType.DATE)
	private Date fechaDeEmision;
	@Enumerated(EnumType.STRING)
	private estadoFactura1 estado1;
	@Enumerated(EnumType.STRING)
	private estadoFactura2 estado2;
	
	private Socio socio;
	@OneToMany
	private LinkedList<Reserva> listaReservas;
	
	public Factura() {
		super();
		//codigoFactura = codigo++;
	}
	
	public Factura(Socio socio) {
		//codigoFactura = codigo++;
		precio = 0.0;
		LocalDate fecha = LocalDate.of(1970, 1, 1);
		fechaDeEmision = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
		estado1 = estadoFactura1.NO_PAGADA;
		estado2 = estadoFactura2.NO_VISIBLE;
		
		this.socio = socio;
		this.listaReservas = new LinkedList<Reserva>();
	}

	public Socio getSocio() {
		return socio;
	}

	public void setSocio(Socio socio) {
		this.socio = socio;
	}

	public LinkedList<Reserva> getListaReservas() {
		return listaReservas;
	}

	public void setListaReservas(LinkedList<Reserva> listaReservas) {
		this.listaReservas = listaReservas;
	}

	public int getCodigoFactura() {
		return codigoFactura;
	}

	public void setCodigoFactura(int codigoFactura) {
		this.codigoFactura = codigoFactura;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Date getFechaDeEmision() {
		return fechaDeEmision;
	}

	public void setFechaDeEmision(Date fechaDeEmision) {
		this.fechaDeEmision = fechaDeEmision;
	}

	public estadoFactura1 getEstado1() {
		return estado1;
	}

	public void setEstado1(estadoFactura1 estado1) {
		this.estado1 = estado1;
	}

	public estadoFactura2 getEstado2() {
		return estado2;
	}

	public void setEstado2(estadoFactura2 estado2) {
		this.estado2 = estado2;
	}
	
	public void addReserva(Reserva reserva) {
		this.listaReservas.add(reserva);
	}
	
	public void actualizarPrecioSUMAR(Reserva reserva) {
		this.precio += reserva.getSesion().getActividad().getPrecio();
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return "Factura [codigoFactura=" + codigoFactura + ", precio=" + precio + this.estado1 + " " + this.estado2 + ", fechaDeEmision=" + dateFormat.format(fechaDeEmision) + "]";
	}

	public void removeReserva(Reserva reserva) {
		listaReservas.remove(reserva);
	}

	public void actualizarPrecioRESTAR(Reserva reserva) {
		this.precio -= reserva.getSesion().getActividad().getPrecio();
	}

	public boolean contieneReserva(Reserva reserva) {
		for (Reserva reserva2 : listaReservas) {
			if (reserva2.equals(reserva)) {
				return true;
			}
		}
		return false;
	}
	
}
