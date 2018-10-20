package com.example.luispadilla.projecto_averias.bd;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")
public class User {

    @DatabaseField(generatedId = true, columnName = "user_id", canBeNull = false)
    public int userId;

    @DatabaseField(columnName = "username", canBeNull = false)
    public String username;

    @DatabaseField(columnName = "password", canBeNull = false)
    public String password;

    @SerializedName("nombre")
    @DatabaseField(columnName = "name", canBeNull = false)
    public String name;

    @SerializedName("correo")
    @DatabaseField(columnName = "email", canBeNull = false)
    public String email;

    @SerializedName("tel")
    @DatabaseField(columnName = "phone", canBeNull = false)
    public String phone;

    @SerializedName("cedula")
    @DatabaseField(columnName = "userIdCard", canBeNull = false)
    public String userIdCard;

    public User() {}

    public static class Builder {
        private String username;
        private String password;
        private String name;
        private String email;
        private String phone;
        private String userIdCard;

        public Builder(){
            // TODO make validation should always have username and pass
        }

        public User build(){
            return new User(this);
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder userIdCard(String userIdCard) {
            this.userIdCard = userIdCard;
            return this;
        }

    }

    private User(Builder builder) {
        this.username  = builder.username;
        this.password  = builder.password;
        this.name  = builder.name;
        this.email = builder.email;
        this.phone = builder.phone;
        this.userIdCard = builder.userIdCard;
    }

}
