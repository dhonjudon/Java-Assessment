package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Question;
import model.User;

public class DBConnection {
    // Fetch all questions for admin editing
    public static ResultSet getAllQuestions() throws SQLException {
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM questions");
    }

    // Update question by ID
    public static boolean updateQuestion(int id, String qText, String a, String b, String c, String d, String correct,
            String diff) {
        String sql = "UPDATE questions SET question_text=?, option_a=?, option_b=?, option_c=?, option_d=?, correct_option=?, difficulty_level=? WHERE question_id=?";
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, qText);
            stmt.setString(2, a);
            stmt.setString(3, b);
            stmt.setString(4, c);
            stmt.setString(5, d);
            stmt.setString(6, correct);
            stmt.setString(7, diff);
            stmt.setInt(8, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE THESE TO MATCH YOUR MYSQL
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_app_db";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // --- User Methods ---
    public static User login(String username, String password) {
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setRole(rs.getString("role"));
                u.setFullName(rs.getString("full_name"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean registerUser(User user) {
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO users (username, password, role, full_name, email) VALUES (?, ?, 'player', ?, ?)")) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, "test@email.com"); // Placeholder or add to UI
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Question Methods ---
    public static List<Question> getQuestionsByLevel(String level) {
        List<Question> list = new ArrayList<>();
        // Fetch 5 random questions for the quiz
        String sql = "SELECT * FROM questions WHERE difficulty_level=? ORDER BY RAND() LIMIT 5";
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, level);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Question q = new Question();
                q.setQuestionId(rs.getInt("question_id"));
                q.setQuestionText(rs.getString("question_text"));
                q.setOptionA(rs.getString("option_a"));
                q.setOptionB(rs.getString("option_b"));
                q.setOptionC(rs.getString("option_c"));
                q.setOptionD(rs.getString("option_d"));
                q.setCorrectOption(rs.getString("correct_option"));
                q.setDifficultyLevel(rs.getString("difficulty_level"));
                list.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean addQuestion(Question q) {
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option, difficulty_level) VALUES (?,?,?,?,?,?,?)")) {
            stmt.setString(1, q.getQuestionText());
            stmt.setString(2, q.getOptionA());
            stmt.setString(3, q.getOptionB());
            stmt.setString(4, q.getOptionC());
            stmt.setString(5, q.getOptionD());
            stmt.setString(6, q.getCorrectOption());
            stmt.setString(7, q.getDifficultyLevel());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Reporting / Score Manager Methods ---
    public static void saveScore(int userId, int score, int total, String level) {
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO results (user_id, score, total_questions, difficulty_level) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, userId);
            stmt.setInt(2, score);
            stmt.setInt(3, total);
            stmt.setString(4, level);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getHighScores() throws SQLException {
        // Returns Top Scores: Player Name | Score | Level
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(
                "SELECT u.username, r.score, r.difficulty_level, r.date_taken " +
                        "FROM results r JOIN users u ON r.user_id = u.user_id " +
                        "ORDER BY r.score DESC, r.date_taken DESC LIMIT 20");
    }
}