package de.mxro.utils.drm;

import java.util.Vector;

public class ChangeQueue<R> {
	
	public class Item {
		public final Change<R> change;
		public final boolean undo;
		public final String resourceuri;
		
		public Item( String resourceuri, Change<R> change, boolean undo) {
			super();
			this.change = change;
			this.undo = undo;
			this.resourceuri = resourceuri;
		}
		
		
	}
	
	private final Vector<Item> items;

	public void addChange(String resourceuri, Change<R> change, boolean undo) {
		this.items.add(new Item(resourceuri, change, undo));
	}
	
	public Vector<Item> getItems() {
		return this.items;
	}
	
	public ChangeQueue() {
		super();
		this.items = new Vector<Item>();
	}
	
	
}
