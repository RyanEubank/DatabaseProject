import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;


public class Cleaner {

	public static void main(String[] args) throws IOException {
		
		Pattern p=Pattern.compile("^([^\t]+)\t([^\t]+)\t([^\t]+)\t([^\t]+)\t([^\t]+)\t([^\t]+)\t([0-9]+)");
		Matcher m;
		
		try
		{
			//Opens the file with the appropriate file path
			FileReader myObj =new FileReader("C:\\Users\\blueb\\eclipse-workspace\\Hello\\src\\CleanedLines.txt");
			//Attached a buffer reader to the file so we can read in the lines more easily
			BufferedReader myReader= new BufferedReader(myObj);
			
			//initializing strings so eclipse doesn't yell at me later
			String currentLine="";
			String ISBN10="";
			String ISBN13="";
			String Title="";
			String Author="";
			String Cover="";
			String Publisher="";
			String Pages="";
			
			//Reads in the entire File line by line stops at End of File
			while((currentLine=myReader.readLine())!=null) {
				
				//attempts to match the regex pattern to the current line
				m=p.matcher(currentLine);
				
				//if our regex matches we can break the lines into the appropriate sections
				if(m.matches())
				{
					ISBN10=m.group(1);
					ISBN13=m.group(2);
					Title=m.group(3);
					Author=m.group(4);
					Cover=m.group(5);
					Publisher=m.group(6);
					Pages=m.group(7);
				}
		
			}
			myReader.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error file not found");
			e.printStackTrace();
		}
	

	}

}
