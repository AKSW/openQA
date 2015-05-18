package org.aksw.openqa.component.ui.render;

public class IDGenerator {
	private static IDGenerator idGenerator = null;
	private static long id = 0;
	
	public static IDGenerator getInstance() {
		if(idGenerator == null)
			idGenerator = new IDGenerator();		
		return idGenerator;
	}
	
	public long generateID() {
		return id+=1;
	}
	
}
