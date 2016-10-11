package giis.labs.base.impl;

import giis.labs.base.api.IWorkArea;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;


abstract public class LabsAction extends AbstractAction {
	
	protected IWorkArea frame;
	
	public LabsAction(IWorkArea frame, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(text, icon);
		
		this.frame = frame;
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
	}
	
	abstract public void actionPerformed(ActionEvent e);

}
