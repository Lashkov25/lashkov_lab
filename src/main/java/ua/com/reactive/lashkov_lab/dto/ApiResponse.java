package ua.com.reactive.lashkov_lab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String message;
    private boolean success;

    public ApiResponse(T data) {
        this.data = data;
        this.success = true;
        this.message = "Success";
    }

    public ApiResponse(String message) {
        this.message = message;
        this.success = true;
    }
}
