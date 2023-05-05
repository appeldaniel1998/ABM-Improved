package com.example.abm_improved.Clients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm_improved.AppointmentTypes.AppointmentTypesMainFragment;
import com.example.abm_improved.AppointmentTypes.AppointmentTypesMainFragmentDirections;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Clients.Adapters.ClientsRecyclerAdapter;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.Interfaces;
import com.example.abm_improved.Clients.ClientsMainFragmentDirections;

public class ClientsMainFragment extends BaseFragment {

    private Button addClientButton;

    private RecyclerView clientsRecyclerView;
    private ClientsRecyclerAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private ProgressBar progressBar;

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_clients_main, container, false);

        requireActivity().setTitle("Clients");
        progressBar = requireActivity().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        navController = NavHostFragment.findNavController(ClientsMainFragment.this);

        addClientButton = view.findViewById(R.id.addNewClientButton);

        recyclerViewLayoutManager = new LinearLayoutManager(requireActivity());
        clientsRecyclerView = view.findViewById(R.id.recyclerViewClients);
        clientsRecyclerView.hasFixedSize();
        clientsRecyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        clientsRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        DatabaseUtils.getAllClientsFromDatabase(new OnGetAllClients());

        addClientButton.setOnClickListener(v -> {
            navController.navigate(ClientsMainFragmentDirections.actionClientsMainFragmentToAddNewClientFragment()); // move to AddNewClient fragment
        });
        return view;
    }

    private class OnGetAllClients implements Interfaces.OnFinishQueryInterface {
        @Override
        public void onFinishQuery() {
            recyclerViewAdapter = new ClientsRecyclerAdapter(DatabaseUtils.getClients());
            clientsRecyclerView.setAdapter(recyclerViewAdapter);

            progressBar.setVisibility(View.GONE); // disable loading screen

            //onclick of each item in the recycle view (client in the list)
            recyclerViewAdapter.setOnItemClickListener(position -> {
                navController.navigate(ClientsMainFragmentDirections.actionClientsMainFragmentToEditClientFragment(position)); // move to EditClient fragment
            });
        }
    }
}