package com.example.libraryjdbc.repository;

import com.example.libraryjdbc.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByStatus(String status);

    List<BorrowRecord> findByStudentId(Long studentId);

    boolean existsByStudentIdAndStatus(Long studentId, String status);

    @Query("SELECT br FROM BorrowRecord br JOIN FETCH br.book JOIN FETCH br.student WHERE br.status = 'BORROWED'")
    List<BorrowRecord> findActiveWithDetails();

    @Query("SELECT br FROM BorrowRecord br JOIN FETCH br.book JOIN FETCH br.student")
    List<BorrowRecord> findAllWithDetails();

    @Query("SELECT br FROM BorrowRecord br JOIN FETCH br.book JOIN FETCH br.student WHERE br.student.id = ?1")
    List<BorrowRecord> findByStudentIdWithDetails(Long studentId);

    @Query("SELECT b.title, COUNT(br) FROM BorrowRecord br JOIN br.book b GROUP BY b.id, b.title ORDER BY COUNT(br) DESC")
    List<Object[]> findMostBorrowedBooks();


}