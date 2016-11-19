package ar.edu.dds.tpa.controller;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import ar.edu.dds.tpa.geolocalizacion.Posicion;
import ar.edu.dds.tpa.model.Banco;
import ar.edu.dds.tpa.model.CGP;
import ar.edu.dds.tpa.model.LocalComercial;
import ar.edu.dds.tpa.model.ParadaDeColectivo;
import ar.edu.dds.tpa.model.PuntoDeInteres;
import ar.edu.dds.tpa.persistencia.Mapa;
import ar.edu.dds.tpa.persistencia.MapaEnBaseDeDatos;
import ar.edu.dds.tpa.persistencia.Persistible;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

public class AdministracionDePOIController implements Route, Persistible {
	
	private Map<String, Class<? extends PuntoDeInteres>> tiposPOI = ImmutableMap.of("Parada de colectivo", ParadaDeColectivo.class, 
														  "Local comercial", LocalComercial.class,
														  "Banco", Banco.class,
														  "CGP", CGP.class);
	
	@Override
	public Object handle(Request arg0, Response arg1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ModelAndView buscar(Request request, Response response){
		Map<String, Object> model = new HashMap<>();
		model.put("tipo", tiposPOI.keySet());
		
		String textoBuscado = request.queryMap("textoBuscado").value();
		
		model.put("textoBuscado", textoBuscado);
		
		String tipoBuscado = request.queryMap("tipoPOI").value();
		model.put("tipoBuscado", tipoBuscado);
		//model.put("poi", new Buscador(new Mapa()).buscar(textoBuscado, new Usuario()));
		model.put("poi", new MapaEnBaseDeDatos()
							.obtenerPuntosDeInteres()
							.stream()
							.filter(poi -> poi.getClass() == tiposPOI.get(tipoBuscado))
							.filter(poi -> poi.getNombre().contains(textoBuscado))
							.toArray());
	
		return new ModelAndView(model, "administracionPOI/consultarPOI.hbs");
	}

	public ModelAndView formularioAlta(Request request, Response response){
		return new ModelAndView(null, "administracionPOI/altaPOI.hbs");
	}
	
	public ModelAndView alta(Request request, Response response){
		Map<String, Object> model  = new HashMap<>();
		String nombre = request.queryMap("nombre").value();
		Double longitud = Double.valueOf(request.queryMap("longitud").value());
		Double latitud = Double.valueOf(request.queryMap("latitud").value());
		Posicion coordenadas = new Posicion(longitud, latitud);
		String id = request.queryMap("tipoPOIs").value();
		PuntoDeInteres poi=null;
		switch (id) {

	    case "0":
	    	poi = new ParadaDeColectivo(nombre, coordenadas,null);	break;
		case "1":
			poi = new LocalComercial(nombre, coordenadas,null,null);	break;
		case "2":
			poi = new Banco(nombre, coordenadas,null);   break;
		case "3":
			poi = new CGP(nombre, coordenadas,null);	break;

		}

		new MapaEnBaseDeDatos().agregar(poi);
		//repositorio.persistir(poi);
		return new ModelAndView(null, "administracionPOI/altaPOI.hbs");
	}
	
	public ModelAndView presentarEdicion(Request request, Response response){
		return null;
	}
	
	public ModelAndView editar(Request request, Response response){
		return null;
	}
	
	public ModelAndView eliminar(Request request, Response response){
		Mapa mapa = new MapaEnBaseDeDatos();
		PuntoDeInteres p = mapa.obtenerPuntosDeInteres()
			.stream()
			.filter(poi -> poi.getId() == request.queryMap("id").integerValue())
			.findFirst()
			.get();
		mapa.sacar(p);
		response.redirect("/administracion/consultar");
		return null;
	}
}