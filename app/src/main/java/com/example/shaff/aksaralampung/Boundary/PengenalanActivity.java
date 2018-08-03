package com.example.shaff.aksaralampung.Boundary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaff.aksaralampung.Controller.Binarisation;
import com.example.shaff.aksaralampung.Controller.GraphMatching;
import com.example.shaff.aksaralampung.Controller.Grayscale;
import com.example.shaff.aksaralampung.Controller.Skeletonisation;
import com.example.shaff.aksaralampung.R;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class PengenalanActivity extends AppCompatActivity {
	//attribut yang digunakan untuk kelas PengenalanActivity
    int PICK_IMAGE_REQUEST = 1;
    int[][] imgArr, ArrSkeleton;
    int isSigPad;
    boolean isBrowse, isPengenalan, isSimpan;
    String PolaGraph;
    Bitmap bitmap, imgGray, imgBiner, imgSkeleton;
    TextView tvStatus;
    ImageView imageView;
    Button btnBrowse, btnPengenalan, btnSimpan, btnClear;
    Grayscale grayscaling;
    Binarisation binarisation;
    Skeletonisation skeletonisation;
    GraphMatching graphMatching;
    SignaturePad mSignaturePad;

	//konstruktor kelas PengenalanActivity
    public PengenalanActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengenalan);

		//inisialisasi attribut kelas PengenalanActivity yang didapat dari xmlnya
        btnBrowse = findViewById(R.id.btn_browse);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnPengenalan = findViewById(R.id.btn_pengenalan);
        btnClear = findViewById(R.id.clear);
        imageView = findViewById(R.id.img_citra);
        tvStatus = findViewById(R.id.tv_status_pengenalan);
        mSignaturePad = findViewById(R.id.signature);

		//inisialisasi attribut kelas PengenalanActivity dari kelas-kelas lainnya
        grayscaling = new Grayscale();
        binarisation = new Binarisation();
        skeletonisation = new Skeletonisation();
        graphMatching = new GraphMatching();

		//mengatur ketebalan dari brush milik drawing aksara
        mSignaturePad.setMaxWidth(10);
        mSignaturePad.setMinWidth(10);
		
		//memberi event listener untuk proses drawing pada mSignaturePad
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {

            }
        });

		//proses event listener untuk menjalankan perintah setelah button browse di tekan
        isBrowse = false;
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
                isBrowse = true;
                isPengenalan = isSimpan = false;
                imageView.setVisibility(View.VISIBLE);
                mSignaturePad.setVisibility(View.GONE);
            }
        });

		//proses event listener untuk menjalankan perintah saat button simpan di tekan
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(isBrowse);
                System.out.println(isPengenalan);

                if(isBrowse && isPengenalan || bitmap.getHeight() != 0 && isPengenalan){
                    isSimpan = true;

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(PengenalanActivity.this);
                    LayoutInflater layoutInflater = PengenalanActivity.this.getLayoutInflater();
                    View mView = layoutInflater.inflate(R.layout.simpan_data, null);

                    final EditText getKlasifikasi = mView.findViewById(R.id.et_klasifikasi);
                    final EditText getPolaGraph = mView.findViewById(R.id.et_polaGraph);
                    Button btnSimpanData = mView.findViewById(R.id.btn_simpanData);
                    Button btnBatal = mView.findViewById(R.id.btn_batal);

                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    getPolaGraph.setText(PolaGraph);

                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);

                    btnSimpanData.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(getKlasifikasi.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(), "Anda belum mengisi jenis klasifikasinya!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if(getPolaGraph.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(), "Anda belum mengisi pola aksaranya!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String klasifikasi = getKlasifikasi.getText().toString();
                            String pola = getPolaGraph.getText().toString();

                            SimpanData(klasifikasi, pola);
                            
                            tvStatus.setText("Data berhasil disimpan!");
                            dialog.dismiss();
                        }
                    });

                    btnBatal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Anda belum melakukan Pengenalan!", Toast.LENGTH_SHORT).show();
                }
            }
        });
		
		// proses event listener untk menjalankan perintah saat buttton pengenalan di tekan
        btnPengenalan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSigPad = 0;
                final ArrayList<Integer> Pola;
                final ArrayList<String> kelas_pola;
                final ArrayList<Integer> mVertex;

                if(!isBrowse){
                    isSigPad = 1;
                    bitmap = mSignaturePad.getSignatureBitmap();
                }

                if(isBrowse || bitmap.getHeight() != 0){
                    isPengenalan = true;
                    isSimpan = false;
                    int[] imgAr;

                    //buat objek kelas grayscale
                    grayscaling = new Grayscale();
					//proses pencarian image bitmap grayscaling menggunakan method fungsi Grayscaling(Bitmap) pada objek grayscaling
                    imgGray = grayscaling.Grayscaling(bitmap);

                    //buat objek kelas Binarisation
                    binarisation = new Binarisation();
					//proses pencaraian image bitmap biner menggunakan method fungsi Binarize(Bitmap) pada objek binarisation
                    imgBiner = binarisation.Binarize(imgGray);
					//proses mendapatkan array dari image bitmap biner menggunakan method fungsi MatriksBinarize(bitmap) pada objek binarisation
                    imgArr = binarisation.MatriksBinarize(imgBiner);
                    //proses mendapatkan threshold
                    int threshold = binarisation.Threshold(imgBiner);
                    //proses mendapatkan nilai histogram pixel
                    imgAr = binarisation.histogramImage(imgBiner);

                    //buat objek kelas Skeletonitation
                    skeletonisation = new Skeletonisation();
                    //proses masukan nilai img array
                    skeletonisation.setImgArr(imgArr);

					//tahap-tahap proses pencaraian image skeleton menggunakan method prosedur HitungSkeletonizeTahap1
					//,HitungSkeletonizeTahap2, dan method fungsi isTandaMasihAda pada objek skeletonisation
                    do{
                        skeletonisation.HitungSkeletonizeTahap1();
                        skeletonisation.HitungSkeletonizeTahap2();
                    }while (skeletonisation.isTandaMasihAda());

					//proses mendapatkan image skeleton dan array image skeleton menggunakan method fungsi getImgSkeletonize
					// dan getArrSkeletonize pada objek skeletonisation
                    imgSkeleton = skeletonisation.getImgSkeletonize(imgBiner);
                    ArrSkeleton = skeletonisation.getArrSkeletonize();

                    if(!isBrowse){
                        saveImage(imgSkeleton);
                    }

                    imageView.setVisibility(View.VISIBLE);
                    mSignaturePad.setVisibility(View.GONE);
                    imageView.setImageBitmap(imgSkeleton);
        
                    //buat objek kelas GraphMatching
                    graphMatching = new GraphMatching();
					//proses menghitung banyak vertex dan edges menggunakan method fungsi hitungPolaVertex 
					//pada objek graphMatching
                    PolaGraph = graphMatching.hitungPolaVertex(ArrSkeleton);
                    DatabaseReference reference;
                    Pola = new ArrayList<>();
                    kelas_pola = new ArrayList<>();
                    mVertex = new ArrayList<>();

					//proses pengecekan kecocokan data image hasil pengenalan dengan data di database firebase
                    reference = FirebaseDatabase.getInstance().getReference("pola_graph");

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String nBntkStr= "";
                            int sumV1;
                            sumV1 = 0;

                            for(int i=0; i<PolaGraph.length(); i++){
                                if(String.valueOf(PolaGraph.charAt(i)).equals(" ")){
                                    sumV1 += 1;
                                }
                            }

                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                int sum = 0;
                                int sumV2 = 0;
                                for(int i=0; i<PolaGraph.length(); i++){
                                    if(String.valueOf(PolaGraph.charAt(i)).equals(" ")){
                                        if(snapshot.child("pola_sampel").getValue().toString().contains(nBntkStr)){
                                            nBntkStr = "";
                                        }
                                        else{
                                            sum = sum+1;
                                            nBntkStr = "";
                                        }
                                    }
                                    else{
                                        nBntkStr = nBntkStr + PolaGraph.charAt(i);
                                    }
                                }

                                for(int i=0; i<snapshot.child("pola_sampel").getValue().toString().length(); i++){
                                    if(String.valueOf(snapshot.child("pola_sampel").getValue().toString().charAt(i)).equals(" ")){
                                        sumV2 += 1;
                                    }
                                }

                                String kelas = snapshot.child("nama_kelas").getValue().toString();

                                Pola.add(sum);
                                mVertex.add(sumV2);
                                kelas_pola.add(kelas);
                            }

                            int KemiripanRendah = 100;
                            int PolaTerpilih;
                            PolaTerpilih = Pola.size();

                            for(int i=0; i<Pola.size(); i++){
                                if(Math.abs(mVertex.get(i)-sumV1) == 0){
                                    if(KemiripanRendah > Pola.get(i)){
                                        KemiripanRendah = Pola.get(i);
                                        PolaTerpilih = i;
                                    }
                                }
                            }

                            if(KemiripanRendah <= (mVertex.get(PolaTerpilih)/2)+1 && Math.abs(mVertex.get(PolaTerpilih)-sumV1) == 0 && isSigPad == 1){
                                tvStatus.setText("Aksara berhasil dikenali!, Aksara termasuk dalam kelas '"+kelas_pola.get(PolaTerpilih)+"'");
                                Toast.makeText(getApplicationContext(), "Proses Pengenalan selesai!", Toast.LENGTH_SHORT).show();
                            }
                            else if(KemiripanRendah < 1 && Math.abs(mVertex.get(PolaTerpilih)-sumV1) == 0 && isSigPad == 0){
                                tvStatus.setText("Aksara berhasil dikenali!, Aksara termasuk dalam kelas '"+kelas_pola.get(PolaTerpilih)+"'");
                                Toast.makeText(getApplicationContext(), "Proses Pengenalan selesai!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                tvStatus.setText("Aksara tidak dikenali!");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Anda belum menampilkan citra aksara lampung!", Toast.LENGTH_SHORT).show();
                }
            }
        });

		//proses pembersihan pengenalan pada kanvas hasil drawing dan image
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setVisibility(View.GONE);
                mSignaturePad.setVisibility(View.VISIBLE);
                isBrowse = false;
                mSignaturePad.clear();

                tvStatus.setText("None");
            }
        });
    }

	//method prosedur untuk menampilkan image dari hp android ke program 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            Uri filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                tvStatus.setText("Citra berhasil dimuat!");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //method simpan data
    public void SimpanData(String klasifikasi, String pola){
        DatabaseReference reference;

        reference = FirebaseDatabase.getInstance().getReference("pola_graph");
        String key = reference.push().getKey();

        reference.child(key).child("id_pola").setValue(key);
        reference.child(key).child("nama_kelas").setValue(klasifikasi);
        reference.child(key).child("pola_sampel").setValue(pola);
    }

    //method prosedur untuk membuka browser dan memilih file yang akan ditampilkan
    public void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
    }

	//metode untuk menyimpan gambar hasil pengenalan aksara
    public void saveImage(Bitmap imgSave) {
        //create directory if not exist
        File dir = new File("/sdcard/tempfolder/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File output = new File(dir, "CitraUjiSkeleton.jpg");
        OutputStream os = null;

        try {
            os = new FileOutputStream(output);
            imgSave.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();

            //this code will scan the image so that it will appear in your gallery when you open next time
            MediaScannerConnection.scanFile(this, new String[]{output.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.d("appname", "image is saved in gallery and gallery is refreshed.");
                        }
                    }
            );
        } catch (Exception e) {
        }
    }
}
