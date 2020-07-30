package com.gestvicole.gestionavicole.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBody {
    private String status = null;
    private String message = null;
    private Object response = null;

    public ResponseBody(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ResponseBody with(Object object, String message) {
        return new ResponseBody("OK",message,object);
    }

    public static ResponseBody success(String message) {
        return new ResponseBody("OK",message);
    }

    public static ResponseBody error(String message) {
        return new ResponseBody("KO",message);
    }
}
