package org.blastall.load.main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.blastall.load.utils.InsertBlastRequest;

public class InsertFromFasta {

	public void readFile(String path) throws Throwable{
		
		// Things we will need
		FileInputStream fileInputStream = null;
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		InsertBlastRequest insert = null;
		String line = null;

		
		try{
			
			// Instantiate
			insert = new InsertBlastRequest();
			fileInputStream = new FileInputStream(path);
			dataInputStream = new DataInputStream(fileInputStream);
			bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream), 10000);
			line = "";
			
			// While we have lines to read, insert them
			while ((line = bufferedReader.readLine()) != null)   {
				String sequence = bufferedReader.readLine();
				insert.insert(Integer.parseInt(line.substring(1)), sequence);
				
			}
						
		}catch (Throwable t){
			
			System.out.println("Failed to process: " + path + "\n" + t);
			
		} finally {
			
			// dont forget to close the buffers and streams
			try{
				
				bufferedReader.close();
				dataInputStream.close();
				fileInputStream.close();
				
			}catch(Throwable close){
				System.out.println("Failed to close readers/streams: " + close);
			}
		}
	}

}
