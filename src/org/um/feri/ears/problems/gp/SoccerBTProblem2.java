package org.um.feri.ears.problems.gp;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.orsoncharts.util.json.JSONArray;
import com.orsoncharts.util.json.JSONObject;
import com.orsoncharts.util.json.parser.JSONParser;
import com.orsoncharts.util.json.parser.ParseException;
import org.um.feri.ears.individual.btdemo.gp.HttpResponse;
import org.um.feri.ears.individual.btdemo.gp.Node;
import org.um.feri.ears.individual.btdemo.gp.behaviour.BehaviourTree;
import org.um.feri.ears.individual.btdemo.gp.behaviour.Tree;
import org.um.feri.ears.individual.generations.gp.GPProgramSolution2;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.operators.gp.GPOperator;
import org.um.feri.ears.operators.gp.GPOperator2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SoccerBTProblem2 extends ProgramProblem2 {

    public static final int BULK_EVALUATION_REPEATS = 5;

    public SoccerBTProblem2() {
        super("SoccerBTProblem");
    }

    public SoccerBTProblem2(List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeHeight, int maxTreeHeight, int maxTreeNodes, GPOperator2 pruningOperator, GPOperator2 expansionOperator, GPProgramSolution2 programSolutionGenerator) {
        super("SoccerBTProblem", baseFunctionNodeTypes, baseTerminalNodeTypes, minTreeHeight, maxTreeHeight, maxTreeNodes, pruningOperator, expansionOperator, programSolutionGenerator, Tree.TreeType.BEHAVIOUR, "BAS");

    }

    @Override
    public void evaluate(ProgramSolution2 solution) {
        double eval = solution.tree.evaluate(null);

        solution.setObjective(0, eval);
    }

    @Override
    public void bulkEvaluate(List<ProgramSolution2> solutions){
        JSONArray jsonArray = new JSONArray();
        JSONParser jsonParser = new JSONParser();

        for (ProgramSolution2 solution : solutions) {
            try {
                jsonArray.add(jsonParser.parse(solution.tree.toJsonString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            // Repeat evaluation X times (to avoid connection and other errors)
            for(int nums = 0; nums < BULK_EVALUATION_REPEATS; nums++) {
                String apiUrl = "http://localhost:5016/api/JsonToSoParser";
                String response = sendEvaluateRequest(apiUrl, jsonArray.toJSONString());
                response = response.replace("\"{", "{");
                response = response.replace("}\"", "}");
                response = response.replace("\\", "");
                Gson gson = new Gson();
                HttpResponse obj = gson.fromJson(response, HttpResponse.class);
                System.out.println("Finished with response: " + response);

                // Verify that request was successful
                if (Objects.equals(obj.getStatus(), "Success")) {
                    // Set fitness values after evaluation
                    for (int i = 0; i < solutions.size(); i++) {
                        solutions.get(i).setObjective(0, obj.getObject().getFitness()[i]);
                    }
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO delete method
    public void evaluate(List<ProgramSolution2> solutions){
        JSONArray jsonArray = new JSONArray();
        JSONParser jsonParser = new JSONParser();

        for (ProgramSolution2 solution : solutions) {
            try {
                jsonArray.add(jsonParser.parse(solution.tree.toJsonString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        // Convert solutions to jsonStringArray
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        try {
            String apiUrl = "http://localhost:5016/api/JsonToSoParser";
            String response = sendEvaluateRequest(apiUrl, jsonArray.toJSONString());
            response = response.replace("\"{", "{");
            response = response.replace("}\"", "}");
            response = response.replace("\\", "");
            Gson gson = new Gson();
            //HttpResponse obj = gson.fromJson(response, HttpResponse.class);
            System.out.println("Finished with response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set fitness values after evaluation
        //solution.setObjective(0, eval);
    }

    public String sendEvaluateRequest(String apiUrl, String jsonBody) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set the request method to POST
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Write the JSON payload to the request body
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return response.toString();
            }
        } else {
            throw new RuntimeException("HTTP POST request failed with response code: " + responseCode);
        }
    }
}