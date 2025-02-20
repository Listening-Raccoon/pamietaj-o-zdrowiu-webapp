package com.listeningraccoon.pamietaj_o_zdrowiu.backend.data;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
public class Prescription {
    @Id
    private ObjectId id;

    private String name;
    private String group;
    private String startTime;
    private String endTime;
}
