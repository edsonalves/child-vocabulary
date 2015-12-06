package br.ufpe.cin.childvocabulary.service;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import WordOntoMain.ObjectPropertyType;
import br.ufpe.cin.childvocabulary.util.PropertiesUtil;

public class OntologyService {
	private OWLOntology ontology;
	private OWLOntologyManager manager;
	private OWLDataFactory factory;
	private IRI ontologyIRI;
	private File baseOntology;

	public OntologyService(){
		this.ontologyIRI = IRI.create(PropertiesUtil.getOntologyIri());
		this.manager =  OWLManager.createOWLOntologyManager();
		this.factory = manager.getOWLDataFactory();
		this.baseOntology = new File(PropertiesUtil.getOntologyBaseOntology());
		try {
			this.ontology = manager.loadOntologyFromOntologyDocument(baseOntology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}
	
	public OWLClass createClass(String word) throws OWLOntologyStorageException{
		OWLClass c = factory.getOWLClass(IRI.create(ontologyIRI + "#" + word));
		OWLAxiom declareC = factory.getOWLDeclarationAxiom(c);
		manager.addAxiom(ontology, declareC);
		return c;
	}
	
	public void createObjectProperties(ObjectPropertyType objProperty, OWLClass class1, OWLClass class2){		
		
		if (objProperty == ObjectPropertyType.IS_SIMILAR_TO){	
			OWLObjectProperty isSimilarTo = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#isSimilarTo"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(isSimilarTo, class2);			
			OWLEquivalentClassesAxiom equivalentClassSimilar = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassSimilar);
			
		} else if (objProperty == ObjectPropertyType.IS_ANTONYM_OF){
			OWLObjectProperty isAntonymOf = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#isAntonymOf"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(isAntonymOf, class2);
			OWLEquivalentClassesAxiom equivalentClassAntonym = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassAntonym);

		} else if (objProperty == ObjectPropertyType.HAS_HYPERNYM){
			OWLSubClassOfAxiom isSubClassOf = factory.getOWLSubClassOfAxiom(class1, class2);
			manager.addAxiom(ontology, isSubClassOf);
			
		} else if (objProperty == ObjectPropertyType.HAS_HYPONYM){
			OWLSubClassOfAxiom isSubClassOf = factory.getOWLSubClassOfAxiom(class2, class1);
			manager.addAxiom(ontology, isSubClassOf);
		}
	}
	
	public IRI getOntologyIRI() {
		return ontologyIRI;
	}

	public void setOntologyIRI(IRI ontologyIRI) {
		this.ontologyIRI = ontologyIRI;
	}

	public File getBaseOntology() {
		return baseOntology;
	}

	public void setBaseOntology(File baseOntology) {
		this.baseOntology = baseOntology;
	}

	public OWLOntologyManager getManager() {
		return manager;
	}

	public void setManager(OWLOntologyManager manager) {
		this.manager = manager;
	}

	public OWLOntology getOntology() {
		return ontology;
	}

	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}
}
