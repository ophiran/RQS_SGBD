package dbAccess;
import org.ektorp.ViewResult;


public interface CouchDBAccess {

	void connect(String ip,String port);
	void bindDb(String dbName);
	void close();
	
	ViewResult sendQuery(String query);
	
	//void createIndex(String table,String column,Class MapType);
}
