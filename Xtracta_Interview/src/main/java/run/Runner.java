package run;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import search.HashSearch;
import search.IHashSearch;
import load.ILoad;
import load.InvoiceFileLoader;
import model.Document;
import model.Word;

public class Runner {

	ILoad dataLoader = new InvoiceFileLoader();
	IHashSearch searcher = new HashSearch();

	public static void main(String[] args) {
		// TODO: send file or directory as arguments.
		// TODO: println to show the process
		Runner runner = new Runner();
		String supplierFilePath = "/Users/ganesh/git/Xtracta_Interview/src/test/resources/suppliernames.txt";
		String invoiceFilePath = "/Users/ganesh/git/Xtracta_Interview/src/test/resources/invoice.txt";

		String supplierName = runner.findSupplier(invoiceFilePath,
				supplierFilePath);
		System.out.println("The Supplier Name is :" + supplierName);

	}

	public String findSupplier(String invoiceFilePath, String supplierFilePath) {

		System.out.println("Started building supplier data index");
		this.addToIndex(supplierFilePath);
		System.out.println("Finished building supplier data index");

		System.out.println("Started building invoice data index");
		Document invoiceDoc = dataLoader
				.loadToIndexAndReturnDocument(invoiceFilePath);

		System.out.println("finished building invoice data index");

		String supplierName = "";

		// pragmatically, number of supplier could be lesser than the invoices.
		Iterator<Entry<Integer, List<Word>>> it = invoiceDoc
				.getWordListByLine().entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pair = it.next();
			@SuppressWarnings("unchecked")
			List<Word> words = (List<Word>) pair.getValue();
			for (Word word : words) {
				if (searcher.exists(word.getValue())) {
					supplierName = getSupplierName(invoiceDoc, word);
					System.out.println("The Supplier Id is :"
							+ searcher.getId(supplierName));
					return supplierName;
				}
			}
		}

		/*
		 * for(String line: invoiceFileLines){ int beginingIndex =
		 * line.indexOf("word': '");
		 * 
		 * String word = line.substring(beginingIndex+8, line.indexOf(",",
		 * beginingIndex)); //corner case as apostrophes are possible in
		 * supplier names as well, the last apostrophe is removed with index
		 * position word= word.substring(0, word.length()-1);
		 * 
		 * if(searcher.exists(word)){ return word; } }
		 */
		
		return supplierName;

	}

	private String getSupplierName(Document invoiceDoc, Word matchinWord) {

		String supplierName = "";
		String tempWord = "";
		int lineId = matchinWord.getLineId();
		List<Word> wordList = invoiceDoc.getWordListByLine().get(lineId);
		for (Word word : wordList) {
			tempWord = word.getValue();
			if (!supplierName.isEmpty()) {
				tempWord = supplierName + tempWord;
			}

			if (searcher.exists(tempWord)) {
				supplierName = supplierName + word.getValue() + " ";
			} else {
				if (!supplierName.isEmpty()) {
					return supplierName.trim();
				}

			}
		}

		return supplierName.trim();
	}

	private void addToIndex(String filePath) {
		try {
			File file = new File(filePath);
			List<String> lines = Files.readAllLines(Paths.get(file
					.getAbsolutePath()));
			for (String line : lines) {
				searcher.buildIndex(line);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}