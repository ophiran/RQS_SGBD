import dbAccess.CouchDBAccess;
import dbAccess.CouchDBAccessImpl;


public class Start {

	public static void main(String[] args) {
		//utilisation de l'argument -console pour passer en mode console?
		CouchDBAccess test = new CouchDBAccessImpl();
		test.connect("127.0.0.1", "5984");
		
		
	}

}
