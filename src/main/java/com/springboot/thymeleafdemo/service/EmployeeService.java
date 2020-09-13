package com.springboot.thymeleafdemo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.springboot.thymeleafdemo.entity.Employee;


public interface EmployeeService {

	public List<Employee> findAll();
	
	public Employee findById(int theId);
	
	public void save(Employee theEmployee);
	
	public void deleteById(int theId);
	
	public List<Employee> searchEmployees(String keyword);

	Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
