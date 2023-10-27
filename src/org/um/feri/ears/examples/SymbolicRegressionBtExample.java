package org.um.feri.ears.examples;

import java.io.IOException;
import java.util.Arrays;

import org.um.feri.ears.individual.btdemo.gp.*;
import org.um.feri.ears.individual.btdemo.gp.symbolic.*;
import org.um.feri.ears.individual.btdemo.gp.behaviour.*;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SymbolicRegressionBtExample {
    public static void main(String[] args) throws IOException {
        TreeGenerator treeGenerator = new TreeGenerator(Arrays.asList("x", "y"),-10, 10);
        Node rootNode = treeGenerator.generateRandomTree(3);
        Node.printTree(rootNode, "");

        // Define base nodes
        RootNode root = new RootNode(BehaviourTreeNodeType.ROOT);
        DecoratorNode repeat = new DecoratorNode(BehaviourTreeNodeType.REPEAT);
        CompositeNode sequencer = new CompositeNode(BehaviourTreeNodeType.SEQUENCER);
        CompositeNode selector = new CompositeNode(BehaviourTreeNodeType.SELECTOR);
        ActionNode action1 = new ActionNode(BehaviourTreeNodeType.MOVE_FORWARD);
        ActionNode action2 = new ActionNode(BehaviourTreeNodeType.MOVE_SIDE);
        ActionNode action3 = new ActionNode(BehaviourTreeNodeType.ROTATE);
        DecoratorNode inverter = new DecoratorNode(BehaviourTreeNodeType.INVERTER);
        ConditionNode condition = new ConditionNode(BehaviourTreeNodeType.RAY_HIT_OBJECT);

        // Assign properties
        repeat.setProperties(Arrays.asList(new Property("restartOnSuccess", 0,1, 1), new Property("restartOnFailure", 0,1, 0)));

        action1.setProperties(Arrays.asList(new Property("moveForwardDirection", 0, 3, 1)));
        action2.setProperties(Arrays.asList(new Property("moveSideDirection", 0, 3, 1)));
        action3.setProperties(Arrays.asList(new Property("rotateDirection", 0, 3, 1)));

        condition.setProperties(Arrays.asList(new Property("targetGameObject", 1, 7, 1), new Property("side", 0, 2, 1)));


        // Build tree
        root.setChild(repeat);
        repeat.setChild(sequencer);
        selector.setChildren(Arrays.asList(condition, action1));
        inverter.setChild(action2);
        sequencer.setChildren(Arrays.asList(selector, action3, inverter));

        Tree tree = new Tree("DemoBT", root);
        //Node.printTree(root, "");

        System.out.println(tree.toJsonString());

        // Make a request to https://localhost:7108/api/JsonToSoParser
        /*try {
            String jsonBody = tree.toJsonString();

            String apiUrl = "http://localhost:5016/api/JsonToSoParser";
            String response = sendPostRequest(apiUrl, jsonBody);

            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static String sendPostRequest(String apiUrl, String jsonBody) throws Exception {
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
