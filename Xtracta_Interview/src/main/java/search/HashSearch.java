package search;

import java.util.HashMap;
import java.util.Map;

public class HashSearch implements IHashSearch {

	private Map<String, String> searchIndex = new HashMap<String, String>();
	private Map<String, String> inverseSearchIndex = new HashMap<String, String>();

	public void buildIndex(String keyVal) {
		int seperatorIndex = keyVal.indexOf(",");
		String key = keyVal.substring(0, seperatorIndex);
		String value = keyVal.substring(seperatorIndex + 1);
		if (!searchIndex.containsKey(key)) {
			searchIndex.put(key, value);
		}
		inverseSearchIndex.put(value, key);
	}

	@Override
	public boolean exists(String value) {
		return searchIndex.values().contains(value);
	}

	@Override
	public String getId(String value) {
		return inverseSearchIndex.get(value);
	}

}
