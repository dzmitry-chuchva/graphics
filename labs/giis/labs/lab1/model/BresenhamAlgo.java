package giis.labs.lab1.model;

import giis.labs.base.api.IWorkArea;
import giis.labs.lab1.gui.Lab1;

import java.awt.Color;

/**
 * Класс, представляющий реализацию алгоритма Брезенхама.
 *  
 * @author Dzmitry Chuchva
 */
public class BresenhamAlgo extends AbstractAlgo implements LineAlgorithm {

	/**
	 * Конструктор класса.
	 * 
	 * @param color цвет линии, генерируемой алгоритмом
	 */
	public BresenhamAlgo(Color color) {
		super(color);
	}

	/* (non-Javadoc)
	 * @see model.AbstractAlgo#draw(gui.WorkArea, model.ModelLine)
	 */
	public void draw(Lab1 area, ModelLine line) {
		// реализация алгоритма Брезенхама
		
		// получаем координаты начальной и конечной точек
		int x1 = line.getBegin().x;
		int y1 = line.getBegin().y;
		int x2 = line.getEnd().x;
		int y2 = line.getEnd().y;
		
		// определяем большую из проекций отрезка на координатные оси
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
		
		// определяем длины проекций отрезка на координатные оси 
		int dx = x2 - x1;
		int dy = Math.abs(y2 - y1);
		// определяем "ошибку"
		int error = - (dx >> 1);
		// определяем, в какую сторону будет расти координата y
		int ystep;
		if (y1 < y2) {
			// у будет возрастать
			ystep = 1;
		} else {
			// у будет убывать
			ystep = -1;
		}
		
		// устанавливаем начальное значение у
		int y = y1;
		// основной цикл
		for (int x = x1; x <= x2; x++) {
			// определяем октант
			if (steep) {
				area.plot(y, x, color);
			} else {
				area.plot(x, y, color);
			}
			// накапливаем ошибку
			error += dy;
			// если она стала больше предела
			if (error > 0) {
				// производим приращение координаты у
				y += ystep;
				// сбрасываем ошибку
				error -= dx;
			}
		}
	}
	
	public void drawDebug(Lab1 area, ModelLine line, int step) {
		// реализация алгоритма Брезенхама (отладочная версия)
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
		return "Алгоритм Брезенхама: (x1,y1) = (" + line.getBegin().x + ", " + line.getBegin().y +
		"), (x2,y2) = (" + line.getEnd().x + ", " + line.getEnd().y + "); текущие (x,y) = " 
		+ x + ", " + y + "), шаг №" + step + ".";
	}
	
}
