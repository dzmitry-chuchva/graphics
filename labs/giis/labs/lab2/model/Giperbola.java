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
	 * Функция, рисующая гиперболу 1-ого типа.
	 * @param area область рисования
	 * @param debugEnabled включен ли отладочный режим
	 * @param debugStep шаг отладки (если отладочный режим включен)
	 */
	private void drawType1(Lab2 area, boolean debugEnabled, int debugStep) {
		// каждая из следующих четырех переменных логического типа определяет,
		// необходимо ли рисовать гиперболу в соответствующей четверти системы
		// координат
		boolean first = true;
		boolean second = true;
		boolean third = true;
		boolean fourth = true;
		
		// будет содержать значения "H", "V" или "D" в зависимости от выбранного пикселя
		String pixel = "";
		
		// центр гиперболы (относительно его будут вычислятся все точки)
		int x0 = center.x;
		int y0 = center.y;
		// цвет гиперболы
		Color c = gipColor;
		
		// начальные координаты (относительно центра)
		int x = a;
		int y = 0;
		
		// значение "дельта" (начальное)
		int delta = b2 * (2 * x + 1) - a2 * (1 + 2 * y);
		// значение "сигма"
		int sigma = 0;
		
		// рисование начальных четырех точек (по количеству четвертей)
		first = gipPlot(area,first,x0 + x, y0 + y, c);
		second = gipPlot(area,second,x0 - x, y0 + y, c);
		third = gipPlot(area,third,x0 - x, y0 - y, c);
		fourth = gipPlot(area,fourth,x0 + x, y0 - y, c);
		
		// в случае отладки устанавливаем статус отладки
		if (debugEnabled && debugStep == 0) {
			setDebugStatus(x, y, delta, sigma, pixel);
			return;
		}
		
		// шаг отладки
		int stepCounter = 1;
		// пока хотя бы в одну из четвертей попадает точка гиперболы  
		while (first || second || third || fourth) {
			// определяем "дельта"
			if (delta == 0) {
				// "дельта" равно нулю - диагональный шаг
				x++;
				y++;
				// приращиваем "дельта"
				delta += b2 * (2 * x + 1) - a2 * (1 + 2 * y);
				pixel = "D";
			} else if (delta > 0) {
				// "дельта" больше нуля - определяем "сигма"
				sigma = 2 * (delta - b2 * x) - b2;
				// рассматриваем значение "сигма"
				if (sigma <= 0) {
					// "сигма" меньше либо равна нулю - диагональный шаг
					x++;
					y++;
					// приращиваем "дельта"
					delta += b2 * (2 * x + 1) - a2 * (1 + 2 * y);
					pixel = "D";
				} else {
					// "сигма" больше нуля - вертикальный шаг
					y++;
					// приращиваем "дельта"
					delta += - a2 * (1 + 2 * y);
					pixel = "V";
				}
			} else {
				// "дельта" меньше нуля - определяем "сигма"
				sigma = 2 * (delta + a2 * y) + a2;
				// рассматриваем значение "сигма"
				if (sigma <= 0) {
					// "сигма" меньще либо равна нулю - горизонтальный шаг
					x++;
					// приращиваем "дельта"
					delta += b2 * (2 * x + 1);
					pixel = "H";
				} else {
					// "сигма" больше нуля - диагональный шаг
					x++;
					y++;
					// приращиваем "дельта"
					delta += b2 * (2 * x + 1) - a2 * (1 + 2 * y);
					pixel = "D";
				}
			}
			// рисуем полученные точки в необходимых четвертях системы координат
			first = gipPlot(area,first,x0 + x, y0 + y, c);
			second = gipPlot(area,second,x0 - x, y0 + y, c);
			third = gipPlot(area,third,x0 - x, y0 - y, c);
			fourth = gipPlot(area,fourth,x0 + x, y0 - y, c);
			// в случае отладки устанавливаем отладочную информацию
			if (debugEnabled) {
				if (stepCounter >= debugStep) {
					setDebugStatus(x, y, delta, sigma, pixel);
					return;
				} else {
					stepCounter++;
				}
			}
		}
		// если в "отладочном" режиме - завершить его
		if (debugEnabled) {
			area.stepDebugEnd();
		}
	}
	
	/**
	 * Функция, рисующая гиперболу 2-ого типа.
	 * @param area область рисования
	 * @param debugEnabled включен ли "отладочный" режим
	 * @param debugStep шаг отладки
	 */
	private void drawType2(Lab2 area, boolean debugEnabled, int debugStep) {
		// каждая из следующих четырех переменных логического типа определяет,
		// необходимо ли рисовать гиперболу в соответствующей четверти системы
		// координат
		boolean first = true;
		boolean second = true;
		boolean third = true;
		boolean fourth = true;
		
		// будет содержать значения "H", "V" или "D" в зависимости от выбранного пикселя
		String pixel = "";
		
		// центр гиперболы (относительно его будут вычислятся все точки)
		int x0 = center.x;
		int y0 = center.y;
		// цвет гиперболы
		Color c = gipColor;
		
		// начальные координаты (относительно центра)
		int x = 0;
		int y = b;
		
		// значение "дельта" (начальное)
		int delta = - b2 * (2 * x + 1) + a2 * (1 + 2 * y);
		// значение "сигма"
		int sigma = 0;
		
		// рисование начальных четырех точек (по количеству четвертей)
		first = gipPlot(area,first,x0 + x, y0 + y, c);
		second = gipPlot(area,second,x0 - x, y0 + y, c);
		third = gipPlot(area,third,x0 - x, y0 - y, c);
		fourth = gipPlot(area,fourth,x0 + x, y0 - y, c);
		
		// в случае отладки устанавливаем статус отладки
		if (debugEnabled && debugStep == 0) {
			setDebugStatus(x, y, delta, sigma, pixel);
			return;
		}
		
		// шаг отладки
		int stepCounter = 1;
		// пока хотя бы в одну из четвертей попадает точка гиперболы  
		while (first || second || third || fourth) {
			// определяем "дельта"
			if (delta == 0) {
				// "дельта" равно нулю - диагональный шаг
				x++;
				y++;
				// приращиваем "дельта"
				delta += - b2 * (2 * x + 1) + a2 * (1 + 2 * y);
				pixel = "D";
			} else if (delta > 0) {
				// "дельта" больше нуля - определяем "сигма"
				sigma = 2 * (delta - a2 * y) - a2;
				// рассматриваем значение "сигма"
				if (sigma > 0) {
					// "сигма" больше нуля - горизонтальный шаг					
					x++;
					// приращиваем "дельта"
					delta += - b2 * (2 * x + 1);
					pixel = "H";
				} else {
					// "сигма" меньще либо равна нулю - диагональный шаг					
					x++;
					y++;
					// приращиваем "дельта"
					delta += - b2 * (2 * x + 1) + a2 * (1 + 2 * y);
					pixel = "D";
				}
			} else {
				// "дельта" меньше нуля - определяем "сигма"
				sigma = 2 * (delta + b2 * x) + b2;
				// рассматриваем значение "сигма"
				if (sigma > 0) {
					// "сигма" больше нуля - диагональный шаг					
					x++;
					y++;
					// приращиваем "дельта"
					delta += - b2 * (2 * x + 1) + a2 * (1 + 2 * y);
					pixel = "D";
				} else {
					// "сигма" меньше либо равна нулю - вертикальный шаг					
					y++;
					// приращиваем "дельта"
					delta += a2 * (1 + 2 * y);
					pixel = "V";
				}
			}
			// рисуем полученные точки в необходимых четвертях системы координат
			first = gipPlot(area,first,x0 + x, y0 + y, c);
			second = gipPlot(area,second,x0 - x, y0 + y, c);
			third = gipPlot(area,third,x0 - x, y0 - y, c);
			fourth = gipPlot(area,fourth,x0 + x, y0 - y, c);
			// в случае отладки устанавливаем отладочную информацию
			if (debugEnabled) {
				if (stepCounter >= debugStep) {
					setDebugStatus(x, y, delta, sigma, pixel);
					return;
				} else {
					stepCounter++;
				}
			}
		}
		// если в "отладочном" режиме - завершить его
		if (debugEnabled) {
			area.stepDebugEnd();
		}
	}
	
	/**
	 * Функция, рисующая параболу 1-ого типа.
	 * @param area область рисования
	 * @param debugEnabled включен ли "отладочный" режим
	 * @param debugStep шаг отладки
	 */
//	private void drawType3(Lab2 area, boolean debugEnabled, int debugStep) {
//		// каждая из следующих двух переменных логического типа определяет,
//		// необходимо ли рисовать гиперболу в соответствующей четверти системы
//		// координат
//		boolean first = true;
//		boolean fourth = true;
//		// "строковое" название выбранного пикселя (диагональный, вертикальный, горизонтальный)
//		String pixel = "";
//		
//		// центр гиперболы (центр системы координат, в которой будет рисоваться гипербола)
//		int x0 = center.x;
//		int y0 = center.y;
//		// цвет гиперболы
//		Color c = gipColor;
//				
//		// начальная точка (относительно центра)
//		int x = 0;
//		int y = 0;
//		
//		// начальное значение "дельта"
//		int delta = - 2 * y + 2 * p - 1;
//		// начальное значение "сигма"
//		int sigma = 0;
//		
//		// рисуем первые две точки
//		first = gipPlot(area,first,x0 + x, y0 + y, c);
//		fourth = gipPlot(area,fourth,x0 + x, y0 - y, c);
//		
//		// в случае отладки устанавливаем отладочную информацию
//		if (debugEnabled && debugStep == 0) {
//			setDebugStatus(x, y, delta, sigma, pixel);
//			return;
//		}
//		
//		// счетчик шагов алгоритма
//		int stepCounter = 1;
//		// пока есть необходимость рисовать гиперболу
//		while (first || fourth) {
//			// рассматриваем значение "дельта"
//			if (delta == 0) {
//				// "дельта" равно нулю - выбираем диагональный шаг
//				x++;
//				y++;
//				// приращиваем "дельта"
//				delta += - 2 * y + 2 * p - 1;
//				pixel = "D";
//			} else if (delta > 0) {
//				// "дельта" больще нуля - определяем "сигма"
//				sigma = 2 * (delta - p);
//				// рассматриваем "сигма"
//				if (sigma <= 0) {
//					// "сигма" меньше либо равна нулю - диагональный шаг
//					x++;
//					y++;
//					// приращиваем "дельта"
//					delta += - 2 * y + 2 * p - 1;
//					pixel = "D";
//				} else {
//					// "сигма" больше нуля - вертикальный шаг
//					y++;
//					delta += - 2 * y - 1;
//					pixel = "V";
//				}
//			} else {
//				// "дельта" меньше нуля - определяем "сигма"
//				sigma = 2 * (delta + y) + 1;
//				// рассматриваем "сигма"
//				if (sigma <= 0) {
//					// "сигма" меньше либо равна нулю - горизонтальный шаг
//					x++;
//					// приращиваем "дельта"
//					delta += 2 * p;
//					pixel = "H";
//				} else {
//					// "сигма" больше нуля - диагональный шаг
//					x++;
//					y++;
//					// приращиваем "дельта"
//					delta += - 2 * y + 2 * p - 1;
//					pixel = "D";
//				}
//			}
//			// рисуем если необходимо пиксели
//			first = gipPlot(area,first,x0 + x, y0 + y, c);
//			fourth = gipPlot(area,fourth,x0 + x, y0 - y, c);
//			// если находимся в отладочном режиме
//			if (debugEnabled) {
//				// устанавливаем отладочную информацию
//				if (stepCounter >= debugStep) {
//					setDebugStatus(x, y, delta, sigma, pixel);
//					return;
//				} else {
//					stepCounter++;
//				}
//			}
//		}
//		
//		// в случае отладочного режима - завершаем отладку после завершения алгоритма
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
