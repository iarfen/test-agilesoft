package com.agilesoftTest.dao;

import org.springframework.stereotype.Repository;

import com.agilesoftTest.model.Task;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.repository.CrudRepository;

@Repository
@Configurable
public interface TasksDAO extends CrudRepository<Task, Long>{}

