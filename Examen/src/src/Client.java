package src;

import java.awt.Toolkit;
import javax.swing.JOptionPane;
import org.pushingpixels.substance.api.SubstanceCortex;
import org.pushingpixels.substance.api.skin.BusinessBlackSteelSkin;

public class Client extends javax.swing.JFrame {
    
    /**
     * Constructor of the Client class.
     **/
    public Client() {
        initComponents();
        setIcon();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ipTextField = new javax.swing.JTextField();
        portTextField = new javax.swing.JTextField();
        connectButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(500, 286));
        setPreferredSize(new java.awt.Dimension(500, 286));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ipTextField.setText("localhost");
        ipTextField.setMinimumSize(new java.awt.Dimension(8, 20));
        ipTextField.setPreferredSize(new java.awt.Dimension(8, 20));
        getContentPane().add(ipTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 150, 20));

        portTextField.setText("1234");
        portTextField.setMinimumSize(new java.awt.Dimension(8, 20));
        portTextField.setPreferredSize(new java.awt.Dimension(8, 20));
        getContentPane().add(portTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 140, 150, 20));

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });
        getContentPane().add(connectButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 170, -1, -1));

        jLabel1.setText("Login:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 70, 120, -1));

        jLabel2.setText("Host:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 100, 70, 20));

        jLabel3.setText("Port:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, -1, 20));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/img/login.png"))); // NOI18N
        jLabel4.setText("jLabel4");
        jLabel4.setMaximumSize(new java.awt.Dimension(256, 256));
        jLabel4.setMinimumSize(new java.awt.Dimension(256, 256));
        jLabel4.setPreferredSize(new java.awt.Dimension(256, 256));
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 250));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel5.setText("Welcome!");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 180, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * This function check if the parameters needed to do the connection are 
     * ok. If they are, it creates an instance of the Workbench class, which 
     * will attempt to connect to the server, if the connection is succesful,
     * we check if the status property of the Workbench object is not equal to 
     * 0, then show the Workbench Frame.
     **/
    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        
        String host = ipTextField.getText();
        String port_string = portTextField.getText();
        
        // Check if the parms are not empty
        if(port_string.isEmpty() || host.isEmpty()){
            showErrorMessageDialog("Please fill both IP and Port fields");
            return;
        }
        
        // Try to parse the port string to an integer
        try{
            
            int port = Integer.parseInt(port_string);
            // Pass the arguments to the Workbench constructor
            Workbench wb = new Workbench(host, port);
            //Try to connect to the host
            wb.connect();
            
            // Check the connection status
            if(wb.status == 0){
                showErrorMessageDialog("Unable to connect.");
                return;
            }
            
            // Read the databases from the server and show them into a JTree
            wb.setDatabasesList();
            
            // Show the Workbench Frame and hide the Login
            this.setVisible(false);
            wb.setLocationRelativeTo(null);
            wb.setVisible(true);
            
        }catch(Exception e){
            showErrorMessageDialog(e.getMessage());
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    /**
     * This function is used to show an error message using the JOptionPane
     * class.
     **/
    public void showErrorMessageDialog(String error){
        JOptionPane.showMessageDialog(null,error, "Error",JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                // Set the Substance Look and Feel
                SubstanceCortex.GlobalScope.setSkin(new BusinessBlackSteelSkin());
            } catch (Exception e) {
                System.out.println("Substance Graphite failed to initialize");
            }
            Client client = new Client();
            client.setLocationRelativeTo(null);
            client.setVisible(true);
        });
    }
    
    /**
     * This function sets the Workbench icon to the Login Frame.
     **/
     private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectButton;
    private javax.swing.JTextField ipTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField portTextField;
    // End of variables declaration//GEN-END:variables
}
