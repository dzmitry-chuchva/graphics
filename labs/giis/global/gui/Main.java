package giis.global.gui;

import giis.labs.base.api.IWorkArea;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Main {
	
	public static final int DEFAULT_CELL_SIZE = 10;
	public static final int MAX_CELL_SIZE = 100;
	public static final int MIN_CELL_SIZE = 2;
	
	public static final String propertiesFile = "labs.classes";
	
	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args.clone());
            }
        });
	}
	
	public static void createAndShowGUI(String[] args) {
		int customCellSize = DEFAULT_CELL_SIZE;
		if (args.length > 0) {
			try {
				customCellSize = Integer.parseInt(args[0]);
				if (customCellSize > MAX_CELL_SIZE || customCellSize < MIN_CELL_SIZE) {
					throw new Exception();
				}
			}
			catch (Exception e) {
				customCellSize = DEFAULT_CELL_SIZE;
			}
		}
		
		List areas = loadClasses(propertiesFile,customCellSize);
		if (areas != null && !areas.isEmpty()) {
			ModelFrame frame = new ModelFrame(areas);
			frame.pack();
			frame.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Error while loading labs from file " + propertiesFile);
			System.exit(1);
		}
	}
	
	public static List loadClasses(String name, int cellSize) {
		BufferedReader reader = null;
		List elems = new ArrayList();	
		ClassLoader loader = ClassLoader.getSystemClassLoader();
				
		// read class names and instantiate them
		try {
			reader = new BufferedReader(new FileReader(name));
			String line;
			while ((line = reader.readLine()) != null) {
				IWorkArea el = null;
				try {
					Object o = loader.loadClass(line).newInstance();
					el = (IWorkArea)o;
				} catch (ClassNotFoundException e) {
					System.out.println("warning: implementation of " + line + " not found, ignored...");
				} catch (InstantiationException e) {
					System.out.println("warning: error occured while loading of " + line);
				} catch (IllegalAccessException e) {
					System.out.println("warning: error occured while loading of " + line);
				} catch (ClassCastException e) {
					System.out.println("warning: " + line + " doesnot implement IWorkArea interface, ignored...");
				} catch (Exception e) {
					System.out.println("general error: " + e.getMessage() + ". Continuing...");
				} finally {
					if (el != null) {
						el.setCellSize(cellSize);
						elems.add(el);
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			return null;
		}
		catch (IOException e) {
			return null;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return elems;
	}
}
