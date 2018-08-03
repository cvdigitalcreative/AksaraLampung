package com.example.shaff.aksaralampung.Controller;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;



public class Skeletonisation {
    private int[][] imgArr;
    private ArrayList<int[]> koordinatMarks;

	//konstruktor pada kelas Skeletonisation
    public Skeletonisation(){}

	//memberi nilai awal untuk matriks dari image biner
    public void setImgArr(int[][] binerArr){
        int i, j;

        imgArr = new int[binerArr.length][binerArr[0].length];

        for(i=0; i<binerArr.length; i++){
            for(j=0; j<binerArr[i].length; j++){
                imgArr[i][j] = binerArr[i][j];
            }
        }
    }
	
	//proses tahap awal dari proses skeletonisasi
    public void HitungSkeletonizeTahap1(){
        int i, j, k, l, height, width, kondisi1, kondisi2, kondisi3, kondisi4;
        int[][] subBinerArr;
        int[] koordinat;

        height = imgArr.length;
        width = imgArr[0].length;
        koordinatMarks = new ArrayList<>();

        for(i=0; i<height; i++){
            for(j=0; j<width; j++){
                if(i != 0 && i!=height-1 && j != 0 && j!=width-1){
                    if(imgArr[i][j] == 0){
                        subBinerArr = new int[3][3];
                        kondisi1 = kondisi2 = kondisi3 = kondisi4 = 0;

						//kondisi 1 merupakan syarat untuk skeletonisasi pertama
						//kondisi 2 merupakan syarat untuk skeletonisasi kedua
						//kondisi 3 merupakan syarat untuk skeletonisasi ketiga
						//kondisi 4 merupakan syarat untuk skeletonisasi keempat
						//subBinerArr merupakan matriks yang menunjukkan bagian image yang akan dianalisis ketetanggaannya
                        subBinerArr[0][0] = imgArr[i-1][j-1];
                        subBinerArr[0][1] = imgArr[i-1][j];
                        subBinerArr[0][2] = imgArr[i-1][j+1];
                        subBinerArr[1][0] = imgArr[i][j-1];
                        subBinerArr[1][1] = imgArr[i][j];
                        subBinerArr[1][2] = imgArr[i][j+1];
                        subBinerArr[2][0] = imgArr[i+1][j-1];
                        subBinerArr[2][1] = imgArr[i+1][j];
                        subBinerArr[2][2] = imgArr[i+1][j+1];

                        for(k=0; k<subBinerArr.length; k++){
                            for(l=0; l<subBinerArr[k].length;l++){
								//penghitungan untuk mendapatkan syarat pertama dari skeletonisasi
                                if(subBinerArr[k][l] == 0 && (k!=1 || l!=1)){
                                    kondisi1 += 1;
                                }

								//penghitungan untuk mendapatkan syarat kedua dari skeletonisasi
                                if(k==0 && l!=2){
                                    if(subBinerArr[k][l] == 255 && subBinerArr[k][l+1] == 0){
                                        kondisi2 += 1;
                                    }
                                }

                                if(l==2 && k!= 2){
                                    if(subBinerArr[k][l] == 255 && subBinerArr[k+1][l] == 0){
                                        kondisi2 += 1;
                                    }
                                }

                                if(k == 2 && l!=0){
                                    if(subBinerArr[k][l] == 255 && subBinerArr[k][l-1] == 0){
                                        kondisi2 += 1;
                                    }
                                }

                                if(l==0 && k!=0){
                                    if(subBinerArr[k][l] == 255 && subBinerArr[k-1][l] == 0){
                                        kondisi2 += 1;
                                    }
                                }

                            }
                        }

						//penghitungan untuk mendapatkan syarat ketiga dari skeletonisasi
                        if(subBinerArr[0][1]==255 || subBinerArr[1][2]==255 || subBinerArr[2][1]==255){
                            kondisi3 = 1;
                        }

						//penghitungan untuk mendapatkan syarat keempat dari skeletonisasi
                        if(subBinerArr[1][2]==255 || subBinerArr[2][1]==255 || subBinerArr[1][0]==255){
                            kondisi4 = 1;
                        }

						//pemberian mark atau tanda pada matriks subBinerArr yang memenuhi kondisi atau persyaratan
                        if(kondisi1>= 2 && kondisi1<=6 && kondisi2==1 && kondisi3==1 && kondisi4==1){
                            koordinat = new int[2];
                            koordinat[0] = i;
                            koordinat[1] = j;
                            koordinatMarks.add(koordinat);
                        }
                    }
                }
            }
        }

		//pergantian dari pixel hitam ke putih untuk pixel yang ditandai atau diberikan mark pada matriks subBinerArr
        for(i=0; i<koordinatMarks.size(); i++){
            imgArr[koordinatMarks.get(i)[0]][koordinatMarks.get(i)[1]] = 255;
        }
    }

	//proses tahap kedua dari proses skeletonisasi
    public void HitungSkeletonizeTahap2(){
        int i, j, k, l, height, width, kondisi1, kondisi2, kondisi3, kondisi4;
        int[][] subBinerArr;
        int[] koordinat;

        height = imgArr.length;
        width = imgArr[0].length;
        koordinatMarks = new ArrayList<>();

        for(i=0; i<height; i++){
            for(j=0; j<width; j++){
                if(i != 0 && i!=height-1 && j != 0 && j!=width-1){
                    if(imgArr[i][j] == 0){
                        subBinerArr = new int[3][3];
                        kondisi1 = kondisi2 = kondisi3 = kondisi4 = 0;

                        subBinerArr[0][0] = imgArr[i-1][j-1];
                        subBinerArr[0][1] = imgArr[i-1][j];
                        subBinerArr[0][2] = imgArr[i-1][j+1];
                        subBinerArr[1][0] = imgArr[i][j-1];
                        subBinerArr[1][1] = imgArr[i][j];
                        subBinerArr[1][2] = imgArr[i][j+1];
                        subBinerArr[2][0] = imgArr[i+1][j-1];
                        subBinerArr[2][1] = imgArr[i+1][j];
                        subBinerArr[2][2] = imgArr[i+1][j+1];

                        for(k=0; k<subBinerArr.length; k++){
                            for(l=0; l<subBinerArr[k].length;l++){
                                if(subBinerArr[k][l] == 0 && (k!=1 || l!=1)){
                                    kondisi1 += 1;
                                }

                                if(k==0 && l!=2){
                                    if(subBinerArr[k][l] == 255 && subBinerArr[k][l+1] == 0){
                                        kondisi2 += 1;
                                    }
                                }

                                if(l==2 && k!= 2){
                                    if(subBinerArr[k][l] == 255 && subBinerArr[k+1][l] == 0){
                                        kondisi2 += 1;
                                    }
                                }

                                if(k == 2 && l!=0){
                                    if(subBinerArr[k][l] == 255 && subBinerArr[k][l-1] == 0){
                                        kondisi2 += 1;
                                    }
                                }

                                if(l==0 && k!=0){
                                    if(subBinerArr[k][l] == 255 && subBinerArr[k-1][l] == 0){
                                        kondisi2 += 1;
                                    }
                                }

                            }
                        }

                        if(subBinerArr[0][1]==255 || subBinerArr[1][2]==255 || subBinerArr[1][0]==255){
                            kondisi3 = 1;
                        }

                        if(subBinerArr[0][1]==255 || subBinerArr[2][1]==255 || subBinerArr[1][0]==255){
                            kondisi4 = 1;
                        }

                        if(kondisi1>= 2 && kondisi1<=6 && kondisi2==1 && kondisi3==1 && kondisi4==1){
                            koordinat = new int[2];
                            koordinat[0] = i;
                            koordinat[1] = j;
                            koordinatMarks.add(koordinat);
                        }
                    }
                }
            }
        }

        for(i=0; i<koordinatMarks.size(); i++){
            imgArr[koordinatMarks.get(i)[0]][koordinatMarks.get(i)[1]] = 255;
        }
    }

	//metode funsi untuk mengecek apakah citra masih memiliki tanda/mark pada proses skeletonisasi
    public boolean isTandaMasihAda(){
        boolean mark = false;

        if(!koordinatMarks.isEmpty()){
            mark = true;
        }

        return mark;
    }

	//metode funsi untuk mendapatkan image bitmap skeleton
    public Bitmap getImgSkeletonize(Bitmap img){
        Bitmap imgSkeleton;
        int i, j, height, width, colorValue;

        height = imgArr.length;
        width = imgArr[0].length;

        imgSkeleton = Bitmap.createBitmap(width, height, img.getConfig());

        for(i=0; i<height; i++){
            for(j=0; j<width; j++){
                colorValue = imgArr[i][j];
                imgSkeleton.setPixel(j, i, Color.rgb(colorValue, colorValue, colorValue));
            }
        }

        return imgSkeleton;
    }

	//metode fungsi untuk mendapatkan array dari image skeleton
    public int[][] getArrSkeletonize(){
        return imgArr;
    }
}
