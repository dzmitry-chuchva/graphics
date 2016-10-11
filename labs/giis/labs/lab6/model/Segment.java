package giis.labs.lab6.model;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class Segment {
	
	private Point begin;
	private Point end;
	private Color color; 
	
	public Segment(Point begin, Point end) {
		this.begin = begin;
		this.end = end;
		this.color = randomColor();
	}
	
	public Point getPointAt(double t) {
		Point rv = new Point();
		rv.x = (int)(begin.x + t * (end.x - begin.x));
		rv.y = (int)(begin.y + t * (end.y - begin.y));
		return rv;
	}

	public Point getBegin() {
		return begin;
	}

	public void setBegin(Point begin) {
		this.begin = begin;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	private static Color randomColor() {
		Random colorGen = new Random(System.currentTimeMillis());
		Color c = new Color(colorGen.nextInt(256),colorGen.nextInt(256),colorGen.nextInt(256));
		return c;
	}
}
