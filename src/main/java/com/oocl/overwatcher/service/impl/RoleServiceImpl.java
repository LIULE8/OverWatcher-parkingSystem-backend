package com.oocl.overwatcher.service.impl;

import com.oocl.overwatcher.entities.Role;
import com.oocl.overwatcher.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LIULE9
 */
@Service
public class RoleServiceImpl {

  private final RoleRepository roleRepository;

  @Autowired
  public RoleServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public Role findRoleByName(String roleName) {
    return roleRepository.findByName(roleName);
  }
}
