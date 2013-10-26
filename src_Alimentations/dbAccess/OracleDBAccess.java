package dbAccess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import alimCB.MovieDocument;
import oracle.jdbc.OracleDriver;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

public class OracleDBAccess {
	
	private Connection connection;
	private Map typeMap;
	
	
	public OracleDBAccess() throws SQLException {
		DriverManager.registerDriver(new OracleDriver());
	}
	
	public synchronized void startConnection(String dbPath,String login,String password) throws SQLException{
		connection = DriverManager.getConnection("jdbc:oracle:thin:"+dbPath, login, password);
		if(typeMap == null){
			typeMap = connection.getTypeMap();
			typeMap.put("MOVIE_T",MovieDocument.class);
			connection.setTypeMap(typeMap);
		}
	}
	
	public synchronized void stopConnection() throws SQLException{
		if(connection!=null)
			connection.close();
	}
	
	public Connection getConnection(){
		return connection;
	}
	
	
	public synchronized void sendObject(String type,List<Object> object) throws SQLException{
		sendObject(type, object.toArray());
	}
	
	public synchronized void sendObject(String type,Collection<Object> object) throws SQLException{
		sendObject(type,object.toArray());
	}
	
	public synchronized void sendObject(String type,Object[] object) throws SQLException{
		ArrayDescriptor aDesc = ArrayDescriptor.createDescriptor(type, getConnection());
		
		CallableStatement statement = connection.prepareCall("{call addMovies(?)}");
		statement.setArray(1, connection.createArrayOf(type, object));
		statement.execute();
		statement.close();
		connection.commit();
	}
	
	@Override
	protected void finalize() throws Throwable {
		stopConnection();
	}
}
