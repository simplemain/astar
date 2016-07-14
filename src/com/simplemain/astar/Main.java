package com.simplemain.astar;


public class Main
{
	// 地图元素
	static final char START   = 'S';  // 起点
	static final char END     = 'E';  // 终点
	static final char SPACE   = '.';  // 空地
	static final char WALL    = 'W';  // 墙
	static final char VISITED = '-';  // 被访问过
	static final char ON_PATH = '@';  // 在结果路径上
	
	// 地图字符串
	static final String[] S_MAP = {
		". . . . . . . . . . . . . . . . . . . .", 
		". . . W W W W . . . . . . . . . . . . .",
		". . . . . . W . . . . . . . . . . . . .", 
		". . . . . . W . . . . . . . . . . . . .", 
		". . S . . . W . . . . . . . . . . . . .", 
		". . . . . . W . . . . . . . . . . . . .", 
		". . . . . . W . . . . . . . . . . . . .", 
		". . . . . . W . . . . . . . . . . . . .", 
		". . . W W W W . . . . . . . . . . . . .", 
		". . . . . . . . . . . . . . . . . E . ."
	};
	
	// 地图
	static char[][] MAP    = new char[S_MAP[0].replace(" ", "").length()][S_MAP.length];
	// 地图最大尺寸
	static Point MAX_PNT   = new Point(MAP.length, MAP[0].length);
	// 起点
	static Point START_PNT = null;
	// 终点
	static Point END_PNT   = null;
	
	public static void main(String[] args)
	{
		genMap();
		printMap();
		
		search();
		
		printMap();
	}

	/**
	 * 用地图字符串产生地图数据
	 */
	static void genMap()
	{
		int idx = 0;
		for (String s : S_MAP)
		{
			char[] cs = s.replace(" ", "").toCharArray();
			for (int i = 0; i < cs.length; i++)
			{
				MAP[i][idx] = cs[i];
				switch (cs[i])
				{
				case START:
					START_PNT = new Point(i, idx);
					break;
				case END:
					END_PNT = new Point(i, idx);
					break;
				}
			}
			idx++;
		}
	}

	/**
	 * 打印地图
	 */
	static void printMap()
	{
		for (int j = 0; j < MAX_PNT.y; j++)
		{
			for (int i = 0; i < MAX_PNT.x; i++)
			{
				System.out.printf("%c ", MAP[i][j]);
			}
			System.out.printf("\n");
		}
		System.out.printf("\n");
	}
	
	/**
	 * 搜索算法
	 */
	static void search()
	{
		final MinHeap heap = new MinHeap(); // 用最小堆来记录扩展的点
		final int[][] directs = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}}; // 可以扩展的四个方向
		
		heap.add(new Data(START_PNT, 0, 0, null)); // 把起始点放入堆
		Data lastData = null; // 找到的最后一个点的数据,用来反推路径
		
		for (boolean finish = false; !finish && !heap.isEmpty(); )
		{
			final Data data = heap.getAndRemoveMin(); // 取出f值最小的点
			final Point point = data.point;
			if (MAP[point.x][point.y] == SPACE) // 将取出的点标识为已访问点
			{
				MAP[point.x][point.y] = VISITED;
			}
			
			for (int[] d : directs) // 遍历四个方向的点
			{
				final Point newPnt = new Point(point.x + d[0], point.y + d[1]);
				if (newPnt.x >= 0 && newPnt.x < MAX_PNT.x && newPnt.y >= 0 && newPnt.y < MAX_PNT.y)
				{
					char e = MAP[newPnt.x][newPnt.y];
					if (e == END) // 如果是终点,则跳出循环,不用再找
					{
						lastData = data;
						finish = true;
						break;
					}
					if (e != SPACE) // 如果不是空地,就不需要再扩展
					{
						continue;
					}
					
					final Data inQueueData = heap.find(newPnt);
					if (inQueueData != null) // 如果在堆里,则更新g值
					{
						if (inQueueData.g > data.g + 1)
						{
							inQueueData.g = data.g + 1;
							inQueueData.parent = data;
						}
					}
					else // 如果不在堆里,则放入堆中
					{
						double h = h(newPnt);
						Data newData = new Data(newPnt, data.g + 1, h, data);
						heap.add(newData);
					}
				}
			}
		}

		// 反向找出路径
		for (Data pathData = lastData; pathData != null; ) 
		{
			Point pnt = pathData.point;
			if (MAP[pnt.x][pnt.y] == VISITED)
			{
				MAP[pnt.x][pnt.y] = ON_PATH;
			}
			pathData = pathData.parent;
			
		}
	}

	/**
	 * h函数
	 */
	static double h(Point pnt)
	{
//		return hBFS(pnt);
		return hEuclidianDistance(pnt);
//		return hPowEuclidianDistance(pnt);
//		return hManhattanDistance(pnt);
	}
	
	/**
	 * 曼哈顿距离,小于等于实际值
	 */
	static double hManhattanDistance(Point pnt)
	{
		return Math.abs(pnt.x - END_PNT.x) + Math.abs(pnt.y - END_PNT.y);
	}
	
	/**
	 * 欧式距离,小于等于实际值
	 */
	static double hEuclidianDistance(Point pnt)
	{
		return Math.sqrt(Math.pow(pnt.x - END_PNT.x, 2) + Math.pow(pnt.y - END_PNT.y, 2));
	}
	
	/**
	 * 欧式距离平方,大于等于实际值
	 */
	static double hPowEuclidianDistance(Point pnt)
	{
		return Math.pow(pnt.x - END_PNT.x, 2) + Math.pow(pnt.y - END_PNT.y, 2);
	}
	
	/**
	 * BFS的h值,恒为0
	 */
	static double hBFS(Point pnt)
	{
		return 0;
	}

}
