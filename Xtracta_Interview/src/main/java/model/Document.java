package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Document {
	
	private long documentId;
	private String documentName;
	private Map<Integer, List<Word>> wordListByLine = new HashMap<Integer, List<Word>>(); 

	public long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public Map<Integer, List<Word>> getWordListByLine() {
		return wordListByLine;
	}

	public void setWordListByLine(Map<Integer, List<Word>> wordListByLine) {
		this.wordListByLine = wordListByLine;
	}

}
