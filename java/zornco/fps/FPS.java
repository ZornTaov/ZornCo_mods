package zornco.fps;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

// central controller for mod
@Mod(modid=FPS.MODID, name=FPS.MODID, version=FPS.VERSION) // uncomment this to compile
public class FPS {
	
	@SidedProxy(clientSide="zornco.fps.ClientProxy", serverSide="zornco.fps.CommonProxy")
	public static CommonProxy proxy;
	
	public static List<EntityPlayer> currentPlayerList = new ArrayList<EntityPlayer>();
	public boolean needsToRefresh;

	public static final String MODID = "FPS";
	public static final String VERSION = "1.0";
	public static boolean useOldSkinServ = false;
	public static HashMap<String, Boolean> neutralList = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> friendList = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> enemyList = new HashMap<String, Boolean>();
	public static final String friendFilename = "friends.list";
	public static final String enemyFilename = "enemies.list";
	public static final String optionsFilename = "conf";

	public static int friendArrowColor = 0x0000FF00; // the green
	public static int enemyArrowColor = 0x00FF0000; // the red
	public static int neutralArrowColor = 0x00FFFF00; // the yellow

	public static boolean showHudInfo = true;
	public static boolean showOtherPos = true;

	// The instance of your mod that Forge uses.
	@Instance(FPS.MODID)
	public static FPS instance;

	public int getPlayerCount() {
		return currentPlayerList.size();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {

		proxy.init();
		this.needsToRefresh = false;
		FPS.proxy.loadOptions();
		this.loadLists();
		FPS.proxy.saveOptions();
		this.saveLists();
		System.out.println("FPS Loaded");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.registerTickHandler();
		System.out.println("FPS post");
	}
	public void saveLists()
	{
		proxy.saveList(FPS.friendFilename,FPS.friendList);
		proxy.saveList(FPS.enemyFilename,FPS.enemyList);
	}
	public void loadLists()
	{
		proxy.loadList(FPS.friendFilename,FPS.friendList);
		proxy.loadList(FPS.enemyFilename,FPS.enemyList);
	}
}
