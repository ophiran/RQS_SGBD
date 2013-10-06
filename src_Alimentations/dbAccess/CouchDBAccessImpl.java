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

public class CouchDBAccessImpl implements CouchDBAccess {

	Logger log;
	HttpClient httpClient;
	CouchDbInstance dbInstance;
	CouchDbConnector db;
	
	public CouchDBAccessImpl() {
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
		db = new StdCouchDbConnector(dbName, dbInstance);
		log.log(Level.INFO, "Binded to db " + dbName);
	}

	@Override
	public void close() {
		httpClient.shutdown();
	}

	@Override
	public ViewResult sendQuery(String docId, String viewName) {
        ViewQuery query = new ViewQuery().designDocId(docId).viewName(viewName);
		return db.queryView(query);
	}

	@Override
	public ViewResult sendQuery(String query) {
		return null;
	}

}
