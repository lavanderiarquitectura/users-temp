package co.edu.unal.controller;

import co.edu.unal.exception.ResourceNotFoundException;
import co.edu.unal.model.Room;
import co.edu.unal.repository.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RoomController {

	@Autowired
	RoomRepository roomRepository;

	// Get All Rooms
	@GetMapping("/rooms")
	public List<Room> getAllRooms() {
	    return roomRepository.findAll();
	}
	
	// Get a Single Room
	@GetMapping("/rooms/{id}")
	public Room getRoomById(@PathVariable(value = "id") Long roomId) {
	    return roomRepository.findById(roomId)
	            .orElseThrow(() -> new ResourceNotFoundException("Note", "id", roomId));
	}
	
	// Create a new Room
	@PostMapping("/rooms")
	public Room createRoom(@Valid @RequestBody Room room) {
	    return roomRepository.save(room);
	}

	// Update a Room
	@PutMapping("/rooms/{id}")
	public Room updateRoom(@PathVariable(value = "id") Long roomId,
	                                        @Valid @RequestBody Room roomDetails) {

	    Room room = roomRepository.findById(roomId)
	            .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));

	    Room updatedRoom = roomRepository.save(room);
	    return updatedRoom;
	}

	// Delete a Room
	@DeleteMapping("/rooms/{id}")
	public ResponseEntity<?> deleteRoom(@PathVariable(value = "id") Long roomId) {
	    Room room = roomRepository.findById(roomId)
	            .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));

	    roomRepository.delete(room);

	    return ResponseEntity.ok().build();
	}


	
}
