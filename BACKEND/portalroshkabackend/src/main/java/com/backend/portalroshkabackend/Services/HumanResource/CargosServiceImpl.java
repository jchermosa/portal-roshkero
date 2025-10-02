package com.backend.portalroshkabackend.Services.HumanResource;

<<<<<<< HEAD
import com.backend.portalroshkabackend.DTO.PositionDto;
import com.backend.portalroshkabackend.DTO.PositionInsertDto;
import com.backend.portalroshkabackend.DTO.PositionUpdateDto;
import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Repositories.CargosRepository;
import com.backend.portalroshkabackend.Repositories.UserRepository;
import com.backend.portalroshkabackend.tools.SaveManager;
import com.backend.portalroshkabackend.tools.errors.errorslist.CargoNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.AutoMap;
import com.backend.portalroshkabackend.tools.validator.Validator;
=======
import com.backend.portalroshkabackend.DTO.th.cargos.*;
import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.TH.CargosRepository;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.CargosMapper;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
>>>>>>> parent of dca61a3 (se elimino backend)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
@Service
public class CargosServiceImpl implements ICargosService{
    private final CargosRepository cargosRepository;

    private final Validator validator;

    @Autowired
    public CargosServiceImpl(CargosRepository cargosRepository,
                               Validator validator) {
        this.cargosRepository = cargosRepository;

        this.validator = validator;
=======
import java.util.List;

import static com.backend.portalroshkabackend.tools.MessagesConst.*;

@Service("cargosService") // Para hacer keyed dependency injection :D
public class CargosServiceImpl implements ICommonRolesCargosService<CargosResponseDto, CargoByIdResponseDto, CargosDefaultResponseDto, CargoInsertDto, CargoUpdateDto> {
    private final CargosRepository cargosRepository;
    private final UserRepository userRepository;
    private final ValidatorStrategy<CargoInsertDto> insertValidator;
    private final ValidatorStrategy<CargoUpdateDto> updateValidator;
    private final ValidatorStrategy<Cargos> deleteValidator;

    private final RepositoryService repositoryService;


    @Autowired
    public CargosServiceImpl(CargosRepository cargosRepository,
                             UserRepository userRepository,
                             ValidatorStrategy<CargoInsertDto> insertValidator,
                             ValidatorStrategy<CargoUpdateDto> updateValidator,
                             ValidatorStrategy<Cargos> deleteValidator,
                             RepositoryService repositoryService){
        this.cargosRepository = cargosRepository;
        this.userRepository = userRepository;
        this.insertValidator = insertValidator;
        this.updateValidator = updateValidator;
        this.deleteValidator = deleteValidator;
        this.repositoryService = repositoryService;
>>>>>>> parent of dca61a3 (se elimino backend)
    }

    @Transactional(readOnly = true)
    @Override
<<<<<<< HEAD
    public Page<PositionDto> getAllPositions(Pageable pageable) {
        Page<Cargos> positions = cargosRepository.findAll(pageable);

        return positions.map(AutoMap::toPositionDto);
=======
    public Page<CargosResponseDto> getAll(Pageable pageable){
        Page<Cargos> cargos = cargosRepository.findAll(pageable);

        return cargos.map(CargosMapper::toCargosResponseDto);
>>>>>>> parent of dca61a3 (se elimino backend)
    }

    @Transactional(readOnly = true)
    @Override
<<<<<<< HEAD
    public PositionDto getPositionById(int id) {
        var position = cargosRepository.findById(id)
                .orElseThrow(() -> new CargoNotFoundException(id));

        return AutoMap.toPositionDto(position);
=======
    public CargoByIdResponseDto getById(int idCargo) {
        Cargos cargo = repositoryService.findByIdOrThrow(
                cargosRepository,
                idCargo,
                () -> new CargoNotFoundException(idCargo)
        );

        List<Usuario> usersWithProvidedCargo = userRepository.findAllByCargo_IdCargo(idCargo);

        return CargosMapper.toCargoByIdResponseDto(cargo, usersWithProvidedCargo);
>>>>>>> parent of dca61a3 (se elimino backend)
    }

    @Transactional
    @Override
<<<<<<< HEAD
    public PositionDto addPosition(PositionInsertDto positionInsertDto) {
        Cargos position = new Cargos();

        position.setNombre(positionInsertDto.getNombre());

        Cargos savedPosition = SaveManager.saveEntity( () -> cargosRepository.save(position), "Error al añadir el cargo: ");

        return AutoMap.toPositionDto(savedPosition);

=======
    public CargosDefaultResponseDto add(CargoInsertDto insertDto) {
        Cargos cargo = new Cargos();

        insertValidator.validate(insertDto);

        CargosMapper.toCargosFromInsertDto(cargo, insertDto);

        Cargos addedCargo = repositoryService.save(
                cargosRepository,
                cargo,
                DATABASE_DEFAULT_ERROR
        );

        return CargosMapper.toCargosDefaultResponseDto(addedCargo.getIdCargo(), CARGO_CREATED_MESSAGE);
>>>>>>> parent of dca61a3 (se elimino backend)
    }

    @Transactional
    @Override
<<<<<<< HEAD
    public PositionDto updatePosition(PositionUpdateDto positionUpdateDto, int id) {
        Cargos position = cargosRepository.findById(id)
                .orElseThrow(() -> new CargoNotFoundException(id));

        position.setNombre(positionUpdateDto.getNombre());

        Cargos updatedPosition = SaveManager.saveEntity( () -> cargosRepository.save(position), "Error al actualizar el cargo: ");

        return AutoMap.toPositionDto(updatedPosition);

=======
    public CargosDefaultResponseDto update(int idCargo, CargoUpdateDto updateDto) {
        Cargos cargo = repositoryService.findByIdOrThrow(
                cargosRepository,
                idCargo,
                () -> new CargoNotFoundException(idCargo)
        );

        updateValidator.validate(updateDto);

        cargo.setNombre(updateDto.getNombre());

        Cargos cargoUpdated = repositoryService.save(
                cargosRepository,
                cargo,
                DATABASE_DEFAULT_ERROR
        );

        return CargosMapper.toCargosDefaultResponseDto(cargoUpdated.getIdCargo(), CARGO_UPDATED_MESSAGE);
>>>>>>> parent of dca61a3 (se elimino backend)
    }

    @Transactional
    @Override
<<<<<<< HEAD
    public PositionDto deletePosition(int id) {
        Cargos position = cargosRepository.findById(id)
                .orElseThrow(() -> new CargoNotFoundException(id));

        //TODO: Aca se va a lanzar exepcion CargoAlreadyInactive si ya se encuentra inactivo el cargo, falta la nueva base de datos solamente

        //TODO: Aca se daria de baja el cargo, ej: position.setEstado(false);

        Cargos deletedPosition = SaveManager.saveEntity( () -> cargosRepository.save(position), "Error al eliminar cargo");

        return AutoMap.toPositionDto(deletedPosition);
=======
    public CargosDefaultResponseDto delete(int idCargo) {
        Cargos cargo = repositoryService.findByIdOrThrow(
                cargosRepository,
                idCargo,
                () -> new CargoNotFoundException(idCargo)
        );

        deleteValidator.validate(cargo);

        repositoryService.delete(
                cargosRepository,
                cargo,
                DATABASE_DEFAULT_ERROR
        );

        return CargosMapper.toCargosDefaultResponseDto(idCargo, CARGO_DELETED_MESSAGE); // Eliminar registro
>>>>>>> parent of dca61a3 (se elimino backend)

    }
}
