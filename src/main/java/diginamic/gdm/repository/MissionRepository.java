package diginamic.gdm.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dao.Status;
/**
 * 
 * 
 * @author Joseph
 *
 */
public interface MissionRepository extends JpaRepository<Mission, Integer> {

    List<Mission> findByNatureIs(Nature nature);

    List<Mission> findByNatureOrderByStartDateDesc(Nature nature);
    List<Mission> findByNatureAndStartDateBefore(Nature nature, LocalDateTime date);
    List<Mission> findByNatureAndStartDateAfter(Nature nature, LocalDateTime date);

    List<Mission> findByCollaboratorOrderByStartDateDesc(Collaborator collaborator);
    List<Mission> findByCollaboratorAndEndDateAfterAndStatusNotOrderByStartDate(Collaborator collaborator, LocalDateTime startDate,Status status);
    List<Mission> findByCollaboratorAndEndDateAfterAndStatusOrderByStartDate(Collaborator collaborator, LocalDateTime startDate,Status status);

    List<Mission> findByCollaboratorAndStatusNot(Collaborator collaborator, Status status);
    List<Mission> findByCollaboratorAndStatus(Collaborator collaborator, Status status);

    List<Mission> findByStatusAndEndDateBeforeAndHasBonusBeenEvaluatedFalse(Status status, LocalDateTime currentDate);

    List<Mission> findByStatusAndEndDateBefore(Status status, LocalDateTime currentDate);

    List<Mission> findByStatus(Status status);

    List<Mission> findByCollaborator(Collaborator collaborator);


}
