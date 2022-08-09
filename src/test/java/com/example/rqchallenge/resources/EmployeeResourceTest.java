package com.example.rqchallenge.resources;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.rqchallenge.models.Employee;

class EmployeeResourceTest {

	private final String ServiceUrl = "http://localhost:8080";
	
	@Test
	void test() {
		// Test for REST API 'http://localhost:8080/employees'
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Employee[]> responseEntity = restTemplate.getForEntity(ServiceUrl + "/employees", Employee[].class);
			Employee[] empList = responseEntity.getBody();
			assertNotNull(empList,"EmployeeResourceTest: getForEntity() failed to get employee list.");
			assertTrue(empList.length > 0);
			
			Employee emp1 = empList[0];
			Employee emp24 = empList[23];

			assertEquals(1,emp1.getId());
			assertEquals("Tiger Nixon",emp1.getEmployee_name());
			assertEquals(320800,emp1.getEmployee_salary());
			assertEquals(61,emp1.getEmployee_age());
			
			assertEquals(24,emp24.getId());
			assertEquals("Doris Wilder",emp24.getEmployee_name());
			assertEquals(85600,emp24.getEmployee_salary());
			assertEquals(23,emp24.getEmployee_age());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception while getting all employee list by /employees Rest API.");
		}
		
		// Test for REST API 'http://localhost:8080/employee/{id}'
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Employee> responseEntity = restTemplate.getForEntity(ServiceUrl + "/employee/1", Employee.class);
			Employee employee = responseEntity.getBody();
			assertNotNull(employee,"EmployeeResourceTest: getForEntity() failed to get employee record.");

			assertEquals(1,employee.getId());
			assertEquals("Tiger Nixon",employee.getEmployee_name());
			assertEquals(320800,employee.getEmployee_salary());
			assertEquals(61,employee.getEmployee_age());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception while getting employee record by /employee/{id} Rest API.");
		}
		
		// Test for REST API 'http://localhost:8080/search/*'
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Employee[]> responseEntity = restTemplate.getForEntity(ServiceUrl + "/search/t", Employee[].class);
			Employee[] empList = responseEntity.getBody();
			assertNotNull(empList,"EmployeeResourceTest: getForEntity() failed to search employee.");
			assertTrue(empList.length > 0);
			
			Employee emp1 = empList[0];

			assertEquals(1,emp1.getId());
			assertEquals("Tiger Nixon",emp1.getEmployee_name());
			assertEquals(320800,emp1.getEmployee_salary());
			assertEquals(61,emp1.getEmployee_age());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception while searching employee list by /search/* Rest API.");
		}
		
		// Test for REST API 'http://localhost:8080/highestSalary'
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(ServiceUrl + "/highestSalary", Integer.class);
			Integer highSalary = responseEntity.getBody();
			assertNotNull(highSalary,"EmployeeResourceTest: getForEntity() failed to get high employee salary.");
			assertEquals(725000,highSalary);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception while getting highest employee salary by /highestsalary Rest API.");
		}
		
		// Test for REST API 'http://localhost:8080/topTenHighestEarningEmployeeNames'
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(ServiceUrl + "/topTenHighestEarningEmployeeNames", String[].class);
			String[] empList = responseEntity.getBody();
			assertNotNull(empList,"EmployeeResourceTest: getForEntity() failed to get top 10 highest earning employees.");
			assertTrue(empList.length > 0);
			
			String emp1 = empList[0];
			String emp2 = empList[9];

			assertEquals("Paul Byrd",emp1.toString());
			assertEquals("Tiger Nixon",emp2.toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception while getting employee list by /topTenHighestEarningEmployeeNames Rest API.");
		}
		
		// Test for REST API 'http://localhost:8080/create'
		try {
			Employee employeeInput = new Employee(0,"John Gandee",100000,50,"employee.png");
			assertNotNull(employeeInput,"EmployeeResourceTest: Failed to create employee input object.");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Employee> responseEntity = restTemplate.postForEntity(ServiceUrl + "/create",employeeInput, Employee.class);
			Employee employee = responseEntity.getBody();
			assertNotNull(employee,"EmployeeResourceTest: getForEntity() failed to create employee record.");

			assertTrue(employee.getId() > 0);
			assertEquals("John Gandee",employee.getEmployee_name());
			assertEquals(100000,employee.getEmployee_salary());
			assertEquals(50,employee.getEmployee_age());
			assertEquals("employee.png",employee.getProfile_image());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception while creating employee record by /create Rest API.");
		}
		
		// Test for REST API 'http://localhost:8080/delete/{id}'
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Employee> responseEntity = restTemplate.getForEntity(ServiceUrl + "/employee/1",Employee.class);
			Employee employee = responseEntity.getBody();
			assertNotNull(employee,"EmployeeResourceTest: getForEntity() failed to get employee record for employee id 1.");
			ResponseEntity<String> response =
					restTemplate.exchange(ServiceUrl + "/delete/1",HttpMethod.DELETE, null, String.class);
			String delResponse = response.getBody();
			     assertTrue(response.getStatusCode() == HttpStatus.OK);
			assertEquals(employee.getEmployee_name(),delResponse.toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception while creating employee record by /create Rest API.");
		}
	}
}
