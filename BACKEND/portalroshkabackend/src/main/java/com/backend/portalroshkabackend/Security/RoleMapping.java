package com.backend.portalroshkabackend.Security;

import org.springframework.stereotype.Component;

/**
 * Documentación para mapeo de roles en el sistema de autorización.
 * Este archivo documenta qué roles tienen acceso a qué endpoints.
 */
@Component
public class RoleMapping {

    /**
     * Documentación del mapeo de roles y sus permisos:
     * 
     * MAPEO ACTUAL DE ROLES:
     * - Rol ID 1: TALENTO HUMANO -> ROLE_1 (acceso a /api/v1/admin/th/**)
     * - Rol ID 2: OPERACIONES -> ROLE_2 (acceso a /api/v1/admin/operations/**)
     * - Rol ID 3: ADMINISTRADOR DE SISTEMAS -> ROLE_3 (acceso a /api/v1/admin/sysadmin/**)
     * - Rol ID 4: DESARROLLADOR -> ROLE_4 (permisos de desarrollador)
     * - Rol ID 5: DIRECTIVO -> ROLE_5 (permisos directivos - acceso completo)
     * - Rol ID 6: ADMINISTRATIVO -> ROLE_6 (permisos básicos administrativos)
     * 
     * ENDPOINTS Y PERMISOS:
     * - /api/v1/admin/th/** -> Accesible por ROLE_1 (Talento Humano) y ROLE_5 (Directivo)
     * - /api/v1/admin/operations/** -> Accesible por ROLE_2 (Operaciones) y ROLE_5 (Directivo)
     * - /api/v1/admin/sysadmin/** -> Accesible por ROLE_3 (Administrador de Sistemas) y ROLE_5 (Directivo)
     * - /api/v1/admin/** -> Accesible solo por ROLE_5 (Directivo) para otros endpoints no específicos
     * 
     * @param rolId ID del rol del usuario
     * @return String representando la descripción del rol
     */
    public String getRoleDescription(Integer rolId) {
        if (rolId == null) {
            return "Usuario sin rol asignado";
        }
        
        switch (rolId) {
            case 1:
                return "Talento Humano - Gestión de recursos humanos";
            case 2:
                return "Operaciones - Gestión de operaciones";
            case 3:
                return "Administrador de Sistemas - Administración del sistema";
            case 4:
                return "Desarrollador - Desarrollo de software";
            case 5:
                return "Directivo - Permisos ejecutivos completos";
            case 6:
                return "Administrativo - Permisos básicos administrativos";
            default:
                return "Rol no reconocido";
        }
    }

    /**
     * Obtiene el nombre del rol basado en su ID
     * @param rolId ID del rol
     * @return Nombre del rol
     */
    public String getRoleName(Integer rolId) {
        if (rolId == null) {
            return "SIN_ROL";
        }
        
        switch (rolId) {
            case 1:
                return "TALENTO_HUMANO";
            case 2:
                return "OPERACIONES";
            case 3:
                return "ADMINISTRADOR_SISTEMAS";
            case 4:
                return "DESARROLLADOR";
            case 5:
                return "DIRECTIVO";
            case 6:
                return "ADMINISTRATIVO";
            default:
                return "ROL_DESCONOCIDO";
        }
    }
}