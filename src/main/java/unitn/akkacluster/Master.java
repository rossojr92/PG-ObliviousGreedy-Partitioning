package unitn.akkacluster;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Master extends UntypedActor 
{
	// #region Fields
	
	private final String m_GraphName;
	
    private final int m_Executors;
    
    private final int m_Partitions;
    
    private int m_RegisteredNodeCounter = 0;
  
    private int m_EndWorkMessageReceived;
    private FloatCollection m_Times = null;
    
    private Timer m_Timer = null;
    
    private HashMap<Integer,ActorRef> m_Nodes;

    // #
    
    @Override
    public void onReceive(Object i_Message) throws Exception 
    {
        if (i_Message instanceof RegisterNodeMessage) 
        {
        	HandleRegisterNodeMessage((RegisterNodeMessage) i_Message);
        }
        else if(i_Message instanceof EndWorkMessage)
        {
        	HandleEndWorkMessage((EndWorkMessage) i_Message);
        }
        else
        {
            unhandled(i_Message);
        } 
    }
    
    // #region Handle RegisterNode Message
    
    private void HandleRegisterNodeMessage(RegisterNodeMessage i_Message)
    {   
    	int id = i_Message.GetId();
    	
    	m_Nodes.put(id, getSender());
        
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[MASTER][HandleRegisterNodeMessage] NODE REGISTERED WITH ID: " + id + ". (" + (m_RegisteredNodeCounter + 1) + " / " + m_Executors + ")");
    	Debug.Log("------------------------------------------------------------");
    	
    	++m_RegisteredNodeCounter;
        
        if (m_RegisteredNodeCounter == m_Executors) 
        {	
        	Debug.Log("------------------------------------------------------------");
        	Debug.Log("[MASTER][HandleRegisterNodeMessage] ALL NODE ARE REGISTERED.");
        	Debug.Log("------------------------------------------------------------");
        	
        	StartWorks();
        }
    }
    
    private void StartWorks()
    {
    	m_EndWorkMessageReceived = 0;
    	m_Times = new FloatCollection();
    	
    	m_Timer = new Timer();
    	
    	// #region Build Messages
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[MASTER][StartWorks] START BUILDING MESSAGES.");
    	Debug.Log("------------------------------------------------------------");
    	
    	StartWorkMessage[] messages = new StartWorkMessage[m_Executors];
    	
    	for(int i = 0; i < messages.length; ++i)
    	{
    		messages[i] = new StartWorkMessage(m_Partitions);
    	}
  
    	FileReader reader = new FileReader("inputs/" + m_GraphName + ".txt");
    	
    	int edges = reader.NextInt();
    	int vertices = reader.NextInt();
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log(m_GraphName + " GRAPH HAS " + vertices + " VERTICES AND " + edges + " EDGES.");
    	Debug.Log("------------------------------------------------------------");
    	
    	ArrayList<UndirectedEdge> edgesList = new ArrayList<UndirectedEdge>();
    	
    	for(int i = 0; i < edges; ++i)
    	{
    		UndirectedEdge currentEdge = UndirectedEdge.Read(reader);
    		
    		edgesList.add(currentEdge);
    	}
    	
    	reader.Close();
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("START SHUFFLE EDGES.");
    	Debug.Log("------------------------------------------------------------");
    	
    	Collections.shuffle(edgesList);
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("EDGES SHUFFLED.");
    	Debug.Log("------------------------------------------------------------");
    	
    	for(int i = 0; i < edges; ++i)
    	{
    		UndirectedEdge currentEdge = edgesList.get(i);
    		
    		messages[i % m_Executors].AddEdge(currentEdge);
    	}
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[MASTER][StartWorks] MESSAGES BUILT.");
    	Debug.Log("------------------------------------------------------------");
    	
    	// #
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[MASTER][StartWorks] START SENDING MESSAGE.");
    	Debug.Log("------------------------------------------------------------");
    	
    	int index = 0;
    	
    	m_Timer.Start();
    	
    	for(ActorRef actor : m_Nodes.values())
    	{	
    		actor.tell(messages[index++], getSelf());
    	}
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[MASTER][StartWorks] ALL MESSAGES WERE DELIVERED.");
    	Debug.Log("------------------------------------------------------------");
    }
    
    // #
    
    // #region Handle EndWork Message
    
    private void HandleEndWorkMessage(EndWorkMessage i_Message)
    {
    	++m_EndWorkMessageReceived;
    	
    	m_Times.AddValue(i_Message.GetPartitioningTimeInSeconds());
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[MASTER][HandleEndWorkMessage] NODE " + i_Message.GetNodeId() + " ENDS WORK IN " + i_Message.GetPartitioningTimeInSeconds() + " SEC.");
    	Debug.Log("------------------------------------------------------------");
    	
    	if(m_EndWorkMessageReceived == m_Executors)
    	{
    		// All workers have finished.
    		
    		m_Timer.Stop();
    		
    		Debug.Log("------------------------------------------------------------");
    		Debug.Log("[MASTER][HandleEndWorkMessage] ALL WORKERS HAVE FINISHED IN " + m_Timer.GetDurationInSecond() + " SEC.");
    		Debug.Log("[MASTER][HandleEndWorkMessage] AVG TIME FOR EACH NODE: " + m_Times.GetAvg() + " SEC.");
    		Debug.Log("------------------------------------------------------------");
    		
    		CreateOutputFile();
    		CreateOutputDebugFile();
    		
    		StopAll();
    	}
    }
    
    private void StopAll()
    {
		for(ActorRef actor : m_Nodes.values())
		{
			actor.tell(new ShutDownMessage(), getSelf());
		}
		
		getContext().system().shutdown();
    }
    
    
    private void CreateOutputFile()
    {
    	FileWriter writer = new FileWriter("time.txt");
    	
    	if(writer != null)
    	{
    		writer.PrintLn(m_GraphName + " " + m_Executors + " " + m_Partitions);
    		
    		writer.PrintLn(m_Timer.GetDurationInSecond());
    		
    		for(int i = 0; i < m_Times.Size(); ++i)
    		{
    			writer.PrintLn(m_Times.Get(i));
    		}
    		
    		writer.PrintLn(m_Times.GetAvg());
    		writer.PrintLn(m_Times.GetHighestValue());
    		writer.PrintLn(m_Times.GetLowestValue());
    		
    		writer.Close();
    	}
    }
    
    private void CreateOutputDebugFile()
    {
    	FileWriter writer = new FileWriter("debug_time.txt");
    	
    	if(writer != null)
    	{
    		writer.PrintLn(m_GraphName + " " + m_Executors + " " + m_Partitions);
    		
    		writer.NewLine();
    		
    		writer.PrintLn("Total time: " + m_Timer.GetDurationInSecond());
    		
    		writer.NewLine();
    		
    		for(int i = 0; i < m_Times.Size(); ++i)
    		{
    			writer.PrintLn("Executor " + (i + 1) + ": " + m_Times.Get(i));
    		}
    		
    		writer.NewLine();
    		
    		writer.PrintLn("Avg: " + m_Times.GetAvg());
    		writer.PrintLn("Highest time: " + m_Times.GetHighestValue());
    		writer.PrintLn("Lowest time: " + m_Times.GetLowestValue());
    		
    		writer.Close();
    	}
    }
    
    // #
    
    // #region Constructors
    
    public Master(String i_GraphName, int i_Executors, int i_Partitions) 
    {
    	m_GraphName = i_GraphName;
    	m_Partitions = i_Partitions;
    	
        m_Executors = i_Executors;
        
        m_Nodes = new  HashMap<>();
        
        m_RegisteredNodeCounter = 0;
    }
    
    // #
}
