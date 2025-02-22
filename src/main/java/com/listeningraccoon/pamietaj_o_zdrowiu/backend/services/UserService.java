package com.listeningraccoon.pamietaj_o_zdrowiu.backend.services;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsersByFilter(String filter) {
        Query query = new Query();
        Pattern pattern = Pattern.compile(Pattern.quote(filter), Pattern.CASE_INSENSITIVE);

        Criteria criteria = new Criteria().orOperator(
                Criteria.where("firstName").regex(pattern),
                Criteria.where("lastName").regex(pattern),
                Criteria.where("email").regex(pattern));
        query.addCriteria(criteria);

        return mongoTemplate.find(query, User.class);
    }

    public long getCount() {
        return userRepository.count();
    }

//    public void getUserWithPrescription(Prescription prescription) {
//        Query query = new Query();
//        Criteria criteria = new Criteria().
//
//
//        Criteria criteria = new Criteria(Criteria.where("prescriptions").in(prescription.get_id()));
//
//        mongoTemplate.find()
//    }
}
