import java.util.ArrayList;

public class Base64 {

    private static int buffer; //здесь я храню символы и разбиваю блоки по 6 бит
    private static int BufferBitCounter = 0; // счетчик бит в буффере
    private static int numOfBits = 0; // число бит в строке которая передается на кодирование.
    public static ArrayList<Byte> ListOfBytes = new ArrayList<>();

    // Это алфавит Base64;
    private final static String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";


    public static String StartEncode(byte[] inputArray) {
        String result = "";

        // число бит, которое занимает кодируемое сообщение
        numOfBits = inputArray.length * 8;

        // число бит должно быть кратным 6 (так как сообщение разбиваем на 6 бит)
        if (numOfBits % 6 == 0)
            result += Base64Encode(inputArray);
        else if (numOfBits % 6 == 2) {
            result += Base64Encode(inputArray) + "==";
        }

        else if (numOfBits % 6 == 4) {
            result += Base64Encode(inputArray) + "=";
        }

        return result;
    }

    private static String Base64Encode(byte[] byteArr) {

        buffer = 0;
        BufferBitCounter = 0;

        String result = ""; // здесь будем хранить результат
        int temp6Bit = 0; // Временная переменная - в ней сохраняем 6-бит из буфера, путем смещения вправо

        for (int i = 0; i < byteArr.length; ++i) {
            //for (byte symbol : byteArr) {
            byte symbol = byteArr[i];

          /* т.к.  в Java перменная byte знаковая и ее диапазон -127 ... 127
             будем использовать переменную short. В Java байты могут быть отрицат. величины,
             поэтому приводим их в нормальное значение добавив 256. */

            short symbl = symbol;
            if (symbl < 0 ) symbl += 256;

            buffer = (buffer << 8); // сдвигаем влево для добавления битов символа - 8 бит
            // (каждый новый символ занимает 8 бит)
            buffer = (buffer | symbl); // добавляем символ в буфер
            BufferBitCounter += 8; // Прибавляем счетчик бит в буффере на 8 бит (добавили символ - он занимает 8 бит)

            /* Если количество бит в передаваеваемом сообщнии не кратно 6-ти (мы кодируем сообщение по 6 бит)
             тогда нам нужно добавить в самый конец цепочки байтов
             (поэтому мы здесь проверяем что i == byteArr.length -1)
             необходимые пустые биты, чтобы длина была кратной 6-ти
            */
            if (i == byteArr.length -1 && numOfBits % 6 == 2) {
                buffer = buffer << 4;
                BufferBitCounter += 4;}
            else if (i == byteArr.length -1 && numOfBits % 6 == 4 ) {
                buffer = buffer << 2;
                BufferBitCounter += 2;}

            // если наш счетчик в буфере больше или равен 6 - разбиваем буфер на блок из 6 бит
            while (BufferBitCounter >= 6)
            {
                /* получаем блок из 6-ти бит сдвигая вправо (Насколько нужно сдвинуть? Мы это определяем с помощью счетчика бит
                 * мы сдвигаем столько бит, сколько показывает счетчик BufferBitCounter МИНУС 6,
                 * т.к. 6 бит мы оставляем для блока в 6 бит, который мы кодируем в символ MIME64 */
                temp6Bit = buffer >> (BufferBitCounter - 6);
                BufferBitCounter -= 6; // После того как мы получили блок из 6 бит для MIME64 у нас буфер на 6 бит меньше

                // Очищаем буффер, если счетчик использованных бит равен нулю
                if (BufferBitCounter == 0) {
                    buffer = 0;
                }
                else {
                    // очищаем буфер от выброшенных битов -- буфер имеет тип int - 32 бит
                    buffer = (int) (buffer << (32 - BufferBitCounter));
                    buffer = (int) (buffer >>> (32 - BufferBitCounter));
                }

                result += abc.charAt(temp6Bit); // присваиваем с результату закодированный в MIME64 символ
            }
        }

        return result; // Выводим закодированный в MIME64 символ на экран
    }


    public static byte[] StartDecode(byte[] bytes) {

        // Вначале работы методов startMimeEncode и startMimeDecode инициализируем переменные нулями
        buffer = 0; // здесь я храню символы и разбиваю блоки по 6 бит
        BufferBitCounter = 0; // счетчик бит в буффере
        ListOfBytes.clear();

        byte mimeAbcPosition; // Нам необходимо определить позицию символа в MIME-Алфавите
        char mimeLetter;


        // Перебираем поисмвольно тест закодированный в MIME64. Напомню, мы его перевели в массив байт, для удобства
        for (int i = 0; i < bytes.length; i++)
        {
            // Нам нужно теперь байт из массива перевести в символ (char)
            mimeLetter = (char)(bytes[i]);
            if (mimeLetter == '=' ) break;
            // и определить позицию символа в массиве алфавите MIME64
            mimeAbcPosition = (byte)abc.indexOf(mimeLetter);
            // теперь вызываем метод декодирования MIME64 отправляя туда позицию символа в алфавите MIME64
            Mime64Decode(mimeAbcPosition);
        }

        Byte[] byteArray = new Byte[1];
        byteArray = ListOfBytes.toArray(byteArray);
        byte[] result = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++)
            result[i] = byteArray[i];

        return result;
    }

    /*
     * Метод принимает параметр - byte Symbol, который определяет положение символа в массиве-алфавите MIME64
     * В процессе работы метода - в буфере накапливаются биты
     */
    public static void Mime64Decode(byte Symbol)
    {
        byte temp8bit = 0; // Временная переменная, для вырезания из буфера блоков по 8 бит (привычные нам символы в кодировке win-1251 занимают 8 бит)

        /* Сдвигаем буфер влево на 6 бит для добавления битов нового числа-позиции в алфавите
        * (каждый новый символ (т.е. позиция в алфавите MIME) занимает 6 бит) */
        buffer = (buffer << 6);
        buffer = (buffer | Symbol); // добавляем новые биты в буфер в освобожденное место (сдвинули выше, освободив для битов место)
        BufferBitCounter += 6; // Теперь счетчик битов на 6 битов больше

        // В случае если счетчик битов >= 8 разбиваем буфер на блоки из 8 битов
        while (BufferBitCounter >= 8)
        {
          /* получаем блок из 8-ти бит сдвигая вправо (Насколько нужно сдвинуть? Мы это определяем с помощью счетчика бит
           * мы сдвигаем столько бит, сколько показывает счетчик BufferBitCounter МИНУС 8,
           * т.к. 8 бит мы оставляем для блока в 8 бит, который мы декодируем в символ windows-1251 */
            temp8bit = (byte)(buffer >> (BufferBitCounter - 8));
            BufferBitCounter -= 8; // Теперь счетчик битов в буфере уменьшается на 8
            buffer = (buffer << (16 - BufferBitCounter)); // очищаем буфер от выброшенных битов -- буфер имеет тип ushort - 16 бит
            buffer = (buffer >> (16 - BufferBitCounter));

            // добавляем декодированные 8битные кусочки в "массив" байтов (List<byte>), который затем будет преобразован в текст в формате UTF-8
            ListOfBytes.add(temp8bit);
        }
    }

}
