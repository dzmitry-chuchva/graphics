package giis.labs.lab4.model;

import giis.labs.base.api.IWorkArea;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Area;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Polygon {
	
	private Point[] points = null;
	private FillAlgoType algo;
	private Color color;
	
	private static Set<Color> colorCache = new HashSet<Color>();
	
	public Polygon(Point[] points, FillAlgoType algo) {
		this.points = points;
		this.algo = algo;
		setColor(randomColor());
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
	
	public void draw(IWorkArea area) {
		draw(area,false,0);
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void draw(IWorkArea area, boolean debugMode, int debugStep) {
		switch (algo) {
		case FILL_SAR:
			FillAlgos.fillSAR(area, points, getColor(), debugMode, debugStep);
			break;
		case FILL_COLOR:
			FillAlgos.fillColor(area, points, getColor(), debugMode, debugStep);
			break;
		}
	}

	public FillAlgoType getAlgo() {
		return algo;
	}

	public void setAlgo(FillAlgoType algo) {
		this.algo = algo;
	}

	public Point[] getPoints() {
		return points;
	}

	public void setPoints(Point[] points) {
		this.points = points;
	}
	
	private static Color randomColor() {
		Random colorGen = new Random(System.currentTimeMillis());
		Color c = null;;
		do {
			c = new Color(colorGen.nextInt(256),colorGen.nextInt(256),colorGen.nextInt(256));
		} while (colorCache.add(c));
		return c;
	}
}
