package com.epam.rd.autotasks.springemployeecatalog.services;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import java.util.List;

import static com.epam.rd.autotasks.springemployeecatalog.utils.PagingCriteria.PagingRecord;

public interface EmployeeService {
    Employee findEmployeeById(Long id, boolean showFullChain);
    List<Employee> findAllEmployees(PagingRecord record);
    List<Employee> findAllEmployeesByManagerId(Long managerId, PagingRecord page);
    List<Employee> findAllEmployeeByDepartmentName(String depName, PagingRecord pagingRecord);
    List<Employee> findAllEmployeeByDepartmentId(Long depId, PagingRecord pagingRecord);
}