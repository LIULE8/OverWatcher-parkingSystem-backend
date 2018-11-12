package com.oocl.overwatcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author LIULE9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    /**
     * 角色id
     */
    private Long id;

    /**
     * 角色名字
     */
    private String name;

    /**
     * 角色管理的用户的DTO
     */
    private List<UserDTO> userDTOList;

}
