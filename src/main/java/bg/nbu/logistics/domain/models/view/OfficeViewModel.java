package bg.nbu.logistics.domain.models.view;

import bg.nbu.logistics.domain.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class OfficeViewModel extends BaseViewModel {
    private String address;
    private Set<User> employees = new HashSet<>();
}
