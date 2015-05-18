package org.aksw.openqa.component.answerformulation.synthesizer.impl;

import java.util.Map;

import org.aksw.openqa.component.answerformulation.AbstractSynthesizerFactorySpi;
import org.aksw.openqa.component.answerformulation.ISynthesizer;

public class DefaultSynthesizerFactory extends AbstractSynthesizerFactorySpi {
	@Override
	public ISynthesizer create(Map<String, Object> params) {
		return create(DefaultRDFTermSynthesizer.class, params);
	}
}
