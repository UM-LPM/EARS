package org.um.feri.ears.statistic.rating_system;

import org.um.feri.ears.statistic.rating_system.glicko2.Glicko2Rating;
import org.um.feri.ears.statistic.rating_system.true_skill.TrueSkillRating;

/**
 * Parameters about the game for calculating TrueSkill and Glicko2.
 */

public class GameInfo {
	/* TrueSkill parameters */
	public static final double defaultInitialMean = 25.0;
	public static final double defaultBeta = defaultInitialMean / 6.0;
	public static final double defaultDrawProbability = 0.1;
	public static final double defaultDynamicsFactor = defaultInitialMean / 300.0;
	public static final double defaultInitialStandardDeviation = defaultInitialMean / 3.0;
	public double initialMean;
	public double initialStandardDeviation;
	public double beta;
	public double dynamicsFactor;
	public double drawProbability;

	/* Glicko2 parameters */
	public static final double defaultInitialRating = 1500.0;
	public static final double defaultInitialRatingDeviation = 350.0;
	public static final double defaultInitialRatingVolatility = 0.06;

	public GameInfo(double initialMean, double initialStandardDeviation, double beta, double dynamicFactor, double drawProbability) {

		this.initialMean = initialMean;
		this.initialStandardDeviation = initialStandardDeviation;
		this.beta = beta;
		this.dynamicsFactor = dynamicFactor;
		this.drawProbability = drawProbability;
	}

	public static GameInfo getDefaultGameInfo() {
		// We return a fresh copy since we have public setters that can mutate state
		return new GameInfo(defaultInitialMean, defaultInitialStandardDeviation, defaultBeta, defaultDynamicsFactor, defaultDrawProbability);
	}

	public static TrueSkillRating getDefaultTrueSkillRating() {
		return new TrueSkillRating(defaultInitialMean, defaultInitialStandardDeviation);
	}

	public static Glicko2Rating getDefaultGlicko2Rating() {
		return new Glicko2Rating(defaultInitialRating, defaultInitialRatingDeviation, defaultInitialRatingVolatility);
	}

	public double getInitialMean() {
		return this.initialMean;
	}

	public double getInitialStandardDeviation() {
		return this.initialStandardDeviation;
	}

	public double getBeta() {
		return this.beta;
	}

	public double getDynamicsFactor() {
		return this.dynamicsFactor;
	}

	public double getDrawProbability() {
		return this.drawProbability;
	}

	public void setInitialMean(final double initialMean) {
		this.initialMean = initialMean;
	}

	public void setInitialStandardDeviation(final double initialStandardDeviation) {
		this.initialStandardDeviation = initialStandardDeviation;
	}

	public void setBeta(final double beta) {
		this.beta = beta;
	}

	public void setDynamicsFactor(final double dynamicsFactor) {
		this.dynamicsFactor = dynamicsFactor;
	}

	public void setDrawProbability(final double drawProbability) {
		this.drawProbability = drawProbability;
	}
}