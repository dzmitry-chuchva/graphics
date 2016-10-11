package giis.labs.lab3.model;

import java.awt.Color;
import java.awt.Point;

abstract public class BaseForm implements InterpolatedLine {

	protected FormType type;
	protected Color color, markerColor;
	
	protected double lastT = 0.0d;
	protected Point[] lastPoints = null;
	
	public FormType getType() {
		return type;
	}

	public void setType(FormType type) {
		this.type = type;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getMarkerColor() {
		return markerColor;
	}

	public void setMarkerColor(Color markerColor) {
		this.markerColor = markerColor;
	}

	public Point[] getLastPoints() {
		return lastPoints;
	}

	public void setLastPoints(Point[] lastPoints) {
		this.lastPoints = lastPoints;
	}

	public double getLastT() {
		return lastT;
	}

	public void setLastT(double lastT) {
		this.lastT = lastT;
	}

}
