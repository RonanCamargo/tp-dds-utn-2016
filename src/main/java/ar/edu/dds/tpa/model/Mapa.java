package ar.edu.dds.tpa.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Mapa {
	private List<PuntoDeInteres> puntosDeInteres;

	public Mapa() {
		puntosDeInteres = new ArrayList<>();
	}

	public void agregarPuntoDeInteres(PuntoDeInteres unPuntoDeInteres) {
		puntosDeInteres.add(unPuntoDeInteres);
	}
	
	public void sacarPuntoDeInteres(PuntoDeInteres unPuntoDeInteres) {
		puntosDeInteres.remove(unPuntoDeInteres);
	}
	
	public void modificarPuntoDeInteres(PuntoDeInteres unPuntoDeInteres, PuntoDeInteres puntoDeInteresModificado) {
		puntosDeInteres.remove(unPuntoDeInteres);
		puntosDeInteres.add(puntoDeInteresModificado);
	}

	public List<PuntoDeInteres> buscarPorTextoLibre(String unaFrase) {
		List<String> palabrasClave = Arrays.asList(unaFrase.split(" "));

		return puntosDeInteres.stream()
				.filter(elem -> palabrasClave.stream().anyMatch(palabra -> elem.condicionDeBusqueda(palabra)))
				.collect(Collectors.toList());
	}
}