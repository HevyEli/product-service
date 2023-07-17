package org.example.dto;

import org.springframework.http.HttpStatus;

public class ResponseMessage {
    private HttpStatus statusCode;
    private String message;
    private String productResponse;

    public ResponseMessage(HttpStatus statusCode, String message , String productResponse) {
        this.statusCode = statusCode;
        this.message = message;
        this.productResponse = productResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = HttpStatus.valueOf(statusCode);
    }

    public String getProductResponse() {
        return productResponse;
    }

    public void setProductResponse(String productResponse) {
        this.productResponse = productResponse;
    }
}
