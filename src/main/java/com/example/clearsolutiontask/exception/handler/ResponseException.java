package com.example.clearsolutiontask.exception.handler;

import lombok.Data;

@Data
public class ResponseException {
    private int statusCode;
    private String message;

    public ResponseException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
