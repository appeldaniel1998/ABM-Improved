package com.example.abm_improved.Clients;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.abm_improved.Clients.Adapters.ClientsRecyclerAdapter;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.Interfaces;
import com.example.abm_improved.Utils.Interfaces.OnFinishQueryInterface;

import java.lang.annotation.Inherited;

public class ClientsMainFragment extends Fragment {

    private Button addClientButton;

    private RecyclerView clientsRecyclerView;
    private ClientsRecyclerAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_clients_main, container, false);

        requireActivity().setTitle("Clients");
        progressBar = requireActivity().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        addClientButton = view.findViewById(R.id.addNewClientButton);

        recyclerViewLayoutManager = new LinearLayoutManager(requireActivity());
        clientsRecyclerView = view.findViewById(R.id.recyclerViewClients);
        clientsRecyclerView.hasFixedSize();
        clientsRecyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        clientsRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        DatabaseUtils.getAllClientsFromDatabase(new OnGetAllClients());

        addClientButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddNewClientFragment()).addToBackStack(null).commit();
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
                // Pass to the next fragment ---------->
                // Create a new instance of the next fragment and set its arguments
                Bundle args = new Bundle();
                args.putString("clientIndex", position + ""); // The uid of the client is passed to the next fragment
                EditClientFragment fragment = new EditClientFragment();
                fragment.setArguments(args);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                // <--------------------------
            });
        }
    }
}