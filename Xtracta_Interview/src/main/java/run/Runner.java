package run;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import search.HashSearch;
import search.IHashSearch;
import load.ILoad;
import load.InvoiceFileLoader;
import model.Document;
import model.Word;

public class Runner {

	ILoad dataLoader = new InvoiceFileLoader();
	IHashSearch searcher = new HashSearch();
	static Map<String, Document> documentMap = new HashMap<String, Document>();

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO: send file or directory as arguments.
		// TODO: println to show the process
		Runner runner = new Runner();
		String supplierFilePath = args[0];
		String invoiceFilePath = args[1];

		runner.buildNecessaryIndices(invoiceFilePath, supplierFilePath);

		// create a scanner so we can read the command-line input
		Scanner scanner = new Scanner(System.in);

		do {
			System.out
					.println("Enter invoice file name with extension to find its supplier information");
			String invoiceFileName = scanner.next();
			Document doc = documentMap.get(invoiceFileName);

			String supplierName = runner.findSupplier(doc);
			System.out.println("The Supplier Name is :" + supplierName);
		} while (true);
	}

	// for interview sake, building index happens as a part of search (as it
	// runs in memory) as the first step.
	public String findSupplier(Document invoiceDoc) {

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

	private void buildNecessaryIndices(String invoiceFilePath,
			String supplierFilePath) {
		System.out.println("Started building supplier data index");
		this.addToIndex(supplierFilePath);
		System.out.println("Finished building supplier data index");

		System.out.println("Started building invoice data index");

		documentMap = dataLoader
				.loadToIndexAndReturnDocumentMap(invoiceFilePath);

		System.out.println("finished building invoice data index");
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
			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					addFilesToIndex(f);
				}
			} else {
				addFilesToIndex(file);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addFilesToIndex(File file) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(file
				.getAbsolutePath()));
		for (String line : lines) {
			searcher.buildIndex(line);
		}

	}

}
