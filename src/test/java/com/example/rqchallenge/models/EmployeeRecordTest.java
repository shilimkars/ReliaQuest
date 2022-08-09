package com.example.rqchallenge.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmployeeRecordTest {

	@Test
	void test() {
		EmployeeRecord empRecord = new EmployeeRecord();
		Employee emp1 = new Employee(100,"John",150000,40,"employee1.png");
		
		empRecord.setData(emp1);
		empRecord.setMessage("message");
		empRecord.setStatus("status");
		
		Employee emp2 = empRecord.getData();
		assertEquals("status",empRecord.getStatus());
		assertEquals("message",empRecord.getMessage());
		
		assertEquals(100,emp2.getId());
		assertEquals("John",emp2.getEmployee_name());
		assertEquals(150000,emp2.getEmployee_salary());
		assertEquals(40,emp2.getEmployee_age());
		assertEquals("employee1.png",emp2.getProfile_image());
	}
}
