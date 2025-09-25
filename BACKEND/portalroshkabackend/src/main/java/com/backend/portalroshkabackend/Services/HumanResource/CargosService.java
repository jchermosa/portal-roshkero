package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.th.cargos.CargoByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargoInsertDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosDefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosResponseDto;
import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.CargosRepository;
import com.backend.portalroshkabackend.Repositories.UserRepository;
import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.DatabaseOperationException;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.CargosMapper;
import com.backend.portalroshkabackend.tools.validator.CargoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CargosService implements ICargosService{
    private final CargosRepository cargosRepository;
    private final UserRepository userRepository;
    private final CargoValidator cargoValidator;
    private final RepositoryService repositoryService;

    private final String DATABASE_DEFAULT_ERROR = "Ocurrio un error en la operacion. ";
    private final String OPERATION_DEFAULT_MESSAGE = "Operacion exitosa.";

    @Autowired
    public CargosService(CargosRepository cargosRepository,
                         UserRepository userRepository,
                         CargoValidator cargoValidator,
                         RepositoryService repositoryService){
        this.cargosRepository = cargosRepository;
        this.userRepository = userRepository;
        this.cargoValidator = cargoValidator;
        this.repositoryService = repositoryService;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CargosResponseDto> getAllCargos(Pageable pageable){
        Page<Cargos> cargos = cargosRepository.findAll(pageable);

        return cargos.map(CargosMapper::toCargosResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public CargoByIdResponseDto getCargoById(int idCargo) {
        Cargos cargo = cargosRepository.findById(idCargo).orElseThrow( () -> new CargoNotFoundException(idCargo));
        List<Usuario> usersWithProvidedCargo = userRepository.findAllByCargo_IdCargo(idCargo);

        return CargosMapper.toCargoByIdResponseDto(cargo, usersWithProvidedCargo);
    }

    @Transactional
    @Override
    public CargosDefaultResponseDto addCargo(CargoInsertDto insertDto) {
        Cargos cargo = new Cargos();

        cargoValidator.validateCargoUniqueName(insertDto.getNombre(), null);

        CargosMapper.toCargosFromInsertDto(cargo, insertDto);

        Cargos addedCargo = repositoryService.save(
                cargosRepository,
                cargo,
                DATABASE_DEFAULT_ERROR
        );

        return CargosMapper.toCargosDefaultResponseDto(addedCargo.getIdCargo(), OPERATION_DEFAULT_MESSAGE);
    }

    @Transactional
    @Override
    public CargosDefaultResponseDto updateCargo(int idCargo, CargoInsertDto updateDto) {
        Cargos cargo = cargosRepository.findById(idCargo).orElseThrow( () -> new CargoNotFoundException(idCargo));

        cargoValidator.validateCargoUniqueName(updateDto.getNombre(), idCargo);

        cargo.setNombre(updateDto.getNombre());

        Cargos cargoUpdated = repositoryService.save(
                cargosRepository,
                cargo,
                DATABASE_DEFAULT_ERROR
        );

        return CargosMapper.toCargosDefaultResponseDto(cargoUpdated.getIdCargo(), OPERATION_DEFAULT_MESSAGE);
    }

    @Transactional
    @Override
    public CargosDefaultResponseDto deleteCargo(int idCargo) {
        Cargos cargo = cargosRepository.findById(idCargo).orElseThrow( () -> new CargoNotFoundException(idCargo));

        cargoValidator.validateCargoDontHaveUsersAssigned(idCargo, cargo.getNombre());

        repositoryService.delete(
                cargosRepository,
                cargo,
                DATABASE_DEFAULT_ERROR
        );

        return CargosMapper.toCargosDefaultResponseDto(idCargo, OPERATION_DEFAULT_MESSAGE); // Eliminar registro

    }
}
