package nl.gogognome.gogologbook.entities;

import java.util.Date;

public class FilterCriteria {

	public Date from;
	public Date to;
	public String user;
	public String project;
	public String customer;
	public String town;
	public String category;
	public String message;

	public static FilterCriteria createFindAll() {
		return new FilterCriteria();
	}

}
