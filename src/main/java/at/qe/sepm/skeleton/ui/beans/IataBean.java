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
 * Contains all possible IATA Codes
 */
@ManagedBean
@ApplicationScoped
public class IataBean {
	private String iata;	
	private List<String> iataList;
	
	@PostConstruct
	public void init() {
        iataList = new ArrayList<>();		
		iataList.add("HAM");
		iataList.add("STR");
		iataList.add("SXF");
		iataList.add("MUC");
		iataList.add("DUS");
		iataList.add("SXF");
		iataList.add("FRA");
		iataList.add("ADR");
		iataList.add("EIN");
		iataList.add("GLG");
		iataList.add("ROT");
		iataList.add("HMS");
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public List<String> getIataList() {
		return iataList;
	}

	public void setIataList(List<String> iataList) {
		this.iataList = iataList;
	}
	
	
	
}
