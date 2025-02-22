package com.listeningraccoon.pamietaj_o_zdrowiu.backend.repositories;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PrescriptionRepository extends MongoRepository<Prescription, ObjectId> {
}
