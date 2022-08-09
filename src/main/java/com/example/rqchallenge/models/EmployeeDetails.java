package com.example.rqchallenge.models;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDetails {
	private String status;
	private List<Employee> data;
	private String message;
	
	public EmployeeDetails() {
		data = new ArrayList<Employee>();
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public List<Employee> getData() {
		return data;
	}
	
	public void setData(List<Employee> data) {
		this.data = data;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
