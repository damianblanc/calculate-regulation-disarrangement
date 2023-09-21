package com.bymatech.calculateregulationdisarrangement.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    USER_READ("management:read"),
    USER_UPDATE("management:update"),
    USER_CREATE("management:create"),
    USER_DELETE("management:delete"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    ;

    @Getter
    private final String permission;
}