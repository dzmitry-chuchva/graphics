package giis.labs.lab6.gui;

import giis.labs.base.api.IWorkArea;
import giis.labs.base.impl.AbstractArea;
import giis.labs.lab6.actions.Lab6ActionContainer;
import giis.labs.lab6.model.ClipArea;
import giis.labs.lab6.model.Segment;

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

import javax.naming.OperationNotSupportedException;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class Lab6 extends AbstractArea implements IWorkArea, KeyListener {
	
	public static final Color LINE_COLOR = Color.BLACK;
	public static final int MAX_POINT_LIST_SIZE = 100;
	private static final Color TMP_DOT_COLOR = Color.YELLOW;
	private final String shortDescription = "Л.р. №6";
	private final String longDescription = "Лабораторная работа №6. Отсечение.";
	
	private ClipArea clipArea = null;
	private List<Point> currentPoints = new ArrayList<Point>();
	private Segment segment = null;
	private Point clipAreaMoveOrigin = null;
	
	private boolean drawNormals = true, drawInvisible = true;
	
	public Lab6() {
		super();
		
		addKeyListener(this);
		
		Lab6ActionContainer.initializeFactory(this);
		
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
		
		menu = new JMenu("Отсечение");
		menus.add(menu);
		
		menuItem = new JCheckBoxMenuItem(Lab6ActionContainer.getDrawInvisibleAction());
		menu.add(menuItem);
		
		menuItem = new JCheckBoxMenuItem(Lab6ActionContainer.getDrawNormalsAction());
		menu.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem(Lab6ActionContainer.getClearAction());
		menu.add(menuItem);
		
		menuBar = menus.toArray(new JMenu[0]);
		
		return menuBar;
	}
	
	private JToolBar createToolbar() {
		toolBar = new JToolBar("Toolbar",JToolBar.HORIZONTAL);
		
		toolBar.add(new JToggleButton(Lab6ActionContainer.getDrawInvisibleAction()));
		toolBar.add(new JToggleButton(Lab6ActionContainer.getDrawNormalsAction()));
		
		toolBar.addSeparator();
		
		toolBar.add(Lab6ActionContainer.getClearAction());
		return toolBar;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (clipArea != null) {
			clipArea.draw(this,drawNormals,drawInvisible);
		}
		
		for (Iterator<Point> it = currentPoints.iterator(); it.hasNext(); ) {
			plot(it.next(), TMP_DOT_COLOR);
		}
		
		endPaintComponent();
	}

	public void clearArea() {
		clipArea = null;
		currentPoints.clear();
		repaint();
	}

	public boolean isDebugMode() {
		return false;
	}

	public void setDebugMode(boolean mode) {
	}

	public void stepDebug() {
	}

	public void stepDebugEnd() {
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		requestFocusInWindow();
		if (clipArea == null) {
			if (arg0.getButton() == MouseEvent.BUTTON1) {
				if (currentPoints.size() < MAX_POINT_LIST_SIZE) {
					currentPoints.add(translate(arg0.getPoint()));
				} else {
					JOptionPane.showMessageDialog(this, "Достигнуто максимальное значение вершин для одного полигона! Нажмите Enter для создания полигона.");
				}
			} else if (arg0.getButton() == MouseEvent.BUTTON3) {
				currentPoints.clear();
			}
			repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (clipAreaMoveOrigin != null) {
			Point p = translate(arg0.getPoint());
			clipArea.shift(p.x - clipAreaMoveOrigin.x, p.y - clipAreaMoveOrigin.y);
			clipAreaMoveOrigin = p;
			repaint();
		} else if (segment != null) {
			Point tmp = translate(arg0.getPoint());
			segment.setEnd(tmp);
			clipArea.recalcVisiblenessFor(segment);
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (clipArea != null) {
			Point p = translate(arg0.getPoint());
			MarkerTest test = new MarkerTest();
			if (test.isMarker(p)) {
				clipAreaMoveOrigin = p;
			} else {
				if (segment == null) {
					segment = new Segment(p,p);
					clipArea.addSegment(segment);
					repaint();
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (clipArea != null) {
			if (clipAreaMoveOrigin != null) {
				Point p = translate(arg0.getPoint());
				clipArea.shift(p.x - clipAreaMoveOrigin.x, p.y - clipAreaMoveOrigin.y);
				clipAreaMoveOrigin = null;
				repaint();
			} else if (segment != null) {
				Point tmp = translate(arg0.getPoint());
				segment.setEnd(tmp);
				segment = null;
				repaint();
			}
		}
	}

	public void keyPressed(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent arg0) {
		if (clipArea == null && arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				clipArea = new ClipArea(currentPoints.toArray(new Point[0]));
			} catch (OperationNotSupportedException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				clipArea = null;
			} finally {
				currentPoints.clear();
				repaint();
			}
		}
	}

	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	protected void recalcSize() {
		super.recalcSize();
		
		if (clipArea != null) {
			int side  = getSideSize() / getCellSize();
			Area clip = new Area(new Rectangle(0,0, side, side));
			
			if (!clip.contains(clipArea.getAWTPolygon().getBounds2D())) {
				clipArea = null; 
			} else {
				List<Segment> segments = clipArea.getSegments();
				for (Iterator<Segment> it = segments.iterator(); it.hasNext(); ) {
					Segment segment = it.next();
					if (!clip.contains(segment.getBegin()) || (!clip.contains(segment.getEnd()))) {
						it.remove();
					}
				}
			}
		}
		
	}

	public void setDrawInvisible(boolean drawInvisible) {
		this.drawInvisible = drawInvisible;
		repaint();
	}

	public void setDrawNormals(boolean drawNormals) {
		this.drawNormals = drawNormals;
		repaint();
	}

	public boolean isDrawInvisible() {
		return drawInvisible;
	}

	public boolean isDrawNormals() {
		return drawNormals;
	}
	
	class MarkerTest {
		private int markerIndex = -1;
		
		public MarkerTest() {
		}
		
		public boolean isMarker(Point p) {
			if (clipArea == null) {
				return false;
			}
			
			Point[] points = clipArea.getPoints();
			for (int i = 0; i < points.length; i++) {
				if (points[i].equals(p)) {
					markerIndex = i;
					return true;
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
		
	}

}
