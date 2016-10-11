package giis.labs.lab3.actions;

import giis.labs.base.impl.LabsAction;
import giis.labs.lab3.gui.Lab3;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;


abstract public class Lab3Action extends LabsAction {
	
	protected Lab3 frame;
	
	public Lab3Action(Lab3 frame, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(frame,text,icon,desc,mnemonic);
		
		this.frame = frame;
	}
	
	abstract public void actionPerformed(ActionEvent e);

}
