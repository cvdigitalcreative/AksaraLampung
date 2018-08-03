package com.example.shaff.aksaralampung.Controller;

import java.util.ArrayList;



public class GraphMatching {
	//konstruktor untuk kelas GraphMatching
    public GraphMatching(){}

	//metode fungsi untuk mendapatkan pola dari vertex dan edge beserta arahnya
    public String hitungPolaVertex(int[][] ArrSkeleton){
        int i, j, k, l, height, width, nonStraight, nVertex, nTetanggaLurus;
        int[] subArr;
        int[] posisiTemps;
        String polaGraph;
        String polaArah;
        ArrayList<Integer> posisiTetangga;
        ArrayList<String> arahVertex;
        ArrayList<int[]> simpanPosisi;

        height = ArrSkeleton.length;
        width = ArrSkeleton[0].length;

        polaGraph = "";
        nVertex = 0;
        simpanPosisi = new ArrayList<>();

        for(i=0; i<height; i++){
            for(j=0; j<width; j++){
                if(i != 0 && i!=height-1 && j != 0 && j!=width-1){
					//mengecek apakah pixel yang dilewati adalah pixel untuk warna hitam
                    if(ArrSkeleton[i][j] == 0){
                        polaArah = "";
                        subArr = new int[10];
                        nTetanggaLurus = 0;

						//matriks subArr untuk pencarian matriks ketetanggaan
                        subArr[9] = ArrSkeleton[i-1][j-1];
                        subArr[2] = ArrSkeleton[i-1][j];
                        subArr[3] = ArrSkeleton[i-1][j+1];
                        subArr[8] = ArrSkeleton[i][j-1];
                        subArr[1] = ArrSkeleton[i][j];
                        subArr[4] = ArrSkeleton[i][j+1];
                        subArr[7] = ArrSkeleton[i+1][j-1];
                        subArr[6] = ArrSkeleton[i+1][j];
                        subArr[5] = ArrSkeleton[i+1][j+1];
                        posisiTetangga = new ArrayList<>();
                        arahVertex = new ArrayList<>();
						
						//proses mencari arah dan posisi
                        for(k=2; k<=9; k++){
                            if(subArr[k] == 0){
                                int posisi = k;
                                String arah = "";

                                if(k == 2){
                                    arah = "d1";
                                }
                                else if(k == 3){
                                    arah = "d2";
                                }
                                else if(k == 4){
                                    arah = "d3";
                                }
                                else if(k == 5){
                                    arah = "d4";
                                }
                                else if(k == 6){
                                    arah = "d5";
                                }
                                else if(k == 7){
                                    arah = "d6";
                                }
                                else if(k == 8){
                                    arah = "d7";
                                }
                                else if(k == 9){
                                    arah = "d8";
                                }

                                String arahPixel = arah;

                                posisiTetangga.add(posisi);
                                arahVertex.add(arahPixel);
                            }
                        }

						//pencarian pola arah dan vertex pada proses pencocokan graf
                        nonStraight = 0;
                        if(posisiTetangga.size() == 2){
                            if(Math.abs(posisiTetangga.get(0)-posisiTetangga.get(1)) < 4){
                                nTetanggaLurus = nTetanggaLurus + 1;
                            }
                            else{
                                if(posisiTetangga.get(0) == 2 && posisiTetangga.get(1) == 6){
                                    nTetanggaLurus = nTetanggaLurus + 1;
                                }
                                else if(posisiTetangga.get(0) == 3 && posisiTetangga.get(1) == 7){
                                    nTetanggaLurus = nTetanggaLurus + 1;
                                }
                                else if(posisiTetangga.get(0) == 4 && posisiTetangga.get(1) == 8){
                                    nTetanggaLurus = nTetanggaLurus + 1;
                                }
                                else if(posisiTetangga.get(0) == 5 && posisiTetangga.get(1) == 9){
                                    nTetanggaLurus = nTetanggaLurus + 1;
                                }
                                else if(posisiTetangga.get(0) == 6 && posisiTetangga.get(1) == 2){
                                    nTetanggaLurus = nTetanggaLurus + 1;
                                }
                                else if(posisiTetangga.get(0) == 7 && posisiTetangga.get(1) == 3){
                                    nTetanggaLurus = nTetanggaLurus + 1;
                                }
                                else if(posisiTetangga.get(0) == 8 && posisiTetangga.get(1) == 4){
                                    nTetanggaLurus = nTetanggaLurus + 1;
                                }
                                else if(posisiTetangga.get(0) == 9 && posisiTetangga.get(1) == 5){
                                    nTetanggaLurus = nTetanggaLurus + 1;
                                }
                                else{
                                    if(simpanPosisi.isEmpty()){
                                        posisiTemps = new int[2];
                                        posisiTemps[0] = posisiTetangga.get(0);
                                        posisiTemps[1] = posisiTetangga.get(1);
                                        simpanPosisi.add(posisiTemps);

                                        nonStraight += 1;

                                        polaArah += arahVertex.get(0);
                                        polaArah += arahVertex.get(1);
                                    }
                                    else{
                                        for(k=0; k<simpanPosisi.size(); k++){
                                            if(posisiTetangga.get(0) == simpanPosisi.get(k)[0] && posisiTetangga.get(1) == simpanPosisi.get(k)[1]){
                                                nTetanggaLurus = nTetanggaLurus + 1;
                                            }
                                        }

                                        if(nTetanggaLurus == 0){
                                            posisiTemps = new int[2];
                                            posisiTemps[0] = posisiTetangga.get(0);
                                            posisiTemps[1] = posisiTetangga.get(1);
                                            simpanPosisi.add(posisiTemps);

                                            nonStraight += 1;

                                            polaArah += arahVertex.get(0);
                                            polaArah += arahVertex.get(1);
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            for(k=0; k<posisiTetangga.size()-1; k++){
                                for(l=k+1; l<posisiTetangga.size(); l++){
                                    if(posisiTetangga.get(k) == 2 && posisiTetangga.get(l) == 6){
                                        nTetanggaLurus = nTetanggaLurus + 1;
                                    }
                                    else if(posisiTetangga.get(k) == 3 && posisiTetangga.get(l) == 7){
                                        nTetanggaLurus = nTetanggaLurus + 1;
                                    }
                                    else if(posisiTetangga.get(k) == 4 && posisiTetangga.get(l) == 8){
                                        nTetanggaLurus = nTetanggaLurus + 1;
                                    }
                                    else if(posisiTetangga.get(k) == 5 && posisiTetangga.get(l) == 9){
                                        nTetanggaLurus = nTetanggaLurus + 1;
                                    }
                                    else if(posisiTetangga.get(k) == 6 && posisiTetangga.get(l) == 2){
                                        nTetanggaLurus = nTetanggaLurus + 1;
                                    }
                                    else if(posisiTetangga.get(k) == 7 && posisiTetangga.get(l) == 3){
                                        nTetanggaLurus = nTetanggaLurus + 1;
                                    }
                                    else if(posisiTetangga.get(k) == 8 && posisiTetangga.get(l) == 4){
                                        nTetanggaLurus = nTetanggaLurus + 1;
                                    }
                                    else if(posisiTetangga.get(k) == 9 && posisiTetangga.get(l) == 5){
                                        nTetanggaLurus = nTetanggaLurus + 1;
                                    }
                                    else{
                                        if(Math.abs(posisiTetangga.get(k)-posisiTetangga.get(l)) == 1 || Math.abs(posisiTetangga.get(k)-posisiTetangga.get(l)) == 7){
                                            nTetanggaLurus = nTetanggaLurus + 1;
                                        }
                                        else{
                                            nonStraight += 1;

                                            if(nonStraight == 1){
                                                polaArah += arahVertex.get(k);
                                                polaArah += arahVertex.get(l);
                                            }
                                            else{
                                                polaArah += arahVertex.get(l);
                                            }
                                        }
                                    }
                                }
                            }
                        }
						
						//proses pemberian syarat pola vertex dan arah yang ditentukan untuk mendapatkan
						//pola graf dari image hasil draw/browse
                        if(nonStraight >= 1 && nTetanggaLurus == 0){
                            System.out.println("Banyak Tetangga : "+posisiTetangga.size());

                            nVertex = nVertex +1;
                            polaGraph = polaGraph+"V"+String.valueOf(nVertex)+polaArah+" ";
                        }
                        else if (posisiTetangga.size() == 1){
                            System.out.println("Banyak Tetangga : "+posisiTetangga.size());

                            nVertex = nVertex +1;
                            polaGraph = polaGraph+"V"+String.valueOf(nVertex)+arahVertex.get(0)+" ";
                        }
                    }
                }
            }
        }
        return polaGraph;
    }
}
