package com.listeningraccoon.pamietaj_o_zdrowiu.backend.services;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.repositories.PrescriptionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Prescription createPrescription(String name, String group, String startDate, String endDate, String time, String email) {
        Prescription prescription = prescriptionRepository.insert(new Prescription(name, group, startDate, endDate, time));

        mongoTemplate.update(User.class)
                .matching(Criteria.where("email").is(email))
                .apply(new Update().push("prescriptions").value(prescription))
                .first();

        return prescription;
    }

    public Optional<Prescription> getPrescriptionById(ObjectId id) {
        return prescriptionRepository.findById(id);
    }

    public List<Prescription> getAllPrescriptionsOfUser(String email) {
        Optional<User> user = userService.getUserByEmail(email);

        return user.map(User::getPrescriptions).orElse(null);
    }

    public List<Prescription> getPrescriptionsOfUserByFilter(String email, String filter) {
        List<Prescription> prescriptions = getAllPrescriptionsOfUser(email);
        List<Prescription> filteredPrescriptions = new ArrayList<>();
        if (prescriptions != null) {
            for (Prescription prescription : prescriptions) {
                if (prescription.getName().contains(filter) || prescription.getGroup().contains(filter)) {
                    filteredPrescriptions.add(prescription);
                }
            }
        }
        return filteredPrescriptions;
    }

    public void deletePrescription(ObjectId prescriptionId) {
        prescriptionRepository.deleteById(prescriptionId);
    }

    public long getCount() {
        return prescriptionRepository.count();
    }
}
