package search;

public interface IHashSearch extends ISearch {

	public void buildIndex(String keyVal);
	public String getId(String value);

}
