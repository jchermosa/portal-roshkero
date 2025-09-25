package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.th.cargos.CargoByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargoInsertDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosDefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosResponseDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolDefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolInsertDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolesResponseDto;
import com.backend.portalroshkabackend.Models.Roles;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.TH.RolesRepository;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.roles.RolesNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.RolesMapper;
import com.backend.portalroshkabackend.tools.validator.RolValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backend.portalroshkabackend.tools.MessagesConst.*;

@Service("rolesService") // Para hacer keyed dependency injection anashe
public class RolesServiceImpl implements ICommonRolesCargosService<RolesResponseDto, RolByIdResponseDto, RolDefaultResponseDto, RolInsertDto, RolInsertDto>{
    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;
    private final RepositoryService repositoryService;
    private final RolValidator rolValidator;

    @Autowired
    public RolesServiceImpl(RolesRepository rolesRepository,
                            RepositoryService repositoryService,
                            UserRepository userRepository,
                            RolValidator rolValidator){
        this.rolesRepository = rolesRepository;
        this.userRepository = userRepository;
        this.repositoryService = repositoryService;
        this.rolValidator = rolValidator;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RolesResponseDto> getAll(Pageable pageable) {
        Page<Roles> roles = rolesRepository.findAll(pageable);

        return roles.map(RolesMapper::toRolesResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public RolByIdResponseDto getById(int idRol) {
        Roles rol = repositoryService.findByIdOrThrow(
                rolesRepository,
                idRol,
                () -> new RolesNotFoundException(idRol)
        );

        List<Usuario> usersWithProvidedRol = userRepository.findAllByRol_IdRol(idRol);

        return RolesMapper.toRolByIdResponseDto(rol, usersWithProvidedRol);
    }

    @Transactional
    @Override
    public RolDefaultResponseDto add(RolInsertDto insertDto) {
        Roles rol = new Roles();

        rolValidator.validateRolUniqueName(insertDto.getNombre(), null);

        RolesMapper.toRolesFromInsertDto(rol, insertDto);

        Roles addedRol = repositoryService.save(
                rolesRepository,
                rol,
                DATABASE_DEFAULT_ERROR
        );

        return RolesMapper.toRolesDefaultResponseDto(addedRol.getIdRol(), ROL_CREATED_MESSAGE);
    }

    @Transactional
    @Override
    public RolDefaultResponseDto update(int idRol, RolInsertDto updateDto) {
        Roles rol = repositoryService.findByIdOrThrow(
                rolesRepository,
                idRol,
                () -> new RolesNotFoundException(idRol)
        );

        rolValidator.validateRolUniqueName(updateDto.getNombre(), idRol);

        rol.setNombre(updateDto.getNombre());

        Roles rolUpdated = repositoryService.save(
                rolesRepository,
                rol,
                DATABASE_DEFAULT_ERROR
        );

        return RolesMapper.toRolesDefaultResponseDto(rolUpdated.getIdRol(), ROL_UPDATED_MESSAGE);
    }

    @Transactional
    @Override
    public RolDefaultResponseDto delete(int idRol) {
        Roles rol = repositoryService.findByIdOrThrow(
                rolesRepository,
                idRol,
                () -> new RolesNotFoundException(idRol)
        );

        rolValidator.validateRolDontHaveUsersAssigned(idRol, rol.getNombre());

        repositoryService.delete(
                rolesRepository,
                rol,
                DATABASE_DEFAULT_ERROR
        );

        return RolesMapper.toRolesDefaultResponseDto(idRol, ROL_DELETED_MESSAGE);
    }
}
