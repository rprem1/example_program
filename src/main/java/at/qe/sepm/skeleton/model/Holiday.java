package at.qe.sepm.skeleton.model;


import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.Entity;
import org.springframework.data.domain.Persistable;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GeneratedValue;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.Objects;
import javax.persistence.GenerationType;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;


/**
 * Entity of a class repersenting the holiday of a user
 */
@Entity
public class Holiday implements Persistable<Long>, Serializable{


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;


    private String username;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    private String holidayFrom;
    private String holidayUntil;
    private int holidayDays;


    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;



    public int getHolidayDays(){return holidayDays;}

    public void setHolidayDays(int days){

        this.holidayDays = days;

    }

    public Holiday() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public void setId(Long id) {
        this.id = id;
    }


    public String getHolidayFrom() { return holidayFrom; }

    public void setHolidayFrom(String holidayFrom) { this.holidayFrom = holidayFrom; }

    public String getHolidayUntil() { return holidayUntil; }

    public void setHolidayUntil(String holidayUntil) { this.holidayUntil = holidayUntil; }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(String id) {
        setId(Long.parseLong(id));
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Holiday)) {
            return false;
        }
        final Holiday other = (Holiday) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


}