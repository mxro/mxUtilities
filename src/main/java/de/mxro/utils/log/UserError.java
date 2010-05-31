package de.mxro.utils.log;

import java.util.Vector;


public abstract class UserError {
	
	public Vector<String> logged=new Vector<String>();
	
	public static enum Priority { LOW, NORMAL, HIGH, INFORMATION }
	
	public boolean triggerExceptions=false;
	
	public void showError(String message, Exception e) {
		this.log(e);
		this.showError(message);
	}
	
	public void showError(String message) {
		this.showError(message, "");
	}
	public void showError(String message, String debugInfo) {
		this.log("UserError.showError: "+message);
		this.log("debugInfo: "+debugInfo);
		this.printError(message, debugInfo);
	}
	
	public abstract void printError(String message, String debugInfo);
	
	public void log(String what, Priority priority) {
		String msg = priority+" "+what;
		this.logged.add(msg);
		//if (priority != Priority.LOW)
		System.out.println(msg);
		if (this.triggerExceptions && priority.toString().equals(Priority.HIGH.toString()))
			throw new RuntimeException("Error with High Priortiy! To switch this off, set triggerExpcetion property of UserError object.");
	}
	
	public void log(Object caller, String what, Priority priority) {
		this.logged.add(what);
		
		System.out.println(priority+" "+ caller.getClass()+" "+what);
		if (this.triggerExceptions && priority.equals(Priority.HIGH))
			throw new RuntimeException("Error with High Priortiy! To switch this off, set triggerExpcetion property of UserError object.");
	}
	
	public void log(String what) {
		this.logged.add(what);
		System.out.println(what);
	}
	
	public void log(Exception e) {
		this.log(e.toString());
		/*final ByteArrayOutputStream bs = new ByteArrayOutputStream();
		final PrintWriter w = new PrintWriter(bs);
		e.printStackTrace(w);
		e.printStackTrace();
		this.log("Exception: "+e.getClass().getName());
		this.log("Exception message: "+e.getMessage());
		this.log("Stack Trace: ");
		this.log(bs.toString());*/
	}
	
	public void log(Object o) {
		//this.log(new XStream().toXML(o));
	}
	
	public static UserError singelton;
	
	static {
		//singelton = new SwingUserError();
	
	}

	@Override
	protected void finalize() throws Throwable {
		//System.out.println()
		//for (int i=0;i<logged.size();i++) System.out.println(logged.get(i));
		super.finalize();
	}
	
	
	
}
