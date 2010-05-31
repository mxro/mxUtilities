package de.mxro.utils.log.impl;

import de.mxro.utils.log.UserError;


public class CommandLineUserError extends UserError {

	@Override
	public void printError(String message, String debugInfo) {
		System.out.println("Error: "+message);
		System.out.println("Debug Info: "+debugInfo);
	}

}
