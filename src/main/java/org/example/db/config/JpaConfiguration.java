package org.example.db.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JpaConfiguration {

    private String driver;
    private String url;
    private String userName;
    private String password;
    private String persistentUnit;

}
