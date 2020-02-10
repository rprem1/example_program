package at.qe.sepm.skeleton.ui.controllers;

import at.qe.sepm.skeleton.model.Flight;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.services.FlightService;
import at.qe.sepm.skeleton.services.UserService;
import at.qe.sepm.skeleton.ui.beans.MessageBean;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the user detail view.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("view")
public class UserDetailController {

    @Autowired
    private UserService userService;

    @Autowired
    private FlightService flightService;
    
    @Autowired
    private RosterController rosterController;
    
    @Autowired
    private MessageBean messageBean;
    /**
     * Attribute to cache the currently displayed user
     */
    private User user;
    private Flight flight;

    /**
     * Sets the currently displayed user and reloads it form db. This user is
     * targeted by any further calls of
     * {@link #doReloadUser()}, {@link #doSaveUser()} and
     * {@link #doDeleteUser()}.
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
        doReloadUser();
    }

    /**
     * Returns the currently displayed user.
     *
     * @return
     */
    public User getUser() {
        return user;
    }




    /**
     * Action to force a reload of the currently displayed user.
     */
    public void doReloadUser() {
        user = userService.loadUser(user.getUsername());
    }

    /**
     * Action to save the currently displayed user.
     */
    public void doSaveUser() {

        user = this.userService.saveUser(user);
    }

    /**
     * Action to delete the currently displayed user.
     * @throws ParseException 
     */
    public void doDeleteUser() throws ParseException {
    	List<Flight> temp = (List<Flight>) flightService.getAllFlights();
		Flight saveTempFlight = null;
		boolean executedFlight = false;
		for (Flight flight : temp) {
			if(user.getJobTitle().contentEquals("Pilot")) {
				if(flight.getAssignedPilots().contains(user)) {
					flight.getAssignedPilots().remove(user);
					flight.setCurrentPersonal(flight.getCurrentPersonal()-1);
					flight.setIsValidFlight(false);
					executedFlight = true;
				}
			}
			if(user.getJobTitle().contentEquals("Board Crew")) {
				if(flight.getAssignedBoardpersonal().contains(user)) {
					flight.getAssignedBoardpersonal().remove(user);
					flight.setCurrentPersonal(flight.getCurrentPersonal()-1);
					flight.setIsValidFlight(false);
					 executedFlight = true;
				}
			}
			saveTempFlight = flight;
	    	saveTempFlight.setUpdateDate(new Date());
	    	
	    	this.flightService.hardSave(saveTempFlight);
		}
		if(executedFlight)
			messageBean.alertInformation("Info", "Flight got invalid!");
        this.userService.deleteUser(user);
        user = null;
    }

}
