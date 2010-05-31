package de.mxro.filesystem.ext;

import de.mxro.utils.Utils;
import de.mxro.utils.log.UserError;

public abstract class OpenObject {
	
	protected final String path;
	
	public static OpenObject newInstance(String url) {
		switch (Utils.getOperatingSystem()) {
			case Utils.WINDOWS: return new WindowsOpenObject(url); 
			case Utils.MACOSX: return new MacOSXOpenObject(url);
			
			default: UserError.singelton.showError("Unsupported Operating System!"); break;
		}
		return null;
	}
	
	public OpenObject(String path) {
		super();
		this.path = path;
	}
	
	public abstract boolean open();

	public String getPath() {
		return this.path;
	}
}
