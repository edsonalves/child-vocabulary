package br.ufpe.cin.childvocabulary.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import br.ufpe.cin.childvocabulary.util.PropertiesUtil;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;

public class WordNetService {

	private IDictionary dictionary;
	
	public WordNetService(){
		URL url = null;
	
		try {
			url = new URL ("file:" + PropertiesUtil.getWordNetDictionaryDict() );

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		setDictionary(new Dictionary (url));
		try {
			getDictionary().open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public IDictionary getDictionary() {
		return dictionary;
	}
	public void setDictionary(IDictionary dictionary) {
		this.dictionary = dictionary;
	}
	
	
}
