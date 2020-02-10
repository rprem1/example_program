package at.qe.sepm.skeleton.ui.controllers;

import at.qe.sepm.skeleton.model.Holiday;
import at.qe.sepm.skeleton.services.HolidayService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the holiday list view.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("view")
public class HolidayListController {

    @Autowired
    private HolidayService holidayService;

    /**
     * Returns a list of all holidays.
     *
     * @return
     */
    public Collection<Holiday> getHolidays() {
        return holidayService.getAllHolidays();
    }

    /**
     * Fetches the holidays for a certain user
     * @param username the user to fetch the holidays from
     * @return returns a Collection of all holidays of the user
     */
    public Collection<Holiday> getHolidayByUser(String username){

        return holidayService.getHolidayByUser(username);
    }




}
