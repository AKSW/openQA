package org.aksw.openqa.process;

import java.util.List;

public interface IProcessor<K, T> {
	public List<K> exec(List<IStage<K,T>> stages, List<T> input,  IStageExecutor<K,T> stageExecutor, Object... staticArgs) throws Exception;
}
