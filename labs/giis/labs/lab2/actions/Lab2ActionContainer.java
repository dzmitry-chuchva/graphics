package giis.labs.lab2.actions;

import giis.global.gui.ModelFrame;
import giis.labs.base.impl.LabsAction;
import giis.labs.base.impl.LabsActionFactory;
import giis.labs.lab2.gui.Lab2;

import java.awt.event.KeyEvent;

import javax.swing.Action;

public class Lab2ActionContainer {
	
	private static Lab2Action type1Action = null;
	private static Lab2Action type2Action = null;
	private static LabsAction clearAction = null;
	private static LabsAction debugAction = null;
	private static LabsAction stepAction = null;
	private static LabsAction stepAllAction = null;
		
	public static void initializeFactory(Lab2 area) {
		if (type1Action == null) {
			type1Action = new Type1Action(
					area,
					"Гипербола типа 1",
					ModelFrame.loadImageIcon("general/AlignTop24", "Гипербола типа 1"),
					"Гипербола типа 1",
					KeyEvent.VK_1);
		}
		type1Action.putValue(Action.SELECTED_KEY, true);
		
		if (type2Action == null) {
			type2Action = new Type2Action(
					area,
					"Гипербола типа 2",
					ModelFrame.loadImageIcon("general/AlignBottom24", "Гипербола типа 2"),
					"Гипербола типа 2",
					KeyEvent.VK_2);
		}
		type2Action.putValue(Action.SELECTED_KEY, false);
		
		clearAction = LabsActionFactory.getClearAction(area);
		debugAction = LabsActionFactory.getDebugAction(area);
		stepAction = LabsActionFactory.getStepAction(area);
		stepAllAction = LabsActionFactory.getStepAllAction(area);
	}
	
	public static Lab2Action getType1Action() {
		return type1Action;
	}
	
	public static Lab2Action getType2Action() {
		return type2Action;
	}
	
	public static LabsAction getClearAction() {
		return clearAction;
	}
	
	public static LabsAction getDebugAction() {
		return debugAction;
	}
	
	public static LabsAction getStepAction() {
		return stepAction;
	}
	
	public static LabsAction getStepAllAction() {
		return stepAllAction;
	}
	
}
