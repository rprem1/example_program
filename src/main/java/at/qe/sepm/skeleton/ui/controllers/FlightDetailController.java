package at.qe.sepm.skeleton.ui.controllers;

import at.qe.sepm.skeleton.model.Aircraft;
import at.qe.sepm.skeleton.model.Flight;
import at.qe.sepm.skeleton.services.AircraftService;
import at.qe.sepm.skeleton.services.FlightService;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the flight detail view.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("view")
public class FlightDetailController{



    @Autowired
    private FlightService flightService;
    
    
    /**
     * Attribute to cache the currently displayed flight
     */
    private Flight flight;

    /**
     * Sets the currently displayed flight and reloads it form db. This flight is
     * targeted by any further calls of
     * {@link #doReloadFlight()}, {@link #doSaveFlight()} and
     * {@link #doDeleteFlight()}.
     *
     * @param flight
     */
    public void setFlight(Flight flight) {
        this.flight = flight;
        doReloadFlight();
    }

    /**
     * Returns the currently displayed flight.
     *
     * @return
     */
    public Flight getFlight() {
        return flight;
    }



    /**
     * Action to force a reload of the currently displayed flight.
     */
    public void doReloadFlight() {
        flight = flightService.loadFlight(flight.getFlightId());
    }

    /**
     * Action to save the currently displayed flight.
     * @throws ParseException 
     */
    public void doSaveFlight() throws ParseException {

        flight = this.flightService.saveFlight(flight);
    }

    /**
     * Action to delete the currently displayed flight.
     */
    public void doDeleteFlight() {
        this.flightService.deleteFlight(flight);
        flight = null;
    }
    
    public void doHardSaveFlight() {
        flight = this.flightService.editFlight(flight);
    }


}