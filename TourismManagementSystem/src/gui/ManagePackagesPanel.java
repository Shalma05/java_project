package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import dao.PackageDAO;
import models.Package;

public class ManagePackagesPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable packagesTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField destinationField;
    private JTextArea descriptionArea;
    private JSpinner durationSpinner;
    private JSpinner priceSpinner;
    private JSpinner seatsSpinner;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private Package selectedPackage;
    
    public ManagePackagesPanel() {
        setLayout(new BorderLayout(10, 10));
        
        // Create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new TitledBorder("Tour Packages"));
        
        // Create table model
        String[] columnNames = {"ID", "Name", "Destination", "Duration (days)", "Price ($)", "Available Seats"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        packagesTable = new JTable(tableModel);
        packagesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(packagesTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        // Create form panel
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(new TitledBorder("Package Details"));
        
        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        fieldsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        fieldsPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        fieldsPanel.add(nameField);
        
        fieldsPanel.add(new JLabel("Destination:"));
        destinationField = new JTextField();
        fieldsPanel.add(destinationField);
        
        fieldsPanel.add(new JLabel("Duration (days):"));
        durationSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
        fieldsPanel.add(durationSpinner);
        
        fieldsPanel.add(new JLabel("Price ($):"));
        priceSpinner = new JSpinner(new SpinnerNumberModel(100.0, 10.0, 10000.0, 10.0));
        fieldsPanel.add(priceSpinner);
        
        fieldsPanel.add(new JLabel("Available Seats:"));
        seatsSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        fieldsPanel.add(seatsSpinner);
        
        fieldsPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        fieldsPanel.add(descScrollPane);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        addButton = new JButton("Add Package");
        updateButton = new JButton("Update Package");
        deleteButton = new JButton("Delete Package");
        clearButton = new JButton("Clear Form");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(formPanel, BorderLayout.SOUTH);
        
        // Load packages
        loadPackages();
        
        // Add listeners
        packagesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = packagesTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        int packageId = (int) tableModel.getValueAt(selectedRow, 0);
                        displayPackageDetails(packageId);
                    }
                }
            }
        });
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPackage();
            }
        });
        
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePackage();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePackage();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
    }
    
    private void loadPackages() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Load packages from database
        PackageDAO packageDAO = new PackageDAO();
        List<Package> packages = packageDAO.getAllPackages();
        
        for (Package pkg : packages) {
            Object[] row = {
                pkg.getPackageId(),
                pkg.getName(),
                pkg.getDestination(),
                pkg.getDuration(),
                pkg.getPrice(),
                pkg.getAvailableSeats()
            };
            tableModel.addRow(row);
        }
    }
    
    private void displayPackageDetails(int packageId) {
        PackageDAO packageDAO = new PackageDAO();
        selectedPackage = packageDAO.getPackageById(packageId);
        
        if (selectedPackage != null) {
            nameField.setText(selectedPackage.getName());
            destinationField.setText(selectedPackage.getDestination());
            durationSpinner.setValue(selectedPackage.getDuration());
            priceSpinner.setValue(selectedPackage.getPrice());
            seatsSpinner.setValue(selectedPackage.getAvailableSeats());
            descriptionArea.setText(selectedPackage.getDescription());
            
            updateButton.setEnabled(true);
            deleteButton.setEnabled(true);
        }
    }
    
    private void addPackage() {
        String name = nameField.getText();
        String destination = destinationField.getText();
        int duration = (int) durationSpinner.getValue();
        double price = (double) priceSpinner.getValue();
        int seats = (int) seatsSpinner.getValue();
        String description = descriptionArea.getText();
        
        // Validate input
        if (name.isEmpty() || destination.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create package object
        Package pkg = new Package();
        pkg.setName(name);
        pkg.setDestination(destination);
        pkg.setDuration(duration);
        pkg.setPrice(price);
        pkg.setAvailableSeats(seats);
        pkg.setDescription(description);
        
        // Add package to database
        PackageDAO packageDAO = new PackageDAO();
        boolean success = packageDAO.addPackage(pkg);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Package added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadPackages();  // Refresh packages
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add package", "Add Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updatePackage() {
        if (selectedPackage == null) {
            JOptionPane.showMessageDialog(this, "Please select a package to update", "Update Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String name = nameField.getText();
        String destination = destinationField.getText();
        int duration = (int) durationSpinner.getValue();
        double price = (double) priceSpinner.getValue();
        int seats = (int) seatsSpinner.getValue();
        String description = descriptionArea.getText();
        
        // Validate input
        if (name.isEmpty() || destination.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Update package object
        selectedPackage.setName(name);
        selectedPackage.setDestination(destination);
        selectedPackage.setDuration(duration);
        selectedPackage.setPrice(price);
        selectedPackage.setAvailableSeats(seats);
        selectedPackage.setDescription(description);
        
        // Update package in database
        PackageDAO packageDAO = new PackageDAO();
        boolean success = packageDAO.updatePackage(selectedPackage);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Package updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadPackages();  // Refresh packages
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update package", "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deletePackage() {
        if (selectedPackage == null) {
            JOptionPane.showMessageDialog(this, "Please select a package to delete", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirm deletion
        int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete package " + selectedPackage.getName() + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            PackageDAO packageDAO = new PackageDAO();
            boolean success = packageDAO.deletePackage(selectedPackage.getPackageId());
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Package deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPackages();  // Refresh packages
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete package", "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearForm() {
        selectedPackage = null;
        nameField.setText("");
        destinationField.setText("");
        durationSpinner.setValue(1);
        priceSpinner.setValue(100.0);
        seatsSpinner.setValue(10);
        descriptionArea.setText("");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        packagesTable.clearSelection();
    }
}
