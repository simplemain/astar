package com.simplemain.astar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MinHeap
{
	private final ArrayList<Data> queue = new ArrayList<>();  // 用于存储堆的数组列表
	private int endPnt = 0;  // 标记堆的尾部位置
	private final Map<String, Data> index = new HashMap<>();  // 用于快速查找堆中元素的映射
	
	public Data getAndRemoveMin()
	{
		if (isEmpty())
		{
			return null;
		}
		
		Data head = queue.get(0);  // 获取堆的最小元素
		Data last = queue.get(endPnt - 1);  // 获取堆的最后一个元素
		queue.set(0, last);  // 将最后一个元素放到堆顶
		endPnt--;
		index.remove(getKey(head.point));  // 从映射中移除堆的最小元素
		
		topDown();  // 重新构建堆
		
		return head;
	}
	
	public Data find(Point pnt)
	{
		return index.get(getKey(pnt));
	}  // 在映射中查找指定点的数据对象
	
	public void add(Data data)
	{
		if (queue.size() > endPnt)
		{
			queue.set(endPnt, data);
		}
		else
		{
			queue.add(data);
		}
		endPnt++;
		
		index.put(getKey(data.point), data);  // 在映射中添加点和数据对象的映射关系
		
		bottomUp();
	}
	
	public boolean isEmpty()
	{
		return endPnt <= 0;
	}
	
	private String getKey(Point pnt)
	{
		return String.format("%d-%d", pnt.x, pnt.y);
	}  // 根据点的坐标生成映射的键值
	
	private void topDown()
	{
		for (int cur = 0; cur < endPnt; )
		{
			int left  = 2 * cur + 1;
			int right = 2 * cur + 2;
			
			Data dc = queue.get(cur);
			Data dl = left < endPnt ? queue.get(left) : null;
			Data dr = right < endPnt ? queue.get(right) : null;
			
			int next = -1;
			Data dn = dc;
			if (dl != null && dl.f() < dn.f())  // 如果左子节点存在且左子节点的f值小于当前节点的f值
			{
				next = left;
				dn = dl;
			}
			if (dr != null && dr.f() < dn.f())  // 如果右子节点存在且右子节点的f值小于当前节点和左子节点的f值
			{
				next = right;
				dn = dr;
			}
			
			if (next >= 0 && next < endPnt)  // 如果存在需要交换的子节点
			{
				queue.set(next, dc);
				queue.set(cur, dn);
				cur = next;
			}
			else
			{
				break;
			}
		}
		
		
	}

	private void bottomUp()
	{
		for (int cur = endPnt - 1; cur >= 0; )
		{
			int parent = (cur - 1) / 2;
			if (parent < 0)
			{
				break;
			}
			
			Data dc = queue.get(cur);
			Data dp = queue.get(parent);
			
			if (dc.f() < dp.f())   // 如果当前节点的f值小于父节点的f值
			{
				queue.set(parent, dc);
				queue.set(cur, dp);
				cur = parent;
			}
			else
			{
				break;
			}
		}
	}
}
