package giis.labs.lab1.actions;

import giis.labs.lab1.gui.Lab1;
import giis.labs.lab1.model.WuAlgo;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;


public class WuAlgoAction extends Lab1Action {
	
	public WuAlgoAction(Lab1 frame, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(frame, text, icon, desc, mnemonic);
		setAlgo(new WuAlgo(Color.BLUE));
	}

	public void actionPerformed(ActionEvent arg0) {
		frame.setCurrentAlgo(getAlgo());
	}

}
