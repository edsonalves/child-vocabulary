package WordOntoMain;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Vocabulary {
	public ArrayList<String> VocabularyList;
	private Scanner scanner;

	public Vocabulary(String Arquivo) {
		VocabularyList = new ArrayList<String>(); //cria novo arrayList vazio
		try {
			scanner = new Scanner(new File(Arquivo)); //abre o leitor do arquivo

			while(scanner.hasNextLine()) { // percorre o arquivo
				String word = scanner.nextLine(); //atribui a word o conteúdo da próxima linha
				VocabularyList.add(word.toUpperCase()); //coloca word em uppercase

			}	

		} catch (FileNotFoundException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			scanner.close(); //fecha o arquivo
		}
	}

	
	public boolean search(String word){ //pesquisa a existência de word no ArrayList
		
		int i;
		int size = VocabularyList.size(); //tamanho do ArrayList
		String newWord = word.toUpperCase(); //coloca word em uppercase para facilitar a comparação
		
		for(i=0 ; i < size; i++){ //percorre o ArrayList comparando word com o elemento de cada posição
			
			if(newWord.compareTo(VocabularyList.get(i)) == 0){
				return true;
			}  
		}
		return false;
	}
	
	public ArrayList<String> VocabularyList(){
		
		return VocabularyList;
	}
	
	public int getSize(){
		
		int size = this.VocabularyList.size(); 
		
		return size;
	}

}
