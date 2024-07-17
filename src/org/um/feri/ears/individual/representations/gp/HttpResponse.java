package org.um.feri.ears.individual.representations.gp;

import java.util.HashMap;

public class HttpResponse {
    public class HttpServerResponse{
        private FitnessIndividual[] popFitness;
        private int[][] btsNodeCallFrequencies;

        public FitnessIndividual[] getFitnesses() {
            return popFitness;
        }

        public int[][] getBtsNodeCallFrequencies() {
            return btsNodeCallFrequencies;
        }
    }
    private String status;
    private String message;
    private HttpServerResponse object;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpServerResponse getObject() {
        return object;
    }

    public void setObject(HttpServerResponse object) {
        this.object = object;
    }
}