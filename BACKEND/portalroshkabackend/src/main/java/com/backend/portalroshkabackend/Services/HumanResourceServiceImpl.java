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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Slf4j
@Service
public class HumanResourceServiceImpl implements IHumanResourceService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CargosRepository cargosRepository;
    private final RolesRepository rolesRepository;
    private final EquiposRepository equiposRepository;

    @Autowired
    public HumanResourceServiceImpl(UserRepository userRepository,
                                    RequestRepository requestRepository,
                                    CargosRepository cargosRepository,
                                    RolesRepository rolesRepository,
                                    EquiposRepository equiposRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.cargosRepository = cargosRepository;
        this.rolesRepository = rolesRepository;
        this.equiposRepository = equiposRepository;
    }

    @Transactional
    @Override
    public boolean updateEmail(int id, String newEmail) {
        Usuario user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        validateUniqueEmail(newEmail, id); // Si el correo ya esta asginado a otro usuario, lanza excepcion

        user.setCorreo(newEmail);

        // Este try catch lanza una excepcion si ocurre algun error al usar el metodo save() de Jpa

        try {
            userRepository.save(user);

            return true;
        } catch (JpaSystemException ex) {
            throw new DatabaseOperationException("Error al actualizar email: ", ex);
        }
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

        validateUniqueCedula(insertDto.getNroCedula(), null); // Validaciones de reglas de negocio
        validateUniqueEmail(insertDto.getCorreo(), null);
        validateRelatedEntities(insertDto.getIdRol(), insertDto.getIdEquipo(), insertDto.getIdCargo());


        Usuario user = AutoMap.toUsuarioFromInsertDto(insertDto); // Para mapear el InserDto a entidad Usuario

        try {
            Usuario savedUser = userRepository.save(user);

            return AutoMap.toUserDto(savedUser);
        } catch (JpaSystemException ex) {
            throw new DatabaseOperationException("Error al guardadr usuario: ", ex);
        }


    }

    @Transactional
    @Override
    public UserDto updateEmployee(UserUpdateDto updateDto, int id) {
        Usuario user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        validateUniqueCedula(updateDto.getNroCedula(), id); // Validaciones de reglas de negocio
        validateUniqueEmail(updateDto.getCorreo(), id);
        validateRelatedEntities(updateDto.getIdRol(), updateDto.getIdEquipo(), updateDto.getIdCargo());

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

        try {
            Usuario updatedUser = userRepository.save(user);

            return AutoMap.toUserDto(updatedUser);
        } catch (JpaSystemException ex) {
            throw new DatabaseOperationException("Error al actualizar usuario: ", ex);
        }


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

        try {
            Usuario deletedUser = userRepository.save(user);

            return AutoMap.toUserDto(deletedUser);
        } catch (JpaSystemException ex) {
            throw new DatabaseOperationException("Error al eliminar usuario: ", ex);
        }

    }

    // ----------------- Request -----------------

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


        try {
            requestRepository.save(request);

            return true;
        } catch (JpaSystemException ex) {
            throw new DatabaseOperationException("Error al rechazar la solicitud: ", ex.getCause());
        }

    }

    @Transactional
    @Override
    public RequestDto addNewRequestType() {
        return null;
    }


    // ----------------- Position -----------------

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

        try {
            Cargos savedPosition = cargosRepository.save(position);

            return AutoMap.toPositionDto(savedPosition);
        } catch (JpaSystemException ex) {
            throw new DatabaseOperationException("Error al añadir cargo: ", ex);
        }

    }

    @Transactional
    @Override
    public PositionDto updatePosition(PositionUpdateDto positionUpdateDto, int id) {
        Cargos position = cargosRepository.findById(id)
                .orElseThrow(() -> new CargoNotFoundException(id));

        position.setNombre(positionUpdateDto.getNombre());

        try {
            Cargos updatedPosition = cargosRepository.save(position);

            return AutoMap.toPositionDto(updatedPosition);
        } catch (JpaSystemException ex) {
            throw new DatabaseOperationException("Error al actualizar cargo: ", ex);
        }

    }

    @Transactional
    @Override
    public PositionDto deletePosition(int id) {
        Cargos position = cargosRepository.findById(id)
                .orElseThrow(() -> new CargoNotFoundException(id));

        //TODO: Aca se va a lanzar exepcion CargoAlreadyInactive si ya se encuentra inactivo el cargo, falta la nueva base de datos solamente

        //TODO: Aca se daria de baja el cargo, ej: position.setEstado(false);

        try {
            Cargos deletedPosition = cargosRepository.save(position);

            return AutoMap.toPositionDto(deletedPosition);
        } catch (JpaSystemException ex) {
            throw new DatabaseOperationException("Error al eliminar cargo: ", ex);
        }

    }



    // ------------------ HELPERS ------------------

    private void validateUniqueEmail(String correo, Integer excludeUserId){
        boolean exists = (excludeUserId == null)
                ? userRepository.existsByCorreo(correo)
                : userRepository.existsByCorreoAndIdUsuarioNot(correo, excludeUserId);

        if (exists){
            throw new DuplicateEmailException(correo);
        }
    }

    private void validateUniqueCedula(Integer nroCedula, Integer excludeUserId){
        boolean exists = (excludeUserId == null)
                ? userRepository.existsByNroCedula(nroCedula)
                : userRepository.existsByNroCedulaAndIdUsuarioNot(nroCedula, excludeUserId);

        if (exists){
            throw new DuplicateCedulaException(nroCedula);
        }
    }

    private void validateRelatedEntities(Integer idRol, Integer idEquipo, Integer idCargo){
        if (!rolesRepository.existsById(idRol)) throw new RolesNotFoundException(idRol);

        if (!equiposRepository.existsById(idEquipo)) throw new EquipoNotFoundException(idEquipo);

        if (!cargosRepository.existsById(idCargo)) throw new CargoNotFoundException(idCargo);
    }

}