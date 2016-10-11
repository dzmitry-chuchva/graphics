package giis.labs.lab4.actions;

import giis.labs.base.impl.LabsAction;
import giis.labs.lab4.gui.Lab4;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;


abstract public class Lab4Action extends LabsAction {
	
	protected Lab4 frame;
	
	public Lab4Action(Lab4 frame, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(frame,text,icon,desc,mnemonic);
		
		this.frame = frame;
	}
	
	abstract public void actionPerformed(ActionEvent e);

}
