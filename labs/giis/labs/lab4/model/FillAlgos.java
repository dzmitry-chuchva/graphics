package giis.labs.lab4.model;

import giis.labs.base.api.IWorkArea;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.TreeMap;

public final class FillAlgos {
	
	// ���� ��������� �������
	private static Color markColor = Color.CYAN;
	// ���� ���������� ��� 
	private static Color sarColor = Color.RED;
	// ���� �������
	private static Color contourColor = Color.BLACK;
	
	private FillAlgos() {
		
	}
	
	/**
	 * ���������� �������� �������������� �� ��������� � ������������� ������� �������� �����.
	 * 
	 * @param area ������� ���������
	 * @param points ������ ��������� ����� ��������������
	 * @param color ���� �������
	 * @param debugMode ������� �� ���������� �����
	 * @param debugStep �� ����� ��� ��������� �������� 
	 */
	public static void fillSAR(IWorkArea area, Point[] points, Color color, boolean debugMode, int debugStep) {
		// ��������� ������, � ������� ����� ����������� ������ ���� ����� ��������������, ��������������� � ������� ����������� ������
		TreeMap<Integer,LinkedList<Edge>> edgeLists = new TreeMap<Integer,LinkedList<Edge>>();
		
		// ������� ������������ � ����������� y ����� ����� �������������� (��� ����������� ������ ��������� ������
		// ����������� ������)
		int maxY = points[0].y, minY = points[0].y;
		for (int i = 1; i < points.length; i++) {
			if (points[i].y > maxY) {
				maxY = points[i].y;
			}
			if (points[i].y < minY) {
				minY = points[i].y;
			}
		}
		
		// ��������� ������ ����� ��������������
		int start, end;
		for (int i = 1; i <= points.length; i++) {
			// ������� ������ ����� "���������" (��������� ����� � �������� ��������� ����� ������ ����� � �������)
			if (i == points.length) {
				start = 0;
			} else {
				start = i;
			}
			end = i - 1;
			
			// ���������� �������������� �����
			if (points[start].y == points[end].y) {
				continue;
			}
			
			// ��������� ��������� ���������� ����� (x, y, dx, dy) 
			int y, dy;
			double x;
			// ��������� �
			x = (points[start].y < points[end].y) ? points[end].x : points[start].x;
			// ��������� y, ��� �� ����� ����������� ������ ��� �������� �����
			y = (points[start].y < points[end].y) ? points[end].y : points[start].y;
			// ���������� ����������� �����, ������������ ������
			dy = Math.abs(points[start].y - points[end].y);
			// ���������� � �� ����� ����������� ������ � ������
			double dx = (double)(points[end].x - points[start].x) / (double)( - points[end].y + points[start].y);
			
			// ������� ������ ������ "�����" � ��������� ���
			Edge e = new Edge();
			e.setDx(dx);
			e.setDy(dy);
			e.setX(x);
			
			// ����������� ���������� ������ � ������� ����������� ������ (������ ��� � ������ �����, ��������������
			// �� ����������� �������)
			LinkedList<Edge> list = null;
			if (edgeLists.containsKey(y)) {
				list = edgeLists.get(y);
			} else {
				list = new LinkedList<Edge>();
				edgeLists.put(new Integer(y), list);
			}
			list.add(e);
		}
		
		int stepCounter = 0;

		// ��������� ����������� ������
		int y = maxY;
		// ������ �������� �����
		LinkedList<Edge> active = new LinkedList<Edge>();
		// ��������� � ���� ������ ����� (�� ��������� ����������� ������)
		if (edgeLists.containsKey(y)) {
			active.addAll(edgeLists.get(y));
		}
		
		StringBuffer buff = new StringBuffer("Scan Line\t\tSegment count\t\tSegments\n");
		// ���� �������� (���� ���� ����� � ���)
		do {
			// ��������� ��� �� ����������� �
			Collections.sort(active);
			buff.append(y + "\t\t" + active.size() + "\t\t");
			// ���� �� ����� ����� � ���
			for (Iterator<Edge> it = active.iterator(); it.hasNext(); ) {
				// �������� ����� �1
				Edge e1 = it.next();
				buff.append(e1 + ", ");
				
				// ��������� dy �� �������
				e1.setDy(e1.getDy() - 1);
				// ���� ����� ������ ����, ����� ������� �� ���
				if (e1.getDy() <= 0) {
					it.remove();
				}
				// �������� ����� �2
				Edge e2 = it.next();
				buff.append(e2 + ", ");
				
				// ��������� dy �� �������
				e2.setDy(e2.getDy() - 1);
				// ���� ����� ������ ����, ����� ������� �� ���
				if (e2.getDy() <= 0) {
					it.remove();
				}
				// ��������� ����� �������������� ������ ����� ��������
				area.line((int)Math.round(e1.getX()), y, (int)Math.round(e2.getX() - 0.25d), y, color);
				
				// ���������� � ����� �1 (���������� � �� d�) 
				processX(e1);
				// ���������� X ����� �2 (���������� � �� d�)
				processX(e2);
			}
			buff.append("\n");
			
			if (debugMode && stepCounter++ == debugStep) {
				drawContour(area, points, contourColor);
				return;
			}
			
			// ��������� � ��������� ����������� ������
			y--;
			// ��������� ����� �� ������� ����������� ������ (���� ����)
			if (edgeLists.containsKey(y)) {
				active.addAll(edgeLists.get(y));
			}
		} while (!active.isEmpty());
		
		System.out.println(buff.toString());
		
		drawContour(area, points, contourColor);
		if (debugMode) {
			area.stepDebugEnd();
		}
	}

	private static void processX(Edge e1) {
		e1.setX(e1.getX() + e1.getDx());
	}
	
	/**
	 * ���������� �������� �������������� �� ��������� ����������� ���������� � ���������.
	 * 
	 * @param area ������� ���������
	 * @param points ������ ��������� ����� ��������������
	 * @param color ���� �������
	 * @param debugMode ������� �� ���������� �����
	 * @param debugStep �� ����� ��� ��������� �������� 
	 */
	public static void fillColor(IWorkArea area, Point[] points, Color color, boolean debugMode, int debugStep) {
		// ���� ����������� ��������
		Stack<Point> stack = new Stack<Point>();
		Color colorColor = color;
		
		// ��������� ���������� ������ ��������
		Point first = calcInsidePixel(points);
		if (first == null) {
			area.setCurrentMessage("�� ������� ���������� ���������� ����� ��� ��������������.");
			return;
		} else {
			area.setCurrentMessage("");
		}
		
		// ������ ������� "����������� ������"
		drawContour(area,points,markColor);
		
		int stepCounter = 0;
		// ������ ������ �������� � ����
		stack.push(first);
		// ���� �������� (���� ���� ����������� �������� �� ����)
		while (!stack.isEmpty()) {
			// ������� ��������� ����������� ������ �� �����
			Point p = stack.pop();
			// ���������� ��� ������ ��������
			area.plot(p, colorColor);
			
			int x = p.x; int y = p.y;
			// �������� � ������������ �������
			int tmpX = x;
			
			// ����������� ������ ���� �� �������� �������
			x = tmpX + 1;
			while (!area.getPixel(x, y).equals(markColor)) {
				area.plot(x++, y, colorColor);
			}
			// �������� � ������ �������
			int rightX = x - 1;
			
			// �������� � �������� ������������ �������
			x = tmpX - 1;
			// ����������� ����� ���� �� �������� �������
			while (!area.getPixel(x, y).equals(markColor)) {
				area.plot(x--, y, colorColor);
			}
			// �������� � ����� �������
			int leftX = x + 1;
			
			// ��������, ���� �� �� ����� ������ � ����� �� ������ ������������� ������� � ��������� (leftX, rightX)
			// (���� ��� �����, ����� ����������� ���������� ����� ����������� ��������)
			check(area,stack,leftX,rightX,y + 1,colorColor);
			check(area,stack,leftX,rightX,y - 1,colorColor);
			
			if (debugMode && stepCounter++ == debugStep) {
				drawContour(area, points, contourColor);
				return;
			}
		}
		// ������ ������� ������� ������
		drawContour(area,points,contourColor);
		if (debugMode) {
			area.stepDebugEnd();
		}
	}
	
	/**
	 * ��������� ������� ��� ������������� �������� � �������������� �������.
	 * 
	 * @param area ������� ���������
	 * @param stack ���� ����������� ��������
	 * @param leftX ����� (���������) ���������� � �������
	 * @param rightX ������ (��������) ���������� � �������
	 * @param yy ���������� y ��������������� �������
	 * @param colorColor ���� ����������
	 */
	private static void check(IWorkArea area, Stack<Point> stack, int leftX, int rightX, int yy, Color colorColor) {
		int x = leftX;
		int y = yy;
		// ��������� �� ����� ������� �� ����� ������� �� ������
		while (x <= rightX) {
			// ��������� ������� ������� (� ���������� ���������������)
			boolean flag = false;
			while (!area.getPixel(x, y).equals(markColor) && !area.getPixel(x, y).equals(colorColor) && x <= rightX) {
				if (!flag) {
					flag = true;
				}
				x++;
			}
			
			// ������ ����������� ������ ������������ ������������� ������
			if (flag) {
				if (x == rightX && !area.getPixel(x,y).equals(markColor) && !area.getPixel(x, y).equals(colorColor)) {
					stack.push(new Point(x,y));
				} else {
					stack.push(new Point(x - 1,y));
				}
				flag = false;
			}
			
			// ���������� ��� ����������� �������
			int enterX = x;
			while ((area.getPixel(x, y).equals(markColor) || area.getPixel(x, y).equals(colorColor)) && x <= rightX) {
				x++;
			}
			if (enterX == x) {
				x++;
			}
		}
	}
	
	private static void drawContour(IWorkArea area,Point[] points, Color color) {
		for (int i = 1; i < points.length; i++) {
			area.line(points[i - 1], points[i], color);
		}
		area.line(points[0], points[points.length - 1], color);
	}
	
	private static Point calcInsidePixel1(Point[] points) {
		Point first = new Point();
		for (int i = 0; i < points.length; i++) {
			first.x += points[i].x;
			first.y += points[i].y;
		}
		first.x /= points.length;
		first.y /= points.length;
		return first;
	}
	
	private static Point calcInsidePixel(Point[] points) {
		return calcInsidePixel2(points);
	}
	
	private static Point calcInsidePixel2(Point[] points) {
		java.awt.Polygon poly = new java.awt.Polygon();
		for (int i = 0; i < points.length; i++) {
			poly.addPoint(points[i].x, points[i].y);
		}
		Area area = new Area(poly);
		
		Point try1 = calcInsidePixel1(points);
		if (area.contains(try1.x, try1.y)) {
			return try1;
		}
		
		return null;
		
//		Point vertex = points[1];
//		Point end1 = points[0];
//		Point end2 = points[2];
//		int oneQuater = getQuarter(new Line2D.Float(vertex,end1));
//		int anotherQuater = getQuarter(new Line2D.Float(vertex,end2));
	}
	
	private static int getQuarter(Line2D line) {
		double x = line.getX2() - line.getX1();
		double y = line.getY2() - line.getY1();
		if (x >= 0) {
			if (y >= 0) {
				return 1;
			} else {
				return 4;
			}
		} else {
			if (y >= 0) {
				return 2;
			} else {
				return 3;
			}
		}
	}
}
