package com.backend.portalroshkabackend.Services.HumanResource.subservices;

import com.backend.portalroshkabackend.Models.PermisosAsignados;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.TipoPermisos;
import com.backend.portalroshkabackend.Repositories.PermisosRepository;
import com.backend.portalroshkabackend.Repositories.TH.PermisosAsignadosRepository;
import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.permisos.PermissionTypeNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestAlreadyAcceptedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.backend.portalroshkabackend.tools.MessagesConst.DATABASE_DEFAULT_ERROR;

@Service("acceptPermissionsService")
public class PermissionServiceImpl implements IAcceptRequestService {
    private final PermisosAsignadosRepository permisosAsignadosRepository;
    private final PermisosRepository permisosRepository;
    private final RepositoryService repositoryService;

    @Autowired
    public PermissionServiceImpl(PermisosAsignadosRepository permisosAsignadosRepository,
                                 PermisosRepository permisosRepository,
                                 RepositoryService repositoryService) {
        this.permisosAsignadosRepository = permisosAsignadosRepository;
        this.permisosRepository = permisosRepository;
        this.repositoryService = repositoryService;
    }


    @Transactional
    @Override
    public void acceptRequest(Solicitud request) {
        Optional<PermisosAsignados> permisosAsignadosOptional = permisosAsignadosRepository.findBySolicitud_idSolicitud(request.getIdSolicitud());

        PermisosAsignados permisosAsignados;


        if (permisosAsignadosOptional.isPresent()) {
            permisosAsignados = permisosAsignadosOptional.get();
            if (permisosAsignados.getConfirmacionTH() == true) throw new RequestAlreadyAcceptedException(permisosAsignados.getSolicitud().getIdSolicitud());
            permisosAsignados.setConfirmacionTH(true);

        } else {
            permisosAsignados = new PermisosAsignados();

            String comentario = request.getComentario();
            Integer idTipoPermiso = extraerIdTipoPermiso(comentario);

            if (idTipoPermiso == null)
                throw new IllegalArgumentException("No se pudo extraer el tipo de permiso del comentario.");

            TipoPermisos tipoPermiso = repositoryService.findByIdOrThrow(
                    permisosRepository,
                    idTipoPermiso,
                    () -> new PermissionTypeNotFoundException(idTipoPermiso)
            );

            permisosAsignados.setTipoPermiso(tipoPermiso);
            permisosAsignados.setSolicitud(request);
            permisosAsignados.setConfirmacionTH(true);

        }

        repositoryService.save(
                permisosAsignadosRepository,
                permisosAsignados,
                DATABASE_DEFAULT_ERROR
        );
    }

    private Integer extraerIdTipoPermiso(String comentario) {
        Pattern pattern = Pattern.compile("\\((\\d+)\\)");
        Matcher matcher = pattern.matcher(comentario);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }
}
