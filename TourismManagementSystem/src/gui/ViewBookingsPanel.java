package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.BookingDAO;
import dao.UserDAO;
import models.Booking;
import models.User;

public class ViewBookingsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private UserDAO userDAO = new UserDAO(); // Add this to look up usernames
    
    public ViewBookingsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create table model
        String[] columnNames = {"ID", "User", "Package", "Booking Date", "Travel Date", "Travelers", "Total Price ($)", "Status", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bookingsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        buttonsPanel.add(refreshButton);
        add(buttonsPanel, BorderLayout.SOUTH);
        
        // Add listeners
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBookings();
            }
        });
        
        // Load bookings
        loadBookings();
    }
    
    private void loadBookings() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Load bookings from database
        BookingDAO bookingDAO = new BookingDAO();
        List<Booking> bookings = bookingDAO.getAllBookings();
        
        for (Booking booking : bookings) {
            // Get username from userId
            User user = userDAO.getUserById(booking.getUserId());
            String username = (user != null) ? user.getUsername() : "Unknown";
            
            // Create a button panel for actions
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton cancelButton = new JButton("Cancel");
            
            if (booking.getStatus().equals("CANCELLED")) {
                cancelButton.setEnabled(false);
            }
            
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cancelBooking(booking.getBookingId());
                }
            });
            
            actionPanel.add(cancelButton);
            
            Object[] row = {
                booking.getBookingId(),
                username, // Use the username we looked up
                booking.getPackageId(),
                dateFormat.format(booking.getBookingDate()),
                dateFormat.format(booking.getTravelDate()),
                booking.getNumTravelers(),
                String.format("%.2f", booking.getTotalPrice()),
                booking.getStatus(),
                "Cancel"  // This will be replaced with the button
            };
            tableModel.addRow(row);
            
            // Set the button as the renderer for the last column
            bookingsTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
            bookingsTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JButton(), this, booking.getBookingId()));
        }
    }
    
    public void cancelBooking(int bookingId) {
        int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to cancel this booking?", 
                "Confirm Cancellation", 
                JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            BookingDAO bookingDAO = new BookingDAO();
            boolean success = bookingDAO.updateBookingStatus(bookingId, "CANCELLED");
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBookings();  // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel booking", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
