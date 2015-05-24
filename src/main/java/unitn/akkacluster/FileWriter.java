package unitn.akkacluster;

import java.io.BufferedWriter;
import java.io.PrintWriter;

public class FileWriter 
{
	private PrintWriter _Writer = null;
	private String _FilePath = "";
	
	private boolean _bAppend = false;
	
	public FileWriter(final String i_FilePath, boolean i_bAppend)
	{
		_FilePath = i_FilePath;
		_bAppend = i_bAppend;
		
		InitializeWriter();
	}
	
	public FileWriter(final String i_FilePath)
	{
		this(i_FilePath, false);
	}
	

	private boolean InitializeWriter()
	{	
		try
		{
			_Writer = new PrintWriter( new BufferedWriter( new java.io.FileWriter(_FilePath, _bAppend) ) );
			
			return true;
		}
		catch(Exception e)
		{
			Debug.LogError("Error while creating file.");
			return false;
		}
	}
	
	public boolean IsReadyToWrite()
	{
		return (_Writer != null);
	}
	
	public void NewLine()
	{
		if(_Writer != null)
		{
			_Writer.println();
		}
	}
	
	public void PrintLn(int i)
	{
		if(_Writer != null)
		{
			_Writer.println(i);
		}
	}
	
	public void PrintLn(String s)
	{
		if(_Writer != null)
		{
			_Writer.println(s);
		}
	}
	
	public void Print(int i)
	{
		if(_Writer != null)
		{
			_Writer.print(i);
		}
	}
	
	public void Print(String s)
	{
		if(_Writer != null)
		{
			_Writer.print(s);
		}
	}
	
	public void Print(Object obj)
	{
		if(_Writer != null)
		{
			_Writer.print(obj.toString());
		}
	}
	
	public void PrintLn(Object obj)
	{
		if(_Writer != null)
		{
			_Writer.println(obj.toString());
		}
	}
	
	public void Close()
	{
		if(_Writer != null)
		{
			_Writer.close();
		}
	}
	
	@Override
	public void finalize()
	{
		if(_Writer != null)
		{
			_Writer.close();
		}
	}
}
