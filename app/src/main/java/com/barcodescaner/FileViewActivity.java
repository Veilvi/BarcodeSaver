package com.barcodescaner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.barcodescaner.DataAnalysysFragments.EditData;

import com.barcodescaner.MainFragments.FileViewAdapter;
import com.barcodescaner.MainFragments.FragmentRecordAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


    public class FileViewActivity extends AppCompatActivity {
        RecyclerView mRecyclerView;
        FileViewAdapter mdataAdapter;
        View ChildView;
        int RecyclerViewItemPosition;
        private String filePath = "/storage/emulated/0/Documents/QRCodes.txt";
        private File file = new File(filePath);
        private List<String> lines = new ArrayList<String>();
        private DataClass dataItem;


        ArrayList<DataClass> data = new ArrayList<DataClass>();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_file_view);
            mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);


            try {
                lines = getArrayListFromFile(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            fillArrays(lines);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mdataAdapter = new FileViewAdapter(data);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
            mRecyclerView.setAdapter(mdataAdapter);

        }


        @Override
        protected void onResume() {
            super.onResume();

            mdataAdapter.setOnItemClickListener(new FileViewAdapter.ClickListener(){

                @Override
                public void onItemClick(int position, View v) {
                    Intent intent = new Intent(FileViewActivity.this, DataAnalyse.class);
                    intent.putExtra("list", (ArrayList<String>) lines);
                    int StringNumber = position * 4;
                    intent.putExtra("string_number", StringNumber);
                    String QrValue = data.get(position).qrCode;
                    intent.putExtra("BarcodeValue", QrValue);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(int position, View v) {
                    FragmentRecordAction fragmentRecordAction;
                    FragmentManager fragmentManager= getSupportFragmentManager();
                    Bundle bundle =new Bundle();
                    int pos= position*4;
                    String Title= lines.get(pos+1)+" "+lines.get(pos+2)+" "+lines.get(pos+3);
                    String Description=lines.get(pos);
                    bundle.putString("Title",Title);
                    bundle.putString("Description",Description);
                    fragmentRecordAction=new FragmentRecordAction();
                    fragmentRecordAction.setArguments(bundle);
                    fragmentRecordAction.show(fragmentManager,"FragmentRecordAction");
                }
            });


        }

        @Override
        public void onBackPressed() {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        ;

        public static ArrayList<String> getArrayListFromFile(File f)
                throws FileNotFoundException {
            Scanner s;
            ArrayList<String> list = new ArrayList<String>();
            s = new Scanner(f);
            //s.useDelimiter("^");
            while (s.hasNext()) {
                list.add(s.next());
            }
            s.close();
            return list;
        }

        public void fillArrays(List<String> incomeArray) {
            for (int i = 0; i < incomeArray.size(); i = i + 4) {
                dataItem = new DataClass();
                dataItem.setQrCode(incomeArray.get(i));
                dataItem.setText1(incomeArray.get(i + 1));
                dataItem.setText2(incomeArray.get(i + 2));
                dataItem.setText3(incomeArray.get(i + 3));
                data.add(dataItem);
            }
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setMessage(R.string.message_text)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TXTMechanic mechanic = new TXTMechanic();
                                int pos= viewHolder.getAdapterPosition()*4;
                                mechanic.deleteRecord(getApplicationContext(),(ArrayList<String>)lines,filePath,pos);
                                data.remove(viewHolder.getAdapterPosition());
                                mdataAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mdataAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).create().show();


            }
        };
    }


