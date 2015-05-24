package unitn.akkacluster;

public class Obj<T> 
{
	T value = null;
	
	public T GetValue()
	{
		return value;
	}
	
	public void SetValue(T i_Value)
	{
		value = i_Value;
	}
	
	public Obj(T i_Value)
	{
		value = i_Value;
	}
	
	public Obj()
	{
		value = null;
	}
}