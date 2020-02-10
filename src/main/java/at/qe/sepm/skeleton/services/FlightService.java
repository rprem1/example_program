	package at.qe.sepm.skeleton.services;

import at.qe.sepm.skeleton.model.Aircraft;
import at.qe.sepm.skeleton.model.AuditLog;
import at.qe.sepm.skeleton.model.Flight;
import at.qe.sepm.skeleton.model.Holiday;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.repositories.AircraftRepository;
import at.qe.sepm.skeleton.repositories.AuditLogRepository;
import at.qe.sepm.skeleton.repositories.FlightRepository;
import at.qe.sepm.skeleton.repositories.UserRepository;
import at.qe.sepm.skeleton.ui.beans.AvailableAircraftBean;
import at.qe.sepm.skeleton.ui.beans.MessageBean;
import at.qe.sepm.skeleton.ui.controllers.FlightDetailController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating flight data.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("application")
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;
    
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    @Autowired
    private AircraftRepository aircraftRepository;
    
    @Autowired
    private HolidayService holidayService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AircraftService aircraftService;
    
    
    @Autowired
    private MessageBean messageBean;

    /**
     * Returns a collection of all flights.
     *
     * @return
     */
    public Collection<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
    /**
     * Loads a single flight identified by its flightId.
     *
     * @param flightId the flightId to search for
     * @return the flight with the given flightname
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Flight loadFlight(String flightId) {
        return flightRepository.findFirstByFlightId(flightId);
    }
    /**
     * Saves the flight. This method will also set {@link Flight#createDate} for new
     * entities or {@link Flight#updateDate} for updated entities. The flight
     * requesting this operation will also be stored as {@link Flight#createDate}
     * or {@link Flight#updateFlight} respectively.
     *
     * @param flight the flight to save
     * @return the updated flight
     */
    
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public int getRandomNumber(int lengthOfPersonalList) {
    	Random randomGenerator = new Random();
    	int pickElement = randomGenerator.nextInt(lengthOfPersonalList);
    	return pickElement;
    }
    
    public Date parseStringdateToDate(String date) throws ParseException {
    	return (new SimpleDateFormat("yyyy-mm-dd").parse(date));    
    }

	/**
	 * Returns, if the Date is between two Dates
	 */
    public boolean betweenDate(Date start, Date end, Date depTime, Date arrTime, Flight flight) {
    	List<Date> holidayDayList = new ArrayList<>();
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		
		Calendar dep = Calendar.getInstance();
		dep.setTime(depTime);
		Calendar arr = Calendar.getInstance();
		arr.setTime(arrTime);
		
		from.setTime(start);
		to.setTime(end);
		
		while(from.before(to)) {
			holidayDayList.add(from.getTime());
			from.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		Calendar personalIsAgainHomeAirport = Calendar.getInstance();
		long persHomeInMilli = arrTime.getTime();
		persHomeInMilli += 7200000;
		persHomeInMilli += flight.getFlightTimeInMilli();
		personalIsAgainHomeAirport.setTimeInMillis(persHomeInMilli);
		
		Date personalHomeTime = personalIsAgainHomeAirport.getTime();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		for (Date holi : holidayDayList) {
			if(fmt.format(holi).equals(fmt.format(depTime)) || 
					fmt.format(holi).equals(fmt.format(arrTime)) || 
						fmt.format(holi).equals(fmt.format(personalHomeTime))) {
				return true;
			}
		}
    	
    	return false;
    }


	/**
	 * gets used by the assign personal function
	 */
    public Collection<User> assignPersonal(Collection<User> personal, int requiredPersonal, Flight flight) throws ParseException {
    	Date flightDeparture = flight.getDepartureTime();
    	Date flightArrival = flight.getArrivalTime();
    	List<User> availablePersonal = new ArrayList<>();
    	List<User> executingPersonal = new ArrayList<>();
    	double tempHoursWorkedWeek;
    	
    	Collection<Flight> allFlights = getAllFlights();
    	double hoursWorked = 0;
    	
    	for (User currentUser : personal) {
    		Collection<Holiday> listOfHolidays = holidayService.getHolidayByUser(currentUser.getUsername());
    		boolean userHasHolidayOnFlight = false;
    		
    		if(listOfHolidays.isEmpty()) {
    			userHasHolidayOnFlight = false;
    		}
    		else {
    			for (Holiday holiday : listOfHolidays) {
    				if(betweenDate(parseStringdateToDate(holiday.getHolidayFrom()), parseStringdateToDate(holiday.getHolidayUntil()), flightDeparture, flightArrival, flight))
    					userHasHolidayOnFlight = true;
					}
    		}
    		List<Date> weekOfInterest = currentUser.getWeekOfInterest(flight.getDepartureTime(), flight.getArrivalTime());
    		List<Flight> flightsBetween = flightRepository.findFlightsBetween(weekOfInterest.get(0), weekOfInterest.get(1));
    		hoursWorked = 0;
    		boolean isBetweenFlightBreak = false;
    		if(!(flightsBetween.isEmpty())) {
    			isBetweenFlightBreak = currentUser.calculateBreak(flightsBetween, flight);
	    		for (Flight val : flightsBetween) {
	    				if(val.getAssignedBoardpersonal().contains(currentUser))
	    					hoursWorked += val.getFlightTimeInHours()*2+12;
	    				if(val.getAssignedPilots().contains(currentUser))
	    					hoursWorked += val.getFlightTimeInHours()*2+12;
				}
	    		if(currentUser.getAvailable(isBetweenFlightBreak, currentUser.calculateHoursWithNewFlight(hoursWorked,flight.getFlightTimeInHours()), !(userHasHolidayOnFlight)))
	    			availablePersonal.add(currentUser);
    		}
    		else {
    			if(!(userHasHolidayOnFlight))
    					availablePersonal.add(currentUser);
	    	}
    	}

    	if(availablePersonal.size() < requiredPersonal) {
    		return null;
    	}
    	else {
			while (requiredPersonal > 0) {
				int randNum = getRandomNumber(availablePersonal.size());
				executingPersonal.add(availablePersonal.get(randNum));
				availablePersonal.remove(randNum);
				--requiredPersonal;
			}
		}
    	return executingPersonal;
    }


	/**
	 * Assign personal to a flight
	 * @throws ParseException
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public boolean assignPersonalToFlight(Flight flight) throws ParseException {
    	Collection<User> boardcrew = userService.getBoardpersonal();
    	Collection<User> pilots = userService.getAllPilots();
    	Set<User> pilotsExecuting = new HashSet<>();
    	Set<User> boardcrewExecuting = new HashSet<>();
    	
    	if(!boardcrew.isEmpty())
    		boardcrewExecuting.addAll(assignPersonal(boardcrew, flight.getScheduledAircraft().getRequiredBoardpersonalAircraft(), flight));
    	
    	if(boardcrewExecuting.isEmpty()) {
    		messageBean.alertError("Error", "No Boardcrew for flight " + flight.getFlightId() + " !");
    		return false;
    	}
    	
    	if(!pilots.isEmpty())
    		pilotsExecuting.addAll(assignPersonal(pilots, flight.getScheduledAircraft().getRequiredPilotsAircraft(), flight));
    	
    	if(pilotsExecuting.isEmpty()) {
    		messageBean.alertError("Error", "No Pilots for flight " + flight.getFlightId() + " !");
    		return false;
    	}
    	
    	flight.setAssignedBoardpersonal(boardcrewExecuting);
    	flight.setAssignedPilots(pilotsExecuting);

    	EmailService emailService = new EmailService();
    	emailService.sendMailToCrew(pilotsExecuting,boardcrewExecuting,flight);
    	return true;
    }
    
    public void callReturnFlight(Flight returnFlight) {
    	returnFlight.setFlightId(returnFlight.getFlightId() + " Flight back");
    	String tempIataTo = returnFlight.getIataFrom();
    	returnFlight.setIataFrom(returnFlight.getIataTo());
    	returnFlight.setIataTo(tempIataTo);
    	
    	flightRepository.save(returnFlight);
    }
    
    private List<String> availableAircraftList;
    
	public void init() {
		availableAircraftList = new ArrayList<>();
		Collection<Aircraft> tempAllAir = aircraftRepository.findAll();
		Collection<Flight> tempAllFlights = flightRepository.findAll();
		
		for (Flight fl: tempAllFlights) {
			for (Aircraft val : tempAllAir) {
				if(fl.getScheduledAircraft().equals(val))
					continue;
				availableAircraftList.add(val.getAircraftId());
			}
		}
	}

    
    public List<String> getAvailableAircraftList() {
		return availableAircraftList;
	}
	public void setAvailableAircraftList(List<String> availableAircraftList) {
		this.availableAircraftList = availableAircraftList;
	}

	/**
	 * saves the flight
	 * @param flight
	 * @return returns the saved flight
	 * @throws ParseException
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Flight saveFlight(Flight flight) throws ParseException {
        AuditLog auditlog = new AuditLog();
        auditlog.setDate(new Date());
    	List<String> flightIds = new ArrayList<>();
    	for (Flight val: flightRepository.findAll()) {
			flightIds.add(val.getFlightId());
		}
        if (flight.isNew()) {
            flight.setCreateDate(new Date());
            flight.setDateFlight(flight.getDepartureTime());
            flight.setFlightTime();
            flight.setScheduledAircraft(aircraftService.loadAircraft(flight.getScheduledAircraftId()));
            boolean setInputInvalid = false;
            if(flight.getFlightId() == null) {
            	messageBean.alertError("Error", "Please enter FlightId!");
            	setInputInvalid = true;
            }
            if(flightIds.contains(flight.getFlightId())) {
            	messageBean.alertError("Error", "FlightId already in Use!");
            	setInputInvalid = true;
            }
            if(flight.getDepartureTime().after(flight.getArrivalTime())) {
            	messageBean.alertError("Error", "Flight departure time is greater than arrivaltime!");
            	setInputInvalid = true;
            }
            if(flight.getIataFrom().equals(flight.getIataTo())) {
            	messageBean.alertError("Error", "Flight IATAs are the same!");
            setInputInvalid = true;
            }
            if(flight.getFlightTimeInMilli() > (3600000*12)) {
            	messageBean.alertError("Error", "Flight time is more than 12 hours!");
            setInputInvalid = true;
            }
            if(flight.getNumberOfPassengers() > flight.getScheduledAircraft().getCapacityAircraft()) {
            	messageBean.alertError("Error", "Aircraft has not the capacity!");
         		setInputInvalid = true;
            }
            if(setInputInvalid)
            	return null;
            if(assignPersonalToFlight(flight)) {
            	flight.setIsValidFlight(true);
            }

        } else {
            flight.setUpdateDate(new Date());
            flight.setUpdateFlight(getAuthenticatedUser());
        }

        if(flight.getIsValidFlight())
        	messageBean.alertInformation("Success", "Flight was successfully created!");
        else {
        	messageBean.alertError("Error", "Flight was not created!");
        	return null;
        }
        RequestContext.getCurrentInstance().execute("PF('flightCreationDialogPickAircraft').hide()");
        RequestContext.getCurrentInstance().execute("PF('flightCreationDialog').hide()");
        if(flight.getIsValidFlight()) {
        	flight.setRequiredPersonal(flight.getScheduledAircraft().getRequiredBoardpersonalAircraft() + flight.getScheduledAircraft().getRequiredPilotsAircraft());
        	flight.setCurrentPersonal(flight.getRequiredPersonal());
        	flightRepository.save(flight);
        	Flight returnFlight = flight;
        	long flightBackArrivalTime = flight.getArrivalTime().getTime() - flight.getDepartureTime().getTime();
        	returnFlight.setFlightId(flight.getFlightId() + " 101");
        	String flightBackIata = flight.getIataFrom(); 
        	returnFlight.setIataFrom(flight.getIataTo());
        	returnFlight.setIataTo(flightBackIata);
        	returnFlight.setCurrentPersonal(flight.getCurrentPersonal());
        	returnFlight.setRequiredPersonal(flight.getRequiredPersonal());
        	Calendar flightBackTime = Calendar.getInstance();
        	flightBackTime.setTime(flight.getArrivalTime());
        	flightBackTime.add(Calendar.HOUR_OF_DAY, 12);
        	returnFlight.setDepartureTime(flightBackTime.getTime());
        	returnFlight.setArrivalTime(new Date(flightBackArrivalTime + returnFlight.getDepartureTime().getTime()));
        	returnFlight.setIsValidFlight(true);
        	returnFlight.setNumberOfPassengers(0);
            auditlog.setMessage("Flight " + flight.getFlightId()+ " was created.");
            auditLogRepository.save(auditlog);
        	return flightRepository.save(returnFlight);
        }
        else
        	return null;
	}

    /**
     * Deletes the flight.
     *
     * @param flight the flight to delete
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public void deleteFlight(Flight flight) {
        AuditLog auditlog = new AuditLog();
        auditlog.setDate(new Date());
        auditlog.setMessage("Flight " + flight.getFlightId() + " was deleted.");
        auditLogRepository.save(auditlog);
        
    	messageBean.alertInformation("Success", "Deleted flight " + flight.getFlightId() + "!");
    	String tempPostFix = flight.getFlightId()+" 101";
    	String tempPreFix = flight.getFlightId();
    	String fixed = tempPreFix.replace(" 101", "");
    	flightRepository.delete(flight);
    	List<Flight> allFlights = (List<Flight>) getAllFlights();
    	for (Flight val: allFlights) {
			if(val.getId().contentEquals(tempPostFix) || val.getId().contentEquals(tempPreFix) || val.getId().contentEquals(fixed))
				flightRepository.delete(val);
		}
    }

    private Flight getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return flightRepository.findFirstByFlightId(auth.getName());
    }
    
    public void errorMessage(String errorMsg, FacesMessage.Severity severity) {
    	FacesMessage msg = new FacesMessage(severity, errorMsg, null);
    	FacesContext.getCurrentInstance().addMessage(errorMsg, msg);
    }
    
    public void hardSave(Flight flight) {
    	flightRepository.save(flight);
    }
    
    public void hardDelete(Flight flight) {
    	flightRepository.delete(flight);
    }
    
    public Flight editFlight(Flight flight) {
    	messageBean.alertInformation("Success", "Edited flight " + flight.getFlightId() + "!");
    	return flightRepository.save(flight);
    }
    
}
