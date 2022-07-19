package com.dambae200.dambae200.domain.model;

import javax.persistence.Embeddable;

@Embeddable
public class Email {
    private String name;

    private String host;

    public String toString(){
        return "${name}@${host}";
    }

}
