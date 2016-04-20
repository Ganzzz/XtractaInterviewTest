package load;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Document;
import model.Word;

public class InvoiceFileIndexer implements ILoadMapIndex<String, Document> {

	@Override
	public Map<String, Document> loadToIndexAndReturnIndex(String filePath) {
		File file = new File(filePath);
		Map<String, Document> documentMap = new HashMap<String, Document>();
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				Document doc = new Document();
				doc = buildIndex(f.getAbsolutePath(), doc);
				documentMap.put(doc.getDocumentName().toLowerCase(), doc);
			}

		} else {
			Document doc = new Document();
			doc = buildIndex(filePath, doc);
			documentMap.put(doc.getDocumentName(), doc);
		}
		return documentMap;
	}

	@Override
	public boolean loadToIndex(String filePath) {

		try {
			// New index document
			Document doc = new Document();
			doc = buildIndex(filePath, doc);

			// store to a storage layer here. Not implemented as for this
			// interview, documents are returned and held in-memory.

		} catch (Exception ex) {
			// NOTE: for interview sake, logger are not implemented.
			System.err.println(ex.getMessage());
			return false;
		}
		return true;
	}

	private Document buildIndex(String filePath, Document doc) {
		List<Word> docWords = null;

		// set the document name
		Path p = Paths.get(filePath);
		doc.setDocumentName(p.getFileName().toString());

		// read the contents of the file as lines
		List<String> fileLines = null;
		fileLines = readFileAsLines(filePath);

		String[] metadataTokens = null;
		String[] keyVal = null;
		String key = null;
		String val = null;
		Integer line_id = null;
		for (String line : fileLines) {

			line_id = null;

			// each line has a word and meta data about the word from the
			// document
			metadataTokens = null;
			metadataTokens = line.split(",");
			Word currentWord = new Word();
			for (String token : metadataTokens) {
				keyVal = token.split(":");
				if (keyVal.length != 2)
					continue;
				key = keyVal[0];
				val = keyVal[1];
				if (val != null)
					val = val.replace("'", "").replace("}", "").trim();

				// for interview sake, the metadata is hardcoded instead of
				// using a inversion of control or configuration principles
				if (key.contains("'word'")) {
					currentWord.setValue(val);

				} else if (key.contains("'word_id'")) {
					currentWord.setWordId(Integer.parseInt(val));

				} else if (key.contains("'line_id'")) {
					line_id = Integer.parseInt(val);
					currentWord.setLineId(line_id);
				}
			}

			// Adding the wordlist to per document index
			if (doc.getWordListByLine().containsKey(line_id)) {
				docWords = doc.getWordListByLine().get(line_id);
			} else {
				docWords = new ArrayList<Word>();
			}
			docWords.add(currentWord);
			// A presorted array is not sorted again. Optimized
			// implementation
			Collections.sort(docWords);
			doc.getWordListByLine().put(line_id, docWords);
		}

		return doc;
	}

	public List<String> readFileAsLines_fromClassPath(String fileName) {
		List<String> lines = null;
		try {

			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(fileName).getFile());

			lines = readFileAsLines(file.getAbsolutePath());

		} catch (Exception ex) {
			// NOTE: for interview sake, logger are not implemented.
			System.out.print(ex.getMessage());
		}
		return lines;

	}

	public List<String> readFileAsLines(String filePath) {
		List<String> lines = null;
		try {
			File file = new File(filePath);
			lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
		} catch (Exception ex) {
			// NOTE: for interview sake, logger are not implemented.
			System.out.print(ex.getMessage());
		}
		return lines;
	}

	public static void main(String[] args) {
		ILoadMapIndex dataLoader = new InvoiceFileIndexer();
		dataLoader
				.loadToIndex("/Users/ganesh/git/Xtracta_Interview/src/test/resources/invoice.txt");

	}

}
