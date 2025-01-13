package org.um.feri.ears.util;

import org.um.feri.ears.individual.representations.gp.IndividualFitness;
import org.um.feri.ears.problems.gp.ProgramSolution;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class RequestBodyParams implements Serializable
{
    protected String CoordinatorURI;
    protected String[] EvalEnvInstanceURIs;
    protected String SourceFilePath;
    protected String DestinationFilePath;
    protected IndividualFitness[] LastEvalIndividualFitnesses;

    public RequestBodyParams()
    {
        this("", new String[]{}, "", "", null);
    }

    public RequestBodyParams(String coordinatorURI, String[] evalEnvInstanceURIs, String sourceFilePath, String destinationFilePath){
        this(coordinatorURI, evalEnvInstanceURIs, sourceFilePath, destinationFilePath, null);
    }

    public RequestBodyParams(String coordinatorURI, String[] evalEnvInstanceURIs, String sourceFilePath, String destinationFilePath, IndividualFitness[] lastEvalPopRatings){
        this.CoordinatorURI = coordinatorURI;
        this.EvalEnvInstanceURIs = evalEnvInstanceURIs;
        this.SourceFilePath = sourceFilePath;
        this.DestinationFilePath = destinationFilePath;
        this.LastEvalIndividualFitnesses = lastEvalPopRatings;
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

    public IndividualFitness[] getLastEvalIndividualFitnesses() {
        return LastEvalIndividualFitnesses;
    }

    public void setLastEvalIndividualFitnesses(IndividualFitness[] lastEvalIndividualFitnesses) {
        LastEvalIndividualFitnesses = lastEvalIndividualFitnesses;
    }

    public void setLastEvalIndividualFitnesses(List<ProgramSolution> solutions, LastEvalIndividualFitnessesRatingCompositionType lastEvalIndividualFitnessesRatingCompositionType) {
        if(solutions == null || solutions.size() == 0 || solutions.get(0).getFitness().individualID == -1)
        {
            return;
        }

        IndividualFitness[] lastEvalIndividualFitnesses = new IndividualFitness[solutions.size()];

        for (int i = 0; i < solutions.size(); i++) {
            switch (lastEvalIndividualFitnessesRatingCompositionType){
                case Default:
                    lastEvalIndividualFitnesses[i] = new IndividualFitness(i);
                    break;
                case Mean:
                    int finalI = i;
                    lastEvalIndividualFitnesses[i] = new IndividualFitness(i, new HashMap<>(){{
                        put("Rating", solutions.get(finalI).getFitness().getAdditionalValues().get("Rating"));
                    }});
                    break;
                case RatingUnchanged:
                    if(!solutions.get(i).isDirty())
                        lastEvalIndividualFitnesses[i] = new IndividualFitness(i, solutions.get(i).getFitness().getAdditionalValues());
                    else
                        lastEvalIndividualFitnesses[i] = new IndividualFitness(i);
                    break;
                case RatingAll:
                    lastEvalIndividualFitnesses[i] = new IndividualFitness(i, solutions.get(i).getFitness().getAdditionalValues());
                    break;
                case GradualDecrease:
                    throw new IllegalArgumentException("GradualDecrease not implemented");
                default:
                    throw new IllegalArgumentException("Unknown LastEvalIndividualFitnessesRatingCompositionType");
            }
        }
        this.LastEvalIndividualFitnesses = lastEvalIndividualFitnesses;
    }

    public void resetLastEvalIndividualFitnesses() {
        this.LastEvalIndividualFitnesses = null;
    }
}