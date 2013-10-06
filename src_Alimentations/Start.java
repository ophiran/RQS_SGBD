import dbAccess.DBAccess;
import dbAccess.CouchDBAccess;


public class Start {

	public static void main(String[] args) {
		//utilisation de l'argument -console pour passer en mode console?
		DBAccess test = new CouchDBAccess();
		test.connect("127.0.0.1", "5984");
		test.bindDb("movies");
		
	}

}
