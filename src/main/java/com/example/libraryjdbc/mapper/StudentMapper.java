package com.example.libraryjdbc.mapper;

import com.example.libraryjdbc.dto.StudentCreateRequest;
import com.example.libraryjdbc.dto.StudentResponse;
import com.example.libraryjdbc.entity.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    public static StudentResponse toResponse(StudentResponse student) {
        if (student == null) return null;

        return new StudentResponse(student.getId(), student.getFullName(),
                student.getEmail(), student.getPhone(), student.getCreatedAt());
    }

    public static Student toEntity(StudentCreateRequest request) {
        if (request == null) return null;

        Student student = new Student();
        student.setEmail(request.getEmail());
        student.setFullName(request.getFullName());
        student.setPhone(request.getPhone());

        return student;
    }


}
