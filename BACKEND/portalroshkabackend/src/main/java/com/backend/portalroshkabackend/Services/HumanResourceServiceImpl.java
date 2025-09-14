package com.backend.portalroshkabackend.Services;

import com.backend.portalroshkabackend.DTO.PositionDto;
import com.backend.portalroshkabackend.DTO.PositionInsertDto;
import com.backend.portalroshkabackend.DTO.PositionUpdateDto;
import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.RequestRejectedDto;
import com.backend.portalroshkabackend.DTO.UserDto;
import com.backend.portalroshkabackend.DTO.UserInsertDto;
import com.backend.portalroshkabackend.DTO.UserUpdateDto;
import com.backend.portalroshkabackend.Models.Solicitudes;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.*;
import com.backend.portalroshkabackend.Models.Cargos;

import com.backend.portalroshkabackend.tools.errors.errorslist.*;
import com.backend.portalroshkabackend.tools.mapper.AutoMap;
import com.backend.portalroshkabackend.tools.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
public class HumanResourceServiceImpl implements IHumanResourceService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CargosRepository cargosRepository;
    private final RolesRepository rolesRepository;
    private final EquiposRepository equiposRepository;

    private final Validator validator;

    @Autowired
    public HumanResourceServiceImpl(UserRepository userRepository,
                                    RequestRepository requestRepository,
                                    CargosRepository cargosRepository,
                                    RolesRepository rolesRepository,
                                    EquiposRepository equiposRepository,
                                    Validator validator) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.cargosRepository = cargosRepository;
        this.rolesRepository = rolesRepository;
        this.equiposRepository = equiposRepository;

        this.validator = validator;
    }

    @Transactional
    @Override
    public boolean updateEmail(int id, String newEmail) {
        Usuario user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        validator.validateUniqueEmail(newEmail, id); // Si el correo ya esta asginado a otro usuario, lanza excepcion

        user.setCorreo(newEmail);

        // Este metodo recibe una funcion como primer parametro, y mensaje de error como segundo parametro,
        // dentro de saveEntity se maneja el try/catch por si ocurre error al guardar la entidad.

        Usuario savedUser = saveEntity(() -> userRepository.save(user), "Error al guarda el usuario: ");
        return true;
    }

    @Override
    public boolean updatePhone(int id, String newPhone) {
        return false;
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
        Page<Usuario> users = userRepository.findAllActiveEmployees(pageable);

        return users.map(AutoMap::toUserDto); // Retorna una lista de empleados activos (DTOs)
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserDto> getAllInactiveEmployees(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllInactiveEmployees(pageable);

        return users.map(AutoMap::toUserDto); // Retorna la lista de empleados inactivos
    }

    @Override
    public Page<UserDto> getAllEmployeesByRol(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByOrderByIdRolAsc(pageable);

        return users.map(AutoMap::toUserDto);
    }

    @Override
    public Page<UserDto> getAllEmployeesByTeam(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByOrderByIdEquipoAsc(pageable);

        return users.map(AutoMap::toUserDto);
    }

    @Override
    public Page<UserDto> getAllEmployeesByPosition(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByOrderByIdCargoAsc(pageable);

        return users.map(AutoMap::toUserDto);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getEmployeeById(int id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)); //Lanza una excepcion personalizada

        return AutoMap.toUserDto(user); //Si no existe usuario con esa id retorna null, sino retorna un Dto

    }

    @Transactional
    @Override
    public UserDto addEmployee(UserInsertDto insertDto) {

        validator.validateUniqueCedula(insertDto.getNroCedula(), null); // Validaciones de reglas de negocio
        validator.validateUniqueEmail(insertDto.getCorreo(), null);
        validator.validateRelatedEntities(insertDto.getIdRol(), insertDto.getIdEquipo(), insertDto.getIdCargo());

        Usuario user = AutoMap.toUsuarioFromInsertDto(insertDto); // Para mapear el InserDto a entidad Usuario

        Usuario savedUser = saveEntity( () -> userRepository.save(user), "Error al guardar el usuario: ");

        return AutoMap.toUserDto(savedUser);

    }

    @Transactional
    @Override
    public UserDto updateEmployee(UserUpdateDto updateDto, int id) {
        Usuario user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        validator.validateUniqueCedula(updateDto.getNroCedula(), id); // Validaciones de reglas de negocio
        validator.validateUniqueEmail(updateDto.getCorreo(), id);
        validator.validateRelatedEntities(updateDto.getIdRol(), updateDto.getIdEquipo(), updateDto.getIdCargo());

        user.setNombre(updateDto.getNombre());
        user.setApellido(updateDto.getApellido());
        user.setNroCedula(updateDto.getNroCedula());
        user.setCorreo(updateDto.getCorreo());
        user.setIdRol(updateDto.getIdRol());
        user.setFechaIngreso(updateDto.getFechaIngreso()); // TODO: Preguntar si usar PUT o PATCH
        user.setEstado(updateDto.isEstado());
        user.setContrasena(updateDto.getContrasena());
        user.setTelefono(updateDto.getTelefono());
        user.setIdEquipo(updateDto.getIdEquipo());
        user.setIdCargo(updateDto.getIdCargo());
        user.setFechaNacimiento(updateDto.getFechaNacimiento());

        Usuario updatedUser = saveEntity( () -> userRepository.save(user), "Error al actualizar el usuario: ");

        return AutoMap.toUserDto(updatedUser);

    }

    @Transactional
    @Override
    public UserDto deleteEmployee(int id) {
        Usuario user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!user.isEstado()) {
            throw new UserAlreadyInactiveException(id);
        }

        user.setEstado(false); // Da de baja el empleado de la base de datos

        Usuario deletedUser = saveEntity( () -> userRepository.save(user), "Error al eliminar el usuario: ");

        return AutoMap.toUserDto(deletedUser);

    }

    // ----------------- Solicitudes -----------------

    @Transactional(readOnly = true)
    @Override
    public Page<RequestDto> getAllRequests(Pageable pageable) {
        Page<Solicitudes> requests = requestRepository.findAll(pageable);
        return requests.map(AutoMap::toRequestDto); // Retorna todas las solicitudes (DTOs)
    }

    @Transactional
    @Override
    public boolean acceptRequest(int idRequest) {
        Solicitudes request = requestRepository.findById(idRequest)
                .orElseThrow(() -> new RequestNotFoundException(idRequest));

        // Si se acepta la solicitud, rechazado y estado de la solicitu se setea a false
        request.setRechazado(false);
        request.setEstado(false);

        return true;
    }

    @Transactional
    @Override
    public boolean rejectRequest(int idRequest, RequestRejectedDto rejectedDto) {
        Solicitudes request = requestRepository.findById(idRequest)
                .orElseThrow(() -> new RequestNotFoundException(idRequest));

        request.setRechazado(true); // Setea la solicitud como rechazada
        request.setComentario(rejectedDto.getComentario());

        saveEntity( () -> requestRepository.save(request), "Error al rechazar la solicitud: ");
        return true;

    }

    @Transactional
    @Override
    public RequestDto addNewRequestType() {
        return null;
    }


    // ----------------- Cargos -----------------

    @Transactional(readOnly = true)
    @Override
    public Page<PositionDto> getAllPositions(Pageable pageable) {
        Page<Cargos> positions = cargosRepository.findAll(pageable);

        return positions.map(AutoMap::toPositionDto);
    }

    @Transactional(readOnly = true)
    @Override
    public PositionDto getPositionById(int id) {
        var position = cargosRepository.findById(id)
                .orElseThrow(() -> new CargoNotFoundException(id));

        return AutoMap.toPositionDto(position);
    }

    @Transactional
    @Override
    public PositionDto addPosition(PositionInsertDto positionInsertDto) {
        Cargos position = new Cargos();

        position.setNombre(positionInsertDto.getNombre());

        Cargos savedPosition = saveEntity( () -> cargosRepository.save(position), "Error al añadir el cargo: ");

        return AutoMap.toPositionDto(savedPosition);

    }

    @Transactional
    @Override
    public PositionDto updatePosition(PositionUpdateDto positionUpdateDto, int id) {
        Cargos position = cargosRepository.findById(id)
                .orElseThrow(() -> new CargoNotFoundException(id));

        position.setNombre(positionUpdateDto.getNombre());

        Cargos updatedPosition = saveEntity( () -> cargosRepository.save(position), "Error al actualizar el cargo: ");

        return AutoMap.toPositionDto(updatedPosition);

    }

    @Transactional
    @Override
    public PositionDto deletePosition(int id) {
        Cargos position = cargosRepository.findById(id)
                .orElseThrow(() -> new CargoNotFoundException(id));

        //TODO: Aca se va a lanzar exepcion CargoAlreadyInactive si ya se encuentra inactivo el cargo, falta la nueva base de datos solamente

        //TODO: Aca se daria de baja el cargo, ej: position.setEstado(false);

        Cargos deletedPosition = saveEntity( () -> cargosRepository.save(position), "Error al eliminar cargo");

        return AutoMap.toPositionDto(deletedPosition);

    }



    // ------------------ HELPERS ------------------



    private <T> T saveEntity(Supplier<T> saveOperation, String errorMessage){
        try{
            return saveOperation.get();
        } catch (JpaSystemException ex){
            throw new DatabaseOperationException(errorMessage, ex);
        }
    }
}