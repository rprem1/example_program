package at.qe.sepm.skeleton.ui.beans;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.RequestScoped;
import javax.faces.flow.FlowScoped;
import javax.swing.event.ChangeEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import at.qe.sepm.skeleton.model.Aircraft;
import at.qe.sepm.skeleton.model.Flight;
import at.qe.sepm.skeleton.repositories.FlightRepository;
import at.qe.sepm.skeleton.services.AircraftService;
import at.qe.sepm.skeleton.services.FlightService;
import at.qe.sepm.skeleton.ui.controllers.AircraftListController;
import at.qe.sepm.skeleton.ui.controllers.FlightCreationController;
import net.bytebuddy.asm.Advice.Local;

/**
 * This Bean is to get all available aircraft for flights
 */
@ManagedBean
@Scope("request")
public class AvailableAircraftBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private AircraftService aircraftService;
	
	@Autowired
	private FlightService flightService;
	
	
	private List<String> availableAircraftList;
	
	private List<String> availableAircraftListNow;
	
	public List<String> getAvailableAircraftListNow() {
		return availableAircraftListNow;
	}


	public void setAvailableAircraftListNow(List<String> availableAircraftListNow, Flight current) {
		
		List<Flight> allFlights = (List<Flight>) flightService.getAllFlights();
		Calendar currentFlightDay = Calendar.getInstance();
		currentFlightDay.setTime(current.getDepartureTime());
		
		Calendar allreadyInQue = Calendar.getInstance();
		
		for (Flight val: allFlights) {
			allreadyInQue.setTime(val.getDepartureTime());
			if(currentFlightDay.get(Calendar.DAY_OF_YEAR) == allreadyInQue.get(Calendar.DAY_OF_YEAR) && allreadyInQue.get(Calendar.YEAR) == currentFlightDay.get(Calendar.YEAR))
				availableAircraftListNow.remove(val.getScheduledAircraft().getAircraftId());
		}
		
		this.availableAircraftListNow = availableAircraftListNow;
	}

	@Autowired
	FlightCreationController flightCreation;
	
	@PostConstruct
	public void init() {
		availableAircraftList = new ArrayList<>();
		Collection<Aircraft> temp = new HashSet<>();
		temp.addAll(validate(aircraftService.getAllAircrafts()));
		for (Aircraft val : temp)
			availableAircraftList.add(val.getAircraftId());
		
		List<Flight> allFlights = (List<Flight>) flightService.getAllFlights();
		
		

		Collections.sort(availableAircraftList);
	}

	/**
	 * checks if a aircraft is already scheduled
	 * @param allAircrafts all aircrafts which should be checked
	 * @return returns all none scheduled aircrafts
	 */
	private Collection<? extends Aircraft> validate(Collection<Aircraft> allAircrafts) {
		//gives me all aircrafts back where the capacity is in range
		Collection<Aircraft> availableAircrafts = new HashSet<>();
		List<Flight> allFlights = new ArrayList<>();
		allFlights.addAll(flightService.getAllFlights());


		
		for (Aircraft aircraft : allAircrafts)
			if(!aircraft.isScheduled())
				availableAircrafts.add(aircraft);
		return availableAircrafts;
	}


	public AircraftService getAircraftService() {
		return aircraftService;
	}

	public void setAircraftService(AircraftService aircraftService) {
		this.aircraftService = aircraftService;
	}

	public List<String> getAvailableAircraftList() {
		return availableAircraftList;
	}

	public void setAvailableAircraftList(List<String> availableAircraftList) {
		this.availableAircraftList = availableAircraftList;
	}
	
    public void changeAircraft(ChangeEvent eg){
    	availableAircraftList.add("bbb");
    }
	
	
}
