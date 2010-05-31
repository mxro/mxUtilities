package de.mxro.filesystem.ext;

import java.io.IOException;

import de.mxro.filesystem.File;
import de.mxro.utils.Mxro;
import de.mxro.utils.URIImpl;
import de.mxro.utils.log.UserError;

public class WindowsOpenObject extends OpenObject {
	public WindowsOpenObject(String path)  {
		super(path);	
	}
	
	@Override
	public boolean open() {
		try {
			final File file = Mxro.getUtilityAppsFolder().getFile(URIImpl.create("OpenURL.exe"));
			if (file != null) {
				String execString = file.getURI().getFile().getAbsolutePath()+ " \""+this.getPath()+"\"";
				final Process p = Runtime.getRuntime().exec(execString);
				assert p == p; 
				return true;
			}
			
			if (file == null) {
				UserError.singelton.log(this, "open: could not find program 'OpenURL.exe' to execute in '"+Mxro.getUtilityAppsFolder().getURI()+"'", UserError.Priority.HIGH);
			}
			
			if (!new java.io.File("OpenURL.exe").exists()) {
				de.mxro.utils.log.UserError.singelton.log(this, "No file OpenURL.exe in  "+new java.io.File("").getAbsolutePath(), UserError.Priority.HIGH);
				return false;
			}
			final Process p = Runtime.getRuntime().exec("OpenURL.exe \""+this.getPath()+"\"");
			assert p == p; // to avoid warning
		} catch (final IOException e) {
			return false;
		}
		return true;
	}
}
