package com.example.rqchallenge.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class EmployeeDetailsTest {

	@Test
	void test() {
		EmployeeDetails empDetails = new EmployeeDetails();
		List<Employee> empList = new ArrayList<Employee>();
		
		Employee emp1 = new Employee(100,"John",150000,40,"employee1.png");
		Employee emp2 = new Employee(200,"Mary",120000,35,"employee2.png");
		empList.add(emp1);
		empList.add(emp2);
		
		empDetails.setData(empList);
		empDetails.setMessage("message");
		empDetails.setStatus("status");
		
		assertEquals("status",empDetails.getStatus());
		assertEquals("message",empDetails.getMessage());
		List<Employee> empList2 = empDetails.getData();
		
		Employee emp3 = empList2.get(0);
		Employee emp4 = empList2.get(1);
		
		assertEquals(100,emp3.getId());
		assertEquals("John",emp3.getEmployee_name());
		assertEquals(150000,emp3.getEmployee_salary());
		assertEquals(40,emp3.getEmployee_age());
		assertEquals("employee1.png",emp3.getProfile_image());

		assertEquals(200,emp4.getId());
		assertEquals("Mary",emp4.getEmployee_name());
		assertEquals(120000,emp4.getEmployee_salary());
		assertEquals(35,emp4.getEmployee_age());
		assertEquals("employee2.png",emp4.getProfile_image());

	}

}
