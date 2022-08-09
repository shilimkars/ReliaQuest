package com.example.rqchallenge.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.rqchallenge.models.Employee;
import com.example.rqchallenge.models.EmployeeDelete;
import com.example.rqchallenge.models.EmployeeDetails;
import com.example.rqchallenge.models.EmployeeRecord;


@Service
@Configurable
public class EmployeeInfo {

	private final String DUMMY_REST_API_EXAMPLE_URL = "https://dummy.restapiexample.com";
	private final String API_V1_URL = "/api/v1";
	private final long WAIT_TIME_BETWEEN_REQUEST_IN_SECONDS = 3;
	private final int REQUEST_FAIL_RETRY_COUNT = 10;
	
	@Autowired
	private RestTemplate restTemplate;
	
    public List<Employee> getAllEmployees(AtomicReference<HttpStatus> httpStatus) throws IOException {
		System.out.println("Service.EmployeeInfo: Entry getAllEmployees.");
		
		httpStatus.set(HttpStatus.OK);
		List<Employee> empList = new ArrayList<Employee>();
		try {
			boolean bException = false;
			int retry = 0;
			do {
				try {
					EmployeeDetails empDetails = restTemplate.getForObject(DUMMY_REST_API_EXAMPLE_URL + API_V1_URL + "/employees", EmployeeDetails.class);
					for (Employee employee : empDetails.getData()) {
						empList.add(employee);
					}
					bException = false;
				}
				catch(Exception Ex) {
					System.out.println("Service.EmployeeInfo: Exception while when restTemplate.getForObject called. Error: " + Ex.getMessage());
					bException = true;
					retry++;
					TimeUnit.SECONDS.sleep(WAIT_TIME_BETWEEN_REQUEST_IN_SECONDS);
				}
			} while (bException && (retry < REQUEST_FAIL_RETRY_COUNT));
			
			if (empList.isEmpty()) {
				System.out.println("Service.EmployeeInfo: Employee list is empty.");
				httpStatus.set(HttpStatus.NOT_FOUND);
			}
			else {
				System.out.println("Service.EmployeeInfo: Employee list is successfully returned.");
			}
		}
		catch(Exception Ex) {
			System.out.println("Service.EmployeeInfo: Exception while getting the employee list. Error: " + Ex.getMessage());
			httpStatus.set(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return empList;
	}

    public Employee getEmployeeById(String id,AtomicReference<HttpStatus> httpStatus) {
		System.out.println("Service.getEmployeeById: Entry getEmployeeById.");
		
		httpStatus.set(HttpStatus.OK);
		Employee employee = new Employee();
		try {
			boolean bException = false;
			int retry = 0;
			do {
				try {
					EmployeeRecord employeeRecord = restTemplate.getForObject(DUMMY_REST_API_EXAMPLE_URL + API_V1_URL + "/employee/" + id, EmployeeRecord.class);
					employee = employeeRecord.getData();
					bException = false;
				}
				catch(Exception Ex) {
					System.out.println("Service.getEmployeeById: Exception while when restTemplate.getForObject called. Error: " + Ex.getMessage());
					bException = true;
					retry++;
					TimeUnit.SECONDS.sleep(WAIT_TIME_BETWEEN_REQUEST_IN_SECONDS);
				}
			} while (bException && (retry < REQUEST_FAIL_RETRY_COUNT));
			
			if ((employee != null) && (employee.getId() == Integer.valueOf(id))) {
				System.out.println("Service.getEmployeeById: Employee record successfully returned.");
			}
			else {
				System.out.println("Service.getEmployeeById: Employee record not found.");
				httpStatus.set(HttpStatus.NOT_FOUND);
			}
		}
		catch(Exception Ex) {
			System.out.println("Service.getEmployeeById: Exception while getting the employee record. Error: " + Ex.getMessage());
			httpStatus.set(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return employee;
    }
    
    public Employee createEmployee(Employee employeeInput,AtomicReference<HttpStatus> httpStatus) {
		
		httpStatus.set(HttpStatus.OK);
		Employee employee = new Employee();
		try {
			if ((employeeInput == null) 
				|| (employeeInput.getEmployee_name().isEmpty()) 
				|| (employeeInput.getEmployee_salary() <= 0 ) 
				|| (employeeInput.getEmployee_age() <= 0)
				) {
				System.out.println("Service.createEmployee: Invalid input or bad request.");
				httpStatus.set(HttpStatus.BAD_REQUEST);
				return employee;
			}
			
			boolean bException = false;
			int retry = 0;
			do {
				try {
			    	EmployeeRecord employeeRecord = restTemplate.postForObject(DUMMY_REST_API_EXAMPLE_URL + API_V1_URL + "/create", 
							employeeInput,EmployeeRecord.class);
			    	employee = employeeRecord.getData();
					bException = false;
				}
				catch(Exception Ex) {
					System.out.println("Service.createEmployee: Exception while restTemplate.postForObject called."
							+ " Error: " + Ex.getMessage());
					bException = true;
					retry++;
					TimeUnit.SECONDS.sleep(WAIT_TIME_BETWEEN_REQUEST_IN_SECONDS);
				}
			} while (bException && (retry < REQUEST_FAIL_RETRY_COUNT));
			
			if (employee == null) {
				System.out.println("Service.createEmployee: Failed to create employee. Name = " 
						+ employeeInput.getEmployee_name());
				httpStatus.set(HttpStatus.BAD_REQUEST);
			}
			else {
				System.out.println("Service.createEmployee: Employee created successfully. Employee Name = " 
						+ employee.getEmployee_name());
			}
		}
		catch(Exception Ex) {
			System.out.println("Service.createEmployee: Exception while creating the employee. Error: " 
					+ Ex.getMessage());
			httpStatus.set(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return employee;
    }
    
    public Employee deleteEmployeeById(String id,AtomicReference<HttpStatus> httpStatus) {
		System.out.println("Service.deleteEmployeeById: Entry deleteEmployeeById.");
		
		httpStatus.set(HttpStatus.OK);
		Employee employee = new Employee();
		try {
			boolean bException = false;
			int retry = 0;
			EmployeeDelete delResponse = null;
			
			do {
				try {
					EmployeeRecord employeeRecord = restTemplate.getForObject(DUMMY_REST_API_EXAMPLE_URL + API_V1_URL + "/employee/" + id, EmployeeRecord.class);
					employee = employeeRecord.getData();
					if (employee != null) {
						ResponseEntity<EmployeeDelete> response =
								restTemplate.exchange(DUMMY_REST_API_EXAMPLE_URL + API_V1_URL + "/delete/" + id,
								HttpMethod.DELETE, null, EmployeeDelete.class);
						delResponse = response.getBody();
						httpStatus.set(response.getStatusCode());
					}
					else {
						httpStatus.set(HttpStatus.NOT_FOUND);
						break;
					}
					bException = false;
				}
				catch(Exception Ex) {
					System.out.println("Service.deleteEmployeeById: Exception while when restTemplate.delete called. Error: " + Ex.getMessage());
					bException = true;
					retry++;
					TimeUnit.SECONDS.sleep(WAIT_TIME_BETWEEN_REQUEST_IN_SECONDS);
				}
			} while (bException && (retry < REQUEST_FAIL_RETRY_COUNT));
			
			if ((employee != null) && (httpStatus.get() == HttpStatus.OK)) {
				System.out.println("Service.deleteEmployeeById: Employee record successfully deleted.");
			}
			else {
				System.out.println("Service.deleteEmployeeById: Employee record not found.");
				httpStatus.set(HttpStatus.NOT_FOUND);
			}
		}
		catch(Exception Ex) {
			System.out.println("Service.deleteEmployeeById: Exception while getting the employee record. Error: " + Ex.getMessage());
			httpStatus.set(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return employee;
    }
}
