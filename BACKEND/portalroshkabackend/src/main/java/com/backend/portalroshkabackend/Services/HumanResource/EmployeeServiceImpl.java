package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.*;
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

@Service("humanEmployeeService")
public class EmployeeServiceImpl implements IEmployeeService, ITHService{
    private final UserRepository userRepository;

    private final Validator validator;

    @Autowired
    public EmployeeServiceImpl(UserRepository userRepository,
                                    Validator validator) {
        this.userRepository = userRepository;

        this.validator = validator;
    }

    @Transactional
    @Override
    public EmailUpdatedDto updateEmail(int id, String newEmail) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        validator.validateUniqueEmail(newEmail, id); // Si el correo ya esta asginado a otro usuario, lanza excepcion

        user.setCorreo(newEmail);

        Usuario savedUser = SaveManager.saveEntity(() -> userRepository.save(user), "Error al guardar el usuario: ");// Este metodo recibe una funcion como primer parametro, y mensaje de error como segundo parametro, dentro de saveEntity se maneja el try/catch por si ocurre error al guardar la entidad.

        return AutoMap.toEmailUpdatedDto(savedUser);
    }

    @Transactional
    @Override
    public PhoneUpdatedDto updatePhone(int id, String newPhone) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        validator.validateUniquePhone(newPhone, id);

        user.setTelefono(newPhone);

        Usuario savedUser = SaveManager.saveEntity(() -> userRepository.save(user), "Error al guardar el usuario: ");

        return AutoMap.toPhoneUpdatedDto(savedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserDto> getAllEmployees(Pageable pageable) {
        Page<Usuario> users = userRepository.findAll(pageable); // Guarda todos los usuarios en users

        return users.map(AutoMap::toUserDto); //Retorna una lista de dtos
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserDto> getAllActiveEmployees(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByEstadoTrue(pageable);

        return users.map(AutoMap::toUserDto); // Retorna una lista de empleados activos (DTOs)
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserDto> getAllInactiveEmployees(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByEstadoFalse(pageable);

        return users.map(AutoMap::toUserDto); // Retorna la lista de empleados inactivos
    }

    @Override
    public Page<UserDto> getAllEmployeesByRol(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByOrderByIdRolAsc(pageable);

        return users.map(AutoMap::toUserDto);
    }

    // @Override
    // public Page<UserDto> getAllEmployeesByTeam(Pageable pageable) {
    //     Page<Usuario> users = userRepository.findAllByOrderByIdEquipoAsc(pageable);

    //     return users.map(AutoMap::toUserDto);
    // }

    @Override
    public Page<UserDto> getAllEmployeesByPosition(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByOrderByIdCargoAsc(pageable);

        return users.map(AutoMap::toUserDto);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getEmployeeById(int id) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)); //Lanza una excepcion personalizada

        return AutoMap.toUserDto(user); //Si no existe usuario con esa id retorna null, sino retorna un Dto

    }

    @Transactional
    @Override
    public UserDto addEmployee(UserInsertDto insertDto) {

        validator.validateUniqueCedula(insertDto.getNroCedula(), null); // Validaciones de reglas de negocio
        validator.validateUniqueEmail(insertDto.getCorreo(), null);
        validator.validateUniquePhone(insertDto.getTelefono(), null);
        validator.validateRelatedEntities(insertDto.getIdRol(), insertDto.getIdEquipo(), insertDto.getIdCargo());  // LLama al metodo que verifica si el rol/equipo o cargo asignado existen

        Usuario user = AutoMap.toUsuarioFromInsertDto(insertDto); // Para mapear el InserDto a entidad Usuario

        Usuario savedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al guardar el usuario: ");

        return AutoMap.toUserDto(savedUser);

    }

    @Transactional
    @Override
    public UserDto updateEmployee(UserUpdateDto updateDto, int id) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        validator.validateUniqueCedula(updateDto.getNroCedula(), id); // Validaciones de reglas de negocio
        validator.validateUniqueEmail(updateDto.getCorreo(), id);
        validator.validateUniquePhone(updateDto.getTelefono(), id);
        validator.validateRelatedEntities(updateDto.getIdRol(), updateDto.getIdEquipo(), updateDto.getIdCargo());

        user.setNombre(updateDto.getNombre());
        user.setApellido(updateDto.getApellido());
        user.setNroCedula(updateDto.getNroCedula());
        user.setCorreo(updateDto.getCorreo());
        // user.setIdRol(updateDto.getIdRol());
        user.setFechaIngreso(updateDto.getFechaIngreso()); // TODO: Preguntar si usar PUT o PATCH
        // user.setEstado(updateDto.isEstado());
        user.setContrasena(updateDto.getContrasena());
        user.setTelefono(updateDto.getTelefono());
        // user.setIdEquipo(updateDto.getIdEquipo());
        // user.setIdCargo(updateDto.getIdCargo());
        user.setFechaNacimiento(updateDto.getFechaNacimiento());

        Usuario updatedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al actualizar el usuario: ");

        return AutoMap.toUserDto(updatedUser);

    }

    @Transactional
    @Override
    public UserDto deleteEmployee(int id) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        // if (!user.isEstado()) throw new UserAlreadyInactiveException(id);

        // user.setEstado(false); // Da de baja el empleado de la base de datos

        Usuario deletedUser = SaveManager.saveEntity( () -> userRepository.save(user), "Error al eliminar el usuario: ");

        return AutoMap.toUserDto(deletedUser);

    }
}
