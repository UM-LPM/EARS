package org.um.feri.analyse.sopvisualization;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AncestorUtil {

    private AncestorUtil() {
    }

    public static void saveAncestorLogging4Visualization(String path, Task task, Algorithm alg, int runID) {
        String algID = alg.getId();
        algID = algID.replaceAll("_", "");
        algID = algID.replaceAll("\\\\", "");
        algID = algID.replaceAll("/", "");
        String fileName = path + "\\" + algID + "_" + task.getProblemName() + "_D" + task.getNumberOfDimensions();

        String pop_size = alg.getParameterValue("popSize");
        StringBuilder head = new StringBuilder();
        if (pop_size == null) pop_size = "1";
        head.append(alg.getId()).append(";").append(";[\"").append(pop_size).append("\"];").append(runID).append(";"); //X id
        head.append(task.getProblemName()).append(";").append(task.getNumberOfDimensions()).append(";[").append(task.getMaxEvaluations()).append("];").append("\n");

        ArrayList<SolutionBase> ancestors = task.getAncestors();

        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName + ".txt")))) {

            bw.write(head.toString()); //first line
            for (SolutionBase ancestor : ancestors) {
                List<SolutionBase> parents = ancestor.parents;
                bw.write("{");
                bw.write(ancestor.getID() + ";" + ancestor.getGenerationNumber() + ";");
                bw.write("[");
                if (parents != null) {
                    for (int j = 0; j < parents.size(); ++j) {
                        bw.write("" + parents.get(j).getID());
                        if (j + 1 < parents.size())
                            bw.write(",");
                    }
                }
                bw.write("];0;");
                bw.write(ancestor.getID() + ";" + ancestor.getEval() + ";" + Arrays.toString(Util.toDoubleArray(((NumberSolution<Double>) ancestor).getVariables())));
                bw.write("}\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAncestorLogging(String fileName, Task task) {

        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName + ".csv")))) {

            ArrayList<SolutionBase> ancestors = task.getAncestors();
            for (SolutionBase ancestor : ancestors) {
                List<SolutionBase> parents = ancestor.parents;
                bw.write(ancestor.getID() + ";" + ancestor.getEval() + ";" + Arrays.toString(Util.toDoubleArray(((NumberSolution<Double>) ancestor).getVariables())) + ";");
                if (parents != null) {
                    bw.write("[");
                    for (int j = 0; j < parents.size(); ++j) {
                        bw.write("" + parents.get(j).getID());
                        if (j + 1 < parents.size())
                            bw.write(",");
                    }
                    bw.write("]");
                }
                bw.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveGraphingFile(String fileName, Task task, Algorithm alg) {

        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName + ".txt")))) {
            ArrayList<SolutionBase> ancestors = task.getAncestors();

            AlgorithmInfo info = alg.getAlgorithmInfo();

            bw.write("'" + alg.getId() + ";[" + alg.getParametersAsString() + "];" + task.getProblemName() + ";" + task.getNumberOfDimensions() + ";[\"" + task.getStopCriterion().getName() + "\"];'+\n");

            for (int i = 0; i < ancestors.size(); ++i) {
                List<SolutionBase> parents = ancestors.get(i).parents;

                bw.write("'{" + ancestors.get(i).getID() + ";" + ancestors.get(i).getGenerationNumber() + ";");

                if (parents != null) {
                    bw.write("[");
                    for (int j = 0; j < parents.size(); ++j) {
                        bw.write("" + parents.get(j).getID());
                        if (j + 1 < parents.size())
                            bw.write(",");
                    }
                    bw.write("];");

                } else {
                    bw.write("[-1,-1];");
                }

                bw.write(ancestors.get(i).getTimeStamp() + ";" + ancestors.get(i).getEvaluationNumber() + ";" + ancestors.get(i).getEval() + ";" + Arrays.toString(Util.toDoubleArray(((NumberSolution<Double>) ancestors.get(i)).getVariables())) + "}'");

                if (i + 1 < ancestors.size()) {
                    bw.write("+\n");
                } else {
                    bw.write(";");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
