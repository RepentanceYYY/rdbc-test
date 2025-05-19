package com.test.security6.comm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ResponseResult success(int code,String message,T data){
        return new ResponseResult(code,message,data);
    }
    public static <T> ResponseResult success(String message,T data){
        return new ResponseResult(200,message,data);
    }
    public static <T> ResponseResult success(T data){
        return new ResponseResult(200,"success",data);
    }
    public static <T> ResponseResult success(){
        return new ResponseResult(200,"success",null);
    }
    public static <T> ResponseResult error(int code,String message,T data){
        return new ResponseResult(code,message,data);
    }
    public static <T> ResponseResult error(String message,T data){
        return new ResponseResult(404,message,data);
    }
    public static <T> ResponseResult error(T data){
        return new ResponseResult(404,"error",data);
    }
    public static <T> ResponseResult error(){
        return new ResponseResult(404,"error",null);
    }
}
