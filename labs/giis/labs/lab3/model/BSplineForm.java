package giis.labs.lab3.model;

import giis.labs.lab3.gui.Lab3;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;

public class BSplineForm extends BaseForm implements InterpolatedLine {
	
	private Point[] points;
		
	public BSplineForm(Point[] points) {
		this.points = new Point[points.length + 3];
		this.points = Arrays.copyOf(points, this.points.length);
		
		// make circle
		this.points[points.length] = points[0];
		this.points[points.length + 1] = points[1];
		this.points[points.length + 2] = points[2];
		setType(FormType.BSPLINE);
		setColor(Color.BLUE);
		setMarkerColor(new Color(getColor().getRed(),getColor().getGreen(),getColor().getBlue(),128));
	}

	public void draw(Lab3 area, boolean debug, int debugStep) {
		// јлгоритм генерации ¬-сплайна
		
		// вычисл€ем N начальных точек, где N - количество сегментов
		Point[] prevs = new Point[points.length];
		for (int segment = 0; segment < points.length - 3; segment++) {
			prevs[segment] = Interpolation.bspline(0.0, points[segment], points[segment + 1], points[segment + 2], points[segment + 3]);
			// и рисуем их
			area.plot(prevs[segment], color);
		}
		// провер€ем отладочную информацию
		if (debug && debugStep == 0) {
			setLastT(0.0);
			Point[] p = Arrays.copyOfRange(prevs, 0, prevs.length - 3); 
			setLastPoints(p);
			return;
		}
		// основной цикл по t
		int stepCounter = 0;
		for (double t = tStep; t <= 1.0; t += tStep, stepCounter++) {
			// производим вычисление следующей точки сразу на всех сегментах дл€ текщего t 
			for (int segment = 0; segment < points.length - 3; segment++) {
				Point p = Interpolation.bspline(t, points[segment], points[segment + 1], points[segment + 2], points[segment + 3]);
				// если очередна€ точка не сосед с предыдущей дл€ данного сегмента
				if (Math.abs(p.x - prevs[segment].x) > 1 || (Math.abs(p.y - prevs[segment].y) > 1)) {
					// соедин€ем их линией
					area.line(prevs[segment], p, color);
				} else {
					// иначе просто рисуем
					area.plot(p, color);
				}
				// сохран€ем в качестве предыдущей
				prevs[segment] = p;
			}
			// провер€ем отладочную информацию
			if (debug && debugStep == stepCounter) {
				setLastT(t);
				Point[] p = Arrays.copyOfRange(prevs, 0, prevs.length - 3); 
				setLastPoints(p);
				return;
			}
		}
		// если в отладочном режиме - завершить его
		if (debug) {
			area.stepDebugEnd();
		}
	}
	
	public void draw(Lab3 area) {
		draw(area,false,0);
	}

	public Point[] getPoints() {
		return points;
	}

	public void setPoints(Point[] points) {
		this.points = points;
	}
	
	public Point[] getChangeablePoints() {
		Point[] tmp = new Point[points.length - 3];
		tmp = Arrays.copyOfRange(points, 0, points.length - 3);
		return tmp;
	}
	
	public void setChangeablePoint(int index, Point value) {
		points[index] = value;
		if (index < 3) {
			points[points.length + index - 3] = value;
		}
	}

}
