package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.RequestedBookRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.dao.RoleRepository;
import com.ibizabroker.lms.dao.BorrowRepository;
import com.ibizabroker.lms.entity.Role;
import com.ibizabroker.lms.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service class responsible for managing user-related operations in the library system.
 * Handles user creation, role management, and user statistics.
 * Provides functionality for system initialization with admin user creation.
 *
 * @author codematrix
 * @version 1.0
 */
@Service
public class UserService {

    /**
     * Repository for performing database operations on users.
     */
    @Autowired
    private UsersRepository usersRepository;

    /**
     * Repository for managing user roles.
     */
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Repository for managing book borrowing records.
     */
    @Autowired
    private BorrowRepository borrowRepository;

    /**
     * Repository for managing book requests.
     */
    @Autowired
    private RequestedBookRepository requestedBookRepository;

    /**
     * Encoder for securely hashing user passwords.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Creates an admin user if one does not already exist in the system.
     * This method is used during system initialization to ensure there is always
     * an administrator account available. The method:
     * 1. Checks if admin user exists
     * 2. Creates or retrieves admin role
     * 3. Creates admin user with default credentials if needed
     * 
     * The default admin credentials are:
     * - Username: admin
     * - Password: admin123
     *
     * This method is transactional to ensure data consistency.
     */
    @Transactional
    public void createAdminUserIfNotExists() {
        Optional<Users> existingAdmin = usersRepository.findByUsername("admin");

        if (!existingAdmin.isPresent()) {
            // Check if "Admin" role exists
            Optional<Role> adminRoleOptional = roleRepository.findByRoleName("Admin");
            Role adminRole;

            if (adminRoleOptional.isPresent()) {
                adminRole = adminRoleOptional.get();
            } else {
                // Create the "Admin" role if it does not exist
                adminRole = new Role();
                adminRole.setRoleName("Admin");
                adminRole = roleRepository.save(adminRole);
            }

            // Create the admin user
            Users adminUser = new Users();
            adminUser.setUsername("admin");
            adminUser.setName("Administrator");
            adminUser.setPassword(passwordEncoder.encode("admin123")); // Always hash passwords!

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            adminUser.setRole(roles);

            usersRepository.save(adminUser);
            System.out.println("✅ Admin user created successfully!");
        } else {
            System.out.println("✅ Admin user already exists!");
        }
    }

    /**
     * Gets the total number of users in the system.
     *
     * @return The total count of registered users
     */
    public long getTotalUsers() {
        return usersRepository.count();
    }
}
