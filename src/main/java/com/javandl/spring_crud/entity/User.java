package com.javandl.spring_crud.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data//ломбок аннотация: генерирует геттеры, сеттеры, иквалс, хеш код методы
@NoArgsConstructor//ломбок аннотация: конструктор без аргуметов
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String login;

    @Column
    private String password;

    // список МОИ ДИСКИ
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disk> disks;

    // список ВЗЯТЫЕ МНОЙ ДИСКИ
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Disk> takenDisks;


}