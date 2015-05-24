package unitn.akkacluster;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class Node extends UntypedActor
{
	private ActorRef m_Master = null;
    
	private final int m_Id; 
    private final String m_MasterPath;
    
    private PowerGraphExecutor m_Executor = null;
    
    @Override
    public void onReceive(Object i_Message)
    {
        if(i_Message instanceof RegisterNodeMessage)
        {
            HandleRegisterNodeMessage((RegisterNodeMessage) i_Message);
        }
        else if(i_Message instanceof StartWorkMessage)
        {
        	HandleStartWorkMessage((StartWorkMessage) i_Message);
		}
        else if (i_Message instanceof ShutDownMessage)
        {
            HandleShutDownMessage((ShutDownMessage) i_Message);
        }
        else
        {
            unhandled(i_Message);
        }
    }

    // #region Handle RegisterNode Message
    
    private void HandleRegisterNodeMessage(RegisterNodeMessage i_Message)
    {	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[NODE][HandleRegisterNodeMessage] I' M GONNA REGISTER MY SELF ON MASTER WITH ID: " + i_Message.GetId() + ". (Master path: " + m_MasterPath + ")");
    	Debug.Log("------------------------------------------------------------");
    	
    	getContext().actorSelection(m_MasterPath).tell(new RegisterNodeMessage(m_Id), getSelf());
    
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[NODE][HandleRegisterNodeMessage] REGISTERED.");
    	Debug.Log("------------------------------------------------------------");
    }
    
    // #
    
    // #region Handle StartWork Message
    
    private void HandleStartWorkMessage(StartWorkMessage i_Message)
    {
    	m_Master = getSender();
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[NODE][HandleStartWorkMessage] I HAVE TO PARTITION " + i_Message.GetEdges().size() + " EDGES IN " + i_Message.GetNumberOfPartitions() + " GROUPS.");
    	Debug.Log("------------------------------------------------------------");
    	
    	m_Executor = new PowerGraphExecutor();
    	
    	Timer timer = new Timer();
    	
    	timer.Start();
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[NODE][HandleStartWorkMessage] INITIALIZING DATA.");
    	Debug.Log("------------------------------------------------------------");
    	
    	m_Executor.InitializeData(i_Message.GetEdges());
    	
    	timer.Stop();
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[NODE][HandleStartWorkMessage] DATA INITIALIZED IN " + timer.GetDurationInSecond() + " SEC.");
    	Debug.Log("------------------------------------------------------------");
    	
    	timer.Reset();
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[NODE][HandleStartWorkMessage] START PARTITIONING.");
    	Debug.Log("------------------------------------------------------------");
    	
    	timer.Start();
    	
    	m_Executor.Partition(i_Message.GetNumberOfPartitions());
    	
    	timer.Stop();
    	
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[NODE][HandleStartWorkMessage] END PARTITIONING IN " + timer.GetDurationInSecond() + " SEC.");
    	Debug.Log("------------------------------------------------------------");
    	
    	m_Master.tell(new EndWorkMessage(m_Id, timer.GetDuration()), getSelf());
    }
    
    // #
    
    // #region Handle ShutDown Message
    
    private void HandleShutDownMessage(ShutDownMessage i_Message)
    {
    	Debug.Log("------------------------------------------------------------");
    	Debug.Log("[NODE][HandleShutDownMessage] SHUTDOWN MESSAGE RECEIVED.");
    	Debug.Log("------------------------------------------------------------");
    	
    	getContext().system().shutdown();
    }
    
    // #
    
    // #region Constructors
    
    public Node(int i_Id, String i_MasterPath)
    {
        m_Id = i_Id;
        m_MasterPath = i_MasterPath;
    }
    
    // #
}
