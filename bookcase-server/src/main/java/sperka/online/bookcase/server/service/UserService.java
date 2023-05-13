package sperka.online.bookcase.server.service;

import sperka.online.bookcase.server.dto.UserInfoDto;

import java.util.List;

public interface UserService {
    boolean initializeUsers();

    List< UserInfoDto > getAll();

    UserInfoDto get(Long id);

    UserInfoDto getByUsername(String username);

    long countUsers();

    boolean createUser( String username, String password, List< String > roles );

    boolean modifyPassword( String username, String oldPassword, String newPassword );

    boolean modifyUser( Long id, String username, String password, List< String > roles );

    boolean deleteUser( Long id, String currentUsername );
}
