package dbAccess;

import java.sql.ResultSet;

import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;

public class CouchDBAccessImpl implements CouchDBAccess {

	HttpClient httpClient;
	
	@Override
	public void connect(String ip, String port) {
		httpClient = new StdHttpClient.Builder().host("127.0.0.1").port(5984).build();
	}

	@Override
	public void bindDb(String db) {
		httpClient.shutdown();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public ResultSet sendQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}

}
