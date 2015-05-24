package unitn.akkacluster;

public class EndWorkMessage extends Message
{
	private static final long serialVersionUID = 1L;

	private long m_PartitioningTime;
	
	private int m_NodeId;
	
	public int GetNodeId()
	{
		return m_NodeId;
	}
	
	public long GetPartitioningTime()
	{
		return m_PartitioningTime;
	}
	
	public float GetPartitioningTimeInSeconds()
	{
		return ((float) m_PartitioningTime) / 1000f;
	}
	
	public EndWorkMessage(int i_NodeId, long i_PartitioningTime)
	{
		m_NodeId = i_NodeId;
		m_PartitioningTime = i_PartitioningTime;
	}
}
