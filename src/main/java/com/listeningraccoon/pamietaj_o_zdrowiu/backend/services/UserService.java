package com.listeningraccoon.pamietaj_o_zdrowiu.backend.services;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.WebUser;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrescriptionService prescriptionService;
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

    public void deletePrescriptionOfUserById(String email, ObjectId prescriptionId) {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptionsOfUser(email);

        for (Prescription prescription : prescriptions) {
            if (prescription.getId().equals(prescriptionId)) {
                prescriptions.remove(prescription);
                break;
            }
        }
        mongoTemplate.update(User.class)
                .matching(Criteria.where("email").is(email))
                .apply(new Update().set("prescriptions", prescriptions))
                .first();
    }

    public void assignWebUser(ObjectId webUserId, ObjectId userId) {
        mongoTemplate.update(User.class)
                .matching(Criteria.where("id").is(userId))
                .apply(new Update().push("assignedUsers").value(webUserId))
                .first();
    }

    public long getCount() {
        return userRepository.count();
    }


}
