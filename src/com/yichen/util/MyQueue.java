package com.yichen.util;

import java.util.LinkedList;
import java.util.Queue;

public class MyQueue<T> {
	public Queue<T> delAElem(T t, Queue<T> queue) {
		Queue<T> q = null;
		boolean f = false;
		if (queue != null) {
			q = new LinkedList<T>();
			while (queue.peek() != null) {
				T tmp = queue.poll();
				if (!tmp.equals(t)) {
					q.offer(tmp);
				}
			}

		}
		return q;
	}
}
