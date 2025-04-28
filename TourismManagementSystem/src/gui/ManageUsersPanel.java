package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import dao.UserDAO;
import models.User;

public class ManageUsersPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable usersTable;
    private DefaultTableModel tableModel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JCheckBox adminCheckBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private User selectedUser;
    
    public ManageUsersPanel() {
        setLayout(new BorderLayout(10, 10));
        
        // Create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new TitledBorder("Users"));
        
        // Create table model
        String[] columnNames = {"ID", "Username", "Full Name", "Email", "Phone", "Address", "Admin"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        usersTable = new JTable(tableModel);
        usersTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(usersTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        // Create form panel
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(new TitledBorder("User Details"));
        
        JPanel fieldsPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        fieldsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        fieldsPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        fieldsPanel.add(usernameField);
        
        fieldsPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        fieldsPanel.add(passwordField);
        
        fieldsPanel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        fieldsPanel.add(fullNameField);
        
        fieldsPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        fieldsPanel.add(emailField);
        
        fieldsPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        fieldsPanel.add(phoneField);
        
        fieldsPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        fieldsPanel.add(addressField);
        
        fieldsPanel.add(new JLabel("Admin:"));
        adminCheckBox = new JCheckBox();
        fieldsPanel.add(adminCheckBox);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        addButton = new JButton("Add User");
        updateButton = new JButton("Update User");
        deleteButton = new JButton("Delete User");
        clearButton = new JButton("Clear Form");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(formPanel, BorderLayout.SOUTH);
        
        // Load users
        loadUsers();
        
        // Add listeners
        usersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = usersTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        int userId = (int) tableModel.getValueAt(selectedRow, 0);
                        displayUserDetails(userId);
                    }
                }
            }
        });
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });
        
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
    }
    
    private void loadUsers() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Load users from database
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        
        for (User user : users) {
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.isAdmin() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }
    
    private void displayUserDetails(int userId) {
        UserDAO userDAO = new UserDAO();
        selectedUser = userDAO.getUserById(userId);
        
        if (selectedUser != null) {
            usernameField.setText(selectedUser.getUsername());
            passwordField.setText(selectedUser.getPassword());
            fullNameField.setText(selectedUser.getFullName());
            emailField.setText(selectedUser.getEmail());
            phoneField.setText(selectedUser.getPhone());
            addressField.setText(selectedUser.getAddress());
            adminCheckBox.setSelected(selectedUser.isAdmin());
            
            updateButton.setEnabled(true);
            deleteButton.setEnabled(true);
        }
    }
    
    private void addUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        boolean isAdmin = adminCheckBox.isSelected();
        
        // Validate input
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create user object
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setAdmin(isAdmin);
        
        // Add user to database
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.registerUser(user);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUsers();  // Refresh users
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add user. Username may already exist.", "Add Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateUser() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Please select a user to update", "Update Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        boolean isAdmin = adminCheckBox.isSelected();
        
        // Validate input
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Update user object
        selectedUser.setUsername(username);
        selectedUser.setPassword(password);
        selectedUser.setFullName(fullName);
        selectedUser.setEmail(email);
        selectedUser.setPhone(phone);
        selectedUser.setAddress(address);
        selectedUser.setAdmin(isAdmin);
        
        // Update user in database
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUser(selectedUser);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUsers();  // Refresh users
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update user", "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteUser() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirm deletion
        int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete user " + selectedUser.getUsername() + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.deleteUser(selectedUser.getUserId());
            
            if (success) {
                JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();  // Refresh users
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user", "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearForm() {
        selectedUser = null;
        usernameField.setText("");
        passwordField.setText("");
        fullNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        adminCheckBox.setSelected(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        usersTable.clearSelection();
    }
}

