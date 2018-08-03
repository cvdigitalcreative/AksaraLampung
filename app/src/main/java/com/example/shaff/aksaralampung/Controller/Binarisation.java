package com.example.shaff.aksaralampung.Controller;

import android.graphics.Bitmap;
import android.graphics.Color;


public class Binarisation {
	//konstruktor pada kelas binerisasi
    public Binarisation(){}

	//proses pencarian threshold
    public int Threshold(Bitmap imgGray){
        int th, total, i;
        int[] histogram;
        float tMean,variance, maxVariance,firstCM,zerothCM;

        th = 0;
        total = imgGray.getHeight()*imgGray.getWidth();

        tMean = 0;
        maxVariance = 0;
        firstCM = zerothCM = 0;
        histogram = histogramImage(imgGray);

        for (i = 0; i < 256; i++)
            tMean += i * histogram[i] / (total);

        for (i = 0; i < 256; i++) {
            zerothCM += histogram[i] / (total);
            firstCM += i * histogram[i] / (total);
            variance = (tMean * zerothCM - firstCM);
            variance *= variance;

            variance /= zerothCM * (1 - zerothCM);
            if (maxVariance < variance){
                maxVariance = variance;
                th = i;
            }
        }

        return th;
    }

	//proses mendapatkan image biner dari image grayscale
    public Bitmap Binarize(Bitmap imgGray){
        int red, newPixel, th, i, j;
        Bitmap imgBinarize;

		//proses pencarian threshld pada proses binerisasi
        th = Threshold(imgGray);

		//proses pembentukan objek image biner menggunakan tinggi dan lebar dari image grayscale
        imgBinarize = Bitmap.createBitmap(imgGray.getWidth(), imgGray.getHeight(), imgGray.getConfig());

		//proses binerisasi pada objek image biner dari image grayscale
        for(i=0; i<imgGray.getWidth(); i++){
            for(j=0; j<imgGray.getHeight(); j++){
                red = Color.red(imgGray.getPixel(i,j));

                if(red > th){
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }

                imgBinarize.setPixel(i, j, Color.rgb(newPixel, newPixel, newPixel));
            }
        }

        return imgBinarize;
    }

	//proses untuk mendapatkan matriks dari image biner
    public int[][] MatriksBinarize(Bitmap imgBiner){
        int red, i, j;

        int[][] BinarizeArr = new int[imgBiner.getHeight()][imgBiner.getWidth()];

        for(i=0; i<imgBiner.getWidth(); i++){
            for(j=0; j<imgBiner.getHeight(); j++){
                red = Color.red(imgBiner.getPixel(i,j));

                BinarizeArr[j][i] = red;
            }
        }

        return BinarizeArr;
    }

	//proses untuk mendapatkan histogram dari image grayscale
    public int[] histogramImage(Bitmap imgGray){
        int i, j, red, pixel;
        int[] histogram = new int[256];

        for(i=0; i<histogram.length; i++){
            histogram[i] = 0;
        }

        for(i=0; i<imgGray.getWidth(); i++){
            for(j=0; j<imgGray.getHeight(); j++){
                pixel = imgGray.getPixel(i,j);
                red = Color.red(pixel);
                histogram[red]++;
            }
        }

        return histogram;
    }
}
