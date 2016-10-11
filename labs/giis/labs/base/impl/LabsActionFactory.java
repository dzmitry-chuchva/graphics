package giis.labs.base.impl;

import giis.global.gui.ModelFrame;
import giis.labs.base.api.IWorkArea;

import java.awt.event.KeyEvent;

import javax.swing.Action;

public class LabsActionFactory {

	public static LabsAction getClearAction(IWorkArea area) {
		LabsAction clearAction = new ClearAction(
					area,
					"�������� ������� �������",
					ModelFrame.loadImageIcon("general/Delete24", "��������"),
					"�������� ������� �������",
					KeyEvent.VK_J);
		return clearAction;
	}
	
	public static LabsAction getDebugAction(IWorkArea area) {
		LabsAction debugAction = new DebugAction(
					area,
					"����� �������",
					ModelFrame.loadImageIcon("general/Undo24", "�������"),
					"�������� ����� �������",
					KeyEvent.VK_D);
		debugAction.putValue(Action.SELECTED_KEY, false);
		return debugAction;
	}
	
	public static LabsAction getStepAction(IWorkArea area) {
		LabsAction stepAction = new StepAction(
					area,
					"���",
					ModelFrame.loadImageIcon("media/Play24", "���"),
					"���������� ��� ���������",
					KeyEvent.VK_I);
		stepAction.setEnabled(false);
		return stepAction;
	}
	
	public static LabsAction getStepAllAction(IWorkArea area) {
		LabsAction stepAllAction = new StepAllAction(
					area,
					"����������",
					ModelFrame.loadImageIcon("media/FastForward24", "����������"),
					"���������� ���� �� �����",
					KeyEvent.VK_R);
		stepAllAction.setEnabled(false);
		return stepAllAction;
	}
}
