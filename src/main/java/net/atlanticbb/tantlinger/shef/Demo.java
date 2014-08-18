
package net.atlanticbb.tantlinger.shef;

import java.io.IOException;
import java.io.InputStream;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.atlanticbb.tantlinger.io.IOUtils;

/**
 *
 * @author Bob Tantlinger
 */
public class Demo {

    public Demo() {
        


        HTMLEditorPane editor = new HTMLEditorPane();
        InputStream in = Demo.class.getResourceAsStream("/net/atlanticbb/tantlinger/shef/htmlsnip.txt");
        try{
            editor.setText(IOUtils.read(in));
        }catch(IOException ex) {
            ex.printStackTrace();
        } finally {
            IOUtils.close(in);
        }

        JFrame frame = new JFrame();
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(editor.getEditMenu());
        menuBar.add(editor.getFormatMenu());
        menuBar.add(editor.getInsertMenu());
        frame.setJMenuBar(menuBar);

        frame.setTitle("HTML Editor Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.getContentPane().add(editor);
        frame.setVisible(true);
        
    }

    public static void main(String args[]) {

        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ex){}


        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
               new Demo();
            }
        });
    }

}
