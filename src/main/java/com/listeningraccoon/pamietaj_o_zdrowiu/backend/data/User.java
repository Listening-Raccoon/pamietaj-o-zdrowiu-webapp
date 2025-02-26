package com.listeningraccoon.pamietaj_o_zdrowiu.backend.data;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Getter
    private ObjectId id;

    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String email;
    @Getter
    @DocumentReference
    private List<Prescription> prescriptions;
    @Getter
    @DocumentReference
    private List<WebUser> assignedUsers;

    public User(String firstName, String lastName, String email, List<Prescription> prescriptions, List<WebUser> assignedUsers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.prescriptions = prescriptions;
        this.assignedUsers = assignedUsers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nID: " + id);
        sb.append("\nFirst Name: " + firstName);
        sb.append("\nLast Name: " + lastName);
        sb.append("\nEmail: " + email);
        sb.append("\nPrescriptions: ");
        if (prescriptions.isEmpty()) {
            sb.append("none");
            return sb.toString();
        }
        for (Prescription p : prescriptions) {
            sb.append(p.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
