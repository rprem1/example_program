package at.qe.sepm.skeleton.validator;

import javax.annotation.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import at.qe.sepm.skeleton.model.Flight;
import at.qe.sepm.skeleton.services.AircraftService;
import at.qe.sepm.skeleton.services.FlightService;
import at.qe.sepm.skeleton.services.UserService;

@ManagedBean
@Scope("prototype")
public class FlightValidator {
	@Autowired
	private FlightService flightService;
	@Autowired
	private AircraftService aircraftService;
	@Autowired
	private UserService userService;
	
	private FlightValidator flight;
	
	public boolean validateFlight() {
		flight.validateTime();
		flight.validateCapacity();
		flightvalidatePilot();
		validateBoardCrew();
		return true;
	}

	private void validateBoardCrew() {
		
	}

	private void flightvalidatePilot() {
		
	}

	private void validateCapacity() {
		
	}

	private void validateTime() {
		
	}

	
	
	
	
}
