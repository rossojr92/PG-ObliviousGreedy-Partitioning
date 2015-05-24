package unitn.akkacluster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PowerGraphExecutor 
{
	// #region Fields
	
	private Map<Integer, Integer> _VerticesMap = null;
	private Set<UndirectedEdge> _Edges = null;
	private Map<Integer, Integer> _ReverseVerticesMap = null;
	
	private Machines _Machines = null;
	
	private int[] _VerticesDegree = null;
	
	private boolean _bInitialized = false;
	
	// #

	// #region Initialize Executor
	
	public void InitializeData(Set<UndirectedEdge> i_Edges)
	{	
		FillData(i_Edges);
		
		_bInitialized = true;
	}
	
	// #
	
	// #region Partitioning algorithm
	
	public void Partition(int i_NumberOfPartitions)
	{
		if(!_bInitialized)
		{
			Debug.LogWarning("[PowerGraph executor] PowerGraphExecutor not initialized.");
			
			return;
		}
		
		_Machines = new Machines(i_NumberOfPartitions);
		
		int[] processedEdgeForVertex = new int[_VerticesDegree.length];
		
		Iterator<UndirectedEdge> it = _Edges.iterator();
		
		for(int i = 0; i < _Edges.size(); ++i)
		{
			// Do greedy choice on edges
			
			UndirectedEdge currentEdge = it.next();
			
			Vertex u = new Vertex(currentEdge.GetFirst());
			Vertex v = new Vertex(currentEdge.GetSecond());
		
			Set<Integer> Au = _Machines.GetMachinesIndexWithVertex(u); // Machine that contains vertex u
			Set<Integer> Av = _Machines.GetMachinesIndexWithVertex(v); // Machine that contains vertex v
			
			Set<Integer> intersection = new HashSet<Integer>(Au);
			
			intersection.retainAll(Av); // Transform intersection (that is equal to Au) into the intersection between Au and Av
			
			if(!intersection.isEmpty())
			{
				// Case 1: A(u) intersect A(v) -> assign the edge to a machine in the intersection
				
				Iterator<Integer> iterator = intersection.iterator();
				
				int index = iterator.next();
				
				_Machines.GetMachineByIndex(index).AddUndirectedEdge(u, v);
				
			}
			else if(!Au.isEmpty() && !Av.isEmpty())
			{
				// Case 2 -> A(u) and A(v) aren' t empty and don' t intersect, 
				// then the edge should be assigned to one of the machines from vertex with the most unassigned edges
				
				Vertex vertex = (_VerticesDegree[u.GetId()] - processedEdgeForVertex[u.GetId()] > _VerticesDegree[v.GetId()] - processedEdgeForVertex[v.GetId()]) ? u : v;

				Set<Integer> m = _Machines.GetMachinesIndexWithVertex(vertex);

				Iterator<Integer> iterator = m.iterator();
				
				int index = iterator.next();
				
				_Machines.GetMachineByIndex(index).AddUndirectedEdge(u, v);
			
			}
			else if((!Au.isEmpty()) ^ (!Av.isEmpty()))
			{
				// Case 3 -> If only one of the two vertices has been assigned, then choose a machine from the assigned vertex
				if(!Au.isEmpty())
				{
					// Choose a machine from Au
					Iterator<Integer> iterator = Au.iterator();
					
					int index = iterator.next();
					
					_Machines.GetMachineByIndex(index).AddUndirectedEdge(u, v);
				}
				else 
				{
					// Choose a machine from Av
					Iterator<Integer> iterator = Av.iterator();
					
					int index = iterator.next();
					
					_Machines.GetMachineByIndex(index).AddUndirectedEdge(u, v);
				}
				
			}
			else 
			{
				// Case 4 -> If neither vertex has been assigned, then assign the edge to the least loaded machine
				_Machines.GetLeastLoadedMachine().AddUndirectedEdge(u, v);
				
			}
			
			++processedEdgeForVertex[u.GetId()];
			++processedEdgeForVertex[v.GetId()];
		}
	}
	
	// #
	
	// #region Private methods
	
	private void FillData(Set<UndirectedEdge> i_Edges)
	{	
		FillEdgesAndVertices(i_Edges);
		
		EvaluateVerticesDegree();
	}
	
	private void FillEdgesAndVertices(Set<UndirectedEdge> i_Edges)
	{
		int edgesCount = i_Edges.size();
		int vertexIndex = 0;
		
		Iterator<UndirectedEdge> it = i_Edges.iterator();
		
		for(int i = 0; i < edgesCount; ++i)
		{
			UndirectedEdge currentEdge = it.next();
			
			int first = currentEdge.GetFirst();
			int second = currentEdge.GetSecond();
			
			UndirectedEdge edge = new UndirectedEdge();
			
			if(_VerticesMap.get(first) == null)
			{
				edge.SetFirst(vertexIndex);
				
				_ReverseVerticesMap.put(vertexIndex, first);
				_VerticesMap.put(first, vertexIndex);
				
				++vertexIndex;
			}
			else 
			{
				edge.SetFirst(_VerticesMap.get(first));
			}
			
			if(_VerticesMap.get(second) == null)
			{
				edge.SetSecond(vertexIndex);
				
				_ReverseVerticesMap.put(vertexIndex, second);
				_VerticesMap.put(second, vertexIndex);
				
				++vertexIndex;
			}
			else 
			{
				edge.SetSecond(_VerticesMap.get(second));
			}
			
			_Edges.add(edge);
		}
	}
	
	private void EvaluateVerticesDegree()
	{
		_VerticesDegree = new int[_VerticesMap.size()];
		
		Iterator<UndirectedEdge> iterator = _Edges.iterator();
		
		for(int i = 0; i < _Edges.size(); ++i)
		{
			UndirectedEdge current = iterator.next();
			
			++_VerticesDegree[current.GetFirst()];
			++_VerticesDegree[current.GetSecond()];	
		}
	}
	
	// #
	
	// #region Constructors
	
	public PowerGraphExecutor()
	{
		_VerticesMap = new HashMap<Integer, Integer>();
		
		_ReverseVerticesMap = new HashMap<Integer, Integer>();
		
		_Edges = new HashSet<UndirectedEdge>();
	}
	
	// #
}
