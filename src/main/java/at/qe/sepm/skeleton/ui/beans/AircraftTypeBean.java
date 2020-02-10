package at.qe.sepm.skeleton.ui.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.springframework.web.context.annotation.ApplicationScope;

/**
 * Contains all aircraft types, which are supported
 */
@ManagedBean
@ApplicationScoped
public class AircraftTypeBean {
	private String aircraftType;	
	private List<String> aircraftTypeList;
	
	@PostConstruct
	public void init() {
        aircraftTypeList = new ArrayList<>();		
		aircraftTypeList.add("AIRBUS");
		aircraftTypeList.add("BOEING");
		aircraftTypeList.add("EMBRAER");
	}

	public String getAircraftType() {
		return aircraftType;
	}

	public void setAircraftType(String aircraftType) {
		this.aircraftType = aircraftType;
	}

	public List<String> getAircraftTypeList() {
		return aircraftTypeList;
	}

	public void setAircraftTypeList(List<String> aircraftTypeList) {
		this.aircraftTypeList = aircraftTypeList;
	}


	
	
	
}
