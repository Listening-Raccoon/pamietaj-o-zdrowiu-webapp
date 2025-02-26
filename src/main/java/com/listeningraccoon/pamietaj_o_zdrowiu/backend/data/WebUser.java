package com.listeningraccoon.pamietaj_o_zdrowiu.backend.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.atmosphere.config.service.Get;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "web-users")
@AllArgsConstructor
@NoArgsConstructor
public class WebUser {
    @Id
    @Getter
    private ObjectId id;

    @Getter
    private String email;
    @Getter
    private String passwordSalt;
    @Getter
    private String passwordHash;

    public WebUser(String email, String password) {
        this.email = email;
        this.passwordSalt = RandomStringUtils.secure().next(32);
        this.passwordHash = DigestUtils.sha1Hex(password + passwordSalt);
    }

    public boolean comparePassword(String password) {
        return DigestUtils.sha1Hex(password + passwordSalt).equals(passwordHash);
    }
}
