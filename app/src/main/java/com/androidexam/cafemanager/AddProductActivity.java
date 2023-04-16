package com.androidexam.cafemanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidexam.cafemanager.databinding.ActivityAddProductBinding;
import com.androidexam.cafemanager.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AddProductActivity extends AppCompatActivity {

    private String idProduct;
    private Uri uriImage;


    private ActivityAddProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        idProduct = intent.getStringExtra("idProduct");

        setDataSpinner();

        if (!TextUtils.isEmpty(idProduct)) {
            loadDataProduct();
            clickSaveEdit();
        } else {
            clickAdd();
        }
        clickImage();
    }

    private void clickSaveEdit() {
        binding.btnAdd.setOnClickListener(view -> {
            Product newProduct = getProduct();
            if (newProduct != null) {

            }
        });
    }

    private void loadDataProduct() {
        Product product = new Product();
        binding.etName.setText(product.getName());
        binding.etPrice.setText(String.valueOf(product.getPrice()));
        binding.etPrice.setText(product.getDescription());
        Picasso.get().load(product.getUrlImage()).into(binding.imgProduct);
    }

    private void clickAdd() {

        binding.btnAdd.setOnClickListener(view -> {
            Product newProduct = getProduct();
            if (newProduct != null) {
                DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");
                String id = productsRef.push().getKey();
                newProduct.setId(id);
                productsRef.child(id).setValue(newProduct);
                if (uriImage != null) {
                    StorageReference imageProductRef = FirebaseStorage.getInstance().getReference().child("/imageProduct");
                    StorageReference imageRef = imageProductRef.child(id + ".jpg");
                    UploadTask uploadTask = imageRef.putFile(uriImage);

                    // Add an OnSuccessListener to the upload task to get the download URL of the uploaded image
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image upload successful, get the download URL
                            Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                            downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Get the download URL
                                    String downloadUrl = uri.toString();
                                    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");
                                    productsRef.child(id).child("urlImage").setValue(downloadUrl);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("image", exception.toString());
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image upload in progress, update UI to show progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        }
                    });
                }
                Toast.makeText(this, "Thêm Sản Phẩm Mới Thành Công", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Product getProduct() {
        if (checkProductInput()) {
            Product newProduct = new Product();
            newProduct.setName(binding.etName.getText().toString());
            newProduct.setCategory(binding.spCategory.getSelectedItem().toString());
            newProduct.setPrice(Integer.parseInt(binding.etPrice.getText().toString()));
            newProduct.setDescription(binding.etDescription.getText().toString());
            return newProduct;
        }
        return null;
    }

    private boolean checkProductInput() {
        if (TextUtils.isEmpty(binding.etName.getText().toString())) {
            Toast.makeText(this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(binding.etPrice.getText().toString())) {
            Toast.makeText(this, "Vui lòng nhập giá sản phẩm", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(binding.etDescription.getText().toString())) {
            Toast.makeText(this, "Vui lòng nhập mô tả sản phẩm", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private void clickImage() {
        ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        uriImage = uri;
                        Picasso.get().load(uri).into(binding.imgProduct);
                    }
                });

        binding.imgProduct.setOnClickListener(view -> {
            imagePickerLauncher.launch("image/*");
        });
    }


    private void setDataSpinner() {
        ArrayList<String> category = new ArrayList<>();
        category.add("Cà Phê");
        category.add("Trà Sữa");
        category.add("Nước ép");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, category);
        binding.spCategory.setAdapter(adapter);
    }


}