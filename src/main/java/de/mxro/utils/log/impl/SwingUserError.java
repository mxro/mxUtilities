package de.mxro.utils.log.impl;

import de.mxro.utils.log.UserError;

public class SwingUserError extends UserError {


	@Override
	public void printError(String message, String debugInfo) {
		//new SwtUserErrorDialog(this, message, debugInfo);
		new SwingUserErrorDialog(true, null, this, message, debugInfo).setVisible(true);
	}

}
