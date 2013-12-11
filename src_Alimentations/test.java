import java.sql.SQLException;
import java.util.ArrayList;

import dbAccess.OracleDBAccess;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;


public class test {

	static OracleDBAccess oraclDBTest;
	public static void main(String[] args) {
		try {
			oraclDBTest = new OracleDBAccess();
			oraclDBTest.startConnection("@localhost:1521:XE", "BD_CB", "dummy");
			
			ArrayDescriptor arrayDesc = ArrayDescriptor.createDescriptor("MOVIES_T",oraclDBTest.getConnection());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
