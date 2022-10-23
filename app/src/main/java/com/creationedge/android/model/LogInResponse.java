package com.creationedge.android.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogInResponse {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_nicename")
    @Expose
    private String userNicename;
    @SerializedName("user_display_name")
    @Expose
    private String userDisplayName;
    @SerializedName("user_role")
    @Expose
    private List<String> userRole = null;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    /**
     * No args constructor for use in serialization
     *
     */
    public LogInResponse() {
    }

    /**
     *
     * @param firstName
     * @param lastName
     * @param userDisplayName
     * @param userEmail
     * @param avatar
     * @param userRole
     * @param userId
     * @param token
     * @param userNicename
     */
    public LogInResponse(String token, String userEmail, String userNicename, String userDisplayName, List<String> userRole, Integer userId, String firstName, String lastName, String avatar) {
        super();
        this.token = token;
        this.userEmail = userEmail;
        this.userNicename = userNicename;
        this.userDisplayName = userDisplayName;
        this.userRole = userRole;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNicename() {
        return userNicename;
    }

    public void setUserNicename(String userNicename) {
        this.userNicename = userNicename;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public List<String> getUserRole() {
        return userRole;
    }

    public void setUserRole(List<String> userRole) {
        this.userRole = userRole;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}