package unitn.akkacluster;

public class Vertex 
{
	private int m_Id;
	
	public Vertex(int i_Id)
	{
		m_Id = i_Id;
	}
	
	public int GetId()
	{
		return m_Id;
	}
	
	public boolean equals(Vertex i_Other)
	{
		return (m_Id == i_Other.GetId());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return ((obj instanceof Vertex) && (m_Id == ((Vertex) obj).GetId()));
	}
	
	@Override
	public int hashCode()
	{
		return m_Id;
	}
	
	// #region Write and Read
	
	public void WriteLn(FileWriter i_Writer)
	{
		if(i_Writer != null)
		{
			i_Writer.PrintLn(m_Id);
		}
	}
	
	public void Write(FileWriter i_Writer)
	{
		if(i_Writer != null)
		{
			i_Writer.Print(m_Id);
		}
	}
	
	public static Vertex Read(FileReader i_Reader)
	{
		if(i_Reader != null)
		{
			int id = i_Reader.NextInt();
		
			Vertex v = new Vertex(id);
			
			return v;
		}
		
		return null;
	}
	
	//#
}
