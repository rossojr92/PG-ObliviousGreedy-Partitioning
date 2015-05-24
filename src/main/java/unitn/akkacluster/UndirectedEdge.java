package unitn.akkacluster;

import java.io.Serializable;

public class UndirectedEdge implements Serializable
{
	private static final long serialVersionUID = 1L;

	// #region Fields
	
	private int m_First;
	private int m_Second;
	
	// #
	
	// #region Constructors
	
	public UndirectedEdge(int i_First, int i_Second)
	{
		m_First = i_First;
		m_Second = i_Second;
	}
	
	public UndirectedEdge()
	{
		this(-1, -1);
	}
	
	// #
	
	// #region Getters and Setters
	
	public int GetFirst()
	{
		return m_First;
	}
	
	public int GetSecond()
	{
		return m_Second;
	}
	
	public void SetFirst(int i_Id)
	{
		m_First = i_Id;
	}
	
	public void SetSecond(int i_Id)
	{
		m_Second = i_Id;
	}
	
	// #
	
	public boolean equals(UndirectedEdge i_Other)
	{
		return ((m_First == i_Other.GetFirst() && m_Second == i_Other.GetSecond()) || (m_First == i_Other.GetSecond() && m_Second == i_Other.GetFirst()));
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return ((obj instanceof UndirectedEdge) && (this.equals((UndirectedEdge) obj)));
	}
	
	@Override
	public int hashCode()
	{	
		int hash = 7;
		
		hash *= 7 + m_First;
		hash *= 7 + m_Second;
		
		return hash;
	}
	
	// #region Write and Read
	
	public void WriteLn(FileWriter i_Writer)
	{
		if(i_Writer != null)
		{
			i_Writer.PrintLn(m_First + " " + m_Second);
		}
	}
	
	public void Write(FileWriter i_Writer)
	{
		if(i_Writer != null)
		{
			i_Writer.Print(m_First + " " + m_Second);
		}
	}
	
	public static UndirectedEdge Read(FileReader i_Reader)
	{
		if(i_Reader != null)
		{
			int first = i_Reader.NextInt();
			int second = i_Reader.NextInt();
			
			UndirectedEdge edge = new UndirectedEdge(first, second);
			
			return edge;
		}
		
		return null;
	}
	
	// #

}
