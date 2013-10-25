package dbAccess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import oracle.jdbc.OracleDriver;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

public class OracleDBAccess {
	
	private Connection connection;
	
	public OracleDBAccess() throws SQLException {
		DriverManager.registerDriver(new OracleDriver());
	}
	
	public synchronized void startConnection(String dbPath,String login,String password) throws SQLException{
		connection = DriverManager.getConnection("jdbc:oracle:thin:"+dbPath, login, password);
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
		
		CallableStatement statement = connection.prepareCall("{call test_array(?)}");
		statement.setArray(1, new ARRAY(aDesc, connection, object));
		statement.execute();
		statement.close();
		connection.commit();
	}
}
