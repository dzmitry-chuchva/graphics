package giis.global.gui;

import giis.labs.base.api.IWorkArea;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ModelFrame extends JFrame implements ChangeListener, ActionListener {
	
	private List areas;
	private int current;
	private JToolBar currentToolbar = null;
	private JMenuBar menuBar = null;
	private JTabbedPane tabPane= null;
	private JMenu labsMenu = null;
	
	public ModelFrame(List areas) {
		super();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800,700));
		
		this.areas = new ArrayList(areas);
		this.current = 0;
		
		ButtonGroup bg = new ButtonGroup();
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		labsMenu = new JMenu("Лабораторные работы");
		menuBar.add(labsMenu);
		tabPane = new JTabbedPane();
		tabPane.addChangeListener(this);
		JRadioButtonMenuItem menuItem;
		for (int i = 0; i < areas.size(); i++) {
			IWorkArea area = (IWorkArea)areas.get(i);
			tabPane.addTab(area.getShortDescription(), area.getComponent());
			menuItem = new JRadioButtonMenuItem(area.getLongDescription());
			menuItem.setActionCommand(String.valueOf(i));
			menuItem.addActionListener(this);
			if (i == 0) {
				menuItem.setSelected(true);
			}
			bg.add(menuItem);
			labsMenu.add(menuItem);
		}
		labsMenu.addSeparator();
		JMenuItem item = new JMenuItem("О программе",loadImageIcon("general/About24", "About"));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Сборник лабораторных работ по курсу ГИИСиКГ\nЧучвы Дмитрия, гр. 421703\n(c) 2007");
			}
			
		});
		labsMenu.add(item);
		
		getContentPane().add(tabPane,BorderLayout.CENTER);
	}
	
	public static ImageIcon loadImageIcon(String imageName, String altText) {
	    String imgLocation = "toolbarButtonGraphics/"
	                         + imageName
	                         + ".gif";
	    URL imageURL = Main.class.getResource(imgLocation);

	    if (imageURL != null) {                      //image found
	        return new ImageIcon(imageURL, altText);
	    } else {                                     //no image found
	        System.err.println("Resource not found: " + imgLocation);
	    }

	    return null;
	}
	
	public void setAreas(List areas) {
		this.areas = areas;
	}
	
	public IWorkArea getCurrentArea() {
		if (areas != null && !areas.isEmpty()) {
			return (IWorkArea)areas.get(current);
		} else {
			return null;
		}
	}

	public void stateChanged(ChangeEvent arg0) {
		JTabbedPane tabPane = (JTabbedPane)arg0.getSource();
		updateFrameFromTab(tabPane.getSelectedIndex());
	}

	@Override
	public void setTitle(String arg0) {
		super.setTitle("КГ ст. гр. 421703 Чучвы Дмитрия: " + arg0);
	}
	
	public void updateFrameFromTab(int tabNo) {
		IWorkArea selectedArea = (IWorkArea)areas.get(tabNo);
		setTitle(selectedArea.getLongDescription());
		
		if (labsMenu != null) {
			JMenuItem item = labsMenu.getItem(tabNo);
			if (item != null) {
				item.setSelected(true);
			}
		}
		
		// remove previous menus if exist, they start always from 1
		Component[] comps = menuBar.getComponents();
		for (int i = 1; i < comps.length; i++) {
			menuBar.remove(comps[i]);
		}
		
		JMenu[] areaBar = selectedArea.getMenuBar();
		for (int i = 0; i < areaBar.length; i++) {
			menuBar.add(areaBar[i]);
		}
		
		if (currentToolbar != null) {
			remove(currentToolbar);
		}
		
		currentToolbar = selectedArea.getToolBar();
		add(currentToolbar,BorderLayout.PAGE_START);
		
		repaint();
	}
	
//	public void selectWorkArea(int areaNo) {
//		tabPane.setSelectedIndex(areaNo);
//		updateFrameFromTab(0);
//	}

	public void actionPerformed(ActionEvent arg0) {
		int areaNo = Integer.parseInt(arg0.getActionCommand());
		tabPane.setSelectedIndex(areaNo);
	}

}
