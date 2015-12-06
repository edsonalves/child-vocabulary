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

import WordOntoMain.ObjectPropertyType;
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

	public static void init(){
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
		for(String word : wordList){
			IIndexWord indexWord = wordNetService.getDictionary().getIndexWord(word, pos);
			if (indexWord != null){
				writerFound.println( word + " | " + pos.toString());
				IWordID wordID = indexWord.getWordIDs().get(0);
				IWord wordValue = wordNetService.getDictionary().getWord(wordID);

				OWLClass wordClass = null;
				try {
					wordClass = ontologyService.createClass(word.replace(" ", "_"));
				} catch (OWLOntologyStorageException e) {
					e.printStackTrace();
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.SIMILAR_TO)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					for(String wordAux : wordList ){
						
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countSimilar++;
							ontologyService.createObjectProperties(ObjectPropertyType.IS_SIMILAR_TO, wordClass, wordClassRelated);
						}
					}
				}
				
				for (IWordID relatedWords : wordValue.getRelatedWords(Pointer.ANTONYM)){
					String relatedLema = wordNetService.getDictionary().getWord(relatedWords).getLemma();
					for(String wordAux : wordList ){
						if(wordAux.equalsIgnoreCase(relatedLema) &&  !wordAux.equalsIgnoreCase(word)){
							OWLClass wordClassRelated = null;
							try {
								wordClassRelated = ontologyService.createClass(relatedLema.replace(" ", "_"));
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							countAntony++;
							ontologyService.createObjectProperties(ObjectPropertyType.IS_ANTONYM_OF, wordClass, wordClassRelated);
						}
					}
				}
			}else{
				writerNotFound.println( word + " | " + pos.toString());
			}	
		}
		System.out.println("IS_SIMILAR_TO: "+ countSimilar);
		System.out.println("IS_ANTONYM_OF: "+ countAntony);
	}
	
	public static void start(){
		//Ler Substantivos
		ReaderFileUtil listaSubstantivo = new ReaderFileUtil("src/main/resources/input/PalavrasFrequentesSubstantivos.txt");
		int countSubstantivos = listaSubstantivo.getWordList().size();
		System.out.println("Substantivo iniciado ... - "+ countSubstantivos);
		buildClassesAndProperties(listaSubstantivo.getWordList(), POS.NOUN);
		System.out.println("Substantivo finalizado.");
		
		//Ler Adjetivos
		ReaderFileUtil listaAdjetivos = new ReaderFileUtil("src/main/resources/input/PalavrasFrequentesAdjetivos.txt");
		int countAdjetivos = listaAdjetivos.getWordList().size();
		System.out.println("Adjetivo iniciado ... - "+ countAdjetivos);
		buildClassesAndProperties(listaAdjetivos.getWordList(), POS.ADJECTIVE);
		System.out.println("Adjetivo finalizado.");

		//Ler Verbos
		ReaderFileUtil listaVerbos = new ReaderFileUtil("src/main/resources/input/PalavrasFrequentesVerbos.txt");
		int countVerbos = listaVerbos.getWordList().size();
		System.out.println("Verbo iniciado ... - "+ countVerbos);
		buildClassesAndProperties(listaVerbos.getWordList(), POS.VERB);
		System.out.println("Verbo finalizado.");
		
		//salva ontologia
		System.out.println("Salvando ontologia ...");
		File output = new File("src/main/resources/output/output.owl");
		OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
		try {
			ontologyService.getManager().saveOntology(ontologyService.getOntology(), owlxmlFormat, IRI.create(output.toURI()));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
		System.out.println("Ontologia salva.");
		
		writerFound.close();
		writerNotFound.close();
	}
	
	public static void finish(){
		
	}
	public static void main(String[] args) {
		init();
		start();
	}

}
