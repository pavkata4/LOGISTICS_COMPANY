package bg.nbu.logistics.services.offices;

import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_COURIER;
import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_EMPLOYEE;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.*;

import bg.nbu.logistics.services.users.UserService;
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
    private final UserService userService;
    private final Mapper mapper;
    
    @Autowired
    public OfficeServiceImpl(ModelMapper modelMapper, OfficeRepository officeRepository, UserService userService, Mapper mapper) {
        this.modelMapper = modelMapper;
        this.officeRepository = officeRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public void addEmployee(long officeId, UserServiceModel userServiceModel) {
        if (isUserAnEmployee(userServiceModel)) {
            return;
        }

        final Optional<Office> office = officeRepository.findById(officeId);
        if (!office.isPresent()) {
            log.error(OFFICE_NOT_FOUND_ERROR_TEMPLATE, officeId, userServiceModel.getUsername());
                    return;
        }

        userService.changeRoleById(userServiceModel.getId(), ROLE_EMPLOYEE);


        Set<User> usersInOffice = office.get().getEmployees();
        usersInOffice.add((modelMapper.map(userServiceModel, User.class)));
        office.get().setEmployees(usersInOffice);
        officeRepository.saveAndFlush(office.get());
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
    public Optional<OfficeServiceModel> findOfficeById(long id) {
        final Optional<Office> office = officeRepository.findById(id);
        if (office.isEmpty()) {
            return empty();
        }

        return of(modelMapper.map(office.get(), OfficeServiceModel.class));
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
                .anyMatch(role -> role.equals(ROLE_EMPLOYEE) || role.equals(ROLE_COURIER));
    }
}
