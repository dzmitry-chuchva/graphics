package giis.labs.lab3.actions;

import giis.global.gui.ModelFrame;
import giis.labs.base.impl.LabsAction;
import giis.labs.base.impl.LabsActionFactory;
import giis.labs.lab3.gui.Lab3;

import java.awt.event.KeyEvent;

import javax.swing.Action;

public class Lab3ActionContainer {
	
	private static Lab3Action ermitAction = null;
	private static Lab3Action bezierAction = null;
	private static Lab3Action bsplineAction = null;
	private static LabsAction clearAction = null;
	private static LabsAction debugAction = null;
	private static LabsAction stepAction = null;
	private static LabsAction stepAllAction = null;
		
	public static void initializeFactory(Lab3 area) {
		if (ermitAction == null) {
			ermitAction = new ErmitAction(
					area,
					"Метод Эрмита",
					ModelFrame.loadImageIcon("general/AlignTop24", "Метод Эрмита"),
					"Выбрать метод Эрмита",
					KeyEvent.VK_1);
		}
		ermitAction.putValue(Action.SELECTED_KEY, true);
		
		if (bezierAction == null) {
			bezierAction = new BezierAction(
					area,
					"Метод Безье",
					ModelFrame.loadImageIcon("general/AlignCenter24", "Метод Безье"),
					"Выбрать метод Безье",
					KeyEvent.VK_2);
		}
		bezierAction.putValue(Action.SELECTED_KEY, false);
		
		if (bsplineAction == null) {
			bsplineAction = new BSplineAction(
					area,
					"Сглаживание B-сплайном",
					ModelFrame.loadImageIcon("general/AlignBottom24", "Сглаживание В-сплайном"),
					"Выбрать сглаживание В-сплайном",
					KeyEvent.VK_3);
		}
		bsplineAction.putValue(Action.SELECTED_KEY, false);
		
		clearAction = LabsActionFactory.getClearAction(area);
		debugAction = LabsActionFactory.getDebugAction(area);
		stepAction = LabsActionFactory.getStepAction(area);
		stepAllAction = LabsActionFactory.getStepAllAction(area);
	}
	
	public static LabsAction getBezierAction() {
		return bezierAction;
	}

	public static LabsAction getBSplineAction() {
		return bsplineAction;
	}

	public static LabsAction getErmitAction() {
		return ermitAction;
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
