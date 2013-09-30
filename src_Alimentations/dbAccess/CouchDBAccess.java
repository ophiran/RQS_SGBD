package dbAccess;
import java.sql.ResultSet;


public interface CouchDBAccess {

	void connect(String ip,String port);
	void bindDb(String db);
	void close();
	
	ResultSet sendQuery(String query);
}
