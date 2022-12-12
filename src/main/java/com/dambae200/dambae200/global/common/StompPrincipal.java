package com.dambae200.dambae200.global.common;

import java.security.Principal;

class StompPrincipal implements Principal {
    private String name;

    public StompPrincipal(String name) { this.name = name; }

    @Override
    public String getName() { return name;}
}