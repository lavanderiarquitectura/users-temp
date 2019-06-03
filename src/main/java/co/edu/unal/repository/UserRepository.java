package co.edu.unal.repository;

import co.edu.unal.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public List<User> findByPersonalId(Integer personalId);
}
