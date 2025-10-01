package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.EmailUpdatedDto;
import com.backend.portalroshkabackend.DTO.PhoneUpdatedDto;
import com.backend.portalroshkabackend.DTO.th.self.*;
import com.backend.portalroshkabackend.Models.SolicitudesTH;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.*;
import com.backend.portalroshkabackend.tools.SaveManager;
import com.backend.portalroshkabackend.tools.errors.errorslist.RequestNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.UserNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.AutoMap;
import com.backend.portalroshkabackend.tools.validator.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ThSelfServiceImpl implements IThSelfService {
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
    ) {
        this.beneficiosRepository = beneficiosRepository;
        this.solicitudTHTipoRepository = solicitudTHTipoRepository;
        this.tipoDispositivoRepository = tipoDispositivoRepository;
        this.permisosRepository = permisosRepository;
        this.solicitudesTHRepository = solicitudesTHRepository;
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

    @Transactional
    @Override
    public RequestResponseDto sendRequest(int id, SendSolicitudDto sendSolicitudDto) {
        SolicitudesTH solicitudesTH = new SolicitudesTH();

        System.out.println(sendSolicitudDto.getFechaInicio());

        AutoMap.toSolicitudesThFromSendDto(solicitudesTH, sendSolicitudDto);

        System.out.println(solicitudesTH.getFechaInicio());

        SaveManager.saveEntity(() -> solicitudesTHRepository.save(solicitudesTH), "Error al guardar la solicitud: ");

        RequestResponseDto responseDto = new RequestResponseDto();
        responseDto.setId(solicitudesTH.getIdSolicitudTH());
        responseDto.setMessage("Solicitud enviada con exito");

        System.out.println(solicitudesTH.getFechaInicio());

        return responseDto;
    }

    @Override
    public RequestResponseDto updateRequest(int idSolicitudTh, UpdateSolicitudDto updateSolicitudDto) {
        SolicitudesTH request  = solicitudesTHRepository.findById(idSolicitudTh).orElseThrow(() -> new RequestNotFoundException(idSolicitudTh));

        AutoMap.toSolicitudesThFromUpdateSolicitudDto(request, updateSolicitudDto);

        SaveManager.saveEntity(() -> solicitudesTHRepository.save(request), "Error al actualizar la solicitud: ");

        RequestResponseDto responseDto = new RequestResponseDto();
        responseDto.setId(request.getIdSolicitudTH());
        responseDto.setMessage("Solicitud actualizada con exito");

        return responseDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BenefitsTypesResponseDto> getAllBenefitsTypes() {
        return beneficiosRepository.findAll().stream().map(AutoMap::toBenefitsResponseDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<DevicesTypesResponseDto> getAllDevicesTypes() {
        return tipoDispositivoRepository.findAll().stream().map(AutoMap::toDevicesTypesResponseDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<PermissionsTypesResponseDto> getAllPermissionsTypes() {
        return permisosRepository.findAll().stream().map(AutoMap::toPermissionsTypesResponseDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<SolicitudTHTipoResponseDto> getAllRequestTypes() {
        return solicitudTHTipoRepository.findAll().stream().map(AutoMap::toSolicitudTHTipoResponseDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MisSolicitudesResponseDto> getAllSelfRequests(int idUsuario, Pageable pageable) {
        return solicitudesTHRepository.findAllByUsuario_idUsuario(idUsuario, pageable).map(AutoMap::toMisSolicitudesResponseDto);
    }

    @Override
    public SolicitudEspecificaResponseDto getRequestById(int idUsuario ,int idSolicitudTh) {
        SolicitudesTH request = solicitudesTHRepository.findByUsuario_IdUsuarioAndIdSolicitudTH(idUsuario, idSolicitudTh);

        if (request == null) throw new RequestNotFoundException(idSolicitudTh);

        return AutoMap.toSolicitudEspecificaResponseDto(request);
    }


}
