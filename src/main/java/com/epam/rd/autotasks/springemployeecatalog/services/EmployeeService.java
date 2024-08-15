package com.epam.rd.autotasks.springemployeecatalog.services;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployee(int page, int size, String sortField) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(modify(sortField)));
        Page<Employee> result = employeeRepository.findAll(pageable);
        return getEmployees(result.toList());
    }

    public List<Employee> getEmployeesByManagerId(int page, int size, String sortField, Long managerId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(modify(sortField)));
        Page<Employee> result = employeeRepository.getEmployeesByManager_Id(managerId, pageable);
        return getEmployees(result.stream().collect(Collectors.toList()));
    }

    private List<Employee> getEmployees(List<Employee> result) {
        return result.stream()
                .map(e -> {
                    Employee copy = new Employee(e);
                    if (copy.getManager() != null) {
                        copy.getManager().setManager(null);
                    }
                    return copy;
                })
                .collect(Collectors.toList());
    }

    public Employee getEmployeeById(Long id, boolean showFullChain) {
        return showFullChain ?
                employeeRepository.findById(id).orElseThrow(NoSuchElementException::new) :
                getEmployees(
                List.of(
                        employeeRepository
                                .findById(id)
                                .orElseThrow(NoSuchElementException::new)
                )
        ).get(0);
    }

    private String modify(String input) {
        if(input.equals("lastName") || input.equals("firstName") || input.equals("middleName")) {
            return "fullName." + input;
        } else {
            return input;
        }
    }

    public List<Employee> getEmployeesByDepartment(String dep, int page, int size, String sortField) {
        try {
            return getEmployees(
                    employeeRepository
                            .getEmployeesByDepartment_Id(
                                    Long.parseLong(dep),
                                    PageRequest.of(page, size, Sort.by(modify(sortField))))
                            .getContent()
            );
        } catch (NumberFormatException e) {
            return getEmployees(employeeRepository
                    .getEmployeesByDepartment_Name(dep, PageRequest.of(page, size, Sort.by(modify(sortField))))
                    .getContent());
        }
    }
}