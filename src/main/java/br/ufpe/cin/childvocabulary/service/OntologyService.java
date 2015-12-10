package br.ufpe.cin.childvocabulary.service;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
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

import br.ufpe.cin.childvocabulary.util.PropertiesUtil;
import edu.mit.jwi.item.Pointer;

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
		word = word.replace(" ", "_");
		OWLClass c = factory.getOWLClass(IRI.create(ontologyIRI + "#" + word));
		OWLAxiom declareC = factory.getOWLDeclarationAxiom(c);
		manager.addAxiom(ontology, declareC);
		return c;
	}

	public OWLDataFactory getFactory() {
		return factory;
	}

	public void createAnnotation(OWLClass classe, String annotation){
		OWLAnnotation commentAnno = factory.getOWLAnnotation(factory.getRDFSComment(), factory.getOWLLiteral(annotation, "en"));
		OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(classe.getIRI(), commentAnno);
		manager.applyChange(new AddAxiom(ontology, ax));	
	}
	
	public void createObjectProperties(Pointer pointer, OWLClass class1, OWLClass class2){		

		if (pointer == null){
			OWLObjectProperty isSimilarTo = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#sameSynset"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(isSimilarTo, class2);			
			OWLEquivalentClassesAxiom equivalentClassSimilar = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassSimilar);
		}
		
		if (pointer == Pointer.SIMILAR_TO){	
			OWLObjectProperty isSimilarTo = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#isSimilarTo"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(isSimilarTo, class2);			
			OWLEquivalentClassesAxiom equivalentClassSimilar = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassSimilar);

		} else if (pointer == Pointer.ANTONYM){
			OWLObjectProperty isAntonymOf = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#isAntonymOf"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(isAntonymOf, class2);
			OWLEquivalentClassesAxiom equivalentClassAntonym = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassAntonym);

		} else if (pointer == Pointer.HYPERNYM){
			OWLSubClassOfAxiom isSubClassOf = factory.getOWLSubClassOfAxiom(class1, class2);
			manager.addAxiom(ontology, isSubClassOf);

		} else if (pointer == Pointer.HYPONYM){
			OWLSubClassOfAxiom isSubClassOf = factory.getOWLSubClassOfAxiom(class2, class1);
			manager.addAxiom(ontology, isSubClassOf);

		} else if (pointer == Pointer.ALSO_SEE){
			OWLObjectProperty alsoSee = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#alsoSee"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(alsoSee, class2);
			OWLEquivalentClassesAxiom equivalentClassAlsoSee = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassAlsoSee);

		} else if (pointer == Pointer.CAUSE){
			OWLObjectProperty cause = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#cause"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(cause, class2);
			OWLEquivalentClassesAxiom equivalentClassCause = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassCause);

		} else if (pointer == Pointer.VERB_GROUP){
			OWLObjectProperty verbGoup = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#verbGoup"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(verbGoup, class2);
			OWLEquivalentClassesAxiom equivalentClassVerbGoup = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassVerbGoup);

		} else if (pointer == Pointer.DERIVATIONALLY_RELATED){
			OWLObjectProperty derivationallyRelated = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#derivationallyRelated"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(derivationallyRelated, class2);
			OWLEquivalentClassesAxiom equivalentClassDerivationallyRelated = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassDerivationallyRelated);

		} else if (pointer == Pointer.ATTRIBUTE){
			OWLObjectProperty attibute = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#attribute"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(attibute, class2);
			OWLEquivalentClassesAxiom equivalentClassAttibute = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassAttibute);

		} else if (pointer == Pointer.ENTAILMENT){
			OWLObjectProperty entailment = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#entailment"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(entailment, class2);
			OWLEquivalentClassesAxiom equivalentClassEntailment = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassEntailment);
			
		} else if (pointer == Pointer.PERTAINYM){
			OWLObjectProperty pertainym = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#pertainym"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(pertainym, class2);
			OWLEquivalentClassesAxiom equivalentClassPertainym = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassPertainym);
			
		} else if (pointer == Pointer.PARTICIPLE){
			OWLObjectProperty participle = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#participle"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(participle, class2);
			OWLEquivalentClassesAxiom equivalentClassParticiple = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassParticiple);
			
		} else if (pointer == Pointer.DERIVED_FROM_ADJ){
			OWLObjectProperty derivedFromAdj = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#derivedFromAdj"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(derivedFromAdj, class2);
			OWLEquivalentClassesAxiom equivalentClassDerivedFromAdj = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassDerivedFromAdj);
			
		} else if (pointer == Pointer.HOLONYM_MEMBER){
			OWLObjectProperty holonymMember = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#holonymMember"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(holonymMember, class2);
			OWLEquivalentClassesAxiom equivalentClassHolonymMember = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassHolonymMember);
			
		} else if (pointer == Pointer.HOLONYM_PART){
			OWLObjectProperty holonymPart = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#holonymPart"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(holonymPart, class2);
			OWLEquivalentClassesAxiom equivalentClassHolonymPart = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassHolonymPart);
			
		} else if (pointer == Pointer.HOLONYM_SUBSTANCE){
			OWLObjectProperty holonymSubstance = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#holonymSubstance"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(holonymSubstance, class2);
			OWLEquivalentClassesAxiom equivalentClassHolonymSubstance = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassHolonymSubstance);
			
		} else if (pointer == Pointer.MERONYM_MEMBER){
			OWLObjectProperty meronymMember = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#meronymMember"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(meronymMember, class2);
			OWLEquivalentClassesAxiom equivalentClassMeronymMember = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassMeronymMember);
			
		} else if (pointer == Pointer.MERONYM_PART){
			OWLObjectProperty meronymPart = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#meronymPart"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(meronymPart, class2);
			OWLEquivalentClassesAxiom equivalentClassMeronymPart = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassMeronymPart);
			
		} else if (pointer == Pointer.MERONYM_SUBSTANCE){
			OWLObjectProperty meronymSubst = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#meronymSubst"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(meronymSubst, class2);
			OWLEquivalentClassesAxiom equivalentClassMeronymSubst = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassMeronymSubst);
			
		} else if (pointer == Pointer.REGION){
			OWLObjectProperty meronymRegion = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#meronymRegion"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(meronymRegion, class2);
			OWLEquivalentClassesAxiom equivalentClassRegion = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassRegion);
			
		} else if (pointer == Pointer.REGION_MEMBER){
			OWLObjectProperty meronymRegionMember = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#meronymRegionMember"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(meronymRegionMember, class2);
			OWLEquivalentClassesAxiom equivalentClassRegionMember = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassRegionMember);
			
		} else if (pointer == Pointer.TOPIC){
			OWLObjectProperty topic = factory.getOWLObjectProperty(IRI.create(PropertiesUtil.getOntologyIri()+"#topic"));
			OWLObjectSomeValuesFrom ligacao = factory.getOWLObjectSomeValuesFrom(topic, class2);
			OWLEquivalentClassesAxiom equivalentClassTopic = factory.getOWLEquivalentClassesAxiom(class1, ligacao);
			manager.addAxiom(ontology, equivalentClassTopic);
			
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
