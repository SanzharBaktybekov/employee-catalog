package com.epam.rd.autotasks.springemployeecatalog.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.persistence.*;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "EMPLOYEE")
public class Employee {
    @Id
    @Column(name = "ID")
    private Long id;
    @Embedded
    private FullName fullName;
    @Enumerated(EnumType.STRING)
    @Column(name = "POSITION", length = 9)
    private Position position;
    @ManyToOne
    @JoinColumn(name = "MANAGER")
    private Employee manager;
    @Column(name = "HIREDATE")
    private LocalDate hired;

    @Column(name = "SALARY")
    private BigDecimal salary;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT")
    private Department department;

    @JsonCreator
    public Employee(@JsonProperty("id") final Long id,
                    @JsonProperty("fullName") final FullName fullName,
                    @JsonProperty("position") final Position position,
                    @JsonProperty("hired") final LocalDate hired,
                    @JsonProperty("salary") final BigDecimal salary,
                    @JsonProperty("manager") final Employee manager,
                    @JsonProperty("department") final Department department) {
        this.id = id;
        this.fullName = fullName;
        this.position = position;
        this.hired = hired;
        this.salary = salary.setScale(5, RoundingMode.HALF_UP);
        this.manager = manager;
        this.department = department;
    }

    public Employee() {

    }

    public Employee(Employee other) {
        this.id = other.id;
        this.fullName = other.fullName;
        this.position = other.position;
        this.hired = other.hired;
        this.salary = other.salary;
        this.manager = other.manager != null ? new Employee(other.manager) : null;
        this.department = other.department;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FullName getFullName() {
        return fullName;
    }

    public void setFullName(FullName fullName) {
        this.fullName = fullName;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }


    public LocalDate getHired() {
        return hired;
    }

    public void setHired(LocalDate hired) {
        this.hired = hired;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
                Objects.equals(fullName, employee.fullName) &&
                position == employee.position &&
                Objects.equals(manager, employee.manager) &&
                Objects.equals(hired, employee.hired) &&
                Objects.equals(salary, employee.salary) &&
                Objects.equals(department, employee.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, position, manager, hired, salary, department);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", fullName=" + fullName +
                ", position=" + position +
                ", manager=" + manager +
                ", hireDate=" + hired +
                ", salary=" + salary +
                ", department=" + department +
                '}';
    }

    public static class Parser {
        private static ObjectMapper mapper = new ObjectMapper();

        static {
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        }

        public static String toJson(Employee employee) {
            try {
                final StringWriter writer = new StringWriter();
                mapper.writeValue(writer, employee);
                return writer.toString();
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        public static Employee parseJson(String json) {
            try {
                return mapper.readValue(json, Employee.class);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }
    }

}
