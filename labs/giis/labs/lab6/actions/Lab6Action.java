package giis.labs.lab6.actions;

import giis.labs.base.impl.LabsAction;
import giis.labs.lab6.gui.Lab6;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;


abstract public class Lab6Action extends LabsAction {
	
	protected Lab6 frame;
	
	public Lab6Action(Lab6 frame, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(frame,text,icon,desc,mnemonic);
		
		this.frame = frame;
	}
	
	abstract public void actionPerformed(ActionEvent e);

}
