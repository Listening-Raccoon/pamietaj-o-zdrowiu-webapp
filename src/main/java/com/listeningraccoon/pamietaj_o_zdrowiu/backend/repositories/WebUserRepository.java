package com.listeningraccoon.pamietaj_o_zdrowiu.backend.repositories;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.WebUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WebUserRepository extends MongoRepository<WebUser, ObjectId> {
    public WebUser findByEmail(String email);
}
