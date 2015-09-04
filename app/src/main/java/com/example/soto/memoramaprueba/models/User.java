package com.example.soto.memoramaprueba.models;

/**
 * This class is a basic model representation of an user.
 * @author Jose Soto
 */
public class User {

    private int id;
    private String userName;
    private String nickName;
    private String password;
    private String imageURL;
    private int bestScore = 30*60*1000; //30 minutes by default

    public User(int id, String userName, String nickName, String password, String imageURL, int bestScore) {
        this.id = id;
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
        this.imageURL = imageURL;
        this.bestScore = bestScore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", bestScore=" + bestScore +
                '}';
    }
}
