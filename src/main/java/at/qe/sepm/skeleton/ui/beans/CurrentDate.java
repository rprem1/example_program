package at.qe.sepm.skeleton.ui.beans;

import java.util.Calendar;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Returns the current Date
 */
@Component
@Scope("request")
public class CurrentDate {
	public Date getCurrentDate() {
		return (new Date());
	}
	
	public Date getCurrentDateArr() {
	    Calendar calendar = Calendar.getInstance();
	    Date date = new Date();
		calendar.setTime(date);
	    calendar.add(Calendar.HOUR_OF_DAY, 1);
	    return calendar.getTime();
	}
	
	public Date getCurrentDateMaxArr(Date date) {
	    Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
	    calendar.add(Calendar.HOUR_OF_DAY, 11);
	    return calendar.getTime();
	}
	
	private Date temp;

	public Date getTemp() {
		return temp;
	}

	public void setTemp(Date temp) {
		this.temp = temp;
	}
	
	
	
	
}
