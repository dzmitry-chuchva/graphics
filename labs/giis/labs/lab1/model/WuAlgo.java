package giis.labs.lab1.model;

import giis.labs.base.api.IWorkArea;
import giis.labs.lab1.gui.Lab1;

import java.awt.Color;

/**
 * Класс, представляющий реализацию алгоритма Ву.
 * @author Dzmitry Chuchva
 */
public class WuAlgo extends AbstractAlgo implements LineAlgorithm {
	
	/**
	 * Конструктор класса.
	 * @param color цвет линии, генерируемой алгоритмом
	 */
	public WuAlgo(Color color) {
		super(color);
	}

	/* (non-Javadoc)
	 * @see model.AbstractAlgo#draw(gui.WorkArea, model.ModelLine)
	 */
	public void draw(Lab1 area, ModelLine line) {
		
		// реализация алгоритма Ву
		
		// получим координаты начальной и конечной точки
		int x1 = line.getBegin().x;
		int y1 = line.getBegin().y;
		int x2 = line.getEnd().x;
		int y2 = line.getEnd().y;
				
		// определим основную ось
		boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);
		// если основная ось Y, меняем начальную и конечную точку
		if (steep) {
			int tmp = x1;
			x1 = y1;
			y1 = tmp;
			
			tmp = x2;
			x2 = y2;
			y2 = tmp;
		}
		// если линия идет справа налево, меняем начальную и конечную точку
		if (x1 > x2) {
			int tmp = x1;
			x1 = x2;
			x2 = tmp;
			
			tmp = y1;
			y1 = y2;
			y2 = tmp;
		}
		
		// определим длины проекций отрезка на оси
		int dx = x2 - x1;
		int dy = y2 - y1;
		
		// если линия строго горизонтальная или вертикальная, используем алгоритм Брезенхама
		if (dx == 0 || dy == 0) {
			new BresenhamAlgo(color).draw(area, line);
			return;
		}
		
		// коэффициент уклона (тангенс угла наклона)
		float gradient = (float)dy / dx;
		
		// вычисляем параметры начальной точки
		float xend = round(x1);
		float yend = y1 + gradient * (xend - x1);
		float xgap = rfpart(x1 + 0.5f);
		float xpxl1 = xend;
		float ypxl1 = ipart(yend);
		
		// производим проверку октанта
		if (steep) {
			wuPlot(area,ypxl1,xpxl1, rfpart(yend) * xgap);
			wuPlot(area,ypxl1 + 1,xpxl1, fpart(yend) * xgap);
		} else {
			wuPlot(area,xpxl1,ypxl1, rfpart(yend) * xgap);
			wuPlot(area,xpxl1,ypxl1 + 1, fpart(yend) * xgap);
		}
		
		float intery = yend + gradient;
		
		// вычисляем параметры конечной точки
		xend = round(x2);
		yend = y2 + gradient * (xend - x2);
		xgap = rfpart(x2 + 0.5f);
		float xpxl2 = xend;
		float ypxl2	 = ipart(yend);
		
		// производим проверку октанта
		if (steep) {
			wuPlot(area,ypxl2,xpxl2, rfpart(yend) * xgap);
			wuPlot(area,ypxl2 + 2,xpxl2, fpart(yend) * xgap);
		} else {
			wuPlot(area,xpxl2,ypxl2, rfpart(yend) * xgap);
			wuPlot(area,xpxl2,ypxl2 + 1, fpart(yend) * xgap);
		}
		
		// основной цикл
		for (float x = xpxl1 + 1; x <= xpxl2 - 1; x += 1.0f) {
			// производим проверку октанта
			if (steep) {
				wuPlot(area, ipart(intery), x, rfpart(intery));
				wuPlot(area, ipart(intery) + 1, x, fpart(intery));
			} else {
				wuPlot(area,x,ipart(intery), rfpart(intery));
				wuPlot(area,x,ipart(intery) + 1, fpart(intery));
			}
			
			intery += gradient;
		}
	}
	
	/* (non-Javadoc)
	 * @see model.AbstractAlgo#drawDebug(gui.WorkArea, model.ModelLine, int)
	 */
	public void drawDebug(Lab1 area, ModelLine line, int step) {
		
		// реализация алгоритма Ву (отладочная версия)
		
		// получим координаты начальной и конечной точки
		int x1 = line.getBegin().x;
		int y1 = line.getBegin().y;
		int x2 = line.getEnd().x;
		int y2 = line.getEnd().y;
			
		// определим основную ось
		boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);
		// если основная ось Y, меняем начальную и конечную точку
		if (steep) {
			int tmp = x1;
			x1 = y1;
			y1 = tmp;
			
			tmp = x2;
			x2 = y2;
			y2 = tmp;
		}
		
		// если линия идет справа налево, меняем начальную и конечную точку
		if (x1 > x2) {
			int tmp = x1;
			x1 = x2;
			x2 = tmp;
			
			tmp = y1;
			y1 = y2;
			y2 = tmp;
		}
		
		// определим длины проекций отрезка на оси
		int dx = x2 - x1;
		int dy = y2 - y1;
		
		// если линия строго горизонтальная или вертикальная, используем алгоритм Брезенхама 
		if (dx == 0 || dy == 0) {
			new BresenhamAlgo(color).drawDebug(area, line, step);
			return;
		}
		
		// коэффициент уклона (тангенс угла наклона)
		float gradient = (float)dy / dx;
		
		// вычисляем параметры начальной точки
		float xend = round(x1);
		float yend = y1 + gradient * (xend - x1);
		float xgap = rfpart(x1 + 0.5f);
		float xpxl1 = xend;
		float ypxl1 = ipart(yend);
		
		// производим проверку октанта
		if (steep) {
			wuPlot(area,ypxl1,xpxl1, rfpart(yend) * xgap);
			wuPlot(area,ypxl1 + 1,xpxl1, fpart(yend) * xgap);
		} else {
			wuPlot(area,xpxl1,ypxl1, rfpart(yend) * xgap);
			wuPlot(area,xpxl1,ypxl1 + 1, fpart(yend) * xgap);
		}
		area.reportMsg(algoMessage(area,line,(int)xpxl1,(int)ypxl1,step));
		
		float intery = yend + gradient;
		
		// вычисляем параметры конечной точки
		xend = round(x2);
		yend = y2 + gradient * (xend - x2);
		xgap = rfpart(x2 + 0.5f);
		float xpxl2 = xend;
		float ypxl2	 = ipart(yend);
		
		// производим проверку октанта
		if (steep) {
			wuPlot(area,ypxl2,xpxl2, rfpart(yend) * xgap);
			wuPlot(area,ypxl2 + 2,xpxl2, fpart(yend) * xgap);
		} else {
			wuPlot(area,xpxl2,ypxl2, rfpart(yend) * xgap);
			wuPlot(area,xpxl2,ypxl2 + 1, fpart(yend) * xgap);
		}
		
		// основной цикл
		float x; int stepCounter = 0;
		for (x = xpxl1 + 1; x <= xpxl2 - 1 && stepCounter < step; x += 1.0f, stepCounter++) {
			area.reportMsg(algoMessage(area,line,(int)x,(int)intery,step));
			// производим проверку октанта
			if (steep) {
				wuPlot(area, ipart(intery), x, rfpart(intery));
				wuPlot(area, ipart(intery) + 1, x, fpart(intery));
			} else {
				wuPlot(area,x,ipart(intery), rfpart(intery));
				wuPlot(area,x,ipart(intery) + 1, fpart(intery));
			}
			
			intery += gradient;
		}
		
		if (x > xpxl2 - 1 && step != 0) {
			area.stepDebugEnd();
		}
	}
	
	/**
	 * Установка пиксела для алгоритма Ву.
	 * 
	 * @param area ссылка на рабочую область
	 * @param x координата Х
	 * @param y координата Y
	 * @param i интенсивность цвета
	 */
	private void wuPlot(Lab1 area, float x, float y, float i) {
		// выбираем интенсивность цвета в соответствии с переданным коэффициентом
		Color c = new Color(color.getRed(),color.getGreen(),color.getBlue(),(int)(255 * i));
		area.plot((int)x,(int)y,c);
	}
	
	/**
	 * Возвращет целую часть.
	 * 
	 * @param x вещественный аргумент
	 * @return целая часть от аргумента
	 */
	private int ipart(float x) {
		return (int)x;
	}
	
	/**
	 * Возвращает округленное вещественное число.
	 * 
	 * @param x вещественный аргумент
	 * @return целое, являющееся результатом округления аргумента
	 */
	private int round(float x) {
		return ipart(x + 0.5f);
	}
	
	/**
	 * Возвращает дробную часть вещественного числа.
	 * 
	 * @param x вещественный аргумент
	 * @return дробная часть аргумента
	 */
	private float fpart(float x) {
		return (Math.abs(x - ipart(x)));
	}
	
	/**
	 * Возвращает дополнение вещественного числа до единицы.
	 * 
	 * @param x вещественный аргумент
	 * @return дополнение вещественного аргумента до единицы
	 */
	private float rfpart(float x) {
		return 1.0f - fpart(x);
	}
	
	/* (non-Javadoc)
	 * @see model.AbstractAlgo#algoMessage(gui.WorkArea, model.ModelLine, int, int, int)
	 */
	public String algoMessage(IWorkArea area, ModelLine line, int x, int y, int step) {
		return "Алгоритм Ву: (x1,y1) = (" + line.getBegin().x + ", " + line.getBegin().y +
		"), (x2,y2) = (" + line.getEnd().x + ", " + line.getEnd().y + "); текущие (x,y) = " 
		+ x + ", " + y + "), шаг №" + step + ".";
	}

}
