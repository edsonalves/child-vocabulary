package br.ufpe.cin.childvocabulary.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.childvocabulary.util.PropertiesUtil;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

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

	
	
	
	public ISynset getISynset(String word, POS pos){
		IIndexWord indexWord = null;
		indexWord = dictionary.getIndexWord(word, pos);
		
		List<ISynset> iSynsets = null;
		
		if(indexWord == null){
			return null;
		}

		List<IWordID> listIWordID = indexWord.getWordIDs();
		

		if(listIWordID == null){
			return null;
		}
		iSynsets = new ArrayList<ISynset>();

		for(IWordID iWordID : listIWordID){
			IWord iWord = this.dictionary.getWord(iWordID);
			if(iWord == null){
				continue;
			}
			ISynset iSynset = iWord.getSynset();

			if(iSynset == null || iSynset.getWords() == null){
				continue;
			}
			iSynsets.add(iSynset);
		}
		return iSynsets.isEmpty() ? null : iSynsets.get(0);
	}


}
