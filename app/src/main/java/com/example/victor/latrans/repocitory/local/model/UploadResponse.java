package com.example.victor.latrans.repocitory.local.model;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;

/**
 * Created by Victor on 9/11/2017.
 */

public class UploadResponse {

    public TransferState mState;
    public Long mLong;

    public Exception getException() {
        return mException;
    }

    public void setException(Exception exception) {
        mException = exception;
    }

    public Exception mException;

    public UploadResponse(TransferState state, Long aLong, Exception exception) {
        mState = state;
        mLong = aLong;
        mException = exception;
    }

    public static UploadResponse state(TransferState state) {
        return new  UploadResponse( state, null, null);
    }

    public static UploadResponse progress(Long aLong) {
        return new  UploadResponse(null, aLong, null);
    }

    public static UploadResponse error(Exception exception) {
        return new  UploadResponse(null, null, exception);
    }



    public TransferState getState() {
        return mState;
    }

    public void setState(TransferState state) {
        mState = state;
    }

    public Long getLong() {
        return mLong;
    }

    public void setLong(Long aLong) {
        mLong = aLong;
    }


}
