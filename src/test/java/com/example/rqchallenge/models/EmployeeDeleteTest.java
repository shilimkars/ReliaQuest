package com.example.rqchallenge.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmployeeDeleteTest {

	@Test
	void test() {
		EmployeeDelete emp1 = new EmployeeDelete();
		emp1.setStatus("status");
		emp1.setMessage("message");;
		emp1.setData("data");;
		
		assertEquals("status",emp1.getStatus());
		assertEquals("message",emp1.getMessage());
		assertEquals("data",emp1.getData());
		
		EmployeeDelete emp2 = new EmployeeDelete("status","data","message");
		assertEquals("status",emp2.getStatus());
		assertEquals("message",emp2.getMessage());
		assertEquals("data",emp2.getData());
	}

}
