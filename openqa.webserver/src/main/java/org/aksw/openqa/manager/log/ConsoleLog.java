package org.aksw.openqa.manager.log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.aksw.openqa.Status;

public class ConsoleLog {

	public final static String LOG_FILE_NAME = "openQA.log";
	private static ConsoleLog instance;
	private static ArrayBlockingQueue<String> messages = new ArrayBlockingQueue<String>(300);
	private static ArrayBlockingQueue<MemoryMeasure> memoryConsume = new ArrayBlockingQueue<MemoryMeasure>(300);
	private File file = null;
	private Thread memoryMeasureThread = null;
	private boolean running = true;
	
	public ConsoleLog() {
		String userDir = System.getProperty("user.dir");
		file = new File(userDir + "\\" + ConsoleLog.LOG_FILE_NAME);
		
		memoryMeasureThread = new Thread(new MemoryMeasureThread());		
		memoryMeasureThread.start();
	}
	
	public static ConsoleLog getInstance() {
		if(instance == null)
			instance = new ConsoleLog();
		return instance;
	}
	
	public void addMessage(String message) {
		if(messages.remainingCapacity() == 0)
			messages.poll();
		messages.add(message);
	}
	
	public void clearLogMessages() {
		messages.clear();
	}
	
	public void clearMemoryConsumeLog() {
		memoryConsume.clear();
	}
	
	public String[] getLastLogMessages() {
		List<String> messages = new ArrayList<String>(ConsoleLog.messages);
		Collections.reverse(messages);
		return messages.toArray(new String[messages.size()]);
	}
	
	public MemoryMeasure[] getLastLogMemoryMeasures() {
		return memoryConsume.toArray(new MemoryMeasure[memoryConsume.size()]);
	}
	
	public File getLoggerFile() {
		return file;
	}
	
	public class MemoryMeasure  {
		public Date date; // time in hours
		public double memory; // memory in MB 
	}
	
	protected class MemoryMeasureThread implements Runnable {

		@Override
		public void run() {			
			try {
				while(running) {
					MemoryMeasure mesure = getCurrentMemoryMesure();
					ConsoleLog.getInstance().addMesure(mesure);
					Thread.sleep(60000);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		
	}

	public void addMesure(MemoryMeasure mesure) {
		if(memoryConsume.remainingCapacity() == 0)
			memoryConsume.poll();
		memoryConsume.add(mesure);
	}
	
	public MemoryMeasure getCurrentMemoryMesure() {		
		MemoryMeasure measure = new MemoryMeasure();
		long time = System.currentTimeMillis();
		double memory = Status.getSystemMemory(); // memory in Megabytes
		measure.memory = memory;
		measure.date = new Date(time);
		return measure;
	}

	public void shutdown() {
		running = false;
	}
}
