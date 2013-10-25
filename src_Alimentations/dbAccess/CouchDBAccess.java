package dbAccess;

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

public class CouchDBAccess{

	private Logger log;
	private HttpClient httpClient;
	private CouchDbInstance dbInstance;
	private CouchDbConnector db;
	
	public CouchDBAccess() {
		log = Logger.getLogger("CouchDBAccess Logger");
	}
	
	public void connect(String ip, String port, String dbName) {
		httpClient = new StdHttpClient.Builder().host(ip).port(Integer.parseInt(port)).build();
		dbInstance = new StdCouchDbInstance(httpClient);
		log.log(Level.INFO, "Connected to "  + ip + ":" + port);
		db = new StdCouchDbConnector(dbName, dbInstance);
		log.log(Level.INFO, "Binded to db " + dbName);
	}


	public void close() {
		if(httpClient != null) {
			httpClient.shutdown();
			log.log(Level.INFO,"Closed connection");
		}
		else {
			log.log(Level.WARNING, "Trying to close an inexistant connection");
		}
	}

	public ViewResult sendQuery(String docId, String viewName) {
		if(db != null) {
	        ViewQuery query = new ViewQuery().designDocId(docId).viewName(viewName);
			return db.queryView(query);
		}
		else {
			return null;
		}
	}

	public ViewResult sendQueryKey(String docId,String key) {
		if(db != null){
			ViewQuery query = new ViewQuery().allDocs().key(key);
			return db.queryView(query);
		}
		else {
			return null;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
	}

}
