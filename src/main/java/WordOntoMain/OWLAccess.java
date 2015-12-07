package WordOntoMain;

import java.io.File;
import java.io.IOException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class OWLAccess {
	OWLOntology ontology;
	IRI ontologyIRI;
	OWLOntologyManager manager;


	// CONSTRUTOR

	public OWLAccess(IRI ontologyIRI, File file) throws OWLOntologyCreationException{

		this.ontologyIRI = ontologyIRI;

		this.manager = OWLManager.createOWLOntologyManager();
		
		this.ontology = manager.loadOntologyFromOntologyDocument(file);

	}

	// MÉTODOS

	public OWLClass createClass(String word) throws OWLOntologyStorageException{

		OWLDataFactory factory = manager.getOWLDataFactory();
		
		OWLClass c = factory.getOWLClass(IRI.create(ontologyIRI + "#" + word));
		OWLAxiom declareC = factory.getOWLDeclarationAxiom(c);
		
		manager.addAxiom(ontology, declareC);
		
		//ontology.saveOntology();
		
		return c;
	}


	public void createEquivalentClass(OWLClass class1, OWLClass class2){ 
		
		//TORNA DUAS CLASSES EQUIVALENTES
		
		OWLDataFactory factory = manager.getOWLDataFactory();
				
		OWLEquivalentClassesAxiom equivalentClass = factory.getOWLEquivalentClassesAxiom(class1, class2);		
		manager.addAxiom(ontology, equivalentClass);
	}
	
	
	public void createObjectProperties(Pointer objProperty, OWLClass class1, OWLClass class2){
		// CRIA RELAÇÃO ENTRE DUAS CLASSES 

		OWLDataFactory factory = manager.getOWLDataFactory();
		
		if (objProperty == Pointer.IS_SIMILAR_TO){

			OWLObjectProperty isSimilarTo = factory.getOWLObjectProperty(IRI.create("http://cin.ufpe.br/nmf/vocabularyOntology#isSimilarTo"));

			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(isSimilarTo, class2);			
			OWLEquivalentClassesAxiom equivalentClassSimilar = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			
			manager.addAxiom(ontology, equivalentClassSimilar);


		} else if (objProperty == Pointer.IS_ANTONYM_OF){

			OWLObjectProperty isAntonymOf = factory.getOWLObjectProperty(IRI.create("http://cin.ufpe.br/nmf/vocabularyOntology#isAntonymOf"));

			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(isAntonymOf, class2);
			
			OWLEquivalentClassesAxiom equivalentClassAntonym = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			
			manager.addAxiom(ontology, equivalentClassAntonym);
			

		} else if (objProperty == Pointer.HAS_HYPERNYM){

			OWLSubClassOfAxiom isSubClassOf = factory.getOWLSubClassOfAxiom(class1, class2);

			manager.addAxiom(ontology, isSubClassOf);
			
		} else if (objProperty == Pointer.HAS_HYPONYM){
			
			OWLSubClassOfAxiom isSubClassOf = factory.getOWLSubClassOfAxiom(class2, class1);

			manager.addAxiom(ontology, isSubClassOf);
		}

	}


	public void createDataProperties(OWLClass classWord, DataPropertyType dataPropType, String value){
		// CRIA RELAÇÃO ENTRE CLASSE E LITERAL

		OWLDataFactory factory = manager.getOWLDataFactory();


		if (dataPropType == DataPropertyType.HAS_DESCRIPTION){

			OWLDataProperty hasDescription = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#hasDescription"));

			OWLDatatype dt = factory.getOWLDatatype(IRI.create(ontologyIRI + "#" + value));

			OWLDataPropertyDomainAxiom domainAxiom = factory.getOWLDataPropertyDomainAxiom(hasDescription, classWord);
			OWLDataPropertyRangeAxiom rangeAxiom = factory.getOWLDataPropertyRangeAxiom(hasDescription, dt);

			manager.addAxiom(ontology, domainAxiom);
			manager.addAxiom(ontology, rangeAxiom);

		} else if (dataPropType == DataPropertyType.HAS_GLOSS){

			OWLDataProperty hasGloss = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#hasGloss"));

			OWLDatatype dt = factory.getOWLDatatype(IRI.create(ontologyIRI + "#" + value));

			OWLDataPropertyDomainAxiom domainAxiom = factory.getOWLDataPropertyDomainAxiom(hasGloss, classWord);
			OWLDataPropertyRangeAxiom rangeAxiom = factory.getOWLDataPropertyRangeAxiom(hasGloss, dt);

			manager.addAxiom(ontology, domainAxiom);
			manager.addAxiom(ontology, rangeAxiom);

		} else if (dataPropType == DataPropertyType.HAS_RELATED_TERM){

			OWLDataProperty hasRelatedTerm = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#hasRelatedTerm"));

			OWLDatatype dt = factory.getOWLDatatype(IRI.create(ontologyIRI + "#" + value));

			OWLDataPropertyDomainAxiom domainAxiom = factory.getOWLDataPropertyDomainAxiom(hasRelatedTerm, classWord);
			OWLDataPropertyRangeAxiom rangeAxiom = factory.getOWLDataPropertyRangeAxiom(hasRelatedTerm, dt);

			manager.addAxiom(ontology, domainAxiom);
			manager.addAxiom(ontology, rangeAxiom);

		} else {

			OWLDataProperty hasObjectProperty = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#hasObjectProperty"));

			OWLDatatype dt = factory.getOWLDatatype(IRI.create(ontologyIRI + "#" + value));

			OWLDataPropertyDomainAxiom domainAxiom = factory.getOWLDataPropertyDomainAxiom(hasObjectProperty, classWord);
			OWLDataPropertyRangeAxiom rangeAxiom = factory.getOWLDataPropertyRangeAxiom(hasObjectProperty, dt);

			manager.addAxiom(ontology, domainAxiom);
			manager.addAxiom(ontology, rangeAxiom);

		}



	}

	
	public void createAnnotation(OWLClass classe, String texto){
		
		OWLDataFactory factory = manager.getOWLDataFactory();
		
		OWLClass classeClass = factory.getOWLClass(IRI.create(ontologyIRI + "#" + classe));
		
		OWLAnnotation commentAnno = factory.getOWLAnnotation(factory.getRDFSComment(), factory.getOWLLiteral(texto, "en"));
		
		OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(classeClass.getIRI(),commentAnno);
		
		manager.applyChange(new AddAxiom(ontology, ax));
		
		
	}
	

	public void saveOntology () throws IOException, OWLOntologyStorageException, OWLOntologyCreationException{

		OWLOntologyManager manager = OWLManager.createConcurrentOWLOntologyManager();

		File file = new File("C:\\Users\\Natália\\workspace\\wordonto\\ontologiaDeEntrada.owl");
		
		OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();

		manager.saveOntology(ontology, owlxmlFormat, IRI.create(file.toURI()));
		
		System.out.println("Axiomas: " + ontology.getAxiomCount());

	}


	public OWLOntology getOntology() {
		return ontology;
	}



	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}



	public IRI getOntologyIRI() {
		return ontologyIRI;
	}



	public void setOntologyIRI(IRI ontologyIRI) {
		this.ontologyIRI = ontologyIRI;
	}



	public OWLOntologyManager getManager() {
		return manager;
	}



	
	
	public void setManager(OWLOntologyManager manager) {
		this.manager = manager;
	}

}



