package at.qe.sepm.skeleton.ui.controllers;

import at.qe.sepm.skeleton.model.Flight;
import at.qe.sepm.skeleton.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Date;

/**
 * Controller to filter the flights for the manager flight overview
 */
@Component
@Scope("view")
public class FlightFilterController {

    @Autowired
    private FlightService flightService;
    private List<Flight> flights;
    private List<Flight> filteredFlights;

    public Collection<Flight> getFlights() {
        flights = new ArrayList(flightService.getAllFlights());
        return flights;
    }

    public List<Flight> getFilteredFlights() {
        return filteredFlights;
    }

    public void setFilteredFlights(List<Flight> filteredFlights) {
        this.filteredFlights = filteredFlights;
    }

    /**
     * custom filter to check, if a flight is between two dates
     * @return true or false, if the flight is in the time span
     */
    public boolean filterByDate(Object value, Object filter, Locale locale) {

        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }
        if (value == null) {
            return false;
        }

        DateFormat df = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM);

        Date filterDate = (Date) value;
        Date dateFrom;
        Date dateTo;
        try {
            String fromPart = filterText.substring(0, filterText.indexOf("-"));
            String toPart = filterText.substring(filterText.indexOf("-") + 1);
            dateFrom = fromPart.isEmpty() ? null : df.parse(fromPart);
            dateTo = toPart.isEmpty() ? null : df.parse(toPart);
        } catch (ParseException pe) {
            return false;
        }

        return (dateFrom == null || filterDate.after(dateFrom)) && (dateTo == null || filterDate.before(dateTo));
    }

}
