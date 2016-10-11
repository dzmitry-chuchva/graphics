package giis.labs.lab3.model;

import giis.labs.lab3.gui.Lab3;

import java.awt.Color;
import java.awt.Point;

public class BezierLine extends BaseForm implements InterpolatedLine {
	
	private Point p1, p2, p3, p4;
		
	public BezierLine(Point p1, Point p2, Point p3, Point p4) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		setType(FormType.BEZIER);
		setColor(Color.GREEN);
		setMarkerColor(new Color(getColor().getRed(),getColor().getGreen(),getColor().getBlue(),128));
	}

	public void draw(Lab3 area) {
		draw(area,false,0);				
	}
	
	public void draw(Lab3 area, boolean debug, int debugStep) {
		// јлгоритм генерации формы Ѕезье
		
		// вычисл€ем первую точку
		Point pstart = Interpolation.bezier(0.0, p1, p2, p3, p4);
		// рисуем ее		
		area.plot(pstart, color);
		// провер€ем отладочную информацию		
		if (debug && debugStep == 0) {
			setLastT(0.0);
			setLastPoints(new Point[] { pstart });
			return;
		}
		// основной цикл (по параметру t)		
		int stepCounter = 0;
		for (double t = tStep; t <= 1.0; t += tStep, stepCounter++) {
			// вычисл€ем очередную точку
			Point p = Interpolation.bezier(t, p1, p2, p3, p4);
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

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public Point getP3() {
		return p3;
	}

	public void setP3(Point p3) {
		this.p3 = p3;
	}

	public Point getP4() {
		return p4;
	}

	public void setP4(Point p4) {
		this.p4 = p4;
	}
	
	public Point[] getChangeablePoints() {
		return new Point[] { p1, p2, p3, p4 };
	}
	
	public void setChangeablePoint(int index, Point value) {
		switch (index) {
		case 0:
			p1 = value;
			break;
		case 1:
			p2 = value;
			break;
		case 2:
			p3 = value;
			break;
		case 3:
			p4 = value;
			break;
		}
	}
}
