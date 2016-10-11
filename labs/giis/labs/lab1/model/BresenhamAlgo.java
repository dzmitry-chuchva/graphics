package giis.labs.lab1.model;

import giis.labs.base.api.IWorkArea;
import giis.labs.lab1.gui.Lab1;

import java.awt.Color;

/**
 * �����, �������������� ���������� ��������� ����������.
 *  
 * @author Dzmitry Chuchva
 */
public class BresenhamAlgo extends AbstractAlgo implements LineAlgorithm {

	/**
	 * ����������� ������.
	 * 
	 * @param color ���� �����, ������������ ����������
	 */
	public BresenhamAlgo(Color color) {
		super(color);
	}

	/* (non-Javadoc)
	 * @see model.AbstractAlgo#draw(gui.WorkArea, model.ModelLine)
	 */
	public void draw(Lab1 area, ModelLine line) {
		// ���������� ��������� ����������
		
		// �������� ���������� ��������� � �������� �����
		int x1 = line.getBegin().x;
		int y1 = line.getBegin().y;
		int x2 = line.getEnd().x;
		int y2 = line.getEnd().y;
		
		// ���������� ������� �� �������� ������� �� ������������ ���
		boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);
		
		// ���� �������� ��� Y, ������ ��������� � �������� ����� 
		if (steep) {
			int tmp = x1;
			x1 = y1;
			y1 = tmp;
			
			tmp = x2;
			x2 = y2;
			y2 = tmp;
		}
		// ���� ����� ���� ������ ������, ������ ��������� � �������� �����
		if (x1 > x2) {
			int tmp = x1;
			x1 = x2;
			x2 = tmp;
			
			tmp = y1;
			y1 = y2;
			y2 = tmp;
		}
		
		// ���������� ����� �������� ������� �� ������������ ��� 
		int dx = x2 - x1;
		int dy = Math.abs(y2 - y1);
		// ���������� "������"
		int error = - (dx >> 1);
		// ����������, � ����� ������� ����� ����� ���������� y
		int ystep;
		if (y1 < y2) {
			// � ����� ����������
			ystep = 1;
		} else {
			// � ����� �������
			ystep = -1;
		}
		
		// ������������� ��������� �������� �
		int y = y1;
		// �������� ����
		for (int x = x1; x <= x2; x++) {
			// ���������� ������
			if (steep) {
				area.plot(y, x, color);
			} else {
				area.plot(x, y, color);
			}
			// ����������� ������
			error += dy;
			// ���� ��� ����� ������ �������
			if (error > 0) {
				// ���������� ���������� ���������� �
				y += ystep;
				// ���������� ������
				error -= dx;
			}
		}
	}
	
	public void drawDebug(Lab1 area, ModelLine line, int step) {
		// ���������� ��������� ���������� (���������� ������)
		int x1 = line.getBegin().x;
		int y1 = line.getBegin().y;
		int x2 = line.getEnd().x;
		int y2 = line.getEnd().y;
				
		boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);
		if (steep) {
			int tmp = x1;
			x1 = y1;
			y1 = tmp;
			
			tmp = x2;
			x2 = y2;
			y2 = tmp;
		}
		
		if (x1 > x2) {
			int tmp = x1;
			x1 = x2;
			x2 = tmp;
			
			tmp = y1;
			y1 = y2;
			y2 = tmp;
		}
		
		int dx = x2 - x1;
		int dy = Math.abs(y2 - y1);
		int error = - (dx >> 1);
		int ystep;
		if (y1 < y2) {
			ystep = 1;
		} else {
			ystep = -1;
		}
		
		int y = y1;
		int x;
		for (x = x1; x <= x2 && x <= x1 + step; x++) {
			area.reportMsg(algoMessage(area,line,x,y,step));			
			if (steep) {
				area.plot(y, x, color);
			} else {
				area.plot(x, y, color);
			}
			error += dy;
			if (error > 0) {
				y += ystep;
				error -= dx;
			}
		}
		
		if (x > x2 && step != 0) {
			area.stepDebugEnd();
		}
	}

	public String algoMessage(IWorkArea area, ModelLine line, int x, int y, int step) {
		return "�������� ����������: (x1,y1) = (" + line.getBegin().x + ", " + line.getBegin().y +
		"), (x2,y2) = (" + line.getEnd().x + ", " + line.getEnd().y + "); ������� (x,y) = " 
		+ x + ", " + y + "), ��� �" + step + ".";
	}
	
}
