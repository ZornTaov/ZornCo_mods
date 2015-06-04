package zornco.fps;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class FPSKeyHandler {
	
	static KeyBinding show = new KeyBinding("Show FPS Menu", Keyboard.KEY_Y, "key.categories.gameplay");
	public static GuiFriendFoe guiFF;
	
	public FPSKeyHandler() {
		
		ClientRegistry.registerKeyBinding(show);
	}
	//@Override
	public String getLabel() {
		return "fpskeybindings";
	}
	@SubscribeEvent
	public void keypress(KeyInputEvent event)
	{
		if(Keyboard.getEventKey() == show.getKeyCode())
		{
			//System.out.println("ding");
			if(FMLClientHandler.instance().getClient().currentScreen == null)
			{
				//System.out.println("dong");
				FPSKeyHandler.guiFF = new GuiFriendFoe(
						FPS.instance,
						FPS.currentPlayerList, 
						FPS.friendList, FPS.neutralList, FPS.enemyList,
						FPS.friendArrowColor, FPS.neutralArrowColor, FPS.enemyArrowColor,
						FPS.showHudInfo, FPS.showOtherPos
						);
				FMLClientHandler.instance().displayGuiScreen(FMLClientHandler.instance().getClient().thePlayer, guiFF);
			}
		}
	}
}
