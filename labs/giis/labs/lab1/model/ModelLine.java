package giis.labs.lab1.model;

import java.awt.Point;

public class ModelLine {
	private Point begin, end;
	private LineAlgorithm algo;
		
	public ModelLine(Point begin, Point end, LineAlgorithm algo) {
		this.begin = begin;
		this.end = end;
		this.algo = algo;
	}

	public LineAlgorithm getAlgo() {
		return algo;
	}

	public void setAlgo(LineAlgorithm algo) {
		this.algo = algo;
	}

	public Point getBegin() {
		return begin;
	}

	public void setBegin(Point begin) {
		this.begin = begin;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}
	
}
