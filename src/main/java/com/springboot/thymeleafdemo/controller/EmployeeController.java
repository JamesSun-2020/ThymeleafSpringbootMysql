package com.springboot.thymeleafdemo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.thymeleafdemo.entity.Employee;
import com.springboot.thymeleafdemo.service.EmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
	
	private EmployeeService employeeService;
	
	List<Employee> theEmployees = null;
	
	public EmployeeController(EmployeeService theEmployeeService) {
		this.employeeService = theEmployeeService;
	}
	
	// add mapping for "list" no pagination
	@GetMapping("/list")
	public String listEmployees(Model theModel) {
		
//		// get employees from db
//		theEmployees = employeeService.findAll();
//		// add to the Spring MVC model
//		theModel.addAttribute("employees", theEmployees);
//		
//		return "list-employees";
		
		return this.getPaginatedEmployees(1, "firstName", "asc", theModel);
		
	}
	
	// get paginated employees and sorted parameters by fields
		@GetMapping("/list/{pageNo}")
		public String getPaginatedEmployees(@PathVariable(value="pageNo") int pageNo,
											@RequestParam("sortField") String sortField,
											@RequestParam("sortDir") String sortDir,Model theModel) {
			
			//@Value("${pageSize}") //inject pageSize from application.properties
			int pageSize =5; 
			
			Page<Employee> page = this.employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
			
			theEmployees = page.getContent();
			
			// add pages info to Model
			theModel.addAttribute("currentPage", pageNo);
			theModel.addAttribute("totalPages", page.getTotalPages());
			theModel.addAttribute("totalItems", page.getTotalElements());
			
			theModel.addAttribute("sortField", sortField);
			theModel.addAttribute("sortDir", sortDir);
			theModel.addAttribute("reverseSortDir", sortDir.equals("asc")?"desc":"asc");
			
			theModel.addAttribute("employees", theEmployees);
			
			 // return employee list
			return "list-employees";
		}
	
//	// add mapping for "list" and pagination from another way
//		@GetMapping("/list")
//		public String listEmployees(Model theModel, 
//									@RequestParam("page") Optional<Integer> page, 
//									@RequestParam("size") Optional<Integer> size) {
//			
//			int currentPage = page.orElse(1);
//			int pageSize = size.orElse(5);
//			
//			// get employees from db
//			theEmployees = employeeService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
//			// add to the Spring MVC model
//			theModel.addAttribute("employees", theEmployees);
//			
//			return "list-employees";
//			
//		}
	
	
	
	// add employee pre-populate form
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		
		// create model attribute to bind form data
		Employee theEmployee = new Employee();
		
		theModel.addAttribute("employee", theEmployee);
		
		return "employees/employee-form";
	}
	
	// save employee by passing form data
	@PostMapping("/save")
	public String saveEmployee(@ModelAttribute("employee") Employee theEmployee) {
		// save the employee
		employeeService.save(theEmployee);
		
		// use a redirect to prevent duplicate submissions
		return "redirect:/employees/list";
	}
	
	// update employee pre-populate form
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("employeeId") int theId, Model theModel) {
		
		// get employee from the service by Id
		Employee theEmployee = employeeService.findById(theId);
		
		// set employee as a model attribute to pre-populate the form
		theModel.addAttribute("employee", theEmployee);
		
		// send over to the form
		return "employees/employee-form";
	}
	
	// delete employee
	@GetMapping("/delete")
	public String delete(@RequestParam("employeeId") int theId, Model theModel) {
		// delete the employee
		employeeService.deleteById(theId);
		
		//redirect to employee list
		return "redirect:/employees/list";
	}
	
	// search employee
	@GetMapping("/search")
	public String searchEmployees(@RequestParam("keyword") String keyword, Model theModel) {
		List<Employee> theEmployees = employeeService.searchEmployees(keyword);
		theModel.addAttribute("employees", theEmployees);
		theModel.addAttribute("keyword", keyword);
		return "list-employees";
	}
	
	
}
