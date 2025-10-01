package com.backend.portalroshkabackend.Services.HumanResource;

<<<<<<< HEAD
import com.backend.portalroshkabackend.DTO.*;
import com.backend.portalroshkabackend.DTO.th.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.UserResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.*;
import com.backend.portalroshkabackend.tools.SaveManager;
import com.backend.portalroshkabackend.tools.errors.errorslist.UserAlreadyInactiveException;
import com.backend.portalroshkabackend.tools.errors.errorslist.UserNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.AutoMap;
import com.backend.portalroshkabackend.tools.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeServiceImpl implements IEmployeeService {
    private final UserRepository userRepository;

    private final Validator validator;

    @Autowired
    public EmployeeServiceImpl(UserRepository userRepository,
                                    Validator validator) {
        this.userRepository = userRepository;

        this.validator = validator;
    }

    // ----------- TH COMO ADMINISTRADOR -----------

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponseDto> getAllEmployees(Pageable pageable) {
        Page<Usuario> users = userRepository.findAll(pageable); // Guarda todos los usuarios en users

        return users.map(AutoMap::toUserDto); //Retorna una lista de dtos
=======
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.DTO.th.employees.DefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.Repositories.TH.UsuarioSpecifications;
import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.UserNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.EmployeeMapper;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
>>>>>>> parent of dca61a3 (se elimino backend)
    }

    @Transactional(readOnly = true)
    @Override
<<<<<<< HEAD
    public Page<UserResponseDto> getAllActiveEmployees(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByEstado(EstadoActivoInactivo.A, pageable);

        return users.map(AutoMap::toUserDto); // Retorna una lista de empleados activos (DTOs)
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponseDto> getAllInactiveEmployees(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByEstado(EstadoActivoInactivo.I, pageable);

        return users.map(AutoMap::toUserDto); // Retorna la lista de empleados inactivos
    }

    @Override
    public Page<UserResponseDto> getAllEmployeesByRol(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByOrderByRolesAsc(pageable);

        return users.map(AutoMap::toUserDto);
    }


    @Override
    public Page<UserResponseDto> getAllEmployeesByPosition(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByOrderByCargosAsc(pageable);

        return users.map(AutoMap::toUserDto);
=======
    public Page<UserResponseDto> getAllEmployeesByFilters(Integer rolId, Integer cargoId, EstadoActivoInactivo estado, Pageable pageable) {
        Specification<Usuario> spec = Specification.allOf(
                UsuarioSpecifications.hasRol(rolId),
                UsuarioSpecifications.hasCargo(cargoId),
                UsuarioSpecifications.hasEstado(estado)
        );

        Page<Usuario> users = userRepository.findAll(spec, pageable);

        return users.map(EmployeeMapper::toUserResponseDto);
>>>>>>> parent of dca61a3 (se elimino backend)
    }

    @Transactional(readOnly = true)
    @Override
    public UserByIdResponseDto getEmployeeById(int id) {
<<<<<<< HEAD
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)); //Lanza una excepcion personalizada

        return AutoMap.toUserByIdDto(user); //Si no existe usuario con esa id retorna null, sino retorna un Dto
=======
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
>>>>>>> parent of dca61a3 (se elimino backend)

    }

    @Transactional
    @Override
<<<<<<< HEAD
    public UserResponseDto addEmployee(UserInsertDto insertDto) {

        validator.validateUniqueCedula(insertDto.getNroCedula(), null); // Validaciones de reglas de negocio
        validator.validateUniqueEmail(insertDto.getCorreo(), null);
        validator.validateUniquePhone(insertDto.getTelefono(), null);
        validator.validateRelatedEntities(insertDto.getRoles(), insertDto.getCargos());  // LLama al metodo que verifica si el rol/equipo o cargo asignado existen

        Usuario user = AutoMap.toUsuarioFromInsertDto(insertDto); // Para mapear el InserDto a entidad Usuario

        Usuario savedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al guardar el usuario: ");

        return AutoMap.toUserDto(savedUser);
=======
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
>>>>>>> parent of dca61a3 (se elimino backend)

    }

    @Transactional
    @Override
<<<<<<< HEAD
    public UserResponseDto updateEmployee(UserUpdateDto updateDto, int id) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        validator.validateUniqueCedula(updateDto.getNroCedula(), id); // Validaciones de reglas de negocio
        validator.validateUniqueEmail(updateDto.getCorreo(), id);
        validator.validateUniquePhone(updateDto.getTelefono(), id);
        validator.validateRelatedEntities(updateDto.getRoles(), updateDto.getCargos());

        AutoMap.toUsuarioFromUpdateDto(user, updateDto);

        Usuario updatedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al actualizar el usuario: ");

        return AutoMap.toUserDto(updatedUser);

    }

    @Transactional
    @Override
    public UserResponseDto deleteEmployee(int id) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (user.getEstado()== EstadoActivoInactivo.I) {
            throw new UserAlreadyInactiveException(id);
        }

        user.setEstado(EstadoActivoInactivo.I); // Da de baja el empleado de la base de datos

        Usuario deletedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al eliminar el usuario: ");

        return AutoMap.toUserDto(deletedUser);
=======
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
>>>>>>> parent of dca61a3 (se elimino backend)

    }
}
