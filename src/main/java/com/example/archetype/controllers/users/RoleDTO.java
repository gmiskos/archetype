package com.example.archetype.controllers.users;

import java.util.List;

public class RoleDTO {

    private Long id;

    private String name;
    private List<PrivilegeDTO> privileges;

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

    public List<PrivilegeDTO> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegeDTO> privileges) {
        this.privileges = privileges;
    }
}
