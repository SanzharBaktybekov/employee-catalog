package com.epam.rd.autotasks.springemployeecatalog.repository;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> getEmployeesByManagerId(Long managerId, Pageable pageable);
    Page<Employee> getEmployeesByDepartmentName(String department_id, Pageable pageable);
    Page<Employee> getEmployeesByDepartmentId(Long department_id, Pageable pageable);
}
