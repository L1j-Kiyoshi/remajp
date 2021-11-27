package l1j.server.MJ3SEx.IntervalDecorator;

import l1j.server.server.model.L1Character;

public interface ActionIntervalDecorator<T extends L1Character> {
	public double decoration(T owner, double interval);
}
