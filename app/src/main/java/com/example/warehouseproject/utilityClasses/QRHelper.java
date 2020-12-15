package com.example.warehouseproject.utilityClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

/**
 * QRHelper class
 *
 * Данный класс реализует вспомогательные функции для работы с изображениями на этапе генерации QR-кодов
 */
public class QRHelper {

    /**
     * Данный метод переводит изображение в массив байт
     * @param bmp изображение
     * @return массив байт изображения
     */
    public byte[] imagetobyte (Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return imageBytes;
    }

    /**
     * Данный метод переводит массив байт в изображение
     * @param bytearr массив байт
     * @return изображение
     */
    public Bitmap bytetoimage(byte[] bytearr){
        Bitmap bmp = BitmapFactory.decodeByteArray(bytearr, 0, bytearr.length);
        return bmp;
    }


    /**
     * Данный метод переводит массив байт изображения в строку base64
     * @param image
     * @return
     */
    public String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    /**
     * Данный метод переводит строку base64, в которой закодирован массив байт в изображение
     * @param input строка base64
     * @return изображение
     */
    public Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
