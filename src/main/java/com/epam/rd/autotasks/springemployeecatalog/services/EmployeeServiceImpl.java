package com.epam.rd.autotasks.springemployeecatalog.services;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.repository.EmployeeRepository;
import com.epam.rd.autotasks.springemployeecatalog.utils.PagingCriteria;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.epam.rd.autotasks.springemployeecatalog.utils.PagingCriteria.PagingRecord;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    public List<Employee> findAllEmployees(PagingRecord record) {
        return getEmployees(
                employeeRepository
                        .findAll(record.toPageable()).toList()
        );
    }
    public List<Employee> findAllEmployeesByManagerId(Long managerId, PagingRecord page) {
        Page<Employee> result = employeeRepository.getEmployeesByManagerId(managerId, PagingCriteria.from(page));
        return getEmployees(result.getContent());
    }

    public Employee findEmployeeById(Long id, boolean showFullChain) {
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

    public List<Employee> findAllEmployeeByDepartmentName(String depName, PagingRecord pagingRecord) {
        return getEmployees(employeeRepository
                .getEmployeesByDepartmentName(depName, PagingCriteria.from(pagingRecord))
                .getContent());
    }

    public List<Employee> findAllEmployeeByDepartmentId(Long depId, PagingRecord pagingRecord) {
        return getEmployees(employeeRepository
                .getEmployeesByDepartmentId(depId, PagingCriteria.from(pagingRecord))
                .getContent()
        );
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
}