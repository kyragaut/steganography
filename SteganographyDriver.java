/**
 *
 * @author Kyra GauthierDickey
 * Project 4
 * February 26, 2018
 * This part of the program is responsible for running the encoding and decoding
 * 
 */

import java.util.Scanner;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.io.FileInputStream;
import java.io.FileNotFoundException; 

public class SteganographyDriver {

	public static void main(String[] args) {
		
		//get user input for secret message
		System.out.print("Enter secret message!: ");
		Scanner keyboard = new Scanner(System.in);
		String message = keyboard.nextLine();
		System.out.println("You entered: '" + message + "'");
		
		//read in file
		File file = new File("NoSecretMessage.wav");
		//make sure file exists
		if(file.exists() == false)
		{
			System.out.println("File not found.");
			return;
		}
		
		try
		{
			System.out.println("Encoding the file...");
			Steganography.encodeMessage(file, new File("SecretMessage2.wav"), message);
			System.out.println("Decoding the file...");
			System.out.println(Steganography.decodeMessage(new File("SecretMessage2.wav")));
		}
		catch (SecretMessageException | NotEnoughSpaceException e) 
		{
			e.printStackTrace();
		}


		
		

	}

}
