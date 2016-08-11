package flightSearch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDConexion {
	
	private Connection m_conexion;	
	public String user;
    public String password;
    public String urlbbdd;
	
	public BDConexion() throws ClassNotFoundException{
		this.urlbbdd = "jdbc:postgresql://127.0.0.1:5432/Fly";
		this.user = "postgres";
		this.password = "adminadmin";
		Class.forName("org.postgresql.Driver");
		conectar();
	}
	private void conectar(){
		try {
			m_conexion = DriverManager.getConnection(urlbbdd, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Connection getConexion() {
        return m_conexion;
    }
}
