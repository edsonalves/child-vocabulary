package br.ufpe.cin.childvocabulary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import br.ufpe.cin.childvocabulary.service.OntologyService;
import br.ufpe.cin.childvocabulary.service.WordNetService;
import br.ufpe.cin.childvocabulary.util.ProgressBar;
import br.ufpe.cin.childvocabulary.util.ReaderFileUtil;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

public class Application {
	static WordNetService wordNetService = null;
	static OntologyService ontologyService = null;
	static ProgressBar progressBar = null;
	static PrintWriter writerFound = null;
	static PrintWriter writerNotFound = null;
	static PrintWriter writerContentFound = null;

	public static void init() {
		wordNetService = new WordNetService();
		ontologyService = new OntologyService();
		progressBar = new ProgressBar();
		try {
			writerFound = new PrintWriter("src/main/resources/output/found.txt", "UTF-8");
			writerNotFound = new PrintWriter("src/main/resources/output/notFound.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void buildClassesAndProperties(ArrayList<String> wordList, POS pos){
		int countSimilar = 0;
		int countAntony = 0;
		int countHyper = 0;
		int countHypo = 0;
		int countAlso = 0;
		int countCause = 0;
		int countVerbGroup = 0;
		int countDerivational = 0;
		int countAtributte = 0;
		int countEntailment = 0;
		int countPertainym = 0;
		int countParticiple = 0;
		int countDerivedAdj = 0;
		int countHolonymMember = 0;
		int countHolonymPart = 0;
		int countHolonymSubst = 0;
		int countMeronymMember = 0;
		int countMeronymPart = 0;
		int countMeronymSubst = 0;
		int countRegion = 0;
		int countRegionMember = 0;
		int countTopic = 0;
	
		for(String word : wordList){
			word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
			IIndexWord indexWord = wordNetService.getDictionary().getIndexWord(word, pos);
			if (indexWord != null){
				writerFound.println( word + " | " + pos.toString());
				IWordID wordID = indexWord.getWordIDs().get(0);
				IWord wordValue = wordNetService.getDictionary().getWord(wordID);	
				
/*				///////////////////////////////////////
				
				for(IWordID id: wordValue.getRelatedWords()) {
				
				  System.out.println( wordNetService.getDictionary().getWord( id ).getLemma() );
				
				}
				
				for(Entry<IPointer, List<IWordID>> entry: wordValue.getRelatedMap().entrySet()) {
					
				  System.out.println( entry.getKey() );
				  System.out.println( entry.getValue() );
				  
				}
				
				///////////////////////////////////////
*/
								
/*				System.out.println("Lemma: " + wordValue.getLemma());
				System.out.println("POS: " + wordValue.getPOS());
				System.out.println("Sense Key: " + wordValue.getSenseKey().toString());
				System.out.println("RelatedMap: " + wordValue.getRelatedMap().toString());
				System.out.println("Verb frames: " + wordValue.getVerbFrames());
				System.out.println("Adjective Marker: " + wordValue.getAdjectiveMarker());
				System.out.println("-----------------------------------------");*/
				
				OWLClass wordClass = null;
				try {
					wordClass = ontologyService.createClass(word.replace(" ", "_"));					
				} catch (OWLOntologyStorageException e) {
					e.printStackTrace();
				}
				
				ontologyService.createAnnotation(wordClass, wordValue.getSynset().getGloss().toString());
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.SIMILAR_TO)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;	
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countSimilar++;
							ontologyService.createObjectProperties(Pointer.SIMILAR_TO, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.ANTONYM)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;	
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countAntony++;
							ontologyService.createObjectProperties(Pointer.ANTONYM, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.HYPERNYM)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countHyper++;
							ontologyService.createObjectProperties(Pointer.HYPERNYM, wordClass, wordClassRelated);							
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.HYPONYM)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countHypo++;
							ontologyService.createObjectProperties(Pointer.HYPONYM, wordClass, wordClassRelated);							
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.ALSO_SEE)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countAlso++;
							ontologyService.createObjectProperties(Pointer.ALSO_SEE, wordClass, wordClassRelated);							
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.CAUSE)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countCause++;
							ontologyService.createObjectProperties(Pointer.CAUSE, wordClass, wordClassRelated);							
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.VERB_GROUP)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countVerbGroup++;
							ontologyService.createObjectProperties(Pointer.VERB_GROUP, wordClass, wordClassRelated);							
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.DERIVATIONALLY_RELATED)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countDerivational++;
							ontologyService.createObjectProperties(Pointer.DERIVATIONALLY_RELATED, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.ATTRIBUTE)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countAtributte++;
							ontologyService.createObjectProperties(Pointer.ATTRIBUTE, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.ENTAILMENT)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countEntailment++;
							ontologyService.createObjectProperties(Pointer.ENTAILMENT, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.PERTAINYM)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countPertainym++;
							ontologyService.createObjectProperties(Pointer.PERTAINYM, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.PARTICIPLE)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countParticiple++;
							ontologyService.createObjectProperties(Pointer.PARTICIPLE, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.DERIVED_FROM_ADJ)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countDerivedAdj++;
							ontologyService.createObjectProperties(Pointer.DERIVED_FROM_ADJ, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.HOLONYM_MEMBER)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countHolonymMember++;
							ontologyService.createObjectProperties(Pointer.HOLONYM_MEMBER, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.HOLONYM_PART)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countHolonymPart++;
							ontologyService.createObjectProperties(Pointer.HOLONYM_PART, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.HOLONYM_SUBSTANCE)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countHolonymSubst++;
							ontologyService.createObjectProperties(Pointer.HOLONYM_SUBSTANCE, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.MERONYM_MEMBER)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countMeronymMember++;
							ontologyService.createObjectProperties(Pointer.MERONYM_MEMBER, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.MERONYM_PART)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countMeronymPart++;
							ontologyService.createObjectProperties(Pointer.MERONYM_PART, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.MERONYM_SUBSTANCE)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countMeronymSubst++;
							ontologyService.createObjectProperties(Pointer.MERONYM_SUBSTANCE, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.REGION)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countRegion++;
							ontologyService.createObjectProperties(Pointer.REGION, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.REGION_MEMBER)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countRegionMember++;
							ontologyService.createObjectProperties(Pointer.REGION_MEMBER, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.TOPIC)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					relatedLema = relatedLema.substring(0, 1).toUpperCase() + relatedLema.substring(1).toLowerCase();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countTopic++;
							ontologyService.createObjectProperties(Pointer.TOPIC, wordClass, wordClassRelated);
						}
					}
				}
				
			}else{
				writerNotFound.println( word + " | " + pos.toString());
			}	
		}
		System.out.println("IS_SIMILAR_TO: "+ countSimilar);
		System.out.println("IS_ANTONYM_OF: "+ countAntony);
		System.out.println("HAS_HYPERNYM: "+ countHyper);
		System.out.println("HAS_HYPONYM: "+ countHypo);
		System.out.println("ALSO_SEE: "+ countAlso);
		System.out.println("CAUSE: "+ countCause);
		System.out.println("VERB GROUP: "+ countVerbGroup);
		System.out.println("DERIVATIONALLY RELATED: "+ countDerivational);
		System.out.println("ATRIBUTTE: "+ countAtributte);
		System.out.println("ENTAILMENT: "+ countEntailment);
		System.out.println("PERTAINYM: "+ countPertainym);
		System.out.println("PARTICIPLE: "+ countParticiple);
		System.out.println("DERIVED FROM ADJ: "+ countDerivedAdj);
		System.out.println("HOLONYM MEMBER: "+ countHolonymMember);
		System.out.println("HOLONYM PART: "+ countHolonymPart);
		System.out.println("HOLONYM SUBSTANCE: "+ countHolonymSubst);
		System.out.println("MERONYM MEMBER: "+ countMeronymMember);
		System.out.println("MERONYM PART: "+ countMeronymPart);
		System.out.println("MERONYM SUBSTANCE: "+ countMeronymSubst);
		System.out.println("REGION: "+ countRegion);
		System.out.println("REGION MEMBER: "+ countRegionMember);
		System.out.println("TOPIC: "+ countTopic);
		System.out.println("------------------------------------");
		
		
	}

	public static void start() {
		// Ler Substantivos
		ReaderFileUtil listaSubstantivo = new ReaderFileUtil(
				"src/main/resources/input/PalavrasFrequentesSubstantivos.txt");
		int countSubstantivos = listaSubstantivo.getWordList().size();
		System.out.println("Substantivo iniciado ... - " + countSubstantivos);
		buildClassesAndProperties(listaSubstantivo.getWordList(), POS.NOUN);
		System.out.println("Substantivo finalizado.");

		// Ler Adjetivos
		ReaderFileUtil listaAdjetivos = new ReaderFileUtil("src/main/resources/input/PalavrasFrequentesAdjetivos.txt");
		int countAdjetivos = listaAdjetivos.getWordList().size();
		System.out.println("Adjetivo iniciado ... - " + countAdjetivos);
		buildClassesAndProperties(listaAdjetivos.getWordList(), POS.ADJECTIVE);
		System.out.println("Adjetivo finalizado.");

		// Ler Verbos
		ReaderFileUtil listaVerbos = new ReaderFileUtil("src/main/resources/input/PalavrasFrequentesVerbos.txt");
		int countVerbos = listaVerbos.getWordList().size();
		System.out.println("Verbo iniciado ... - " + countVerbos);
		buildClassesAndProperties(listaVerbos.getWordList(), POS.VERB);
		System.out.println("Verbo finalizado.");

		// Salva Ontologia
		System.out.println("Salvando ontologia ...");
		File output = new File("src/main/resources/output/output.owl");
		OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
		try {
			ontologyService.getManager().saveOntology(ontologyService.getOntology(), owlxmlFormat,
					IRI.create(output.toURI()));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
		System.out.println("Ontologia salva.");

		writerFound.close();
		writerNotFound.close();
	}

	public static void finish() {

	}

	public static void main(String[] args) {
		init();
		start();
	}

}
