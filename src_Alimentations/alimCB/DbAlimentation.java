package alimCB;

import java.util.Map;

import setupCI.ApplicationInfo;
import dbAccess.CouchDBAccess;

public class DbAlimentation {

	public static void main(String[] args) {
		ApplicationInfo dbInfo = ApplicationInfo.getInstance();
		CouchDBAccess db = new CouchDBAccess();
		db.connect(dbInfo.getIp(), dbInfo.getPort(), dbInfo.getDbName());
		
		MovieDocument movie = new MovieDocument(db.getDocument(47933));
		System.out.println(movie);
		movie.diplayImage();
	}

}
