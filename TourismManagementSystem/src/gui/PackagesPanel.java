package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

import dao.BookingDAO;
import dao.PackageDAO;
import models.Booking;
import models.Package;
import models.User;

public class PackagesPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private User user;
    private JTable packagesTable;
    private DefaultTableModel tableModel;
    private JTextArea descriptionArea;
    
    // Date selection components
    private JComboBox<Integer> travelDay, travelMonth, travelYear;
    
    private JSpinner travelersSpinner;
    private JTextField totalPriceField;
    private JButton bookButton;
    private Package selectedPackage;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public PackagesPanel(User user) {
        this.user = user;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new TitledBorder("Tour Packages"));
        
        // Create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
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
        
        // Create details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(new TitledBorder("Package Details & Booking"));
        
        // Description panel
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
        descPanel.add(descScrollPane, BorderLayout.CENTER);
        
        detailsPanel.add(descPanel, BorderLayout.NORTH);
        
        // Booking panel
        JPanel bookingPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        bookingPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Travel date components
        bookingPanel.add(new JLabel("Travel Date (DD/MM/YYYY):"));
        JPanel travelDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Create day, month, year dropdowns for travel date
        travelDay = new JComboBox<>();
        for (int i = 1; i <= 31; i++) travelDay.addItem(i);
        
        travelMonth = new JComboBox<>();
        for (int i = 1; i <= 12; i++) travelMonth.addItem(i);
        
        travelYear = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i <= currentYear + 2; i++) travelYear.addItem(i);
        
        travelDatePanel.add(travelDay);
        travelDatePanel.add(new JLabel("/"));
        travelDatePanel.add(travelMonth);
        travelDatePanel.add(new JLabel("/"));
        travelDatePanel.add(travelYear);
        bookingPanel.add(travelDatePanel);
        
        bookingPanel.add(new JLabel("Number of Travelers:"));
        travelersSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        bookingPanel.add(travelersSpinner);
        
        bookingPanel.add(new JLabel("Total Price:"));
        totalPriceField = new JTextField();
        totalPriceField.setEditable(false);
        bookingPanel.add(totalPriceField);
        
        bookingPanel.add(new JLabel(""));
        bookButton = new JButton("Book Package");
        bookButton.setEnabled(false);
        bookingPanel.add(bookButton);
        
        detailsPanel.add(bookingPanel, BorderLayout.CENTER);
        
        add(detailsPanel, BorderLayout.SOUTH);
        
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
        
        // Add action listeners to update price when travelers change
        travelersSpinner.addChangeListener(e -> updateTotalPrice());
        
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookPackage();
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
            descriptionArea.setText(selectedPackage.getDescription());
            updateTotalPrice();
            bookButton.setEnabled(selectedPackage.getAvailableSeats() > 0);
        }
    }
    
    private Date getTravelDate() {
        try {
            String dateStr = travelYear.getSelectedItem() + "-" + 
                             travelMonth.getSelectedItem() + "-" + 
                             travelDay.getSelectedItem();
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    private void updateTotalPrice() {
        if (selectedPackage != null) {
            int travelers = (int) travelersSpinner.getValue();
            double totalPrice = selectedPackage.getPrice() * travelers;
            totalPriceField.setText(String.format("%.2f", totalPrice));
        }
    }
    
    private void bookPackage() {
        if (selectedPackage == null) {
            JOptionPane.showMessageDialog(this, "Please select a package first", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Date travelDate = getTravelDate();
        if (travelDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a valid travel date", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if travel date is in the future
        Date today = new Date();
        if (travelDate.before(today)) {
            JOptionPane.showMessageDialog(this, "Travel date must be in the future", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int travelers = (int) travelersSpinner.getValue();
        if (travelers > selectedPackage.getAvailableSeats()) {
            JOptionPane.showMessageDialog(this, "Not enough seats available", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double totalPrice = selectedPackage.getPrice() * travelers;
        
        // Confirm booking
        int choice = JOptionPane.showConfirmDialog(this, 
                "Confirm booking for " + selectedPackage.getName() + "?\n" +
                "Travel Date: " + dateFormat.format(travelDate) + "\n" +
                "Travelers: " + travelers + "\n" +
                "Total Price: $" + String.format("%.2f", totalPrice),
                "Confirm Booking", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            // Create booking
            Booking booking = new Booking();
            booking.setUserId(user.getUserId());
            booking.setPackageId(selectedPackage.getPackageId());
            booking.setBookingDate(new Date());
            booking.setTravelDate(travelDate);
            booking.setNumTravelers(travelers);
            booking.setTotalPrice(totalPrice);
            booking.setStatus("CONFIRMED");
            
            BookingDAO bookingDAO = new BookingDAO();
            boolean success = bookingDAO.createBooking(booking);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Booking successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPackages();  // Refresh packages to update available seats
                clearBookingForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to book package", "Booking Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearBookingForm() {
        // Reset date selections to current date
        Calendar cal = Calendar.getInstance();
        travelDay.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
        travelMonth.setSelectedItem(cal.get(Calendar.MONTH) + 1);
        travelYear.setSelectedItem(cal.get(Calendar.YEAR));
        
        travelersSpinner.setValue(1);
        totalPriceField.setText("");
        bookButton.setEnabled(false);
        selectedPackage = null;
        packagesTable.clearSelection();
        descriptionArea.setText("");
    }
}
