package giis.labs.lab3.model;

import java.awt.Point;

public class Interpolation {
	
	/**
	 * ������� ������.
	 */
	private static final double ermitMatrix[][] = {
			{	2,	-2,	1,	1	},
			{	-3,	3,	-2,	-1	},
			{	0,	0,	1,	0	},
			{	1,	0,	0,	0	}
	};
	
	/**
	 * ������� �����.
	 */
	private static final double bezierMatrix[][] = {
		{	-1,	3,	-3,	1	},
		{	3,	-6,	3,	0	},
		{	-3,	3,	0,	0	},
		{	1,	0,	0,	0	}
	};
	
	/**
	 * ������� �-�������
	 */
	private static final double bsplineMatrix[][] = {
		{	-1/6.0d,	3/6.0d,		-3/6.0d,	1/6.0d	},
		{	3/6.0d,		-6/6.0d,	3/6.0d,		0/6.0d	},
		{	-3/6.0d,	0/6.0d,		3/6.0d,		0/6.0d	},
		{	1/6.0d,		4/6.0d,		1/6.0d,		0/6.0d	}
	};
	
	/**
	 * ��������� ��������� ����� ����� ������.
	 * @param t �������� t
	 * @param p1 ������ �������� �����
	 * @param p4 ������ �������� �����
	 * @param r1 ������ ����������� � ������ �������� �����
	 * @param r4 ������ ����������� �� ������ �������� �����
	 * @return ����������� �����
	 */
	public static Point ermit(double t, Point p1, Point p4, Point r1, Point r4) {
		// ��������� ������� T
		double tMatrix[][] = makeTMatrix(t);
		// ��������� ������� ������� �����
		double pointsMatrix[][] = makeParamMatrix(p1,p4,r1,r4);
		
		// �������� ������� � �� ������� �����, ����� �� ������� ������� �����
		double point[][] = Matrices.multiple(Matrices.multiple(tMatrix, ermitMatrix),pointsMatrix);
		// ������� ���������
		return new Point((int)Math.round(point[0][0]),(int)Math.round(point[0][1]));
	}
	
	/**
	 * ��������� ��������� ����� ����� �����.
	 * @param t �������� t
	 * @param p1 ������ �������� �����
	 * @param p2 ������ �������� �����
	 * @param p3 ������ ������� �����
	 * @param p4 ������ ������� �����
	 * @return ����������� �����
	 */
	public static Point bezier(double t, Point p1, Point p2, Point p3, Point p4) {
		// ��������� ������� T		
		double tMatrix[][] = makeTMatrix(t);
		// ��������� ������� ������� �����		
		double pointsMatrix[][] = makeParamMatrix(p1,p2,p3,p4);
		
		// �������� ������� � �� ������� �����, ����� �� ������� ������� �����		
		double point[][] = Matrices.multiple(Matrices.multiple(tMatrix, bezierMatrix),pointsMatrix);
		// ������� ���������		
		return new Point((int)Math.round(point[0][0]),(int)Math.round(point[0][1]));
	}
	
	/**
	 * ��������� ��������� ����� �-�������.
	 * @param t �������� t
	 * @param p1 ���������� ����� ��������
	 * @param p2 ������ �������� ����� ��������
	 * @param p3 ������ �������� ����� ��������
	 * @param p4 �������� ����� ��������
	 * @return ����������� �����
	 */
	public static Point bspline(double t, Point pp, Point p1, Point p2, Point pn) {
		// ��������� ������� T		
		double tMatrix[][] = makeTMatrix(t);
		// ��������� ������� ������� �����		
		double pointsMatrix[][] = makeParamMatrix(pp,p1,p2,pn);
		
		// �������� ������� � �� ������� �����, ����� �� ������� ������� �����		
		double point[][] = Matrices.multiple(Matrices.multiple(tMatrix, bsplineMatrix),pointsMatrix);
		// ������� ���������		
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
