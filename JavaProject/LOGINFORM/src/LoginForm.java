import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*; // Importation de JFrame

import com.formdev.flatlaf.FlatDarculaLaf;

public class LoginForm extends JFrame { // Déclaration de la classe publique LoginForm et extends JFrame

    final private Font mainFont = new Font("Sergoe print", Font.BOLD, 18);
    JTextField tfEmail;
    JPasswordField pfPassword;


    /**
     * 
     */
    public void initialize() { // Initialisation


        /************************  FORM PANEL  ************************/

        JLabel lbLoginForm = new JLabel("Login Form", SwingConstants.CENTER);
        lbLoginForm.setFont(mainFont);
        
        JLabel lbEmail = new JLabel("Email");
        lbEmail.setFont(mainFont);

        tfEmail = new JTextField();
        tfEmail.setFont(mainFont);

        JLabel lbPassword = new JLabel("Password");
        lbPassword.setFont(mainFont);

        pfPassword = new JPasswordField();
        pfPassword.setFont(mainFont);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.add(lbLoginForm);
        formPanel.add(lbEmail);
        formPanel.add(tfEmail);
        formPanel.add(lbPassword);
        formPanel.add(pfPassword);


        /***********************  BUTTONS PANEL  **************************/
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(mainFont);
        btnLogin.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                User user = getAuthenticatedUser(email, password);

                if (user != null) {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.initialize(user);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                     "Email or Password is invalid.", 
                     "Try again.", 
                     JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(mainFont);
        btnCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-Generated method stub
                dispose();
            }

        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonsPanel.add(btnLogin);
        buttonsPanel.add(btnCancel);


        /*******************  INITIALIZE THE FRAME  ***********************/
        add(formPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);




        setTitle("Login Form"); // Title of the window
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // Close the window by clicking on the cross
        setSize(400, 500); // Set the size of the window
        setMinimumSize(new Dimension(350, 450)); // Set the minimun size of the screen
        //setResizable(false);
        setLocationRelativeTo(null); // Allows to put the window on the scren
        setVisible(true); // Make the window visible

    }
    

    private User getAuthenticatedUser(String email, String password) {
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // Connected to database successfully...

            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
            }

            preparedStatement.close();
            conn.close();

        }catch(Exception e){
            System.out.println("Database connexion failed!");
        }


        return user;
    }

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel( new FlatDarculaLaf() );

        } catch( Exception ex ) {

            System.err.println( "Failed to initialize LaF" );
        }  

        LoginForm loginForm = new LoginForm();
        loginForm.initialize();
    }

}