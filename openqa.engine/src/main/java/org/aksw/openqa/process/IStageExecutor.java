package org.aksw.openqa.process;

import java.util.List;

public interface IStageExecutor<K, T> {
	public List<K> exec(List<IStage<K,T>> stages, List<T> input, Object... staticArgs) throws Exception;
}
