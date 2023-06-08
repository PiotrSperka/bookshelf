package sperka.pl.bookcase.server.service;

import sperka.pl.bookcase.server.dto.CreateUserRequestDto;
import sperka.pl.bookcase.server.dto.InitializeUserRequestDto;
import sperka.pl.bookcase.server.dto.ModifyUserRequestDto;
import sperka.pl.bookcase.server.dto.UserInfoDto;

import java.util.List;

public interface UserService {
    boolean initializeUser( InitializeUserRequestDto dto );

    List< UserInfoDto > getAll();

    UserInfoDto get( Long id );

    UserInfoDto getByUsername( String username );

    long countUsers();

    boolean createUser( CreateUserRequestDto dto );

    boolean modifyPassword( String username, String oldPassword, String newPassword );

    boolean modifyUser( ModifyUserRequestDto dto );

    boolean deleteUser( Long id, String currentUsername );

    boolean resetPassword( String token, String password, String passwordRepeat );

    boolean sendResetPasswordToken( String email );
}
