package zornco.versioner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="Versioner", name="Versioner", version="0.1")
public class Versioner {

	@Instance("Versioner")
	public static Versioner instance;
	private static VersionChecker checker;
	public static Logger logger = Logger.getLogger("Versioner");
	private Configuration config;
	private String myUrl;
	private String myVer;
	private String myName;
	private String myDl;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			config.load();
			myUrl = config.get(Configuration.CATEGORY_GENERAL, "versionUrl", "").getString();
			myVer = config.get(Configuration.CATEGORY_GENERAL, "modVersion", "").getString();
			myName = config.get(Configuration.CATEGORY_GENERAL, "modName", "").getString();
			myDl = config.get(Configuration.CATEGORY_GENERAL, "dlURL", "").getString();
		}
		catch (Exception e)
		{
			FMLLog.log(Level.ERROR, e, "IronChest has a problem loading it's configuration");
		}
		finally
		{
			if (config.hasChanged())
				config.save();
		}
	}
	@EventHandler
	public void load(FMLPostInitializationEvent event) {
		checker = new VersionChecker(myUrl, myVer, myName, myDl);
		logger.info("Versioner has been enabled!");
	}
	
	@EventHandler
	public void worldLoad(FMLLoadCompleteEvent event)
	{
		logger.info("HelloWorld");
		if(checker.outString != "")
		{
			logger.info(checker.outString);
		}
	}
	public class VersionChecker implements Runnable{
		Thread runner;
		public String outString;
		private URL versionUrl;
		private String serverVersion;
		private String modVersion;
		private String dlURL;
		private String modName;
		/**
		 * VersionChecker is a simple class that checks a version file on your web site and
		 * compares it to the current version of your mod.
		 *
		 * @param URL The url where your version file is located
		 * @param modVer The version of the mod
		 * @param name The name of your mod
		 * @param dl Where your mod can be downloaded from
		 **/
		public VersionChecker(String URL, String modVer, String name, String dl){
			runner = new Thread(this, "Version Checker");
			modVersion = modVer;
			dlURL = dl;
			modName = name;
			try {
				versionUrl = new URL(URL);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			runner.start();
		}
		private String checkVersion(){
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(versionUrl.openStream()));
				serverVersion = reader.readLine();
				reader.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return tellInfo();
		}
		private boolean doVersionsMatch(){
			if(serverVersion == null){
				return true;
			}
			return serverVersion.equals(modVersion);
		}
		/**
		 * This method returns a string that can be used for console spam or as an in game
		 * chat message. It is recommended that you call this in your onTickInGame method when the world
		 * is not null. Be sure to only call this method once.
		 *
		 * @return Returns a string containing info on the latest version of your mod, and a link to where
		 * the user can download the mod.
		 **/
		public String tellInfo(){
			if(!doVersionsMatch()){
				return "Your current version of " + modName + " is out of date. The current version is " +
						serverVersion + " and can be downloaded at " + dlURL;
			}
			return "";
		}
		@Override
		public void run() {
			String s = checkVersion();
			outString = s;
		}
	}
}
