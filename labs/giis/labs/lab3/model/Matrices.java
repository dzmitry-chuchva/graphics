package giis.labs.lab3.model;

public class Matrices {
	
	public static double[][] initZeroMatrix(int n, int m) {
		double matrix[][] = new double[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				matrix[i][j] = 0.0;
			}
		}
		return matrix;
	}
	
	public static double[][] initIdentMatrix(int n, int m) {
		double matrix[][] = new double[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				matrix[i][j] = (i == j) ? 1.0 : 0.0;
			}
		}
		return matrix;
	}
	
	public static double[][] multiple(double a[][], double b[][]) {
		if (a[0].length != b.length) {
			throw new ArithmeticException("Matrices must be equivalent!");
		}
		
		int n = a.length;
		int m = b[0].length;
		double matrix[][] = new double[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				for (int k = 0; k < a[0].length; k++) {
					matrix[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return matrix;
	}

}
