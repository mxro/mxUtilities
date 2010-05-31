package de.mxro.filesystem.ext;

import java.io.IOException;

import de.mxro.filesystem.File;
import de.mxro.utils.Mxro;
import de.mxro.utils.URIImpl;
import de.mxro.utils.log.UserError;

public class MacOSXOpenTerminal extends OpenTerminal {

	@Override
	public boolean open() {
		File openterminal;
		
			openterminal = Mxro.getUtilityAppsFolder().getFile(URIImpl.create("openterminal.scpt"));
		
		if (openterminal == null) {
			
			UserError.singelton.log("MacOSXOpenTerminal.open: openterminal.scpt does not exist in application directory: "+new java.io.File("").getAbsolutePath());
			return false;
		}
		try {
			// UserError.singelton.log(getPath());
			final String[] cmds = {"osascript", openterminal.makeLocal().getAbsolutePath(), this.getPath()};
			final Process p = Runtime.getRuntime().exec(cmds);
			assert p == p; // to aviod the warning
			/*java.io.InputStream is = p.getErrorStream();
			java.io.ByteArrayOutputStream bs = new java.io.ByteArrayOutputStream();
			System.out.println(bs.toString());*/
		} catch (final IOException e) {
			return false;
		}
		return true;
	}

	public MacOSXOpenTerminal(String path) {
		super(path);
	}
	
}
