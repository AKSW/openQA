package org.aksw.openqa.view.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.aksw.openqa.Status;

@ManagedBean(name="statisticsSessionController")
@SessionScoped
public class StatisticsSessionController {
	
	public int getNumberOfQueries() {
		return Status.getNumberOfQueries();
	}

	public int getNumberOfErrors() {
		return Status.getNumberOfErrors();
	}

	public int getQueriesWithoutResult() {
		return Status.getQueriesWithourResult();
	}
	
	public double getTimeRunning() {
		double longRunning =  ((System.currentTimeMillis() - Status.getStartRunning().getTime()) / 1000) / 3600; // time running in hours		
		return longRunning;
	}
	
	public double getUsedMemory() {
		return Status.getSystemMemory();
	}
	
	public int getActiveThreads() {
		return java.lang.Thread.activeCount();
	}
	
}
