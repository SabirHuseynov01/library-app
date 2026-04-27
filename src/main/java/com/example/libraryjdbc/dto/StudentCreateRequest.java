package com.example.libraryjdbc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class StudentCreateRequest {

    @NotBlank(message = "Fullname bos qalabilmez")
    private String fullName;

    @NotBlank(message = "Email bos qalabilmez")
    @Email(message = "Email format xetasi")
    private String email;
    private String phone;

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }
}
