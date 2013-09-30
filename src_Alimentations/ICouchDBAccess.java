import java.sql.ResultSet;


public interface ICouchDBAccess {

	void connect(String ip,String port);
	void bindDb(String db);
	void close();
	
	ResultSet sendQuery(String query);
}
