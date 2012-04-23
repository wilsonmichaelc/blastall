package org.blastall.blast.main;

import org.apache.log4j.Logger;

public class RunWithoutTimer {

	private static Logger log = Logger.getLogger(RunWithoutTimer.class);

	public static void main(String[] args) throws Throwable {

		// Get an instance of the main class
		Main main = null;

		// Try to run the main class
		try {

			main = new Main();

			// Keep running until we get an explicit 'stop' command.
			while(!(main.run()).equals("stop")){;}


		 // make sure to log a general error when the main class fails.
		}catch(Throwable t){

			log.error("General error. Failed to run Main class.", t);
			throw t;
		}

	}

}
