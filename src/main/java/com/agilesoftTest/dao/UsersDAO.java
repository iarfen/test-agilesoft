package com.agilesoftTest.dao;

import com.agilesoftTest.model.User;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Configurable
public interface UsersDAO  extends CrudRepository<User, Long> {}