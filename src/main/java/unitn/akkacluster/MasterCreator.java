package unitn.akkacluster;

import akka.japi.Creator;

public class MasterCreator implements Creator<Master>
{
	private static final long serialVersionUID = 1L;
	
	private String m_GraphName;
	private int m_Partitions;
	
	private int m_Executors;
	
	@Override
	public Master create()
	{
		return new Master(m_GraphName, m_Executors, m_Partitions);
	}
	
	public MasterCreator(String i_GraphName, int i_Executors, int i_Partitions)
	{
		m_GraphName = i_GraphName;
		m_Executors = i_Executors;
		m_Partitions = i_Partitions;
	}
}
