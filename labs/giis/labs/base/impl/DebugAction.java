package giis.labs.base.impl;

import giis.labs.base.api.IWorkArea;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;

public class DebugAction extends LabsAction {

	public DebugAction(IWorkArea frame, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(frame, text, icon, desc, mnemonic);
	}

	public void actionPerformed(ActionEvent e) {
		Boolean selected = (Boolean)getValue(Action.SELECTED_KEY);
		frame.setDebugMode(selected);
	}

}
