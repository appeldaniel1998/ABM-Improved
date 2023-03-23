package com.example.abm_improved.Products;

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

import java.util.UUID;

public class AddNewProductFragment extends BaseFragment {

    private EnterProductDetails enterProductDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.templates_enter_product_details, container, false);

        enterProductDetails = new EnterProductDetails(view, onChooseProfilePicListener, requireActivity());
        requireActivity().setTitle("Add New Product");

        enterProductDetails.getAddProductButton().setOnClickListener(v -> {
            Product product = new Product(UUID.randomUUID().toString(),
                    enterProductDetails.getProductNameEditText().getText().toString(),
                    enterProductDetails.getProductDescriptionEditText().getText().toString(),
                    enterProductDetails.getProductCategoryEditText().getText().toString(),
                    Double.parseDouble(enterProductDetails.getProductPriceEditText().getText().toString()),
                    Integer.parseInt(enterProductDetails.getProductQuantityEditText().getText().toString()));
            DatabaseUtils.uploadRelevantProductInfo(product,
                    requireActivity(),
                    FirebaseStorage.getInstance().getReference().child("Products").child(product.getUid()).child("profile.jpg"));

            // if successful, go back to the previous fragment
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductsMainFragment()).commit();
        });


        return view;
    }
}
