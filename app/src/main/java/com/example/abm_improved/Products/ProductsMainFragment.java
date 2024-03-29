package com.example.abm_improved.Products;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.abm_improved.AppointmentTypes.AppointmentTypesMainFragmentDirections;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Products.Adapters.ProductsRecyclerAdapter;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.Interfaces;

public class ProductsMainFragment extends BaseFragment {

    private Button addProductButton;

    private RecyclerView productsRecyclerView;
    private ProductsRecyclerAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private ProgressBar progressBar;

    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_products_main, container, false);

        requireActivity().setTitle("Products");
        progressBar = requireActivity().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        addProductButton = view.findViewById(R.id.addNewProductButton);

        recyclerViewLayoutManager = new LinearLayoutManager(requireActivity());
        productsRecyclerView = view.findViewById(R.id.recyclerViewClients);
        productsRecyclerView.hasFixedSize();
        productsRecyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        productsRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        navController = NavHostFragment.findNavController(ProductsMainFragment.this);

        DatabaseUtils.getAllProductsFromDatabase(new OnGetAllProducts());

        addProductButton.setOnClickListener(v -> {
            navController.navigate(ProductsMainFragmentDirections.actionProductsMainFragmentToAddNewProductFragment()); // move to AddNewProduct fragment
        });

        return view;
    }


    private class OnGetAllProducts implements Interfaces.OnFinishQueryInterface {
        @Override
        public void onFinishQuery() {
            recyclerViewAdapter = new ProductsRecyclerAdapter(DatabaseUtils.getProducts());
            productsRecyclerView.setAdapter(recyclerViewAdapter);

            progressBar.setVisibility(View.GONE); // disable loading screen

            //onclick of each item in the recycle view (client in the list)
            recyclerViewAdapter.setOnItemClickListener(position -> {
                navController.navigate(ProductsMainFragmentDirections.actionProductsMainFragmentToEditProductFragment(position)); // move to EditAppointmentType fragment
            });
        }
    }
}