package zornco.mazegen;

import java.util.logging.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="MazeGen", name="MazeGen", version="0.1")
public class MazeGen {

	@Instance("MazeGen")
	public static MazeGen instance;

	public static Logger logger = Logger.getLogger("MazeGen");

	private static TabMazeGen mazeGenTab;

	public Item mazeMaker;
	public Item mazeRatMaker;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		mazeGenTab = new TabMazeGen("MazeGen");

		mazeMaker = new ItemMazeMaker().setMaxStackSize(1).setUnlocalizedName("mazeMaker").setCreativeTab(MazeGen.mazeGenTab);
		registerItem(mazeMaker);
		mazeRatMaker = new ItemRatMazeMaker().setMaxStackSize(1).setUnlocalizedName("mazeRatMaker").setCreativeTab(MazeGen.mazeGenTab);
		registerItem(mazeRatMaker);
	}
	@EventHandler
	public void load(FMLPostInitializationEvent event) {
		logger.info("MazeGen has been enabled!");
	}
	
	public static void registerItem(Item item)
	{
 		GameRegistry.registerItem(item, item.getUnlocalizedName().replace("item.", ""));
	}
	
	public class TabMazeGen extends CreativeTabs {

		public TabMazeGen(String label) 
		{
			super(label);
		}
		@Override
		public Item getTabIconItem() {
			return null;
		}
		@Override
	    public ItemStack getIconItemStack()
	    {
			return new ItemStack(GameRegistry.findItem("MazeGen", "mazeMaker"));
	    }


	}
}