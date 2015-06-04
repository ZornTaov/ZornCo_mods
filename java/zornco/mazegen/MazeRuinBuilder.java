package zornco.mazegen;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class MazeRuinBuilder
{
	private Maze maze;
	private int[][] offset = new int[][] { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };
	private Block[] wallTypes = new Block[] {Blocks.stonebrick, Blocks.planks, Blocks.sand, Blocks.obsidian};
	private int startX, startY, startZ;
	public void buildWalls(World world, int x, int y, int z, int radius)
	{
		maze = new Maze();
		this.startX = x;
		this.startY = y;
		this.startZ = z;
		for (int i = 0; i < maze.getMapSize()[0]; i++)
		{
			for (int j = 0; j < maze.getMapSize()[1]; j++)
			{
				if(maze.getMap()[i][j] != null)
				{
					buildRoom(world, maze.getMap()[i][j], startX + (j - maze.getMapStart()[1]) * radius * 2, startY, startZ + (i - maze.getMapStart()[0]) * radius * 2, radius, 2);
				}
			}
		}
		
	}
	
	private void buildRoom(World world, Maze.Room room, int x, int y, int z, int radius, int height)
	{

		for (int i = 0 - radius; i < radius; i++) {
			for (int j = 0 - height; j < height; j++) {
				for (int k = 0 - radius; k < radius; k++) {
					world.setBlock(x + i, y + j, z + k, Blocks.air);
				}
			}
		}
		for (int i = 0; i < 4; i++)
		{
			if(!room.getThisRoom().getWallsOpen(i))
			{
				for (int j = 0 - radius; j <= radius; j++)
				{
					for (int k = 0 - height; k < height; k++)
					{
						world.setBlock(x + offset[i][0] * radius + (offset[i][1] != 0?j:0), y + k, z + offset[i][1] * radius + (offset[i][0] != 0?j:0), wallTypes[i]);
					}
				}
			}
			/*else
			{

				for (int j = 0 - radius; j <= radius; j++)
				{
					for (int k = 0; k < height; k++)
					{
						world.setBlock(x + offset[i][0] * radius + (offset[i][1] != 0?j:0), y + k, z + offset[i][1] * radius + (offset[i][0] != 0?j:0), Blocks.sandstone);
					}
				}
			}*/
		}
	}
}
