/**
 * 
 */
package load;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Document;
import model.Supplier;

/**
 * This is based on the concept of inverted index using maps
 * 
 * @author ganesh
 *
 */
public class SupplierFileIndexer implements
		ILoadMapIndex<String, List<Supplier>> {

	@Override
	public boolean loadToIndex(String filePath) {
		// not implemented for the sake of interview as its not used for this
		// exercise
		return false;
	}

	@Override
	public Map<String, List<Supplier>> loadToIndexAndReturnIndex(String filePath) {

		Map<String, List<Supplier>> supplierMap = new HashMap<String, List<Supplier>>();
		try {
			File file = new File(filePath);

			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					if(!f.getAbsolutePath().contains("txt")){
						continue;
					}
					supplierMap = buildIndex(f.getAbsolutePath(), supplierMap);
				}

			} else {
				supplierMap = buildIndex(filePath, supplierMap);

			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return supplierMap;
	}

	private Map<String, List<Supplier>> buildIndex(String absolutePath,
			Map<String, List<Supplier>> supplierMap) throws IOException {

		List<String> lines = Files.readAllLines(Paths.get(absolutePath));
		for (String line : lines) {
			Supplier supplier = new Supplier();
			int seperatorIndex = line.indexOf(",");
			supplier.setSupplierId(line.substring(0, seperatorIndex));
			supplier.setSupplierName(line.substring(seperatorIndex + 1));
			for (String partName : supplier.getSupplierName().split(" ")) {
				if (!supplierMap.containsKey(partName)) {
					List<Supplier> names = new ArrayList<Supplier>();
					names.add(supplier);
					supplierMap.put(partName, names);
				} else {
					List<Supplier> names = supplierMap.get(partName);
					names.add(supplier);
				}
			}

		}
		return supplierMap;
	}

}
