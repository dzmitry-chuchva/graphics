package giis.labs.lab4.model;

public class Edge implements Comparable {
	
	private double x;
	private int dy;
	private double dx;
	
	public int compareTo(Object arg0) {
		Edge e = (Edge)arg0;
		return new Double(x).compareTo(new Double(e.getX()));
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public String toString() {
		return "(" + x + " " + dx + " " + dy + ")";		
	}

}
