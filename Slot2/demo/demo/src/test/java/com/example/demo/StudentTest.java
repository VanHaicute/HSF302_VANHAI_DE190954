package com.example.demo;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StudentTest {

    @Autowired
    private StudentService studentService;

    @PersistenceContext
    private EntityManager entityManager;



    @Test
    public void testCreateAndRetrieveStudent() {

        studentService.createStudent("Test Student", "test@fpt.edu.vn", 25);
        Long id = (Long) entityManager.createQuery("SELECT MAX(s.id) FROM Student s").getSingleResult();

        entityManager.flush();
        entityManager.clear();

        // 2. Tìm theo đúng ID đó
        Student retrievedStudent = entityManager.find(Student.class, id);

        assertNotNull(retrievedStudent, "Student should exist in database");
        assertEquals(id, retrievedStudent.getId()); // So sánh với ID thực tế
        assertEquals("Test Student", retrievedStudent.getFullName());
    }

    @Test
    public void testDeleteStudent() {
        studentService.createStudent("Delete Me", "delete@fpt.edu.vn", 30);
        Long id = (Long) entityManager.createQuery("SELECT MAX(s.id) FROM Student s").getSingleResult();

        studentService.deleteStudent(id);

        entityManager.flush();
        entityManager.clear();

        Student deletedStudent = entityManager.find(Student.class, id);
        assertNull(deletedStudent, "Student should be null after deletion");
    }
}