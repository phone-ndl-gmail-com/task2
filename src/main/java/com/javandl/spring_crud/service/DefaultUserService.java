package com.javandl.spring_crud.service;

import com.javandl.spring_crud.dto.UserDto;
import com.javandl.spring_crud.repository.UserRepository;
import lombok.extern.java.Log;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@AllArgsConstructor
@Log
public class DefaultUserService implements UserService {

    @Autowired
    final private HttpSession httpSession;  // сессия

    private final UserRepository userRepository;

    // автозаполнение базы
    private void autoFillDb() {

        if(userRepository.getRowsUsers() == 0) {
            userRepository.addToUsers(1, "Дмитрий", "ndl", "123");
            userRepository.addToUsers(2, "Иван", "ivan", "123");
            userRepository.addToUsers(3, "Света", "sveta", "123");
            userRepository.addToUsers(4, "Юля", "julia", "123");
            userRepository.addToDisks(1, "Властелин колец",1);
            userRepository.addToDisks(2, "Хоббит",1);
            userRepository.addToDisks(3, "Матрица",1);
            userRepository.addToDisks(4, "Звездные Войны",1);
            userRepository.addToDisks(5, "Агент 007. Казино Рояль",2);
            userRepository.addToDisks(6, "Агент 007. Квант Милосердия",2);
            userRepository.addToDisks(7, "Агент 007. Координаты Скайфолл",2);
            userRepository.addToDisks(8, "Агент 007. Спектр",2);
            userRepository.addToDisks(9, "Карнавальная ночь",3);
            userRepository.addToDisks(10, "Ирония судьбы или с легким паром",3);
            userRepository.addToDisks(11, "Джентльмены удачи",3);
            userRepository.addToDisks(12, "Иван Васильевич меняет профессию",3);
            userRepository.addToDisks(13, "Простые сложности",4);
            userRepository.addToDisks(14, "Хороший год",4);
            userRepository.addToDisks(15, "Любовь и голуби",4);
            userRepository.addToDisks(16, "Самая обаятельная и превлекательная",4);
            userRepository.addToUsersTakenDisks(1,2);
            userRepository.addToUsersTakenDisks(11,4);
            userRepository.addToUsersTakenDisks(12,4);
            userRepository.addToUsersTakenDisks(7,1);
            userRepository.addToUsersTakenDisks(16,1);
        }
    }

    // Сессионный секъюрити атрибут CURRENT_USER_ID: если =null то =0
    private int getCurrentUserId() {

        if (httpSession.getAttribute("CURRENT_USER_ID") == null)
            httpSession.setAttribute("CURRENT_USER_ID", 0);
        return (int)httpSession.getAttribute("CURRENT_USER_ID");
    }

    // Список всех моих дисков
    @Override
    public List<Object[]> findMyDisks() {

        autoFillDb();   // при первом обращении - автозаполнение базы
        // если есть авотризация - выдаем список, иначе -1
        return getCurrentUserId() != 0 ?
                userRepository.findMyDisks(getCurrentUserId(),false) :
                userRepository.getNotAccessCode(-1);
    }

    // Список взятых у меня дисков
    @Override
    public List<Object[]> findMyTakenDisks() {

        // если есть авотризация - выдаем список, иначе -1
        return getCurrentUserId() != 0 ?
                userRepository.findMyDisks(getCurrentUserId(),true) :
                userRepository.getNotAccessCode(-1);
    }

    // Список мною взятых дисков
    @Override
    public List<Object[]> findNotMyTakenDisks() {

        // если есть авотризация - выдаем список, иначе -1
        return getCurrentUserId() != 0 ?
                userRepository.findNotMyTakenDisks(getCurrentUserId()) :
                userRepository.getNotAccessCode(-1);
    }

    // Список свободных дисков
    @Override
    public List<Object[]> findFreeDisks() {

        // если есть авотризация - выдаем список, иначе -1
        return getCurrentUserId() != 0 ?
                userRepository.findFreeDisks(getCurrentUserId()) :
                userRepository.getNotAccessCode(-1);
    }

    // проверка логина и пароля при входе
    @Override
    public String validateLoginAndPassword(UserDto userDto) {

        List<Object[]> result = userRepository.findUserByLogin(userDto.getLogin());
        if (result.isEmpty()) {
            httpSession.setAttribute("CURRENT_USER_ID", 0);
            return "{\"status\":\"notAccess\"}";
        }
        int getId = (int)Long.parseLong(result.get(0)[0].toString());
        String getName = result.get(0)[1].toString();
        String getPassword = result.get(0)[2].toString();
        if(!userDto.getPassword().equals(getPassword)) {
            httpSession.setAttribute("CURRENT_USER_ID", 0);
            return "{\"status\":\"notAccess\"}";
        }
        httpSession.setAttribute("CURRENT_USER_ID", getId);
        return "{\"status\":\"ok\",\"name\":\"" + getName + "\"}";
    }

    // выход из системы
    @Override
    public String logoutUser() {

        httpSession.setAttribute("CURRENT_USER_ID", 0);
        return "{\"status\":\"ok\"}";
    }

    // взять диск
    @Override
    public void takeDisk(Integer diskId) {

        userRepository.addToUsersTakenDisks(diskId, getCurrentUserId());
    }

    // вернуть диск
    @Override
    public void returnDisk(Integer diskId) {

        userRepository.deleteFromUsersTakenDisks(diskId);
    }

}
