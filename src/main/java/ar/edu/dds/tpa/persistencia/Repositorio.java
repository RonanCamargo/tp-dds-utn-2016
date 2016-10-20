package ar.edu.dds.tpa.persistencia;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Repositorio {

	private static SessionFactory sessionFactory;
	private static Session sesion;

	public Repositorio() {
		sessionFactory = new Configuration().configure("Hibernate.xml").buildSessionFactory();
	}

	public Repositorio(String rutaDelArchivoDeConfiguracionDeHibernate) {
		sessionFactory = new Configuration().configure(rutaDelArchivoDeConfiguracionDeHibernate).buildSessionFactory();
	}

	public static Session obtenerSesion() {
		if (sesion == null || !sesion.isOpen()) {
			sesion = sessionFactory.openSession();
		}
		return sesion;
	}
	
	public void cerrarSesion() {
		sesion.close();
	}

	public void persistir(Object unObjeto) {
		obtenerSesion().saveOrUpdate(unObjeto);
	}

	public void eliminar(Object unObjeto) {
		obtenerSesion().delete(unObjeto);
	}

	public <T> T buscarPorID(Class<T> clase, int id) {
		return (T)obtenerSesion().find(clase, id);
	}

	public <T> List<T> traerTodos(Class<T> clase) {
		@SuppressWarnings("unchecked")
		List<T> objetosEncontrados = obtenerSesion().createQuery("from " + clase.getName()).getResultList();
		return objetosEncontrados;
	}
}