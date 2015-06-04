package zornco.fps;

import java.util.HashMap;

import net.minecraft.client.Minecraft;

public class CommonProxy {
	public void init() {}

	public void loadList(String filename, HashMap<String, Boolean> list)	{}

	public void saveList(String filename, HashMap<String, Boolean> list)	{}

	public void loadOptions() {}

	public void saveOptions() {}

	public void refresh() {}

	public void drawRotatedTexturedModalRect(int posX, int posY, int texU, int texV, int width, int height, double angle, int colorIndex, Minecraft minecraft) {}

	public void drawTexturedHeadModalRect(int posX, int posY, int texU, int texV, int width, int height, Minecraft minecraft) {}

	public void registerTickHandler() {}
}
