package giis.labs.lab6.actions;

import giis.global.gui.ModelFrame;
import giis.labs.base.impl.LabsAction;
import giis.labs.base.impl.LabsActionFactory;
import giis.labs.lab6.gui.Lab6;

import java.awt.event.KeyEvent;

import javax.swing.Action;

public class Lab6ActionContainer {
	
	private static Lab6Action drawInvisibleAction = null;
	private static Lab6Action drawNormalsAction = null;
	private static LabsAction clearAction = null;
	private static LabsAction debugAction = null;
	private static LabsAction stepAction = null;
	private static LabsAction stepAllAction = null;
		
	public static void initializeFactory(Lab6 area) {
		if (drawInvisibleAction == null) {
			drawInvisibleAction = new DrawInvisibleAction(
					area,
					"Рисовать невидимые части",
					ModelFrame.loadImageIcon("general/AlignTop24", "Рисование невидимых частей"),
					"Включить отображение невидимых частей отрезков",
					KeyEvent.VK_1);
		}
		drawInvisibleAction.putValue(Action.SELECTED_KEY, true);
		
		if (drawNormalsAction == null) {
			drawNormalsAction = new DrawNormalsAction(
					area,
					"Рисовать нормали",
					ModelFrame.loadImageIcon("general/AlignTop24", "Рисование нормалей"),
					"Включить отображение внутренних нормалей многоугольника",
					KeyEvent.VK_2);
		}
		drawNormalsAction.putValue(Action.SELECTED_KEY, true);
		
		clearAction = LabsActionFactory.getClearAction(area);
		debugAction = LabsActionFactory.getDebugAction(area);
		stepAction = LabsActionFactory.getStepAction(area);
		stepAllAction = LabsActionFactory.getStepAllAction(area);
	}
	
	public static Lab6Action getDrawInvisibleAction() {
		return drawInvisibleAction;
	}

	public static Lab6Action getDrawNormalsAction() {
		return drawNormalsAction;
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
