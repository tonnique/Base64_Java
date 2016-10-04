import java.io.*;
import java.util.Scanner;

public class FileUtils {

    // расширение для закодированного файла
    private static final String encodeFileExtention = ".base64";
    private static final String decodeFileExtention = "_base64.dec";
    private static final int cols = 76; // количество символов в строке, при записи файла,
        // после которых ставить перенос на новую строку, для удобства чтения


    public static void writeEncoded(File filename, String strOut) throws FileNotFoundException {

        filename = getFileNameForEncoded(filename);

        try (PrintWriter out = new PrintWriter(filename))  {

            int i = 0;
            for (char c : strOut.toCharArray()) {
                i++;
                out.print(c);
                if (i % cols == 0) out.println();
            }
        }
    }

    public static void writeDecoded(File filename, byte[] bytes) throws IOException {

        filename = getFileNameForDecoded(filename);

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(filename)))  {
            out.write(bytes);
            out.flush();
        }
    }

    public static byte[] readSelectedFile(File fileName) throws IOException {

        byte[] byteArr;

        String ReadIn = "";
        try (Scanner in = new Scanner(new FileInputStream(fileName))) {

            while (in.hasNext()) {
               ReadIn += in.nextLine();
            }
        }

        byteArr = ReadIn.getBytes();

        /*
        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName))) {
            byteArr = new byte[in.available()];
            in.readFully(byteArr);
        } */

        return  byteArr;

    }


    private static String removeUTF8BOM(String s) {
        if (s.startsWith("\uFEFF")) {
            s = s.substring(1);
        }
        return s;
    }


    /* В этом методе заменяется расширение выбранного файла, на заданное в константе
    класса FileUtils.encodeFileExtention (.base64) */
    private static File getFileNameForEncoded(File file) {
        // здесь вычисляем путь к файлу (диск:\путь до файла\
        String path = file.toString().substring(0, file.toString().lastIndexOf("\\") + 1);
        // здесь вычисляем имя файла (без расширения)
        String fileName = file.toString().substring(file.toString().lastIndexOf("\\") + 1,
                file.toString().indexOf("."));
        // формируем новое имя файла, изменив расширение
        File fileToWrite = new File(path + fileName + encodeFileExtention);


        return fileToWrite;
    }

    /* В этом методе заменяется расширение выбранного файла, на заданное в константе
класса FileUtils.encodeFileExtention (*_base64.dec) */
    private static File getFileNameForDecoded(File file) {
        // здесь вычисляем путь к файлу (диск:\путь до файла\
        String path = file.toString().substring(0, file.toString().lastIndexOf("\\") + 1);
        // здесь вычисляем имя файла (без расширения)
        String fileName = file.toString().substring(file.toString().lastIndexOf("\\") + 1,
                file.toString().indexOf("."));
        // формируем новое имя файла, изменив расширение
        File fileToWrite = new File(path + fileName + decodeFileExtention);


        return fileToWrite;
    }

}
