package com.example.abm_improved.Products;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.DataClasses.Product;
import com.example.abm_improved.Products.Templates.EnterProductDetails;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditProductFragment extends BaseFragment {
    private EnterProductDetails enterProductDetails;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.templates_enter_product_details, container, false);

        enterProductDetails = new EnterProductDetails(view, onChooseProfilePicListener, requireActivity());
        requireActivity().setTitle("Edit Product");

        // Getting index of current client from previous fragment
        Bundle args = getArguments();
        assert args != null;
        int productIndex = Integer.parseInt(args.getString("productIndex"));
        Product currProduct = DatabaseUtils.getProducts().get(productIndex);

        enterProductDetails.setProductDetails(currProduct, requireActivity());

        //Connecting with Firebase storage and retrieving image
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Products").child(currProduct.getUid()).child("profile.jpg");
        DatabaseUtils.loadImageToImageView(storageReference, enterProductDetails.getProductImage(), requireActivity());

        enterProductDetails.getAddProductButton().setOnClickListener(v -> {
            Product product = new Product(currProduct.getUid(),
                    enterProductDetails.getProductNameEditText().getText().toString(),
                    enterProductDetails.getProductDescriptionEditText().getText().toString(),
                    enterProductDetails.getProductCategoryEditText().getText().toString(),
                    Double.parseDouble(enterProductDetails.getProductPriceEditText().getText().toString()),
                    Integer.parseInt(enterProductDetails.getProductQuantityEditText().getText().toString()));
            DatabaseUtils.uploadRelevantProductInfo(product, requireActivity(), storageReference);

            // if successful, go back to the previous fragment
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductsMainFragment()).commit();
        });

        enterProductDetails.getDeleteProductButton().setOnClickListener(v -> {
            //Ask for confirmation before deleting the client
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirm Deletion of product");
            builder.setMessage("Are you sure you want to delete " + currProduct.getName() + " and all its data?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) { // Confirmation received
                    DatabaseUtils.deleteProductFromDatabase(currProduct.getUid()); // Delete product from database
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductsMainFragment()).commit(); // Go back to previous fragment
                }
            });
            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return view;
    }
}
