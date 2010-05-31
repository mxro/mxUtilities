package de.mxro.utils.background;

import java.util.Vector;

public class Background {
	
	private class Workhorse extends Thread {

		private final Vector<Activity> activities;
		
		
		@Override
		public void run() {
			super.run();
			while (this.activities.size() > 0) {
				//UserError.singelton.log("Background: Start of Activity: "+this.activities.firstElement().getClass().getName(), UserError.Priority.LOW);
				this.activities.firstElement().run();
				//UserError.singelton.log("Background: End of Activity: "+this.activities.firstElement().getClass().getName(), UserError.Priority.LOW);
				//System.out.println(activities.size());
				this.activities.remove(this.activities.firstElement());
				//System.out.println("a"+activities.size());
			}
			Background.this.addActivity(null);
		}

		public Workhorse(final Vector<Activity> activities) {
			super();
			this.setPriority(Thread.MIN_PRIORITY);
			this.activities = new Vector<Activity>();
			this.activities.addAll(activities);
		}
		
		public synchronized void  addActivity(Activity a) {
			this.activities.add(a);
		}
		
	}
	
	private final Vector<Activity> activities;
	private Workhorse workhorse;
	
	
	public synchronized void addActivity(Activity a) {
		if (a == null) {
			this.workhorse = null;
			return;
		}
		
		if (this.workhorse == null) {
			this.activities.add(a);
			this.workhorse = new Workhorse(this.activities);
			this.activities.removeAllElements();
		} else {
			this.workhorse.addActivity(a);
		}
		if (this.workhorse.getState().equals(Thread.State.NEW)) {
			this.workhorse.start();
		}
	}
	
	public Background() {
		super();
		this.activities = new Vector<Activity>();
		this.workhorse = null;
	}

	
	
}
