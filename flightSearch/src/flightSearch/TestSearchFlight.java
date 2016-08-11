package flightSearch;

import org.junit.Test;

import flightSearch.Search;

public class TestSearchFlight {
	
	@Test
	public void BusquedaVuelosNoAirportOrigen() {
		System.out.println("\n**MAD -> VALL (No Airport Origen) ***"); 
		Search search = new Search();
		search.setAdultos("1");
		search.setNinios("0");
		search.setInfants("0");
		search.setAirpotOring("Valladolid");
		search.setAirpotDestino("Madrid");		
		search.setFecha("16/09/2016");		
		SearchFlight SearchFlight = new SearchFlight();
		SearchFlight.buscar(search);
	}
	
	@Test
	public void BusquedaVuelosNoAirportDest() {
		System.out.println("\n**MAD -> VALL (No Airport Destino) ***"); 
		Search search = new Search();
		search.setAdultos("1");
		search.setNinios("0");
		search.setInfants("0");
		search.setAirpotOring("Madrid");
		search.setAirpotDestino("Valladolid");		
		search.setFecha("16/09/2016");		
		SearchFlight SearchFlight = new SearchFlight();
		SearchFlight.buscar(search);
	}
	
	@Test
	public void BusquedaVuelosAMS_FRA() {
		System.out.println("\n**AMS -> FRA ***"); 
		Search search = new Search();
		search.setAdultos("1");
		search.setNinios("0");
		search.setInfants("0");
		search.setAirpotOring("Amsterdam");
		search.setAirpotDestino("Frakfurt");		
		search.setFecha("16/09/2016");		
		SearchFlight SearchFlight = new SearchFlight();
		SearchFlight.buscar(search);
	}
	
	@Test
	public void BusquedaVuelosLHR_IST() {
		
		System.out.println("\n***** LHR -> IST *****"); 
		Search search = new Search();
		search.setAdultos("2");
		search.setNinios("1");
		search.setInfants("1");		
		search.setAirpotOring("London");
		search.setAirpotDestino("Istambul");
		search.setFecha("26/08/2016");		
		SearchFlight SearchFlight = new SearchFlight();
		SearchFlight.buscar(search);
	}
	
	@Test
	public void BusquedaVuelosBCN_MAD () {
		
		System.out.println("\n***** BCN -> MAD *****"); 
		Search search = new Search();
		search.setAdultos("1");
		search.setNinios("2");
		search.setInfants("0");		
		search.setAirpotOring("Barcelona");
		search.setAirpotDestino("Madrid");
		search.setFecha("12/08/2016");		
		SearchFlight SearchFlight = new SearchFlight();
		SearchFlight.buscar(search);
	}
	
	@Test
	public void BusquedaVuelosCDG_FRA() {
		
		System.out.println("\n***** CDG -> FRA *****"); 
		Search search = new Search();
		search.setAdultos("1");
		search.setNinios("1");
		search.setInfants("0");		
		search.setAirpotOring("Paris");
		search.setAirpotDestino("Frakfurt");
		search.setFecha("12/08/2016");		
		SearchFlight SearchFlight = new SearchFlight();
		SearchFlight.buscar(search);
	}	
}
