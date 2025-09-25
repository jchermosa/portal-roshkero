package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.*;
import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.DTO.th.employees.DefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.TH.*;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.tools.SaveManager;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.UserNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.EmployeeMapper;
import com.backend.portalroshkabackend.tools.validator.EmployeeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeServiceImpl implements IEmployeeService {
    private final UserRepository userRepository;

    private final EmployeeValidator employeeValidator;

    @Autowired
    public EmployeeServiceImpl(UserRepository userRepository,
                                    EmployeeValidator employeeValidator) {
        this.userRepository = userRepository;

        this.employeeValidator = employeeValidator;
    }

    @Override
    public DefaultResponseDto resetUserPassword(int id) {
        Usuario user = userRepository.findById(id).orElseThrow( () -> new UserNotFoundException(id));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        user.setContrasena(encoder.encode(user.getNroCedula()));
        user.setRequiereCambioContrasena(true);

        Usuario savedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al restablecer la contraseña.");

        return EmployeeMapper.toDefaultResponseDto(savedUser.getIdUsuario(), "Contraseña restablecida.");
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
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)); //Lanza una excepcion personalizada

        return EmployeeMapper.toUserByIdDto(user);

    }

    @Transactional
    @Override
    public DefaultResponseDto addEmployee(UserInsertDto insertDto) {

        employeeValidator.validateUniqueCedula(insertDto.getNroCedula(), null); // Validaciones de reglas de negocio
        employeeValidator.validateUniqueEmail(insertDto.getCorreo(), null);
        employeeValidator.validateUniquePhone(insertDto.getTelefono(), null);
        employeeValidator.validateRelatedEntities(insertDto.getRol(), insertDto.getCargo());  // LLama al metodo que verifica si el rol/equipo o cargo asignado existen

        Usuario user = EmployeeMapper.toUsuarioFromInsertDto(insertDto); // Para mapear el InserDto a entidad Usuario

        Usuario savedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al guardar el usuario: ");

        return EmployeeMapper.toDefaultResponseDto(savedUser.getIdUsuario(), "Usuario creado con exito.");

    }

    @Transactional
    @Override
    public DefaultResponseDto updateEmployee(UserUpdateDto updateDto, int id) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        employeeValidator.validateUniqueCedula(updateDto.getNroCedula(), id); // Validaciones de reglas de negocio
        employeeValidator.validateUniqueEmail(updateDto.getCorreo(), id);
        employeeValidator.validateUniquePhone(updateDto.getTelefono(), id);
        employeeValidator.validateRelatedEntities(updateDto.getRoles(), updateDto.getCargos());

        EmployeeMapper.toUsuarioFromUpdateDto(user, updateDto);

        Usuario updatedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al actualizar el usuario: ");

        return EmployeeMapper.toDefaultResponseDto(updatedUser.getIdUsuario(), "Usuario actualizado con exito.");

    }

    @Transactional
    @Override
    public DefaultResponseDto deleteEmployee(int id) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        employeeValidator.validateEmployeeDontHavePendientRequests(id, user.getNombre(), user.getApellido());
        employeeValidator.validateEmployeeIsActive(user.getEstado(), user.getNombre(), user.getApellido());


        user.setEstado(EstadoActivoInactivo.I); // Da de baja el empleado de la base de datos

        Usuario deletedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al eliminar el usuario: ");

        return EmployeeMapper.toDefaultResponseDto(deletedUser.getIdUsuario(), "Usuario eliminado con exito.");

    }
}
