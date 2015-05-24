package unitn.akkacluster;

public class EnvironmentSettings 
{
	private static final boolean _Debug = true;
	
	private static final boolean _WarningEnabled = true;
	
	private static final boolean _ErrorEnabled = true;
	
	public static boolean IsDebug()
	{
		return _Debug;
	}
	
	public static boolean WarningEnabled()
	{
		return _WarningEnabled;
	}
	
	public static boolean ErrorEnabled()
	{
		return _ErrorEnabled;
	}
}