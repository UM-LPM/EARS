package org.um.feri.analyse.correlationdependence;
import java.util.ArrayList;
import java.util.Random;

import org.um.feri.analyse.util.MersenneTwister;
import org.um.feri.ears.problems.*;

public class CorrelationAndDependence {
	// http://en.wikipedia.org/wiki/Correlation_and_dependence
	private static int randomseed = 9092011;
	private Problem problem;
	double r_xy; //correlation coefficient - Pearson correlation
	CRPoint[] all;
	int size;
	double ax; //average
	double ay; //average
	double min_x;
	double max_x;
	double min_y;
	double max_y;
	
	public static Random rnd = new MersenneTwister(randomseed );
	private static double minEuclidianDistance(double x_i[][], Double x_j[]) {

		double r=euclidianDistance(x_i[0], x_j);
		double t;
		for (int i=1; i<x_i.length; i++) {
			t=euclidianDistance(x_i[i], x_j);
			if (t<r) {r = t;}
		}
		return r;
	}
	private static double euclidianDistance(double x_i[], Double x_j[]) {
		double r=0;
		for (int i=0; i<x_i.length; i++) {
			r+=(x_i[i]-x_j[i])*(x_i[i]-x_j[i]);
		}
		r = Math.sqrt(r);
		//System.out.print(r+" ");
		return r;
	}
	
	public CorrelationAndDependence(Problem p, int size) {
		this.problem = p;
		this.size = size;
		ax=0;
		ay=0;
		min_x = Double.MAX_VALUE;
		max_x = Double.MIN_VALUE;
		min_y = Double.MAX_VALUE;
		max_y = Double.MIN_VALUE;
		all = new CRPoint[size];
		generateAndFillNewRandom();
	}
	
	public void generateAndFillNewRandom() {
		int dim = problem.getNumberOfDimensions();
		double best[][] = problem.getOptimalVector();
		double bestf = problem.getGlobalOptimum();
		Double ind[] = new Double[dim];
		ArrayList<Double> individual;
		double sx=0;
		double sy=0;
		double xi=0;
		double yi=0;
		min_x = Double.MAX_VALUE;
		max_x = Double.MIN_VALUE;
		min_y = Double.MAX_VALUE;
		max_y = Double.MIN_VALUE;
		for (int i=0; i<size; i++) {
			//create random
			for (int j = 0; j < dim; j++) {
				Double tmp;
				tmp = problem.lowerLimit.get(j) + rnd.nextDouble()
						* problem.upperLimit.get(j);
				ind[j]=tmp;
			}
			xi = minEuclidianDistance(best, ind);
			//individual = new ArrayList<Double>( Arrays.asList(ind) );
			yi = Math.abs(bestf-problem.eval(ind));
			sx+=xi;
			sy+=yi;
			if (xi<min_x) min_x=xi;
			if (xi>max_x) max_x=xi;
			if (yi<min_y) min_y=yi;
			if (yi>max_y) max_y=yi;
			all[i] = new CRPoint(xi,yi);
		}
		ax = sx/size;
		ay = sy/size;
		calcPearsonCorrelation();
	}
	
	/**
	 * Calcs and sets r_xy; //correlation coefficient - Pearson correlation
	 */
	private void calcPearsonCorrelation() {
		double sum_up = 0; //formula
		double sum_d1 = 0; //formula
		double sum_d2 = 0; //formula
		double tmp_x, tmp_y;
		for (int i=0; i<size; i++) {
			tmp_x = all[i].getX()-ax;
			tmp_y=all[i].getY()-ay;
			sum_up+=tmp_x*tmp_y;	
			sum_d1+=tmp_x*tmp_x;
			sum_d2+=tmp_y*tmp_y;
		}
		r_xy =  sum_up/Math.sqrt(sum_d1*sum_d2);
	}
	public String toString() {
		return problem.getName()+" (dim="+problem.getNumberOfDimensions()+") n="+size+" r_xy="+r_xy;
	}
	public String discriteGraph(int x, int y) {
		int polje[][] = new int[x+1][y+1];
		double dx = max_x/x;
		double dy = max_y/y;
		for (int i=0; i<size; i++) {
			polje[(int)(all[i].getX()/dx)][(int)(all[i].getY()/dy)]++;
		}
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<x; i++) {
			for (int j=0; j<y; j++) {
				//sb.append(Util.df3.format(x*dx)).append(" ").append(Util.df3.format(y*dy)).append(" ").append(polje[i][j]);
				if (polje[i][j]>0) {
					sb.append(i+1).append(" ").append(j+1).append(" 1 1 ").append(polje[i][j]);
				sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
	
	public int getGraphArea(int x, int y)
	{
		int area = 0;
		 
		int polje[][] = new int[x+1][y+1];
		double dx = max_x/x;
		double dy = max_y/y;
		for (int i=0; i<size; i++) {
			polje[(int)(all[i].getX()/dx)][(int)(all[i].getY()/dy)]++;
		}
		
		for (int i=0; i<x; i++) {
			for (int j=0; j<y; j++) {
				if (polje[i][j] != 0) {
					area++;
				}
			}
		}
		
		return area;
	}
}



