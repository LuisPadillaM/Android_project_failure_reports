package com.example.luispadilla.projecto_averias.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.luispadilla.projecto_averias.R;
import com.example.luispadilla.projecto_averias.bd.DatabaseHelper;
import com.example.luispadilla.projecto_averias.bd.User;
import com.example.luispadilla.projecto_averias.utils.FragmentUtils;
import com.example.luispadilla.projecto_averias.utils.PreferenceManager;
import com.example.luispadilla.projecto_averias.utils.Utilities;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginInteractionListener, AddUserFragment.AddUserInteractionListener{
    FragmentManager fm;
    LoginFragment loginFm = LoginFragment.newInstance();
    AddUserFragment addFm = AddUserFragment.newInstance();
    Integer FragmentContainer = R.id.fragment_container;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = DatabaseHelper.getInstance(MainActivity.this);
        fm = getSupportFragmentManager();
        FragmentUtils.addFragment(fm, FragmentContainer, loginFm);
    }


    @Override
    public void addUser() {
        this.validateForm();
    }


    public Boolean userAlreadyExist(String username){
        List<User> users;
        Boolean userExist = true;
        try {
            Dao<User, Integer> userDao = dbHelper.getUserDao();
            users = userDao.queryBuilder().where().eq("username", username.trim()).query();
            userExist = users.size() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userExist;
    }

    public boolean emptyFormValues(HashMap<String, Object> map){
        Boolean isFormInvalid = false;
        for (Object value : map.values()) {
            if(Utilities.isEmptyStringField((String)value)){
                isFormInvalid = true;
                break;
            };
        }
        return isFormInvalid;
    }

    public void validateForm() {
        String name = addFm.userName.getText().toString();
        String email = addFm.userEmail.getText().toString();
        String phone = addFm.userPhone.getText().toString();
        String idCard = addFm.userIdCard.getText().toString();
        String userName = addFm.userUserName.getText().toString();
        String password = addFm.userPassword.getText().toString();
        String passwordConfirm = addFm.userPasswordConfirm.getText().toString();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("email", email);
        map.put("phone", phone);
        map.put("idCard", idCard);
        map.put("userName", userName);
        map.put("password", password);
        map.put("passwordConfirm", passwordConfirm);

        if(!password.equals(passwordConfirm)){
            Utilities.showToast(this, "Passwords doesn't match");
            return;
        }

        if(this.userAlreadyExist(userName)){
            Utilities.showToast(this, "User already exist");
            return;
        }

        if(this.emptyFormValues(map)){
            Utilities.showToast(this, "Form has empty values");
            return;

        }

        User.Builder userBuilder = new User.Builder();
        userBuilder.username(userName).password(password).name(name).email(email).phone(phone).userIdCard(idCard);
        User newUser = userBuilder.build();

        if(this.addDBUser(newUser)){
            FragmentUtils.changeFragment(fm, FragmentContainer, loginFm, true);
        } else {
            Utilities.showToast(this, "There's an error adding your user");
        }

    }

    public Boolean addDBUser (User user) {
        try {
            Dao<User, Integer> userDao = dbHelper.getUserDao();
            userDao.create(user);
            PreferenceManager.savePreferences(this, user.username , user.email);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void login() {
        try {
            Dao<User, Integer> userDao = dbHelper.getUserDao();
            Where filter = userDao.queryBuilder().where()
                    .eq("username", loginFm.userInput.getText().toString())
                    .and().eq("password", loginFm.passwordInput.getText().toString());
            List<User> users = filter.query();

            //Si no se encontro ningun usuario, es porque no existe
            if(users.size() == 0){
                Utilities.showToast(MainActivity.this, "Wrong username or password");
                return;
            } else {
                Utilities.SwitchActivity(this, FaultActivity.class, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void register() {
        FragmentUtils.changeFragment(fm, FragmentContainer, addFm, true);
    }
}
