package com.springboot.thymeleafdemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.thymeleafdemo.dao.EmployeeRepository;
import com.springboot.thymeleafdemo.entity.Employee;


@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired  //EmployeeRepository is injected with this @Autowired annotation
	private EmployeeRepository employeeRepository;
	
	// automatically inject DAO by constructor
	public EmployeeServiceImpl(EmployeeRepository theEmployeeRepository) {
		employeeRepository = theEmployeeRepository;
	}

	@Override
	public List<Employee> findAll() {
		//return employeeRepository.findAll();
		return employeeRepository.findAllByOrderByLastNameAsc();
	}

	@Override
	public Employee findById(int theId) {
		Optional<Employee> result = employeeRepository.findById(theId);
		
		Employee theEmployee = null;
		
		if (result.isPresent()) {
			theEmployee = result.get();
		} else {
			throw new RuntimeException("Did not find employee id -" + theId);
		}
		return theEmployee;
	}

	@Override
	public void save(Employee theEmployee) {
		employeeRepository.save(theEmployee);
	}

	@Override
	public void deleteById(int theId) {
		employeeRepository.deleteById(theId);
	}

	// here is for pagination only
//	@Override
//	public Page<Employee> findPaginated(int pageNo, int pageSize) {
//
//		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
//		
//		return employeeRepository.findAll(pageable);
//	}

	// add pagination and sort by fields (column heads firstName, lastName)
	@Override
	public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {

		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():
																			  Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort); //default page number is 0 (yes it is weird)
		
		return employeeRepository.findAll(pageable);
	}

	@Override
	public List<Employee> searchEmployees(String keyword) {
		
		if (keyword != null && keyword.trim().length() > 0) {
			// search for last name or first name, case sensitive
			return employeeRepository.searchEmployees(keyword);
		}
		
		return employeeRepository.findAll();
	}
	
}
