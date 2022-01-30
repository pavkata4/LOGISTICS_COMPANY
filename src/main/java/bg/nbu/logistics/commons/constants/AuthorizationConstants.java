package bg.nbu.logistics.commons.constants;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class AuthorizationConstants {
    public static final String IS_ANONYMOUS = "isAnonymous()";
    public static final String IS_AUTHENTICATED = "isAuthenticated()";
    
    public static final String UNABLE_TO_FIND_USER_BY_NAME_MESSAGE = "Unable to find user by name.";
    public static final String UNABLE_TO_FIND_OFFICE_BY_ID_MESSAGE = "Unable to find office by id.";
}
