package flightSearch;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import flightSearch.PriceInfants;
import flightSearch.Search;
import flightSearch.Vuelo;

public class Procesos {
	
	public static Vuelo[] recuperarVuelos(Search search, int numDias, PriceInfants[]  listaPrecioInfantes) {
		
		Vuelo[] vuelosRecuperados =  null;

		Connection con = null;
	    Statement st = null;
	    ResultSet resultado = null;		
		try {
			
			BDConexion dataBase = new BDConexion();
			con = dataBase.getConexion();
			st = con.createStatement();
			resultado  = null;
			String sql = null;			
			
			sql = "SELECT * FROM \"FLIGHTS\".\"FLIGHTS\" WHERE \"ORIGIN\" =  \'"+search.getAirpotOring()+"\' AND \"DESTINATION\" =  \'"+search.getAirpotDestino()+"\'";
	       
			resultado = st.executeQuery(sql);
			ArrayList<Vuelo> arrayVuelos = new ArrayList<Vuelo>();
	        while(resultado.next()) {
	        	 
	        	 Double precioBase = resultado.getDouble("BASE_PRICE");	     		 
	     		 Double precioInfanteDia = 0.0;
	     		 Double totalTrayecto = 0.0;
	     		 Double totalAdultos = 0.0;
	     		 Double totalNinios = 0.0;
	     		 Double totalInfantes = 0.0;
	     		 Double porcentaje = null;
	     		 
	     		 float porc; 
	     		 // Tarifas ADULTOS
	     		 if (numDias > 30){                        // 80 %  //197.0 * 80% = 157.6 	     			
	     			porcentaje = 0.80;  
	     		    porc = Math.abs((float) (precioBase-(precioBase * porcentaje))); 
	     		    totalTrayecto =  precioBase - porc; 	     		    
	     		 }else if (numDias >= 16 && numDias <= 30){  // 100%
	     			totalTrayecto = precioBase;	     		 
	     		 }else if (numDias >= 3 && numDias <= 15){   // 120%
	     			porcentaje = 1.20;  
	     		    porc = Math.abs((float) (precioBase-(precioBase * porcentaje))); 
	     		    totalTrayecto =  precioBase + porc;  
	     		    
		         }else{   // menor que 3 días                // 150%    
		        	 porcentaje = 1.50;  
		     		 porc = Math.abs((float) (precioBase-(precioBase * porcentaje))); 
		     		 totalTrayecto =  precioBase + porc;  
		         }
	     		 
	     		 totalAdultos  =  Double.valueOf(search.getAdultos())* totalTrayecto;
	     		 // Tarifas NIÑOS (33%)
	     		 
	     		 porcentaje= 0.33; //(33%) 
	     		 porc = Math.abs((float) (totalTrayecto - (totalTrayecto * porcentaje)));
	     		 totalNinios =   Double.valueOf(search.getNinios()) * porc;
	     		 
	     		 // Tarifas INFANTS -->Precio de la Aerolinea
	     		 // Precio de la Aerolinea
	     		 if (listaPrecioInfantes != null ) {
	     			for ( PriceInfants item : listaPrecioInfantes ){
	    				if (item.getIata().substring(0, 2).equals(resultado.getString("AIRLINE").substring(0, 2))){
	    					precioInfanteDia = item.getPrice();
	    				}
	    			}
	     		 }
	     		
	     		 totalInfantes = precioInfanteDia * Double.valueOf(search.getInfants());
	     		
	     		 Vuelo vuelo = new Vuelo();
	     		 vuelo.setIATA(resultado.getString("AIRLINE"));	    
	     		 vuelo.setTotalPrice(String.valueOf(Math.rint((totalAdultos + totalNinios + totalInfantes)*100)/100).concat(" €"));
	     		
	     		 
	     		// vuelo.setTotalPrice(Math.rint((totalAdultos + totalNinios + totalInfantes)*100)/100);
	     		 arrayVuelos.add(vuelo);
	        }
	        if (arrayVuelos.size()>0){
		        vuelosRecuperados = new Vuelo[arrayVuelos.size()];
				vuelosRecuperados = arrayVuelos.toArray(vuelosRecuperados);
	        }
			resultado.close();
	        st.close();
	        con.close();
	        
		} catch(Exception e) {
			
			System.out.println(e.getMessage());
	        
		}  finally {
	        try {
	            if (resultado != null) {
	            	resultado.close();
	            }
	            if (st != null) {
	                st.close();
	            }
	            if (con != null) {
	                con.close();
	            }
	        } catch (SQLException ex) {
	        	System.out.println(ex.getMessage());
	        }
	    } 
		
		return vuelosRecuperados;
	}
	
	public static String recuperarCOD_IATA(String airpot) {
		
		Connection con = null;
	    Statement st = null;
	    ResultSet resultado = null;
	    String COD_IATA = null;
		
		try {
			
			String bbdd = "jdbc:postgresql://127.0.0.1:5432/Fly";
			String user = "postgres";
			String password = "adminadmin";
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(bbdd, user, password );
			st = con.createStatement();
			
			String sql = null;
			
			// Búscamos el IATA del Aeropueto Origen
			sql = "SELECT * FROM \"FLIGHTS\".\"AIRPORTS\" WHERE \"CITY\" =  \'"+airpot+"\'";
			
			resultado = st.executeQuery(sql);
			while(resultado.next()) {
				COD_IATA = resultado.getString("IATA");
			}
			
			resultado.close();
	        st.close();
	        con.close();
		} catch(Exception e) {
			System.out.println(e.getMessage());
	        
		}  finally {
	        try {
	            if (resultado != null) {
	            	resultado.close();
	            }
	            if (st != null) {
	                st.close();
	            }
	            if (con != null) {
	                con.close();
	            }
	        } catch (SQLException ex) {
	        	System.out.println(ex.getMessage());
	        }
	    } 
		
		
		return COD_IATA;
	}

	public static PriceInfants[] cargarPreciosInfantes() {
		
		PriceInfants[] listaPreciosInfants =  null;

		Connection con = null;
	    Statement st = null;
	    ResultSet resultado = null;
		
		try {			
			String bbdd = "jdbc:postgresql://127.0.0.1:5432/Fly";
			String user = "postgres";
			String password = "adminadmin";
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(bbdd, user, password );
			st = con.createStatement();
			resultado  = null;
			String sql = null;
						
			sql = "SELECT * FROM \"FLIGHTS\".\"AIR_LINES_PRICE_INFANT\" ";
			resultado = st.executeQuery(sql);
			ArrayList<PriceInfants> arrayPrice = new ArrayList<PriceInfants>();
	        while(resultado.next()) {
	        	 
	        	PriceInfants precios = new PriceInfants();
	        	precios.setIata(resultado.getString("IATA_CODE"));
	        	precios.setName(resultado.getString("NAME"));
	        	precios.setPrice(resultado.getDouble("PRICE"));
	        	
	        	arrayPrice.add(precios);
	        }
	        
	        listaPreciosInfants = new PriceInfants[arrayPrice.size()];
	        listaPreciosInfants = arrayPrice.toArray(listaPreciosInfants);
	        
	        resultado.close();
	        st.close();
	        con.close();
	        
		} catch(Exception e) {
			
			System.out.println(e.getMessage());
	        
		}  finally {
	        try {
	            if (resultado != null) {
	            	resultado.close();
	            }
	            if (st != null) {
	                st.close();
	            }
	            if (con != null) {
	                con.close();
	            }
	        } catch (SQLException ex) {
	        	System.out.println(ex.getMessage());
	        }
	    } 
		
		return listaPreciosInfants;		
	}
	
	
}
