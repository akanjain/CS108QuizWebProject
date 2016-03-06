package web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClockTimeStamp {
	
	public static String getTimeStamp() {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd:HH.mm.ss").format(new Date());
		return timeStamp;
	}
	
	public static String getTimeStampMinusMin(int minutes) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd:HH.mm.ss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -(minutes));
		return dateFormat.format(cal.getTime());
	}
	
}
