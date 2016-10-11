package giis.labs.lab6.model;

import giis.labs.base.api.IWorkArea;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

public class ClipArea {
	
	private static final Color INSTALLED_POINTS_COLOR = Color.MAGENTA;
	private static final int NORMAL_DRAW_LENGTH = 5;
	private static final Color NORMALS_COLOR = Color.ORANGE;
	private static final Color CONTOUR_COLOR = Color.BLACK;
	
	private Point[] points;
	private List<Visibleness> visibleness = new ArrayList<Visibleness>();
	private Point2D.Double[] normals;
	
	public ClipArea(Point[] points) throws OperationNotSupportedException {
		this.points = points;
		
		// test protuberance
		if (!isGood()) {
			throw new OperationNotSupportedException("Многоугольник должен быть выпуклым!");
		}
		calcNormals();
	}
	
	public void shift(int dx, int dy) {
		for (int i = 0; i < points.length; i++) {
			points[i].x += dx;
			points[i].y += dy;
		}
		recalcVisibleness();
	}
	
	public void recalcVisibleness() {
		for (Iterator<Visibleness> it = visibleness.iterator(); it.hasNext(); ) {
			calcVisibleness(it.next());
		}
	}
	
	public void recalcVisiblenessFor(Segment segment) {
		for (Iterator<Visibleness> it = visibleness.iterator(); it.hasNext(); ) {
			Visibleness visible = it.next();
			if (visible.getSegment().equals(segment)) {
				calcVisibleness(visible);
				break;
			}
		}
	}
	
	public void addSegment(Segment segment) {
		Visibleness visible = new Visibleness(segment);
		calcVisibleness(visible);
		visibleness.add(visible);
	}
	
	public void draw(IWorkArea area, boolean drawNormals, boolean drawInvisible) {
		drawContour(area, points, CONTOUR_COLOR);
		
		if (drawNormals) {
			drawNormals(area, points, normals, NORMALS_COLOR);
		}
		
		for (Iterator<Visibleness> it = visibleness.iterator(); it.hasNext(); ) {
			Visibleness visible = it.next();
			Segment segment = visible.getSegment();
			if (visible.isVisible()) {
				Point vstart = segment.getPointAt(visible.getTStart());
				Point vend = segment.getPointAt(visible.getTEnd());
				if (drawInvisible) {
					area.line(segment.getBegin(), vstart, getInvisibleColor(segment.getColor()));
					area.line(vend, segment.getEnd(), getInvisibleColor(segment.getColor()));
				}
				area.line(vstart, vend, segment.getColor());
			} else {
				if (drawInvisible) {
					area.line(segment.getBegin(), segment.getEnd(), getInvisibleColor(segment.getColor()));
				}
			}
		}
	}
	
	private static void drawContour(IWorkArea area,Point[] points, Color color) {
		for (int i = 1; i < points.length; i++) {
			area.line(points[i - 1], points[i], color);
		}
		area.line(points[0], points[points.length - 1], color);
		
		for (int i = 0; i < points.length; i++) {
			area.plot(points[i], INSTALLED_POINTS_COLOR);
		}
	}
	
	private static void drawNormals(IWorkArea area,Point[] points, Point2D.Double[] normals, Color color) {
		int i;
		color = new Color(color.getRed(),color.getGreen(),color.getBlue(),64);
		for (i = 0; i < points.length - 1; i++) {
			Point middle = new Point((points[i + 1].x + points[i].x) / 2,(points[i + 1].y + points[i].y) / 2);
			//Point2D.Double normalized = normalize(normals[i],NORMAL_DRAW_LENGTH);
			area.line(middle.x,middle.y,(int)(middle.x + NORMAL_DRAW_LENGTH * normals[i].x),(int)(middle.y + NORMAL_DRAW_LENGTH * normals[i].y), color);
		}
		Point middle = new Point((points[0].x + points[i].x) / 2,(points[0].y + points[i].y) / 2);
		//Point2D.Double normalized = normalize(normals[i],NORMAL_DRAW_LENGTH);
		area.line(middle.x,middle.y,(int)(middle.x + NORMAL_DRAW_LENGTH * normals[i].x),(int)(middle.y + NORMAL_DRAW_LENGTH * normals[i].y), color);
	}
	
	private static Point2D.Double normalize(Point2D.Double normal, int coeff) {
		double length = Math.sqrt(normal.x * normal.x + normal.y * normal.y) / coeff;
		return new Point2D.Double(normal.x / length,normal.y / length);
	}
	
	public boolean isSingular() {
		java.awt.Polygon poly = getAWTPolygon();
		Area test = new Area(poly);
		return test.isSingular() && !test.isEmpty();
	}
	
	public java.awt.Polygon getAWTPolygon() {
		java.awt.Polygon poly = new java.awt.Polygon();
		for (int i = 0; i < points.length; i++) {
			poly.addPoint(points[i].x, points[i].y);
		}
		return poly;
	}

	public List<Segment> getSegments() {
		List<Segment> rv = new ArrayList<Segment>();
		for (Iterator<Visibleness> it = visibleness.iterator(); it.hasNext(); ) {
			rv.add(it.next().getSegment());
		}
		return rv;
	}
	
	private void calcNormals() {
		normals = new Point2D.Double[points.length];
		
		int i;
		for (i = 0; i < points.length - 1; i++) {
			normals[i] = new Point2D.Double();
			normals[i].x = - (points[i + 1].y - points[i].y);
			normals[i].y = points[i + 1].x - points[i].x;
			
			normals[i] = normalize(normals[i], 1);
		}
		
		normals[i] = new Point2D.Double();
		normals[i].x = - (points[0].y - points[i].y);
		normals[i].y = points[0].x - points[i].x;
		normals[i] = normalize(normals[i], 1);
		
		// test whether calced normals are inner
		Point innerPoint = calcInsidePixel(points);
		if (normals[0].x * innerPoint.x + normals[0].y * innerPoint.y < 0) {
			// revert all normals backwards
			for (i = 0; i < normals.length; i++) {
				normals[i].x = - normals[i].x;
				normals[i].y = - normals[i].y;
			}
		}
	}
	
	private void calcVisibleness(Visibleness visible) {
		// алгоритм отсечения Кируса-Бека
		
		// получаем концы отрезка
		Point P1 = visible.getSegment().getBegin();
		Point P2 = visible.getSegment().getEnd();
		
		// вычисляем направляющий вектор отрезка
		Point D = new Point(P2.x - P1.x,P2.y - P1.y);
		
		// инициализируем переменные
		double tstart = 0.0, tend = 1.0;
		boolean invisible = false;
		
		// цикл по ребрам области отсечения
		for (int i = 0; i < points.length; i++) {
			// получим очередную вершину области отсечения
			Point Fi = points[i];
			// вычислим вектор из вершины в начальную точку отрезка
			Point wi = new Point(P1.x - Fi.x,P1.y - Fi.y);
			
			// скалярные произведения (D * n, wi * n)
			double divide = (double)(wi.x * normals[i].x + wi.y * normals[i].y);
			double divider = (double)(D.x * normals[i].x + D.y * normals[i].y);
			
			// проверяем знаки скалярных произведений
			if (divider != 0) {
				double ti = - divide / divider;
				if (divider < 0) {
					if (ti < tstart) {
						invisible = true;
						break;
					}
					if (ti < tend) {
						tend = ti;
					}
				} else {
					if (ti > tend) {
						invisible = true;
						break;
					}
					if (ti > tstart) {
						tstart = ti;
					}
				}
			} else {
				if (divide < 0) {
					invisible = true;
					break;
				}
			}
		}
		if (!invisible) {
			if (tstart > tend) {
				invisible = true;
			}
		}
		
		if (invisible) {
			visible.makeInvisible();
		} else {
			visible.setTStart(tstart);
			visible.setTEnd(tend);
		}
	}
	
	private static Point calcInsidePixel(Point[] points) {
		Point first = new Point();
		for (int i = 0; i < points.length; i++) {
			first.x += points[i].x;
			first.y += points[i].y;
		}
		first.x /= points.length;
		first.y /= points.length;
		return first;
	}
	
	private boolean isGood() {
		if (isSingular()) {
			return true;
		}
		return false;
	}
	
	private static Color getInvisibleColor(Color color) {
		Color rv = new Color(color.getRed(),color.getGreen(),color.getBlue(),64);
		return rv;
	}

	public Point[] getPoints() {
		return points;
	}
	
	
}
