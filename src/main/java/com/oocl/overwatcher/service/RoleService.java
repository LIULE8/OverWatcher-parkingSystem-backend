package com.oocl.overwatcher.service;

import com.oocl.overwatcher.entities.Role;
import com.oocl.overwatcher.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    public List<Role> findAllParkingBoys(){
        return roleRepository.findByName("员工");
    }
}
