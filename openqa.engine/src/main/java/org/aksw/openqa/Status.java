package org.aksw.openqa;

import java.util.Date;

public class Status {
	
	private static Date startRunning = new Date();
	private static int numberOfQueries = 0;
	private static int numberOfErrors = 0;
	private static int queriesWithourResult = 0;
	
	public static Date getStartRunning() {
		return startRunning;
	}
	
	public static int getNumberOfQueries() {
		return numberOfQueries;
	}

	public static void setNumberOfQueries(int numberOfQueries) {
		Status.numberOfQueries = numberOfQueries;
	}

	public static int getNumberOfErrors() {
		return numberOfErrors;
	}

	public static void setNumberOfErrors(int numberOfErrors) {
		Status.numberOfErrors = numberOfErrors;
	}

	public static int getQueriesWithourResult() {
		return queriesWithourResult;
	}

	public static void setQueriesWithourResult(int queriesWithourResult) {
		Status.queriesWithourResult = queriesWithourResult;
	}

	public static void setStartRunning(Date startRunning) {
		Status.startRunning = startRunning;
	}
	
	public static double getSystemMemory() {
		double memory = Runtime.getRuntime().totalMemory()/1048576.0; // memory in Megabytes
		return memory;
	}
}
