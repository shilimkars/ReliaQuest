package com.example.rqchallenge.employees;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.rqchallenge.models.Employee;

import java.io.IOException;
import java.util.List;
//import java.util.Map;

@RestController
public interface IEmployeeController {

    @GetMapping("/employees")
    ResponseEntity<List<Employee>> getAllEmployees() throws IOException;

    @GetMapping("/search/{searchString}")
    ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString);

    @GetMapping("/employee/{id}")
    ResponseEntity<Employee> getEmployeeById(@PathVariable String id);

    @GetMapping("/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    @PostMapping("/create")
    //ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput);
    ResponseEntity<Employee> createEmployee(@RequestBody Employee employeeInput);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id);

}
