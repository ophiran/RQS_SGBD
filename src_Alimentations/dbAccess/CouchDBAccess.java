package DBAccess;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

public class CouchDBAccess implements DBAccess {

	Logger log;
	HttpClient httpClient;
	CouchDbInstance dbInstance;
	CouchDbConnector db;
	
	public CouchDBAccess() {
		log = Logger.getLogger("CouchDBAccess Logger");
	}
	
	@Override
	public void connect(String ip, String port) {
		httpClient = new StdHttpClient.Builder().host(ip).port(Integer.parseInt(port)).build();
		dbInstance = new StdCouchDbInstance(httpClient);
		log.log(Level.INFO, "Connected to "  + ip + ":" + port);
	}

	@Override
	public void bindDb(String dbName) {
		if(dbInstance != null){
			db = new StdCouchDbConnector(dbName, dbInstance);
			log.log(Level.INFO, "Binded to db " + dbName);
		}
		else {
			log.log(Level.WARNING, "Binding to a non-connected DB");
		}
	}

	@Override
	public void close() {
		if(httpClient != null) {
			httpClient.shutdown();
			log.log(Level.INFO,"Closed connection");
		}
		else {
			log.log(Level.WARNING, "Trying to close an inexistant connection");
		}
	}

	@Override
	public ViewResult sendQuery(String docId, String viewName) {
		if(db != null) {
	        ViewQuery query = new ViewQuery().designDocId(docId).viewName(viewName);
			return db.queryView(query);
		}
		else {
			return null;
		}
	}

	@Override
	public ViewResult sendQuery(String query) {
		return null;
	}

}
