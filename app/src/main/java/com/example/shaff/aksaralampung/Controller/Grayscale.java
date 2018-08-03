package com.example.shaff.aksaralampung.Controller;

import android.graphics.Bitmap;
import android.graphics.Color;



public class Grayscale {
    public Grayscale(){}

    public Bitmap Grayscaling(Bitmap img){
        Bitmap imgGray;
        double redDot, greenDot, blueDot;
        int width, height, i, j, R, G, B, pixel;

        width = img.getWidth();
        height = img.getHeight();

        redDot = 0.2126;
        greenDot = 0.7152;
        blueDot = 0.0722;

		//proses untuk membuat objek image grayscale berdasarkan tinggi dan lebar dari imaga asli
        imgGray = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());

		//proses pembentukan image grayscale 
        for(i=0; i<width; i++){
            for(j=0; j<height; j++){
                pixel = img.getPixel(i,j);

                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                R = G = B = (int)((R*redDot)+(G*greenDot)+(B*blueDot));
                imgGray.setPixel(i, j, Color.rgb(R, G, B));
            }
        }

        return imgGray;
    }
}
