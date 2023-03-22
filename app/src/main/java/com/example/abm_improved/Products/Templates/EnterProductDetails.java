package com.example.abm_improved.Products.Templates;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.example.abm_improved.DataClasses.Product;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.Interfaces;
import com.google.firebase.storage.FirebaseStorage;

public class EnterProductDetails {

    ImageView productImage;
    private final EditText productQuantityEditText;
    private final Interfaces.OnChooseProfilePicListener onChooseProfilePicListener; //for choosing profile pic
    private final Button addProductButton;
    private final Button deleteProductButton;
    private final EditText productNameEditText;
    private final EditText productDescriptionEditText;
    private final EditText productCategoryEditText;
    private final EditText productPriceEditText;

    public EnterProductDetails(View view, Interfaces.OnChooseProfilePicListener onChooseProfilePicListener, FragmentActivity currActivity) {
        this.productImage = view.findViewById(R.id.productPicture);
        this.productQuantityEditText = view.findViewById(R.id.quantityEditText);
        this.onChooseProfilePicListener = onChooseProfilePicListener;
        this.addProductButton = view.findViewById(R.id.doneButton);
        this.deleteProductButton = view.findViewById(R.id.deleteButton);
        this.productNameEditText = view.findViewById(R.id.nameEditText);
        this.productDescriptionEditText = view.findViewById(R.id.descriptionEditText);
        this.productCategoryEditText = view.findViewById(R.id.categoryEditText);
        this.productPriceEditText = view.findViewById(R.id.priceEditText);
        Button plusQuantityButton = view.findViewById(R.id.plusOneQuantityButton);
        Button minusQuantityButton = view.findViewById(R.id.minusOneQuantityButton);

        this.productImage.setOnClickListener(v -> {
            this.onChooseProfilePicListener.onImageClick(this.productImage);
        });

        plusQuantityButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(this.productQuantityEditText.getText().toString());
            quantity++;
            this.productQuantityEditText.setText(String.valueOf(quantity));
        });

        minusQuantityButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(this.productQuantityEditText.getText().toString());
            if (quantity > 0) {
                quantity--;
                this.productQuantityEditText.setText(String.valueOf(quantity));
            }
        });
    }

    public Button getAddProductButton() {
        return addProductButton;
    }

    public Button getDeleteProductButton() {
        return deleteProductButton;
    }

    public ImageView getProductImage() {
        return productImage;
    }

    public EditText getProductQuantityEditText() {
        return productQuantityEditText;
    }

    public EditText getProductNameEditText() {
        return productNameEditText;
    }

    public EditText getProductDescriptionEditText() {
        return productDescriptionEditText;
    }

    public EditText getProductCategoryEditText() {
        return productCategoryEditText;
    }

    public EditText getProductPriceEditText() {
        return productPriceEditText;
    }
}
