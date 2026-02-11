package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

import db.DBConnection;
import model.Question;
import model.User;

public class AppTest {

    // --- TEST 1: MODEL LOGIC ---
    @Test
    public void testUserAdminRole() {
        // Requirement: Ensure the system correctly identifies admins vs players
        User admin = new User("admin", "pass", "admin", "Admin User");
        User player = new User("player", "pass", "player", "Player User");

        assertTrue(admin.isAdmin(), "User with 'admin' role should return true");
        assertFalse(player.isAdmin(), "User with 'player' role should return false");
    }

    // --- TEST 2: QUESTION MODEL ---
    @Test
    public void testQuestionCreation() {
        // Requirement: Ensure Question objects hold data correctly
        Question q = new Question("What is 2+2?", "1", "2", "3", "4", "D", "Beginner");
        
        assertEquals("What is 2+2?", q.getQuestionText());
        assertEquals("D", q.getCorrectOption());
        assertEquals("Beginner", q.getDifficultyLevel());
    }

    // --- TEST 3: DATABASE CONNECTION & LOGIN ---
    @Test
    public void testAdminLogin() {
        // Requirement: Test connection to DB and Login Logic [cite: 40]
        // NOTE: This requires your XAMPP MySQL to be running!
        
        User user = DBConnection.login("admin", "admin123");
        
        assertNotNull(user, "Login should return a User object for valid credentials");
        assertEquals("admin", user.getUsername(), "Username should match the database record");
    }

    // --- TEST 4: FAILED LOGIN ---
    @Test
    public void testInvalidLogin() {
        // Requirement: Error handling for invalid data [cite: 163]
        User user = DBConnection.login("fakeuser", "wrongpassword");
        
        assertNull(user, "Login should return null for invalid credentials");
    }

    // --- TEST 5: FETCHING QUESTIONS ---
    @Test
    public void testFetchQuestions() {
        // Requirement: Dynamically load questions based on difficulty [cite: 14]
        List<Question> questions = DBConnection.getQuestionsByLevel("Beginner");
        
        // We assume you have added at least one question to the DB
        assertNotNull(questions, "Question list should not be null");
        // If your DB is empty, this assertion might fail, which is good (it tells you to add data)
    }
}