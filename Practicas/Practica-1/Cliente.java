import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.SwingConstants;

public class Cliente extends JFrame {
	
	static DataInputStream dis1;
    static DataOutputStream dos;
    static String[] nombres;
    static long[] tamanios;
    static String[] paths;
    static JFileChooser jf;
	static File[] f;
	static Scanner entrada=new Scanner(System.in);
	private JPanel contentPane;
	static int puerto = 7777;
	static String dir = "127.0.0.1";
	static Socket cl;
	static Cliente frame;
	static JFrame carga;
	static JProgressBar barraDeProgreso;
	static Container contenido;
    static Border borde;
	static JOptionPane jop;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					cl = new Socket(dir,puerto);
					dis1 = new DataInputStream(cl.getInputStream());
					dos = new DataOutputStream(cl.getOutputStream());
					frame = new Cliente();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	static void recibirArchivos(){
        try{
            String nombre = dis1.readUTF();
            long tam = dis1.readLong();
            System.out.println("Recibiendo el archivo: "+nombre);
            long recibidos = 0;
            int n = 0, porcentaje = 0;
            DataOutputStream dos2 = new DataOutputStream(new FileOutputStream(nombre));

            while(recibidos < tam){

                byte[] b = new byte[1500];
                n = dis1.read(b);
                recibidos += n;
                dos2.write(b,0,n);
                dos2.flush();
                porcentaje = (int)((recibidos*100)/tam);
                System.out.print("\rRecibido el "+porcentaje+"%");

            }

            System.out.println();

            dos2.close();
            cl.close();
        
        }catch(Exception e){
            e.printStackTrace();
        }

    }

	static void transmitirArchivo(long tam, String path, String nombre){

		try{
			dos.writeUTF(nombre);
			dos.flush();
			dos.writeLong(tam);
			dos.flush();
			System.out.println("Transmitiendo el archivo: "+nombre);
			int n = 0, porcentaje = 0;
			long enviados = 0;

			DataInputStream dis2 = new DataInputStream(new FileInputStream(path));
			byte[] b = new byte[1500];

			while(enviados < tam){

				n = dis2.read(b);
				enviados += n;
				dos.write(b,0,n);
				dos.flush();
				porcentaje = (int)((enviados*100)/tam);
				System.out.print("\rTransmitiendo el "+porcentaje+"% del archivo");

			}
			Thread.currentThread().sleep(1000);
			System.out.println();
			dis2.close();

		}catch(Exception e){
			e.printStackTrace();
		}

		return;

	}
	
	/**
	 * Create the frame.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public Cliente() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnSubir = new JButton("Subir");
		btnSubir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					dos.writeInt(1);
					dos.flush();
					jf = new JFileChooser();
					jf.requestFocus();
					jf.setMultiSelectionEnabled(true);
					int r = jf.showOpenDialog(null);
	
					if(r == JFileChooser.APPROVE_OPTION){
						
						f = jf.getSelectedFiles();
						dos.writeInt(f.length);
						dos.flush();
						int i = 0;
						while(i < f.length){
							transmitirArchivo(f[i].length(),f[i].getAbsolutePath(),f[i].getName());
							i++;
						}
					}

					jop = new JOptionPane();
					jop.showMessageDialog(null, "Archivos subidos exitosamente :)");
					dos.close();
					cl.close();
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnSubir.setBounds(20, 306, 161, 66);
		contentPane.add(btnSubir);
		
		JButton btnDescargar = new JButton("Descargar");
		btnDescargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					dos.writeInt(2);
					dos.flush();
					int status=dis1.readInt();
					if(status == -1){
						System.out.println("No hay archivos para descargar");
						return;
					}
					int noArchivos = dis1.read();
					String nombre;
					String[] nombresArray = new String[noArchivos];
					for (int i=0;i<noArchivos;i++){
						nombre=dis1.readUTF();
						System.out.println((i+1)+".- "+nombre);
						nombresArray[i] = nombre;
					}
					JComboBox archivos = new JComboBox(nombresArray);
					Dimension max =new Dimension(500,500);
					jop = new JOptionPane();
					jop.setMaximumSize(max);
					jop.showConfirmDialog(null, archivos, "¿Que archivo desea descargar?", jop.PLAIN_MESSAGE);
					int n = archivos.getSelectedIndex();
					dos.writeInt(n);
					dos.flush();
					recibirArchivos();
					jop.showMessageDialog(null, "Archivo Descargado exitosamente :)");
					dos.close();
					cl.close();
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnDescargar.setBounds(244, 306, 169, 66);
		contentPane.add(btnDescargar);
		
		JLabel label = new JLabel("");
		label.setBounds(20, 20, 200, 200);
		ImageIcon ii = new ImageIcon("img\\cloud.png");
		Image img = ii.getImage();
		Image nimg = img.getScaledInstance(label.getHeight(), label.getWidth(), Image.SCALE_SMOOTH);
		ImageIcon escalada = new ImageIcon(nimg);
		label.setIcon(escalada);
		contentPane.add(label);
		
		JLabel lblDropbox = new JLabel("DROPBOX");
		lblDropbox.setHorizontalAlignment(SwingConstants.CENTER);
		lblDropbox.setFont(new Font("Candara", Font.PLAIN, 30));
		lblDropbox.setBounds(20, 251, 393, 44);
		contentPane.add(lblDropbox);
	}
}
