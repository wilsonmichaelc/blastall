package org.blastall.load.main;

import java.io.File;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Throwable{

		InsertFromFasta insertFromFasta = null;
		String yourPath = "not provided";
		Scanner kb = null;
		File f = null; 
		boolean fileExists = false;
		
		try{

			kb = new Scanner(System.in);

			while(!yourPath.equals("-1") && fileExists == false){
				
				System.out.println("Usage: BlastAll_LoadFasta.jar /full/path/to/file.fasta \n");
				
				yourPath = kb.nextLine();
				
				f = new File(yourPath);
				
				if(!f.exists()){ 
					System.out.println("Could not find: " + yourPath);
				} else {
					fileExists = true;
				}
				
			}

		} catch(Throwable t){

			throw t;

		}

		try {

			insertFromFasta = new InsertFromFasta();
			insertFromFasta.readFile(yourPath);

		} catch (Throwable t){

			System.out.println("Failed to load fasta: \n" + t);
			throw t;
		}

	}

}
