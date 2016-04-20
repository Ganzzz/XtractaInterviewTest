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
import load.ILoadMapIndex;
import load.InvoiceFileIndexer;
import load.SupplierFileIndexer;
import model.Document;
import model.Supplier;
import model.Word;

public class Runner {

	ILoadMapIndex<String, Document> dataLoader = new InvoiceFileIndexer();
	ILoadMapIndex<String, List<Supplier>> supplierDataIndexer = new SupplierFileIndexer();

	Map<String, Document> invoiceDocumentMap = new HashMap<String, Document>();
	Map<String, List<Supplier>> supplierMap = new HashMap<String, List<Supplier>>();

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
			Document doc = runner.invoiceDocumentMap.get(invoiceFileName
					.toLowerCase());
			if (doc == null) {
				System.out.println("File not found in Index");
				continue;
			}

			Supplier supplier = runner.findSupplier(doc);
			if (supplier == null) {
				System.out.println("Unable to find supplier Information");
			}
			System.out.println("The Supplier Name is :"
					+ supplier.getSupplierName());
			System.out.println("The Supplier Id is :"
					+ supplier.getSupplierId());
		} while (true);
	}

	// for interview sake, building index happens as a part of search (as it
	// runs in memory) as the first step.
	public Supplier findSupplier(Document invoiceDoc) {

		Supplier supplier = null;

		// pragmatically, number of supplier could be lesser than the invoices.
		Iterator<Entry<Integer, List<Word>>> it = invoiceDoc
				.getWordListByLine().entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pair = it.next();
			@SuppressWarnings("unchecked")
			List<Word> words = (List<Word>) pair.getValue();
			for (Word word : words) {
				if (supplierMap.containsKey(word.getValue())) {
					supplier = getSupplierName(invoiceDoc, word);
					return supplier;
				}
			}
		}

		return supplier;

	}

	private void buildNecessaryIndices(String invoiceFilePath,
			String supplierFilePath) {
		System.out.println("Started building supplier data index");
		supplierMap = this.supplierDataIndexer
				.loadToIndexAndReturnIndex(supplierFilePath);
		System.out.println("Finished building supplier data index");

		System.out.println("Started building invoice data index");

		invoiceDocumentMap = dataLoader
				.loadToIndexAndReturnIndex(invoiceFilePath);

		System.out.println("finished building invoice data index");
	}

	private Supplier getSupplierName(Document invoiceDoc, Word matchingWord) {

		List<Supplier> matchingNameSupplierList = supplierMap.get(matchingWord
				.getValue());
		List<Word> matchingLine = invoiceDoc.getWordListByLine().get(
				matchingWord.getLineId());

		// one supplier name could be a partial match to another supplier name.
		// to pick the most matching supplier name, we iterate on the inverted
		// index
		Supplier bestMatchingSupplier = null;

		String matchString = "";
		// if inverted index has only one supplier with this partial matching,
		// return immediately
		if (matchingNameSupplierList.size() == 1)
			return matchingNameSupplierList.get(0);

		for (Supplier supplier : matchingNameSupplierList) {
			matchString = "";
			for (Word w : matchingLine) {

				if (!matchString.isEmpty()) {

					if (supplier.getSupplierName().equalsIgnoreCase(
							matchString + " " + w.getValue())) {
						if (bestMatchingSupplier == null
								|| bestMatchingSupplier.getSupplierName()
										.length() < supplier.getSupplierName()
										.length())
							bestMatchingSupplier = supplier;
						// find next best matching supplier
						break;
					} else {
						matchString = matchString + " " + w.getValue();
					}

				} else {
					if (supplier.getSupplierName().contains(w.getValue())) {
						matchString = w.getValue();
					}
				}
			}
		}
		return bestMatchingSupplier;
	}

}
