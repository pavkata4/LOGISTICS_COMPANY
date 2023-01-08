package com.example.logistics.services.offices;

import static com.example.logistics.commons.constants.RoleConstants.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.*;

import com.example.logistics.services.users.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.logistics.commons.utils.Mapper;
import com.example.logistics.domain.entities.Office;
import com.example.logistics.domain.entities.User;
import com.example.logistics.domain.models.service.OfficeServiceModel;
import com.example.logistics.domain.models.service.RoleServiceModel;
import com.example.logistics.domain.models.service.UserServiceModel;
import com.example.logistics.repositories.OfficeRepository;
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
    public void operationsWithUsers(long officeId, UserServiceModel userServiceModel, String authority) {
        if ((authority.equals("employee") || authority.equals("courier")) && isUserAnEmployee(userServiceModel)) {
            return;
        } else if (authority.equals("user") && !isUserAnEmployee(userServiceModel)) {
            return;
        }

        Optional<Office> office = officeRepository.findById(officeId);
        if (!office.isPresent()) {
            log.error(OFFICE_NOT_FOUND_ERROR_TEMPLATE, officeId, userServiceModel.getUsername());
            return;
        }

        if (authority.equals("employee")) {
            userService.changeRoleById(userServiceModel.getId(), ROLE_EMPLOYEE);
        } else if (authority.equals("courier")) {
            userService.changeRoleById(userServiceModel.getId(), ROLE_COURIER);
        } else {
            userService.changeRoleById(userServiceModel.getId(), ROLE_USER);
        }


        Set<User> usersInOffice = office.get().getEmployees();
        if (!authority.equals("user")) {
            usersInOffice.add((modelMapper.map(userServiceModel, User.class)));
        } else {
            for (User user : usersInOffice) {
                if (user.getId() == userServiceModel.getId()) {
                    usersInOffice.remove(user);
                    break;
                }
            }
        }
        office.get().setEmployees(usersInOffice);
        officeRepository.saveAndFlush(office.get());
    }

    @Override
    public OfficeServiceModel createOffice(OfficeServiceModel officeServiceModel) {

        final Office office = modelMapper.map(officeServiceModel, Office.class);
        office.setId(office.getId());
        office.setAddress(office.getAddress());
        office.setEmployees(office.getEmployees());

        return modelMapper.map(officeRepository.saveAndFlush(office), OfficeServiceModel.class);
    }

    @Override
    public OfficeServiceModel updateOffice(OfficeServiceModel officeServiceModel) {
        final Office office = modelMapper.map(officeServiceModel, Office.class);
        office.setId(office.getId());
        office.setAddress(office.getAddress());
        office.setEmployees(office.getEmployees());

        return officeRepository.findById(officeServiceModel.getId())
                .map(updateOffice -> modelMapper.map(officeRepository.saveAndFlush(office), OfficeServiceModel.class)).orElseThrow();
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
