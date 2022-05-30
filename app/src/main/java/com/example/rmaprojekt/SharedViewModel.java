package com.example.rmaprojekt;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<JSONObject> infoObject = new MutableLiveData<>();

    public void setInfoObject(JSONObject newInfoObject) {
        infoObject.setValue(newInfoObject);
    }

    public LiveData<JSONObject> getInfObject() {
        return infoObject;
    }
}
