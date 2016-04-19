/**
 * 
 */
package load;

import model.Document;

/**
 * Data loading environment and storage could be different for different
 * requirements, load size, data complexity, indices used etc. This ILoad
 * interface exposes methods for different load implementation. This load can
 * load into an in-memory/disk based Index model. Or can be extended to index it
 * using a database or external indexes.
 * 
 * @author ganesh
 */
public interface ILoad {

	// NOTE: Assumption: for the interview sake, the structure & complexity of
	// different invoice files are treated to be the same.

	/**
	 * returns a boolean value saying the success or failure of loading into an index.
	 * 
	 * @param filePath
	 * @return
	 */
	public boolean loadToIndex(String filePath);
	
	/**
	 * returns a Document with an optmised index. For the sake of interview the document is not stored, but rather its in memory, this is
	 * a helper method.
	 * 
	 * @param filePath
	 * @return
	 */
	public Document loadToIndexAndReturnDocument(String filePath);

}
