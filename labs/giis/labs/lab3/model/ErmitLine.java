package giis.labs.lab3.model;

import giis.labs.lab3.gui.Lab3;

import java.awt.Color;
import java.awt.Point;

public class ErmitLine extends BaseForm implements InterpolatedLine {
	
	private Point start;
	private Point end;
	private Point startDirection;
	private Point endDirection;
	
	public ErmitLine(Point start, Point end, Point startDirection, Point endDirection) {
		this.start = start;
		this.end = end;
		this.startDirection = new Point();
		this.startDirection.x = startDirection.x - start.x;
		this.startDirection.y = startDirection.y - start.y;
		this.endDirection = new Point();
		this.endDirection.x = - endDirection.x + end.x;
		this.endDirection.y = - endDirection.y + end.y;
		setType(FormType.ERMIT);
		setColor(Color.RED);
		setMarkerColor(new Color(getColor().getRed(),getColor().getGreen(),getColor().getBlue(),128));
	}

	public void draw(Lab3 area, boolean debug, int debugStep) {
		// јлгоритм генерации формы Ёрмита
		
		// вычисл€ем первую точку
		Point pstart = Interpolation.ermit(0.0, start, end, startDirection, endDirection);
		// рисуем ее
		area.plot(pstart, color);
		// провер€ем отладочную информацию
		if (debug && debugStep == 0) {
			setLastT(0.0);
			setLastPoints(new Point[] { pstart });
			return;
		}
		// основной цикл (по параметру t)
		int stepCounter = 1;
		for (double t = tStep; t <= 1.0; t += tStep, stepCounter++) {
			// вычисл€ем очередную точку
			Point p = Interpolation.ermit(t, start, end, startDirection, endDirection);
			// если она не сосед с предыдущей
			if (Math.abs(p.x - pstart.x) > 1 || (Math.abs(p.y - pstart.y) > 1)) {
				// соедин€ем вычисленную и предыдущую точку линией 
				area.line(pstart, p, color);
			} else {
				// иначе просто рисуем вычисленную точку
				area.plot(p, color);
			}
			// сохран€ем в качестве предыдущей
			pstart = p;
			// провер€ем отладочную информацию
			if (debug && debugStep == stepCounter) {
				setLastT(t);
				setLastPoints(new Point[] { pstart });
				return;
			}
		}
		// если в режиме отладки - закончить его
		if (debug) {
			area.stepDebugEnd();
		}
	}
	
	public void draw(Lab3 area) {
		draw(area,false,0);
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	public Point getEndDirection() {
		return endDirection;
	}

	public void setEndDirection(Point endDirection) {
		this.endDirection = endDirection;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getStartDirection() {
		return startDirection;
	}

	public void setStartDirection(Point startDirection) {
		this.startDirection = startDirection;
	}
	
	public Point[] getChangeablePoints() {
		Point p3 = new Point(start.x + startDirection.x, start.y + startDirection.y);
		Point p4 = new Point(end.x - endDirection.x, end.y - endDirection.y);
		return new Point[] { start, end, p3, p4 };
	}
	
	public void setChangeablePoint(int index, Point value) {
		switch (index) {
		case 0:
			start = value;
			break;
		case 1:
			end = value;
			break;
		case 2:
			startDirection.x = value.x - start.x;
			startDirection.y = value.y - start.y;
			break;
		case 3:
			endDirection.x = - value.x + end.x;
			endDirection.y = - value.y + end.y;
			break;
		}
	}
	
}
