package com.gregorybroche.imageresolver.classes;

public class ValidationResponse {
    private boolean isSuccess;
    private Object[] data;
    private String message;

    public ValidationResponse(boolean isSuccess, Object[] data, String message){
        this.isSuccess = isSuccess;
        this.data = data;
        this.message = message;
    }

    public boolean isSuccess(){
        return this.isSuccess;
    }

    public Object[] getData(){
        return this.data;
    }

    public String getMessage(){
        return this.message;
    }

    public boolean isDataNull(){
        return this.data == null;
    }

    public boolean isMessageNull(){
        return this.message == null;
    }
}
