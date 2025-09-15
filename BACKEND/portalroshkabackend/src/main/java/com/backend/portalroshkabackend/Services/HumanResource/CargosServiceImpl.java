package com.backend.portalroshkabackend.Services.HumanResource;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CargosServiceImpl implements ICargosService{
    private final CargosRepository cargosRepository;

    private final Validator validator;

    @Autowired
    public CargosServiceImpl(CargosRepository cargosRepository,
                               Validator validator) {
        this.cargosRepository = cargosRepository;

        this.validator = validator;
    }

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

        Cargos savedPosition = SaveManager.saveEntity( () -> cargosRepository.save(position), "Error al añadir el cargo: ");

        return AutoMap.toPositionDto(savedPosition);

    }

    @Transactional
    @Override
    public PositionDto updatePosition(PositionUpdateDto positionUpdateDto, int id) {
        Cargos position = cargosRepository.findById(id)
                .orElseThrow(() -> new CargoNotFoundException(id));

        position.setNombre(positionUpdateDto.getNombre());

        Cargos updatedPosition = SaveManager.saveEntity( () -> cargosRepository.save(position), "Error al actualizar el cargo: ");

        return AutoMap.toPositionDto(updatedPosition);

    }

    @Transactional
    @Override
    public PositionDto deletePosition(int id) {
        Cargos position = cargosRepository.findById(id)
                .orElseThrow(() -> new CargoNotFoundException(id));

        //TODO: Aca se va a lanzar exepcion CargoAlreadyInactive si ya se encuentra inactivo el cargo, falta la nueva base de datos solamente

        //TODO: Aca se daria de baja el cargo, ej: position.setEstado(false);

        Cargos deletedPosition = SaveManager.saveEntity( () -> cargosRepository.save(position), "Error al eliminar cargo");

        return AutoMap.toPositionDto(deletedPosition);

    }
}
