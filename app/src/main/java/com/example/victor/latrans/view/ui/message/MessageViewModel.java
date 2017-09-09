package com.example.victor.latrans.view.ui.message;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.MessageRepository;
import com.example.victor.latrans.repocitory.local.db.entity.Message;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Victor on 9/9/2017.
 */

public class MessageViewModel extends ViewModel implements AppComponent.Injectable {

    @Inject
    MessageRepository mMessageRepository;

    @Override
    public void inject(AppComponent appComponent) {
        appComponent.inject(this);
    }

    LiveData<Resource<List<Message>>> mLiveData;
     MutableLiveData<Integer> dialogueId = new MutableLiveData<>();

     public void setDialogueId(int id){
         dialogueId.setValue(id);
    }



    public LiveData<Resource<List<Message>>> getResponse(){
        mLiveData = new MutableLiveData<>();
        processResponse();
        return mLiveData;
    }

    private void processResponse(){
        //TODO remove hardcoded id
        mLiveData = Transformations.switchMap(dialogueId, new Function<Integer, LiveData<Resource<List<Message>>>>() {
            @Override
            public LiveData<Resource<List<Message>>> apply(Integer input) {
                return mMessageRepository.getMessages(input);
            }
        });
    }

}
