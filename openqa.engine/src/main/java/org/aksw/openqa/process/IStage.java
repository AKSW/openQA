package org.aksw.openqa.process;

import java.util.List;

public interface IStage <K, T> {
	public List<K> exec(List<T> input, Object... staticArgs) throws Exception;
}
