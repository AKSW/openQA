package org.aksw.openqa.process;

import java.util.List;

public class AbstractProcess<K, T> implements IProcessor<K, T> {

	@Override
	public List<K> exec(List<IStage<K, T>> stages, List<T> input, IStageExecutor<K, T> stageExecutor,
			Object... staticArgs) throws Exception {
		return stageExecutor.exec(stages, input, staticArgs);
	}
	
}
