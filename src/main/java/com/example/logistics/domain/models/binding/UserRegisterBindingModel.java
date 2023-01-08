package com.example.logistics.domain.models.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterBindingModel {
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    private String password;
}