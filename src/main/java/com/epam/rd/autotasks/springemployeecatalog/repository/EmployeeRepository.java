package com.epam.rd.autotasks.springemployeecatalog.repository;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findAllEmployeesByManagerId(Long managerId, Pageable pageable);
    Page<Employee> findAllEmployeesByDepartmentName(String department_id, Pageable pageable);
    Page<Employee> findAllEmployeesByDepartmentId(Long department_id, Pageable pageable);
}
