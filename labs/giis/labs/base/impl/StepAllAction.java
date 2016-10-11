package giis.labs.base.impl;

import giis.labs.base.api.IWorkArea;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

public class StepAllAction extends LabsAction {

	public StepAllAction(IWorkArea frame, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(frame, text, icon, desc, mnemonic);
	}

	public void actionPerformed(ActionEvent e) {
			frame.stepDebugEnd();
	}
}
