package com.example.rqchallenge.models;

import java.util.Comparator;

public class EmployeeSalaryComparator implements Comparator<Employee> {
	@Override
    public int compare(Employee emp1, Employee emp2) {
        return (emp1.getEmployee_salary() - emp2.getEmployee_salary());
    }
}
