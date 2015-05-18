package org.aksw.openqa.main;


public class QueryResult extends ProcessResult {
	
	public QueryResult() {		
	}
	
	public QueryResult(ProcessResult processResult) {
		super(processResult);
	}

	public class Attr {
		public static final String INTERPRETING_RESULT = "QueryResult.INTERPRETING_RESULT";
		public static final String SYNTHESIS_RESULT = "QueryResult.SYNTHESIS_RESULT";
		public static final String RETRIEVAL_RESULT = "QueryResult.RETRIEVAL_RESULT";
	}
}