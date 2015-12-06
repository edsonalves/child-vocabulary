package br.ufpe.cin.childvocabulary.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReaderFileUtil {
	private ArrayList<String> wordList;
	private Scanner scanner;
	
	public ReaderFileUtil(String path){
		wordList = new ArrayList<String>();
		try {
			scanner = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(scanner.hasNextLine()) {
			String word = scanner.nextLine(); 
			wordList.add(word);
		}
		scanner.close(); 
	}

	public ArrayList<String> getWordList() {
		return wordList;
	}
	
}
