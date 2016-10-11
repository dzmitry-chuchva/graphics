package giis.labs.lab2.actions;

import giis.labs.base.impl.LabsAction;
import giis.labs.lab2.gui.Lab2;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;


abstract public class Lab2Action extends LabsAction {
	
	protected Lab2 frame;
	
	public Lab2Action(Lab2 frame, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(frame,text,icon,desc,mnemonic);
		
		this.frame = frame;
	}
	
	abstract public void actionPerformed(ActionEvent e);

}
