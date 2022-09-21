package diginamic.gdm.repository;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dao.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Mission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface MissionRepository extends JpaRepository<Mission, Integer> {

    List<Mission> findByNatureIs(Nature nature);

    List<Mission> findByCollaboratorOrderByStartDateDesc(Collaborator collaborator);
    List<Mission> findByCollaboratorAndEndDateAfterOrderByStartDate(Collaborator collaborator, LocalDateTime startDate);

    List<Mission> findByCollaboratorAndStatus(Collaborator collaborator, Status status);

    List<Mission> findByStatusAndEndDateBeforeAndHasBonusBeenEvaluatedFalse(Status status, LocalDateTime currentDate);

    List<Mission> findByStatusAndEndDateBefore(Status status, LocalDateTime currentDate);

    List<Mission> findByStatus(Status status);


}
