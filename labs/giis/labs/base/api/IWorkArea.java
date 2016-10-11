package giis.labs.base.api;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JToolBar;

public interface IWorkArea extends ComponentListener, MouseListener, MouseMotionListener {
	
	public String getShortDescription();
	public String getLongDescription();
	
	public JMenu[] getMenuBar();
	public JToolBar getToolBar();
	public JComponent getComponent();
	
	public void setCellSize(int cellSize);
	public int getCellSize();
	
	public int getSideSize();
	
	public void setDebugMode(boolean mode);
	public boolean isDebugMode();
	public void stepDebug();
	public void stepDebugEnd();
	
	public void plot(Point p, Color c);
	public void plot(int x, int y, Color c);
	public void line(Point p1, Point p2, Color c);
	public void line(int x1, int y1, int x2, int y2, Color c);
	public Color getPixel(Point p);
	public Color getPixel(int x, int y);
	
	public void clearArea();
	
	public String getCurrentMessage();
	public void setCurrentMessage(String currentMessage);
}