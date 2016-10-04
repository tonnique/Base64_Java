import org.omg.CORBA.Environment;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Antony on 22.07.2016.
 */
public class Frame extends JFrame {
    //private static final int WIDTH = 640;
    //private static final int HEIGHT = 480;

    private Listener theListener;
    private JButton choose = new JButton();
    private JButton encode = new JButton();
    private JButton decode = new JButton();
    private JLabel status;

    public Frame() {
        super("MIME Decoder");

        theListener = new Listener(this);

        setControls();
        pack();

        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
        setLocation(x, y);
    }

    private void setControls() {

       addButton(choose, "Выбрать файл", "choose", theListener);
       addButton(encode, "MIME encode", "encode", theListener);
       addButton(decode, "MIME decode", "decode", theListener);

       Container c = getContentPane();
       JPanel ButtonPanel = new JPanel();
       JPanel statusPanel = new JPanel(new BorderLayout());

       ButtonPanel.add(choose);
       ButtonPanel.add(encode);
       ButtonPanel.add(decode);

       c.add(ButtonPanel, BorderLayout.NORTH);

        status = new JLabel();
        Border border = status.getBorder();
        Border margins = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        status.setBorder(new CompoundBorder(border, margins));
        status.setText("No File Selected");

       statusPanel.add(status, BorderLayout.WEST);
       c.add(statusPanel, BorderLayout.SOUTH);
    }

    /**
     * Метод настраивает кнопку
     * @param button - кнопка
     * @param title - надпись на кнопке
     * @param command - ActionCommand
     * @param listener - Обработчик события
     */
    private void addButton(JButton button, String title, String command, ActionListener listener) {
        button.setText(title);
        button.setActionCommand(command);
        button.addActionListener(listener);
    }

    protected JLabel getStatus() {
        return status;
    }

    protected void setStatus(String txt) {
        status.setText(txt);
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Frame frame = new Frame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}