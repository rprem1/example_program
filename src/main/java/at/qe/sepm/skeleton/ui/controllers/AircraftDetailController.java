package at.qe.sepm.skeleton.ui.controllers;

import at.qe.sepm.skeleton.model.Aircraft;
import at.qe.sepm.skeleton.model.Flight;
import at.qe.sepm.skeleton.services.AircraftService;
import at.qe.sepm.skeleton.services.FlightService;
import at.qe.sepm.skeleton.ui.beans.MessageBean;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * Controller for the aircraft detail view
 **/

@Component
@Scope("view")
public class AircraftDetailController {

    @Autowired
    private AircraftService aircraftService;
    
    @Autowired
    private FlightService flightService;
    
    @Autowired
    private MessageBean messageBean;
    /**
     * Attribute to cache the currently displayed aircraft
     */
    private Aircraft aircraft;
    private Flight flight;

    /**
     * Sets the currently displayed aircraft and reloads it form db. This aircraft is
     * targeted by any further calls of
     * {@link #doReloadAircraft()}, {@link #doSaveAircraft()} and
     * {@link #doDeleteAircraft()}.
     *
     * @param aircraft
     */
    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
        doReloadAircraft();
    }

    /**
     * Returns the currently displayed aircraft.
     *
     * @return
     */
    public Aircraft getAircraft() {
        return aircraft;
    }

    /**
     * Action to force a reload of the currently displayed aircraft.
     */
    public void doReloadAircraft() {
        aircraft = aircraftService.loadAircraft(aircraft.getAircraftId());
    }

    /**
     * Action to save the currently displayed aircraft.
     */
    public void doSaveAircraft() {

        aircraft = this.aircraftService.saveAircraft(aircraft);
    }

    /**
     * Action to delete the currently displayed aircraft.
     * @throws ParseException 
     */
    public void doDeleteAircraft() throws ParseException {
    		List<Flight> temp = (List<Flight>) flightService.getAllFlights();
    		Flight saveTempFlight = null;
    		boolean executedFlight = false;
    		for (Flight flight : temp) {
				if(flight.getScheduledAircraftId() == aircraft.getAircraftId()) {
					saveTempFlight = flight;
					flightService.hardDelete(flight);
		    		saveTempFlight.setUpdateDate(new Date());
		    		saveTempFlight.setScheduledAircraft(null);
		    		saveTempFlight.setScheduledAircraftId(null);
		    		saveTempFlight.setIsValidFlight(false);;
		    		executedFlight = true;
		    		flightService.hardSave(saveTempFlight);
				}
    		}
    		if(executedFlight)
    			messageBean.alertInformation("Info", "Flight got invalid!");
    		this.aircraftService.deleteAircraft(aircraft);
        aircraft = null;
    }

}