package com.agilesoftTest.model;

import jakarta.persistence.*;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public User user;

    @Column
    public String name;
    
    @Column
    public String description;
    
    @Column
    public Date createdAt;

    @Column
    public Date updatedAt;
    
    @Column
    public Boolean current;
}
