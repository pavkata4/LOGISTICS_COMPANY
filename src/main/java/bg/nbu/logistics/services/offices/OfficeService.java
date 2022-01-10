package bg.nbu.logistics.services.offices;

import bg.nbu.logistics.domain.models.service.UserServiceModel;

public interface OfficeService {
    void addEmployee(long officeId, UserServiceModel userServiceModel);
}
