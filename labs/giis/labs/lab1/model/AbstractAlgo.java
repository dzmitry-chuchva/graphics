package giis.labs.lab1.model;

import giis.labs.base.api.IWorkArea;
import giis.labs.lab1.gui.Lab1;

import java.awt.Color;

public class AbstractAlgo implements LineAlgorithm {
	
	protected Color color = Color.BLACK;
	
	public AbstractAlgo() {
	}
	
	public AbstractAlgo(Color color) {
		this.color = color;
	}

	public void draw(Lab1 area, ModelLine line) {
		area.plot(line.getBegin(),color);
		area.plot(line.getEnd(),color);
	}
	
	public void drawDebug(Lab1 area, ModelLine line, int step) {
		area.plot(line.getBegin(),color);
		area.plot(line.getEnd(),color);
		area.reportMsg(algoMessage(area, line, line.getBegin().x, line.getEnd().y, step));
		if (step != 0) {
			area.stepDebugEnd();			
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String algoMessage(IWorkArea area, ModelLine line, int x, int y, int step) {
		return "AbstractAlgo: line = " + line + " step = " + step;
	}

}
