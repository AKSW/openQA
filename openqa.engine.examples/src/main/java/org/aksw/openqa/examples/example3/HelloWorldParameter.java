package org.aksw.openqa.examples.example3;

import org.aksw.openqa.component.object.Params;
import org.aksw.openqa.examples.HelloWorldParameters;

public class HelloWorldParameter extends Params {
	
	public void setMatrixLine(Integer matrixLine) {
		setParam(HelloWorldParameters.Property.MATRIX_LINE, matrixLine);
	}
	
	public Integer getMatrixLine() {
		return getParam(HelloWorldParameters.Property.MATRIX_LINE, Integer.class);
	}
}
