package com.springboot.thymeleafdemo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.springboot.thymeleafdemo.entity.Employee;

@RepositoryRestResource(path="api")  // add REST service
public interface EmployeeRepository extends JpaRepository<Employee, Integer> { 
	//							PagingAndSortingRepository<Employee, Integer> {  //JpaRepository Covered it already
	// no need any code here as JpaRepository already have
	// findAll(), findbyId(), Save, Delete.......
	
	// add a method to sort by last name
	public List<Employee> findAllByOrderByLastNameAsc();
	
	// add a search method, concatenate each field with a whitespace to avoid undesired results from the string concatenation
	@Query("SELECT e FROM Employee e WHERE CONCAT(e.firstName, ' ', e.lastName, ' ', e.email) LIKE %?1%")
	public List<Employee> searchEmployees(String keyword);
	
}
