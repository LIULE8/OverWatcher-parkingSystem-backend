package com.oocl.overwatcher.dto;

import com.oocl.overwatcher.entities.Role;
import com.oocl.overwatcher.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class RoleDTO {
    private Long id;
    private String name;
    private List<EmployeeDto> employeeDto;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<EmployeeDto> getEmployeeDto() {
        return employeeDto;
    }

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.employeeDto = role.getUsers().stream().map(user -> new EmployeeDto(user)).collect(Collectors.toList());
    }
}
