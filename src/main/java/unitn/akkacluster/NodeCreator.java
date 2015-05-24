package unitn.akkacluster;

import akka.japi.Creator;

public class NodeCreator implements Creator<Node>
{
	private static final long serialVersionUID = 1L;
	
	int m_Id;
	String m_MasterPath;
	
	@Override
	public Node create()
	{
		return new Node(m_Id, m_MasterPath);
	}
	
	public NodeCreator(int i_Id, String i_MasterPath)
	{
		m_Id = i_Id;
		m_MasterPath = i_MasterPath;
	}	
}
