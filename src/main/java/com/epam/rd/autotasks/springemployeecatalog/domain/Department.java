package com.epam.rd.autotasks.springemployeecatalog.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

@Entity
@Table(name = "DEPARTMENT")
public class Department {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", length = 14)
    private String name;

    @Column(name = "LOCATION", length = 13)
    private String location;

    // Getters, setters, equals, hashCode, toString methods
    @JsonCreator
    public Department(@JsonProperty("id") final Long id,
                      @JsonProperty("name") final String name,
                      @JsonProperty("location") final String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Department() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
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

        public static String toJson(Department department){
            try {
                final StringWriter writer = new StringWriter();
                mapper.writeValue(writer, department);
                return writer.toString();
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        public static Department parseJson(String json){
            try {
                return mapper.readValue(json, Department.class);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }
    }

}
