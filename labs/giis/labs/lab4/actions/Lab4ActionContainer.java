package giis.labs.lab4.actions;

import giis.global.gui.ModelFrame;
import giis.labs.base.impl.LabsAction;
import giis.labs.base.impl.LabsActionFactory;
import giis.labs.lab4.gui.Lab4;

import java.awt.event.KeyEvent;

import javax.swing.Action;

public class Lab4ActionContainer {
	
	private static Lab4Action sarAction = null;
	private static Lab4Action colorAction = null;
	private static LabsAction clearAction = null;
	private static LabsAction debugAction = null;
	private static LabsAction stepAction = null;
	private static LabsAction stepAllAction = null;
		
	public static void initializeFactory(Lab4 area) {
		if (sarAction == null) {
			sarAction = new SarAction(
					area,
					"Алгоритм c САР",
					ModelFrame.loadImageIcon("general/AlignTop24", "Алгоритм, использующий САР"),
					"Выбрать алгоритм закраски, основанный на САР",
					KeyEvent.VK_1);
		}
		sarAction.putValue(Action.SELECTED_KEY, true);
		
		if (colorAction == null) {
			colorAction = new ColorAction(
					area,
					"Алгоритм постр. заполнения с затравкой",
					ModelFrame.loadImageIcon("general/AlignBottom24", "Алгоритм построчного заполнения с затравкой"),
					"Выбрать алгоритм построчного заполнения с затравкой",
					KeyEvent.VK_2);
		}
		colorAction.putValue(Action.SELECTED_KEY, false);
		
		clearAction = LabsActionFactory.getClearAction(area);
		debugAction = LabsActionFactory.getDebugAction(area);
		stepAction = LabsActionFactory.getStepAction(area);
		stepAllAction = LabsActionFactory.getStepAllAction(area);
	}
	
	public static LabsAction getSarAction() {
		return sarAction;
	}

	public static LabsAction getColorAction() {
		return colorAction;
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
