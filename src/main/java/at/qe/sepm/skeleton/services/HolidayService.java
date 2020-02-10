package at.qe.sepm.skeleton.services;


import at.qe.sepm.skeleton.model.AuditLog;
import at.qe.sepm.skeleton.model.Flight;
import at.qe.sepm.skeleton.model.Holiday;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.repositories.AuditLogRepository;
import at.qe.sepm.skeleton.repositories.FlightRepository;
import at.qe.sepm.skeleton.repositories.HolidayRepository;
import at.qe.sepm.skeleton.ui.beans.MessageBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating holiday data.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("application")
public class HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;
    
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    @Autowired
    private MessageBean messageBean;
    
    @Autowired
    private FlightRepository flightRepository;

    


    /**
     * method to get all holidays
     *
     * @return Returns a collection of all holidays.
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Collection<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    /**
     * method to get the holidays of a single user
     * @param username the username of the user
     * @return returns the holidays of this user
     */
    public Collection <Holiday> getHolidayByUser(String username){return holidayRepository.findByUsername(username);}



    /**
     * Loads a single holiday identified by its holidayname.
     *
     * @param holidayname the holidayname to search for
     * @return the holiday with the given holidayname
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE') or hasAuthority('MANAGER')")
    public Holiday loadHoliday(Long id) {
        return holidayRepository.findFirstById(id);
    }




    /**
     * Saves the holiday. This method will also set {@link Holiday#createDate} for new
     * entities or {@link Holiday#updateDate} for updated entities. The holiday
     * requesting this operation will also be stored as {@link Holiday#createDate}
     * or {@link Holiday#updateHoliday} respectively.
     *
     * @param holiday the holiday to save
     * @return the updated holiday
     * @throws ParseException 
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE') or hasAuthority('MANAGER')")
    public Holiday saveHoliday(Holiday holiday) throws ParseException {
    	AuditLog auditlog = new AuditLog();
        auditlog.setDate(new Date());
        
        if (holiday.isNew()) {
            holiday.setCreateDate(new Date());
            auditlog.setMessage("Holiday from " + holiday.getHolidayFrom() + " until " + holiday.getHolidayUntil() + " for " + holiday.getUsername() +" was created.");
        } else {
            holiday.setUpdateDate(new Date());
            auditlog.setMessage("Holiday from " + holiday.getHolidayFrom() + " until " + holiday.getHolidayUntil() +" for " + holiday.getUsername() + " was updated.");
        }
        if(isAlreadyScheduled(holiday)) {
        	messageBean.alertInformation("Error", "Holiday was not created!");
        	return null;
        }
        if(holiday.isNew())
        	messageBean.alertInformation("Info", "Holiday was created!");
        else
        	messageBean.alertInformation("Info", "Holiday was updated!");
        
        auditLogRepository.save(auditlog);
        return holidayRepository.save(holiday);
    }
    
    
    /**
     * checks if the requesting user is already scheduled for a flight
     * at the requested holidays
     * @throws ParseException 
     */
    public boolean isAlreadyScheduled(Holiday holiday) throws ParseException {
		List<Flight> allFlights = flightRepository.findAll();
		List<Flight> listOfInteresst = new ArrayList<>();
		for (Flight flight : allFlights) {
			for (User boardpers: flight.getAssignedBoardpersonal()) {
				if(boardpers.getUsername().equals(holiday.getUsername())) {
					listOfInteresst.add(flight);
					break;
				}
			}
			for (User pilots: flight.getAssignedPilots()) {
				if(pilots.getUsername().equals(holiday.getUsername())) {
					listOfInteresst.add(flight);
					break;
				}
			}
		}
		List<Date> holidays = new ArrayList<>();
		Date from = new SimpleDateFormat("yyyy-MM-dd").parse(holiday.getHolidayFrom());
		Date to = new SimpleDateFormat("yyyy-MM-dd").parse(holiday.getHolidayUntil());
		
		while(from.before(to)) {
			holidays.add(from);
			Calendar tmp = Calendar.getInstance();
			tmp.setTime(from);
			tmp.add(Calendar.HOUR_OF_DAY, 24);
			from = tmp.getTime();
		}
		
		for (Flight val: listOfInteresst) {
			Calendar flightdep = Calendar.getInstance();
			Calendar flightarr = Calendar.getInstance();
			flightdep.setTime(val.getDepartureTime());
			flightarr.setTime(val.getArrivalTime());
			for (Date holi: holidays) {
				Calendar holidayCal = Calendar.getInstance();
				holidayCal.setTime(holi);
				if(flightdep.get(Calendar.DAY_OF_YEAR) == holidayCal.get(Calendar.DAY_OF_YEAR) &&
                  flightdep.get(Calendar.YEAR) == holidayCal.get(Calendar.YEAR))
					return true;
				if(flightarr.get(Calendar.DAY_OF_YEAR) == holidayCal.get(Calendar.DAY_OF_YEAR) &&
		                  flightarr.get(Calendar.YEAR) == holidayCal.get(Calendar.YEAR))
					return true;
			}
		}
    	return false;
    }


    /**
     * Deletes the holiday.
     *
     * @param holiday the holiday to delete
     */
    public void deleteHoliday(Holiday holiday) {
    	AuditLog auditlog = new AuditLog();
        auditlog.setDate(new Date());
        auditlog.setMessage("Holiday from " + holiday.getHolidayFrom() + " until " + holiday.getHolidayUntil() + " for " + holiday.getUsername() +" was deleted.");
        auditLogRepository.save(auditlog);
        holidayRepository.delete(holiday);
        messageBean.alertInformation("Info", "Holiday was deleted!");
    }



}
