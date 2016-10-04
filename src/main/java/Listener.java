import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Listener implements ActionListener {

    Frame frame;
    JFileChooser chooser;
    File selectedFile;


    public Listener(Frame f) {
        frame = f;
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton srcButton = (JButton) e.getSource();

        switch (srcButton.getActionCommand()) {
            case "choose":
               chooseFile();
                break;
            case "encode":

                if (selectedFile != null) {

                    try {

                        byte[] bytes = FileUtils.readSelectedFile(selectedFile);
                        FileUtils.writeEncoded(selectedFile,
                            Base64.StartEncode(bytes));
                        frame.setStatus(selectedFile + " is encoded");
                    }
                    catch(IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                break;

            case "decode":

                if (selectedFile != null) {

                    try {
                        byte[] bytes = Base64.StartDecode(FileUtils.readSelectedFile(selectedFile));
                        FileUtils.writeDecoded(selectedFile, bytes);
                    }
                    catch (IOException ex) {
                        ex.getMessage();
                    }
                }

                break;
        }
    }

    private void chooseFile() {
        int result = chooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            chooser.setCurrentDirectory(selectedFile);
            frame.setStatus(selectedFile.getPath());
        }
        else {
            frame.setStatus("No File Selected");
        }
    }
}
