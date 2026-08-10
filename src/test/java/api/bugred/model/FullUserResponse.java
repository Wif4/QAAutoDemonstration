package api.bugred.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//TODO:
// FulluserResponse inheritant of the RegisterUser??
// Response->Request type of bond?? Maybe shouldn't extend RegisterUser

@JsonIgnoreProperties(ignoreUnknown = true)
public class FullUserResponse extends RegisterUser{
    private String avatar;
    private Integer birthday;
    private String hobby;
    private String gender;
    private Integer dateStart;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getBirthday() {
        return birthday;
    }

    public void setBirthday(Integer birthday) {
        this.birthday = birthday;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getDateStart() {
        return dateStart;
    }

    public void setDateStart(Integer dateStart) {
        this.dateStart = dateStart;
    }
}
