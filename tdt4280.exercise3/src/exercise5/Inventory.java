package exercise5;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Inventory {
	List<Item> items;

	public Inventory(int size){

		items = new ArrayList<Item>();
		ArrayList<String> names = new ArrayList<String>();
		try{
			FileInputStream fstream = new FileInputStream("verbs");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				names.add(strLine);
			}
			in.close();
			fstream.close();
			br.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		for (int i = 0; i < size; i++) {
			items.add(new Item(names.get((int)(Math.random()*names.size())), (int)(Math.random()*names.size())));
		}

		//	for (Item it : items) {
		//		System.out.println(it.getName());
		//		System.out.println(it.getPrice());
		//	}


	}
	public static void main(String[] args) {
		new Inventory(10);
	}
}