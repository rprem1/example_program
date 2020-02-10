package at.qe.sepm.skeleton.model;

import java.io.Serializable;
import java.sql.Time;

import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.services.AircraftService;
import at.qe.sepm.skeleton.model.Aircraft;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Persistable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.text.SimpleDateFormat;
/**
 * Entity representing flights.
 * The flight model saves everything about a flight
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Entity
public class Flight implements Persistable<String>, Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 50)
    private String flightId;
    private String iataFrom;
    private String iataTo;
    private Date departureTime;
    private Date arrivalTime;
    private Date dateFlight;
    private String flightTime;
    private Boolean isValidFlight;
    private int requiredPersonal;
    private int currentPersonal;
    
    @OneToOne(optional = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(name="aircraft_id", joinColumns={@JoinColumn(name="aircraft_id")})
    private Aircraft scheduledAircraft;
    
    @Value("${var.string:#{NULL}}")
    private String scheduledAircraftId;

    
    
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
            name = "assigned_Pilots",
            joinColumns = @JoinColumn(name = "flight_Id"),
            inverseJoinColumns = @JoinColumn(name = "username")
            )
    private Set<User> assignedPilots;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
            name = "assigned_Boardcrew",
            joinColumns = @JoinColumn(name = "flight_Id"),
            inverseJoinColumns = @JoinColumn(name = "username")
            )
    private Set<User> assignedBoardpersonal;
	private String personal = "";
    private int numberOfPassengers;




    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @ManyToOne(optional = true)
    private Flight updateFlight;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    

    
    public String getFlightTime() {
		return flightTime;
	}


	public void setFlightTime(){

        this.flightTime = calcFlightTime(this.departureTime, this.arrivalTime);

    }


    public String calcFlightTime(Date deparTime, Date arrTime){

        long dep = deparTime.getTime();
        long arr = arrTime.getTime();
        long diffInMillis = Math.abs(arr - dep);
        long minutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
        int minutesFlight = (int) minutes;
        StringBuilder minutesConverted = new StringBuilder();
        minutesConverted.append(Integer.toString(minutesFlight));
        return minutesConverted.toString();
    }

    public Flight getUpdateFlight() {
        return updateFlight;
    }

    public void setUpdateFlight(Flight updateFlight) {
        this.updateFlight = updateFlight;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getIataFrom() {
        return iataFrom;
    }

    public void setIataFrom(String iataFrom) {
        this.iataFrom = iataFrom;
    }

    public String getIataTo() {
        return iataTo;
    }

    public void setIataTo(String iataTo) {
        this.iataTo = iataTo;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getDateFlight() {
        return dateFlight;
    }

    public void setDateFlight(Date dateFlight) {
        this.dateFlight = dateFlight;
    }

    public Set<User> getAssignedPilots() {
        return assignedPilots;
    }

    public void setAssignedPilots(Set<User> assignedPilots) {
        this.assignedPilots = assignedPilots;
    }

    public Set<User> getAssignedBoardpersonal() {
        return assignedBoardpersonal;
    }

    public void setAssignedBoardpersonal(Set<User> assignedBoardpersonal) {
        this.assignedBoardpersonal = assignedBoardpersonal;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        final Flight other = (Flight) obj;
        if (!Objects.equals(this.flightId, other.flightId)) {
            return false;
        }
        return true;
    }



    @Override
    public String toString() {
        return "at.qe.sepm.skeleton.model.Flight[ id=" + flightId + " ]";
    }

    @Override
    public String getId() {
        return getFlightId();
    }

    public void setId(String id) {
        setFlightId(id);
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }


	public Aircraft getScheduledAircraft() {
		return scheduledAircraft;
	}


	public void setScheduledAircraft(Aircraft scheduledAircraft) {
		this.scheduledAircraft = scheduledAircraft;
	}


	public String getScheduledAircraftId() {
		return scheduledAircraftId;
	}


	public void setScheduledAircraftId(String scheduledAircraftId) {
		this.scheduledAircraftId = scheduledAircraftId;
	}
	
    public String getPersonal() {
    	personal = "";
    	for( User user: this.assignedPilots) {
    		personal = personal + user.getUsername() +", ";
    	}
    	for( User user: this.assignedBoardpersonal) {
    		personal = personal + user.getUsername() + ", ";
    	}
    	if ( personal.contentEquals("")) {
    		return "";
    	}
    	return personal.substring(0, personal.length()-2);
    }
	
	public long getFlightTimeInMilli() {
		if(departureTime == null || arrivalTime == null)
			return 0;
		long milli = getArrivalTime().getTime() - getDepartureTime().getTime();
		if(milli < 0)
			return 0;
		return milli;
	}
	
	public Double getFlightTimeInHours() {
		long milli = getFlightTimeInMilli();
		double hours = milli / 1000 * 60 * 60;
		return (double) milli;
	}


	public Boolean getIsValidFlight() {
		return isValidFlight;
	}


	public void setIsValidFlight(Boolean isValidFlight) {
		this.isValidFlight = isValidFlight;
	}


	public int getRequiredPersonal() {
		return requiredPersonal;
	}


	public void setRequiredPersonal(int requiredPersonal) {
		this.requiredPersonal = requiredPersonal;
	}


	public int getCurrentPersonal() {
		return currentPersonal;
	}


	public void setCurrentPersonal(int currentPersonal) {
		this.currentPersonal = currentPersonal;
	}
	
	
	
	
	
	

}