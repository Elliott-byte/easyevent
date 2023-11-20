package com.example.easyevent.type;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthData {
    private String userEmail;
    private String token;
    private Integer tokenExpiration;
}
