package dbAccess;
import java.sql.ResultSet;


public interface CouchDBAccess {

	void connect(String ip,String port);
	void bindDb(String dbName);
	void close();
	
	ResultSet sendQuery(String query);
	
	//void createIndex(String table,String column,Class MapType);
}
