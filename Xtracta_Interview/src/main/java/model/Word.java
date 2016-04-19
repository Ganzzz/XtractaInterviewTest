package model;

public class Word implements Comparable<Word> {

	private String value;
	private int lineId;
	private int wordId;

	// TODO: would be convenient for application, but not implemented for this
	// interview
	private Word nextWord;
	private Word previousWord;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	public int getWordId() {
		return wordId;
	}

	public void setWordId(int wordId) {
		this.wordId = wordId;
	}

	public Word getNextWord() {
		return nextWord;
	}

	public void setNextWord(Word nextWord) {
		this.nextWord = nextWord;
	}

	public Word getPreviousWord() {
		return previousWord;
	}

	public void setPreviousWord(Word previousWord) {
		this.previousWord = previousWord;
	}

	@Override
	public int compareTo(Word o) {
		// this.getWordId() >= o.getWordId() ? 1 : -1;
		return this.getWordId() - o.getWordId();
	}

}
