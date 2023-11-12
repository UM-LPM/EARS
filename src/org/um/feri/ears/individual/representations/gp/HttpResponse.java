package org.um.feri.ears.individual.btdemo.gp;

public class HttpResponse {
    public class Eval{
        private double[] fitness;

        public double[] getFitness() {
            return fitness;
        }

        public void setFitness(double[] fitness) {
            fitness = fitness;
        }
    }
    private String status;
    private String message;
    private Eval object;

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

    public Eval getObject() {
        return object;
    }

    public void setObject(Eval object) {
        this.object = object;
    }
}