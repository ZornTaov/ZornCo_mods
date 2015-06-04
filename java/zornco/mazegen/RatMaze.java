package zornco.mazegen;

import java.util.Random;

public class RatMaze
{
	private int sizeX = 100;
	private int sizeY = 100;
	private RatRoom[][][] dungeon = new RatRoom[1][sizeX][sizeY];
	private Random rand = new Random();
	private int startX;
	private int startY;
	

	public RatMaze()
	{
		empty_floor(0);
		new_floor(0);
	}
	public RatRoom[][][] getMap()
	{
		return dungeon;
	}
	public int[] getMapSize()
	{
		return new int[] {sizeY, sizeX};
	}
	public int[] getMapStart()
	{
		return new int[] {startY, startX};
	}

	class RatRoom
	{
		public WallType n = WallType.WALL,
				e = WallType.WALL,
				s = WallType.WALL,
				w = WallType.WALL;
		public boolean v = false;
		public boolean getWallsOpen(int i)
		{
			switch (i)
			{
			case DIR_NORTH:
				return n == WallType.OPENING;

			case DIR_SOUTH:
				return s == WallType.OPENING;

			case DIR_EAST:
				return e == WallType.OPENING;

			case DIR_WEST:
				return w == WallType.OPENING;

			}
			return true;
		}		
	}
	enum WallType {
		OPENING,
		SOLID_WALL,
		WALL
	}
	
	public final static int DIR_NORTH = 0;
	public final static int DIR_EAST  = 1;
	public final static int DIR_SOUTH = 2;
	public final static int DIR_WEST  = 3;
	
	class TunnelStep
	{
		int row;
		int col;
		TunnelStep(int r, int c)
		{
			this.row = r;
			this.col = c;
		}
	}
	// map stuff ff used by new game to set up fioor structures 
	private void empty_floor(int floor_num) {
		int r,c;
		for(r = 0; r < sizeY; r++) {
			for(c = 0; c < sizeX; c++) {
				dungeon[floor_num][r][c] = new RatRoom(); 
			}
		}
	}
	// dig floor 
	public void new_floor(int floor_num) { 
		int r,c,depth;
		TunnelStep[] tunnel = new TunnelStep[sizeX*sizeY];
		int dir,odir;
		boolean found,search;
		boolean[][] visited = new boolean[sizeX][sizeY]; 
		
		for(r = 0; r < sizeY; r++) { 
			for(c = 0; c < sizeX; c++) {  
				visited[r][c] = false; 
			}
		}
		
		// fix the edges of the map 
		for(r = 0; r < sizeY; r++) { 
			dungeon[floor_num][r][0].w = WallType.SOLID_WALL;
			dungeon[floor_num][r][sizeX - 1].e = WallType.SOLID_WALL;
		}
		for(c = 0; c < sizeX; c++) {
			dungeon[floor_num][0][c].n = WallType.SOLID_WALL; 
			dungeon[floor_num][sizeY - 1][c].s = WallType.SOLID_WALL; 
		}
		// starting Cell 
		startY = r = rand.nextInt(sizeY); 
		startX = c = rand.nextInt(sizeX); 
		visited[r][c] = true; 
		depth = 0;
		tunnel[depth]= new TunnelStep(r, c);

		System.out.println(depth + " " + r + " " + c);
		while(true) { 
			dir = rand.nextInt(4);
			odir = dir;
			found = false;
			search = false;
			while(!found) {
				switch(dir) {
					case DIR_NORTH: // north 
						if(dungeon[floor_num][r][c].n == WallType.WALL && !visited[r - 1][c]) {
						dungeon[floor_num][r][c].n = WallType.OPENING;
						r--;
						dungeon[floor_num][r][c].s = WallType.OPENING;
						found = true;
					}
					break;
					case DIR_EAST: // east 
						if(dungeon[floor_num][r][c].e == WallType.WALL && !visited[r][c + 1]) {
						dungeon[floor_num][r][c].e = WallType.OPENING;
						c++;
						dungeon[floor_num][r][c].w = WallType.OPENING;
						found = true;
					}
					break;
					case DIR_SOUTH: // south 
						if(dungeon[floor_num][r][c].s == WallType.WALL && !visited[r + 1][c]) {
						dungeon[floor_num][r][c].s = WallType.OPENING;
						r++; 
						dungeon[floor_num][r][c].n = WallType.OPENING;
						found = true;
					}
					break;
					case DIR_WEST: // west 
						if(dungeon[floor_num][r][c].w == WallType.WALL && !visited[r][c - 1]) {
						dungeon[floor_num][r][c].w = WallType.OPENING;
						c--;
						dungeon[floor_num][r][c].e = WallType.OPENING;
						found = true;
					}
					break;
				}
				if(!found) {
					// did we not find a vaiid dir?
					dir++;
					if(dir > 3) {
						dir = 0;
					}
					if(dir == odir) {
						search = true;
						break;
					}
				} else {
					depth++;
					tunnel[depth]= new TunnelStep(r, c);
					System.out.println(depth + " " + r + " " + c);
					visited[r][c] = true;
				}
			}
			if(search) {
				// find a ceii to branch from 
				if(depth > 0) {
					tunnel[depth]= null;
					depth--;
					r = tunnel[depth].row;
					c = tunnel[depth].col;
					System.out.println(depth + " " + r + " " + c);
				} else {
					break; 
				}
			}
		}
		// fix the edges of the map 
		for(r = 0; r < sizeY; r++) { 
			dungeon[floor_num][r][0].w = WallType.WALL;
			dungeon[floor_num][r][sizeX - 1].e = WallType.WALL;
		}
		for(c = 0; c < sizeX; c++) { 
			dungeon[floor_num][0][c].n = WallType.WALL;
			dungeon[floor_num][sizeY - 1][c].s = WallType.WALL; 
		}
	}
}
