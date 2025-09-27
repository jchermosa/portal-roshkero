package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.DTO.th.employees.DefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.UserNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.EmployeeMapper;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backend.portalroshkabackend.tools.MessagesConst.*;

@Service
public class EmployeeServiceImpl implements IEmployeeService {
    private final UserRepository userRepository;
    private final RepositoryService repositoryService;
    private final ValidatorStrategy<UserInsertDto> insertValidator;
    private final ValidatorStrategy<Usuario> deleteValidator;
    private final ValidatorStrategy<UserUpdateDto> updateValidator;

    @Autowired
    public EmployeeServiceImpl(UserRepository userRepository,
                               RepositoryService repositoryService,
                               @Qualifier("employeeInsertValidator") ValidatorStrategy<UserInsertDto> insertValidator,
                               @Qualifier("employeeDeleteValidator") ValidatorStrategy<Usuario> deleteValidator,
                               @Qualifier("employeeUpdateValidator") ValidatorStrategy<UserUpdateDto> updateValidator) {
        this.userRepository = userRepository;
        this.repositoryService = repositoryService;
        this.insertValidator = insertValidator;
        this.deleteValidator = deleteValidator;
        this.updateValidator = updateValidator;
    }

    @Override
    public DefaultResponseDto resetUserPassword(int id) {
        Usuario user = repositoryService.findByIdOrThrow(
                userRepository,
                id,
                () -> new UserNotFoundException(id)
        );

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        user.setContrasena(encoder.encode(user.getNroCedula()));
        user.setRequiereCambioContrasena(true);

        Usuario savedUser = repositoryService.save(
                userRepository,
                user,
                DATABASE_DEFAULT_ERROR
        );

        return EmployeeMapper.toDefaultResponseDto(savedUser.getIdUsuario(), PASSWORD_RESETED_MESSAGE);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponseDto> getAllEmployees(Pageable pageable) {
        Page<Usuario> users = userRepository.findAll(pageable);

        return users.map(EmployeeMapper::toUserResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponseDto> getAllActiveEmployees(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByEstado(EstadoActivoInactivo.A, pageable);

        return users.map(EmployeeMapper::toUserResponseDto); // Retorna una lista de empleados activos (DTOs)
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponseDto> getAllInactiveEmployees(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByEstado(EstadoActivoInactivo.I, pageable);

        return users.map(EmployeeMapper::toUserResponseDto); // Retorna la lista de empleados inactivos
    }

    @Override
    public Page<UserResponseDto> getAllEmployeesByRol(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByOrderByRolAsc(pageable);

        return users.map(EmployeeMapper::toUserResponseDto);
    }


    @Override
    public Page<UserResponseDto> getAllEmployeesByPosition(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByOrderByCargoAsc(pageable);

        return users.map(EmployeeMapper::toUserResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public UserByIdResponseDto getEmployeeById(int id) {
        var user = repositoryService.findByIdOrThrow(
                userRepository,
                id,
                () -> new UserNotFoundException(id)
        );

        return EmployeeMapper.toUserByIdDto(user);

    }

    @Override
    public UserDto getEmployeeByCedula(String cedula) {
        Usuario user = userRepository.findByNroCedula(cedula).orElseThrow( () -> new UserNotFoundException(cedula));

        return EmployeeMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public DefaultResponseDto addEmployee(UserInsertDto insertDto) {

        insertValidator.validate(insertDto);

        Usuario user = EmployeeMapper.toUsuarioFromInsertDto(insertDto); // Para mapear el InserDto a entidad Usuario

        Usuario savedUser = repositoryService.save(
                userRepository,
                user,
                DATABASE_DEFAULT_ERROR
        );

        return EmployeeMapper.toDefaultResponseDto(savedUser.getIdUsuario(), EMPLOYEE_CREATED_MESSAGE);

    }

    @Transactional
    @Override
    public DefaultResponseDto updateEmployee(UserUpdateDto updateDto, int id) {
        Usuario user = repositoryService.findByIdOrThrow(
                userRepository,
                id,
                () -> new UserNotFoundException(id)
        );

        updateValidator.validate(updateDto);

        EmployeeMapper.toUsuarioFromUpdateDto(user, updateDto);

        Usuario updatedUser = repositoryService.save(
                userRepository,
                user,
                DATABASE_DEFAULT_ERROR
        );

        return EmployeeMapper.toDefaultResponseDto(updatedUser.getIdUsuario(), EMPLOYEE_UPDATED_MESSAGE);

    }

    @Transactional
    @Override
    public DefaultResponseDto deleteEmployee(int id) {
        Usuario user = repositoryService.findByIdOrThrow(
                userRepository,
                id,
                () -> new UserNotFoundException(id)
        );

        deleteValidator.validate(user);

        user.setEstado(EstadoActivoInactivo.I); // Da de baja el empleado de la base de datos

        Usuario deletedUser = repositoryService.save(
                userRepository,
                user,
                DATABASE_DEFAULT_ERROR
        );

        return EmployeeMapper.toDefaultResponseDto(deletedUser.getIdUsuario(), EMPLOYEE_DELETED_MESSAGE);

    }
}
