package DataAcces;

import java.time.*;
import java.util.*;

import javax.persistence.*;
import domain.API_Banco;
import domain.Actividad;
import domain.Factura;
import domain.Factura.estadoFactura1;
import domain.Factura.estadoFactura2;
import domain.Reserva;
import domain.Sesion;
import domain.Socio;
import domain.gradoExigenciaException;
import domain.Reserva.estadoReserva;
import domain.Sala;


public class DataAccess {
	private String dbName = "America.odb";
	private EntityManagerFactory emf;
	private EntityManager em;

	public DataAccess() {
	}
	
	public void openDb() {
		emf = Persistence.createEntityManagerFactory(dbName);
		em = emf.createEntityManager();	
		System.out.println("Database opened");
	}
	
	public void closeDb() {
		em.close();
		System.out.println("Database closed");
	}
	
	public List<Sesion> getSesiones(String nombreActividad, int gradoExigencia){
		TypedQuery<Sesion> query = em.createQuery(
		        "SELECT s FROM Sesion s WHERE s.actividad.nombre=?1 AND  s.actividad.gradoExigencia=?2"
				, Sesion.class
				);
		query.setParameter(1, nombreActividad);
		query.setParameter(2, gradoExigencia);
		return (List<Sesion>) query.getResultList();
	}
	
	public boolean alcanzadoMaxReservas(Socio socio) {
		return socio.getNumeroReservasHechas() == socio.getNumeroMaximoReservas();
		
	}
	
	public boolean isLlenaSesion(Sesion sesion) {
		return sesion.getCantidadParticipantes() == sesion.getSala().getAforoMaximo();
	}
	
	public Reserva getUnaReservaLibre(Sesion sesion) {
			openDb();
			TypedQuery<Reserva> query = em.createQuery(
					"SELECT r FROM Reserva r WHERE r.sesion.idSesion=?1 AND r.estado=?2"
					, Reserva.class
					);
			query.setParameter(1, sesion.getIdSesion());
			query.setParameter(2, estadoReserva.NO_HECHA);
			Reserva res = query.getResultList().get(0);
			closeDb();
			return res;	
	}
	
	public Factura getFacturaNoVisible(Socio socio) {
		openDb();
		TypedQuery<Factura> query = em.createQuery(
			"SELECT f FROM Factura f WHERE f.socio.numSocio=?1 AND f.estado2=?2"
			, Factura.class
				);
		query.setParameter(1, socio.getNumSocio());
		query.setParameter(2, estadoFactura2.NO_VISIBLE);
		Factura factura = query.getResultList().get(0);
		closeDb();
		
		return factura;
	}
	
	public Socio getSocio(int numSocio) {
		openDb();
		Socio socio = em.find(Socio.class, numSocio);
		closeDb(); 
		return socio;
	}
	
	public Sesion getSesion(int idSesion) {
		openDb();
		Sesion sesion = em.find(Sesion.class, idSesion);
		closeDb();
		return sesion;
	}
	
	public Factura getFactura(int codigoFactura) {
		openDb();
		Factura factura = em.find(Factura.class, codigoFactura);
		closeDb();
		return factura;
	}
	
	public List<Factura> getFacturasNoPagadas(int numSocio){
		TypedQuery<Factura> query = em.createQuery(
				"SELECT f FROM Factura f WHERE f.socio.numSocio=?1 AND f.estado1=?2 AND f.estado2=?3"
				, Factura.class 
				);
			query.setParameter(1, numSocio);
			query.setParameter(2, estadoFactura1.NO_PAGADA);
			query.setParameter(3, estadoFactura2.VISIBLE);
			return (List<Factura>) query.getResultList();
	}
	
	public boolean pagarFactura(int datos ,int codigoFactura, API_Banco api_Banco) {
		openDb();
		em.getTransaction().begin();
		
		Factura factura = em.find(Factura.class, codigoFactura);
		
		if (api_Banco.pagar(datos, factura.getPrecio())) {
			factura.setEstado1(estadoFactura1.PAGADA);
//			em.merge(factura);
			
			em.getTransaction().commit();
			closeDb();
			return true;
		} else {
			em.getTransaction().commit();
			closeDb();
			return false;
		}
	}
	
	public boolean existeActividad(String nombreActividad, int gradoExigencia) {
	    try {
	        TypedQuery<Actividad> query = em.createQuery(
	            "SELECT a FROM Actividad a WHERE a.nombre=?1 AND a.gradoExigencia=?2",
	            Actividad.class
	        );
	        query.setParameter(1, nombreActividad);
	        query.setParameter(2, gradoExigencia);
	        
	        query.getResultList().get(0); // Si llega aquí, existe
	        return true;
	    } catch (Exception e) {
	        return false; // No se encontró ninguna actividad
	    }
	}

	
	public void addActividad(String nombreActividad, int gradoExigencia, int precio) {
		openDb();
		em.getTransaction().begin();
		
		try {
			Actividad actividad = new Actividad(nombreActividad, gradoExigencia, precio);
			em.persist(actividad);
			em.getTransaction().commit();
		} catch (gradoExigenciaException e) { //Aqui no se llega nunca porque cubro el caso en el GUI
			// TODO Auto-generated catch block
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			closeDb();
		}
	}
	
	public List<Sesion> getSesionesDeSala(String nombreSala){
		TypedQuery<Sesion> query = em.createQuery(
				"SELECT s FROM Sesion s WHERE s.sala.nombre=?1"
				, Sesion.class
				);
		query.setParameter(1, nombreSala);
		List<Sesion> sesions = query.getResultList();
		return sesions;
	}
	
	public Sala getSala(String nombreSala) {
		openDb();
		Sala sala = em.find(Sala.class, nombreSala);
		closeDb();
		return sala;
	}
	
	public Actividad getActividad(String nombreActividad, int gradoExigencia) {
		openDb();
		TypedQuery<Actividad> query = em.createQuery(
				"SELECT a FROM Actividad a WHERE a.nombre=?1 AND a.gradoExigencia=?2"
				, Actividad.class
				);
		query.setParameter(1, nombreActividad);
		query.setParameter(2, gradoExigencia);
		Actividad actividad = (Actividad) query.getResultList().get(0);
		closeDb();
		return actividad;
	}
	
	public boolean isLlenaSala(Sala sala, LocalDate fecha) {
		return sala.isSalaLlena(fecha);
	}
	
	public boolean estaOcupadaSala(Sala sala, LocalDate fecha, LocalTime horaIni) {
		List<Sesion> list = sala.getListaSesiones();
		for (Sesion sesion : list) {
			if (sesion.getFecha().equals(Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant())) 
					&& sesion.getHoraInicio().equals(Date.from(horaIni.atDate(LocalDate.of(1970, 1, 1)).atZone(ZoneId.systemDefault()).toInstant()))) return true;
		}
		return false;
	}
	
	//Antes de llamar a este metodo se mira si esta ocupada o si esta llena
	public void addSesionSala(String nombreSala, String nombreActividad, int gradoExigencia, LocalDate fecha, LocalTime horaIni, LocalTime horaFin) {
		openDb();
		em.getTransaction().begin();
		
		Sala sala = em.find(Sala.class, nombreSala);

		Query query = em.createQuery(
				"SELECT a FROM Actividad a WHERE a.nombre=?1 AND a.gradoExigencia=?2"
					);
		query.setParameter(1, nombreActividad);
		query.setParameter(2, gradoExigencia);
		Actividad actividad = (Actividad) query.getResultList().get(0);
		
		Sesion sesion = new Sesion(sala, actividad, fecha, horaIni, horaFin);
		
		sala.addSesion(sesion);
		actividad.addSesion(sesion);
		
		em.persist(sesion);
//		em.merge(sala);
//		em.merge(actividad);
		
		em.getTransaction().commit();
		closeDb();
	}

	public List<Socio> getSocios() {
		openDb();
		TypedQuery<Socio> query = em.createQuery(
				"SELECT s FROM Socio s"
				, Socio.class	);
		List<Socio> laList = query.getResultList();
		closeDb();
		return laList;
	}

	public void setEstado2Visible(Factura fi) {
		openDb();
		em.getTransaction().begin();
		
		fi.setEstado2(estadoFactura2.VISIBLE);
//		em.merge(fi);
		
		em.getTransaction().commit();
		closeDb();
	}

	public void crearFactura(Socio socio) {
		openDb();
		em.getTransaction().begin();
		
		Factura fi = new Factura(socio);
		
		socio.addFactura(fi);
		
//		em.merge(socio);
		em.persist(fi);
		
		em.getTransaction().commit();
		closeDb();
	}

	public Reserva getReserva(int idReserva) {
		openDb(); 
		Reserva reserva = em.find(Reserva.class, idReserva);
		closeDb();
		return reserva;
	}

	public void cancelarReserva(Socio socio, Reserva reserva, Factura factura) {
		openDb();
		em.getTransaction().begin();
		
		reserva.setEstado(estadoReserva.NO_HECHA);
		reserva.removeFactura();
		reserva.removeSocio();
		
		factura.removeReserva(reserva);
		factura.actualizarPrecioRESTAR(reserva);
		
		socio.removeReserva(reserva);
		socio.actualizarNumeroReservasMenos1();
		
//		em.merge(reserva);
//		em.merge(factura);
//		em.merge(socio);
		
		em.getTransaction().commit();
		closeDb();
	}

	public Sesion getSesionReserva(Reserva reserva) {
		openDb(); 
		TypedQuery<Sesion> query = em.createQuery(
				"SELECT s FROM Sesion s WHERE s.reserva.idReserva=?1"
				, Sesion.class	);
		query.setParameter(1, reserva.getIdReserva());
		Sesion sesion = query.getResultList().get(0);
		closeDb();
		return sesion;
	}

	public boolean isListaDeEsperaEmpty(Sesion sesion) {
		// TODO Auto-generated method stub
		return sesion.isListaEsperaEmpty();
	}

	public Socio getAndRemoveFirstListaEspera(Sesion sesion) {
		openDb();
		em.getTransaction().begin();
		
		Socio socio = sesion.getAndRemoveFirstListaEspera();
		socio.removeSesion(sesion);
		
//		em.merge(socio);
		
		em.getTransaction().commit();
		closeDb();
		
		return socio;
	}

	public int actualizarNumeroReservasMas1(Socio soc1) {
		openDb();
		em.getTransaction().begin();
		
		int n = soc1.actualizarNumeroReservasMas1();
//		em.merge(soc1);
		
		em.getTransaction().commit();
		closeDb();
		
		return n;
	}

	public void addReservaAFactura(Factura fi, Reserva reserva) {
		openDb();
		em.getTransaction().begin();
		
		reserva.setFactura(fi);
		
		fi.addReserva(reserva);
		fi.actualizarPrecioSUMAR(reserva);
		
//		em.merge(reserva);
//		em.merge(fi);
		
		em.getTransaction().commit();
		closeDb();
	}

	public void actualizarReserva(Socio socio, Reserva reserva, Sesion sesion) {
		openDb();
		em.getTransaction().begin();
		
		reserva.setSocio(socio);
		socio.addReserva(reserva);
		
		reserva.actualizarFecha();
		reserva.setEstado(estadoReserva.HECHA);
		
		sesion.setCantidadParticipantes(sesion.getCantidadParticipantes() + 1);
		
//		em.merge(reserva);
//		em.merge(socio);
//		em.merge(sesion);
		
		em.getTransaction().commit();
		closeDb();
	}

	public void addListaDeEspera(Socio socio, Sesion sesion) {
		openDb();
		em.getTransaction().begin();
		
		sesion.addListaDeEspera(socio);
		socio.addSesion(sesion);
		
//		em.merge(socio);
//		em.merge(sesion);
		
		em.getTransaction().commit();
		closeDb();
	}
	
	public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("America.odb");
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();

            // 1) Crear y persistir salas
            Sala sala1 = new Sala("Sala1", 15);
            Sala sala2 = new Sala("Sala2", 5);

            // 2) Crear y persistir actividades
            Actividad act1 = new Actividad("Pilates", 3, 15);
            Actividad act2 = new Actividad("Pilates", 5, 20);
            Actividad act3 = new Actividad("Padel",   1,  8);
            Actividad act4 = new Actividad("Padel",   4,  18);
            Actividad act5 = new Actividad("Futbol",   5,  35);
            

            // 3) Crear y persistir socios
            Socio soc1 = new Socio("Juan",    "Alvarez", "juan@mail.com",   1234, 4321, 10);
            Socio soc2 = new Socio("Ariadna", "Gimeno",  "ari@mail.com",    5678, 8765, 5);
            Socio soc3 = new Socio("Esther", "Alesanco",  "acuatico@mail.com",    5464, 1111, 25);
            Socio soc4 = new Socio("Loraine", "Arrese-Igor",  "lora@mail.com",    4444, 0000, 3);

            // 4) Crear y persistir facturas (lado dueño)
            Factura fac1 = new Factura(soc1);
            Factura fac2 = new Factura(soc2);
            Factura fac31 = new Factura(soc3);
            Factura fac32 = new Factura(soc3);   
            fac31.setEstado2(estadoFactura2.VISIBLE);
            fac31.setPrecio(15);
            Factura fac41 = new Factura(soc4);
            Factura fac42 = new Factura(soc4);
            fac41.setEstado2(estadoFactura2.VISIBLE);
            fac41.setPrecio(45);
            
            // 5) Crear y persistir sesiones
            Sesion ses1 = new Sesion(sala1, act1, LocalDate.of(2025,8,1), LocalTime.of( 20,0), LocalTime.of( 21,0));
            Sesion ses2 = new Sesion(sala1, act2, LocalDate.of(2025,8,3), LocalTime.of(18,0), LocalTime.of(19,0));
            Sesion ses3 = new Sesion(sala2, act3, LocalDate.of(2025,8,1), LocalTime.of( 8,0), LocalTime.of( 9,0));
            ses3.setCantidadParticipantes(5);
            Sesion ses4 = new Sesion(sala2, act4, LocalDate.of(2025,8,1), LocalTime.of( 9,0), LocalTime.of( 10,0));
            Sesion ses5 = new Sesion(sala2, act4, LocalDate.of(2025,8,1), LocalTime.of( 10,0), LocalTime.of( 11,0));
            Sesion ses6 = new Sesion(sala2, act5, LocalDate.of(2025,8,1), LocalTime.of( 11,0), LocalTime.of( 12,0));
            // 6) Sincronizar colecciones bidireccionales
            sala1.getListaSesiones().add(ses1);
            sala1.getListaSesiones().add(ses2);
            sala2.getListaSesiones().add(ses3);
            sala2.getListaSesiones().add(ses4);
            sala2.getListaSesiones().add(ses5);
            sala2.getListaSesiones().add(ses6);
//            for(int i = 0; i < 15; i++) {
//            	Sesion sesion = new Sesion(sala2, act3, LocalDate.of(2025,8,1), LocalTime.of(8+i,0), LocalTime.of(9+i,0));
//            	sala2.getListaSesiones().add(sesion);
//            	em.persist(sesion);
//            }

            act1.getListaSesiones().add(ses1);
            act2.getListaSesiones().add(ses2);
            act3.getListaSesiones().add(ses3);
            act4.getListaSesiones().add(ses4);
            act4.getListaSesiones().add(ses5);
            act5.getListaSesiones().add(ses6);

            soc1.addFactura(fac1);
            soc2.addFactura(fac2);
            soc3.addFactura(fac31);
            soc3.addFactura(fac32);
            soc4.addFactura(fac41);
            soc4.addFactura(fac42);
            //soc2.addFactura(fac3);

            //Todos los persist
            em.persist(sala1);
            em.persist(sala2);
            
            em.persist(act1);
            em.persist(act2);
            em.persist(act3);
            em.persist(act4);
            em.persist(act5);
            
            em.persist(soc1);
            em.persist(soc2);
            em.persist(soc3);
            em.persist(soc4);
            
            em.persist(fac1);
            em.persist(fac2);
            em.persist(fac31);
            em.persist(fac32);
            em.persist(fac41);
            em.persist(fac42);
            
            em.persist(ses1);
            em.persist(ses2);
            em.persist(ses3);
            em.persist(ses4);
            em.persist(ses5);
            em.persist(ses6);
            
            for (Reserva r : ses1.getListaReservas()) {
				em.persist(r);
			}
            for (Reserva r : ses2.getListaReservas()) {
				em.persist(r);
			}
            for (Reserva r : ses3.getListaReservas()) {
				em.persist(r);
			}
            
            em.getTransaction().commit();
            
            System.out.println("Inserted");

        } catch (gradoExigenciaException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
        
//        TypedQuery<Actividad> query = em.createQuery(
//				"SELECT a FROM Actividad a"
//				, Actividad.class	);
//        
//		List<Actividad> listaActividads = query.getResultList();
//		for (Actividad actividad : listaActividads) {
//			System.out.println(actividad.toString() + "\n");
//		}   
//      TypedQuery<Socio> query = em.createQuery(
//				"SELECT s FROM Socio s"
//				, Socio.class	);
//        List<Socio> listaSocios = query.getResultList(); 
//			for (Socio socio : listaSocios) {
//				for (Factura factura : socio.getListaFacturas()) {
//					System.out.println(factura.toString());
//				}
//			}
        
//        for (Socio socio : listaSocios) {
//			for (Reserva reserva : socio.getListaReservas()) {
//				System.out.println("El socio " + socio.getNumSocio() + " tiene la reserva: " + reserva.getIdReserva());
//			}
//		}
        
//        Socio socio = em.find(Socio.class, 1);
//        System.out.println(socio.getListaFacturas().get(0).getPrecio());
//		  em.close();
//		  emf.close();
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public void addReservaSocio(Socio socio, Reserva ri) {
		openDb();
		em.getTransaction().begin();
		
		socio.getListaReservas().add(ri);
		ri.setSocio(socio);
		
//		em.merge(socio);
//		em.merge(ri);
		
		em.getTransaction().commit();
		closeDb();
		
	}

	public void addParticipanteSesion(Sesion si) {
		openDb();
		em.getTransaction().begin();
		
		si.setCantidadParticipantes(si.getCantidadParticipantes() + 1);
//		em.merge(si);
		
		em.getTransaction().commit();
		closeDb();
	}

	public void actualizarFechaEmision(Factura fi) {
		openDb();
		em.getTransaction().begin();
		
		LocalDate localDate = LocalDate.now();
		fi.setFechaDeEmision(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
//		em.merge(fi);
		
		em.getTransaction().commit();
		closeDb();
	}

	public void clearListaReserva(Factura fi) {
		openDb();
		em.getTransaction().begin();
		
		fi.getListaReservas().clear();
//		em.merge(fi);
		
		em.getTransaction().commit();
		closeDb();
	}

	public int reservarSesion(int numSocio, int idSesion) {
		em.getTransaction().begin();
		Socio socio = em.find(Socio.class, numSocio);
		Sesion sesion = em.find(Sesion.class, idSesion);
		for (Reserva unekoa : socio.getListaReservas()) {
			if (unekoa.getSesion().equals(sesion)) {
				return -3;
			}
		}
		//Numero maximo de reservas?
		boolean y = (socio.getNumeroReservasHechas() == socio.getNumeroMaximoReservas());
		if (!y) {
			//Esta llena la sala?	
			boolean b = (sesion.getCantidadParticipantes() == sesion.getSala().getAforoMaximo());
			if (!b) {// Hacer reserva
				//conseguir una reserva libre
				TypedQuery<Reserva> query = em.createQuery(
						"SELECT r FROM Reserva r WHERE r.sesion.idSesion=?1 AND r.estado=?2"
						, Reserva.class
						);
				query.setParameter(1, sesion.getIdSesion());
				query.setParameter(2, estadoReserva.NO_HECHA);
				Reserva ri = query.getResultList().get(0);
				
				//Sumar la reserva al socio
				int n = socio.actualizarNumeroReservasMas1();
//				em.merge(socio); //Necesario?
				
				if (n > 4) {	
					TypedQuery<Factura> query2 = em.createQuery(
							"SELECT f FROM Factura f WHERE f.socio.numSocio=?1 AND f.estado2=?2"
							, Factura.class
								);
						query2.setParameter(1, socio.getNumSocio());
						query2.setParameter(2, estadoFactura2.NO_VISIBLE);
						Factura fi = query2.getResultList().get(0);
						
						//Añadir reserva a factura
						ri.setFactura(fi);
						fi.addReserva(ri);
						fi.actualizarPrecioSUMAR(ri);
						
//						em.merge(ri);
//						em.merge(fi);
				}
				//Actualizar parametros de reserva
				ri.setSocio(socio);
				socio.addReserva(ri);
				ri.actualizarFecha();
				ri.setEstado(estadoReserva.HECHA);
				sesion.setCantidadParticipantes(sesion.getCantidadParticipantes() + 1);
//				em.merge(ri);
//				em.merge(socio);
//				em.merge(sesion);
				
				em.getTransaction().commit();
				return ri.getIdReserva();
			} else {
				//Añadir lista a sesion
				sesion.addListaDeEspera(socio);
				socio.addSesion(sesion);
				
//				em.merge(socio);
//				em.merge(sesion);
				
				em.getTransaction().commit();
				return -1; //La sesion esta llena, se te ha añadido a la lista de espera
			}
		} else {
			em.getTransaction().commit();
			return -2; //Numero maximo de reservas alcanzado
		}
	}

	public boolean pagarFactura(String metodo, int numSocio, int codigoFactura) {
		em.getTransaction().begin();
		
		Socio socio = em.find(Socio.class, numSocio);
		
		int datos = socio.getDatosMetodoPago(metodo);
		
		boolean respuesta;
		Factura factura = em.find(Factura.class, codigoFactura);	
		if (API_Banco.getInstance().pagar(datos, factura.getPrecio())) {
			factura.setEstado1(estadoFactura1.PAGADA);
//			em.merge(factura);
			respuesta = true;
		} else {
			respuesta = false;
		}
		
		em.getTransaction().commit();
		return respuesta;
	}

	public boolean addActividad2(String nombreActividad, int gradoExigencia, int precio) {
		em.getTransaction().begin();
		boolean existe = false;

	    try {
	        TypedQuery<Actividad> query = em.createQuery(
	            "SELECT a FROM Actividad a WHERE a.nombre=?1 AND a.gradoExigencia=?2",
	            Actividad.class
	        );
	        query.setParameter(1, nombreActividad);
	        query.setParameter(2, gradoExigencia);
	        
	        query.getResultList().get(0); // Si llega aquí, existe
	        existe = true;
	    } catch (Exception e) {
	        existe = false; // No se encontró ninguna actividad
	    }
	    
		if (!existe) {
			try {
				Actividad actividad = new Actividad(nombreActividad, gradoExigencia, precio);
				em.persist(actividad);
				em.getTransaction().commit();
			} catch (gradoExigenciaException e) { //Aqui no se llega nunca porque cubro el caso en el GUI
				// TODO Auto-generated catch block
				e.printStackTrace();
				em.getTransaction().rollback();
			}
		}else {
			em.getTransaction().commit();
		}
		return existe;
	}

	public int addSesionSala2(String nombreSala, String nombreActividad, int gradoExigencia, LocalDate fecha,
			LocalTime horaIni, LocalTime horaFin) {
		em.getTransaction().begin();
		
		Sala sala = em.find(Sala.class, nombreSala);
		
		boolean llena = sala.isSalaLlena(fecha);
		if (llena) {
			em.getTransaction().commit();
			return -1;//Llena
		} else {
			boolean ocupada = false;
			List<Sesion> list = sala.getListaSesiones();
			for (Sesion sesion : list) {
				if (sesion.getFecha().equals(Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant())) 
						&& sesion.getHoraInicio().equals(Date.from(horaIni.atDate(LocalDate.of(1970, 1, 1)).atZone(ZoneId.systemDefault()).toInstant()))) ocupada = true;
			}
			
			if (ocupada) {
				em.getTransaction().commit();
				return -2;//Ocupada
			} else {
				TypedQuery<Actividad> query = em.createQuery(
						"SELECT a FROM Actividad a WHERE a.nombre=?1 AND a.gradoExigencia=?2"
						, Actividad.class
						);
				query.setParameter(1, nombreActividad);
				query.setParameter(2, gradoExigencia);
				Actividad actividad = query.getResultList().get(0);
				
				Sesion sesion = new Sesion(sala, actividad, fecha, horaIni, horaFin);
				
				sala.addSesion(sesion);
				actividad.addSesion(sesion);
				
				em.persist(sesion);
//				em.merge(sala);
//				em.merge(actividad);
				em.getTransaction().commit();
				return 0;
			}
		}
	}

	public boolean enviarFacturas() {
		em.getTransaction().begin();
		
		LocalDate hoy = LocalDate.now();
		if(hoy.getDayOfWeek() == DayOfWeek.MONDAY) {//Es lunes
			//GetSocios
			TypedQuery<Socio> query = em.createQuery(
					"SELECT s FROM Socio s"
					, Socio.class	);
			List<Socio> listaSocios = query.getResultList();
			
			for (Socio socio : listaSocios) {
				//getFactura No visible
				TypedQuery<Factura> query2 = em.createQuery(
						"SELECT f FROM Factura f WHERE f.socio.numSocio=?1 AND f.estado2=?2"
						, Factura.class
							);
					query2.setParameter(1, socio.getNumSocio());
					query2.setParameter(2, estadoFactura2.NO_VISIBLE);
					Factura fi = query2.getResultList().get(0);
					
				if (fi.getPrecio() > 0) {
					//Set estado2 Visible
					fi.setEstado2(estadoFactura2.VISIBLE);
//					em.merge(fi);
					
					//Actualizar fecha
					LocalDate localDate = LocalDate.now();
					fi.setFechaDeEmision(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//					em.merge(fi);
					
					//
					Factura factura = new Factura(socio);	
					socio.addFactura(factura);
					
//					em.merge(socio);
					em.persist(factura);
					
				}else {//Aprovechar la factura creada si no tiene que pagar nada
					fi.getListaReservas().clear();
				}
				socio.getListaReservas().clear();
				socio.setNumeroReservasHechas(0);
			}
			em.getTransaction().commit();
			return true;
		}else {
			em.getTransaction().commit();
			return false;
		}
	}

	public void cancelarReserva(int idReserva, int numSocio) {
		em.getTransaction().begin();
		
		Socio socio = em.find(Socio.class, numSocio);
		
		Reserva reserva = em.find(Reserva.class, idReserva);
		
		TypedQuery<Factura> query = em.createQuery(
				"SELECT f FROM Factura f WHERE f.socio.numSocio=?1 AND f.estado2=?2"
				, Factura.class
					);
			query.setParameter(1, socio.getNumSocio());
			query.setParameter(2, estadoFactura2.NO_VISIBLE);
		Factura factura = query.getResultList().get(0);
		
		//Cancelar la reserva
		reserva.setEstado(estadoReserva.NO_HECHA);
		reserva.removeSocio();
		socio.removeReserva(reserva);
		socio.actualizarNumeroReservasMenos1();
		
		if(factura.contieneReserva(reserva)) {
			reserva.removeFactura();
			
			factura.removeReserva(reserva);
			factura.actualizarPrecioRESTAR(reserva);			
		}
		
//		em.merge(reserva);
//		em.merge(factura);
//		em.merge(socio);
		
		//Get sesion de la reserva
//		TypedQuery<Sesion> query2 = em.createQuery(
//				//"SELECT s FROM Sesion s WHERE s.listaReservas.idReserva=?1"
//				"SELECT s FROM Sesion s JOIN s.listaReservas r WHERE r.idReserva=?1",
//				Sesion.class	
//				);
//			query2.setParameter(1, reserva.getIdReserva());
//		Sesion sesion = query2.getResultList().get(0);
		
		//Hay gente en la lista de espera??
		Sesion sesion = reserva.getSesion();
		
		boolean vacia = sesion.isListaEsperaEmpty();
		if(!vacia) {
			//Conseguir el primer socio de la listaEspera y borrarle al socio que esta en esa listaEspera
			Socio soc1 = sesion.getAndRemoveFirstListaEspera();
			soc1.removeSesion(sesion);
			
			//Sumar la reserva al socio recogido
			int n = soc1.actualizarNumeroReservasMas1();
//			em.merge(soc1);
			
			TypedQuery<Factura> query3 = em.createQuery(
					"SELECT f FROM Factura f WHERE f.socio.numSocio=?1 AND f.estado2=?2"
					, Factura.class
						);
				query3.setParameter(1, soc1.getNumSocio());
				query3.setParameter(2, estadoFactura2.NO_VISIBLE);
			Factura fi = query3.getResultList().get(0);
			if (n > 4) {
				reserva.setFactura(fi);
				
				fi.addReserva(reserva);
				fi.actualizarPrecioSUMAR(reserva);
				
//				em.merge(reserva);
//				em.merge(fi);
			} 
			reserva.setSocio(soc1);
			soc1.addReserva(reserva);
			
			reserva.actualizarFecha();
			reserva.setEstado(estadoReserva.HECHA);
			
			sesion.setCantidadParticipantes(sesion.getCantidadParticipantes() + 1);
			
//			em.merge(reserva);
//			em.merge(socio);
//			em.merge(sesion);
		}	
		em.getTransaction().commit();
	}

	public List<Integer> getNumeroSocios() {
		TypedQuery<Socio> query = em.createQuery(
				"SELECT DISTINCT s FROM Socio s"
				, Socio.class
				);
		List<Socio> socios = query.getResultList();
		List<Integer> respuesta = new LinkedList<Integer>();
		for (Socio socio : socios) {
			respuesta.add(socio.getNumSocio());
		}
		return (List<Integer>) respuesta;
	}

	public boolean existeSala(String text) {
		try {
			TypedQuery<Sala> query = em.createQuery(
					"SELECT s FROM Sala s WHERE s.nombre=?1"
					, Sala.class
					);
				query.setParameter(1, text);
		        query.getResultList().get(0); // Si llega aquí, existe
		        return true;
	    } catch (Exception e) {
	    	return false; // No se encontró ninguna sala
	    }
	}

	public boolean tieneReservaSocio(int numSocio, int idReserva) {
		try {
			TypedQuery<Reserva> query = em.createQuery(
					"SELECT R FROM Reserva r WHERE r.socio.numSocio=?1"
					, Reserva.class
					);
				query.setParameter(1, numSocio);
			List<Reserva> list = query.getResultList();
			for (Reserva reserva : list) {
				if (reserva.getIdReserva() == idReserva) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {//Vacia
			return false;
		}

	}
}
