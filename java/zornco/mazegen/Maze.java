package zornco.mazegen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maze
{

	private int sizeX = 20;
	private int sizeY = 20;
	private Room[][] map = new Room[sizeX][sizeY];
	private Random rand = new Random();
	private int startX;
	private int startY;
	private List<EnumRooms> lastBranch = new ArrayList<EnumRooms>();
	private int[][] offset = new int[][] { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };

	Maze()
	{
		generateMap();
		
		//System.out.println("----------------------");
		printmap();
	}
	public Room[][] getMap()
	{
		return map;
	}
	public int[] getMapSize()
	{
		return new int[] {sizeX, sizeY};
	}
	public int[] getMapStart()
	{
		return new int[] {startX, startY};
	}
	protected void generateMap()
	{
		this.startX = rand.nextInt(sizeX - 2) + 1;
		this.startY = rand.nextInt(sizeY - 2) + 1;
		int i = rand.nextInt(4);
		map[startX][this.startY] = new Room(getRoom(i + 1), startX, this.startY);
		map[sizeX/2][sizeY/2] =  new Room(getRoom(15), startX, this.startY);
		map[sizeX/2+1][sizeY/2] =  new Room(getRoom(15), startX, this.startY);
		map[sizeX/2-1][sizeY/2] =  new Room(getRoom(15), startX, this.startY);
		map[sizeX/2][sizeY/2+1] =  new Room(getRoom(15), startX, this.startY);
		map[sizeX/2][sizeY/2-1] =  new Room(getRoom(15), startX, this.startY);
		map[sizeX/2+1][sizeY/2+1] =  new Room(getRoom(15), startX, this.startY);
		map[sizeX/2-1][sizeY/2+1] =  new Room(getRoom(15), startX, this.startY);
		map[sizeX/2+1][sizeY/2-1] =  new Room(getRoom(15), startX, this.startY);
		map[sizeX/2-1][sizeY/2-1] =  new Room(getRoom(15), startX, this.startY);
		System.out.println(map[startX][this.startY] + " " + i + " start");
		makeNextRoom(map[startX][this.startY], startX, this.startY);
		//randomMap();
	}

	protected void makeNextRoom(Room prev, int x2, int y2)
	{
		for (int i = 0; i < 4; i++)
		{
			if (x2 + offset[i][1] < sizeX && y2 + offset[i][0] < sizeY && x2 + offset[i][1] > -1 && y2 + offset[i][0] > -1)
				if (prev.getThisRoom().getWallsOpen(i) && map[x2 + offset[i][1]][y2 + offset[i][0]] == null)
				{
					int next = prev.getThisRoom().getNextValid(i)[rand.nextInt(prev.getThisRoom().getNextValid(i).length - 1) + 1];
					map[x2 + offset[i][1]][y2 + offset[i][0]] = new Room( getRoom(next), x2 + offset[i][1], y2 + offset[i][0]);
					
					//System.out.printf("%2s %d\n", map[x2 + offset[i][1]][y2 + offset[i][0]], i);
					makeNextRoom(map[x2 + offset[i][1]][y2 + offset[i][0]], x2 + offset[i][1], y2 + offset[i][0]);
				}
		}
		return;
	}
	
	private void randomMap()
	{
		for (int i = 0; i < sizeX; i++)
		{
			for (int j = 0; j < sizeY; j++)
			{
				map[i][j] = new Room(EnumRooms.values()[rand.nextInt(14)+1], i, j);
			}
		}
	}

	private void printmap()
	{
		for (int i = 0; i < map.length; i++)
		{
			for (int j = 0; j < map.length; j++)
			{
				if (map[i][j] != null)
				{
					System.out.printf("%3d", map[i][j].getThisRoom().getIndex());
				}
				else
				{
					System.out.printf("%3d", 0);
				}
			}
			System.out.println("\n");
		}
	}
	protected EnumRooms getRoom(int i)
	{
		switch (i)
		{
		case 1:
			return EnumRooms.N;

		case 2:
			return EnumRooms.S;

		case 3:
			return EnumRooms.W;

		case 4:
			return EnumRooms.E;

		case 5:
			return EnumRooms.NW;

		case 6:
			return EnumRooms.NE;

		case 7:
			return EnumRooms.SW;

		case 8:
			return EnumRooms.SE;

		case 9:
			return EnumRooms.NWE;

		case 10:
			return EnumRooms.SWE;

		case 11:
			return EnumRooms.WNS;

		case 12:
			return EnumRooms.ENS;

		case 13:
			return EnumRooms.NS;

		case 14:
			return EnumRooms.WE;

		case 15:
			return EnumRooms.NSWE;

		default:
		case 16:
			return EnumRooms.C;
		}

	}
	public class Room
	{
		private final EnumRooms thisRoom;
		// final Object specialRoom;
		private final int x;
		private final int y;

		public Room(EnumRooms thisRoom, int x, int y)
		{
			this.thisRoom = thisRoom;
			this.x = x;
			this.y = y;
		}

		public EnumRooms getThisRoom()
		{
			return thisRoom;
		}

		public int getY()
		{
			return y;
		}

		public int getX()
		{
			return x;
		}
		@Override
		public String toString()
		{
			String result = String.format("%3d%3d%3d", (this.getX() + 1), (this.getY() + 1), this.getThisRoom().getIndex()); 
			return result;
		}
	}

	public enum EnumRooms
	{
		N(1,     new int[][] { { 2, 7, 8, 10, 11, 12, 13, 15 }, {0},                            {0},                            {0}                           }),
		S(2,     new int[][] { {0},                             { 1, 5, 6, 9, 11, 12, 13, 15 }, {0},                            {0}                           }),
		W(3,     new int[][] { {0},                             {0},                            { 4, 6, 8, 9, 10, 12, 14, 15},  {0}                           }),
		E(4,     new int[][] { {0},                             {0},                            {0},                            { 3, 5, 7, 9, 10, 11, 14, 15} }),
                 
		NW(5,    new int[][] { { 2, 7, 8, 10, 11, 12, 13, 15 }, {0},                            { 4, 6, 8, 9, 10, 12, 14, 15},  {0}                           }),
		NE(6,    new int[][] { { 2, 7, 8, 10, 11, 12, 13, 15 }, {0},                            {0},                            { 3, 5, 7, 9, 10, 11, 14, 15} }),
		SW(7,    new int[][] { {0},                             { 1, 5, 6, 9, 11, 12, 13, 15 }, { 4, 6, 8, 9, 10, 12, 14, 15},  {0}                           }),
		SE(8,    new int[][] { {0},                             { 1, 5, 6, 9, 11, 12, 13, 15 }, {0},                            { 3, 5, 7, 9, 10, 11, 14, 15} }),
                 
		NWE(9,   new int[][] { { 2, 7, 8, 10, 11, 12, 13, 15 }, {0},                            { 4, 6, 8, 9, 10, 12, 14, 15},  { 3, 5, 7, 9, 10, 11, 14, 15} }),
		SWE(10,  new int[][] { {0},                             { 1, 5, 6, 9, 11, 12, 13, 15 }, { 4, 6, 8, 9, 10, 12, 14, 15},  { 3, 5, 7, 9, 10, 11, 14, 15} }),
                 
		WNS(11,  new int[][] { { 2, 7, 8, 10, 11, 12, 13, 15 }, { 1, 5, 6, 9, 11, 12, 13, 15 }, { 4, 6, 8, 9, 10, 12, 14, 15},  {0}                           }),
		ENS(12,  new int[][] { { 2, 7, 8, 10, 11, 12, 13, 15 }, { 1, 5, 6, 9, 11, 12, 13, 15 }, {0},                            { 3, 5, 7, 9, 10, 11, 14, 15} }),
                 
		NS(13,   new int[][] { { 2, 7, 8, 10, 11, 12, 13, 15 }, { 1, 5, 6, 9, 11, 12, 13, 15 }, {0},                            {0}                           }),
		WE(14,   new int[][] { {0},                             {0},                            { 4, 6, 8, 9, 10, 12, 14, 15},  { 3, 5, 7, 9, 10, 11, 14, 15} }),
                 
		NSWE(15, new int[][] { { 2, 7, 8, 10, 11, 12, 13, 15 }, { 1, 5, 6, 9, 11, 12, 13, 15 }, { 4, 6, 8, 9, 10, 12, 14, 15},  { 3, 5, 7, 9, 10, 11, 14, 15} }),
		C(16,    new int[][] { {0},                             {0},                            {0},                            {0}                           });

		final int index;
		final int[][] nextValid;
		
		EnumRooms(int index, int[][] next)
		{
			this.index = index;
			this.nextValid = next;
		}

		public int getIndex()
		{
			return index;
		}

		public boolean getWallsOpen(int i)
		{
			return nextValid[i][0] != 0;
		}

		public int[] getNextValid(int i)
		{
			return nextValid[i];
		}
	}
}