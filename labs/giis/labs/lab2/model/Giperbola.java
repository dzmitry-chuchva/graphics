package giis.labs.lab2.model;

import giis.labs.lab2.gui.Lab2;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class Giperbola {
	public static final int GIPERBOLA_TYPE1 = 0;
	public static final int GIPERBOLA_TYPE2 = 1;
	
	private Point center;
	private int type;
	
	private int a,b;
	
	private int a2,b2;
	
	private int lastDelta, lastSigma;
	private int lastX, lastY;
	private String lastPixel;
	
	private Random colorGen = new Random(System.currentTimeMillis());
	private Color gipColor;
	
	public Giperbola() {
		setCenter(new Point(0,0));
		setType(GIPERBOLA_TYPE1);
		setA(4); setB(4);
		setColor(new Color(colorGen.nextInt(256),colorGen.nextInt(256),colorGen.nextInt(256)));
	}
	
	public Giperbola(Point center, int type) {
		this();
		setCenter(center);
		setType(type);
	}
	
	public Giperbola(int type) {
		this();
		setType(type);
	}
	
	public Giperbola(Giperbola ref) {
		this();
		setCenter(ref.getCenter());
		setType(ref.getType());
		setA(ref.getA());
		setB(ref.getB());
	}

	public Color getColor() {
		return gipColor;
	}

	public void setColor(Color gipColor) {
		this.gipColor = gipColor;
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		if (center != null) {
			this.center = center;
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		if (type == GIPERBOLA_TYPE1 || type == GIPERBOLA_TYPE2) {
			this.type = type;
		}
	}
	
	public int getA() {
		return a;
	}

	public void setA(int a) {
		if (a > 0) {
			this.a = a;
			a2 = a * a;
		}
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		if (b > 0) {
			this.b = b;
			b2 = b * b;
		}
	}

	public void draw(Lab2 area, boolean debugEnabled, int debugStep) {
		switch (type) {
		case GIPERBOLA_TYPE1:
			drawType1(area,debugEnabled,debugStep);
			break;
		case GIPERBOLA_TYPE2:
			drawType2(area,debugEnabled,debugStep);
			break;
		}
	}
	
	/**
	 * �������, �������� ��������� 1-��� ����.
	 * @param area ������� ���������
	 * @param debugEnabled ������� �� ���������� �����
	 * @param debugStep ��� ������� (���� ���������� ����� �������)
	 */
	private void drawType1(Lab2 area, boolean debugEnabled, int debugStep) {
		// ������ �� ��������� ������� ���������� ����������� ���� ����������,
		// ���������� �� �������� ��������� � ��������������� �������� �������
		// ���������
		boolean first = true;
		boolean second = true;
		boolean third = true;
		boolean fourth = true;
		
		// ����� ��������� �������� "H", "V" ��� "D" � ����������� �� ���������� �������
		String pixel = "";
		
		// ����� ��������� (������������ ��� ����� ���������� ��� �����)
		int x0 = center.x;
		int y0 = center.y;
		// ���� ���������
		Color c = gipColor;
		
		// ��������� ���������� (������������ ������)
		int x = a;
		int y = 0;
		
		// �������� "������" (���������)
		int delta = b2 * (2 * x + 1) - a2 * (1 + 2 * y);
		// �������� "�����"
		int sigma = 0;
		
		// ��������� ��������� ������� ����� (�� ���������� ���������)
		first = gipPlot(area,first,x0 + x, y0 + y, c);
		second = gipPlot(area,second,x0 - x, y0 + y, c);
		third = gipPlot(area,third,x0 - x, y0 - y, c);
		fourth = gipPlot(area,fourth,x0 + x, y0 - y, c);
		
		// � ������ ������� ������������� ������ �������
		if (debugEnabled && debugStep == 0) {
			setDebugStatus(x, y, delta, sigma, pixel);
			return;
		}
		
		// ��� �������
		int stepCounter = 1;
		// ���� ���� �� � ���� �� ��������� �������� ����� ���������  
		while (first || second || third || fourth) {
			// ���������� "������"
			if (delta == 0) {
				// "������" ����� ���� - ������������ ���
				x++;
				y++;
				// ����������� "������"
				delta += b2 * (2 * x + 1) - a2 * (1 + 2 * y);
				pixel = "D";
			} else if (delta > 0) {
				// "������" ������ ���� - ���������� "�����"
				sigma = 2 * (delta - b2 * x) - b2;
				// ������������� �������� "�����"
				if (sigma <= 0) {
					// "�����" ������ ���� ����� ���� - ������������ ���
					x++;
					y++;
					// ����������� "������"
					delta += b2 * (2 * x + 1) - a2 * (1 + 2 * y);
					pixel = "D";
				} else {
					// "�����" ������ ���� - ������������ ���
					y++;
					// ����������� "������"
					delta += - a2 * (1 + 2 * y);
					pixel = "V";
				}
			} else {
				// "������" ������ ���� - ���������� "�����"
				sigma = 2 * (delta + a2 * y) + a2;
				// ������������� �������� "�����"
				if (sigma <= 0) {
					// "�����" ������ ���� ����� ���� - �������������� ���
					x++;
					// ����������� "������"
					delta += b2 * (2 * x + 1);
					pixel = "H";
				} else {
					// "�����" ������ ���� - ������������ ���
					x++;
					y++;
					// ����������� "������"
					delta += b2 * (2 * x + 1) - a2 * (1 + 2 * y);
					pixel = "D";
				}
			}
			// ������ ���������� ����� � ����������� ��������� ������� ���������
			first = gipPlot(area,first,x0 + x, y0 + y, c);
			second = gipPlot(area,second,x0 - x, y0 + y, c);
			third = gipPlot(area,third,x0 - x, y0 - y, c);
			fourth = gipPlot(area,fourth,x0 + x, y0 - y, c);
			// � ������ ������� ������������� ���������� ����������
			if (debugEnabled) {
				if (stepCounter >= debugStep) {
					setDebugStatus(x, y, delta, sigma, pixel);
					return;
				} else {
					stepCounter++;
				}
			}
		}
		// ���� � "����������" ������ - ��������� ���
		if (debugEnabled) {
			area.stepDebugEnd();
		}
	}
	
	/**
	 * �������, �������� ��������� 2-��� ����.
	 * @param area ������� ���������
	 * @param debugEnabled ������� �� "����������" �����
	 * @param debugStep ��� �������
	 */
	private void drawType2(Lab2 area, boolean debugEnabled, int debugStep) {
		// ������ �� ��������� ������� ���������� ����������� ���� ����������,
		// ���������� �� �������� ��������� � ��������������� �������� �������
		// ���������
		boolean first = true;
		boolean second = true;
		boolean third = true;
		boolean fourth = true;
		
		// ����� ��������� �������� "H", "V" ��� "D" � ����������� �� ���������� �������
		String pixel = "";
		
		// ����� ��������� (������������ ��� ����� ���������� ��� �����)
		int x0 = center.x;
		int y0 = center.y;
		// ���� ���������
		Color c = gipColor;
		
		// ��������� ���������� (������������ ������)
		int x = 0;
		int y = b;
		
		// �������� "������" (���������)
		int delta = - b2 * (2 * x + 1) + a2 * (1 + 2 * y);
		// �������� "�����"
		int sigma = 0;
		
		// ��������� ��������� ������� ����� (�� ���������� ���������)
		first = gipPlot(area,first,x0 + x, y0 + y, c);
		second = gipPlot(area,second,x0 - x, y0 + y, c);
		third = gipPlot(area,third,x0 - x, y0 - y, c);
		fourth = gipPlot(area,fourth,x0 + x, y0 - y, c);
		
		// � ������ ������� ������������� ������ �������
		if (debugEnabled && debugStep == 0) {
			setDebugStatus(x, y, delta, sigma, pixel);
			return;
		}
		
		// ��� �������
		int stepCounter = 1;
		// ���� ���� �� � ���� �� ��������� �������� ����� ���������  
		while (first || second || third || fourth) {
			// ���������� "������"
			if (delta == 0) {
				// "������" ����� ���� - ������������ ���
				x++;
				y++;
				// ����������� "������"
				delta += - b2 * (2 * x + 1) + a2 * (1 + 2 * y);
				pixel = "D";
			} else if (delta > 0) {
				// "������" ������ ���� - ���������� "�����"
				sigma = 2 * (delta - a2 * y) - a2;
				// ������������� �������� "�����"
				if (sigma > 0) {
					// "�����" ������ ���� - �������������� ���					
					x++;
					// ����������� "������"
					delta += - b2 * (2 * x + 1);
					pixel = "H";
				} else {
					// "�����" ������ ���� ����� ���� - ������������ ���					
					x++;
					y++;
					// ����������� "������"
					delta += - b2 * (2 * x + 1) + a2 * (1 + 2 * y);
					pixel = "D";
				}
			} else {
				// "������" ������ ���� - ���������� "�����"
				sigma = 2 * (delta + b2 * x) + b2;
				// ������������� �������� "�����"
				if (sigma > 0) {
					// "�����" ������ ���� - ������������ ���					
					x++;
					y++;
					// ����������� "������"
					delta += - b2 * (2 * x + 1) + a2 * (1 + 2 * y);
					pixel = "D";
				} else {
					// "�����" ������ ���� ����� ���� - ������������ ���					
					y++;
					// ����������� "������"
					delta += a2 * (1 + 2 * y);
					pixel = "V";
				}
			}
			// ������ ���������� ����� � ����������� ��������� ������� ���������
			first = gipPlot(area,first,x0 + x, y0 + y, c);
			second = gipPlot(area,second,x0 - x, y0 + y, c);
			third = gipPlot(area,third,x0 - x, y0 - y, c);
			fourth = gipPlot(area,fourth,x0 + x, y0 - y, c);
			// � ������ ������� ������������� ���������� ����������
			if (debugEnabled) {
				if (stepCounter >= debugStep) {
					setDebugStatus(x, y, delta, sigma, pixel);
					return;
				} else {
					stepCounter++;
				}
			}
		}
		// ���� � "����������" ������ - ��������� ���
		if (debugEnabled) {
			area.stepDebugEnd();
		}
	}
	
	/**
	 * �������, �������� �������� 1-��� ����.
	 * @param area ������� ���������
	 * @param debugEnabled ������� �� "����������" �����
	 * @param debugStep ��� �������
	 */
//	private void drawType3(Lab2 area, boolean debugEnabled, int debugStep) {
//		// ������ �� ��������� ���� ���������� ����������� ���� ����������,
//		// ���������� �� �������� ��������� � ��������������� �������� �������
//		// ���������
//		boolean first = true;
//		boolean fourth = true;
//		// "���������" �������� ���������� ������� (������������, ������������, ��������������)
//		String pixel = "";
//		
//		// ����� ��������� (����� ������� ���������, � ������� ����� ���������� ���������)
//		int x0 = center.x;
//		int y0 = center.y;
//		// ���� ���������
//		Color c = gipColor;
//				
//		// ��������� ����� (������������ ������)
//		int x = 0;
//		int y = 0;
//		
//		// ��������� �������� "������"
//		int delta = - 2 * y + 2 * p - 1;
//		// ��������� �������� "�����"
//		int sigma = 0;
//		
//		// ������ ������ ��� �����
//		first = gipPlot(area,first,x0 + x, y0 + y, c);
//		fourth = gipPlot(area,fourth,x0 + x, y0 - y, c);
//		
//		// � ������ ������� ������������� ���������� ����������
//		if (debugEnabled && debugStep == 0) {
//			setDebugStatus(x, y, delta, sigma, pixel);
//			return;
//		}
//		
//		// ������� ����� ���������
//		int stepCounter = 1;
//		// ���� ���� ������������� �������� ���������
//		while (first || fourth) {
//			// ������������� �������� "������"
//			if (delta == 0) {
//				// "������" ����� ���� - �������� ������������ ���
//				x++;
//				y++;
//				// ����������� "������"
//				delta += - 2 * y + 2 * p - 1;
//				pixel = "D";
//			} else if (delta > 0) {
//				// "������" ������ ���� - ���������� "�����"
//				sigma = 2 * (delta - p);
//				// ������������� "�����"
//				if (sigma <= 0) {
//					// "�����" ������ ���� ����� ���� - ������������ ���
//					x++;
//					y++;
//					// ����������� "������"
//					delta += - 2 * y + 2 * p - 1;
//					pixel = "D";
//				} else {
//					// "�����" ������ ���� - ������������ ���
//					y++;
//					delta += - 2 * y - 1;
//					pixel = "V";
//				}
//			} else {
//				// "������" ������ ���� - ���������� "�����"
//				sigma = 2 * (delta + y) + 1;
//				// ������������� "�����"
//				if (sigma <= 0) {
//					// "�����" ������ ���� ����� ���� - �������������� ���
//					x++;
//					// ����������� "������"
//					delta += 2 * p;
//					pixel = "H";
//				} else {
//					// "�����" ������ ���� - ������������ ���
//					x++;
//					y++;
//					// ����������� "������"
//					delta += - 2 * y + 2 * p - 1;
//					pixel = "D";
//				}
//			}
//			// ������ ���� ���������� �������
//			first = gipPlot(area,first,x0 + x, y0 + y, c);
//			fourth = gipPlot(area,fourth,x0 + x, y0 - y, c);
//			// ���� ��������� � ���������� ������
//			if (debugEnabled) {
//				// ������������� ���������� ����������
//				if (stepCounter >= debugStep) {
//					setDebugStatus(x, y, delta, sigma, pixel);
//					return;
//				} else {
//					stepCounter++;
//				}
//			}
//		}
//		
//		// � ������ ����������� ������ - ��������� ������� ����� ���������� ���������
//		if (debugEnabled) {
//			area.stepDebugEnd();
//		}
//	}
	
	private boolean gipPlot(Lab2 area, boolean needed, int x, int y, Color c) {
		if (!needed) {
			return needed;
		} else {
			int cells = area.getSideSize() / area.getCellSize();
			if (x >= cells || x < 0 || y >= cells || y < 0) {
				return false;
			} else {
				area.plot(x,y,c);
				return true;
			}
		}
	}

	public int getLastDelta() {
		return lastDelta;
	}

	public String getLastPixel() {
		return lastPixel;
	}

	public int getLastSigma() {
		return lastSigma;
	}

	public int getLastX() {
		return lastX;
	}

	public int getLastY() {
		return lastY;
	}
	
	private void setDebugStatus(int x, int y, int delta, int sigma, String pixel) {
		lastX = x;
		lastY = y;
		lastDelta = delta;
		lastSigma = sigma;
		lastPixel = pixel;
	}
}
