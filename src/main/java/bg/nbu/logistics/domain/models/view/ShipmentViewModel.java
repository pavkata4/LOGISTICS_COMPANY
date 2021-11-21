package bg.nbu.logistics.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShipmentViewModel extends BaseViewModel {
    private String sender;
    private String recipient;
    private String address;
    private int weight;
}