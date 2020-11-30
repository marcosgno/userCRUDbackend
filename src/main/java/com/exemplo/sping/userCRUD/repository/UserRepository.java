package com.exemplo.sping.userCRUD.repository;

import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.exemplo.sping.userCRUD.models.User;


public interface UserRepository extends JpaRepository<User, Long>{ 
	
	@Query(value="INSERT INTO users (name, cpf, birth_date, password) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
	@Modifying
	@Transactional
	void insertWithSql(String name, String cpf, LocalDate birthDate, String password);
	
	
	@Query(value="UPDATE users SET name = ?1, cpf = ?2, birth_date = ?3, password = ?4 WHERE id = ?5", nativeQuery = true)
	@Modifying
	@Transactional	
	void updateWithSql(String name, String cpf, LocalDate birthDate, String password, Long id);	
	
	@Query(value="DELETE FROM  users WHERE id = ?1", nativeQuery = true)
	@Modifying
	@Transactional	
	void deleteWithSql(Long id);	
	
	@Query(value="SELECT * FROM  users WHERE cpf = ?1", nativeQuery = true)
	Optional<User> findByCpfWithSql(String name);
}
