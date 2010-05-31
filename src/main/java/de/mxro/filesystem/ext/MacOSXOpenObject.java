package de.mxro.filesystem.ext;

import java.io.IOException;


public class MacOSXOpenObject extends OpenObject {

	
	
	public MacOSXOpenObject(String path) {
		super(path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean open() {
		//File openobject;
		
			//openobject = Mxro.utilityAppsFolder.getFile(URI.create("openobject.scpt"));
			//if (openobject == null) {
			//	UserError.singelton.log("MacOSXOpenObject.open: openobject.scpt does not exist in application directory: "+Mxro.utilityAppsFolder.getURI());
				//return false;
			//}
		
		try {
			// UserError.singelton.log("open "+this.getPath()+"");
			final String[] cmds = {"open", this.getPath()};
			final Process p = Runtime.getRuntime().exec(cmds);
			//Runtime.getRuntime().
			//Process p = Runtime.getRuntime().exec("osascript \""+openobject.makeLocal().getAbsolutePath()+"\" \""+this.getPath()+"\"");
			assert p == p; // to aviod the warning
			//final java.io.InputStream is = p.getErrorStream();
			//final java.io.ByteArrayOutputStream bs = new java.io.ByteArrayOutputStream();
			//System.out.println(bs.toString());
		} catch (final IOException e) {
			return false;
		}
		return true;
	}

}
