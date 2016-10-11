package giis.labs.lab3.model;

import giis.labs.lab3.gui.Lab3;

import java.awt.Color;
import java.awt.Point;

public interface InterpolatedLine {
	
	public static double tStep = 0.1;
		
	public void draw(Lab3 area);
	public void draw(Lab3 area, boolean debug, int debugStep);
	public void setType(FormType type);
	public FormType getType();
	public Point[] getChangeablePoints();
	public void setChangeablePoint(int index, Point value);
	public Color getColor();
	public Color getMarkerColor();
	
	public double getLastT();
	public Point[] getLastPoints();

}
