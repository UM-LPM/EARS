package org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs;

public class KeyedVariable<TKey, TValue> extends Variable<TValue> {
	private final TKey key;
	
	public KeyedVariable(TKey key, TValue prior, String name, Object... args) {
		super(prior, name, args);
		this.key = key;
	}
	
	@SuppressWarnings("all")
	public TKey getKey() {
		return this.key;
	}
}