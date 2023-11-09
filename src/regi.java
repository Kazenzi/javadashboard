import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class regi extends JDialog {
    private JTextField tfname;
    private JTextField tfemail;
    private JTextField tfphone;
    private JTextField tfaddress;
    private JPasswordField tfpassword;
    private JPasswordField tfconfpassw;
    private JButton btnreg;
    private JButton btncancel;
    private JPanel Registerpanel;

   public regi(JFrame parent){
       super(parent);
       setTitle("Create A New Account");
       setContentPane(Registerpanel);
       setMinimumSize(new Dimension(450,474));
       setModal(true);
       setLocationRelativeTo(parent);

       btnreg.addActionListener(new ActionListener() {
           public void setUser(User user) {
               this.user = user;
           }

           public User getUser() {
               return user;
           }

           @Override
           public void actionPerformed(ActionEvent e) {
               registeruser();
           }

           private void registeruser() {
               String name=tfname.getText();
               String email=tfemail.getText();
               String phone=tfphone.getText();
               String address=tfaddress.getText();
               String password=String.valueOf(tfpassword.getPassword());
               String Confirmpassword=String.valueOf(tfconfpassw.getPassword());

               if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()
               ){
                   JOptionPane.showMessageDialog(null,
                           "Enter all Fields",
                           "Try Again",
                           JOptionPane.ERROR_MESSAGE);
                   return;

               }
               if (!tfpassword.equals(tfconfpassw)){
                   JOptionPane.showMessageDialog(null,"Password Mismatch",
                           "Try Again",JOptionPane.ERROR_MESSAGE);
                   return;
               }
              user= addUserToDb(name,email,phone,address,password);
               if (user !=null){
                   dispose();
               }
           }
           private user user;
           private user addUserToDb(String name, String email, String phone, String address, String password){
               User user = null;
               final String DB_URL = "jdbc:mysql://localhost/MyStore?serverTimezone=UTC";
               final String USERNAME = "root";
               final String PASSWORD = "";

               try{
                   Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                   // Connected to database successfully...

                   Statement stmt = conn.createStatement();
                   String sql = "INSERT INTO users (name, email, phone, address, password) " +
                           "VALUES (?, ?, ?, ?, ?)";
                   PreparedStatement preparedStatement = conn.prepareStatement(sql);
                   preparedStatement.setString(1, name);
                   preparedStatement.setString(2, email);
                   preparedStatement.setString(3, phone);
                   preparedStatement.setString(4, address);
                   preparedStatement.setString(5, password);

                   //Insert row into the table
                   int addedRows = ((PreparedStatement) preparedStatement).executeUpdate();
                   if (addedRows > 0) {
                       user = new User();
                       user.name = name;
                       user.email = email;
                       user.phone = phone;
                       user.address = address;
                       user.password = password;
                   }

                   stmt.close();
                   conn.close();
               }catch(Exception e){
                   e.printStackTrace();
               }

               return user;
           }

           public static void main(String[] args) {
               RegistrationForm myForm = new RegistrationForm(null);
               User user = myForm.user;
               if (user != null) {
                   System.out.println("Successful registration of: " + user.name);
               }
               else {
                   System.out.println("Registration canceled");
               }
           }
       }