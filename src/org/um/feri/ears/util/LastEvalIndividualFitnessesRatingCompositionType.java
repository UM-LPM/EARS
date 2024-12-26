package org.um.feri.ears.util;

public enum LastEvalIndividualFitnessesRatingCompositionType {
    Default, // Rating & standard deviation always restart
    Mean, // Mean rating is preserved, standard deviation is set to default
    RatingUnchanged, // Rating and standard deviation are preserved (only for unchanged individuals)
    RatingAll, // Rating and standard deviation are preserved (also for changed individuals)
    GradualDecrease, // Rating is preserved, standard deviation is gradually decreased
}
