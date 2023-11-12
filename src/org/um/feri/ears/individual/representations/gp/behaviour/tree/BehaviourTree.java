package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import com.google.gson.Gson;
import org.um.feri.ears.individual.representations.gp.HttpResponse;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class BehaviourTree extends Tree {
    public BehaviourTree(String name) {
        super(name,null);
    }

    public BehaviourTree(String name, Node rootNode) {
        super(name,rootNode);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        // TODO Call API and execute one game and return the score (fitness)
        try {
            String apiUrl = "http://localhost:5016/api/JsonToSoParser";
            String jsonBody = this.toJsonString();
            String response = sendEvaluateRequest(apiUrl, jsonBody);
            response = response.replace("\"{", "{");
            response = response.replace("}\"", "}");
            response = response.replace("\\", "");
            Gson gson = new Gson();
            HttpResponse obj = gson.fromJson(response, HttpResponse.class);
            //return obj.getObject().getFitness();
            return Double.MAX_VALUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public BehaviourTree clone() {
        BehaviourTree cloned = new BehaviourTree(this.name);
        cloned.rootNode = this.rootNode.clone();
        return cloned;
    }

    @Override
    public String toJsonString(){
        if(rootNode instanceof RootNode)
            return super.toJsonString();
        else
            return insertAdditionalNodes().toJsonString();
    }


    public BehaviourTree insertAdditionalNodes(){
        BehaviourTree newTree = this.clone();
        Node oldRoot = newTree.rootNode;
        RootNode rootNodeNew = new RootNode();
        Repeat repeatNode = new Repeat(BehaviourTreeNodeType.REPEAT, List.of(
                new Property("restartOnSuccess",0, 2, 1),
                new Property("restartOnFailure",0, 2, 1)
        ));
        rootNodeNew.insert(0, repeatNode);
        repeatNode.insert(0, oldRoot);
        newTree.rootNode = rootNodeNew;
        return newTree;
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
