package com.agilesoftTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    public String username;

    @Column
    public String password;

    @Column
    public String name;

    @OneToMany(mappedBy = "user")
    private Set<Task> tasks;
}
