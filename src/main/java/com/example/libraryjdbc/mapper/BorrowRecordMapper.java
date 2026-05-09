package com.example.libraryjdbc.mapper;

import com.example.libraryjdbc.dto.BorrowResponse;
import com.example.libraryjdbc.entity.BorrowRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowRecordMapper {

    public static BorrowResponse toResponse(BorrowRecord borrowRecord) {
        if (borrowRecord == null) return null;

        return new BorrowResponse(borrowRecord.getId(), borrowRecord.getBook().getTitle(),
                borrowRecord.getStudent().getFullName(), borrowRecord.getBorrowDate(),
                borrowRecord.getReturnDate(), borrowRecord.getStatus());
    }


}
