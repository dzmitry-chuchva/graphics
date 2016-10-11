package giis.labs.lab6.model;

public class Visibleness {
	
	private double tStart = 0.0;
	private double tEnd = 1.0;
	private Segment segment;
	
	public Visibleness(Segment segment) {
		this.segment = segment;
	}
	
	public boolean isVisible() {
		return (tStart <= tEnd && tStart >= 0.0 && tEnd <= 1.0);
	}
	
	public void makeInvisible() {
		tEnd = 0.0;
		tStart = 1.0;
	}

	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}

	public double getTEnd() {
		return tEnd;
	}

	public void setTEnd(double end) {
		tEnd = end;
	}

	public double getTStart() {
		return tStart;
	}

	public void setTStart(double start) {
		tStart = start;
	}
	
	

}
