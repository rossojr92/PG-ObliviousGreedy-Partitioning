package unitn.akkacluster;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Machines 
{
	// #region Fields
	
	private Machine[] _Machines = null;

	// #
	
	// #region Constructors
	
	public Machines(int i_Partitions)
	{
		_Machines = new Machine[i_Partitions];
		
		for(int i = 0; i < _Machines.length; ++i)
		{
			_Machines[i] = new Machine();
		}
	}
	
	// #

	// #region Getters
	
	public Machine[] GetMachines()
	{
		return _Machines;
	}
	
	public int GetMachinesCount()
	{
		return _Machines.length;
	}

	
	//#
	
	// #region Logic
	
	public Set<Integer> GetMachinesIndexWithVertex(Vertex i_Vertex)
	{
		return GetMachinesIndexWithVertex(i_Vertex.GetId());
	}
	
	public Set<Integer> GetMachinesIndexWithVertex(int i_VertexId)
	{
		Set<Integer> set = new HashSet<Integer>(); 
	
		for(int i = 0; i < _Machines.length; ++i)
		{
			if(_Machines[i].Contains(i_VertexId))
			{
				set.add(i);
			}
		}
		
		return set;
	}
	
	public int GetLeastLoadedMachineIndex()
	{
		int index = 0;
		
		for(int i = 1; i < _Machines.length; ++i)
		{
			if(_Machines[i].GetEdgesCount() < _Machines[index].GetEdgesCount())
			{
				index = i;
			}
		}
		
		return index;
	}
	
	public Machine GetLeastLoadedMachine()
	{
		return _Machines[GetLeastLoadedMachineIndex()];
	}
	
	public Machine GetMachineByIndex(int i_Index)
	{
		if(_Machines != null)
		{
			return _Machines[i_Index];
		}
		
		return null;
	}
	
	public Machine Get(int i_Index)
	{
		if(_Machines != null)
		{
			return _Machines[i_Index];
		}
		
		return null;
	}
	
	public void SetMachine(Machine i_Machine, int i_Index)
	{
		_Machines[i_Index] = i_Machine; 
	}
	
	public int GetTotalNumberOfEdges()
	{
		int edgesCount = 0;
		
		for(int i = 0; i < _Machines.length; ++i)
		{
			edgesCount += _Machines[i].GetEdgesCount();
		}
		
		return edgesCount;
	}
	
	public int GetTotalNumberOfVertices()
	{
		int verticesCount = 0;
		
		for(int i = 0; i < _Machines.length; ++i)
		{
			verticesCount += _Machines[i].GetVerticesCount();
		}
		
		return verticesCount;
	}
	
	// #
	
	// #region Debug
	
	public void LogInfo()
	{
		Debug.Log("[Machines] I have " + GetMachinesCount() + " machines.");
		
		for(int i = 0; i < GetMachinesCount(); ++i)
		{
			_Machines[i].LogInfo();
		}
	}
	
	// #
	
	// #region Write and Read
	
	// #region Write
	
	public void Write(FileWriter i_Writer)
	{
		if(i_Writer != null)
		{
			if(_Machines != null)
			{
				i_Writer.PrintLn(GetMachinesCount());
				
				for(int i = 0; i < GetMachinesCount(); ++i)
				{
					_Machines[i].Write(i_Writer);
				}
			}
		}
	}
	
	public void Write(Map<Integer, Integer> i_Map, FileWriter i_Writer)
	{
		if(i_Writer != null)
		{
			if(_Machines != null)
			{
				i_Writer.PrintLn(_Machines.length);
			
				for(int i = 0; i < _Machines.length; ++i)
				{
					_Machines[i].Write(i_Map, i_Writer);
				}
			}
		}
	}
	
	public void Write(String i_Path)
	{
		FileWriter writer = new FileWriter(i_Path);
		
		if(writer != null)
		{
			Write(writer);
			
			writer.Close();
		}
	}
	
	public void Write(Map<Integer, Integer> i_Map, String i_Path)
	{
		FileWriter writer = new FileWriter(i_Path);
		
		if(writer != null)
		{
			Write(i_Map, writer);
			
			writer.Close();
		}
	}
	
	// #
	
	// #region Read
	
	public static Machines Read(FileReader i_Reader)
	{
		if(i_Reader != null)
		{
			int partitions = i_Reader.NextInt();
			
			Machines machines = new Machines(partitions);
			
			for(int i = 0; i < partitions; ++i)
			{
				Machine m = Machine.Read(i_Reader);
				
				machines.SetMachine(m, i);
			}
		
			return machines;
		}
		
		return null;
	}
	
	public static Machines Read(String i_Path)
	{
		FileReader reader = new FileReader(i_Path);
	
		if(reader.IsReadyToRead())
		{
			Machines p = Read(reader);
		
			reader.Close();
		
			return p;
		}
	
		return null;
	}
	
	// #
	
	// #
	
}

