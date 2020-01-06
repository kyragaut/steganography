/**
 *
 * @author Kyra GauthierDickey
 * Project 4
 * February 26, 2018
 * This part of the program is responsible for encoding and decoding the message
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class Steganography {
	
	public static String decodeMessage(File inputFile) throws SecretMessageException
	{
		//create a string buffer to append the characters together
		StringBuffer message = new StringBuffer();
		
		//open file for reading
		try
		{
			RandomAccessFile decodee = new RandomAccessFile(inputFile, "r");
			decodee.seek(50);
			int lengthOfMessage = decodee.readInt();
			System.out.println("Message length is " + lengthOfMessage);
			

			//get the characters of the secret message
			for(int i = 0; i < lengthOfMessage; ++i)
			{
				char nextChar = decodee.readChar();
				message.append(nextChar);
				if(i+1 < lengthOfMessage)
				{
					long nextPosition = decodee.readLong();
					decodee.seek(nextPosition);
				}
			}
		}
		//exceptions
		catch(FileNotFoundException e)
		{
			throw new SecretMessageException("File not found when decoding");
		}
		catch(IOException e)
		{
			throw new SecretMessageException(e.toString());
		}
		
		return message.toString();
	}
	
	public static void encodeMessage(File inputFile, File outputFile, String message) throws NotEnoughSpaceException, SecretMessageException
	{
		try
		{
			Path path = Files.copy(inputFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			RandomAccessFile encodedMessage = new RandomAccessFile(path.toString(), "rw");
			
			//write length at byte 50
			encodedMessage.seek(50);
			encodedMessage.writeInt(message.length());
			
			//get locations for encoding
			long[] locations = getDataLocations(54, encodedMessage.length() - 54, message.length());
			
			//encode the message 
			for(int i = 0; i < locations.length; ++i) 
			{
				encodedMessage.seek(locations[i]);
				encodedMessage.writeChar(message.charAt(i));
				if(i+1 < locations.length)
					{
					encodedMessage.writeLong(locations[i+1]);
					}
			}
		}
		catch(IOException e)
		{
			throw new SecretMessageException("IOException when encoding");
		}
		
	}
	
	private static long[] getDataLocations(long start, long stop, int numLocations) throws NotEnoughSpaceException
	{
		
		long length = stop - start;
		if(numLocations > length)
		{
			throw new NotEnoughSpaceException();
		}
		long[] location = new long[numLocations];
		
		//fill array with locations for my characters
		for(int i = 0; i < location.length; ++i)
		{
			location[i] = i*(length/numLocations) + start;
		}

		return location;
	}


}
