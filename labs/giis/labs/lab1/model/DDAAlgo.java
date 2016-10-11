package giis.labs.lab1.model;

import giis.labs.base.api.IWorkArea;
import giis.labs.lab1.gui.Lab1;

import java.awt.Color;

/**
 * �����, �������������� ���������� ��������� ���.
 *  
 * @author Dzmitry Chuchva
 */
public class DDAAlgo extends AbstractAlgo implements LineAlgorithm {

	/**
	 * ����������� ������.
	 * 
	 * @param color ���� �����, ������������ ���������� 
	 */
	public DDAAlgo(Color color) {
		super(color);
	}

	/* (non-Javadoc)
	 * @see model.AbstractAlgo#draw(gui.WorkArea, model.ModelLine)
	 */
	public void draw(Lab1 area, ModelLine line) {
		// ���������� ��������� ���
		
		// ���� ��������� � �������� ����� ���������, ����� ����������� � �����
		if (line.getBegin().equals(line.getEnd())) {
			area.plot(line.getBegin(),color);
			return;
		}
		
		// ���������� ������� �� �������� ������� �� ��� � � Y
		int length = Math.max(Math.abs(line.getEnd().x - line.getBegin().x),
					 		  Math.abs(line.getEnd().y - line.getBegin().y));
		// �������� ������������ ���������� �� ��������������� ����
		float dx = (line.getEnd().x - line.getBegin().x) / (float)length;
		float dy = (line.getEnd().y - line.getBegin().y) / (float)length;
		
		// ���������� ��������� �����
		float x = line.getBegin().x;// + 0.5f * Math.signum(dx);
		float y = line.getBegin().y;// + 0.5f * Math.signum(dy);
		
		// ������ ��
		area.plot((int)x,(int)y,color);
		
		// �������� ����
		int i = 0;
		while (i < length) {
			// ���������� ���������� ���������
			x += dx;
			y += dy;
			
			// ������ ��������� ����� �������
			area.plot((int)x,(int)y,color);
			
			i++;
		}
	}
	
	/* (non-Javadoc)
	 * @see model.AbstractAlgo#drawDebug(gui.WorkArea, model.ModelLine, int)
	 */
	public void drawDebug(Lab1 area, ModelLine line, int step) {
		// ���������� ��������� ��� (���������� �����)
		
		if (line.getBegin().equals(line.getEnd())) { 
			area.reportMsg(algoMessage(area,line,line.getBegin().x, line.getBegin().y, step));
			area.plot(line.getBegin(),color);
			if (step != 0) {
				area.stepDebugEnd();
			}
			return;
		}
		
		int length = Math.max(Math.abs(line.getEnd().x - line.getBegin().x),
					 		  Math.abs(line.getEnd().y - line.getBegin().y));
		float dx = (line.getEnd().x - line.getBegin().x) / (float)length;
		float dy = (line.getEnd().y - line.getBegin().y) / (float)length;
		
		float x = line.getBegin().x;// + 0.5f * Math.signum(dx);
		float y = line.getBegin().y;// + 0.5f * Math.signum(dy);
		
		area.reportMsg(algoMessage(area,line,(int)x,(int)y,step));
		area.plot((int)x,(int)y,color);
						
		int i = 0;
		while (i < length && i < step) {
			x += dx;
			y += dy;
			
			area.plot((int)x,(int)y,color);
			
			i++;
			
			area.reportMsg(algoMessage(area,line,(int)x,(int)y,step));
		}
		
		if (i == length) {
			area.stepDebugEnd();
		}
	}
	
	/* (non-Javadoc)
	 * @see model.AbstractAlgo#algoMessage(gui.WorkArea, model.ModelLine, int, int, int)
	 */
	public String algoMessage(IWorkArea area, ModelLine line, int x, int y, int step) {
		return "�������� ���: (x1,y1) = (" + line.getBegin().x + ", " + line.getBegin().y +
		"), (x2,y2) = (" + line.getEnd().x + ", " + line.getEnd().y + "); ������� (x,y) = " 
		+ x + ", " + y + "), ��� �" + step + ".";
	}

}
