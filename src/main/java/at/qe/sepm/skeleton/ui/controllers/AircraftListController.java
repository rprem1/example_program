package at.qe.sepm.skeleton.ui.controllers;

import at.qe.sepm.skeleton.model.Aircraft;
import at.qe.sepm.skeleton.services.AircraftService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the aircraft list view.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("view")
public class AircraftListController {

    @Autowired
    private AircraftService aircraftService;

    /**
     * Returns a list of all aircrafts.
     *
     * @return
     */
    public Collection<Aircraft> getAircrafts() {
        return aircraftService.getAllAircrafts();
    }

}
