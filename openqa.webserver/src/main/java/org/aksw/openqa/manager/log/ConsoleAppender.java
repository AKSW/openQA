package org.aksw.openqa.manager.log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.log4j.PatternLayout;

public class ConsoleAppender extends org.apache.log4j.ConsoleAppender {
	
	public final static String LAYOUT_PATTERN = "%d{HH:mm:ss dd MMM yyyy} - [%t] %-5p %c %x - %m%n";


	public ConsoleAppender() {
		OutputStream out = new OutputStream() {

  	      @Override
  	      public void write(final int b) throws IOException {
  	      }

  	      @Override
  	      public void write(byte[] b, int off, int len) throws IOException {
  	    	  ConsoleLog.getInstance().addMessage(new String(b, off, len));
  	      }

  	      @Override
  	      public void write(byte[] b) throws IOException {
  	    	ConsoleLog.getInstance().addMessage(new String(b, 0, b.length));
  	      }
  	    };
  	    
	  	Writer writer = new OutputStreamWriter(out);
	  	setWriter(writer);
	  	setLayout(new PatternLayout(LAYOUT_PATTERN));
	}
	
}
