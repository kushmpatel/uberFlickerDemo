package com.uber.kush.helper;

public  class Result {
    public String mResponse;
    public Exception mException;
    public Result(String mResponse) {
        this.mResponse = mResponse;
    }
    public Result(Exception exception) {
        mException = exception;
    }
}