package at.qe.sepm.skeleton.ui.controllers;

import at.qe.sepm.skeleton.model.Holiday;
import at.qe.sepm.skeleton.services.HolidayService;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the holiday detail view.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("view")
public class HolidayDetailController {

    @Autowired
    private HolidayService holidayService;

    /**
     * Attribute to cache the currently displayed holiday
     */
    private Holiday holiday;

    /**
     * Sets the currently displayed holiday and reloads it form db. This holiday is
     * targeted by any further calls of
     * {@link #doReloadHoliday()}, {@link #doSaveHoliday()} and
     * {@link #doDeleteHoliday()}.
     *
     * @param holiday
     */
    public void setHoliday(Holiday holiday) {
        this.holiday = holiday;
        doReloadHoliday();
    }

    /**
     * Returns the currently displayed holiday.
     *
     * @return
     */
    public Holiday getHoliday() {
        return holiday;
    }




    /**
     * Action to force a reload of the currently displayed holiday.
     */
    public void doReloadHoliday() {
        holiday = holidayService.loadHoliday(holiday.getId());
    }

    /**
     * Action to save the currently displayed holiday.
     * @throws ParseException 
     */
    public void doSaveHoliday() throws ParseException {

        holiday = this.holidayService.saveHoliday(holiday);
    }

    /**
     * Action to delete the currently displayed holiday.
     */
    public void doDeleteHoliday() {
        this.holidayService.deleteHoliday(holiday);
        holiday = null;
    }

}
