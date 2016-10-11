package giis.labs.lab3.gui;

import giis.labs.base.api.IWorkArea;
import giis.labs.base.impl.AbstractArea;
import giis.labs.lab3.actions.Lab3ActionContainer;
import giis.labs.lab3.model.BSplineForm;
import giis.labs.lab3.model.BezierLine;
import giis.labs.lab3.model.ErmitLine;
import giis.labs.lab3.model.FormType;
import giis.labs.lab3.model.InterpolatedLine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class Lab3 extends AbstractArea implements IWorkArea, KeyListener {
	
	private final String shortDescription = "Л.р. №3";
	private final String longDescription = "Лабораторная работа №3. Интерполяция и аппроксимация кривых.";
	
	private Vector<InterpolatedLine> forms = new Vector<InterpolatedLine>();
	private Vector<Point> currentPoints = new Vector<Point>();
	private FormType currentType = FormType.ERMIT;  
	
	private Point draggingPoint = null;
	private MarkerTest lastMarkerTest = null;
	
	private static final Color tmpDotsColor = Color.YELLOW;
	private static final Color instDotsColor = Color.PINK;
	
	private boolean debugMode = false;
	private InterpolatedLine debugForm = null;
	private int debugStep = 0;
	
	public Lab3() {
		super();
		
		addKeyListener(this);
		
		Lab3ActionContainer.initializeFactory(this);
		
		createMenu();
		createToolbar();
		
		setShortDescription(shortDescription);
		setLongDescription(longDescription);
		
		addComponentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	private JMenu[] createMenu() {
		JMenu menu, submenu;
		JMenuItem menuItem;
		
		List<JMenu> menus = new ArrayList<JMenu>();
		
		menu = new JMenu("Интерполирование");
		menus.add(menu);
		
		ButtonGroup bg = new ButtonGroup();
		
		menuItem = new JRadioButtonMenuItem(Lab3ActionContainer.getErmitAction());
		menu.add(menuItem);
		bg.add(menuItem);
				
		menuItem = new JRadioButtonMenuItem(Lab3ActionContainer.getBezierAction());
		menu.add(menuItem);
		bg.add(menuItem);
		
		menuItem = new JRadioButtonMenuItem(Lab3ActionContainer.getBSplineAction());
		menu.add(menuItem);
		bg.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem(Lab3ActionContainer.getClearAction());
		menu.add(menuItem);
		
		menu = new JMenu("Отладка");
		menus.add(menu);
		menuItem = new JCheckBoxMenuItem(Lab3ActionContainer.getDebugAction());
		menu.add(menuItem);
		menu.addSeparator();		
		menu.add(Lab3ActionContainer.getStepAction());
		menu.add(Lab3ActionContainer.getStepAllAction());
		
		menuBar = menus.toArray(new JMenu[0]);
		
		return menuBar;
	}
	
	private JToolBar createToolbar() {
		toolBar = new JToolBar("Toolbar",JToolBar.HORIZONTAL);
		
		ButtonGroup bg = new ButtonGroup();
		JToggleButton button = new JToggleButton(Lab3ActionContainer.getErmitAction());
		toolBar.add(button);
		bg.add(button);
		
		button = new JToggleButton(Lab3ActionContainer.getBezierAction());
		toolBar.add(button);
		bg.add(button);
		
		button = new JToggleButton(Lab3ActionContainer.getBSplineAction());
		toolBar.add(button);
		bg.add(button);
		
		toolBar.addSeparator();
		
		toolBar.add(Lab3ActionContainer.getClearAction());
		
		toolBar.addSeparator();
		
		toolBar.add(new JToggleButton(Lab3ActionContainer.getDebugAction()));
		toolBar.add(Lab3ActionContainer.getStepAction());
		toolBar.add(Lab3ActionContainer.getStepAllAction());
		
		return toolBar;
	}

	public void clearArea() {
		forms.clear();
		repaint();
		currentPoints.clear();
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean mode) {
		requestFocusInWindow();
		debugMode = mode;
	}

	public void stepDebug() {
		requestFocusInWindow();
		if (debugForm != null) {
			debugStep++;
			repaint();
		}
	}

	public void stepDebugEnd() {
		requestFocusInWindow();
		if (debugForm != null) {
			debugStep = 0;
			forms.add(debugForm);
			debugForm = null;
			
			Lab3ActionContainer.getClearAction().setEnabled(true);
			Lab3ActionContainer.getBezierAction().setEnabled(true);
			Lab3ActionContainer.getErmitAction().setEnabled(true);
			Lab3ActionContainer.getBSplineAction().setEnabled(true);
			Lab3ActionContainer.getDebugAction().setEnabled(true);
			Lab3ActionContainer.getStepAction().setEnabled(false);
			Lab3ActionContainer.getStepAllAction().setEnabled(false);
			
			setCurrentMessage("");
			
			repaint();
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		float hsb[] = new float [3];
		for (Iterator<InterpolatedLine> it = forms.iterator(); it.hasNext(); ) {
			InterpolatedLine line = it.next();
			Point[] markers = line.getChangeablePoints();
					
			line.draw(this);
			
			for (int i = 0; i < markers.length; i++) {
				plot(markers[i],line.getMarkerColor());
			}
			
		}
		
		for (Iterator<Point> it = currentPoints.iterator(); it.hasNext(); ) {
			plot(it.next(), tmpDotsColor);
		}
		
		if (debugMode && debugForm != null) {
			debugForm.draw(this, debugMode, debugStep);
			if (debugForm != null) {
				// can be null because of draw calls stepEndDebug(), which cleares instance
				
				StringBuffer buff = new StringBuffer("t = " + String.format("%.2f", debugForm.getLastT()) + ": ");
				Point[] pts = debugForm.getLastPoints();
				if (pts.length == 1) {
					buff.append("point (" + pts[0].x + ", " + pts[0].y + ")");
				} else {
					buff.append("points: ");
					for (int i = 0; i < pts.length; i++) {
						buff.append("("  + pts[i].x + "," + pts[i].y + "), ");
					}
					buff.delete(buff.length() - 2, buff.length());
				}
				setCurrentMessage(buff.toString());
			}
		}
		
		endPaintComponent();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (draggingPoint != null) {
			return;
		}
		
		requestFocusInWindow();
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			currentPoints.add(translate(arg0.getPoint()));
			switch (currentType) {
			case ERMIT:
				if (currentPoints.size() == 4) {
					Point[] points = currentPoints.toArray(new Point[0]);
					addForm(new ErmitLine(points[0],points[1],points[2],points[3]));
					currentPoints.clear();
				}
				break;
			case BEZIER:
				if (currentPoints.size() == 4) {
					Point[] points = currentPoints.toArray(new Point[0]);
					addForm(new BezierLine(points[0],points[1],points[2],points[3]));
					currentPoints.clear();
				}
				break;
			case BSPLINE:
				break;
			}
		} else if (arg0.getButton() == MouseEvent.BUTTON3) {
			currentPoints.clear();
		}
		repaint();
	}
	
	private void addForm(InterpolatedLine line) {
		if (debugMode) {
			debugForm = line;
			debugStep = 0;
			Lab3ActionContainer.getClearAction().setEnabled(false);
			Lab3ActionContainer.getBezierAction().setEnabled(false);
			Lab3ActionContainer.getErmitAction().setEnabled(false);
			Lab3ActionContainer.getBSplineAction().setEnabled(false);
			Lab3ActionContainer.getDebugAction().setEnabled(false);
			Lab3ActionContainer.getStepAction().setEnabled(true);
			Lab3ActionContainer.getStepAllAction().setEnabled(true);
		} else {
			forms.add(line);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (draggingPoint != null) {
			draggingPoint = translate(arg0.getPoint());
			lastMarkerTest.getMarkerLine().setChangeablePoint(lastMarkerTest.getMarkerIndex(), draggingPoint);
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (draggingPoint != null) {
			draggingPoint = translate(arg0.getPoint());
			lastMarkerTest.getMarkerLine().setChangeablePoint(lastMarkerTest.getMarkerIndex(), draggingPoint);
			draggingPoint = null;
			lastMarkerTest = null;
			repaint();			
		}
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		if (currentPoints.size() == 0) {
			Point p = translate(arg0.getPoint());
			MarkerTest marker = new MarkerTest();
			if (marker.isMarker(p)) {
				draggingPoint = p;
				lastMarkerTest = marker;
				repaint();
			}
		}
	}

	public void keyPressed(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			if (currentType == FormType.BSPLINE) {
				if (currentPoints.size() >= 4) {
					Point[] points = currentPoints.toArray(new Point[0]);
					addForm(new BSplineForm(points));
					currentPoints.clear();
					repaint();
				} else {
					JOptionPane.showMessageDialog(this, "Недостаточно точек. Необходимо минимум четыре!");
				}
			}
		}
	}

	public void keyTyped(KeyEvent arg0) {
	}
	
	
	
	@Override
	protected void recalcSize() {
		super.recalcSize();
		// remove clipped forms
		int side  = getSideSize() / getCellSize();
		Area clipArea = new Area(new Rectangle(0,0, side, side));
		
		for (Iterator<InterpolatedLine> it = forms.iterator(); it.hasNext(); ) {
			InterpolatedLine line = it.next();
			Point[] pts = line.getChangeablePoints();
			for (int i = 0; i < pts.length; i++) {
				if (!clipArea.contains(pts[i])) {
					it.remove();
					break;
				}
			}
		}
	}
	
	public void setCurrentFormType(FormType type) {
		currentType = type;
		requestFocusInWindow();
	}

	class MarkerTest {
		private InterpolatedLine markerLine = null;
		private int markerIndex = -1;
		
		public MarkerTest() {
		}
		
		public boolean isMarker(Point p) {
			for (Iterator<InterpolatedLine> it = forms.iterator(); it.hasNext(); ) {
				InterpolatedLine line = it.next();
				Point[] pts = line.getChangeablePoints();
				for (int i = 0; i < pts.length; i++) {
					if (pts[i].equals(p)) {
						markerLine = line;
						markerIndex = i;
						return true;
					}
				}
			}
			return false;
		}

		public int getMarkerIndex() {
			return markerIndex;
		}

		public void setMarkerIndex(int markerIndex) {
			this.markerIndex = markerIndex;
		}

		public InterpolatedLine getMarkerLine() {
			return markerLine;
		}

		public void setMarkerLine(InterpolatedLine markerLine) {
			this.markerLine = markerLine;
		}
		
	}

}
