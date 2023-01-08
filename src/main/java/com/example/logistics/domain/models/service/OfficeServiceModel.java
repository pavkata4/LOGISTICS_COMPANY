package com.example.logistics.domain.models.service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.example.logistics.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfficeServiceModel extends BaseServiceModel {
    @NotEmpty
    @NotNull
    private String address;
    private Set<User> employees = new HashSet<>();
}