package com.ergh99.web.connections.users.service;

import com.ergh99.web.connections.users.model.Play;
import com.ergh99.web.connections.users.model.User;
import org.springframework.stereotype.Repository;

/**
 * Created by syn227 on 3/1/16.
 * Primary interface for user data database
 */
@Repository
public interface UserDataRepository {

    User getUser(String userId);

    Play getPlay(String playId);
}
