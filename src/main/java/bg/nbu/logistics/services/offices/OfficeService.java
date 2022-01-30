package bg.nbu.logistics.services.offices;

import java.util.List;
import java.util.Optional;

import bg.nbu.logistics.domain.entities.Office;
import bg.nbu.logistics.domain.models.service.OfficeServiceModel;
import bg.nbu.logistics.domain.models.service.UserServiceModel;

public interface OfficeService {
    void addEmployee(long officeId, UserServiceModel userServiceModel);

    void addCourier(long officeId, UserServiceModel userServiceModel);

    OfficeServiceModel createOffice(OfficeServiceModel officeServiceModel);

    OfficeServiceModel updateOffice(OfficeServiceModel officeServiceModel);
    
    List<OfficeServiceModel> getOffices();

    void removeOffice(long id);

    Optional<OfficeServiceModel> findOfficeById(long id);
    
    Optional<OfficeServiceModel> findOfficeByAddress(String address);
}
