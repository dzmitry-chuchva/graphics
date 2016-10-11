package giis.labs.lab2.gui;

import giis.labs.base.api.IWorkArea;
import giis.labs.base.impl.AbstractArea;
import giis.labs.lab2.actions.Lab2ActionContainer;
import giis.labs.lab2.model.Giperbola;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class Lab2 extends AbstractArea implements IWorkArea {
	
	private final String shortDescription = "Л.р. №2";
	private final String longDescription = "Лабораторная работа №2. Кривые второго порядка.";

	private List<Giperbola> giperbolas = new ArrayList<Giperbola>();
	private Giperbola currentGiperbola = new Giperbola();
	private Giperbola debugGiperbola = null;
	private boolean debug = false;
	private int debugStep = 0;
	
	private JTextField editAField;
	private JTextField editBField;
	
	private static Color disabledEditFieldColor = Color.LIGHT_GRAY;
	
	public Lab2() {
		super();
		
		Lab2ActionContainer.initializeFactory(this);
		
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
		
		menu = new JMenu("Линии второго порядка");
		menus.add(menu);
		
		ButtonGroup bg = new ButtonGroup();
		
		menuItem = new JRadioButtonMenuItem(Lab2ActionContainer.getType1Action());
		menu.add(menuItem);
		bg.add(menuItem);
				
		menuItem = new JRadioButtonMenuItem(Lab2ActionContainer.getType2Action());
		menu.add(menuItem);
		bg.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem(Lab2ActionContainer.getClearAction());
		menu.add(menuItem);
		
		menu = new JMenu("Отладка");
		menus.add(menu);
		menuItem = new JCheckBoxMenuItem(Lab2ActionContainer.getDebugAction());
		menu.add(menuItem);
		menu.addSeparator();		
		menu.add(Lab2ActionContainer.getStepAction());
		menu.add(Lab2ActionContainer.getStepAllAction());
		
		menuBar = menus.toArray(new JMenu[0]);
		
		return menuBar;
	}
	
	private JToolBar createToolbar() {
		toolBar = new JToolBar("Toolbar",JToolBar.HORIZONTAL);
		
		ButtonGroup bg = new ButtonGroup();
		JToggleButton button = new JToggleButton(Lab2ActionContainer.getType1Action());
		toolBar.add(button);
		bg.add(button);
		
		button = new JToggleButton(Lab2ActionContainer.getType2Action());
		toolBar.add(button);
		bg.add(button);
		
		toolBar.addSeparator();
		
		editAField = new JTextField(String.valueOf(currentGiperbola.getA()),3);
		editAField.setDisabledTextColor(Color.BLACK);
		editBField = new JTextField(String.valueOf(currentGiperbola.getB()),3);
		editBField.setDisabledTextColor(Color.BLACK);
		
		toolBar.add(new JLabel("A:"));
		toolBar.addSeparator();
		toolBar.add(editAField);
		
		toolBar.addSeparator();
		
		toolBar.add(new JLabel("B:"));
		toolBar.addSeparator();
		toolBar.add(editBField);
		
		toolBar.addSeparator();
		
		toolBar.addSeparator();
		
		toolBar.add(Lab2ActionContainer.getClearAction());
		
		toolBar.addSeparator();
		
		toolBar.add(new JToggleButton(Lab2ActionContainer.getDebugAction()));
		toolBar.add(Lab2ActionContainer.getStepAction());
		toolBar.add(Lab2ActionContainer.getStepAllAction());
		
		return toolBar;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (Iterator<Giperbola> it = giperbolas.iterator(); it.hasNext(); ) {
			it.next().draw(this,false,0);
		}
		
		if (debug && debugGiperbola != null) {
			debugGiperbola.draw(this, debug, debugStep);
			if (debugGiperbola != null) {
				// can be null because of draw calls stepEndDebug(), which cleares instance
				setCurrentMessage("(" + debugGiperbola.getLastX() + ", " + debugGiperbola.getLastY() +
						"): delta = " + debugGiperbola.getLastDelta() + ", sigma = " + debugGiperbola.getLastSigma() +
						", pixel : " + debugGiperbola.getLastPixel());
			}
		}
		
		endPaintComponent();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (debugGiperbola != null) {
			// if debugging giperbola, dont process mouse clicks
			return;
		}
		switch (currentGiperbola.getType()) {
		case Giperbola.GIPERBOLA_TYPE1:
			try {
				currentGiperbola.setA(Integer.parseInt(editAField.getText()));
				currentGiperbola.setB(Integer.parseInt(editBField.getText()));
			} finally {
				editAField.setText(String.valueOf(currentGiperbola.getA()));
				editBField.setText(String.valueOf(currentGiperbola.getB()));
			}
			break;
		case Giperbola.GIPERBOLA_TYPE2:
			try {
				currentGiperbola.setA(Integer.parseInt(editAField.getText()));
				currentGiperbola.setB(Integer.parseInt(editBField.getText()));
			} finally {
				editAField.setText(String.valueOf(currentGiperbola.getA()));
				editBField.setText(String.valueOf(currentGiperbola.getB()));
			}
			break;
		}
		currentGiperbola.setCenter(translate(arg0.getPoint()));
		if (debug) {
			debugGiperbola = new Giperbola(currentGiperbola);
			Lab2ActionContainer.getClearAction().setEnabled(false);
			Lab2ActionContainer.getType1Action().setEnabled(false);
			Lab2ActionContainer.getType2Action().setEnabled(false);
			Lab2ActionContainer.getDebugAction().setEnabled(false);
			Lab2ActionContainer.getStepAction().setEnabled(true);
			Lab2ActionContainer.getStepAllAction().setEnabled(true);
		} else {
			debugStep = 0;
			giperbolas.add(new Giperbola(currentGiperbola));
		}
		repaint();
	}
	
	public void clearArea() {
		giperbolas.clear();
		repaint();
	}
	
	public void setGiperbolaType(int type) {
		currentGiperbola.setType(type);
//		switch (type) {
//		case Giperbola.GIPERBOLA_TYPE1:
//			toggleEditField(editAField);
//			toggleEditField(editBField);
//			toggleEditField(editPField);
//			break;
//		case Giperbola.GIPERBOLA_TYPE2:
//			toggleEditField(editAField);
//			toggleEditField(editBField);
//			toggleEditField(editPField);
//			break;
//		}
	}
	
	private void toggleEditField(JTextField field) {
		field.setEnabled(!field.isEnabled());
		if (!field.isEnabled()) {
			field.setBackground(disabledEditFieldColor);
		} else {
			field.setBackground(Color.WHITE);
		}
		
	}

	public boolean isDebugMode() {
		return debug;
	}

	public void setDebugMode(boolean mode) {
		debug = mode;
	}

	public void stepDebug() {
		if (debugGiperbola != null) {
			debugStep++;
			repaint();
		}
	}

	public void stepDebugEnd() {
		if (debugGiperbola != null) {
			debugStep = 0;
			giperbolas.add(debugGiperbola);
			debugGiperbola = null;
			
			Lab2ActionContainer.getClearAction().setEnabled(true);
			Lab2ActionContainer.getType1Action().setEnabled(true);
			Lab2ActionContainer.getType2Action().setEnabled(true);
			Lab2ActionContainer.getDebugAction().setEnabled(true);
			Lab2ActionContainer.getStepAction().setEnabled(false);
			Lab2ActionContainer.getStepAllAction().setEnabled(false);
			
			setCurrentMessage("");
			
			repaint();
		}
	}

}
