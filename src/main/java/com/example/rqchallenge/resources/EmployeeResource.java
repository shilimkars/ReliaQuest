package com.example.rqchallenge.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.rqchallenge.models.Employee;
import com.example.rqchallenge.models.EmployeeSalaryComparator;
import com.example.rqchallenge.services.EmployeeInfo;
import com.example.rqchallenge.employees.IEmployeeController;

@RestController
public class EmployeeResource implements IEmployeeController {

	@Autowired
	EmployeeInfo employeeInfo;
	
	public EmployeeResource() {
	}
	
	@GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
		System.out.println("Entry getAllEmployees.");
		
		AtomicReference<HttpStatus> httpStatus = new AtomicReference<HttpStatus>(HttpStatus.OK);
		List<Employee> employeeList =  new ArrayList<Employee>();
		try {
			employeeList = employeeInfo.getAllEmployees(httpStatus);
			if (employeeList.isEmpty()) {
				System.out.println("Couldn't find any employee. Employee list is empty.");
				httpStatus.set(HttpStatus.NOT_FOUND);
			}
			else {
				System.out.println("Got all employee list successfully.");
			}
		}
		catch(Exception Ex) {
			System.out.println("Exception while getting employee list. Error: " + Ex.getMessage());
			httpStatus.set(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<Employee>>(employeeList, httpStatus.get());
	}

    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
		System.out.println("Entry getEmployeesByNameSearch. " + searchString);
		
		AtomicReference<HttpStatus> httpStatus = new AtomicReference<HttpStatus>(HttpStatus.OK);
		List<Employee> searchEmplist =  new ArrayList<Employee>();
		try {
			if (!searchString.isEmpty()) {
				
				List<Employee> employeeList = employeeInfo.getAllEmployees(httpStatus);
				if (!employeeList.isEmpty() && (httpStatus.get() == HttpStatus.OK)) {
					for (Employee employee : employeeList) {
						if (employee.getEmployee_name().toLowerCase().startsWith(searchString.toLowerCase())) {
							searchEmplist.add(employee);
						}
					}
				}
			}
			
			if (searchEmplist.isEmpty()) {
				System.out.println("Couldn't find any employee with the name starting with '" + searchString + "'");
				httpStatus.set(HttpStatus.NOT_FOUND);
			}
			else {
				System.out.println("Found the employee with the given search string '" + searchString + "'");
			}
		}
		catch(Exception Ex) {
			System.out.println("Exception while searching employee name. Error: " + Ex.getMessage());
			searchEmplist.clear();
			httpStatus.set(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<Employee>>(searchEmplist, httpStatus.get());
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
		System.out.println("Entry getEmployeeById. " + id);
		
		AtomicReference<HttpStatus> httpStatus = new AtomicReference<HttpStatus>(HttpStatus.OK);
		Employee employee = null;
		try {
			employee = employeeInfo.getEmployeeById(id,httpStatus);
			if (httpStatus.get() == HttpStatus.OK) {
				System.out.println("Found employee with the id '" + id + "'");
			}
			else if (httpStatus.get() == HttpStatus.NOT_FOUND) {
				System.out.println("Couldn't find any employee with the id '" + id + "'");
			}
		}
		catch(Exception Ex) {
			System.out.println("Exception while searching employee by id. Error: " + Ex.getMessage());
			httpStatus.set(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Employee>(employee, httpStatus.get());
    }

    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
		System.out.println("Entry getHighestSalaryOfEmployees.");
		
		AtomicReference<HttpStatus> httpStatus = new AtomicReference<HttpStatus>(HttpStatus.OK);
		Integer salary = 0;
		try {
			Employee employee = null;
			List<Employee> employeeList = employeeInfo.getAllEmployees(httpStatus);
			if (!employeeList.isEmpty() && (httpStatus.get() == HttpStatus.OK)) {
				Collections.sort(employeeList, new EmployeeSalaryComparator());
				employee = employeeList.get(employeeList.size()-1);
				salary = employee.getEmployee_salary();
				System.out.println("High Salary Employee id: " + employee.getId() + " salary: " + salary);
			}
			else if (httpStatus.get() == HttpStatus.NOT_FOUND) {
				System.out.println("Couldn't find any employee with high salary. Employee list is empty.");
			}
			else {
				System.out.println("Couldn't find any employee with high salary.");
			}
		}
		catch(Exception Ex) {
			System.out.println("Exception while finding high salary of employees. Error: " + Ex.getMessage());
			httpStatus.set(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Integer>(salary, httpStatus.get());
    }

    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
		System.out.println("Entry getTopTenHighestEarningEmployeeNames.");
		
		AtomicReference<HttpStatus> httpStatus = new AtomicReference<HttpStatus>(HttpStatus.OK);
		List<String> empNameList = new ArrayList<String>();
		try { 
			List<Employee> employeeList = employeeInfo.getAllEmployees(httpStatus);
			if (!employeeList.isEmpty() && (httpStatus.get() == HttpStatus.OK)) {
				Collections.sort(employeeList, new EmployeeSalaryComparator());
				
				for (int i = employeeList.size(), j=1; i > 0; i--,j++ ) {
					Employee employee = employeeList.get(i-1);
					empNameList.add(employee.getEmployee_name());
					System.out.println("Top 10 employee with high salary, id: " + employee.getId() + " name: " + employee.getEmployee_name());
					if (j == 10) {
						//TODO: There could be more than 10 employees with top salaries, since 10th, 11th, 12th & so on
						//      could have same salary as the 10th one. But this is not handled in this logic. 
						break;
					}
				}
			}
			if (empNameList.isEmpty()) {
				System.out.println("Couldn't find any top 10 employees with high salary. Employee list is empty.");
				httpStatus.set(HttpStatus.NOT_FOUND);
			}
			else {
				System.out.println("Suceessfully returned top 10 employees with high salary.");
			}
		}
		catch (Exception Ex) {
			System.out.println("Exception while finding top 10 employees with high salary. Error: " + Ex.getMessage());
			httpStatus.set(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<String>>(empNameList, httpStatus.get());
    }

    @PostMapping("/create")
    //public ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput) {
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employeeInput) {
		System.out.println("Entry createEmployee.");
		
		AtomicReference<HttpStatus> httpStatus = new AtomicReference<HttpStatus>(HttpStatus.OK);
		Employee employee = null;
		try {
		    employee = employeeInfo.createEmployee(employeeInput,httpStatus);
		    if (httpStatus.get() == HttpStatus.OK) {
				System.out.println("Suceessfully created employee. id = " + employee.getId());
		    }
		    else {
	    		System.out.println("Failed to create employee with the name = " 
	    				+ employeeInput.getEmployee_name());
		    }
		}
		catch(Exception Ex) {
    		System.out.println("Exception while creating employee with the name = " 
    				+ employeeInput.getEmployee_name() + " Error: " + Ex.getMessage());
    		httpStatus.set(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Employee>(employee, httpStatus.get());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
		System.out.println("Entry deleteEmployeeById. " + id);
		
		AtomicReference<HttpStatus> httpStatus = new AtomicReference<HttpStatus>(HttpStatus.OK);
		String retResponse = "";
		try {
			Employee employee = employeeInfo.deleteEmployeeById(id,httpStatus);
			if (httpStatus.get() == HttpStatus.OK) {
		    	retResponse = "Employee deleted successfully.";
		    	System.out.println("Employee deleted successfully with the given id " + employee.getId());
			}
			else {
	    		System.out.println("Can't find the employee with the given id.");
	    		httpStatus.set(HttpStatus.NOT_FOUND);
	    		retResponse = "Can't find the employee with the given id.";
			}
		}
		catch(Exception Ex) {
    		System.out.println("Exception while deleting the employee with id " + id + " Error:" + Ex.getMessage());
    		httpStatus.set(HttpStatus.INTERNAL_SERVER_ERROR);
    		retResponse = "Exception while deleting the employee with the given id.";
		}
		
		return new ResponseEntity<String>(retResponse, httpStatus.get());
    }
}
