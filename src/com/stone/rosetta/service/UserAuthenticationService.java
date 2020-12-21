package com.stone.rosetta.service;

import com.stone.rosetta.repository.UserRepository;
import com.stone.rosetta.repository.model.User;
import com.stone.rosetta.throwable.IncorrectPasswordException;
import com.stone.rosetta.throwable.UserNotFoundException;

import java.sql.SQLException;

public class UserAuthenticationService {
    
    private static UserAuthenticationService authenticationService;
    private User authenticatedUser;

    private UserRepository userRepository;

    private UserAuthenticationService() throws ClassNotFoundException {
        this.userRepository = new UserRepository();
    }
    
    public static UserAuthenticationService getInstance() throws ClassNotFoundException{
        if(authenticationService == null)
            authenticationService = new UserAuthenticationService();
        return authenticationService;
    }

    public User findByUserName(String userName, String password) throws SQLException, UserNotFoundException, IncorrectPasswordException {
        User user = userRepository.getByUserName(userName);
        if(user == null){
            throw new UserNotFoundException("username \"%s\" not found", userName);
        }else if(isValidPassword(user.getPassword(), password)){
            this.authenticatedUser = user;
            return user;
        }else{
            throw new IncorrectPasswordException();
        }
    }

    private boolean isValidPassword(String originalPassword, String requestedPassword) {
        return originalPassword.equals(requestedPassword);
    }
    
    public User getAuthenticatedUser() {
        return authenticatedUser;
    }


}
