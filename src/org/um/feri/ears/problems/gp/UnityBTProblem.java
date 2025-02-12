package org.um.feri.ears.problems.gp;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.orsoncharts.util.json.JSONArray;
import com.orsoncharts.util.json.parser.JSONParser;
import com.orsoncharts.util.json.parser.ParseException;
import org.um.feri.ears.algorithms.gp.GPAlgorithmExecutor;
import org.um.feri.ears.individual.representations.gp.HttpResponse;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.individual.generations.gp.GPProgramSolution;
import org.um.feri.ears.operators.gp.FeasibilityGPOperator;
import org.um.feri.ears.operators.gp.GPOperator;
import org.um.feri.ears.util.RequestBodyParams;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.visualization.gp.GPInterface;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class UnityBTProblem extends ProgramProblem {

    public static final int BULK_EVALUATION_REPEATS = 5;

    public UnityBTProblem() {
        super("UnityBTProblem", Tree.TreeType.BEHAVIOUR);
    }

    public UnityBTProblem(String problemName, List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeDepth, int maxTreeStartDepth, int maxTreeEndDepth, int maxTreeSize, FeasibilityGPOperator[] feasibilityControlOperators, GPOperator[] bloatControlOperators, GPProgramSolution programSolutionGenerator) {
        super(problemName, baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeDepth, maxTreeStartDepth, maxTreeEndDepth, maxTreeSize, feasibilityControlOperators, bloatControlOperators, programSolutionGenerator, Tree.TreeType.BEHAVIOUR, "BAS", new RequestBodyParams());
    }
    public UnityBTProblem(List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeDepth, int maxTreeStartDepth, int maxTreeEndDepth, int maxTreeSize, FeasibilityGPOperator[] feasibilityControlOperators, GPOperator[] bloatControlOperators, GPProgramSolution programSolutionGenerator) {
        super("UnityBTProblem", baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeDepth, maxTreeStartDepth, maxTreeEndDepth, maxTreeSize, feasibilityControlOperators, bloatControlOperators, programSolutionGenerator, Tree.TreeType.BEHAVIOUR, "BAS", new RequestBodyParams());
    }

    @Override
    public void evaluate(ProgramSolution solution) {
        double eval = solution.tree.evaluate(null);

        solution.setObjective(0, eval);
    }

    @Override
    public void bulkEvaluate(List<ProgramSolution> solutions){
        JSONArray jsonArray = new JSONArray();
        JSONParser jsonParser = new JSONParser();

        for (ProgramSolution solution : solutions) {
            try {
                String json = solution.tree.toJsonString();
                jsonArray.add(jsonParser.parse(json));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        // Repeat evaluation X times (to avoid connection and other errors)
        for(int nums = 0; nums < BULK_EVALUATION_REPEATS; nums++) {
            try {
                String apiUrl = "http://localhost:5016/api/JsonToSoParser";
                String response = Util.sendEvaluateRequest(apiUrl, jsonArray.toJSONString(), 100 * 60 * 1000, getRequestBodyParams(), getJsonBodyDestFolderPath());

                Gson gson = new Gson();
                HttpResponse obj = gson.fromJson(response, HttpResponse.class);
                System.out.println(LocalDateTime.now() + "Finished with response: " + response);

                // Verify that request was successful
                if (Objects.equals(obj.getStatus(), "Success")) {
                    // Set fitness values after evaluation
                    for (int i = 0; i < solutions.size(); i++) {
                        solutions.get(i).setObjective(0, obj.getObject().getFitnesses()[i].finalFitnessStats);
                        solutions.get(i).setFitnesses(obj.getObject().getFitnesses()[i].fitnesses);
                        solutions.get(i).setNodeCallFrequencyCount(obj.getObject().getBtsNodeCallFrequencies()[i]);
                    }
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Save exception to log file
                Util.appendExceptionToLogFile(e, "ErrorReport.txt");

                // Restart Unity instances
                if(GPAlgorithmExecutor.Instance != null){
                    GPAlgorithmExecutor.Instance.restartUnityInstances(true);
                }

                //wait for 5 seconds
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        System.exit(1);
    }

    public void evaluate(List<ProgramSolution> solutions){
        bulkEvaluate(solutions);
    }
}