package com.javandl.spring_crud.service;
import com.javandl.spring_crud.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    List<Object[]> findMyDisks();
    List<Object[]> findMyTakenDisks();
    List<Object[]> findNotMyTakenDisks();
    List<Object[]> findFreeDisks();
    String validateLoginAndPassword(UserDto userDto);
    String logoutUser();
    void takeDisk(Integer diskId);
    void returnDisk(Integer diskId);
}
