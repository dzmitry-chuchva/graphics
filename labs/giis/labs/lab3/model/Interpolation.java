package giis.labs.lab3.model;

import java.awt.Point;

public class Interpolation {
	
	/**
	 * ћатрица Ёрмита.
	 */
	private static final double ermitMatrix[][] = {
			{	2,	-2,	1,	1	},
			{	-3,	3,	-2,	-1	},
			{	0,	0,	1,	0	},
			{	1,	0,	0,	0	}
	};
	
	/**
	 * ћатрица Ѕезье.
	 */
	private static final double bezierMatrix[][] = {
		{	-1,	3,	-3,	1	},
		{	3,	-6,	3,	0	},
		{	-3,	3,	0,	0	},
		{	1,	0,	0,	0	}
	};
	
	/**
	 * ћатрица ¬-сплайна
	 */
	private static final double bsplineMatrix[][] = {
		{	-1/6.0d,	3/6.0d,		-3/6.0d,	1/6.0d	},
		{	3/6.0d,		-6/6.0d,	3/6.0d,		0/6.0d	},
		{	-3/6.0d,	0/6.0d,		3/6.0d,		0/6.0d	},
		{	1/6.0d,		4/6.0d,		1/6.0d,		0/6.0d	}
	};
	
	/**
	 * ¬ычисл€ет очередную точку формы Ёрмита.
	 * @param t параметр t
	 * @param p1 перва€ концева€ точка
	 * @param p4 втора€ концева€ точка
	 * @param r1 вектор касательной в первой концевой точке
	 * @param r4 вектор касательной во второй концевой точке
	 * @return вычисленна€ точка
	 */
	public static Point ermit(double t, Point p1, Point p4, Point r1, Point r4) {
		// построить матрицу T
		double tMatrix[][] = makeTMatrix(t);
		// построить матрицу опорных точек
		double pointsMatrix[][] = makeParamMatrix(p1,p4,r1,r4);
		
		// умножить матрицу “ на матрицу формы, затем на матрицу опорных точек
		double point[][] = Matrices.multiple(Matrices.multiple(tMatrix, ermitMatrix),pointsMatrix);
		// вернуть результат
		return new Point((int)Math.round(point[0][0]),(int)Math.round(point[0][1]));
	}
	
	/**
	 * ¬ычисл€ет очередную точку формы Ѕезье.
	 * @param t параметр t
	 * @param p1 перва€ концева€ точка
	 * @param p2 втора€ концева€ точка
	 * @param p3 перва€ опорна€ точка
	 * @param p4 втора€ опорна€ точка
	 * @return вычисленна€ точка
	 */
	public static Point bezier(double t, Point p1, Point p2, Point p3, Point p4) {
		// построить матрицу T		
		double tMatrix[][] = makeTMatrix(t);
		// построить матрицу опорных точек		
		double pointsMatrix[][] = makeParamMatrix(p1,p2,p3,p4);
		
		// умножить матрицу “ на матрицу формы, затем на матрицу опорных точек		
		double point[][] = Matrices.multiple(Matrices.multiple(tMatrix, bezierMatrix),pointsMatrix);
		// вернуть результат		
		return new Point((int)Math.round(point[0][0]),(int)Math.round(point[0][1]));
	}
	
	/**
	 * ¬ычисл€ет очередную точку ¬-сплайна.
	 * @param t параметр t
	 * @param p1 предыдуща€ точка сегмента
	 * @param p2 перва€ концева€ точка сегмента
	 * @param p3 втора€ концева€ точка сегмента
	 * @param p4 следуща€ точка сегмента
	 * @return вычисленна€ точка
	 */
	public static Point bspline(double t, Point pp, Point p1, Point p2, Point pn) {
		// построить матрицу T		
		double tMatrix[][] = makeTMatrix(t);
		// построить матрицу опорных точек		
		double pointsMatrix[][] = makeParamMatrix(pp,p1,p2,pn);
		
		// умножить матрицу “ на матрицу формы, затем на матрицу опорных точек		
		double point[][] = Matrices.multiple(Matrices.multiple(tMatrix, bsplineMatrix),pointsMatrix);
		// вернуть результат		
		return new Point((int)Math.round(point[0][0]),(int)Math.round(point[0][1]));
	}
	
	private static double[][] makeTMatrix(double t) {
		double tMatrix[][] = Matrices.initZeroMatrix(1, 4);
		tMatrix[0][3] = 1.0;
		tMatrix[0][2] = tMatrix[0][3] * t;
		tMatrix[0][1] = tMatrix[0][2] * t;
		tMatrix[0][0] = tMatrix[0][1] * t;
		return tMatrix;
	}
	
	private static double[][] makeParamMatrix(Point p1, Point p2, Point p3, Point p4) {
		double pointsMatrix[][] = Matrices.initZeroMatrix(4, 2);
		try {
		pointsMatrix[0][0] = p1.x;
		pointsMatrix[0][1] = p1.y;
		
		pointsMatrix[1][0] = p2.x;
		pointsMatrix[1][1] = p2.y;
		
		pointsMatrix[2][0] = p3.x;
		pointsMatrix[2][1] = p3.y;
		
		pointsMatrix[3][0] = p4.x;
		pointsMatrix[3][1] = p4.y;
		} catch (NullPointerException e) {
			System.out.println("NullPointerException");
		}
		return pointsMatrix;
	}

}
