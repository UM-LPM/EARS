package org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs;

import java.util.*;

public abstract class Factor<TValue> {

    protected final List<Message<TValue>> messages = new ArrayList<>();

    private final Map<Message<TValue>, Variable<TValue>> messageToVariableBinding =
      new HashMap<>();

    private final String name;
    protected final List<Variable<TValue>> variables = new ArrayList<>();

    protected Factor(String name) { this.name = "Factor[" + name + "]"; }

    /** Returns the log-normalization constant of that factor **/
    public abstract double getLogNormalization();

    /** Returns the number of messages that the factor has **/
    public int getNumberOfMessages() { return messages.size(); }

    protected List<Variable<TValue>> getVariables() {
        return Collections.unmodifiableList(variables); 
    }

    protected List<Message<TValue>> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    /** Update the message and marginal of the i-th variable that the factor is connected to **/
    public double updateMessage(int messageIndex) {
        return updateMessage(messages.get(messageIndex), messageToVariableBinding.get(messages.get(messageIndex)));
    }

    protected double updateMessage(Message<TValue> message, Variable<TValue> variable) {
        throw new UnsupportedOperationException();
    }

    /** Resets the marginal of the variables a factor is connected to **/
    public void ResetMarginals() {
        messageToVariableBinding.values().forEach(Variable::resetToPrior);
    }

    /**
     * Sends the ith message to the marginal and returns the log-normalization
     * constant
     **/
    public double SendMessage(int messageIndex) {
        Message<TValue> message = messages.get(messageIndex);
        Variable<TValue> variable = messageToVariableBinding.get(message);
        return SendMessage(message, variable);
    }

    protected abstract double SendMessage(Message<TValue> message, Variable<TValue> variable);

    public abstract Message<TValue> CreateVariableToMessageBinding(Variable<TValue> variable);

    protected Message<TValue> CreateVariableToMessageBinding(Variable<TValue> variable, Message<TValue> message) {
        messages.add(message);
        messageToVariableBinding.put(message, variable);
        variables.add(variable);

        return message;
    }

    @Override
    public String toString() { return name; }
}