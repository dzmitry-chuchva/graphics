package giis.labs.lab1.actions;

import giis.global.gui.ModelFrame;
import giis.labs.base.impl.LabsAction;
import giis.labs.base.impl.LabsActionFactory;
import giis.labs.lab1.gui.Lab1;

import java.awt.event.KeyEvent;

import javax.swing.Action;

public class Lab1ActionContainer {
	
	private static Lab1Action ddaAction = null;
	private static Lab1Action wuAction = null;
	private static Lab1Action bresenhamAction = null;
	private static LabsAction clearAction = null;
	private static LabsAction debugAction = null;
	private static LabsAction stepAction = null;
	private static LabsAction stepAllAction = null;
	
	public static void initializeFactory(Lab1 area) {
		if (ddaAction == null) {
			ddaAction = new DDAAlgoAction(
					area,
					"���",
					ModelFrame.loadImageIcon("general/AlignTop24", "���"),
					"������� �������� ���",
					KeyEvent.VK_1);
		}
		ddaAction.putValue(Action.SELECTED_KEY, true);
		
		if (wuAction == null) {
			wuAction = new WuAlgoAction(
					area,
					"�������� ��",
					ModelFrame.loadImageIcon("general/AlignCenter24", "��"),
					"������� �������� ��",
					KeyEvent.VK_3);
		}
		wuAction.putValue(Action.SELECTED_KEY, false);
		
		if (bresenhamAction == null) {
			bresenhamAction = new BresenhamAlgoAction(
					area,
					"�������� ����������",
					ModelFrame.loadImageIcon("general/AlignBottom24", "���������"),
					"������� �������� ����������",
					KeyEvent.VK_2);
		}
		bresenhamAction.putValue(Action.SELECTED_KEY, false);
		
		clearAction = LabsActionFactory.getClearAction(area);
		debugAction = LabsActionFactory.getDebugAction(area);
		stepAction = LabsActionFactory.getStepAction(area);
		stepAllAction = LabsActionFactory.getStepAllAction(area);
		
	}
	
	public static Lab1Action getDDAAction() {
		return ddaAction;
	}
	
	public static Lab1Action getWuAction() {
		return wuAction;
	}
	
	public static Lab1Action getBresenhamAction() {
		return bresenhamAction;
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
