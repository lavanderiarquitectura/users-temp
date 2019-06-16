package co.edu.unal.controller;

import co.edu.unal.exception.ResourceNotFoundException;
import co.edu.unal.model.User;
import co.edu.unal.repository.UserRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
	}
	
	//Check if username/password are valid for login
	@GetMapping("/users/{personal_id}/{password}")
	public ResponseEntity<Object> loginUser(@PathVariable(value = "personal_id") Integer personalID, @PathVariable(value = "password") String password) {
	    List<User> users = userRepository.findByPersonalId(personalID);
	    JSONObject response = new JSONObject();
	    if(users.size() < 1 || !users.get(0).getPassword().equals(password)) {
	    	response.put("login", "failure");
	    	return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
	    }
	    else {
	    	response.put("login", "success");
	    	return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
	    }
	}
	
	// Create a new User
	@PostMapping("/users")
	public User createUser(@Valid @RequestBody User user) {
	    List<User> users = userRepository.findByPersonalId(user.getPersonal_id());
	    if(users.isEmpty())
	    	return userRepository.save(user);
	    else {
	    	User empty = new User();
	    	
	    	return empty;
	    }
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
