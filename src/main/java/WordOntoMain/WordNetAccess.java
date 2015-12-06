package WordOntoMain;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;



public class WordNetAccess {

	private IDictionary iDictionary;
	private String wordNetHome;


	public WordNetAccess(String wordNetHome, String dictionaryHome) throws IOException {

		this.wordNetHome = wordNetHome;
		URL url = new URL ("file:" + this.wordNetHome + dictionaryHome);

		this.iDictionary = new Dictionary (url);
		iDictionary.open();
		
	}

	
	// MÉTODOS

	public Map<ISynset, Sense> getSenses(String word, POS pos){
		
		/*
		 * Recebe uma palavra e um POS e retorna um Map com synsets e sentidos
		 */

		// INICIALIZA indexWord COMO NULL E DEPOIS ATRIBUI UM VALOR DE ACORDO COM O POS DO PARÂMETRO PASSADO
		IIndexWord indexWord = null; 

		if (pos == POS.ADJECTIVE){
			indexWord = iDictionary.getIndexWord(word, POS.ADJECTIVE);
		} else if (pos == POS.NOUN){
			indexWord = iDictionary.getIndexWord(word, POS.NOUN);
		} else if (pos == POS.VERB){
			indexWord = iDictionary.getIndexWord(word, POS.VERB);
		} else {
			indexWord = this.iDictionary.getIndexWord(word, POS.NOUN);
		}

		// SE NÃO EXISTIR A PALAVRA COM AQUELE POS, RETORNA NULL
		if (indexWord == null){
			return null;
		}


		// CRIA UMA LISTA COM OS IDS DAS PALAVRAS
		List<IWordID> listWordID = indexWord.getWordIDs();

		// POR PADRÃO, ESSA LISTA NÃO PODE SER NULA, VAZIA NEM CONTER NULO
		if (listWordID == null){
			return null;
		}

		// CRIA UM MAPEAMENTO CHAVE-VALOR COM SYNSTET E O SENTIDO
		Map<ISynset,Sense> mapSenses = new HashMap<ISynset,Sense>();


		for(IWordID iWordID: listWordID){ 						// PERCORRE CADA ID DE PALAVRA NA LISTA COM OS IDS DAS PALAVRAS
			IWord iWord = this.iDictionary.getWord(iWordID);

			if(iWord == null){	// SE O ID NÃO EXISTIR, CONTINUA
				continue;
			}

			ISynset synset = iWord.getSynset(); // SE EXISTIR, PEGA O SYNSET AO QUAL ELE PERTENCE

			if(synset == null || synset.getWords() == null){ // SE O SYNSET NÃO EXISTIR, CONTINUA
				continue;
			}

			Sense sense = new Sense(); // CRIA UM NOVO SENTIDO PARA A PALAVRA E ATRIBUI VALORES

			sense.setId(iWord.toString());
			sense.setIndividualID(iWord.getSynset().toString().replaceAll("SID-", "").replaceAll("-N", "")); // NÃO ENTENDI!
			sense.setDescription(synset.getGloss());

			mapSenses.put(synset, sense); // COLOCA A CHAVE E O VALOR NO HASHMAP
		}


		return mapSenses.isEmpty() ? null : mapSenses;

	}

	
	public Sense getFirstSense(String word, POS pos){
		
		List<Sense> senses = searchSenses(word, pos);
		
		if(senses != null){
			Sense sense = senses.get(0);
			return sense;
		} else
				
		//Sense sense = senses.get(0);
					
		return null;
		
	}
	
	
 	public List<Sense> searchSenses(String word, POS pos){

		/*
		 * Recebe uma palavra e retorna uma lista com os sentidos daquela palavra. 
		 * Válido apenas para um POS! 
		 */
		
		IIndexWord indexWord = this.iDictionary.getIndexWord(word, pos); //DEPOIS FAZER O TRATAMENTO PARA TODOS OS POS USADOS

		if(indexWord == null){ 
			return null;
		}

		List<IWordID> listWordID = indexWord.getWordIDs(); // CRIA UMA LISTA DE IDS DAS PALAVRAS

		if(listWordID == null){
			return null;
		}



		List<Sense> senses = new ArrayList<Sense>(); // CRIA UMA LISTA DE SENTIDOS

		for(IWordID iWordID : listWordID){ // PERCORRE A LISTA DE IDS DAS PALAVRAS E PEGA A PALAVRA COM AQUELE ID
			IWord iWord = this.iDictionary.getWord(iWordID);

			if(iWord == null){
				return null;
			}

			ISynset iSynset = iWord.getSynset(); // PEGA O SYNSET DA PALAVRA

			if(iSynset == null || iSynset.getWords() == null){ // SE NÃO EXISTIR O SYNSET OU SE ELE NÃO TIVER PALAVRAS, RENORNA NULL
				return null;
			}


			Map<String, List<ISynsetID>> mapRelations = this.getRelations(iSynset); // CRIA UM MAP COM AS RELAÇÕES DE UM SYNSET

			if(mapRelations == null){
				return null;
			}

			Set<String> terms = new HashSet<String>();

			for(Entry<String, List<ISynsetID>> entry : mapRelations.entrySet()){
				for(ISynsetID iSynsetID : entry.getValue()){
					Set<String> termsAux = this.getTerms(iSynsetID);

					if(termsAux != null){
						terms.addAll(termsAux);
					}
				}
			}

			if(!terms.isEmpty()){
				Sense sense = new Sense();
				sense.setLabel(iWord.getLemma().replaceAll("_", " "));
				sense.setSource("WordNet");
				sense.setDescription(iSynset.getGloss());
				sense.setiSynset(iSynset);

				for(String term : terms){
					sense.addRelatedTerm(term);
				}
				senses.add(sense);
			}

		}

		return senses.isEmpty() ? null : senses;	
	}


	public Map<String, List<ISynsetID>> getRelations(ISynset iSynset){


		Map<String,List<ISynsetID>> mapRelationsISynsetIDs = new HashMap<String,List<ISynsetID>>();

		mapRelationsISynsetIDs.put(Pointer.HYPERNYM.toString(), iSynset.getRelatedSynsets(Pointer.HYPERNYM));
		//mapRelationsISynsetIDs.put(Pointer.HYPERNYM_INSTANCE.toString(), iSynset.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE));
		mapRelationsISynsetIDs.put(Pointer.HYPONYM.toString(), iSynset.getRelatedSynsets(Pointer.HYPONYM_INSTANCE));
		//mapRelationsISynsetIDs.put(Pointer.HYPONYM_INSTANCE.toString(), iSynset.getRelatedSynsets(Pointer.HYPONYM_INSTANCE));

		//mapRelationsISynsetIDs.put(Pointer.MERONYM_PART.toString(), iSynset.getRelatedSynsets(Pointer.MERONYM_PART));
		//mapRelationsISynsetIDs.put(Pointer.MERONYM_MEMBER.toString(), iSynset.getRelatedSynsets(Pointer.MERONYM_MEMBER));
		//mapRelationsISynsetIDs.put(Pointer.MERONYM_SUBSTANCE.toString(), iSynset.getRelatedSynsets(Pointer.MERONYM_SUBSTANCE));

		mapRelationsISynsetIDs.put(Pointer.SIMILAR_TO.toString(), iSynset.getRelatedSynsets(Pointer.SIMILAR_TO));

		mapRelationsISynsetIDs.put(Pointer.ANTONYM.toString(), iSynset.getRelatedSynsets(Pointer.ANTONYM));

		return mapRelationsISynsetIDs;
	}
	
	
	public Set<String> getTerms(ISynsetID iSynsetID){

		List<IWord> iWords = this.iDictionary.getSynset(iSynsetID).getWords(); // CRIA UMA LISTA COM AS PALAVRAS DO SYNSET

		if(iWords == null){
			return null;
		}

		Set<String> terms = new HashSet<String>();

		for(IWord iWord : iWords){
			terms.add(iWord.getLemma().replaceAll("_", " ")); // PROVAVELMENTE AS PALAVRAS ESTÃO GUARDADADAS COM "_" NO LUGAR DOS ESPAÇOS
		}

		return terms.isEmpty() ? null : terms;
	}


	public List<ISynset> getISynsets(String word, POS pos){
		
		/*
		 * Recebe uma palavra e sua POS e retorna uma lista com os Synsets que ela pertence
		 */
		
		IIndexWord indexWord = null;
		
		if (pos == POS.ADJECTIVE){
			indexWord = iDictionary.getIndexWord(word, POS.ADJECTIVE);
		} else if (pos == POS.NOUN){
			indexWord = iDictionary.getIndexWord(word, POS.NOUN);
		} else if (pos == POS.VERB){
			indexWord = iDictionary.getIndexWord(word, POS.VERB);
		} else {
			indexWord = this.iDictionary.getIndexWord(word, POS.NOUN );
		}
		
		if(indexWord == null){
			return null;
		}
		
		List<IWordID> listIWordID = indexWord.getWordIDs();
		
		if(listIWordID == null){
			return null;
		}
		
		List<ISynset> iSynsets = new ArrayList<ISynset>();
		
		for(IWordID iWordID : listIWordID){
			
			IWord iWord = this.iDictionary.getWord(iWordID);
			
			if(iWord == null){
				continue;
			}
			
			ISynset iSynset = iWord.getSynset();
			
			if(iSynset == null || iSynset.getWords() == null){
				continue;
			}
			
			iSynsets.add(iSynset);
			
		}
		
		return iSynsets.isEmpty() ? null : iSynsets;
	}


	public IDictionary getiDictionary() {
		return iDictionary;
	}


	public void setiDictionary(IDictionary iDictionary) {
		this.iDictionary = iDictionary;
	}


	public Category getHypernyms(ISynset synset){
		
		List<ISynsetID> hypernyms = new ArrayList<ISynsetID>();
		
		hypernyms.addAll(synset.getRelatedSynsets(Pointer.HYPERNYM));
		hypernyms.addAll(synset.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE));
		
		if(hypernyms.isEmpty()){
			return null;
		}
		
		ISynsetID sid = hypernyms.get(0);
		
		List<IWord> words = this.iDictionary.getSynset(sid).getWords();
		
		if(words == null){
			return null;
		}
		
		IWord iWord = words.iterator().next();
		
		Category category = new Category();
		
		category.setLabel(iWord.getLemma());
		
		ISynset iSynset2 = iWord.getSynset();
		
		if(iSynset2 != null){
			
			Category fatherCategory = this.getHypernyms(iSynset2);
			
			if(fatherCategory != null){
				category.setFather(fatherCategory);
				category.addChild(category);
			}
		}
		return category;
	}
	
	
	@SuppressWarnings("null")
	public List<IWord> getSynonyms(String word, POS pos){
		
		List<IWord> list = null;
		IIndexWord indexWord = iDictionary.getIndexWord(word, pos);
		IWordID wordID = indexWord.getWordIDs().get(0);
		IWord word2 = iDictionary.getWord(wordID);
		ISynset synset = word2.getSynset();
		
		for(IWord w : synset.getWords()){
			list.add(w);
		}
		
		return list;
	}
	
}
