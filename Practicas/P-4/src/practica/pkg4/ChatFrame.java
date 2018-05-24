
package practica.pkg4;

import com.sun.glass.events.KeyEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChatFrame extends JFrame {
    
    public static Scanner in = new Scanner(System.in);
    public static ArrayList<String> users = new ArrayList<String>();
    public static int pto = 1234;
    public static InetAddress grupo = null;
    public static String dir = null;
    public static String nombre = null;
    public MulticastSocket ms = null;
    public static int ch = 0;
    public Emojis emojis;  
    public ArrayList<String> emojisPath = new ArrayList<>();
        
    public ChatFrame(String dir) throws IOException {
        initComponents();
        setLocationRelativeTo(null);
        initializeEmojisPaths();
        emojis = new Emojis(this.botonEmoji.getLocation());
        areaMensajes.setContentType("text/html");
        areaMensajes.setText("<!DOCTYPE html><html><body></body></html>");
        this.dir=dir;
        try{
            ms = new MulticastSocket(pto);
            ms.setReuseAddress(true);
            ms.setTimeToLive(255);
            try{
                grupo = InetAddress.getByName(dir);
            }catch(UnknownHostException u){
                JOptionPane.showMessageDialog(null,"Dirección no valida!");
                System.exit(1);
            }
            ms.joinGroup(grupo);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        comboUsers.removeAllItems();
        emojis.table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = emojis.table.rowAtPoint(evt.getPoint());
                int col = emojis.table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    String tag = emojis.getTag(row, col);
                    mensaje.append("<"+tag+">");
                    emojis.hide();
                }
                else{
                    emojis.hide();
                }
            }
        });
        this.addComponentListener(new ComponentAdapter(){
            public void componentMoved(ComponentEvent e){
                emojis.changeLocation(getLocation(),botonEmoji.getLocation());
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nombreUsuario = new javax.swing.JTextField();
        botonAcceder = new javax.swing.JButton();
        comboUsers = new javax.swing.JComboBox<>();
        botonEmoji = new javax.swing.JButton();
        botonEnviar = new javax.swing.JButton();
        checkPrivado = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        mensaje = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        areaMensajes = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat Multicast");
        setFont(new java.awt.Font("Calibri Light", 0, 10)); // NOI18N
        setResizable(false);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        nombreUsuario.setFont(new java.awt.Font("Calibri Light", 0, 16)); // NOI18N
        nombreUsuario.setToolTipText("Nombre de Usuario");
        nombreUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nombreUsuarioKeyPressed(evt);
            }
        });

        botonAcceder.setFont(new java.awt.Font("Calibri Light", 0, 16)); // NOI18N
        botonAcceder.setText("Acceder");
        botonAcceder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAccederActionPerformed(evt);
            }
        });

        comboUsers.setFont(new java.awt.Font("Calibri Light", 0, 16)); // NOI18N
        comboUsers.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboUsers.setEnabled(false);

        botonEmoji.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica/pkg4/emojis.png"))); // NOI18N
        botonEmoji.setEnabled(false);
        botonEmoji.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEmojiActionPerformed(evt);
            }
        });
        botonEmoji.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                botonEmojiPropertyChange(evt);
            }
        });

        botonEnviar.setFont(new java.awt.Font("Calibri Light", 0, 16)); // NOI18N
        botonEnviar.setText("Enviar");
        botonEnviar.setEnabled(false);
        botonEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEnviarActionPerformed(evt);
            }
        });

        checkPrivado.setFont(new java.awt.Font("Calibri Light", 0, 16)); // NOI18N
        checkPrivado.setText("Privado");
        checkPrivado.setEnabled(false);
        checkPrivado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkPrivadoActionPerformed(evt);
            }
        });

        mensaje.setColumns(20);
        mensaje.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        mensaje.setRows(5);
        mensaje.setEnabled(false);
        mensaje.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mensajeKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(mensaje);

        areaMensajes.setEditable(false);
        areaMensajes.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 204, 204)));
        jScrollPane1.setViewportView(areaMensajes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(nombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(botonAcceder)
                        .addGap(67, 67, 67)
                        .addComponent(comboUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkPrivado))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(botonEmoji, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(botonEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(16, 16, 16)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(checkPrivado)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(comboUsers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addComponent(botonAcceder, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(nombreUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botonEmoji, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonEnviar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonAccederActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAccederActionPerformed
        funcionPollona();
    }//GEN-LAST:event_botonAccederActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cerrar();
    }//GEN-LAST:event_formWindowClosing

    private void botonEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEnviarActionPerformed
        funcionPollona2();
    }//GEN-LAST:event_botonEnviarActionPerformed

    private void checkPrivadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkPrivadoActionPerformed
        if(ch == 0)
            ch = 1;
        else
            ch = 0;
    }//GEN-LAST:event_checkPrivadoActionPerformed

    private void nombreUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreUsuarioKeyPressed
       if(evt.getKeyCode() == KeyEvent.VK_ENTER){
           funcionPollona();
       }
    }//GEN-LAST:event_nombreUsuarioKeyPressed

    private void mensajeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mensajeKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
           funcionPollona2();
       }
    }//GEN-LAST:event_mensajeKeyPressed

    private void botonEmojiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEmojiActionPerformed
        emojis.show();
        System.out.println("Position: "+this.getLocation().toString());
        System.out.println("Button Position: "+this.botonEmoji.getLocation().toString());
        
    }//GEN-LAST:event_botonEmojiActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        emojis.hide();
    }//GEN-LAST:event_formFocusGained

    private void botonEmojiPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_botonEmojiPropertyChange
        
    }//GEN-LAST:event_botonEmojiPropertyChange

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        
    }//GEN-LAST:event_formComponentMoved
    
    public void funcionPollona2(){
        cargarUsuarios(0);
      
        String msj = mensaje.getText();
        String to = (String) comboUsers.getSelectedItem();
        int tipo = ch;
        Enviar enviar;
        
        if(tipo == 0)
            enviar = new Enviar(pto,grupo,ms,nombre,users,msj,tipo);
        else
            enviar = new Enviar(pto,grupo,ms,nombre,users,msj,tipo,to);
        
        enviar.start();
        try {
            enviar.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        mensaje.setText("");
        comboUsers.removeAllItems();
        for(String out : users){
            if(!out.equals(nombre))
                comboUsers.addItem(out);
        }
    }
    
    public void funcionPollona(){
        if(!(nombre = nombreUsuario.getText()).equalsIgnoreCase("")){
            habilitar();
            cargarUsuarios(1);
            Recibir recibir = new Recibir(ms,nombre,users,areaMensajes,emojisPath);
            recibir.start();
        }
        else{
            JOptionPane.showMessageDialog(null,"Ingresa nombre de usuario valido!");
        }
    }
    
    public void habilitar(){
        nombreUsuario.setEnabled(false);
        botonAcceder.setEnabled(false);
        areaMensajes.setEnabled(true);
        botonEnviar.setEnabled(true);
        botonEmoji.setEnabled(true);
        checkPrivado.setEnabled(true);
        comboUsers.setEnabled(true);
        mensaje.setEnabled(true);
    }
    
    public void cerrar(){
        Object [] opciones ={"Aceptar","Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(rootPane,"En realidad desea realizar cerrar la aplicacion","Mensaje de Confirmacion",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,null,opciones,"Aceptar");
        if (eleccion == JOptionPane.YES_OPTION){
            eliminarUsuario();
            System.exit(0);
        }
    }  
    
    public void eliminarUsuario(){
        
        int index = users.lastIndexOf(nombre);
        users.remove(index);
        try{
            File archivo = new File(dir+".txt");
            
            FileWriter fw = new FileWriter (archivo);
            PrintWriter pw = new PrintWriter(fw);
            for(String out : users){
                System.out.println("->"+out);
                pw.println(out);
            }
            fw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cargarUsuarios(int i){
        try{
            users.clear();
            File archivo = new File(dir+".txt");
            
            if(!archivo.exists()){
                BufferedWriter bw;
                bw = new BufferedWriter(new FileWriter(archivo));
            }
            
            FileReader fr = new FileReader (archivo);
            BufferedReader br = new BufferedReader(fr);

            String u = null;
            while((u=br.readLine())!=null)
                users.add(u);
            if(i == 1)
                users.add(nombre);
            fr.close();

            FileWriter fw = new FileWriter (archivo);
            PrintWriter pw = new PrintWriter(fw);
            for(String out : users){
                System.out.println("->"+out);
                pw.println(out);
            }
            fw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void initializeEmojisPaths() throws IOException{
        for(int i = 1; i <=60; i++){
            emojisPath.add("<img width='75' height='75' src='file:/"+new File("./src/practica/pkg4/emojis/"+i+".png").getCanonicalPath().replace(" ","%20").replace("\\","/")+"'>");
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane areaMensajes;
    private javax.swing.JButton botonAcceder;
    private javax.swing.JButton botonEmoji;
    private javax.swing.JButton botonEnviar;
    private javax.swing.JCheckBox checkPrivado;
    private javax.swing.JComboBox<String> comboUsers;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea mensaje;
    private javax.swing.JTextField nombreUsuario;
    // End of variables declaration//GEN-END:variables

}
