package com.example.logistics.domain.models.service;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceModel extends BaseServiceModel {
    @NotEmpty
    @NotNull
    private String username;

    @NotNull
    private String password;

    private Set<RoleServiceModel> authorities = new HashSet<>();

}