package zornco.mazegen;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class MazeRatBuilder
{
	private RatMaze maze;
	private int[][] offset = new int[][] { { 0, -1 },  { 1, 0 }, { 0, 1 }, { -1, 0 } };
	private int startX, startY, startZ;
	public void buildWalls(World world, int x, int y, int z, int radius)
	{
		maze = new RatMaze();
		this.startX = x;
		this.startY = y;
		this.startZ = z;
		for (int r = 0; r < maze.getMapSize()[0]; r++)
		{
			for (int c = 0; c < maze.getMapSize()[1]; c++)
			{
				buildRoom(world, maze.getMap()[0][r][c], 
						startX + (c - maze.getMapStart()[1]) * radius * 2, 
						startY, 
						startZ + (r - maze.getMapStart()[0]) * radius * 2, 
						radius, 3);
			}
		}
		
	}
	
	private void buildRoom(World world, RatMaze.RatRoom room, int x, int y, int z, int radius, int height)
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
			if(!room.getWallsOpen(i))
			{
				for (int j = 0 - radius; j <= radius; j++)
				{
					for (int k = 0; k < height; k++)
					{
						world.setBlock(x + offset[i][0] * radius + (offset[i][1] != 0?j:0), y + k, z + offset[i][1] * radius + (offset[i][0] != 0?j:0), Blocks.stonebrick);
					}
				}
			}
		}
	}
}
