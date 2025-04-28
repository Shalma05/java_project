package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import dao.UserDAO;
import models.User;

public class ProfilePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private User user;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JButton updateButton;
    
    public ProfilePanel(User user) {
        this.user = user;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new TitledBorder("My Profile"));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(user.getUsername());
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(user.getPassword());
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField(user.getPassword());
        formPanel.add(confirmPasswordField);
        
        formPanel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField(user.getFullName());
        formPanel.add(fullNameField);
        
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField(user.getEmail());
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField(user.getPhone());
        formPanel.add(phoneField);
        
        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField(user.getAddress());
        formPanel.add(addressField);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        updateButton = new JButton("Update Profile");
        buttonPanel.add(updateButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listener
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfile();
            }
        });
    }
    
    private void updateProfile() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        
        // Validate input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Update Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Update Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Update user object
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        
        // Update user in database
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUser(user);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile", "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
