package WordOntoMain;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;




public class Main {

	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
		// TODO Auto-generated method stub


		// INSTANCIAR E ABRIR O DICIONÁRIO DO WORDNET

		WordNetAccess wordNetAccess = null;

		try {

			wordNetAccess = new WordNetAccess( "src/main/resources/WordNet", "\\3.0\\dict" );			
		}

		catch (MalformedURLException e1) {

			System.out.println( "ERROR. COULD NOT POSSIBLE TO READ WORDNET FILES." );

			e1.printStackTrace();

		}



		// INICIALIZA A ONTOLOGIA
		IRI ontologyIRI = IRI.create("http://cin.ufpe.br/nmf/vocabularyOntology");
		
		File fileTest = new File("C:\\Users\\Natália\\workspace\\wordonto\\ontologiaDeEntrada.owl");
		OWLAccess ontology = new OWLAccess(ontologyIRI, fileTest);


		// ----------------- PARA SUBSTANTIVOS ----------------- 


		Vocabulary listaDeSubstantivos = new Vocabulary("PalavrasFrequentesSubstantivos.txt");
		int size = listaDeSubstantivos.getSize();

		PrintWriter writerFound = new PrintWriter("ArquivoSaidaSubst.txt", "UTF-8");
		PrintWriter writerNotFound = new PrintWriter("PalavrasNaoEncontradasSubst.txt", "UTF-8");


		for (int i=0; i < size; i++){

			String word = listaDeSubstantivos.VocabularyList().get(i);

			IIndexWord indexWord = wordNetAccess.getiDictionary().getIndexWord(word, POS.NOUN);

			if (indexWord != null){

				IWordID wordID = indexWord.getWordIDs().get(0); 

				Sense sense1 = wordNetAccess.getFirstSense(word, POS.NOUN); // PARA PEGAR APENAS UM SENTIDO (O PRIMEIRO!)

				if(sense1 != null){

					OWLClass wordClass = ontology.createClass(word);
					ontology.createAnnotation(wordClass, "*** Tipo de Anotação ***");


					writerFound.println( "\n  Label: " + sense1.getLabel() );
					writerFound.println( "  Description: " + sense1.getDescription() );
					writerFound.println( "  Related Terms: " + sense1.getRelatedTerms());
					writerFound.println( "  ISynset: " + sense1.getiSynset() );
					writerFound.println( "  ID: " + wordID );
					writerFound.println( "  Gloss: " + sense1.getiSynset().getGloss() );
					writerFound.println( "  Synsets Relacionados: " + sense1.getiSynset().getRelatedSynsets() );
					writerFound.println( "  Relações com Synsets: " + wordNetAccess.getRelations(sense1.getiSynset()) );
					writerFound.println( "-----------------------------------------------");

					Category root = wordNetAccess.getHypernyms( sense1.getiSynset() );

					if( root != null ) {

						root = Utils.getTopFatherCategory( root );

						Utils.printHierarchy( root, -1 );

					}

				}




				// -------------- PARA PEGAR TODOS OS SENTIDOS DE UMA PALAVRA [INÍCIO] --------------

				/*List<Sense> senses = wordNetAccess.searchSenses(word, POS.NOUN);

				if( senses != null ) {

					for(Sense sense: senses) {

						writerFound.println( "\n  Label: " + sense.getLabel() );
						writerFound.println( "  Description: " + sense.getDescription() );
						writerFound.println( "  Related Terms: " + sense.getRelatedTerms());
						writerFound.println( "  ISynset: " + sense.getiSynset() );
						writerFound.println( "  ID: " + wordID );
						writerFound.println( "  Gloss: " + sense.getiSynset().getGloss() );
						writerFound.println( "  Synsets Relacionados: " + sense.getiSynset().getRelatedSynsets() );
						writerFound.println( "  Relações com Synsets: " + wordNetAccess.getRelations(sense.getiSynset()) );
						writerFound.println( "-----------------------------------------------");


						Category root = wordNetAccess.getHypernyms( sense.getiSynset() );

						if( root != null ) {

							root = Utils.getTopFatherCategory( root );

							Utils.printHierarchy( root, -1 );

						}
					}
				}*/
				// -------------- PARA PEGAR TODOS OS SENTIDOS DE UMA PALAVRA [FIM] --------------



			} else {
				writerNotFound.println( word );
			}

		}

		writerFound.close();
		writerNotFound.close();


		//---------------------------------------------------------------------------------------








		OWLClass dog = ontology.createClass("dog");

		OWLClass cachorro = ontology.createClass("cachorro");

		OWLClass rato = ontology.createClass("rato");

		OWLClass gato = ontology.createClass("gato");

		OWLClass kitty = ontology.createClass("kitty");

		OWLClass puppy = ontology.createClass("puppy");

		OWLClass ratito = ontology.createClass("ratito");



		ontology.createObjectProperties(ObjectPropertyType.IS_SIMILAR_TO, dog, cachorro); //essa propriedade é simétrica?? (fiz no Protégé)

		ontology.createObjectProperties(ObjectPropertyType.IS_ANTONYM_OF, gato, rato);

		ontology.createObjectProperties(ObjectPropertyType.HAS_HYPERNYM, kitty, gato);

		ontology.createObjectProperties(ObjectPropertyType.HAS_HYPERNYM, puppy, dog);

		ontology.createObjectProperties(ObjectPropertyType.HAS_HYPONYM, rato, ratito);

		//ontology.createAnnotation(dog, "**********Tipo de Anotação**********");


		/*
		ontology.createDataProperties(x, DataPropertyType.HAS_DESCRIPTION, "cannis familiaris");
		ontology.createDataProperties(y, DataPropertyType.HAS_DESCRIPTION, "cannis");
		 */

		System.out.println(ontology.getOntology().toString());

		ontology.saveOntology();


	}

}

