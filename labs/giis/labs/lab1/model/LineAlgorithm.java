package giis.labs.lab1.model;

import giis.labs.base.api.IWorkArea;
import giis.labs.lab1.gui.Lab1;

public interface LineAlgorithm {
	public void draw(Lab1 area, ModelLine line);
	public void drawDebug(Lab1 area, ModelLine line, int step);
	public String algoMessage(IWorkArea area, ModelLine line, int x, int y, int step);
}
