package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.*;
import com.backend.portalroshkabackend.DTO.th.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.UserResponseDto;
import com.backend.portalroshkabackend.DTO.th.self.DefaultResponseDto;
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
    }

    @Transactional(readOnly = true)
    @Override
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
    }

    @Transactional(readOnly = true)
    @Override
    public UserByIdResponseDto getEmployeeById(int id) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)); //Lanza una excepcion personalizada

        return AutoMap.toUserByIdDto(user); //Si no existe usuario con esa id retorna null, sino retorna un Dto

    }

    @Transactional
    @Override
    public DefaultResponseDto addEmployee(UserInsertDto insertDto) {

        validator.validateUniqueCedula(insertDto.getNroCedula(), null); // Validaciones de reglas de negocio
        validator.validateUniqueEmail(insertDto.getCorreo(), null);
        validator.validateUniquePhone(insertDto.getTelefono(), null);
        validator.validateRelatedEntities(insertDto.getRoles(), insertDto.getCargos());  // LLama al metodo que verifica si el rol/equipo o cargo asignado existen

        Usuario user = AutoMap.toUsuarioFromInsertDto(insertDto); // Para mapear el InserDto a entidad Usuario

        Usuario savedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al guardar el usuario: ");

        return AutoMap.toDefaultResponseDto(savedUser.getIdUsuario(), "Usuario creado con exito.");

    }

    @Transactional
    @Override
    public DefaultResponseDto updateEmployee(UserUpdateDto updateDto, int id) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        validator.validateUniqueCedula(updateDto.getNroCedula(), id); // Validaciones de reglas de negocio
        validator.validateUniqueEmail(updateDto.getCorreo(), id);
        validator.validateUniquePhone(updateDto.getTelefono(), id);
        validator.validateRelatedEntities(updateDto.getRoles(), updateDto.getCargos());

        AutoMap.toUsuarioFromUpdateDto(user, updateDto);

        Usuario updatedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al actualizar el usuario: ");

        return AutoMap.toDefaultResponseDto(updatedUser.getIdUsuario(), "Usuario actualizado con exito.");

    }

    @Transactional
    @Override
    public DefaultResponseDto deleteEmployee(int id) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (user.getEstado() == EstadoActivoInactivo.I) throw new UserAlreadyInactiveException(id);

        user.setEstado(EstadoActivoInactivo.I); // Da de baja el empleado de la base de datos

        Usuario deletedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al eliminar el usuario: ");

        return AutoMap.toDefaultResponseDto(deletedUser.getIdUsuario(), "Usuario eliminado con exito.");

    }
}
