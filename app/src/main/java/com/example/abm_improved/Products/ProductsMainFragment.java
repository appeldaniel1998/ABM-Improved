package com.example.abm_improved.Products;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abm_improved.AppointmentTypes.AddNewAppointmentTypeFragment;
import com.example.abm_improved.AppointmentTypes.AppointmentTypesMainFragment;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;

public class ProductsMainFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_products_main, container, false);


        return view;
    }
}