package com.epam.rd.autotasks.springemployeecatalog.aspects;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.services.EmployeeService;
import com.epam.rd.autotasks.springemployeecatalog.services.EmployeeServiceImpl;
import com.epam.rd.autotasks.springemployeecatalog.utils.PagingCriteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class EmployeeServiceDecorator implements EmployeeService {
    private final Consumer<PagingCriteria.PagingRecord> recordUpdater = PagingCriteria
            .startConfig()
            .when(r -> r.getSort().equals("lastName"))
            .then(r -> r.setSort("fullName.lastName"))
            .build();

    private final EmployeeServiceImpl employeeServiceImpl;
    public EmployeeServiceDecorator(EmployeeServiceImpl employeeServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
    }
    @Override
    public Employee findEmployeeById(Long id, boolean showFullChain) {
        return this.employeeServiceImpl.findEmployeeById(id, showFullChain);
    }
    @Override
    public List<Employee> findAllEmployees(PagingCriteria.PagingRecord record) {
        recordUpdater.accept(record);
        return this.employeeServiceImpl.findAllEmployees(record);
    }

    @Override
    public List<Employee> findAllEmployeesByManagerId(Long managerId, PagingCriteria.PagingRecord record) {
        recordUpdater.accept(record);
        return this.employeeServiceImpl.findAllEmployeesByManagerId(managerId, record);
    }

    @Override
    public List<Employee> findAllEmployeeByDepartmentName(String depName, PagingCriteria.PagingRecord record) {
        recordUpdater.accept(record);
        return this.employeeServiceImpl.findAllEmployeeByDepartmentName(depName, record);
    }

    @Override
    public List<Employee> findAllEmployeeByDepartmentId(Long depId, PagingCriteria.PagingRecord record) {
        recordUpdater.accept(record);
        return this.employeeServiceImpl.findAllEmployeeByDepartmentId(depId, record);
    }
}