package unitn.akkacluster;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Machine 
{
	// #region Fields
	
	private Set<UndirectedEdge> _UndirectedEdges = null;
	private Set<Vertex> _Vertices = null;
		
	// #

	// #region Constructors
		
	public Machine()
	{
		_UndirectedEdges = new HashSet<UndirectedEdge>();
		_Vertices = new HashSet<Vertex>();
	}
		
	// #

	// #region Getters
		
	public Iterator<UndirectedEdge> GetEdgesIterator()
	{
		return _UndirectedEdges.iterator();
	}
		
	public Iterator<Vertex> GetVerticesIterator()
	{
		return _Vertices.iterator();
	}
		
	public Set<UndirectedEdge> GetEdges()
	{
		return _UndirectedEdges;
	}
		
	public Set<Vertex> GetVertices()
	{
		return _Vertices;
	}
		
	public int GetEdgesCount()
	{
		return _UndirectedEdges.size();
	}
		
	public int GetVerticesCount()
	{
		return _Vertices.size();
	}
		
	// #

	// #region Logic
		
	public boolean AddUndirectedEdge(Vertex i_First, Vertex i_Second)
	{
		return AddUndirectedEdge(i_First.GetId(), i_Second.GetId());
	}
		
	public boolean AddUndirectedEdge(UndirectedEdge i_UndirectedEdge)
	{
		boolean insert = (_UndirectedEdges.add(i_UndirectedEdge));
			
		AddVertex(i_UndirectedEdge.GetFirst());
		AddVertex(i_UndirectedEdge.GetSecond());
			
		return insert;
	}
		
	public boolean AddUndirectedEdge(int i_First, int i_Second)
	{
		UndirectedEdge e = new UndirectedEdge(i_First, i_Second);
			
		return AddUndirectedEdge(e);
	}
		
	public boolean Contains(Vertex i_Vertex)
	{
		return _Vertices.contains(i_Vertex);
	}
		
	public boolean Contains(int i_VertexId)
	{
		return Contains(new Vertex(i_VertexId));
	}
		
	public void Clear()
	{
		if(_Vertices != null)
		{
			_Vertices.clear();
		}
		else 
		{
			_Vertices = new HashSet<Vertex>();
		}
		
		if(_UndirectedEdges != null)
		{
			_UndirectedEdges.clear();
		}
		else 
		{
			_UndirectedEdges = new HashSet<UndirectedEdge>();
		}
	}
		
	private void AddVertex(Vertex i_Vertex)
	{
		if(!_Vertices.contains(i_Vertex))
		{
			_Vertices.add(i_Vertex);
		}
	}
		
	private void AddVertex(int i_Id)
	{
		Vertex v = new Vertex(i_Id);
		
		AddVertex(v);
	}
		
	// #
		
	// #region Debug
		
	public void LogInfo()
	{
		Debug.Log("[Machine] I have " + GetVerticesCount() + " vertices and " + GetEdgesCount() + " edges.");
	}
		
	// #
		
	// #region Write and Read
		
	// #region Write
		
	public void Write(FileWriter i_Writer)
	{
		if(i_Writer != null)
		{
			if(_UndirectedEdges != null && _Vertices != null)
			{
				i_Writer.PrintLn(_UndirectedEdges.size());
				
				Iterator<UndirectedEdge> it = _UndirectedEdges.iterator();
					
				for(int i = 0; i < _UndirectedEdges.size(); ++i)
				{
					UndirectedEdge currentEdge = it.next();
						
					currentEdge.WriteLn(i_Writer);
				}
			}
		}
	}
		
	public void Write(Map<Integer, Integer> i_Map, FileWriter i_Writer)
	{
		if(i_Writer != null)
		{
			if(_UndirectedEdges != null && _Vertices != null)
			{
				i_Writer.PrintLn(_UndirectedEdges.size());
			
				Iterator<UndirectedEdge> it = _UndirectedEdges.iterator();
			
				for(int i = 0; i < _UndirectedEdges.size(); ++i)
				{
					UndirectedEdge currentEdge = it.next();
				
					int u = i_Map.get(currentEdge.GetFirst());
					int v = i_Map.get(currentEdge.GetSecond());
				
					UndirectedEdge mappedEdge = new UndirectedEdge(u, v);
				
					mappedEdge.WriteLn(i_Writer);
				}	
			}
		}
	}
		
	// #
		
	// #region Read
		
	public static Machine Read(FileReader i_Reader)
	{
		if(i_Reader != null)
		{
			Machine m = new Machine();
				
			int edgesCount = i_Reader.NextInt();
				
			for(int i = 0; i < edgesCount; ++i)
			{				
				m.AddUndirectedEdge(UndirectedEdge.Read(i_Reader));
			}
				
			return m;
		}
			
		return null;
	}
		
	public static Machine Read(String i_Path)
	{
		FileReader reader = new FileReader(i_Path);
		
		if(reader.IsReadyToRead())
		{
			Machine p = Read(reader);
			
			reader.Close();
			
			return p;
		}
		
		return null;
	}
		
	// #
		
	// #
}