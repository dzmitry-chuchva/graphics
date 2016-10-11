package giis.labs.lab3.actions;

import giis.labs.lab3.gui.Lab3;
import giis.labs.lab3.model.FormType;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

public class BezierAction extends Lab3Action {
	
	public BezierAction(Lab3 frame, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(frame, text, icon, desc, mnemonic);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.setCurrentFormType(FormType.BEZIER);
	}

}
