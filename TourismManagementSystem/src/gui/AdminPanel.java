package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class AdminPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    // Panel names
    private static final String MANAGE_USERS = "MANAGE_USERS";
    private static final String MANAGE_PACKAGES = "MANAGE_PACKAGES";
    private static final String MANAGE_HOTELS = "MANAGE_HOTELS";
    private static final String VIEW_BOOKINGS = "VIEW_BOOKINGS";
    private static final String VIEW_HOTEL_BOOKINGS = "VIEW_HOTEL_BOOKINGS";
    
    public AdminPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new TitledBorder("Admin Panel"));
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton usersBtn = new JButton("Manage Users");
        JButton packagesBtn = new JButton("Manage Packages");
        JButton hotelsBtn = new JButton("Manage Hotels");
        JButton bookingsBtn = new JButton("View Bookings");
        JButton hotelBookingsBtn = new JButton("View Hotel Bookings");
        
        buttonPanel.add(usersBtn);
        buttonPanel.add(packagesBtn);
        buttonPanel.add(hotelsBtn);
        buttonPanel.add(bookingsBtn);
        buttonPanel.add(hotelBookingsBtn);
        
        add(buttonPanel, BorderLayout.NORTH);
        
        // Create card panel for different admin views
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Add panels to card layout
        cardPanel.add(new ManageUsersPanel(), MANAGE_USERS);
        cardPanel.add(new ManagePackagesPanel(), MANAGE_PACKAGES);
        cardPanel.add(new ManageHotelsPanel(), MANAGE_HOTELS);
        cardPanel.add(new ViewBookingsPanel(), VIEW_BOOKINGS);
        cardPanel.add(new ViewHotelBookingsPanel(), VIEW_HOTEL_BOOKINGS);
        
        add(cardPanel, BorderLayout.CENTER);
        
        // Add action listeners
        usersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, MANAGE_USERS);
            }
        });
        
        packagesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, MANAGE_PACKAGES);
            }
        });
        
        hotelsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, MANAGE_HOTELS);
            }
        });
        
        bookingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, VIEW_BOOKINGS);
            }
        });
        
        hotelBookingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, VIEW_HOTEL_BOOKINGS);
            }
        });
        
        // Show users panel by default
        cardLayout.show(cardPanel, MANAGE_USERS);
    }
}
