package com.epam.rd.autotasks.springemployeecatalog.controllers;

import com.epam.rd.autotasks.springemployeecatalog.aspects.EmployeeServiceDecorator;
import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.services.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.epam.rd.autotasks.springemployeecatalog.utils.PagingCriteria.PagingRecord;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class EmployeeController {
    private final EmployeeServiceDecorator employeeServiceImpl;
    public EmployeeController(EmployeeServiceDecorator employeeServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @GetMapping("/employees")
    @ResponseBody
    public List<Employee> findAllEmployees(@ModelAttribute PagingRecord pagingRecord) {
        return employeeServiceImpl.findAllEmployees(pagingRecord);
    }

    @GetMapping("/employees/by_manager/{mgr}")
    @ResponseBody
    public List<Employee> findAllByManagerId(
            @ModelAttribute PagingRecord pagingRecord,
            @PathVariable Long mgr
    ) {
        return employeeServiceImpl.findAllEmployeesByManagerId(mgr, pagingRecord);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long id,
            @RequestParam(name = "full_chain", required = false, defaultValue = "false") boolean showFullChain
    ) {
        try {
            Employee employee = employeeServiceImpl.findEmployeeById(id, showFullChain);
            return ResponseEntity.ok(employee);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/employees/by_department/{dep}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(
            @ModelAttribute PagingRecord pagingRecord,
            @PathVariable String dep
    ) {
        return ResponseEntity.ok(resolveByDepartment(pagingRecord, dep));
    }

    private List<Employee> resolveByDepartment(PagingRecord pagingRecord, String dep) {
        try {
            return employeeServiceImpl.findAllEmployeeByDepartmentId(Long.parseLong(dep), pagingRecord);
        } catch (NumberFormatException e) {
            return employeeServiceImpl.findAllEmployeeByDepartmentName(dep, pagingRecord);
        }
    }
}