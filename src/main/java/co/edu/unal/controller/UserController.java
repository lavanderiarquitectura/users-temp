package co.edu.unal.controller;

import co.edu.unal.exception.ResourceNotFoundException;
import co.edu.unal.model.User;
import co.edu.unal.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserRepository userRepository;

	// Get All Users
	@GetMapping("/users")
	public List<User> getAllUsers() {
	    return userRepository.findAll();
	}
	
	// Get a Single User
	@GetMapping("/users/{id}")
	public User getUserById(@PathVariable(value = "id") Long userId) {
	    return userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("Note", "id", userId));
	}
	
	// Create a new User
	@PostMapping("/users")
	public User createUser(@Valid @RequestBody User user) {
	    return userRepository.save(user);
	}

	// Update a User
	@PutMapping("/users/{id}")
	public User updateUser(@PathVariable(value = "id") Long userId,
	                                        @Valid @RequestBody User userDetails) {

	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

	    User updatedUser = userRepository.save(user);
	    return updatedUser;
	}

	// Delete a User
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

	    userRepository.delete(user);

	    return ResponseEntity.ok().build();
	}
	
}
