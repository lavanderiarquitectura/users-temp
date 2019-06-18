package co.edu.unal.controller;

import co.edu.unal.exception.ResourceNotFoundException;
import co.edu.unal.model.Token;
import co.edu.unal.model.User;
import co.edu.unal.repository.UserRepository;

import org.apache.tomcat.jni.Time;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserRepository userRepository;

	private ArrayList<Token> tokens = new ArrayList<>();
	
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
	
	// Get all tokens
	@GetMapping("/tokens")
	public List<Token> getAllTokens() {
	    return tokens;
	}
		
	// Authenticate token
	@GetMapping("/token/{token}")
	public ResponseEntity<Object> getUserById(@PathVariable(value = "token") String token) {
		JSONObject response = new JSONObject();
		for(Token t : tokens) {
			if(t.getToken().equals(token)) {
				System.out.println("Token found!");
				if(t.getExpiration() >= System.currentTimeMillis()) {
					response.put("user", t.getUserId());
					return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
				}
				//Token expired
				else {
					tokens.remove(t);
					response.put("user", -1);
					return new ResponseEntity<Object>(response.toString(), HttpStatus.FORBIDDEN);
				}
			}
		}
		//Token not found
		response.put("user", -1);
		return new ResponseEntity<Object>(response.toString(), HttpStatus.FORBIDDEN);
	}
		
	
	private String generateTokenString() {
		String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i <20; i++) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
	
	//Check if username/password are valid for login
	@GetMapping("/users/{personal_id}/{password}")
	public ResponseEntity<Object> loginUser(@PathVariable(value = "personal_id") Integer personalID, @PathVariable(value = "password") String password) {
	    List<User> users = userRepository.findByPersonalId(personalID);
	    JSONObject response = new JSONObject();
	    if(users.size() < 1 || !users.get(0).getPassword().equals(password)) {
	    	response.put("login", "failure");
	    	return new ResponseEntity<Object>(response.toString(), HttpStatus.FORBIDDEN);
	    }
	    else {
	    	long dayInMillis = 1000 * 60 * 60 * 24;
	    	Token token = new Token(users.get(0).getPersonal_id(), generateTokenString(), System.currentTimeMillis() + dayInMillis);
	    	tokens.add(token);
	    	response.put("login", token.getToken());
	    	return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
	    }
	}

	
	//Check if username/password are valid for login
	@GetMapping("/operator/{user}/{password}")
	public ResponseEntity<Object> loginUser(@PathVariable(value = "user") String user, @PathVariable(value = "password") String password) {
	    JSONObject response = new JSONObject();
	    if(user.equals("admin") && password.equals("admin123")) {
	    	long dayInMillis = 1000 * 60 * 60 * 24;
	    	Token token = new Token(0, generateTokenString(), System.currentTimeMillis() + dayInMillis);
	    	tokens.add(token);
	    	response.put("login", token.getToken());
	    	return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
	    }
	    else {
	    	response.put("login", "failure");
	    	return new ResponseEntity<Object>(response.toString(), HttpStatus.FORBIDDEN);
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
