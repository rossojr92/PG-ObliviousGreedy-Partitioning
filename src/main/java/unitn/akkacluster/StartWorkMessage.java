package unitn.akkacluster;

import java.util.HashSet;
import java.util.Set;

public class StartWorkMessage extends Message
{
	private static final long serialVersionUID = 1L;

	private int m_NumberOfPartitions;
	
	private Set<UndirectedEdge> m_Edges = null;
	
	public int GetNumberOfPartitions()
	{
		return m_NumberOfPartitions;
	}
	
	public Set<UndirectedEdge> GetEdges()
	{
		return m_Edges;
	}
	
	public void AddEdge(UndirectedEdge i_Edge)
	{
		m_Edges.add(i_Edge);
	}
	
	public void AddEdge(int i_First, int i_Second)
	{
		AddEdge(new UndirectedEdge(i_First, i_Second));
	}
	
	public StartWorkMessage(int i_NumberOfPartitions)
	{
		m_NumberOfPartitions = i_NumberOfPartitions;
		
		m_Edges = new HashSet<UndirectedEdge>();
	}
}
