package sperka.online.bookcase.server.service;

import java.util.List;

public interface AuthService {
    boolean createUser( String username, String password, String roles );

    String loginUser( String username, String password );

    boolean modifyPassword( String username, String oldPassword, String newPassword );

    boolean modifyUser( String username, String password, List< String > roles );

    boolean deleteUser( String username, String currentUsername );
}
