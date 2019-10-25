package br.edu.utfpr.tsi.utfparking.web.content;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PageUser {

    private Long id;

    private String name;

    private String type;

    private String username;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private String plate;

    private String model;

    private boolean isAuthorisedAccess;
}
