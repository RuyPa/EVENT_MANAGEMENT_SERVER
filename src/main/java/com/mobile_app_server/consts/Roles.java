package com.mobile_app_server.consts;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public enum Roles {
    ADMIN(1, "ROLE_ADMIN"),
    USER(2, "ROLE_USER"),
    MANAGER(3, "ROLE_MANAGER");

    private int value;
    private String name;

    private static final Map<Integer, Roles> map = new HashMap<>();

    static {
        for (Roles r : Roles.values()) {
            map.put(r.value, r);
        }
    }

    Roles(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static Roles fromValue(int value) {
        return map.getOrDefault(value, null);
    }
}
