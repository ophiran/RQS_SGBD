import setupCI.IndexCreator;
import dbAccess.DBAccess;
import dbAccess.CouchDBAccess;


public class Start {

	public static void main(String[] args) {
		IndexCreator.createIndexes();
		
	}

}
