package at.qe.sepm.skeleton.repositories;

import at.qe.sepm.skeleton.model.Holiday;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for managing {@link Holiday} entities.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
public interface HolidayRepository extends AbstractRepository<Holiday, String> {

    Holiday findFirstById(Long id);

    @Query("SELECT u FROM Holiday u WHERE u.username = :username")
    List<Holiday> findByUsername(@Param("username") String username);

//    @Query("SELECT u FROM Holiday u WHERE u.username = :username AND u")
//    List<Holiday> findByUsernameAndBetween(@Param("username") String username, 
//    		@Param("startDate") Date startDate, @Param("endDate") Date endDate );



}
