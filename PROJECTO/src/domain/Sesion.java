package domain;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "sesion")
public class Sesion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idSesion;
    private int cantidadParticipantes;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Temporal(TemporalType.TIME)
    private Date horaInicio;
    @Temporal(TemporalType.TIME)
    private Date horaFinal;
    
    @ManyToOne
    private Sala sala;
    private Actividad actividad;
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    private List<Socio> listaEspera = new LinkedList<>();
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    private List<Reserva> listaReservas = new LinkedList<>();
    
	public Sesion() {
		super();
		//idSesion = id++;
	}

    public Sesion(Sala sala, Actividad actividad, LocalDate fecha, LocalTime horaInicio, LocalTime horaFinal) {
        //this.idSesion = id++;
    	this.cantidadParticipantes = 0;
        this.fecha = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.horaInicio = Date.from(horaInicio.atDate(LocalDate.of(1970, 1, 1))  // usamos una fecha base
                .atZone(ZoneId.systemDefault())
                .toInstant());
        this.horaFinal = Date.from(horaFinal.atDate(LocalDate.of(1970, 1, 1))  // usamos una fecha base
                .atZone(ZoneId.systemDefault())
                .toInstant());
        
        this.sala = sala;
        this.actividad = actividad;
        this.listaEspera = new LinkedList<Socio>();
        this.listaReservas = new LinkedList<Reserva>();
        for (int i = 0; i < sala.getAforoMaximo(); i++) {//Crear todas las reservas de la sesion
			listaReservas.add(new Reserva(this));
		}
    }
    
	public int getIdSesion() {
		return idSesion;
	}

	public void setIdSesion(int idSesion) {
		this.idSesion = idSesion;
	}

	public int getCantidadParticipantes() {
		return cantidadParticipantes;
	}

	public void setCantidadParticipantes(int cantidadParticipantes) {
		this.cantidadParticipantes = cantidadParticipantes;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Date getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(Date horaFinal) {
		this.horaFinal = horaFinal;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public Actividad getActividad() {
		return actividad;
	}

	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}

	public List<Socio> getListaEspera() {
		return listaEspera;
	}

	public void setListaEspera(List<Socio> listaEspera) {
		this.listaEspera = listaEspera;
	}

	public List<Reserva> getListaReservas() {
		return listaReservas;
	}

	public void setListaReservas(List<Reserva> listaReservas) {
		this.listaReservas = listaReservas;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	    
		return "Sesion [SesionID= " + idSesion + ", cantidadParticipantes=" + cantidadParticipantes + ", fecha=" + dateFormat.format(fecha) + ", horaInicio="
				+ timeFormat.format(horaInicio) + ", horaFinal=" + timeFormat.format(horaFinal) + "]";
	}
	
	public void addListaDeEspera(Socio socio) {
		this.listaEspera.add(socio);
	}
	
	public Socio getAndRemoveFirstListaEspera() {
		return this.listaEspera.remove(0);
	}
	
	public boolean isListaEsperaEmpty() {
		return this.listaEspera.isEmpty();
	}
}
