package giis.labs.lab4.actions;

import giis.labs.lab4.gui.Lab4;
import giis.labs.lab4.model.FillAlgoType;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

public class SarAction extends Lab4Action {

	public SarAction(Lab4 frame, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(frame, text, icon, desc, mnemonic);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.setCurrentFillType(FillAlgoType.FILL_SAR);
	}

}
