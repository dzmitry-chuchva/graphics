package giis.labs.lab2.actions;

import giis.labs.lab2.gui.Lab2;
import giis.labs.lab2.model.Giperbola;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

public class Type1Action extends Lab2Action {
	
	public Type1Action(Lab2 frame, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(frame, text, icon, desc, mnemonic);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.setGiperbolaType(Giperbola.GIPERBOLA_TYPE1);
	}

}
