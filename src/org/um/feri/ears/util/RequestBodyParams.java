package org.um.feri.ears.util;

import java.io.Serializable;

public class RequestBodyParams implements Serializable
{
    protected String CoordinatorURI;
    protected String[] EvalEnvInstanceURIs;
    protected String SourceFilePath;
    protected String DestinationFilePath;

    public RequestBodyParams(){
        this.CoordinatorURI = "http://localhost:0000";
        this.EvalEnvInstanceURIs = new String[]{"http://localhost:1111"};
        this.SourceFilePath = "";
        this.DestinationFilePath = "";
    }

    public RequestBodyParams(String coordinatorURI, String[] evalEnvInstanceURIs, String sourceFilePath, String destinationFilePath){
        this.CoordinatorURI = coordinatorURI;
        this.EvalEnvInstanceURIs = evalEnvInstanceURIs;
        this.SourceFilePath = sourceFilePath;
        this.DestinationFilePath = destinationFilePath;
    }

    public String getCoordinatorURI() {
        return CoordinatorURI;
    }

    public void setCoordinatorURI(String coordinatorURI) {
        CoordinatorURI = coordinatorURI;
    }

    public String[] getEvalEnvInstanceURIs() {
        return EvalEnvInstanceURIs;
    }

    public void setEvalEnvInstanceURIs(String[] evalEnvInstanceURIs) {
        EvalEnvInstanceURIs = evalEnvInstanceURIs;
    }

    public String getSourceFilePath() {
        return SourceFilePath;
    }

    public void setSourceFilePath(String sourceFilePath) {
        SourceFilePath = sourceFilePath;
    }

    public String getDestinationFilePath() {
        return DestinationFilePath;
    }

    public void setDestinationFilePath(String destinationFilePath) {
        DestinationFilePath = destinationFilePath;
    }
}