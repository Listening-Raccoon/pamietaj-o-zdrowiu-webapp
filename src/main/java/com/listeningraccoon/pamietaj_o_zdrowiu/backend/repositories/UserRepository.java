package com.listeningraccoon.pamietaj_o_zdrowiu.backend.repositories;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    public Optional<User> findByEmail(String email);
}
