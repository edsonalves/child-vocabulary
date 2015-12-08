package br.ufpe.cin.childvocabulary.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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




	public Map<ISynset, IWord> getSenses(String word, POS pos){

		/*
		 * Recebe uma palavra e um POS e retorna um Map com synsets e sentidos
		 */

		IIndexWord indexWord = null; 

		indexWord = dictionary.getIndexWord(word, pos);
		if (indexWord == null){
			return null;
		}

		List<IWordID> listWordID = indexWord.getWordIDs();
		if (listWordID == null){
			return null;
		}

		Map<ISynset,IWord> mapSenses = new HashMap<ISynset,IWord>();

		for(IWordID iWordID: listWordID){ 
			IWord iWord = this.dictionary.getWord(iWordID);
			if(iWord == null){
				continue;
			}

			ISynset synset = iWord.getSynset();
			if(synset == null || synset.getWords() == null){
				continue;
			}

			mapSenses.put(synset, iWord);
		}

//wordValue.getSynset().getGloss().toString()
		return mapSenses.isEmpty() ? null : mapSenses;

	}


}
