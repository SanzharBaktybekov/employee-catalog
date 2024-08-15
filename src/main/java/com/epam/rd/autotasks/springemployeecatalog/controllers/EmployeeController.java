package com.epam.rd.autotasks.springemployeecatalog.controllers;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.services.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class EmployeeController {
    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    @ResponseBody
    public List<Employee> findAllEmployees(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "14") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortField
    ) {
        System.out.printf("page:%s, size:%s, sort:%s\n", page, size, sortField);
        return employeeService.getEmployee(page, size, sortField);
    }


    @GetMapping("/employees/by_manager/{mgr}")
    @ResponseBody
    public List<Employee> findAllByManagerId(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "1") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortField,
            @PathVariable Long mgr
    ) {
        return employeeService.getEmployeesByManagerId(page, size, sortField, mgr);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long id,
            @RequestParam(name = "full_chain", required = false, defaultValue = "false") boolean showFullChain
    ) {
        try {
            Employee employee = employeeService.getEmployeeById(id, showFullChain);
            return ResponseEntity.ok(employee);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/employees/by_department/{dep}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "1") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortField,
            @PathVariable String dep
    ) {
        return ResponseEntity.ok(
                employeeService.getEmployeesByDepartment(dep, page, size, sortField)
        );
    }
}