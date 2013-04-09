package tools;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dictionary {
	

	ArrayList<String> verbs;
	
	public Dictionary(){
		verbs = new ArrayList<String>();
		getList("http://norvig.com/ngrams/word.list");
	}
	
	private void getList(String url){

		URL currentUrl;
		try{
			currentUrl = new URL(url); 
			InputStreamReader isr = new InputStreamReader(currentUrl.openStream());
			BufferedReader br = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();

			while(br.readLine()!=null){
				sb.append(br.readLine()+"\n");
			}
			String content = sb.toString();
			Scanner scanner = new Scanner(content);
			
			while (scanner.hasNextLine()) {
				
				String line = scanner.nextLine();
//				String regex = "(?:ify)$"; //regex that matches an URL
//				Pattern p = Pattern.compile(regex);
//				Matcher m = p.matcher(line);	
//				verbs.add(line);
//				while(m.find()){ 
//					
//					verbs.add(m.group());
//					
//					}
			if(	line.endsWith("ify")) verbs.add(line+"er");
				
			}
			scanner.close();
			isr.close();
			br.close();
		}catch(Exception e){
			
		e.printStackTrace();

		}
		
	}
	
	public void writeToFile(){

		try{
			FileWriter fw = new FileWriter(new File("verbs")); 
			BufferedWriter out = new BufferedWriter(fw);

					for (String wp : verbs) {
						out.write(wp);		//writes the URL
						out.newLine();
					}

			out.close(); 
		}
		catch(Exception e){
			//e.printStackTrace();
		}
	}
	public String getRandomVerber(){
		Random r = new Random();
		if(verbs.isEmpty()) return "Dictionary is empty";
		return verbs.get(r.nextInt(verbs.size()));
	}
	
	public static void main(String[] args) {
		Dictionary d = new Dictionary();
		d.writeToFile();
		System.out.println(d.getRandomVerber());
	}
}
