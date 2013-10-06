package dbAccess;

import java.sql.ResultSet;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

public class CouchDBAccessImpl implements CouchDBAccess {

	HttpClient httpClient;
	CouchDbInstance dbInstance;
	CouchDbConnector db;
	
	@Override
	public void connect(String ip, String port) {
		httpClient = new StdHttpClient.Builder().host("127.0.0.1").port(5984).build();
		dbInstance = new StdCouchDbInstance(httpClient);
	}

	@Override
	public void bindDb(String dbName) {
		db = new StdCouchDbConnector(dbName, dbInstance);
	}

	@Override
	public void close() {
		httpClient.shutdown();
	}

	@Override
	public ResultSet sendQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}

}
