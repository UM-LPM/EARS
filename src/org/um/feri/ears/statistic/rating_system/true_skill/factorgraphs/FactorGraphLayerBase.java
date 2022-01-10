package org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs;

import java.util.Collection;

public abstract class FactorGraphLayerBase<TValue> {

  public abstract Collection<Factor<TValue>> getUntypedFactors();
  public abstract void buildLayer();

  public Schedule<TValue> createPriorSchedule()
  {
      return null;
  }

  public Schedule<TValue> createPosteriorSchedule()
  {
      return null;
  }

  // HACK

  public abstract void setRawInputVariablesGroups(Object value);
  public abstract Object getRawOutputVariablesGroups();
}
