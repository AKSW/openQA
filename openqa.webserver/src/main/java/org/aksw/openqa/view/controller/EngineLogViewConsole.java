package org.aksw.openqa.view.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.aksw.openqa.manager.log.ConsoleLog;
import org.aksw.openqa.manager.log.ConsoleLog.MemoryMeasure;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.CartesianChartModel;

@ManagedBean(name="engineLogConsoleController")
@SessionScoped
public class EngineLogViewConsole implements Serializable {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4523518673061012743L;	
	private static Logger logger = Logger.getLogger(EngineLogViewConsole.class);
	
	private String log;
	private CartesianChartModel linearMemoryConsumeModel;
	
	public EngineLogViewConsole() {
	}
	
	public String getLog() {
		String[] messages = ConsoleLog.getInstance().getLastLogMessages(); 
		log= "";
		for(String message : messages) {
			log += message;
		}		
		return log;
	}
	
	public void cleanLogMessages(ActionEvent event) {
		ConsoleLog.getInstance().clearLogMessages();
	}
	
	public void cleanMemoryConsumeLog(ActionEvent event) {
		ConsoleLog.getInstance().clearMemoryConsumeLog();
	}
	
	public void setLog(String value) {
		log = value;
	}
	
	public StreamedContent getFile() {
		InputStream stream = null;
		try {
			stream = new FileInputStream(ConsoleLog.getInstance().getLoggerFile());
		} catch (FileNotFoundException e) {
			logger.error("Can not find the logger file", e);
		}		
        return new DefaultStreamedContent(stream, "text/plain", ConsoleLog.LOG_FILE_NAME);
    }
	
	 /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A sample chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        return ChartFactory.createTimeSeriesChart(
            "Memory Usage", 
            "Time", 
            "Memory(MB)", 
            dataset,
            false, 
            false, 
            false
        );
    }
    
    /**
     * Creates the memory series set.
     * 
     * @return the memory series set.
     */
    private XYDataset createDataset(MemoryMeasure[] memoryMeasures) {    	
        final TimeSeries series = new TimeSeries("Memory Data");
        for (MemoryMeasure memoryMeasure: memoryMeasures) {
            try {
                series.add(new FixedMillisecond(memoryMeasure.date.getTime()), new Double(memoryMeasure.memory));
            }
            catch (SeriesException e) {
                System.err.println("Error adding to series");
            }
        }
        return new TimeSeriesCollection(series);
    }
	
	public CartesianChartModel getLinearMemoryConsumeModel() {
		return linearMemoryConsumeModel;
	}
	
	public StreamedContent getChart() {
		MemoryMeasure[] memoryMeasures = ConsoleLog.getInstance().getLastLogMemoryMeasures();
		StreamedContent chart = null;
		if(memoryMeasures.length > 0) {
	        XYDataset dataset = createDataset(memoryMeasures);
	        JFreeChart memoryChart = createChart(dataset);
	        File chartFile = new File("dynamichart");	        
			try {
				ChartUtilities.saveChartAsPNG(chartFile, memoryChart, 600, 300);				
				chart = new DefaultStreamedContent(new FileInputStream(chartFile), "image/png");
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	 	return chart;
	}
}
