package com.backend.portalroshkabackend.Repositories.OP;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.TecnologiasEquipos;

@Repository
public interface TecnologiasEquiposRepository extends JpaRepository<TecnologiasEquipos, Integer> {
    List<TecnologiasEquipos> findAllByEquipo_IdEquipo(Integer idEquipo);
    // Если нужно получить все технологии для конкретной команды
    // List<TecnologiasEquipos> findAllByEquipo_Id(Integer idEquipo);

    // // Если нужно удалить связи по команде
    // void deleteAllByEquipo_Id(Integer idEquipo);
}