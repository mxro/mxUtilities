package de.mxro.filesystem.ext;

import de.mxro.utils.Utils;
import de.mxro.utils.log.UserError;

public abstract class OpenTerminal {
	protected final String path;
	
	public static OpenTerminal newInstance(String path) {
		switch (Utils.getOperatingSystem()) {
			// case Utils.WINDOWS: return new WindowsOpenObject(url); 
			case Utils.MACOSX: return new MacOSXOpenTerminal(path);
			
			default: UserError.singelton.showError("Unsupported Operating System for opening terminal!"); break;
		}
		return null;
	}
	
	public String getPath() {
		return this.path;
	}

	public abstract boolean open();

	public OpenTerminal(final String path) {
		super();
		this.path = path;
	}
	
	
}
