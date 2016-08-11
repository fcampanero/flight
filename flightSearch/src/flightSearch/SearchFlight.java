package flightSearch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import flightSearch.PriceInfants;
import flightSearch.Search;
import flightSearch.Vuelo;

public class SearchFlight {	
	
	private String error = null;
	
	public void buscar(Search search) {
		
		PriceInfants[]  listaPrecioInfantes = null;
		if (Integer.valueOf(search.getInfants())>0){
			listaPrecioInfantes = Procesos.cargarPreciosInfantes();
		}
		
		System.out.println("Consulta: Origen: " 		+  search.getAirpotOring() + 
				                    " Destino: " 		+ search.getAirpotDestino() + 
				                    " Adultos: " 		+ search.getAdultos() +
				                    " Niños: " 			+ search.getNinios() + 
				                    " Infantes: " 		+ search.getInfants() + 
				                    " Fecha Salida: " 	+ search.getFecha());

		String codIataOrigen = buscarIATA(search.getAirpotOring());
		if (null == codIataOrigen){
			error = "ERROR en el aeropuerto Origen enviado: " + search.getAirpotOring();
			System.out.println(error);
			return;
		}else{
			search.setAirpotOring(codIataOrigen);
		}
		
		String codIataDestino = buscarIATA(search.getAirpotDestino());
		if (null == codIataDestino){
			error = "ERROR en el aeropuerto Destino enviado: " + search.getAirpotDestino();
			System.out.println(error);
			return;
		}else{
			search.setAirpotDestino(codIataDestino);
		}
		
		int numDias = 0;
		try {
			GregorianCalendar fecha = new GregorianCalendar();
			GregorianCalendar fechaHoy = new GregorianCalendar();
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
			
			long fechaMs = 0;
			long fechaHoyMs = 0;
			fecha.setTime(sdf2.parse(search.getFecha()));
			fechaMs = fecha.getTimeInMillis();
			fechaHoyMs = fechaHoy.getTimeInMillis();
			numDias = Integer.valueOf((int) Math.floor((fechaMs - fechaHoyMs) / (1000 * 60 * 60 * 24)));
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (numDias < 0){
			error = "ERROR en la fecha indicada: " + search.getFecha();
			System.out.println(error);
			return;
		}
		
		
		Vuelo[]  listaVuelos = Procesos.recuperarVuelos(search, numDias, listaPrecioInfantes);
		if (listaVuelos != null) {
			for ( Vuelo item : listaVuelos ){	
				System.out.println("Vuelo-->  IATA: " + item.getIATA() + "; Price " + item.getTotalPrice());	
			}
		}else{
			error = "No flights available";
			System.out.println(error);
		}
				
	}



	public String buscarIATA(String COD_IATA) {
		COD_IATA = Procesos.recuperarCOD_IATA(COD_IATA);
		return COD_IATA;
		
	}

		
}
