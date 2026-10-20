package com.employee.employee_management.Persitance.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.employee.employee_management.Persitance.entity.User;
import com.employee.employee_management.Persitance.repository.UserRepository;

@Service 
@Transactional 
public class UserService {
    
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "User tidak ditemukan dengan ID: " + id));
    }

    // Get user by username
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "User tidak ditemukan: " + username));
    }

    // Create user
    public User createUser(User user, String createdBy) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(
                    "Username sudah digunakan");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                    "Email sudah digunakan");
        }

        Date now = new Date();

        user.setId(null);
        user.setUsercrt(createdBy);
        user.setDtmcrt(now);
        user.setUsrupd(createdBy);
        user.setDtmupd(now);

        return userRepository.save(user);
    }

    // Update user
    public User updateUser(Long id, User updatedUser, String updatedBy) {
        User existingUser = getUserById(id);

        if (userRepository.existsByUsernameAndIdNot(
                updatedUser.getUsername(), id)) {
            throw new IllegalArgumentException(
                    "Username sudah digunakan");
        }

        if (userRepository.existsByEmailAndIdNot(
                updatedUser.getEmail(), id)) {
            throw new IllegalArgumentException(
                    "Email sudah digunakan");
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setStatus(updatedUser.getStatus());

        // Password tidak diubah melalui update profil biasa.
        existingUser.setUsrupd(updatedBy);
        existingUser.setDtmupd(new Date());

        return userRepository.save(existingUser);
    }

    // Approve user
    public User approveUser(Long id, String approvedBy) {
        User user = getUserById(id);

        user.setDtmapp(new Date());
        user.setUserapp(approvedBy);
        user.setStatus("APPROVED");
        user.setUsrupd(approvedBy);
        user.setDtmupd(new Date());

        return userRepository.save(user);
    }

    // Change user status
    public User updateUserStatus(
            Long id, String status, String updatedBy) {

        User user = getUserById(id);

        user.setStatus(status);
        user.setUsrupd(updatedBy);
        user.setDtmupd(new Date());

        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    
}
