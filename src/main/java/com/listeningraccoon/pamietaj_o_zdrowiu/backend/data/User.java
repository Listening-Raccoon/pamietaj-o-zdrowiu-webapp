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
    private ObjectId _id;

    private String firstName;
    private String lastName;
    private String email;
    @DocumentReference
    private List<Prescription> prescriptions;

    public User(String firstName, String lastName, String email, List<Prescription> prescriptions) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.prescriptions = prescriptions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nID: " + _id);
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

    public void set_id(ObjectId id) {
        this._id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public ObjectId get_id() {
        return _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }
}
