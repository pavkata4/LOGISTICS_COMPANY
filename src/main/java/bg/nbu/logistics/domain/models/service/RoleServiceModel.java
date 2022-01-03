package bg.nbu.logistics.domain.models.service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleServiceModel extends BaseServiceModel {
    @NotNull
    @NotEmpty
    private String authority;
}