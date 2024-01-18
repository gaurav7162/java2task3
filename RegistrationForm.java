import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JDialog {
    private JPanel registerPanel;
    private JTextField tfPhone;
    private JTextField tfEmail;
    private JTextField tfName;
    private JTextField tfAdd;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirm;
    private JButton btnRegister;
    private JButton btnCancel;

    public RegistrationForm(JFrame parent){
        super(parent);
        setTitle("Create a New Account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAdd.getText();
        String password = String.copyValueOf(pfPassword.getPassword());
        String confirmPassword = String.copyValueOf(pfConfirm.getPassword());

        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields","Try Again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match","Try Again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        user = addUserToDatabase(name,email,phone,address,password);
        if(user!=null){
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Faild to register new user", "Try Again", JOptionPane.ERROR_MESSAGE);
        }

        }


    public User user;

    private User addUserToDatabase(String name, String email, String phone, String address, String password) {
        User user=null;

        final String DB_URL= "jdbc:mysql:http://localhost/phpmyadmin/index.php?route=/table/structure&db=mystore&table=users";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn= DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            //connected to database successfully

            Statement smt= conn.createStatement();
            String sql= "INSERT INTO users(name,email,phone,address,password)"+ "VALUES(? , ? , ? , ? , ?)";
            PreparedStatement preparedStatement= conn.prepareStatement(sql);

            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,phone);
            preparedStatement.setString(4,address);
            preparedStatement.setString(5,password);

           //insert row into the table
            int addedRows=preparedStatement.executeUpdate();
            if (addedRows > 0){
                user =new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;

            }
            smt.close();
            conn.close();


        }
        catch (Exception e){
            e.printStackTrace();

        }


        return user;
    }

    public static void main(String[] args) {
        RegistrationForm myform=new RegistrationForm(null);
        User user = myform.user;

        if (user != null){
            System.out.println("Succesful registration of : " + user.name);
        }
        else{
            System.out.println("Registration Cancled");
        }
    }
}
