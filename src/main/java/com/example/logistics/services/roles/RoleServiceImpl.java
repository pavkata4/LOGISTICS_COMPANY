package com.example.logistics.services.roles;

import static java.util.stream.Collectors.toUnmodifiableSet;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.logistics.domain.models.service.RoleServiceModel;
import com.example.logistics.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<RoleServiceModel> findAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(role -> modelMapper.map(role, RoleServiceModel.class))
                .collect(toUnmodifiableSet());
    }

    @Override
    public RoleServiceModel findByAuthority(String authority) {
        return modelMapper.map(roleRepository.findByAuthority(authority), RoleServiceModel.class);
    }
}