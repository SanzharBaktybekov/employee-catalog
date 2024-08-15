package com.epam.rd.autotasks.springemployeecatalog.repository;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> getEmployeesByManager_Id(Long managerId, Pageable pageable);
    Page<Employee> getEmployeesByDepartment_Name(String department_id, Pageable pageable);
    Page<Employee> getEmployeesByDepartment_Id(Long department_id, Pageable pageable);
}
