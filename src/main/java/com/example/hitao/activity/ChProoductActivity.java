package com.example.hitao.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hitao.R;
import com.example.hitao.model.Product;
import com.lling.photopicker.PhotoPickerActivity;
import com.lling.photopicker.utils.ImageLoader;
import com.lling.photopicker.utils.OtherUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by fengsiyuan on 16/5/11.
 */
public class ChProoductActivity extends Activity{
    private TextView producttitle;
    private EditText productnameEdit;
    private EditText productnumEdit;
    private EditText productjieshaoEdit;
    private EditText productpriceEdit;
    private ImageView plusButton;
    private ImageView subButton;
    private Button give_up;
    private int Pnum;
    private String Pname;
    private String Pjieshao;
    private Double Pprice;
    private Button addphoto;
    private Spinner PSpinner;
    private static final int PICK_PHOTO = 1;
    private int selectedMode = PhotoPickerActivity.MODE_SINGLE;
    private int maxNum = PhotoPickerActivity.DEFAULT_NUM;
    private Integer Mysellerid;
    private List<String> pathList;

    private final String Parray[]=new String[]{
            "女装",
            "男装",
            "鞋包",
            "饰品",
            "运动",
            "美妆",
            "童装",
            "食品",
            "母婴",
            "百货",
            "家电",
            "数码",
            "家装"
    };//1、女装  2、男装  3、鞋包 4、饰品  5、运动
    // 6、美妆  7、童装  8、食品  9、母婴  10、百货  11、家电  12、数码  13、家装
    private ArrayAdapter<String> arrayAdapter;
    private int PCategory;
    private int CHOOSE_ACT;
    private String fileName;
    private GridView mGrideView;
    private List<String> mResults;
    private GridAdapter mAdapter;
    private int mColumnWidth;
    private Button delate;
    private String PobjectId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chproduct);
        producttitle = (TextView)findViewById(R.id.add_product_title);
        productnumEdit = (EditText)findViewById(R.id.productnum_edit);
        productnameEdit = (EditText)findViewById(R.id.productname_edit);
        productjieshaoEdit = (EditText)findViewById(R.id.productjieshao_edit);
        productpriceEdit = (EditText)findViewById(R.id.productprice_edit);
        plusButton = (ImageView)findViewById(R.id.plusButton);
        subButton = (ImageView)findViewById(R.id.subButton);
        give_up = (Button)findViewById(R.id.give_up);

        addphoto = (Button)findViewById(R.id.add_product_desc_image);
        PSpinner = (Spinner)findViewById(R.id.product_spinner);
        mGrideView = (GridView) findViewById(R.id.gridview);
        int screenWidth = OtherUtils.getWidthInPx(getApplicationContext());
        mColumnWidth = (screenWidth - OtherUtils.dip2px(getApplicationContext(), 4))/3;
        delate = (Button)findViewById(R.id.delate);


        Intent intent = getIntent();
        Mysellerid = intent.getIntExtra("Mysellerid", 0);
        CHOOSE_ACT = intent.getIntExtra("choose_act", 0);
        fileName = intent.getStringExtra("Pfilename");
        PobjectId = intent.getStringExtra("objectId");



        producttitle.setText("修改商品");
        productnameEdit.setText(intent.getStringExtra("Pname"));
        productpriceEdit.setText(intent.getStringExtra("Pprice"));
        productnumEdit.setText(intent.getStringExtra("Pnum"));
        productjieshaoEdit.setText(intent.getStringExtra("Pdesc"));


        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Parray);
        PSpinner.setAdapter(arrayAdapter);
        PSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                PCategory = position + 1;

                //Toast.makeText(getApplicationContext(), "" + (position + 1) + spinner.getItemAtPosition(position), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Toast.makeText(getApplicationContext(), "没有改变的处理", Toast.LENGTH_LONG).show();
            }

        });

        delate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.setObjectId(PobjectId);
                product.delete(ChProoductActivity.this, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        final ProgressDialog progressDialog = new ProgressDialog(ChProoductActivity.this);
                        progressDialog.setTitle("删除成功");
                        progressDialog.setMessage("Loading...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        SellerAct.sellerAct.finish();
                        Intent intent = new Intent(ChProoductActivity.this, SellerAct.class);

                        intent.putExtra("Mysellerid", Mysellerid);

                        startActivity(intent);
                        ChProoductActivity.this.finish();
                        SDetailedAct.sDetailedAct.finish();



                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

            }
        });

        give_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PpriceString = null;
                Pnum = Integer.parseInt(String.valueOf(productnumEdit.getText()));
                Pname = String.valueOf(productnameEdit.getText());
                Pjieshao = String.valueOf(productjieshaoEdit.getText());
                Pprice = Double.parseDouble(String.valueOf(productpriceEdit.getText()));
                PpriceString = String.valueOf(productpriceEdit.getText());

                if (Pnum != 0 && PpriceString != null && Pjieshao != null && Pname != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(ChProoductActivity.this);
                    progressDialog.setTitle("修改成功");
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Pprice = Double.valueOf(PpriceString);
                    final Product product = new Product();
                    product.setNumber(Pnum);
                    product.setPrice(Pprice);
                    product.setProductDesc(Pjieshao);
                    product.setProductName(Pname);
                    product.setSellerId(Mysellerid);
                    product.setCategory(PCategory);
                    if (pathList!=null) {
                        String pathname = pathList.toString().substring(1, pathList.toString().length() - 1);
                        System.out.println("绝对地址" + pathList.toString());

                        final BmobFile bmobFile = new BmobFile(new File(pathname));
                        bmobFile.uploadblock(ChProoductActivity.this, new UploadFileListener() {

                            @Override
                            public void onSuccess() {
                                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                                System.out.println("图片地址：" + bmobFile.getFileUrl(ChProoductActivity.this));
                                product.setProductPic(bmobFile);


                                product.update(ChProoductActivity.this,PobjectId, new UpdateListener() {

                                    @Override
                                    public void onSuccess() {
                                        SellerAct.sellerAct.finish();
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(ChProoductActivity.this, SellerAct.class);
                                        intent.putExtra("Mysellerid", Mysellerid);

                                        startActivity(intent);
                                        ChProoductActivity.this.finish();

                                    }

                                    @Override
                                    public void onFailure(int i, String s) {

                                    }
                                });


                            }

                            @Override
                            public void onProgress(Integer value) {
                                // 返回的上传进度（百分比）
                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                System.out.println("图片地址：null");


                            }
                        });
                    }else {
                        product.update(ChProoductActivity.this,PobjectId, new UpdateListener() {

                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(ChProoductActivity.this, SellerAct.class);
                                intent.putExtra("Mysellerid", Mysellerid);

                                startActivity(intent);

                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });

                    }




                } else {
                    Toast.makeText(getApplicationContext(), "请把信息填写完整", Toast.LENGTH_LONG).show();


                }


            }
        });







        plusandsub();

        addphoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChProoductActivity.this, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
                startActivityForResult(intent, PICK_PHOTO);

            }
        });







    }

    public void plusandsub(){
        Pnum = Integer.parseInt(String.valueOf(productnumEdit.getText()));



        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Pnum<999){
                    Pnum++;
                    productnumEdit.setText(Pnum+"");
                }
            }
        });


        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Pnum > 0) {

                    Pnum--;
                    productnumEdit.setText(Pnum + "");
                }
            }
        });


        productnumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Pnum = Integer.parseInt(String.valueOf(productnumEdit.getText()));
                    System.out.println(Pnum + "");
                } catch (Exception e) {

                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PHOTO){
            if(resultCode == RESULT_OK){
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                showResult(result);
            }
        }
    }

    private void showResult(ArrayList<String> paths){
        if(mResults == null){
            mResults = new ArrayList<String>();
        }
        mResults.clear();
        mResults.addAll(paths);

        if(mAdapter == null){
            mAdapter = new GridAdapter(mResults);
            mGrideView.setAdapter(mAdapter);
        }else {
            mAdapter.setPathList(mResults);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class GridAdapter extends BaseAdapter {


        public GridAdapter(List<String> listUrls) {
            pathList = listUrls;

        }

        @Override
        public int getCount() {
            return pathList.size();
        }

        @Override
        public String getItem(int position) {
            return pathList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setPathList(List<String> pathList) {
            pathList = pathList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.item_image, null);
                imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(imageView);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mColumnWidth, mColumnWidth);
                imageView.setLayoutParams(params);
            }else {
                imageView = (ImageView) convertView.getTag();
            }
            ImageLoader.getInstance().display(getItem(position), imageView, mColumnWidth, mColumnWidth);
            return convertView;
        }
    }

}
