package unitn.akkacluster;

public abstract class DataCollection<T>
{	
	public abstract T Get(int i_Index);
	
	public abstract T GetHighestValue();
	
	public abstract T GetLowestValue();

	public abstract float GetAvg();
	
	public abstract float GetVariance();
	
	public abstract float GetStdDev();
	
	public abstract T GetTotal();
	
	public abstract void AddValue(T i_Value);
	
	public abstract int Size();
	
	public abstract void Clear();

}
