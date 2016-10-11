package giis.labs.lab6.actions;

import giis.labs.lab6.gui.Lab6;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

public class DrawNormalsAction extends Lab6Action {

	public DrawNormalsAction(Lab6 frame, String text, ImageIcon icon,
			String desc, Integer mnemonic) {
		super(frame, text, icon, desc, mnemonic);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.setDrawNormals(!frame.isDrawNormals());
	}

}
