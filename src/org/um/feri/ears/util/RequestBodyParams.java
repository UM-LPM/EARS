package org.um.feri.ears.util;

import org.um.feri.ears.problems.gp.ProgramSolution;

import java.io.Serializable;
import java.util.List;

public class RequestBodyParams implements Serializable
{
    protected String CoordinatorURI;
    protected String[] EvalEnvInstanceURIs;
    protected String SourceFilePath;
    protected String DestinationFilePath;
    protected RatingSystemRating[] LastEvalPopRatings;

    public RequestBodyParams()
    {
        this("", new String[]{}, "", "", new RatingSystemRating[]{});
    }

    public RequestBodyParams(String coordinatorURI, String[] evalEnvInstanceURIs, String sourceFilePath, String destinationFilePath){
        this(coordinatorURI, evalEnvInstanceURIs, sourceFilePath, destinationFilePath, new RatingSystemRating[]{});
    }

    public RequestBodyParams(String coordinatorURI, String[] evalEnvInstanceURIs, String sourceFilePath, String destinationFilePath, RatingSystemRating[] lastEvalPopRatings){
        this.CoordinatorURI = coordinatorURI;
        this.EvalEnvInstanceURIs = evalEnvInstanceURIs;
        this.SourceFilePath = sourceFilePath;
        this.DestinationFilePath = destinationFilePath;
        this.LastEvalPopRatings = lastEvalPopRatings;
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

    public RatingSystemRating[] getLastEvalPopRatings() {
        return LastEvalPopRatings;
    }

    public void setLastEvalPopRatings(RatingSystemRating[] lastEvalPopRatings) {
        LastEvalPopRatings = lastEvalPopRatings;
    }

    public void setLastEvalPopRatings(List<ProgramSolution> solutions) {
        RatingSystemRating[] lastEvalPopRatings = new RatingSystemRating[solutions.size()];
        for (int i = 0; i < solutions.size(); i++) {
            if(!solutions.get(i).isDirty())
                lastEvalPopRatings[i] = new RatingSystemRating(solutions.get(i).getObjective(0), solutions.get(i).getRatingStandardDeviation());
            else
                lastEvalPopRatings[i] = new RatingSystemRating();
        }
        this.LastEvalPopRatings = lastEvalPopRatings;
    }
}