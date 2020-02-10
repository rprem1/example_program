package at.qe.sepm.skeleton.ui.controllers;

import at.qe.sepm.skeleton.model.Aircraft;
import at.qe.sepm.skeleton.services.AircraftService;
import at.qe.sepm.skeleton.ui.beans.MessageBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 Controller for the creation of aircrafts
 **/
@Component
@Scope("view")
public class AircraftCreationController{

    @Autowired
    private AircraftService aircraftService;
    
    @Autowired
    private MessageBean messageBean;

    /**
     * Creates a new aircraft
     */
    private Aircraft aircraft = new Aircraft();


    public boolean setNewAircraft(){

        this.aircraft.setAircraftId("");
        this.aircraft.setAircraftType("");
        this.aircraft.setRequiredPilotsAircraft(0);
        this.aircraft.setRequiredBoardpersonalAircraft(0);
        this.aircraft.setRequiredBoardpersonalAircraft(0);
        this.aircraft.setCapacityAircraft(0);


        return true;

    }

    /**
     * Returns the currently displayed aircraft
     * @return
     */
    public Aircraft getAircraft(){ return aircraft;}

    /**
     * Action to save the currently displayed user.
     */
    public void doSaveAircraft() {
        aircraft = this.aircraftService.saveAircraft(aircraft);
    }




}