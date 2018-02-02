package entity;

import java.io.Serializable;
import java.util.Date;

public class student implements Serializable{
    private String Id;
    private String name;
    private Date birthday;
    private String description;
    private int average;

    public student() {
    }

    public student(String id, String name, Date birthday, String description, int average) {
        this.Id = id;
        this.name = name;
        this.birthday = birthday;
        this.description = description;
        this.average = average;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return "student{" +
                "Id='" + Id + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", description='" + description + '\'' +
                ", average=" + average +
                '}';
    }
}
