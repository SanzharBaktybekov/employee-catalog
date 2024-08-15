package com.epam.rd.autotasks.springemployeecatalog.services;

import com.epam.rd.autotasks.springemployeecatalog.utils.RuleProducer;
import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.repository.EmployeeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.epam.rd.autotasks.springemployeecatalog.utils.PagingCriteria.PagingRecord;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final List<Consumer<PagingRecord>> rules = RuleProducer.getDefaultRules();
    private Pageable buildPageable(PagingRecord record) {
        rules.forEach(r -> r.accept(record));
        return record.toPageable();
    }

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    public List<Employee> findAllEmployees(PagingRecord record) {
        return employeeRepository
                .findAll(buildPageable(record))
                .stream()
                .map(this::simplifyEmployee)
                .collect(Collectors.toList());
    }
    public List<Employee> findAllEmployeesByManagerId(Long managerId, PagingRecord record) {
        return employeeRepository
                .findAllEmployeesByManagerId(managerId, buildPageable(record))
                .map(this::simplifyEmployee)
                .toList();
    }

    public Employee findEmployeeById(Long id, boolean showFullChain) {
        if(showFullChain) {
            return employeeRepository.findById(id).orElseThrow(NoSuchElementException::new);
        }
        return simplifyEmployee(employeeRepository.findById(id).orElseThrow(NoSuchElementException::new));
    }

    public List<Employee> findAllEmployeeByDepartmentName(String depName, PagingRecord record) {
        return employeeRepository
               .findAllEmployeesByDepartmentName(depName, buildPageable(record))
               .getContent()
               .stream()
               .map(this::simplifyEmployee)
               .collect(Collectors.toList());
    }

    public List<Employee> findAllEmployeeByDepartmentId(Long depId, PagingRecord record) {
        return employeeRepository
                .findAllEmployeesByDepartmentId(depId, buildPageable(record))
                .getContent()
                .stream()
                .map(this::simplifyEmployee)
                .collect(Collectors.toList());
    }

    private Employee simplifyEmployee(Employee employee) {
        Employee copy = new Employee(employee);
        if (copy.getManager() != null) {
            copy.getManager().setManager(null);
        }
        return copy;
    }
}