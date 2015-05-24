package unitn.akkacluster;

public class Debug 
{	
	public static void Log(Object obj)
	{
		if(EnvironmentSettings.IsDebug())
		{
			System.out.println("[DEBUG] " + obj.toString());
		}
	}
	
	public static void LogWarning(Object obj)
	{
		if(EnvironmentSettings.IsDebug() && EnvironmentSettings.WarningEnabled())
		{
			System.out.println("[DEBUG - WARNING] " + obj.toString());
		}
	}
	
	public static void LogError(Object obj)
	{
		if(EnvironmentSettings.IsDebug() && EnvironmentSettings.ErrorEnabled())
		{
			System.out.println("[DEBUG - ERROR] " + obj.toString());
		}
	}
}
