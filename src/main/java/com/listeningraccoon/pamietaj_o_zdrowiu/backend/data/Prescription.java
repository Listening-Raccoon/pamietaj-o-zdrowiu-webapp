package com.listeningraccoon.pamietaj_o_zdrowiu.backend.data;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.checkerframework.checker.formatter.qual.Format;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "prescriptions")
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {
    @Id
    private ObjectId _id;

    private String name;
    private String group;
    private String startDate;
    private String endDate;
    private String time;

    public Prescription(String name, String group, String startDate, String endDate, String time) {
        this.name = name;
        this.group = group;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ObjectId get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
