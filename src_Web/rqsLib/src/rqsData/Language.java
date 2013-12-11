package rqsData;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class Language implements SQLData{
	private String code;
	private String language;
	
	public Language(String code, String language) {
		this.code = code;
		this.language = language;
	}
	
	@Override
	public String toString() {
		return code + " : " + language;
	}

	@Override
	public String getSQLTypeName() throws SQLException {
		return "LANGUAGE_T";
	}

	@Override
	public void readSQL(SQLInput stream, String arg1) throws SQLException {
		code = stream.readString();
		language = stream.readString();
	}

	@Override
	public void writeSQL(SQLOutput stream) throws SQLException {
		stream.writeString(code);
		stream.writeString(language);
	}
}
