package giis.labs.lab1.gui;

import giis.labs.base.api.IWorkArea;
import giis.labs.base.impl.AbstractArea;
import giis.labs.lab1.actions.Lab1ActionContainer;
import giis.labs.lab1.model.LineAlgorithm;
import giis.labs.lab1.model.ModelLine;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
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
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class Lab1 extends AbstractArea implements IWorkArea {

	private final String shortDescription = "Л.р. №1";
	private final String longDescription = "Лабораторная работа №1. Рисование отрезков.";
	
	private Vector<ModelLine> lines = new Vector<ModelLine>();
	
	private ModelLine debugLine = null;
	private int debugStep;
	
	private boolean mouseMoving = false;
	private Point start;
	private ModelLine tmpLine;
	private boolean debugMode = false;
	
	private LineAlgorithm currentAlgo;

	public Lab1() {
		super();
		
		Lab1ActionContainer.initializeFactory(this);
		currentAlgo = Lab1ActionContainer.getDDAAction().getAlgo();
		
		setLongDescription(longDescription);
		setShortDescription(shortDescription);
		
		addComponentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		createToolbar();
		createMenu();
	}

	public Lab1(int cellSize) {
		this();
		
		setCellSize(cellSize);
	}
	
	private JMenu[] createMenu() {
		JMenu menu, submenu;
		JMenuItem menuItem;
		
		List<JMenu> menus = new ArrayList<JMenu>();
		
		menu = new JMenu("Алгоритм");
		menu.setMnemonic(KeyEvent.VK_F);
		menus.add(menu);
		
		ButtonGroup bg = new ButtonGroup();
		
		menuItem = new JRadioButtonMenuItem(Lab1ActionContainer.getDDAAction());
		menu.add(menuItem);
		bg.add(menuItem);
		
		menuItem = new JRadioButtonMenuItem(Lab1ActionContainer.getBresenhamAction());
		menu.add(menuItem);
		bg.add(menuItem);
		
		menuItem = new JRadioButtonMenuItem(Lab1ActionContainer.getWuAction());
		menu.add(menuItem);
		bg.add(menuItem);
		
		menu = new JMenu("Дополнително");
		menu.setMnemonic(KeyEvent.VK_L);
		menus.add(menu);
		
		menuItem = new JCheckBoxMenuItem(Lab1ActionContainer.getDebugAction());
		menu.add(menuItem);
		
		menuItem = new JMenuItem(Lab1ActionContainer.getStepAction());
		menu.add(menuItem);
		menuItem = new JMenuItem(Lab1ActionContainer.getStepAllAction());
		menu.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem(Lab1ActionContainer.getClearAction());
		menu.add(menuItem);

		menuBar = menus.toArray(new JMenu[0]);
		return menuBar;
	}
	
	private JToolBar createToolbar() {
		toolBar = new JToolBar("Toolbar",JToolBar.HORIZONTAL);
		
//		toolBar.add(createActionsCombo(
//						new LabsAction[] {
//								Lab1ActionContainer.getDDAAction(),
//								Lab1ActionContainer.getBresenhamAction(),
//								Lab1ActionContainer.getWuAction()
//						}
//		));
		
		ButtonGroup bg = new ButtonGroup();
		JToggleButton button = new JToggleButton(Lab1ActionContainer.getDDAAction());
		bg.add(button);
		toolBar.add(button);
		
		button = new JToggleButton(Lab1ActionContainer.getBresenhamAction());
		bg.add(button);
		toolBar.add(button);
		
		button = new JToggleButton(Lab1ActionContainer.getWuAction());
		bg.add(button);
		toolBar.add(button);
		
		toolBar.addSeparator();
		
		button = new JToggleButton(Lab1ActionContainer.getDebugAction());
		toolBar.add(button);
		toolBar.add(Lab1ActionContainer.getStepAction());
		toolBar.add(Lab1ActionContainer.getStepAllAction());
		
		toolBar.addSeparator();
		
		toolBar.add(Lab1ActionContainer.getClearAction());
		return toolBar;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (Iterator<ModelLine> it = lines.iterator(); it.hasNext();) {
			ModelLine line = it.next();
			LineAlgorithm algo = line.getAlgo();
			algo.draw(this, line);
		}
		
		// if debugLine present, do debug step
		if (debugLine != null) {
			LineAlgorithm algo = debugLine.getAlgo();
			algo.drawDebug(this,debugLine,debugStep);
		}
		
		endPaintComponent();
	}

	public void addLine(ModelLine line) {
		lines.add(line);
		repaint();
	}
	
	public boolean removeLine(ModelLine line) {
		boolean res = lines.remove(line);
		if (res) {
			repaint();
		}
		return res;
	}
	
	public void clearArea() {
		lines.clear();
		repaint();
	}

	protected void recalcSize() {
		super.recalcSize();		
		// remove clipped lines
		int side  = getSideSize() / getCellSize();
		Area clipArea = new Area(new Rectangle(0,0, side, side));
		
		for (Iterator<ModelLine> it = lines.iterator(); it.hasNext(); ) {
			ModelLine line = it.next();
			if (!clipArea.contains(line.getBegin()) || !clipArea.contains(line.getEnd())) {
				it.remove();
			}
		}
		
		if (isDebugLine()) {
			if (!clipArea.contains(debugLine.getBegin()) || !clipArea.contains(debugLine.getEnd())) {
				cancelDebugLine();
			}
		}
	}
	
	public void enableDebugLine(ModelLine line) {
		debugLine = line;
		debugStep = 0;
		Lab1ActionContainer.getStepAction().setEnabled(true);
		Lab1ActionContainer.getStepAllAction().setEnabled(true);
		Lab1ActionContainer.getDDAAction().setEnabled(false);
		Lab1ActionContainer.getWuAction().setEnabled(false);
		Lab1ActionContainer.getBresenhamAction().setEnabled(false);
		Lab1ActionContainer.getClearAction().setEnabled(false);
		Lab1ActionContainer.getDebugAction().setEnabled(false);
	}
	
	public void stepDebug() {
		if (isDebugLine()) {
			debugStep++;
			repaint();
		}
	}
	
	public boolean isDebugLine() {
		return (debugLine != null);
	}
	
	public void stepDebugEnd() {
		if (!isDebugLine()) {
			return;
		}
		
		addLine(debugLine);
		
		debugLine = null;
		setCurrentMessage("");
		Lab1ActionContainer.getStepAction().setEnabled(false);
		Lab1ActionContainer.getStepAllAction().setEnabled(false);
		Lab1ActionContainer.getDDAAction().setEnabled(true);
		Lab1ActionContainer.getWuAction().setEnabled(true);
		Lab1ActionContainer.getBresenhamAction().setEnabled(true);
		Lab1ActionContainer.getClearAction().setEnabled(true);
		Lab1ActionContainer.getDebugAction().setEnabled(true); 
	}
	
	public void cancelDebugLine() {
		debugLine = null;
		setCurrentMessage("");
		Lab1ActionContainer.getStepAction().setEnabled(false);
		Lab1ActionContainer.getDDAAction().setEnabled(true);
		Lab1ActionContainer.getWuAction().setEnabled(true);
		Lab1ActionContainer.getBresenhamAction().setEnabled(true);
		Lab1ActionContainer.getClearAction().setEnabled(true);
		Lab1ActionContainer.getDebugAction().setEnabled(true);
	}
	
	public void reportMsg(String message) {
		setCurrentMessage(message);
	}

	public void mousePressed(MouseEvent arg0) {
		mouseMoving = true;
		start = arg0.getPoint();
		Point pstart = translate(start);
		tmpLine = new ModelLine(pstart,pstart,currentAlgo);
		addLine(tmpLine);
		repaint();
	}

	public void mouseReleased(MouseEvent arg0) {
		if (mouseMoving) {
			Point pend = translate(arg0.getPoint());
			tmpLine.setEnd(pend);
			repaint();
			mouseMoving = false;
			
			// enable debugging of this line if needed
			if (debugMode) {
				removeLine(tmpLine);
				enableDebugLine(tmpLine);
			}
		}
	}

	public void mouseDragged(MouseEvent arg0) {
		if (mouseMoving) {
			tmpLine.setEnd(translate(arg0.getPoint()));
			repaint();
		}
	}

	public void setCurrentAlgo(LineAlgorithm algo) {
		this.currentAlgo = algo;
	}
	
	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	
}
