package com.androidexam.cafemanager;


import static com.androidexam.cafemanager.ProductFragment.ROLE_ADMIN;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidexam.cafemanager.databinding.ActivityAddProductBinding;
import com.androidexam.cafemanager.model.Product;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;


public class AddProductActivity extends AppCompatActivity {

    public static final ArrayList<String> CATEGORY_LIST = new ArrayList<>(Arrays.asList(
            "Cà Phê",
            "Nước Ép",
            "Trà Sữa",
            "Trà Trái Cây",
            "Soda",
            "Sinh Tố"
    ));
    private String idProduct;
    private Uri uriImageFromIntent=null;
    private boolean statusUpdate = false;

    private ActivityAddProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        idProduct = intent.getStringExtra("idProduct");

        setDataSpinner();

        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");

        checkRole(role);
        if (!TextUtils.isEmpty(idProduct)) {
            binding.btnAdd.setText("Sửa");
            loadDataProduct();
            enableComponents(false);
        } else {
            clickAdd();
        }
        clickCancel();
    }

    private void checkRole(String role) {
        if (role.equals(ROLE_ADMIN)) {
            binding.layoutEdit.setVisibility(View.VISIBLE);
            clickImage();
            clickUpdate();
            clickDelete();
        }
    }


    private void clickDelete() {
        binding.layoutProduct.setOnLongClickListener(v -> {
                    showConfirmationDialog();
                    return true;
                }
        );
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Product");
        builder.setMessage("Are you sure you want to delete this product?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Delete the product here
            deleteProduct();
            finish();
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteProduct() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");
        productsRef.child(idProduct).removeValue();
    }

    private void clickCancel() {
        binding.btnCancel.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(idProduct)) {
                binding.btnAdd.setText("Sửa");
                loadDataProduct();
                enableComponents(false);
                uriImageFromIntent = null;
                statusUpdate=false;
            } else {
                finish();
            }
            if(!statusUpdate){
                finish();
            }
        });
    }

    private void enableComponents(boolean type) {
        binding.etDescription.setEnabled(type);
        binding.etPrice.setEnabled(type);
        binding.etName.setEnabled(type);
        binding.spCategory.setEnabled(type);
    }

    private void clickUpdate() {
        binding.btnAdd.setOnClickListener(view -> {
            if (statusUpdate) {
                Product updateProduct = getProduct();
                if (updateProduct != null) {
                    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products").child(idProduct);
                    productsRef.child("name").setValue(updateProduct.getName());
                    productsRef.child("price").setValue(updateProduct.getPrice());
                    productsRef.child("category").setValue(updateProduct.getCategory());
                    productsRef.child("description").setValue(updateProduct.getDescription());
                    saveImage(idProduct);
                    enableComponents(false);
                    statusUpdate = false;
                    Toast.makeText(this, "Cập Nhật Sản Phẩm Thành Công", Toast.LENGTH_SHORT).show();
                    binding.btnAdd.setText("Sửa");
                    finish();
                }
            } else {
                enableComponents(true);
                statusUpdate = true;
                binding.btnAdd.setText("Lưu");
            }
        });
    }

    private void loadDataProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = database.getReference("Products");
        DatabaseReference productRef = productsRef.child(idProduct);

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Product product = snapshot.getValue(Product.class);
                    binding.etName.setText(product.getName());
                    binding.etPrice.setText(String.valueOf(product.getPrice()));
                    binding.etDescription.setText(product.getDescription());
                    if (!TextUtils.isEmpty(product.getUrlImage())) {
                        Picasso.get().load(product.getUrlImage()).error(R.drawable.img).into(binding.imgProduct);
                    }
                    int categoryIndex = CATEGORY_LIST.indexOf(product.getCategory());
                    if (categoryIndex != -1) {
                        binding.spCategory.setSelection(categoryIndex);
                    }
                    ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur
            }
        });
    }

    private void clickAdd() {
        binding.btnAdd.setOnClickListener(view -> {
            Product newProduct = getProduct();
            if (newProduct != null) {
                DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");
                String id = productsRef.push().getKey();
                newProduct.setId(id);
                productsRef.child(id).setValue(newProduct);
                saveImage(id);
                Toast.makeText(this, "Thêm Sản Phẩm Mới Thành Công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void saveImage(String id) {
        if (uriImageFromIntent != null) {
            StorageReference imageProductRef = FirebaseStorage.getInstance().getReference().child("/imageProduct");
            StorageReference imageRef = imageProductRef.child(id + ".jpg");
            UploadTask uploadTask = imageRef.putFile(uriImageFromIntent);

            // Add an OnSuccessListener to the upload task to get the download URL of the uploaded image
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Image upload successful, get the download URL
                Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                downloadUrlTask.addOnSuccessListener(uri -> {
                    // Get the download URL
                    String downloadUrl = uri.toString();
                    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");
                    productsRef.child(id).child("urlImage").setValue(downloadUrl);
                });
            }).addOnFailureListener(exception -> Log.d("image", exception.toString())).addOnProgressListener(taskSnapshot -> {
            });
        }
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
                        uriImageFromIntent = uri;
                        Picasso.get().load(uri).into(binding.imgProduct);
                    }
                });

        binding.imgProduct.setOnClickListener(view -> {
            if (!statusUpdate) {
                enableComponents(true);
                statusUpdate = true;
                binding.btnAdd.setText("Lưu");
            }
            imagePickerLauncher.launch("image/*");
        });
    }


    private void setDataSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CATEGORY_LIST);
        binding.spCategory.setAdapter(adapter);
    }


}