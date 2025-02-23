package com.listeningraccoon.pamietaj_o_zdrowiu.backend.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "prescriptions")
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {
    @Id
    @Getter
    private ObjectId id;

    @Getter
    private String name;
    @Getter
    private String group;
    @Getter
    private String startDate;
    @Getter
    private String endDate;
    @Getter
    private String time;

    public Prescription(String name, String group, String startDate, String endDate, String time) {
        this.name = name;
        this.group = group;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
    }
}
