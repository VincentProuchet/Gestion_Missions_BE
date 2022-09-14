package diginamic.gdm.repository;

import diginamic.gdm.dao.Nature;
import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Mission;

import java.util.List;
import java.util.Set;

public interface MissionRepository extends JpaRepository<Mission, Integer> {

    List<Mission> findByNatureIs(Nature nature);
}
