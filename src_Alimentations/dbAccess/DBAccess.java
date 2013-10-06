package DBAccess;
import org.ektorp.ViewResult;


public interface DBAccess {

	void connect(String ip,String port);
	void bindDb(String dbName);
	void close();
	
	ViewResult sendQuery(String docId,String viewName);
	ViewResult sendQuery(String query);
	
	//void createIndex(String table,String column,Class MapType);
}
