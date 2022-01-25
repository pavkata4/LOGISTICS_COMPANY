package bg.nbu.logistics.services.offices;

import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_EMPLOYEE;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.nbu.logistics.commons.utils.Mapper;
import bg.nbu.logistics.domain.entities.Office;
import bg.nbu.logistics.domain.entities.User;
import bg.nbu.logistics.domain.models.service.OfficeServiceModel;
import bg.nbu.logistics.domain.models.service.RoleServiceModel;
import bg.nbu.logistics.domain.models.service.UserServiceModel;
import bg.nbu.logistics.repositories.OfficeRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OfficeServiceImpl implements OfficeService {
    private static final String OFFICE_NOT_FOUND_ERROR_TEMPLATE = "Office with ID {} is not present. Thefore, user {} cannot be added to this office";

    private final ModelMapper modelMapper;
    private final OfficeRepository officeRepository;
    private final Mapper mapper;
    
    @Autowired
    public OfficeServiceImpl(ModelMapper modelMapper, OfficeRepository officeRepository, Mapper mapper) {
        this.modelMapper = modelMapper;
        this.officeRepository = officeRepository;
        this.mapper = mapper;
    }

    @Override
    public void addEmployee(long officeId, UserServiceModel userServiceModel) {
        if (!isUserAnEmployee(userServiceModel)) {
            return;
        }

        officeRepository.findById(officeId)
                .map(Office::getEmployees)
                .ifPresentOrElse(employees -> employees.add(modelMapper.map(userServiceModel, User.class)),
                        () -> log.error(OFFICE_NOT_FOUND_ERROR_TEMPLATE, officeId, userServiceModel.getUsername()));
    }

    @Override
    public void createOffice(Office office) {
        officeRepository.saveAndFlush(office);
    }

    @Override
    public List<OfficeServiceModel> getOffices() {
        return mapper.mapCollection(officeRepository.findAll(), OfficeServiceModel.class);
    }

    @Override
    public void removeOffice(long id) {
        officeRepository.deleteById(id);
    }

    @Override
    public Optional<OfficeServiceModel> findOfficeByAddress(String address) {
        final Optional<Office> office = officeRepository.findByAddress(address);
        if (office.isEmpty()) {
            return empty();
        }

        return of(modelMapper.map(office.get(), OfficeServiceModel.class));
    }
    
    private boolean isUserAnEmployee(UserServiceModel userServiceModel) {
        return userServiceModel.getAuthorities()
                .stream()
                .map(RoleServiceModel::getAuthority)
                .anyMatch(role -> role.equals(ROLE_EMPLOYEE) || role.equals(ROLE_EMPLOYEE));
    }
}
