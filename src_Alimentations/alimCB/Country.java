package alimCB;

public class Country {
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
}
