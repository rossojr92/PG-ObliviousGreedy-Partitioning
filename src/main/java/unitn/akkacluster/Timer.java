package unitn.akkacluster;

public class Timer 
{
	private long _StartTime;
	private long _EndTime;
	
	private long _CachedTime;
	
	private boolean _bRunning;
	
	public long GetStart()
	{
		return _StartTime;
	}
	
	public long GetEnd()
	{
		if(!_bRunning)
		{
			return _EndTime;
		}
		
		return 0;
	}
	
	public boolean IsRunning()
	{
		return _bRunning;
	}
	
	public void Start()
	{
		if(!_bRunning)
		{
			_bRunning = true;
			_CachedTime = 0;
		
			_StartTime = System.currentTimeMillis();	
		}
	}
	
	public void Stop()
	{
		if(_bRunning)
		{
			_EndTime = System.currentTimeMillis();
		
			_bRunning = false;
		}
	}
	
	public void Resume()
	{
		if(!_bRunning)
		{
			_CachedTime += _EndTime - _StartTime;
		
			Restart();
		}
	}
	
	public void Reset()
	{
		if(!_bRunning)
		{
			_CachedTime = 0;
		
			_StartTime = 0;
			_EndTime = 0;
		}
	}
	
	private void Restart()
	{
		if(!_bRunning)
		{
			_bRunning = true;
		
			_StartTime = System.currentTimeMillis();
		}
	}
	
	public long GetDuration()
	{
		if(_bRunning)
		{
			return System.currentTimeMillis() - _StartTime + _CachedTime;
		}
		
		return _EndTime - _StartTime + _CachedTime;
	}
	
	public float GetDurationInSecond()
	{
		return GetDuration() / 1000.0f;
	}
	
	public Timer()
	{
		_StartTime = 0;
		_EndTime = 0;
		
		_CachedTime = 0;
	}

}