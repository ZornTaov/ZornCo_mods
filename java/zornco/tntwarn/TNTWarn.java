package zornco.tntwarn;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.minecraft.block.BlockTNT;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(modid="TNTWarn", name="TNTWarn", version="0.0.1")
public class TNTWarn {

	// The instance of your mod that Forge uses.
	@Instance("TNTWarn")
	public static TNTWarn instance;

	public FileHandler handler;
	public static Logger logger = Logger.getLogger("TNTWarn");
	public static Logger logger2 = Logger.getLogger("TNTWarnLog");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Stub Method
		try {
			makeLogFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {

		registerEvent();
	}

	public void registerEvent () {
		MinecraftForge.EVENT_BUS.register(this);
		TNTWarn.logger.info("subscribed");
		TNTWarn.logger2.info("subscribed");
	}
	@SubscribeEvent
	public void blockevnt(HarvestDropsEvent event)
	{
		String s = "";
		if(event.block instanceof BlockTNT)
		{
			s += "Break TNT " + event.harvester.getDisplayName() + " " + event.x + " " + event.y + " " + event.z;
		}
		if(s != "")
		{
			TNTWarn.logger.warning(s);
			TNTWarn.logger2.warning(s);
			try {
				sendPacket(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent event)
	{
		String s = "";
		String t = "";
		if(event.action == Action.RIGHT_CLICK_BLOCK) // Works perfectly
		{
			if(event.entityPlayer.getCurrentEquippedItem() != null)
			{
				if(event.entityPlayer.getCurrentEquippedItem().getItem() == Item.getItemFromBlock(Blocks.tnt))
				{ 
					s += ("Place TNT " + event.entityPlayer.getDisplayName() + " " + event.x + " " + event.y + " " + event.z);
				}
				else if(event.entityPlayer.getCurrentEquippedItem().getItem() == Items.lava_bucket)
				{
					s += ("Place LAVA " + event.entityPlayer.getDisplayName() + " " + event.x + " " + event.y + " " + event.z);
				}
				if(s != "")
				{
					TNTWarn.logger.warning(s);
					TNTWarn.logger2.warning(s);
				}
				if(!event.entityPlayer.worldObj.isRemote)
				{
					try {
						sendPacket("PLINTR" + " " + event.entityPlayer.worldObj.getWorldInfo().getWorldName() + " " + event.entityPlayer.getDisplayName() + " " + event.entityPlayer.dimension + " " + event.x + " " + event.y + " " + event.z + " " + event.entityPlayer.getCurrentEquippedItem().getItem().getUnlocalizedNameInefficiently(event.entityPlayer.getCurrentEquippedItem()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		else if(event.action == Action.RIGHT_CLICK_AIR)
		{
			if(event.entityPlayer.getCurrentEquippedItem() != null)
			{
				if(!event.entityPlayer.worldObj.isRemote)
				{
					try {
						sendPacket("PLINTA" + " " + event.entityPlayer.worldObj.getWorldInfo().getWorldName() + " " + event.entityPlayer.getDisplayName() + " " + event.entityPlayer.dimension + " " + (int)event.entityPlayer.posX + " " + (int)event.entityPlayer.posY + " " + (int)event.entityPlayer.posZ + " " + event.entityPlayer.getCurrentEquippedItem().getItem().getUnlocalizedNameInefficiently(event.entityPlayer.getCurrentEquippedItem()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		else if(event.action == Action.LEFT_CLICK_BLOCK)
		{
			if(!event.entityPlayer.worldObj.isRemote)
			{

				if(event.entityPlayer.getCurrentEquippedItem() != null)
				{
					t = " " + event.entityPlayer.getCurrentEquippedItem().getItem().getUnlocalizedNameInefficiently(event.entityPlayer.getCurrentEquippedItem());
				}
				try {
					sendPacket("PLINTL" + " " + event.entityPlayer.worldObj.getWorldInfo().getWorldName().replace(" ", ":") + " " + event.entityPlayer.getDisplayName() + " " + event.entityPlayer.dimension + " " + (int)event.entityPlayer.posX + " " + (int)event.entityPlayer.posY + " " + (int)event.entityPlayer.posZ + t);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void makeLogFile() throws Exception
	{
		handler = new FileHandler("tnt-%g.log", 0, 3);
		handler.setFormatter(new FMLLogFormatter());
		logger2.addHandler(handler);

	}

	public void sendPacket(String text) throws IOException
	{
		final DatagramSocket socket = new DatagramSocket();
		final String MULTICAST_GROUP_ID = "66.171.182.40";
		final int PORT = 2244;
		byte[] buf = new byte[256];
		buf = text.getBytes();
		System.out.println(text);
		final InetAddress group = InetAddress.getByName(MULTICAST_GROUP_ID);
		DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
		socket.send(packet);
		socket.close();
	}

	final class FMLLogFormatter extends Formatter
	{
		final String LINE_SEPARATOR = System.getProperty("line.separator");
		private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public String format(LogRecord record)
		{
			StringBuilder msg = new StringBuilder();
			msg.append(this.dateFormat.format(Long.valueOf(record.getMillis())));
			Level lvl = record.getLevel();

			String name = lvl.getLocalizedName();
			if ( name == null )
			{
				name = lvl.getName();        	
			}

			if ( ( name != null ) && ( name.length() > 0 ) )
			{
				msg.append(" [" + name + "] ");
			}
			else
			{
				msg.append(" ");
			}

			if (record.getLoggerName() != null)
			{
				msg.append("["+record.getLoggerName()+"] ");
			}
			else
			{
				msg.append("[] ");
			}
			msg.append(record.getMessage());
			msg.append(LINE_SEPARATOR);
			Throwable thr = record.getThrown();

			if (thr != null)
			{
				StringWriter thrDump = new StringWriter();
				thr.printStackTrace(new PrintWriter(thrDump));
				msg.append(thrDump.toString());
			}

			return msg.toString();
		}
	}
}