package org.um.feri.ears.individual.representations.gp;

import java.io.Serializable;

public class HttpResponse  implements Serializable {
    public class CoordinatorEvaluationResult {

        private FinalIndividualFitness[] individualFitnesses;

        public FinalIndividualFitness[] getFitnesses() {
            return individualFitnesses;
        }
    }
    private String status;
    private String message;
    private CoordinatorEvaluationResult object;

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

    public CoordinatorEvaluationResult getObject() {
        return object;
    }

    public void setObject(CoordinatorEvaluationResult object) {
        this.object = object;
    }
}