package com.example.rqchallenge.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmployeeTest {

	@Test
	void test() {
		Employee emp1 = new Employee();
		emp1.setId(1);
		emp1.setEmployee_name("Mike");
		emp1.setEmployee_salary(120000);
		emp1.setEmployee_age(32);
		
		assertEquals(1,emp1.getId());
		assertEquals("Mike",emp1.getEmployee_name());
		assertEquals(120000,emp1.getEmployee_salary());
		assertEquals(32,emp1.getEmployee_age());
		
		Employee emp2 = new Employee(2,"John",100000,30,"employee.png");
		assertEquals(2,emp2.getId());
		assertEquals("John",emp2.getEmployee_name());
		assertEquals(100000,emp2.getEmployee_salary());
		assertEquals(30,emp2.getEmployee_age());
		assertEquals("employee.png",emp2.getProfile_image());
	}

}
