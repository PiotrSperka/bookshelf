package sperka.pl.bookcase.server.service;

import sperka.pl.bookcase.server.dto.UserInfoDto;

import java.util.List;

public interface UserService {
    boolean initializeUsers();

    List< UserInfoDto > getAll();

    UserInfoDto get( Long id );

    UserInfoDto getByUsername( String username );

    long countUsers();

    boolean createUser( String username, List< String > roles, String email, String locale );

    boolean modifyPassword( String username, String oldPassword, String newPassword );

    boolean modifyUser( Long id, String username, String password, List< String > roles, Boolean active, String email, String locale );

    boolean deleteUser( Long id, String currentUsername );

    boolean resetPassword( String token, String password, String passwordRepeat );

    boolean sendResetPasswordToken( String email );
}
