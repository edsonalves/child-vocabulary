package br.ufpe.cin.childvocabulary.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	static Properties prop = new Properties();
	static InputStream input = null;

	public static String getWordNetDictionaryDict(){
		try {
			String filename = "application.properties";
			input = PropertiesUtil.class.getClassLoader().getResourceAsStream(filename);
			if(input==null){
				System.out.println("Unable to find " + filename);
				return null;
			}
			//load a properties file from class path, inside static method
			prop.load(input);
			return prop.getProperty("wordNet.dictionary.dict");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String getOntologyIri(){
		try {
			String filename = "application.properties";
			input = PropertiesUtil.class.getClassLoader().getResourceAsStream(filename);
			if(input==null){
				System.out.println("Unable to find " + filename);
				return null;
			}
			//load a properties file from class path, inside static method
			prop.load(input);
			return prop.getProperty("ontology.iri");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static String getOntologyBaseOntology(){
		try {
			String filename = "application.properties";
			input = PropertiesUtil.class.getClassLoader().getResourceAsStream(filename);
			if(input==null){
				System.out.println("Unable to find " + filename);
				return null;
			}
			//load a properties file from class path, inside static method
			prop.load(input);
			return prop.getProperty("ontology.base.ontology");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
}
