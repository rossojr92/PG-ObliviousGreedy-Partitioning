package unitn.akkacluster;

public class RegisterNodeMessage extends Message
{
	private static final long serialVersionUID = 1L;
	
	private final int m_Id;
	
	public int GetId()
	{
		return m_Id;
	}
	
	public RegisterNodeMessage(int i_Id)
	{
		m_Id = i_Id;
	}
}
