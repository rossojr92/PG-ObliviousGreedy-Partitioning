package unitn.akkacluster;

import java.util.ArrayList;

public class IntegerCollection extends DataCollection<Integer>
{
	private ArrayList<Integer> _Data = null;
	
	@Override
	public Integer Get(int i_Index)
	{
		if(i_Index < 0 || i_Index >= _Data.size())
		{
			return null;
		}
		
		return _Data.get(i_Index);
	}
	
	@Override
	public Integer GetHighestValue() 
	{
		int index = 0;
		
		for(int i = 1; i < _Data.size(); ++i)
		{
			if(_Data.get(i) > _Data.get(index))
			{
				index = i;
			}
		}
		
		if(_Data.size() > 0)
		{
			return _Data.get(index);
		}
		
		return null;
	}

	@Override
	public Integer GetLowestValue()
	{
		int index = 0;
		
		for(int i = 1; i < _Data.size(); ++i)
		{
			if(_Data.get(i) < _Data.get(index))
			{
				index = i;
			}
		}
		
		if(_Data.size() > 0)
		{
			return _Data.get(index);
		}
		
		return null;
	}

	@Override
	public float GetAvg()
	{
		Integer sum = GetTotal();
		
		return (((float)sum) / ((float)Size()));
	}

	@Override
	public float GetVariance() 
	{
		float avg = GetAvg();
		
		float sum = 0f;
		
		for(int i = 0; i < _Data.size(); ++i)
		{
			float distance = ((float) _Data.get(i)) - avg;
			
			sum += Mathf.Square(distance);
		}
		
		return sum / Size();
	}
	
	@Override
	public float GetStdDev() 
	{
		return Mathf.Sqrt(GetVariance());
	}
	
	@Override
	public Integer GetTotal() 
	{
		Integer sum = new Integer(0);
		
		for(int i = 0; i < _Data.size(); ++i)
		{
			sum += _Data.get(i);
		}
		
		return sum;
	}

	@Override
	public void AddValue(Integer i_Value) 
	{
		_Data.add(i_Value);
	}

	@Override
	public int Size() 
	{
		return _Data.size();
	}

	@Override
	public void Clear() 
	{
		_Data.clear();
	}
	
	public IntegerCollection()
	{
		_Data = new ArrayList<Integer>();
	}
	
}