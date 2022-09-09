package diginamic.gdm.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Status;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.services.MissionService;
import lombok.AllArgsConstructor;

/**
 * Implementation for {@link MissionService}.
 * 
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
public class MissionServiceImpl implements MissionService {
	
	/**
	 * The {@link MissionRepository} dependency.
	 */
	private MissionRepository missionRepository;

	@Override
	public List<Mission> list() {
		return missionRepository.findAll();
	}

	@Override
	public void create(Mission mission) {
		this.missionRepository.save(mission);
	}

	@Override
	public Mission read(int id) {
		return this.missionRepository.findById(id).orElseThrow();
	}

	@Override
	public Mission update(Mission mission) {
		Mission current = read(mission.getId());
		current.setBonus(mission.getBonus());
		current.setMissionTransport(mission.getMissionTransport());
		current.setStartDate(mission.getStartDate());
		current.setEndDate(mission.getEndDate());
		current.setStartCity(mission.getStartCity());
		current.setEndCity(mission.getEndCity());
		current.setNature(mission.getNature());
		this.missionRepository.save(current);
		return current;
	}

	@Override
	public void delete(int id) {
		Mission mission = read(id);
		this.missionRepository.delete(mission);
	}

	@Override
	public void updateStatus(int id, Status status) {
		Mission mission = read(id);
		mission.setStatus(status);
	}

}
