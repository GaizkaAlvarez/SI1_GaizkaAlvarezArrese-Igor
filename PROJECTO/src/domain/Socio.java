package domain;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "socio")
public class Socio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int numSocio;
	
	private String nombre;
	private String apellido;
	private String email;
	private int numeroCuentaBancaria;
	private int tarjetaCredito;
	private int numeroReservasHechas;
	private int numeroMaximoReservas;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    private List<Factura> listaFacturas = new LinkedList<>();
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Reserva> listaReservas = new LinkedList<>();
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Sesion> listaSesiones = new LinkedList<>();
	
	public Socio() {
		super();
//		numSocio = num++;
	}
	
	public Socio(String nombre, String apellido, String email, int numeroCuentaBancaria, int tarjetaCredito, int numeroMaximoReservas) {
//		this.numSocio = num++;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.numeroCuentaBancaria = numeroCuentaBancaria;
		this.tarjetaCredito = tarjetaCredito;
		this.numeroReservasHechas = 0;
		this.numeroMaximoReservas = numeroMaximoReservas;
		
		this.listaFacturas = new LinkedList<Factura>();
//		this.listaFacturas.add(new Factura(this));//Una, la actual
		this.listaReservas = new LinkedList<Reserva>();//Empty
		this.listaSesiones = new LinkedList<Sesion>();//Empty
	}

	public int getNumSocio() {
		return numSocio;
	}

	public void setNumSocio(int numSocio) {
		this.numSocio = numSocio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getNumeroCuentaBancaria() {
		return numeroCuentaBancaria;
	}

	public void setNumeroCuentaBancaria(int numeroCuentaBancaria) {
		this.numeroCuentaBancaria = numeroCuentaBancaria;
	}

	public int getTarjetaCredito() {
		return tarjetaCredito;
	}

	public void setTarjetaCredito(int tarjetaCredito) {
		this.tarjetaCredito = tarjetaCredito;
	}

	public int getNumeroReservasHechas() {
		return numeroReservasHechas;
	}

	public void setNumeroReservasHechas(int numeroReservasHechas) {
		this.numeroReservasHechas = numeroReservasHechas;
	}

	public int getNumeroMaximoReservas() {
		return numeroMaximoReservas;
	}

	public void setNumeroMaximoReservas(int numeroMaximoReservas) {
		this.numeroMaximoReservas = numeroMaximoReservas;
	}

	public List<Factura> getListaFacturas() {
		return listaFacturas;
	}

	public void setListaFacturas(List<Factura> listaFacturas) {
		this.listaFacturas = listaFacturas;
	}

	public List<Reserva> getListaReservas() {
		return listaReservas;
	}

	public void setListaReservas(List<Reserva> listaReservas) {
		this.listaReservas = listaReservas;
	}

	public List<Sesion> getListaSesiones() {
		return listaSesiones;
	}

	public void setListaSesiones(List<Sesion> listaSesiones) {
		this.listaSesiones = listaSesiones;
	}
	
	public int actualizarNumeroReservasMas1() {
		this.numeroReservasHechas++;
		return this.numeroMaximoReservas;
	}
	
	public void addReserva(Reserva reserva) {
		this.listaReservas.add(reserva);
	}
	
	public int getDatosMetodoPago(String metodo) {
		if (metodo.equals("Tarjeta de crédito")) {
			return this.tarjetaCredito;
		} else {
			return this.numeroCuentaBancaria;
		}
	}

	public void addFactura(Factura fi) {
		this.listaFacturas.add(fi);
	}

	public void removeReserva(Reserva reserva) {
		this.listaReservas.remove(reserva);
	}

	public void actualizarNumeroReservasMenos1() {
		this.numeroReservasHechas--;
	}
	
	public void addSesion(Sesion sesion) {
		this.listaSesiones.add(sesion);
	}
	
	public void removeSesion(Sesion sesion) {
		this.listaSesiones.remove(sesion);
	}
}
