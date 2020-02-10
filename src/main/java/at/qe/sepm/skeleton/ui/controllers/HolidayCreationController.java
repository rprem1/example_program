package at.qe.sepm.skeleton.ui.controllers;

import at.qe.sepm.skeleton.model.Holiday;
import at.qe.sepm.skeleton.services.HolidayService;
import at.qe.sepm.skeleton.ui.beans.MessageBean;
import at.qe.sepm.skeleton.ui.beans.SessionInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.ParseException;
import java.util.Date;




/**
 Controller for the creation of holidays
 **/

@Component
@Scope("view")
public class HolidayCreationController{

    @Autowired
    private HolidayService holidayService;
    
    @Autowired
    MessageBean messageBean;



    /**
     * Creates a new holiday
     */
    private Holiday holiday = new Holiday();
    private SessionInfoBean sessionInfoBean;

    public boolean setNewHoliday() {

        this.holiday.setUsername(sessionInfoBean.getCurrentUserName());
        this.holiday.setHolidayFrom(null);
        this.holiday.setHolidayUntil(null);


        return true;

    }

    /**
     * Returns the currently displayed holiday
     * @return
     */
    public Holiday getHoliday(){ return holiday;}

    /**
     * Action to save the currently displayed holiday
     * @throws ParseException 
     */
    public String doSaveHoliday() throws ParseException{
        holiday = this.holidayService.saveHoliday(holiday);
        if(holiday == null)
        	messageBean.alertInformation("Error", "Holiday was not created!");
        return "viewid?faces-redirect=true";
    }


}