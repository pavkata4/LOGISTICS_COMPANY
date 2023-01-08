package com.example.logistics.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleViewModel extends BaseViewModel {
    private String authority;

    @Override
    public String toString() {
        return authority;
    }
}
