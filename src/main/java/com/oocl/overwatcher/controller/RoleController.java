package com.oocl.overwatcher.controller;

import com.oocl.overwatcher.dto.ChangeParkingLotDTO;
import com.oocl.overwatcher.dto.EmployeeDto;
import com.oocl.overwatcher.dto.ParkingLotDTO;
import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.entities.User;
import com.oocl.overwatcher.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/parkingBoys")

    public List<EmployeeDto> findAllParkingBoys() {
        List<User> parkingBoys = new ArrayList<>();
        roleService.findAllParkingBoys().forEach(role -> parkingBoys.addAll(role.getUsers()));
        return parkingBoys.stream().map(EmployeeDto::new).collect(Collectors.toList());
    }


    @GetMapping("/parkingBoys/condition")

    public List<EmployeeDto> findAllParkingBoysByCondition(String condition, String value) {
        List<User> parkingBoys = new ArrayList<>();
        roleService.findAllParkingBoys().forEach(role -> parkingBoys.addAll(role.getUsers()));
        if (StringUtils.isNotBlank(condition) && "name".equals(condition)) {
            return parkingBoys.stream().filter(parkingBoy -> parkingBoy.getName().contains(value)).map(EmployeeDto::new).collect(Collectors.toList());
        } else if (StringUtils.isNotBlank(condition) && "email".equals(condition)) {
            return parkingBoys.stream().filter(parkingBoy -> parkingBoy.getEmail().contains(value)).map(EmployeeDto::new).collect(Collectors.toList());
        } else if (StringUtils.isNotBlank(condition) && "phone".equals(condition)) {
            return parkingBoys.stream().filter(parkingBoy -> parkingBoy.getPhone().contains(value)).map(EmployeeDto::new).collect(Collectors.toList());
        } else if (StringUtils.isNotBlank(condition) && "status".equals(condition)) {
            return parkingBoys.stream().filter(parkingBoy -> parkingBoy.getStatus().contains(value)).map(EmployeeDto::new).collect(Collectors.toList());
        }
       return parkingBoys.stream().map(EmployeeDto::new).collect(Collectors.toList());
    }
}
