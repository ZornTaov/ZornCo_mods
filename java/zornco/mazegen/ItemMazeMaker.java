package zornco.mazegen;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMazeMaker extends Item
{
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if(!par3World.isRemote)
		{
		MazeRuinBuilder map = new MazeRuinBuilder();
		map.buildWalls(par3World, par4, par5, par6, 2);
		}
		return true;
	}
}
