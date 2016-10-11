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
	
	// цвет временной границы
	private static Color markColor = Color.CYAN;
	// цвет заполнения САР 
	private static Color sarColor = Color.RED;
	// цвет контура
	private static Color contourColor = Color.BLACK;
	
	private FillAlgos() {
		
	}
	
	/**
	 * Производит закраску многоугольника по алгоритму с упорядоченным списком активных ребер.
	 * 
	 * @param area область рисования
	 * @param points массив координат точек многоугольника
	 * @param color цвет заливки
	 * @param debugMode включен ли отладочный режим
	 * @param debugStep по какой шаг выполнять алгоритм 
	 */
	public static void fillSAR(IWorkArea area, Point[] points, Color color, boolean debugMode, int debugStep) {
		// структура данных, в которой будет содержаться список всех ребер многоугольника, ассоциированных с номером сканирующей строки
		TreeMap<Integer,LinkedList<Edge>> edgeLists = new TreeMap<Integer,LinkedList<Edge>>();
		
		// находим максимальное и минимальное y среди точек многоугольника (для определения границ изменения номера
		// сканирующей строки)
		int maxY = points[0].y, minY = points[0].y;
		for (int i = 1; i < points.length; i++) {
			if (points[i].y > maxY) {
				maxY = points[i].y;
			}
			if (points[i].y < minY) {
				minY = points[i].y;
			}
		}
		
		// формируем список ребер многоугольника
		int start, end;
		for (int i = 1; i <= points.length; i++) {
			// сделаем список точек "замкнутым" (последняя точка в качестве следующей имеет первую точку в массиве)
			if (i == points.length) {
				start = 0;
			} else {
				start = i;
			}
			end = i - 1;
			
			// пропускаем горизонтальные ребра
			if (points[start].y == points[end].y) {
				continue;
			}
			
			// вычисляем параметры очередного ребра (x, y, dx, dy) 
			int y, dy;
			double x;
			// начальное х
			x = (points[start].y < points[end].y) ? points[end].x : points[start].x;
			// начальное y, оно же номер сканирующей строки для текущего ребра
			y = (points[start].y < points[end].y) ? points[end].y : points[start].y;
			// количество сканирующих строк, пересекаемых ребром
			dy = Math.abs(points[start].y - points[end].y);
			// приращение х от одной сканирующей строки к другой
			double dx = (double)(points[end].x - points[start].x) / (double)( - points[end].y + points[start].y);
			
			// создаем объект класса "Ребро" и заполняем его
			Edge e = new Edge();
			e.setDx(dx);
			e.setDy(dy);
			e.setX(x);
			
			// ассоциируем полученный объект с номером сканирующей строки (кладем его в список ребер, распределенный
			// по сканирующим строкам)
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

		// начальная сканирующая строка
		int y = maxY;
		// список активных ребер
		LinkedList<Edge> active = new LinkedList<Edge>();
		// добавляем в него первые ребра (по начальной сканирующей строке)
		if (edgeLists.containsKey(y)) {
			active.addAll(edgeLists.get(y));
		}
		
		StringBuffer buff = new StringBuffer("Scan Line\t\tSegment count\t\tSegments\n");
		// цикл закраски (пока есть ребра в САР)
		do {
			// сортируем САР по возрастанию х
			Collections.sort(active);
			buff.append(y + "\t\t" + active.size() + "\t\t");
			// цикл по парам ребер в САР
			for (Iterator<Edge> it = active.iterator(); it.hasNext(); ) {
				// получить ребро №1
				Edge e1 = it.next();
				buff.append(e1 + ", ");
				
				// уменьшить dy на единицу
				e1.setDy(e1.getDy() - 1);
				// если стало меньше нуля, ребро удалить из САР
				if (e1.getDy() <= 0) {
					it.remove();
				}
				// получить ребро №2
				Edge e2 = it.next();
				buff.append(e2 + ", ");
				
				// уменьшить dy на единицу
				e2.setDy(e2.getDy() - 1);
				// если стало меньше нуля, ребро удалить из САР
				if (e2.getDy() <= 0) {
					it.remove();
				}
				// соединить ребра горизонтальной линией цвета закраски
				area.line((int)Math.round(e1.getX()), y, (int)Math.round(e2.getX() - 0.25d), y, color);
				
				// обработать Х ребра №1 (прирастить х на dх) 
				processX(e1);
				// обработать X ребра №2 (прирастить х на dх)
				processX(e2);
			}
			buff.append("\n");
			
			if (debugMode && stepCounter++ == debugStep) {
				drawContour(area, points, contourColor);
				return;
			}
			
			// переходим к следующей сканирующей строке
			y--;
			// добавляем ребра на текущей сканирующей строке (если есть)
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
	 * Производит закраску многоугольника по алгоритму построчного заполнения с затравкой.
	 * 
	 * @param area область рисования
	 * @param points массив координат точек многоугольника
	 * @param color цвет заливки
	 * @param debugMode включен ли отладочный режим
	 * @param debugStep по какой шаг выполнять алгоритм 
	 */
	public static void fillColor(IWorkArea area, Point[] points, Color color, boolean debugMode, int debugStep) {
		// стек затравочных пикселей
		Stack<Point> stack = new Stack<Point>();
		Color colorColor = color;
		
		// вычисляем координаты первой затравки
		Point first = calcInsidePixel(points);
		if (first == null) {
			area.setCurrentMessage("Не удалось определить внутреннюю точку для многоугольника.");
			return;
		} else {
			area.setCurrentMessage("");
		}
		
		// рисуем границу "специальным цветом"
		drawContour(area,points,markColor);
		
		int stepCounter = 0;
		// кладем первую затравку в стек
		stack.push(first);
		// цикл закраски (пока стек затравочных пикселей не пуст)
		while (!stack.isEmpty()) {
			// достать очередной затравочный пиксел из стека
			Point p = stack.pop();
			// нарисовать его цветом закраски
			area.plot(p, colorColor);
			
			int x = p.x; int y = p.y;
			// запомним х затравочного пиксела
			int tmpX = x;
			
			// закрашиваем вправо пока не встретим границу
			x = tmpX + 1;
			while (!area.getPixel(x, y).equals(markColor)) {
				area.plot(x++, y, colorColor);
			}
			// запомним х правой границы
			int rightX = x - 1;
			
			// вспомним х текущего затравочного пиксела
			x = tmpX - 1;
			// закрашиваем влево пока не встретим границу
			while (!area.getPixel(x, y).equals(markColor)) {
				area.plot(x--, y, colorColor);
			}
			// запомним х левой границы
			int leftX = x + 1;
			
			// проверим, есть ли на линии сверху и снизу от данной незакрашенные пикселы в диапазоне (leftX, rightX)
			// (если они будут, будет произведено обновление стека затравочных пикселей)
			check(area,stack,leftX,rightX,y + 1,colorColor);
			check(area,stack,leftX,rightX,y - 1,colorColor);
			
			if (debugMode && stepCounter++ == debugStep) {
				drawContour(area, points, contourColor);
				return;
			}
		}
		// рисуем границу обычным цветом
		drawContour(area,points,contourColor);
		if (debugMode) {
			area.stepDebugEnd();
		}
	}
	
	/**
	 * Проверяет наличие еще незаполненных пикселей в горизонтальном отрезке.
	 * 
	 * @param area область рисования
	 * @param stack стек затравочных пикселей
	 * @param leftX левая (начальная) координата х отрезка
	 * @param rightX правая (конечная) координата х отрезка
	 * @param yy координата y горизонтального отрезка
	 * @param colorColor цвет заполнения
	 */
	private static void check(IWorkArea area, Stack<Point> stack, int leftX, int rightX, int yy, Color colorColor) {
		int x = leftX;
		int y = yy;
		// пробегаем по всему отрезку от левой границы до правой
		while (x <= rightX) {
			// проверяем наличие разрыва (у невыпуклых многоугольников)
			boolean flag = false;
			while (!area.getPixel(x, y).equals(markColor) && !area.getPixel(x, y).equals(colorColor) && x <= rightX) {
				if (!flag) {
					flag = true;
				}
				x++;
			}
			
			// кладем затравочный пиксел обнаруженной незаполненной строки
			if (flag) {
				if (x == rightX && !area.getPixel(x,y).equals(markColor) && !area.getPixel(x, y).equals(colorColor)) {
					stack.push(new Point(x,y));
				} else {
					stack.push(new Point(x - 1,y));
				}
				flag = false;
			}
			
			// пропускаем уже заполненные пикселы
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
