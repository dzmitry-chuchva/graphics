package giis.labs.lab4.gui;

import giis.labs.base.api.IWorkArea;
import giis.labs.base.impl.AbstractArea;
import giis.labs.lab4.actions.Lab4ActionContainer;
import giis.labs.lab4.model.FillAlgoType;
import giis.labs.lab4.model.Polygon;

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

public class Lab4 extends AbstractArea implements IWorkArea, KeyListener {
	
	private final String shortDescription = "Л.р. №4";
	private final String longDescription = "Лабораторная работа №4. Закраска областей.";
	
	private Vector<Point> currentPoints = new Vector<Point>();
	private Vector<Polygon> forms = new Vector<Polygon>();
	
	private FillAlgoType currentType = FillAlgoType.FILL_SAR;
	
	private static final Color tmpDotsColor = Color.YELLOW;
	
	private boolean debugMode = false;
	private Polygon debugForm = null;
	private int debugStep = 0;
	
	private final static int MAX_POINTS_LIST_SIZE = 100;
	
	public Lab4() {
		super();
		
		addKeyListener(this);
		
		Lab4ActionContainer.initializeFactory(this);
		
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
		
		menu = new JMenu("Закраска областей");
		menus.add(menu);
		
		ButtonGroup bg = new ButtonGroup();
		
		menuItem = new JRadioButtonMenuItem(Lab4ActionContainer.getSarAction());
		menu.add(menuItem);
		bg.add(menuItem);
				
		menuItem = new JRadioButtonMenuItem(Lab4ActionContainer.getColorAction());
		menu.add(menuItem);
		bg.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem(Lab4ActionContainer.getClearAction());
		menu.add(menuItem);
		
		menu = new JMenu("Отладка");
		menus.add(menu);
		menuItem = new JCheckBoxMenuItem(Lab4ActionContainer.getDebugAction());
		menu.add(menuItem);
		menu.addSeparator();		
		menu.add(Lab4ActionContainer.getStepAction());
		menu.add(Lab4ActionContainer.getStepAllAction());
		
		menuBar = menus.toArray(new JMenu[0]);
		
		return menuBar;
	}
	
	private JToolBar createToolbar() {
		toolBar = new JToolBar("Toolbar",JToolBar.HORIZONTAL);
		
		ButtonGroup bg = new ButtonGroup();
		JToggleButton button = new JToggleButton(Lab4ActionContainer.getSarAction());
		toolBar.add(button);
		bg.add(button);
		
		button = new JToggleButton(Lab4ActionContainer.getColorAction());
		toolBar.add(button);
		bg.add(button);
		
		toolBar.addSeparator();
		
		toolBar.add(Lab4ActionContainer.getClearAction());
		
		toolBar.addSeparator();
		
		toolBar.add(new JToggleButton(Lab4ActionContainer.getDebugAction()));
		toolBar.add(Lab4ActionContainer.getStepAction());
		toolBar.add(Lab4ActionContainer.getStepAllAction());
		
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
			
			Lab4ActionContainer.getClearAction().setEnabled(true);
			Lab4ActionContainer.getSarAction().setEnabled(true);
			Lab4ActionContainer.getColorAction().setEnabled(true);
			Lab4ActionContainer.getDebugAction().setEnabled(true);
			Lab4ActionContainer.getStepAction().setEnabled(false);
			Lab4ActionContainer.getStepAllAction().setEnabled(false);
			
			setCurrentMessage("");
			
			repaint();
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (Iterator<Polygon> it = forms.iterator(); it.hasNext(); ) {
			Polygon poly = it.next();
					
			poly.draw(this);
		}
		
		for (Iterator<Point> it = currentPoints.iterator(); it.hasNext(); ) {
			plot(it.next(), tmpDotsColor);
		}
		
		if (debugMode && debugForm != null) {
			debugForm.draw(this, debugMode, debugStep);
			if (debugForm != null) {
				// can be null because of draw calls stepEndDebug(), which cleares instance
				setCurrentMessage("Step debug please!");
			}
		}
		
		endPaintComponent();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		requestFocusInWindow();
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			if (currentPoints.size() < MAX_POINTS_LIST_SIZE) {
				currentPoints.add(translate(arg0.getPoint()));
			} else {
				JOptionPane.showMessageDialog(this, "Достигнуто максимальное значение вершин для одного полигона! Нажмите Enter для создания полигона.");
			}
		} else if (arg0.getButton() == MouseEvent.BUTTON3) {
			currentPoints.clear();
		}
		repaint();
	}
	
	private void addForm(Polygon poly) {
		if (debugMode) {
			debugForm = poly;
			debugStep = 0;
			Lab4ActionContainer.getClearAction().setEnabled(false);
			Lab4ActionContainer.getSarAction().setEnabled(false);
			Lab4ActionContainer.getColorAction().setEnabled(false);
			Lab4ActionContainer.getDebugAction().setEnabled(false);
			Lab4ActionContainer.getStepAction().setEnabled(true);
			Lab4ActionContainer.getStepAllAction().setEnabled(true);
		} else {
			forms.add(poly);
		}
		repaint();
	}
	
	public void keyPressed(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			Polygon poly = new Polygon(currentPoints.toArray(new Point[0]), currentType);
			if (poly.isSingular()) {
				addForm(poly);
				currentPoints.clear();
			} else {
				JOptionPane.showMessageDialog(this, "Полигон содержит пересечения ребер либо не является полигоном!");
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
		
		for (Iterator<Polygon> it = forms.iterator(); it.hasNext(); ) {
			Polygon poly = it.next();
			if (!clipArea.contains(poly.getAWTPolygon().getBounds2D())) {
				it.remove();
			}
		}
	}
	
	public void setCurrentFillType(FillAlgoType type) {
		currentType = type;
		requestFocusInWindow();
	}

}
