package com.exemplo.sping.userCRUD.controler;

import java.time.LocalDate;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemplo.sping.userCRUD.models.User;
import com.exemplo.sping.userCRUD.repository.UserRepository;

@RestController
@RequestMapping("api/v1//users")
public class UserController {

	private final UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping
	public Page<User> index(@PageableDefault Pageable pageable) {
		return userRepository.findAll(pageable);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<?> find(@PathVariable Long id) {
		final Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@GetMapping("cpf/{cpf}")
	public ResponseEntity<?> find(@PathVariable String cpf) {
		final Optional<User> user = userRepository.findByCpfWithSql(cpf);
		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}		
	}	

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		String password = user.getCpf().substring(0, 2) + Integer.toString(user.getBirthDate().getMonthValue());
		final String sha1 = DigestUtils.sha1Hex(password);
		user.setPassword(sha1);
		
//		final User savedUser = userRepository.save(user);
		userRepository.insertWithSql(user.getName(), user.getCpf(), user.getBirthDate(), user.getPassword());

		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@PutMapping("{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody User user, BindingResult result) {

		final Optional<User> userBase = userRepository.findById(id);
		if (userBase.isPresent()) {

			if (result.hasErrors()) {
				return new ResponseEntity<>(result.getAllErrors(), HttpStatus.UNPROCESSABLE_ENTITY);
			}
			user.setId(id);			
			user.setPassword(userBase.get().getPassword());
			
//			User updatedUser = userRepository.save(user);
			userRepository.updateWithSql(user.getName(), user.getCpf(), user.getBirthDate(), user.getPassword(), user.getId());

			return new ResponseEntity<>(user, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return userRepository.findById(id).map(user -> {
			//userRepository.delete(user);
			userRepository.deleteWithSql(id);;
			return new ResponseEntity<>(HttpStatus.OK);
		}).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}