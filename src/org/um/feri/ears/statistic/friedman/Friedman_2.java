package org.um.feri.ears.statistic.friedman;

import java.util.*;

public class Friedman_2 {
	
    
    // NEW VARIABLES (NIKI)
	
   public static Results[] statistics_results;
   public static PairStatistics[] statistics_pairs_p_values;
	
   public static Results[] getResults(){
	   return statistics_results;
   }
   
   public static double roundToDecimals(double d, int c)  
   {   
      int temp = (int)(d * Math.pow(10 , c));  
      return ((double)temp)/Math.pow(10 , c);  
   }
	
	public static void setStatistics(Vector datasets, Vector algoritmos, double[][] mean) {
		
		// Variables from SCI2s
		String file_data = "";
		StringTokenizer lineas, tokens;
		String linea, token;
		int i, j, k, m;
		int posicion;
		
		Pair orden[][];
		Pair rank[][];
		boolean encontrado;
		int ig;
		double sum;
		boolean visto[];
		Vector porVisitar;
		double Rj[];
		double friedman;
		double sumatoria=0;
		double termino1, termino2, termino3;
		double iman;
		double Qprima010[] = {0.0,1.645,1.960,2.128,2.242,2.327,2.394,2.450,2.498,2.540,2.576,2.609,2.639,2.666,2.690,2.713,2.735,2.755,2.733,2.791,2.807,2.823,2.838,2.852,2.866};
		double Qprima005[] = {0.0,1.960,2.242,2.394,2.498,2.576,2.639,2.690,2.735,2.773,2.807,2.838,2.866,2.891,2.914,2.936,2.955,2.974,2.992,3.008,3.024,3.038,3.052,3.066,3.078};
		double Qprima001[] = {0.0,2.576,2.807,2.936,3.024,3.091,3.144,3.189,3.227,3.261,3.291,3.317,3.342,3.364,3.384,3.403,3.421,3.437,3.453,3.467,3.481,3.494,3.506,3.518,3.529};
		double q010, q005, q001, CD010, CD005, CD001;
		boolean vistos[];
		int pos, tmp;
		double min;
		double maxVal;
		double rankingRef;
		double Pi[];
		double ALPHAiHolm[];
		double ALPHAiShaffer[];
		String ordenAlgoritmos[];
		double ordenRankings[];
		int order[];
		double adjustedP[][];
		double Ci[];
		double SE;
		boolean parar, otro;
		Vector index = new Vector();
		Vector exhaustiveI = new Vector();
		boolean[][] cuadro;
		double minPi, tmpPi, maxAPi,tmpAPi;
		Relation[] parejitas;
		int lineaN = 0;
		int columnaN = 0;
		Vector T;
		int Tarray[];
	

		// Round means
		/*
		for (i=0; i<datasets.size(); i++) {
	    	for (j=0; j<algoritmos.size(); j++){
	    		mean[i][j] = roundToDecimals(mean[i][j],6);
	    	}
	    }*/
		
	    /* We use the Pair structure to compute and order rankings */
	    orden = new Pair[datasets.size()][algoritmos.size()];
	   // System.out.println(datasets.size()+" "+algoritmos.size());
	    for (i=0; i<datasets.size(); i++) {
	    	for (j=0; j<algoritmos.size(); j++){
	    		orden[i][j] = new Pair (j,mean[i][j]);
	    	}
	    	Arrays.sort(orden[i],Collections.reverseOrder());
	    	//Arrays.sort(orden[i]);
	    }
	
	    /* Building of the rankings table per algorithms and data sets */
	    rank = new Pair[datasets.size()][algoritmos.size()];
	    posicion = 0;
	    for (i=0; i<datasets.size(); i++) {
	    	for (j=0; j<algoritmos.size(); j++){
	    		encontrado = false;
	    		for (k=0; k<algoritmos.size() && !encontrado; k++) {
	    			if (orden[i][k].index == j) {
	    				encontrado = true;
	    				posicion = k+1;
	    			}
	    		}
	    		rank[i][j] = new Pair(posicion,orden[i][posicion-1].value);
	    	}
	    }
	
	    /* In the case of having the same performance, the rankings are equal */
	    for (i=0; i<datasets.size(); i++) {
	    	visto = new boolean[algoritmos.size()];
	    	porVisitar= new Vector();

	    	Arrays.fill(visto,false);
	    	for (j=0; j<algoritmos.size(); j++) {
		    	porVisitar.removeAllElements();
	    		sum = rank[i][j].index;
	    		visto[j] = true;
	    		ig = 1;
	    		for (k=j+1;k<algoritmos.size();k++) {
	    			if (roundToDecimals(rank[i][j].value,6) == roundToDecimals(rank[i][k].value,6) && !visto[k]) {
	    				sum += rank[i][k].index;
	    				ig++;
	    				porVisitar.add(new Integer(k));
	    				visto[k] = true;
	    			}
	    		}
	    		sum /= (double)ig;
	    		rank[i][j].index = sum;
	    		for (k=0; k<porVisitar.size(); k++) {
	    			rank[i][((Integer)porVisitar.elementAt(k)).intValue()].index = sum;
	    		}
	    	}
	    }
	
	    /* Compute the average ranking for each algorithm */
	    Rj = new double[algoritmos.size()];
	    for (i=0; i<algoritmos.size(); i++){
	    	Rj[i] = 0;
	    	//System.out.print("\n" + algoritmos.elementAt(i).toString() + "\t ");
	    	for (j=0; j<datasets.size(); j++) {
	    		Rj[i] += rank[j][i].index;
	    		//System.out.print(rank[j][i].value + "(" + rank[j][i].index + ")\t ");
	    	}
	    	Rj[i] = Rj[i] / ((double)datasets.size());
	    	//System.out.println(Rj[i]);
	    }
	   // System.out.println();

	    /* Save the average ranking per algorithm */
	    statistics_results = new Results[algoritmos.size()];
	    
        for (i=0; i<algoritmos.size();i++){
        	statistics_results[i] = new Results();
        	statistics_results[i].setName(algoritmos.elementAt(i).toString());
        	statistics_results[i].setAverageRank(Rj[i]);
        }
        
	    /* Compute the Friedman statistic */
        
	    termino1 = (12*(double)datasets.size())/((double)algoritmos.size()*((double)algoritmos.size()+1));
	    termino2 = (double)algoritmos.size()*((double)algoritmos.size()+1)*((double)algoritmos.size()+1)/(4.0);
	    for (i=0; i<algoritmos.size();i++) {
	    	sumatoria += Rj[i]*Rj[i];
	    }
	    friedman = (sumatoria - termino2) * termino1;
	   
		double pFriedman, pIman;
        pFriedman = ChiSq(friedman, (algoritmos.size()-1));

        
	    /*Compute the Iman-Davenport statistic*/
	    iman = ((datasets.size()-1)*friedman)/(datasets.size()*(algoritmos.size()-1) - friedman);
		pIman = FishF(iman, (algoritmos.size()-1),(algoritmos.size()-1) * (datasets.size() - 1));

	    termino3 = Math.sqrt((double)algoritmos.size()*((double)algoritmos.size()+1)/(6.0*(double)datasets.size()));
	    
	    
	    /******************************************/
	    /******************************************/
	    /************ NxN COMPARISON **************/	    
	    /******************************************/
	    /******************************************/

		/*Compute the unadjusted p_i value for each comparison alpha=0.05*/	    
	    Pi = new double[(int)combinatoria(2,algoritmos.size())];
	    ALPHAiHolm = new double[(int)combinatoria(2,algoritmos.size())];
	    ALPHAiShaffer = new double[(int)combinatoria(2,algoritmos.size())];
	    ordenAlgoritmos = new String[(int)combinatoria(2,algoritmos.size())];
	    ordenRankings = new double[(int)combinatoria(2,algoritmos.size())];
	    order = new int[(int)combinatoria(2,algoritmos.size())];
	    parejitas = new Relation[(int)combinatoria(2,algoritmos.size())];
	    T = new Vector();
	    T = trueHShaffer(algoritmos.size());
	    Tarray = new int[T.size()];
	    for (i=0; i<T.size(); i++) {
	    	Tarray[i] = ((Integer)T.elementAt(i)).intValue();
	    }
	    Arrays.sort(Tarray);

	    SE = termino3;
	    vistos = new boolean[(int)combinatoria(2,algoritmos.size())];
	    for (i=0, k=0; i<algoritmos.size();i++) {
	    	for (j=i+1; j<algoritmos.size();j++,k++) {
	    		ordenRankings[k] = Math.abs(Rj[i] -Rj[j]);
	    		ordenAlgoritmos[k] = (String)algoritmos.elementAt(i) + " vs. " + (String)algoritmos.elementAt(j);
	    		parejitas[k] = new Relation(i,j);
	    	}
	    }
	    
	    Arrays.fill(vistos,false);
	    for (i=0; i<ordenRankings.length; i++) {
	    	for (j=0;vistos[j]==true;j++);
	    	pos = j;
	    	maxVal = ordenRankings[j];
	    	for (j=j+1;j<ordenRankings.length;j++) {
	    		if (vistos[j] == false && ordenRankings[j] > maxVal) {
	    			pos = j;
	    			maxVal = ordenRankings[j];
	    		}
	    	}
	    	vistos[pos] = true;
	    	order[i] = pos;
	    }
	    
	    /*Computing the logically related hypotheses tests (Shaffer and Bergmann-Hommel)*/
	    pos = 0;
	    tmp = Tarray.length-1;
	    for (i=0; i<order.length; i++) {
	    	Pi[i] = 2*CDF_Normal.normp((-1)*Math.abs((ordenRankings[order[i]])/SE));
	    	ALPHAiHolm[i] = 0.05/((double)order.length-(double)i);
	    	ALPHAiShaffer[i] = 0.05/((double)order.length-(double)Math.max(pos,i));
	    	if (i == pos && Pi[i] <= ALPHAiShaffer[i]) {
	    		tmp--;
	    		pos = (int)combinatoria(2,algoritmos.size()) - Tarray[tmp];
	    	}
	    }
        
	  
		/*For Bergmann-Hommel's procedure, 9 algorithms could suppose intense computation*/
	    if (algoritmos.size() < 9) {
		    for (i=0; i<algoritmos.size(); i++) {
		    	index.add(new Integer(i));
		    }	    	
	        exhaustiveI = obtainExhaustive(index);
	        cuadro = new boolean[algoritmos.size()][algoritmos.size()];
	        for (i=0; i<algoritmos.size(); i++) {
	        	Arrays.fill(cuadro[i], false);
	        }
	        for (i=0; i<exhaustiveI.size(); i++) {	
        		minPi = 2*CDF_Normal.normp((-1)*Math.abs(Rj[((Relation)((Vector)exhaustiveI.elementAt(i)).elementAt(0)).i] - Rj[((Relation)((Vector)exhaustiveI.elementAt(i)).elementAt(0)).j])/SE);
	        	for (j=1; j<((Vector)exhaustiveI.elementAt(i)).size(); j++) {
	        		tmpPi = 2*CDF_Normal.normp((-1)*Math.abs(Rj[((Relation)((Vector)exhaustiveI.elementAt(i)).elementAt(j)).i] - Rj[((Relation)((Vector)exhaustiveI.elementAt(i)).elementAt(j)).j])/SE);
	        		if (tmpPi < minPi) {
	        			minPi = tmpPi;
	        		}
	        	}
	        	if (minPi > (0.05/((double)((Vector)exhaustiveI.elementAt(i)).size()))) {	        		
		        	for (j=0; j<((Vector)exhaustiveI.elementAt(i)).size(); j++) {
		        		cuadro[((Relation)((Vector)exhaustiveI.elementAt(i)).elementAt(j)).i][((Relation)((Vector)exhaustiveI.elementAt(i)).elementAt(j)).j] = true;
		        	}	        		
	        	}
	        }
	    }

	    
		/*Compute the unadjusted p_i value for each comparison alpha=0.10*/	    
	    Pi = new double[(int)combinatoria(2,algoritmos.size())];
	    ALPHAiHolm = new double[(int)combinatoria(2,algoritmos.size())];
	    ALPHAiShaffer = new double[(int)combinatoria(2,algoritmos.size())];
	    ordenAlgoritmos = new String[(int)combinatoria(2,algoritmos.size())];
	    ordenRankings = new double[(int)combinatoria(2,algoritmos.size())];
	    order = new int[(int)combinatoria(2,algoritmos.size())];

	    SE = termino3;
	    vistos = new boolean[(int)combinatoria(2,algoritmos.size())];
	    for (i=0, k=0; i<algoritmos.size();i++) {
	    	for (j=i+1; j<algoritmos.size();j++,k++) {
	    		ordenRankings[k] = Math.abs(Rj[i] -Rj[j]);
	    		ordenAlgoritmos[k] = (String)algoritmos.elementAt(i) + " vs. " + (String)algoritmos.elementAt(j);
	    	}
	    }
	    
	    Arrays.fill(vistos,false);
	    for (i=0; i<ordenRankings.length; i++) {
	    	for (j=0;vistos[j]==true;j++);
	    	pos = j;
	    	maxVal = ordenRankings[j];
	    	for (j=j+1;j<ordenRankings.length;j++) {
	    		if (vistos[j] == false && ordenRankings[j] > maxVal) {
	    			pos = j;
	    			maxVal = ordenRankings[j];
	    		}
	    	}
	    	vistos[pos] = true;
	    	order[i] = pos;
	    }
	    
	    /*Computing the logically related hypotheses tests (Shaffer and Bergmann-Hommel)*/
	    pos = 0;	    
	    tmp = Tarray.length-1;
	    for (i=0; i<order.length; i++) {
	    	Pi[i] = 2*CDF_Normal.normp((-1)*Math.abs((ordenRankings[order[i]])/SE));
	    	ALPHAiHolm[i] = 0.1/((double)order.length-(double)i);
	    	ALPHAiShaffer[i] = 0.1/((double)order.length-(double)Math.max(pos,i));
	    	if (i == pos && Pi[i] <= ALPHAiShaffer[i]) {
	    		tmp--;
	    		pos = (int)combinatoria(2,algoritmos.size()) - Tarray[tmp];
	    	}
	    }
	    

	    /************ ADJUSTED P-VALUES NxN COMPARISON **************/	
	    
	    
	    statistics_pairs_p_values = new PairStatistics[Pi.length];
	    
	    adjustedP = new double[Pi.length][4];
	    pos = 0;
	    tmp = Tarray.length-1;
	    for (i=0; i<adjustedP.length; i++) {
	    	adjustedP[i][0] = Pi[i] * (double)(adjustedP.length);
	    	adjustedP[i][1] = Pi[i] * (double)(adjustedP.length-i);
	    	adjustedP[i][2] = Pi[i] * ((double)adjustedP.length-(double)Math.max(pos,i));
	    	if (i == pos) {
	    		tmp--;
	    		pos = (int)combinatoria(2,algoritmos.size()) - Tarray[tmp];
	    	}
	    	if (algoritmos.size() < 9) {
	    		maxAPi = Double.MIN_VALUE;
	    		minPi = Double.MAX_VALUE;
		        for (j=0; j<exhaustiveI.size(); j++) {
		        	if (exhaustiveI.elementAt(j).toString().contains(parejitas[order[i]].toString())) {
		        		minPi = 2*CDF_Normal.normp((-1)*Math.abs(Rj[((Relation)((Vector)exhaustiveI.elementAt(j)).elementAt(0)).i] - Rj[((Relation)((Vector)exhaustiveI.elementAt(j)).elementAt(0)).j])/SE);
			        	for (k=1; k<((Vector)exhaustiveI.elementAt(j)).size(); k++) {
			        		tmpPi = 2*CDF_Normal.normp((-1)*Math.abs(Rj[((Relation)((Vector)exhaustiveI.elementAt(j)).elementAt(k)).i] - Rj[((Relation)((Vector)exhaustiveI.elementAt(j)).elementAt(k)).j])/SE);
			        		if (tmpPi < minPi) {
			        			minPi = tmpPi;
			        		}
			        	}		        		
			        	tmpAPi = minPi * (double)(((Vector)exhaustiveI.elementAt(j)).size());
			        	if (tmpAPi > maxAPi) {
			        		maxAPi = tmpAPi;
			        	}			     
		        	}
		        }	    		
		    	adjustedP[i][3] = maxAPi;
	    	}
	    }
	    
	    for (i=1; i<adjustedP.length; i++) {
	    	if (adjustedP[i][1] < adjustedP[i-1][1])
	    		adjustedP[i][1] = adjustedP[i-1][1];
	    	if (adjustedP[i][2] < adjustedP[i-1][2])
	    		adjustedP[i][2] = adjustedP[i-1][2];
	    	if (adjustedP[i][3] < adjustedP[i-1][3])
	    		adjustedP[i][3] = adjustedP[i-1][3];
	    }
	    
	    /* Set p-values for statistics pairs */
	    
	    for (i=0; i<Pi.length; i++) {
	    	statistics_pairs_p_values[i] = new PairStatistics((String)algoritmos.elementAt(parejitas[order[i]].i),(String)algoritmos.elementAt(parejitas[order[i]].j));
	    	statistics_pairs_p_values[i].setNeme(adjustedP[i][0]);
	    	statistics_pairs_p_values[i].setHolm(adjustedP[i][1]);
	    	statistics_pairs_p_values[i].setShaf(adjustedP[i][2]);
	    	/*out.println(statistics_pairs_p_values[i].first + " vs. " + statistics_pairs_p_values[i].second);
	    	out.println(statistics_pairs_p_values[i].pNeme + " " +
	    			statistics_pairs_p_values[i].pHolm + " " +
	    			statistics_pairs_p_values[i].pShaf + " " +
	    			statistics_pairs_p_values[i].pBerg
	    			);*/
	    }
	}
	
	public static Vector<PairStatistics> getStatisticsResults(){
		Vector<PairStatistics> result = new Vector<PairStatistics>();
		
		for (int i=0;i<statistics_pairs_p_values.length;i++){
			result.add(statistics_pairs_p_values[i]);
		}
		return result;
	}
	
	public static Vector<PairStatistics> getSignificantlyDifferentPairs(double alpha, String test){
		Vector<PairStatistics> result = new Vector<PairStatistics>();
		
		for (int i=0;i<statistics_pairs_p_values.length;i++){
			if (statistics_pairs_p_values[i].getNeme(alpha) == 1 && test.equals("Nemenyi"))
				result.add(statistics_pairs_p_values[i]);
			else if(statistics_pairs_p_values[i].getHolm(alpha) == 1 && test.equals("Holm"))
				result.add(statistics_pairs_p_values[i]);
			else if(statistics_pairs_p_values[i].getShaf(alpha) == 1 && test.equals("Shaffer"))
				result.add(statistics_pairs_p_values[i]);
		}
		return result;
	}

	public static double checkSignificance(String firstAlg, String secondAlg, double alpha, String test){
		double result = -1;
		for (int i=0;i<statistics_pairs_p_values.length;i++){
			if ((firstAlg.equals(statistics_pairs_p_values[i].first) && secondAlg.equals(statistics_pairs_p_values[i].second))
			|| (secondAlg.equals(statistics_pairs_p_values[i].first) && firstAlg.equals(statistics_pairs_p_values[i].second))){
				if(test.equals("Nemenyi")){
					result = statistics_pairs_p_values[i].getNeme(alpha);
				}else if(test.equals("Holm")){
					result = statistics_pairs_p_values[i].getHolm(alpha);
				}else if(test.equals("Shaffer")){
					result = statistics_pairs_p_values[i].getShaf(alpha);
				}
			}
		}
		return result;
	}
	
	public static Vector<PairStatistics> checkSignificance(String algorithm){
		Vector<PairStatistics> result = new Vector<PairStatistics>();
		
		for (int i=0;i<statistics_pairs_p_values.length;i++){
			if (algorithm.equals(statistics_pairs_p_values[i].first) || algorithm.equals(statistics_pairs_p_values[i].second))
				result.add(statistics_pairs_p_values[i]);
		}
		return result;
	}

	public static String getDiffers(String algorithm, double alpha, String test){
		String differ = "";
		
		for (int i=0;i<statistics_pairs_p_values.length;i++){
			if (algorithm.equals(statistics_pairs_p_values[i].first)){
				if (statistics_pairs_p_values[i].pNeme<alpha && test=="Nemenyi"){
					differ = differ + statistics_pairs_p_values[i].second + ", ";
				}else if (statistics_pairs_p_values[i].pHolm<alpha && test=="Holm"){
					differ = differ + statistics_pairs_p_values[i].second + ", ";
				}else if (statistics_pairs_p_values[i].pShaf<alpha && test=="Shaffer"){
					differ = differ + statistics_pairs_p_values[i].second + ", ";
				}
			}else if(algorithm.equals(statistics_pairs_p_values[i].second)){
				if (statistics_pairs_p_values[i].pNeme<alpha && test=="Nemenyi"){
					differ = differ + statistics_pairs_p_values[i].first + ", ";
				}else if (statistics_pairs_p_values[i].pHolm<alpha && test=="Holm"){
					differ = differ + statistics_pairs_p_values[i].first + ", ";
				}else if (statistics_pairs_p_values[i].pShaf<alpha && test=="Shaffer"){
					differ = differ + statistics_pairs_p_values[i].first + ", ";
				}
			}
		}
		
		return differ;
	}
	
	public static double combinatoria(int m, int n) {
		double result = 1;
		int i;
		
		if (n >= m) {
			for (i=1; i<=m; i++)
				result *= (double)(n-m+i)/(double)i;
		} else {
			result = 0;
		}
		return result;
	}
		
	public static Vector obtainExhaustive (Vector index) {
		
		Vector result = new Vector();
		int i,j,k;
		String binario;
		boolean[] number = new boolean[index.size()];
		Vector ind1, ind2;
		Vector set = new Vector();
		Vector res1, res2;
		Vector temp;
		Vector temp2;
		Vector temp3;

		ind1 = new Vector();
		ind2 = new Vector();
		temp = new Vector();
		temp2 = new Vector();
		temp3 = new Vector();
		
		for (i=0; i<index.size();i++) {
			for (j=i+1; j<index.size();j++) {
				set.addElement(new Relation(((Integer)index.elementAt(i)).intValue(),((Integer)index.elementAt(j)).intValue()));
			}
		}
		if (set.size()>0)
			result.addElement(set);
		
		for (i=1; i<(int)(Math.pow(2, index.size()-1)); i++) {
			Arrays.fill(number, false);
			ind1.removeAllElements();
			ind2.removeAllElements();
			temp.removeAllElements();
			temp2.removeAllElements();
			temp3.removeAllElements();
			binario = Integer.toString(i, 2);
			for (k=0; k<number.length-binario.length();k++) {
				number[k] = false;
			}
			for (j=0; j<binario.length();j++,k++) {
				if (binario.charAt(j) == '1')
					number[k] = true;
			}
			for (j=0; j<number.length; j++) {
				if (number[j] == true) {
					ind1.addElement(new Integer(((Integer)index.elementAt(j)).intValue()));					
				} else {					
					ind2.addElement(new Integer(((Integer)index.elementAt(j)).intValue()));					
				}
			}
			res1 = obtainExhaustive (ind1);
			res2 = obtainExhaustive (ind2);
			for (j=0; j<res1.size();j++) {
				result.addElement(new Vector((Vector)res1.elementAt(j)));
			}
			for (j=0; j<res2.size();j++) {
				result.addElement(new Vector((Vector)res2.elementAt(j)));
			}
			for (j=0; j<res1.size();j++) {
				temp = (Vector)((Vector)res1.elementAt(j)).clone();
				for (k=0; k<res2.size();k++) {
					temp2 = (Vector)temp.clone();
					temp3 = (Vector)((Vector)res2.elementAt(k)).clone();
					if (((Relation)temp2.elementAt(0)).i < ((Relation)temp3.elementAt(0)).i) {
						temp2.addAll((Vector)temp3);					
						result.addElement(new Vector(temp2));						
					} else {
						temp3.addAll((Vector)temp2);					
						result.addElement(new Vector(temp3));					
						
					}
				}
			} 
		}
		for (i=0;i<result.size();i++) {
			if (((Vector)result.elementAt(i)).toString().equalsIgnoreCase("[]")) {
				result.removeElementAt(i);
				i--;
			}
		}
		for (i=0;i<result.size();i++) {
			for (j=i+1; j<result.size(); j++) {	
				if (((Vector)result.elementAt(i)).toString().equalsIgnoreCase(((Vector)result.elementAt(j)).toString())) {
					result.removeElementAt(j);
					j--;
				}
			}
		}
		return result;		
	}
	
	public static Vector trueHShaffer (int k) {
		
		Vector number;
		int j;
		Vector tmp, tmp2;
		int p;
		
		number = new Vector();
		tmp = new Vector();
		if (k <= 1) {			
			number.addElement(new Integer(0));	
		} else {
			for (j=1; j<=k; j++) {
				tmp = trueHShaffer (k-j);
				tmp2 = new Vector();
				for (p=0; p<tmp.size(); p++) {
					tmp2.addElement(((Integer)(tmp.elementAt(p))).intValue() + (int)combinatoria(2,j));
				}
				number = unionVectores (number,tmp2);
			}
		}
		
		return number;
	}
		
	public static Vector unionVectores (Vector a, Vector b) {

		int i;
		
		for (i=0; i<b.size(); i++) {
			if (a.contains(new Integer((Integer)(b.elementAt(i)))) == false) {
				a.addElement(b.elementAt(i));
			}			
		}
		
		return a;		
	}
		
	private static double ChiSq(double x, int n) {
        if (n == 1 & x > 1000) {
            return 0;
        }
        if (x > 1000 | n > 1000) {
            double q = ChiSq((x - n) * (x - n) / (2 * n), 1) / 2;
            if (x > n) {
                return q;
            }
            {
                return 1 - q;
            }
        }
        double p = Math.exp( -0.5 * x);
        if ((n % 2) == 1) {
            p = p * Math.sqrt(2 * x / Math.PI);
        }
        double k = n;
        while (k >= 2) {
            p = p * x / k;
            k = k - 2;
        }
        double t = p;
        double a = n;
        while (t > 0.0000000001 * p) {
            a = a + 2;
            t = t * x / a;
            p = p + t;
        }
        return 1 - p;
    }
		
	private static double FishF(double f, int n1, int n2) {
        double x = n2 / (n1 * f + n2);
        if ((n1 % 2) == 0) {
            return StatCom(1 - x, n2, n1 + n2 - 4, n2 - 2) * Math.pow(x, n2 / 2.0);
        }
        if ((n2 % 2) == 0) {
            return 1 -
                    StatCom(x, n1, n1 + n2 - 4, n1 - 2) *
                    Math.pow(1 - x, n1 / 2.0);
        }
        double th = Math.atan(Math.sqrt(n1 * f / (1.0*n2)));
        double a = th / (Math.PI / 2.0);
        double sth = Math.sin(th);
        double cth = Math.cos(th);
        if (n2 > 1) {
            a = a +
                sth * cth * StatCom(cth * cth, 2, n2 - 3, -1) / (Math.PI / 2.0);
        }
        if (n1 == 1) {
            return 1 - a;
        }
        double c = 4 * StatCom(sth * sth, n2 + 1, n1 + n2 - 4, n2 - 2) * sth *
                   Math.pow(cth, n2) / Math.PI;
        if (n2 == 1) {
            return 1 - a + c / 2.0;
        }
        int k = 2;
        while (k <= (n2 - 1) / 2.0) {
            c = c * k / (k - .5);
            k = k + 1;
        }
        return 1 - a + c;
    }
		
	private static double StatCom(double q, int i, int j, int b) {
        double zz = 1;
        double z = zz;
        int k = i;
        while (k <= j) {
            zz = zz * q * k / (k - b);
            z = z + zz;
            k = k + 2;
        }
        return z;
    }

}
