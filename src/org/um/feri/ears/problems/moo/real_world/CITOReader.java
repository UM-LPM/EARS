package org.um.feri.ears.problems.moo.real_world;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CITOReader {

	private int numberOfUnits;
    private ArrayList<Integer> aspects;
    private int[][] constraintMatrix;
    private int[][] dependencyMatrix;
    private int[][] attributeCouplingMatrix;
    private int[][] methodCouplingMatrix;
    private int[][] methodReturnTypeMatrix;
    private int[][] methodParamTypeMatrix;
    private String softwareName;

	public CITOReader(String softwareName) {
    	
    	this.softwareName = softwareName;
    	String filePath = "problems/" + softwareName + ".txt";
        try {
            readProblem(filePath);
        } catch (IOException ex) {
            Logger.getLogger(CITOReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void readProblem(String filePath) throws IOException {
        Reader inputFile = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filePath)));
        StreamTokenizer token = new StreamTokenizer(inputFile);
        int lineNumber;

        try {

            // Find the string DIMENSION ---------------------------------------
            while (true) {
                token.nextToken();
                if ((token.sval != null) && ((token.sval.compareTo("DIMENSION") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
            }//while
            token.nextToken();
            numberOfUnits = (int) token.nval;
            this.initializeMatrixes();

            // Find the string DEPENDENCY --------------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("DEPENDENCY") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
                token.nextToken();
            }
            lineNumber = token.lineno();

            for (int i = 0; i <= numberOfUnits; i++) {
                boolean lineIndex = true;
                int line = 0, row = 0;

                while (token.lineno() == lineNumber) {
                    if (lineIndex) {
                        line = ((int) token.nval) - 1;
                        lineIndex = false;
                    } else {
                        row = ((int) token.nval) - 1;
                        token.nextToken();

                        dependencyMatrix[line][row] = 1;
                        if ((token.sval.compareTo("I") == 0) || (token.sval.compareTo("It") == 0) || (token.sval.compareTo("Ag") == 0)) {
                            // I, It, Ag = constraint
                            constraintMatrix[line][row] = 1;
                        }
                    }
                    token.nextToken();
                }
                lineNumber = token.lineno();
            }

            // Find the string ATTRIBUTE ---------------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("ATTRIBUTE") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
                token.nextToken();
            }//while
            lineNumber = token.lineno();

            for (int i = 0; i <= numberOfUnits; i++) {
                boolean lineIndex = true;
                int line = 0, row = 0;

                while (token.lineno() == lineNumber) {
                    if (lineIndex) {
                        line = ((int) token.nval) - 1;
                        lineIndex = false;
                    } else {
                        row = ((int) token.nval) - 1;
                        token.nextToken();
                        attributeCouplingMatrix[line][row] = (int) token.nval;
                    }
                    token.nextToken();
                }

                lineNumber = token.lineno();
            }

            // Find the string METHOD ------------------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("METHOD") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
                token.nextToken();
            }
            lineNumber = token.lineno();

            for (int i = 0; i <= numberOfUnits; i++) {
                boolean lineIndex = true;
                int line = 0, row = 0;

                while (token.lineno() == lineNumber) {
                    if (lineIndex) {
                        line = ((int) token.nval) - 1;
                        lineIndex = false;
                    } else {
                        row = ((int) token.nval) - 1;
                        token.nextToken();
                        methodCouplingMatrix[line][row] = (int) token.nval;
                    }
                    token.nextToken();
                }

                lineNumber = token.lineno();
            }

            // Find the string METHODRETURNTYPE --------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("METHODRETURNTYPE") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
                token.nextToken();
            }
            lineNumber = token.lineno();

            for (int i = 0; i <= numberOfUnits; i++) {
                boolean lineIndex = true;
                int line = 0, row = 0;

                while (token.lineno() == lineNumber) {
                    if (lineIndex) {
                        line = ((int) token.nval) - 1;
                        lineIndex = false;
                    } else {
                        row = ((int) token.nval) - 1;
                        token.nextToken();
                        methodReturnTypeMatrix[line][row] = (int) token.nval;
                    }
                    token.nextToken();
                }

                lineNumber = token.lineno();
            }

            // Find the string METHODPARAMTYPE ---------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("METHODPARAMTYPE") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
                token.nextToken();
            }
            lineNumber = token.lineno();

            for (int i = 0; i <= numberOfUnits; i++) {
                boolean lineIndex = true;
                int line = 0, row = 0;

                while (token.lineno() == lineNumber) {
                    if (lineIndex) {
                        line = ((int) token.nval) - 1;
                        lineIndex = false;
                    } else {
                        row = ((int) token.nval) - 1;
                        token.nextToken();
                        methodParamTypeMatrix[line][row] = (int) token.nval;
                    }
                    token.nextToken();
                }

                lineNumber = token.lineno();
            }

            // Find the string ASPECTS ---------------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("ASPECTS") == 0) || (token.sval.compareTo("END") == 0))) {
                    lineNumber = token.lineno() + 1;
                    break;
                }
                token.nextToken();
            }
            token.nextToken();
            while (token.lineno() == lineNumber) {
                aspects.add(((int) token.nval) - 1);
                token.nextToken();
            }
        } catch (Exception e) {
            System.err.println("CITOProblem.readProblem():" + e);
            System.exit(1);
        }
    }

    private void initializeMatrixes() {
        //create instances of matrixes
        this.dependencyMatrix = new int[numberOfUnits][numberOfUnits];
        this.attributeCouplingMatrix = new int[numberOfUnits][numberOfUnits];
        this.methodCouplingMatrix = new int[numberOfUnits][numberOfUnits];
        this.methodReturnTypeMatrix = new int[numberOfUnits][numberOfUnits];
        this.methodParamTypeMatrix = new int[numberOfUnits][numberOfUnits];
        this.constraintMatrix = new int[numberOfUnits][numberOfUnits];
        this.aspects = new ArrayList<>();

        //initialize matrixes with value 0
        for (int i = 0; i < numberOfUnits; i++) {
            for (int j = 0; j < numberOfUnits; j++) {
                this.dependencyMatrix[i][j] = 0;
                this.attributeCouplingMatrix[i][j] = 0;
                this.methodCouplingMatrix[i][j] = 0;
                this.methodReturnTypeMatrix[i][j] = 0;
                this.methodParamTypeMatrix[i][j] = 0;
                this.constraintMatrix[i][j] = 0;
            }
        }
    }
    
    public String getSoftwareName() {
		return softwareName;
	}

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public ArrayList<Integer> getAspects() {
        return aspects;
    }

    public void setAspects(ArrayList<Integer> aspects) {
        this.aspects = aspects;
    }

    public int[][] getConstraintMatrix() {
        return constraintMatrix;
    }

    public void setConstraintMatrix(int[][] constraintMatrix) {
        this.constraintMatrix = constraintMatrix;
    }

    public int[][] getDependencyMatrix() {
        return dependencyMatrix;
    }

    public void setDependencyMatrix(int[][] dependencyMatrix) {
        this.dependencyMatrix = dependencyMatrix;
    }

    public int[][] getAttributeCouplingMatrix() {
        return attributeCouplingMatrix;
    }

    public void setAttributeCouplingMatrix(int[][] attributeCouplingMatrix) {
        this.attributeCouplingMatrix = attributeCouplingMatrix;
    }

    public int[][] getMethodCouplingMatrix() {
        return methodCouplingMatrix;
    }

    public void setMethodCouplingMatrix(int[][] methodCouplingMatrix) {
        this.methodCouplingMatrix = methodCouplingMatrix;
    }

    public int[][] getMethodReturnTypeMatrix() {
        return methodReturnTypeMatrix;
    }

    public void setMethodReturnTypeMatrix(int[][] methodReturnTypeMatrix) {
        this.methodReturnTypeMatrix = methodReturnTypeMatrix;
    }

    public int[][] getMethodParamTypeMatrix() {
        return methodParamTypeMatrix;
    }

    public void setMethodParamTypeMatrix(int[][] methodParamTypeMatrix) {
        this.methodParamTypeMatrix = methodParamTypeMatrix;
    }

}
