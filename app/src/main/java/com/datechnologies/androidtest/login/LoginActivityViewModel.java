package com.datechnologies.androidtest.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.datechnologies.androidtest.model.LoginBody;
import com.datechnologies.androidtest.model.LoginResponse;
import com.datechnologies.androidtest.repository.MainRepository;

public class LoginActivityViewModel extends ViewModel {
    public String emailAddress = null;
    public String password = null;
    public boolean isSuccess = false;
    private static final String SUCCESS_KEY = "Success";
    MutableLiveData<String> loginResult = new MutableLiveData<>();

    MainRepository mainRepository = new MainRepository();

    public void login(String email, String password){
        mainRepository.loginRemote(new LoginBody(email, password), new MainRepository.OnLoginResponse() {
            @Override
            public void onResponse(LoginResponse loginResponse) {
                if(loginResponse.getCode().equals(SUCCESS_KEY)){
                    isSuccess = true;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Code: " + loginResponse.getCode() + "\n");
                sb.append("Message: " + loginResponse.getMessage() + "\n");
                sb.append("Duration: " + loginResponse.getRequestCallTime() + "ms");
                loginResult.postValue(sb.toString());
            }

            @Override
            public void onFailure(Throwable throwable) {
                loginResult.postValue(throwable.toString());
            }
        });
    }

    public LiveData<String> getLoginResult(){
        return loginResult;
    }

}
