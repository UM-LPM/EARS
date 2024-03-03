package org.um.feri.ears.individual.representations.gp;

import java.util.HashMap;

public class HttpResponse {
    public class PopFitness{
        private FitnessIndividual[] PopFitness;

        public FitnessIndividual[] getFitnesses() {
            return PopFitness;
        }
    }
    private String status;
    private String message;
    private PopFitness object;

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

    public PopFitness getObject() {
        return object;
    }

    public void setObject(PopFitness object) {
        this.object = object;
    }
}