package org.aksw.openqa.main;

import java.util.List;

import org.aksw.openqa.component.IComponent;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.object.Result;

public class ProcessResult extends Result {
		private List<? extends IResult> output = null;
		private List<? extends IParams> input = null;
		private long runtime=0;
		private Throwable exception=null;
		
		public ProcessResult() {
		}
		
		public ProcessResult(ProcessResult processResult) {
			this.output = processResult.output;
			this.input = processResult.getInput();
			this.runtime = processResult.getRuntime();
			this.exception = processResult.getException();
		}

		public List<? extends IResult> getOutput() {
			return output;
		}

		public void setOutput(List<? extends IResult> processResults) {
			this.output = processResults;
		}

		public long getRuntime() {
			return runtime;
		}

		public void setRuntime(long runtime) {
			this.runtime = runtime;
		}
		
		public void setComponentSource(IComponent source) {
			this.source = source;
		}

		public Throwable getException() {
			return exception;
		}

		public void setException(Throwable exception) {
			this.exception = exception;
		}

		public List<? extends IParams> getInput() {
			return input;
		}

		public void setInput(List<? extends IParams> params) {
			this.input = params;
		}
	}