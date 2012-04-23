package org.blastall.blast.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class RunWithTimer {

	private static Logger log = Logger.getLogger(RunWithTimer.class);

	public static void main(String[] args) throws Throwable{

		// Some things we will need.
		Main main = null;
		Date now = null;
		
		// 
		try {
			while(true){

				main = new Main();
				now = new Date();

				// get the current hour
				int hour = Integer.parseInt( new SimpleDateFormat("HH").format(now) );

				// if the time is between the hours of 7AM and 6PM, then sleep the correct amount of time
				if( (hour >= 7) && (hour <= 18) ){

					if(log.isDebugEnabled()){ log.debug("Time to sleep!!"); }
					if(hour < 7){
						Thread.currentThread();
						Thread.sleep(60000 * (7-hour));
					} else {
						Thread.currentThread();
						Thread.sleep(60000 * (18-hour));
					}

				} 
				// Everyone left for the day, lets go ahead and run BLAST
				else {

					if(log.isDebugEnabled()){ log.debug("Time to work!!"); }
					while(!(main.run().equals("stop"))){
						hour = Integer.parseInt( new SimpleDateFormat("HH").format(now = new Date()) );
					}

				}

			}
		} catch(Throwable t){
			log.error("General error, failed to run.", t);
			throw t;
		}
	}

}
