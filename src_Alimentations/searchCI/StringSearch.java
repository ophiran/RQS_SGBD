package searchCI;

import java.util.Map;
import java.util.Set;

public class StringSearch extends ThreadSearch {

	public StringSearch(String searchObject, Map<Object, Set<Integer>> index) {
		super(searchObject,index);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean contains(Object t1, Object t2) {
		if(t1 instanceof String && t2 instanceof String){
			return ((String)t1).toLowerCase().contains(((String)t2).toLowerCase());
		}
		return false;
	}

}
