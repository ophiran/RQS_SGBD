package alimCB;

public class Language {
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
}
