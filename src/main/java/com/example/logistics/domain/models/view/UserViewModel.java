package com.example.logistics.domain.models.view;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserViewModel extends BaseViewModel {
    private String username;
    private Set<RoleViewModel> authorities;
}