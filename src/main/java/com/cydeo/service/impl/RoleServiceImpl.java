package com.cydeo.service.impl;


import com.cydeo.dto.RoleDto;
import com.cydeo.repository.RoelRepository;
import com.cydeo.service.RoleService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoelRepository repository;

    private final MapperUtil mapperUtil;

    public RoleServiceImpl(RoelRepository repository, MapperUtil mapperUtil) {
        this.repository = repository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public RoleDto getRoleByDescription(String description) {
        return mapperUtil.convert(repository.findByDescription(description), new RoleDto());
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return repository.findAll().stream().map(role -> mapperUtil.convert(role,new RoleDto())).collect(Collectors.toList());
    }

    @Override
    public RoleDto getRoleById(Long Id) {
        return mapperUtil.convert(repository.findById(Id).get(),new RoleDto());
    }
}
