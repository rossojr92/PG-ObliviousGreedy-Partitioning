package unitn.akkacluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.Scanner;

public class Main 
{
    public static String ip;
    
    // #region Main
    
    public static void main(String[] argv) throws InterruptedException 
    {
        if(argv.length != 2 && argv.length != 5)
        {
        	Debug.LogError("Usage for master: [ec2/local] master [graphName] [Executors] [Partitions]");
            Debug.LogError("Usage for node: [ec2/local] [nodeid]");
            System.exit(1);
        }
        
        if(argv[0].equals("ec2"))
        {
            ip = getIP();
        }
        else if(argv[0].equals("local"))
        {
            ip = "127.0.0.1";
        }

        if (argv[1].equals("master")) 
        {            
        	String graphName = argv[2];
            int executors = Integer.parseInt(argv[3]);
            int partitions = Integer.parseInt(argv[4]);
            	
            Master(graphName, executors, partitions);
        } 
        else
        {
            int id = Integer.parseInt(argv[1]);
            Node(id);
        }
    }
    
    // #
    
    // #region Master MAIN
    
    // If I am the master, I create the system and create the Master Actor
    public static void Master(String i_GraphName, int i_Executors, int i_Partitions) 
    {
        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + ip)
        		.withFallback(ConfigFactory.parseFile(new File("application.conf")));
        
        ActorSystem system = ActorSystem.create("ClusterSystem", config);
        
        @SuppressWarnings("unused")
		ActorRef master = system.actorOf(Props.create(new MasterCreator(i_GraphName, i_Executors, i_Partitions)), "master");
    }

    // #

    // #region Node MAIN
    
    //If I am the node, I need to connect with the master.
    public static void Node(int i_Id) 
    {    
        int port = 0; //random port
        
        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
                .withFallback(ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + ip))
                .withFallback(ConfigFactory.parseFile(new File("application.conf")));
        
        //read the path of Master Actor
        String masterPath = (String) config.getList("akka.cluster.seed-nodes").get(0).unwrapped() + "/user/master"; 
        
        ActorSystem system = ActorSystem.create("ClusterSystem", config);
        
        ActorRef node = system.actorOf(Props.create(new NodeCreator(i_Id, masterPath)), "node" + i_Id);
        
        node.tell(new RegisterNodeMessage(i_Id), null); //Tell the node actor to talk to the master node
    }
    
    // #

    // #region Get IP
    
    //Gets local ip via semi-ugly curl call
    public static String getIP() 
    {
        try 
        {
            Process p = Runtime.getRuntime().exec("curl http://169.254.169.254/latest/meta-data/local-ipv4");
            
            int returnCode = p.waitFor();
            
            if (returnCode == 0) 
            {
                Scanner s = new Scanner(p.getInputStream());
                String ip = s.nextLine();
                s.close();
                
                return ip;
            } 
            else 
            {
                return null;
            }
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.exit(1);
            
            return null;
        }
    }
    
    // #
}
