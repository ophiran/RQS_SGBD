package alimCB;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class Country implements SQLData{
	private String code;
	private String country;
	
	public Country(String code, String country) {
		this.code = code;
		this.country = country;
	}
	
	@Override
	public String toString() {
		return code + " : " + country;
	}

	@Override
	public String getSQLTypeName() throws SQLException {
		return "COUNTRY_T";
	}

	@Override
	public void readSQL(SQLInput stream, String typeName) throws SQLException {
		code = stream.readString();
		country = stream.readString();
	}

	@Override
	public void writeSQL(SQLOutput stream) throws SQLException {
		stream.writeString(code);
		stream.writeString(country);
	}
}
