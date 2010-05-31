package de.mxro.utils.history;

import java.util.List;
import java.util.Vector;

public class History<P> {
	private List<P> pages;
	private int currentPage;
	private static final boolean DEBUG = false;
	
	public void jumpedTo(P page) {
		if (this.currentPage > 0) {
			//System.out.println("before: "+pages.size());
			this.pages = this.pages.subList(0, this.currentPage+1);
			//System.out.println("after: "+pages.size());
		}
		this.pages.add(page);
		this.currentPage++;
		if (DEBUG) {
			System.out.println("currentPage: "+this.currentPage);
			System.out.println("Page: "+page.toString());
			System.out.println("Pages: "+this.pages.size());
		}
	}
	
	public final P goBack() {
		if (this.backPossible()) {
			this.currentPage--;
			return this.pages.get(this.currentPage);
		} else
			return null;
	}
	
	public final P goForward() {
		if (this.forwardPossible()) {
			this.currentPage++;
			return this.pages.get(this.currentPage);
		} else
			return null;
	}
	
	public boolean forwardPossible() {
		return (this.currentPage+2) <= this.pages.size();
	}
	
	public boolean backPossible() {
		return this.currentPage > 0;
	}

	public History() {
		super();
		this.pages = new Vector<P>();
		this.currentPage = -1;
	}
	
}
