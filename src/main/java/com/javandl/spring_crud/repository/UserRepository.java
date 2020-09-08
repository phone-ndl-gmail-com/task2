package com.javandl.spring_crud.repository;

import com.javandl.spring_crud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    // поиск user по login    User findByLogin(String login);

    @Query(value="SELECT u.id, u.name, u.password FROM USERS u WHERE u.LOGIN=:login", nativeQuery = true)
    List<Object[]> findUserByLogin(@Param("login") String login);

    // получить количество users - для автозаполнения базы (если getRowsUsers()=0 - заполнить)
    @Query(value="SELECT COUNT(*) FROM USERS", nativeQuery = true)
    Integer getRowsUsers();

    // получение объекта List<Object[]> с единственным элементом = code
    @Query(value="SELECT COUNT(*)*0+(:code) FROM USERS", nativeQuery = true)
    List<Object[]> getNotAccessCode(@Param("code") int code);

    // Добавить user-а (для автозаполнения)
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO USERS(id, name, login, password) VALUES(:id, :name, :login, :password)", nativeQuery = true)
    Integer addToUsers(@Param("id") int id,
                       @Param("name") String name,
                       @Param("login") String login,
                       @Param("password") String password);

    // добавить disk (для автозаполнения)
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO DISKS(id, name, user_id) VALUES(:id, :name, :user_id)", nativeQuery = true)
    Integer addToDisks(@Param("id") int id,
                       @Param("name") String name,
                       @Param("user_id") int user_id);

    // user взял disk (и для автозаполнения тоже)
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO USERS_TAKEN_DISKS(taken_disks_id, user_id) VALUES(:disk_id, :user_id)", nativeQuery = true)
    Integer addToUsersTakenDisks(@Param("disk_id") int disk_id,
                                 @Param("user_id") int user_id);

    // user вернул disk
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM USERS_TAKEN_DISKS WHERE taken_disks_id=:disk_id", nativeQuery = true)
    Integer deleteFromUsersTakenDisks(@Param("disk_id") int disk_id);

    // Список МОИ ДИСКИ (taken: true - взятые другими user-ами, false - все)
    @Query(value = "SELECT d.id AS id" +
            ", d.name AS name" +
            ", u.id AS user_id" +
            ", u.name AS user_name" +
            ", tu.id AS taken_user_id" +
            ", tu.name AS taken_user_name" +
            " FROM USERS u" +
            " LEFT JOIN DISKS d ON d.user_id=u.id" +
            " LEFT JOIN USERS_TAKEN_DISKS ti ON ti.taken_disks_id=d.id" +
            " LEFT JOIN USERS tu ON tu.id=ti.user_id" +
            " WHERE u.id=:id AND(:taken=false OR IFNULL(ti.taken_disks_id, 0)!=0)" +
            " ORDER BY d.name", nativeQuery = true)
    List<Object[]> findMyDisks(@Param("id") int id, @Param("taken") boolean taken);

    // Список ЧУЖИЕ ДИСКИ, ВЗЯТЫЕ МНОЙ
    @Query(value = "SELECT d.id AS id" +
            ", d.name AS name" +
            ", u.id AS user_id" +
            ", u.name AS user_name" +
            " FROM USERS_TAKEN_DISKS ti" +
            " LEFT JOIN DISKS d ON d.id=ti.taken_disks_id" +
            " LEFT JOIN USERS u ON u.id=d.user_id" +
            " WHERE ti.user_id=:id ORDER BY d.name", nativeQuery = true)
    List<Object[]> findNotMyTakenDisks(@Param("id") int id);

    // Список ДИСКИ, КОТОРЫЕ МОЖНО ВЗЯТЬ
    @Query(value = "SELECT d.id AS id" +
            ", d.name AS name" +
            ", u.id AS user_id" +
            ", u.name AS user_name" +
            " FROM DISKS d" +
            " LEFT JOIN USERS u ON u.id=d.user_id" +
            " LEFT JOIN USERS_TAKEN_DISKS ti ON d.id=ti.taken_disks_id" +
            " WHERE d.user_id!=:id AND IFNULL(ti.taken_disks_id, 0)=0" +
            " ORDER BY u.name, d.name", nativeQuery = true)
    List<Object[]> findFreeDisks(@Param("id") int id);

}