package org.aksw.openqa.process;

import java.util.List;

public abstract class AbstractStageExecutor<K, T> implements IStageExecutor<K, T> {

	@Override
	public List<K> exec(List<IStage<K, T>> stages, List<T> input, Object... staticArgs) throws Exception {
		List<K> result = null;
		for(IStage<K, T> stage : stages) {
			result = stage.exec(input, staticArgs);
			input = (List<T>) result;
		}
		return result;
	}
}
