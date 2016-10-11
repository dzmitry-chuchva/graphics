package giis.labs.lab1.actions;

import giis.labs.base.impl.LabsAction;
import giis.labs.lab1.gui.Lab1;
import giis.labs.lab1.model.LineAlgorithm;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;


abstract public class Lab1Action extends LabsAction {
	
	protected Lab1 frame;
	protected LineAlgorithm algo;
	
	public Lab1Action(Lab1 frame, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(frame,text,icon,desc,mnemonic);
		
		this.frame = frame;
	}
	
	abstract public void actionPerformed(ActionEvent e);

	public LineAlgorithm getAlgo() {
		return algo;
	}

	public void setAlgo(LineAlgorithm algo) {
		this.algo = algo;
	}

}
