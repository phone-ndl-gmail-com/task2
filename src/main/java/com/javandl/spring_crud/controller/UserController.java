package com.javandl.spring_crud.controller;

import com.javandl.spring_crud.dto.UserDto;
import com.javandl.spring_crud.exception.ValidationException;
import com.javandl.spring_crud.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Log
public class UserController {

    private final UserService userService;

    @GetMapping("/findMyDisks")
    public List<Object[]> findMyDisks() {
        log.info("Handling find all my disks request.");
        return userService.findMyDisks();
    }

    @GetMapping("/findMyTakenDisks")
    public List<Object[]> findMyTakenDisks() {
        log.info("Handling find my taken disks request.");
        return userService.findMyTakenDisks();
    }

    @GetMapping("/findNotMyTakenDisks")
    public List<Object[]> findNotMyTakenDisks() {
        log.info("Handling find not my taken disks request.");
        return userService.findNotMyTakenDisks();
    }

    @GetMapping("/findFreeDisks")
    public List<Object[]> findFreeDisks() {
        log.info("Handling find free disks request.");
        return userService.findFreeDisks();
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserDto userDto) throws ValidationException {
        log.info("Handling login users: " + userDto.getLogin());
        return userService.validateLoginAndPassword(userDto);
    }

    @GetMapping("/logout")
    public String logoutUser(){
        log.info("Handling logout users.");
        return userService.logoutUser();
    }

    @DeleteMapping("/takeDisk/{id}")
    public ResponseEntity<Void> takeDisk(@PathVariable Integer id) {
        log.info("Handling take disk (add to usersTakenDisks) request: " + id);
        userService.takeDisk(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/returnDisk/{id}")
    public ResponseEntity<Void> returnDisk(@PathVariable Integer id) {
        log.info("Handling return disk (delete from usersTakenDisks) request: " + id);
        userService.returnDisk(id);
        return ResponseEntity.ok().build();
    }
}
