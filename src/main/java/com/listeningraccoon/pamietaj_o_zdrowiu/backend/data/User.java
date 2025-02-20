package com.listeningraccoon.pamietaj_o_zdrowiu.backend.data;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class User {
    @Id
    private ObjectId id;

    private String firstName;
    private String lastName;
    private String email;
    private List<Prescription> prescriptions;
}
