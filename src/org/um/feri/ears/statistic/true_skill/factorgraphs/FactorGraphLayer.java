package org.um.feri.ears.statistic.true_skill.factorgraphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class FactorGraphLayer
<TParentFactorGraph extends FactorGraph<TParentFactorGraph>,
TValue, 
TBaseVariable extends Variable<TValue>, 
TInputVariable extends Variable<TValue>, 
TFactor extends Factor<TValue>, 
TOutputVariable extends Variable<TValue>> 
    extends FactorGraphLayerBase<TValue> {

    private final List<TFactor> _LocalFactors = new ArrayList<>();
    private final List<List<TOutputVariable>> _OutputVariablesGroups = new ArrayList<>();
    private List<List<TInputVariable>> _InputVariablesGroups = new ArrayList<>();

    protected FactorGraphLayer(TParentFactorGraph parentGraph)
    {
        parentFactorGraph = parentGraph;
    }

    protected List<List<TInputVariable>> getInputVariablesGroups() {
        return _InputVariablesGroups;
    }

    // HACK

    public TParentFactorGraph parentFactorGraph;
    public TParentFactorGraph getParentFactorGraph() { return parentFactorGraph; }
    @SuppressWarnings("unused") // TODO remove if really unnecessary
    private void setParentFactorGraph( TParentFactorGraph parent ) { parentFactorGraph = parent; }
    
    public List<List<TOutputVariable>> getOutputVariablesGroups(){
        return _OutputVariablesGroups;
    }
    
    public void addOutputVariableGroup(List<TOutputVariable> group) {
        _OutputVariablesGroups.add(group);
    }
    
    public void addOutputVariable(TOutputVariable var) {
        List<TOutputVariable> g = new ArrayList<>(1); g.add(var);
        addOutputVariableGroup(g);
    }

    public List<TFactor> getLocalFactors() {
        return _LocalFactors;
    }

    @Override
    public Collection<Factor<TValue>> getUntypedFactors() {
        return new ArrayList<>(_LocalFactors);
    }

    @Override
    @SuppressWarnings("unchecked") // TODO there has to be a safer way to do this
    public void setRawInputVariablesGroups(Object value)
    {
        _InputVariablesGroups = (List<List<TInputVariable>>)value;
    }

    @Override
    public Object getRawOutputVariablesGroups()
    {
        return _OutputVariablesGroups;
    }

    protected Schedule<TValue> scheduleSequence(
                                                 Collection<Schedule<TValue>> itemsToSequence,
                                                 String nameFormat,
                                                 Object... args)
    {
        String formattedName = String.format(nameFormat, args);
        return new ScheduleSequence<>(formattedName, itemsToSequence);
    }

    protected void addLayerFactor(TFactor factor)
    {
        _LocalFactors.add(factor);
    }
}