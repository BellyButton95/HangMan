package Logic;

import java.util.Random;

public class FourPictures {
	private String word;
	private String chars;
	private String[] file;

	// game mechanics
	private int wrongAnswers;
	private String progress;
	private String[] mod;
	private char hintChar;
	public static String winLose;

	public FourPictures(String w, String c, String[] f) {
		word = w.toLowerCase();
		chars = c.toLowerCase();
		file = f;
		wrongAnswers = 0;
		winLose = "";
		mod = new String[word.length()];
		for (int i = 0; i < word.length(); i++) {
			if(i!=word.length()-1){
			mod[i] = "_ ";
			}
			else{
				mod[i] = "_";
			}
		}
		progress = getWordProgress();
	}
	
	

	public String tryProgress(char c) {
		if(isCharAtWord(c)){
			for(int i = 0; i < mod.length; i++){
				if(word.charAt(i)==c){
					mod[i] = c + "";
				}
			}
		}
		else{
			wrongAnswers++;
		}
		return getWordProgress();
	}

	public boolean isCharAtWord(char c) {
		for (int i = 0; i < mod.length; i++) {
			if (word.charAt(i) == c) {
				return true;
			}
		}
		return false;
	}
	
	public String getWordProgress(){
		progress = "";
		for(int i =0; i < mod.length; i++){
			progress += mod[i];
		}
		return progress;
	}
	
	public String hint(){
		String s = progress.replaceAll("\\s", "");
		Random rand = new Random();
		int z = rand.nextInt(word.length());
		hintChar = word.charAt(z);
//		System.out.println(hintChar);
		if(s.charAt(z) != hintChar){
//			System.out.println(progress);
			return tryProgress(hintChar);
		}
		else{
			return hint();
		}
	}
	
	public boolean isGameOver() {
		if(progress.equals(word)){
			winLose = "Correct!";
			return true;
		}
		if(wrongAnswers > 4){
			winLose = "Incorrect! The word was "+word;
			return true;
		}
		return false;
	}
	
	public char getHintChar(){
		return hintChar;
	}

	public int getWA() {
		return wrongAnswers;
	}
	
	public static String getWinLose(){
		return winLose;
	}
	
	public String getProgress(){
		return progress;
	}

	public String getWord() {
		return word;
	}

	public String getChar() {
		return chars;
	}

	public String[] getFile() {
		return file;
	}

	public int getWordLen() {
		return word.length();
	}

	public int getCharLen() {
		return chars.length();
	}

	public String toStrint(){
		return "twa e igra batka";
	}
}
