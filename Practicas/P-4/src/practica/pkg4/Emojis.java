package practica.pkg4;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
  
public class Emojis
{
    final int FIT  = 0;
    JPanel jp = new JPanel(new GridLayout());
    JTable table;
    JScrollPane jsp;
    JFrame f;
    String[] fileNames = {
            "1.png","2.png","3.png","4.png","5.png","6.png","7.png","8.png",
            "9.png","10.png","11.png","12.png","13.png","14.png","15.png",
            "16.png","17.png","18.png","19.png","20.png","21.png","22.png",
            "23.png","24.png","25.png","26.png","27.png","28.png","29.png",
            "30.png","31.png","32.png","33.png","34.png","35.png","36.png",
            "37.png","38.png","39.png","40.png","41.png","42.png","43.png",
            "44.png","45.png","46.png","47.png","48.png","49.png","50.png",
            "51.png","52.png","53.png","54.png","55.png","56.png","57.png",
            "58.png","59.png","60.png"
        };
    String tags[][] = {{"alien","angel","anguished","crying","sad1","devil"},
        {"dizzy","drooling","clown","cowboy","happy1","sad2"},
        {"smile1","smirk","sunglasses","thinking","unamused","sad3"},
        {"worried","surprised","nomouth","expless","fearful","flushed"},
        {"frowning","ghost","grinmacing","happycat","happy2","hearteyes"},
        {"hugging","hungry","kiss1","kiss2","kiss3","maddevil"},
        {"money","neutral","tears","omg","liar","pskull"},
        {"poison","poop","pumpkin","relieved","robot","rolleye"},
        {"shy","sick1","sick2","sick3","skull","sleep"},
        {"smile2","smile3","catsmile","smile4","smile5","smile6"}};
    
    public void changeLocation(Point framePos, Point buttonPos){
        Point newPos = new Point(framePos.x+buttonPos.x,framePos.y+buttonPos.y);
        this.f.setLocation(newPos);
    }
    
    public Emojis(Point emojis)
    {
        f = new JFrame();
        f.setUndecorated(true);
        BufferedImage[][] images = loadImages();
        BufferedImage[][] scaled = getScaledImages(images);
        this.table = createTable(scaled);
        this.table.setRowHeight(scaled[0][0].getHeight());
        DefaultTableCellRenderer renderer =
            (DefaultTableCellRenderer)this.table.getDefaultRenderer(String.class);
        renderer.setHorizontalAlignment(JLabel.CENTER);
        this.jp.add(table);
        this.jsp = new JScrollPane(this.table);
        f.getContentPane().add(jp);
        f.getContentPane().add(new JScrollPane(table));
        f.setSize(400,340);
        f.setLocation(emojis);
        
    }
    
    public void show(){
        f.setVisible(true);
    }
    
    public void hide(){
        f.setVisible(false);
    }
  
    public BufferedImage[][] loadImages()
    {
        BufferedImage[][] images = new BufferedImage[10][6];
        int k = 0;
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 6; j++,k++){
                try
                {
                    URL url = getClass().getResource("emojis/" + fileNames[k]);
                    images[i][j] = ImageIO.read(url);
                }
                catch(MalformedURLException mue)
                {
                    System.err.println("url: " + mue.getMessage());
                }
                catch(IOException ioe)
                {
                    System.err.println("read: " + ioe.getMessage());
                }
            }
        }
            
         return images;
    }
  
    public BufferedImage[][] getScaledImages(BufferedImage[][] in){
        
        int WIDTH = 40;
        int HEIGHT = 40;
        BufferedImage[][] out = new BufferedImage[10][6];
        
        for(int i = 0; i < 10; i++){
            
            for (int j = 0; j < 6; j++) {
                
                out[i][j] = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = out[i][j].createGraphics();
                g2.setColor(Color.white);
                g2.fillRect(0, 0, WIDTH, HEIGHT);
                double width  = in[i][j].getWidth();
                double height = in[i][j].getHeight();
                double xScale = WIDTH  / width;
                double yScale = HEIGHT / height;
                double scale = Math.min(xScale, yScale);  // scale to fit
                double x = (WIDTH - width * scale)/2;
                double y = (HEIGHT - height * scale)/2;
                AffineTransform at = AffineTransform.getTranslateInstance(x, y);
                at.scale(scale, scale);
                g2.drawRenderedImage(in[i][j], at);
                g2.dispose();
                
            }
            
        }
        
        return out;
    }
  
    public JTable createTable(BufferedImage[][] images){
        
        Object[] headers = {"","","","","",""};
        
        DefaultTableModel model = new DefaultTableModel(images,headers){
            @Override
            public Class getColumnClass(int col){
                return images[col][0].getClass();
            }
        };
        
        JTable table = new JTable(model);
        table.setTableHeader(null);
        table.setCellSelectionEnabled(true);
        table.setDefaultRenderer(BufferedImage.class, new ImageRenderer());
        return table;
        
    }
    public void tableActionPerformed(java.awt.event.ActionEvent evt) {                                           
        f.setVisible(false);
    }  
    
    public String getTag(int x, int y){
        return tags[x][y];
    }
}
  
class ImageRenderer extends DefaultTableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row, int column){
        super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
        setIcon(new ImageIcon((BufferedImage)value));
        setHorizontalAlignment(JLabel.CENTER);
        setText("");
        return this;
    }
}