package org.aksw.openqa.process;

import java.util.List;

public abstract class AbstractStage <K, T> implements IStage<K, T> {

	public abstract List<K> exec(List<T> input, Object... staticArgs) throws Exception;

}
