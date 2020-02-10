package at.qe.sepm.skeleton.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Persistable;


/**
 *
 * Entity representing an aircraft.
 * Includes everything needed for an aircraft
 */
@Entity
public class Aircraft implements Persistable<String>, Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(length =100)
	private String aircraftId;

	

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@ManyToOne(optional = true)
	private Aircraft updateAircraft;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date lastFlightTime;
	

	private String aircraftType;
	private int requiredPilotsAircraft;
	private int requiredBoardpersonalAircraft;
	private int capacityAircraft;
	
	/**shows if aircraft is in use
	 */
	@Column(columnDefinition="BOOLEAN DEFAULT false")
	private boolean isScheduled;
	

	public String getAircraftId() {
		return aircraftId;
	}

	public void setAircraftId(String aircraftId) {
		this.aircraftId = aircraftId;
	}

	public String getAircraftType() {
		return aircraftType;
	}

	public void setAircraftType(String aircraftType) {
		this.aircraftType = aircraftType;
	}

	public int getRequiredPilotsAircraft() {
		return requiredPilotsAircraft;
	}

	public void setRequiredPilotsAircraft(int requiredPilotsAircraft) {
		this.requiredPilotsAircraft = requiredPilotsAircraft;
	}

	public int getRequiredBoardpersonalAircraft() {
		return requiredBoardpersonalAircraft;
	}

	public void setRequiredBoardpersonalAircraft(int requiredBoardpersonalAircraft) {
		this.requiredBoardpersonalAircraft = requiredBoardpersonalAircraft;
	}

	public int getCapacityAircraft() {
		return capacityAircraft;
	}

	public void setCapacityAircraft(int capacityAircraft) {
		this.capacityAircraft = capacityAircraft;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Aircraft getUpdateAircraft() {
		return updateAircraft;
	}

	public void setUpdateAircraft(Aircraft updateAircraft) {
		this.updateAircraft = updateAircraft;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}



	@Override
	public String toString() {
		return "at.qe.sepm.skeleton.model.Aircraft[ id=" + aircraftId + " ]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Aircraft)) {
			return false;
		}
		final Aircraft other = (Aircraft) obj;
		if (!Objects.equals(this.aircraftId, other.aircraftId)) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		return java.util.Objects.hash(super.hashCode(), aircraftId, aircraftType, requiredPilotsAircraft, requiredBoardpersonalAircraft, capacityAircraft);
	}


	@Override
	public String getId() {
		return getAircraftId();
	}

	public void setId(String id) {
		setAircraftId(id);
	}

	@Override
	public boolean isNew() {
		return (null == createDate);
	}

	public boolean isScheduled() {
		return isScheduled;
	}

	public void setScheduled(boolean isScheduled) {
		this.isScheduled = isScheduled;
	}

	public Date getLastFlightTime() {
		return lastFlightTime;
	}

	public void setLastFlightTime(Date lastFlightTime) {
		this.lastFlightTime = lastFlightTime;
	}
	
	
	
}
