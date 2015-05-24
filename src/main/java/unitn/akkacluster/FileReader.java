package unitn.akkacluster;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader 
{
	private String _FilePath = "";	
	private Scanner _Scanner = null;
	
	public FileReader(File i_File)
	{
		this(i_File.getPath());
	}
	
	public FileReader(final String i_FilePath)
	{
		_FilePath = i_FilePath;
	
		InitializeScanner();
	}
	
	private boolean InitializeScanner()
	{	
		try
		{
			_Scanner = new Scanner(new File(_FilePath));
			
			return true;
		}
		catch (FileNotFoundException e)
		{
			Debug.LogError("File '" + _FilePath + "' not found.");
		
			return false;
		}
	}
	
	public boolean IsReadyToRead()
	{
		return (_Scanner != null);
	}
	
	public boolean HasNext()
	{
		if(_Scanner != null)
		{
			return _Scanner.hasNext();
		}
		
		return false;
	}
	
	public boolean HasNextInt()
	{
		if(_Scanner != null)
		{
			return _Scanner.hasNextInt();
		}
		
		return false;
	}
	
	public boolean HasNextFloat()
	{
		if(_Scanner != null)
		{
			return _Scanner.hasNextFloat();
		}
		
		return false;
	}
	
	public boolean HasNextLine()
	{
		if(_Scanner != null)
		{
			return _Scanner.hasNextLine();
		}
		
		return false;
	}
	
	public int NextInt()
	{
		if(_Scanner != null)
		{
			return _Scanner.nextInt();
		}
		
		return 0;
	}
	
	public float NextFloat()
	{
		if(_Scanner != null)
		{
			return _Scanner.nextFloat();
		}
		
		return 0f;
	}
	
	public String NextString()
	{
		if(_Scanner != null)
		{
			return _Scanner.next();
		}
		
		return "";
	}
	
	public String NextLine()
	{
		if(_Scanner != null)
		{
			return _Scanner.nextLine();
		}
		
		return "";
	}
	
	public void Close()
	{
		if(_Scanner != null)
		{
			_Scanner.close();
		}
	}
	
	@Override
	public void finalize()
	{
		if(_Scanner != null)
		{
			_Scanner.close();
		}
	}
}