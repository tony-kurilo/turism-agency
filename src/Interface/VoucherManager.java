package Interface;

import Classes.Voucher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class VoucherManager {
    public static void saveVoucherToFile(Voucher voucher) {
        String fileName = "vouchers.txt";

        try (FileWriter fileWriter = new FileWriter(fileName, true);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            // Преобразовать данные из объекта Voucher в строку
            String data = voucher.getUsername() + "," +
                    voucher.getCountry() + "," +
                    voucher.getCity() + "," +
                    voucher.getHotel() + "," +
                    voucher.getBeginDate() + "," +
                    voucher.getEndDate() + "," +
                    voucher.getState() + "," +
                    voucher.getPrice() + "," +
                    voucher.getId();

            // Записать данные в файл
            writer.write(data);
            writer.newLine(); // Добавить новую строку для следующего ваучера
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибки сохранения ваучера в файл
        }
    }
}
