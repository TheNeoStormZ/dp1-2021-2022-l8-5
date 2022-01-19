package org.springframework.dwarf.worker;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dwarf.board.Board;
import org.springframework.dwarf.board.BoardCell;
import org.springframework.dwarf.board.BoardCellService;
import org.springframework.dwarf.game.Game;
import org.springframework.dwarf.game.GameService;
import org.springframework.dwarf.player.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author David Zamora
 * @author José Ignacio García
 *
 */
@Service

public class WorkerService {

	
	private WorkerRepository workerRepo;
	
	@Autowired
	private BoardCellService boardCellService;
	@Autowired
	private GameService gameService;
	
	@Autowired
	public WorkerService(WorkerRepository WorkerRepository) {
		this.workerRepo = WorkerRepository;
	}		
	
	@Transactional
	public int WorkerCount() {
		return (int) workerRepo.count();
	}

	public Iterable<Worker> findAll() {
		return workerRepo.findAll();
	}
	@Transactional(readOnly = true)
	public Optional<Worker> findByWorkerId(int id){
		return workerRepo.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Collection<Worker> findByPlayerId(int id){
		return workerRepo.findByPlayerId(id);
	}

	@Transactional(readOnly = true)
	public Collection<Worker> findByPlayerIdAndGameId(int pid, int gid){
		return workerRepo.findByPlayerIdAndGameId(pid,gid);
	}
	
	public List<Worker> findNotPlacedByPlayerIdAndGameId(int pid, int gid){
		return workerRepo.findNotPlacedByPlayerIdAndGameId(pid, gid);
	}
	
	public List<Worker> findNotPlacedByGameId(int gid){
		return workerRepo.findNotPlacedByGameId(gid);
	}
	
	// need test
	public List<Worker> findNotPlacedAidByGameId(int gid) {
		return workerRepo.findNotPlacedAidByGameId(gid);
	}
	
	// needs test
	public List<Worker> findPlacedByGameId(int gid){
		return workerRepo.findPlacedByGameId(gid);
	}
	
	public void delete(Worker worker) {
		workerRepo.delete(worker);
	}
	
	public List<Worker> findPlayerAidWorkers(int pid) {
		return workerRepo.findAidByPlayerId(pid);
	}
	
	public void deletePlayerWorker(Player player) {
		Collection<Worker> workers = findByPlayerId(player.getId());
		workers.stream().forEach(worker -> delete(worker));
	}
	
	public void deletePlayerAidWorkers(Player player) {
		List<Worker> workers = findPlayerAidWorkers(player.getId());
		workers.stream().forEach(worker -> delete(worker));
	}
	
	@Transactional(rollbackFor = IllegalPositionException.class)
	public void saveWorker(Worker worker) throws DataAccessException, IllegalPositionException {
		if(getWorkerInvalid(worker)) {
			throw new IllegalPositionException();
		}
		
		workerRepo.save(worker);		

	}
	
	public Boolean getWorkerInvalid(Worker worker){
		Boolean res=false;
		if (worker.xposition==null || worker.yposition==null) {
			return false;
		} else {
		res = !(worker.xposition>=0 && worker.xposition<=3);
		res = res || !(worker.yposition>=0 && worker.yposition<=2);
		return res;	
		}
	}
	
	
	@Transactional
	public void createPlayerWorkers(Player player, Game game, Integer imageNumber) throws IllegalPositionException {
		Worker playerWorker1 = new Worker(player, game, imageNumber);
		Worker playerWorker2 = new Worker(player, game, imageNumber);
		this.saveWorker(playerWorker1);
		this.saveWorker(playerWorker2);
		
	}
	
}
