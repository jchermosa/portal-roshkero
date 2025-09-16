package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.EmailUpdatedDto;
import com.backend.portalroshkabackend.DTO.PhoneUpdatedDto;
import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.Models.SolicitudesTH;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.*;
import com.backend.portalroshkabackend.tools.SaveManager;
import com.backend.portalroshkabackend.tools.errors.errorslist.UserNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.AutoMap;
import com.backend.portalroshkabackend.tools.validator.Validator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThSelfServiceImpl implements IThSelfService{
    private final BeneficiosRepository beneficiosRepository;
    private final SolicitudTHTipoRepository solicitudTHTipoRepository;
    private final TipoDispositivoRepository tipoDispositivoRepository;
    private final PermisosRepository permisosRepository;
    private final UserRepository userRepository;
    private final SolicitudesTHRepository solicitudesTHRepository;

    private final Validator validator;

    public ThSelfServiceImpl(BeneficiosRepository beneficiosRepository,
                             SolicitudTHTipoRepository solicitudTHTipoRepository,
                             TipoDispositivoRepository tipoDispositivoRepository,
                             PermisosRepository permisosRepository,
                             UserRepository userRepository,
                             SolicitudesTHRepository solicitudesTHRepository,
                             Validator validator
    ){
        this.beneficiosRepository = beneficiosRepository;
        this.solicitudTHTipoRepository = solicitudTHTipoRepository;
        this.tipoDispositivoRepository = tipoDispositivoRepository;
        this.permisosRepository = permisosRepository;
        this.solicitudesTHRepository = solicitudesTHRepository;
        this.userRepository = userRepository;

        this.validator = validator;
    }

    @Override
    public EmailUpdatedDto updateEmail(int id, String newEmail) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        validator.validateUniqueEmail(newEmail, id); // Si el correo ya esta asginado a otro usuario, lanza excepcion

        user.setCorreo(newEmail);

        Usuario savedUser = SaveManager.saveEntity(() -> userRepository.save(user), "Error al guardar el usuario: ");// Este metodo recibe una funcion como primer parametro, y mensaje de error como segundo parametro, dentro de saveEntity se maneja el try/catch por si ocurre error al guardar la entidad.

        return AutoMap.toEmailUpdatedDto(savedUser);
    }

    @Override
    public PhoneUpdatedDto updatePhone(int id, String newPhone) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        validator.validateUniquePhone(newPhone, id);

        user.setTelefono(newPhone);

        Usuario savedUser = SaveManager.saveEntity(() -> userRepository.save(user), "Error al guardar el usuario: ");

        return AutoMap.toPhoneUpdatedDto(savedUser);
    }

    @Override
    public RequestResponseDto sendRequest(int id, SendSolicitudDto sendSolicitudDto) {
        SolicitudesTH solicitudesTH = new SolicitudesTH();

        AutoMap.toSolicitudesThFromSendDto(solicitudesTH, sendSolicitudDto);

        SaveManager.saveEntity(() -> solicitudesTHRepository.save(solicitudesTH), "Error al guardar la solicitud: ");

        RequestResponseDto responseDto = new RequestResponseDto();
        responseDto.setMessage("Solicitud enviada con exito");

        return responseDto;
    }

    @Override
    public List<BenefitsTypesResponseDto> getAllBenefitsTypes() {
        return beneficiosRepository.findAll().stream().map(AutoMap::toBenefitsResponseDto).toList();
    }

    @Override
    public List<DevicesTypesResponseDto> getAllDevicesTypes() {
        return tipoDispositivoRepository.findAll().stream().map(AutoMap::toDevicesTypesResponseDto).toList();
    }

    @Override
    public List<PermissionsTypesResponseDto> getAllPermissionsTypes() {
        return permisosRepository.findAll().stream().map(AutoMap::toPermissionsTypesResponseDto).toList();
    }

    @Override
    public List<SolicitudTHTipoResponseDto> getAllRequestTypes() {
        return solicitudTHTipoRepository.findAll().stream().map(AutoMap::toSolicitudTHTipoResponseDto).toList();
    }
}
