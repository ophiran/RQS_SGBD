package searchCI;

public class StringSearch extends ThreadSearch {

	@Override
	protected boolean contains(Object t1, Object t2) {
		if(t1 instanceof String && t2 instanceof String){
			return ((String)t1).toLowerCase().contains(((String)t2).toLowerCase());
		}
		return false;
	}

}
