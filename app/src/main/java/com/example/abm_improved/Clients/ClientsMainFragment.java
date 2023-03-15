package com.example.abm_improved.Clients;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.abm_improved.Appointments.AppointmentsMainFragment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.OnFinishQueryInterface;

public class ClientsMainFragment extends Fragment {

    private Button addClientButton;

    private RecyclerView clientsRecyclerView;
    private ClientsRecycleAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_clients_main, container, false);

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
            Bundle bundle = new Bundle();
            AddNewClientFragment addNewClientFragment = new AddNewClientFragment();
            addNewClientFragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, addNewClientFragment).addToBackStack(null).commit();
        });
        return view;
    }

    private class OnGetAllClients implements OnFinishQueryInterface {
        @Override
        public void onFinishQuery() {
            recyclerViewAdapter = new ClientsRecycleAdapter(DatabaseUtils.getClients());
            clientsRecyclerView.setAdapter(recyclerViewAdapter);

            progressBar.setVisibility(View.GONE); // disable loading screen

            //onclick of each item in the recycle view (client in the list)
            recyclerViewAdapter.setOnItemClickListener(position -> {
//                Intent myIntent = new Intent(ClientsMainActivity.this, SingleClientViewActivity.class);
//                myIntent.putExtra("clientUID", BackendHandling.clients.get(position).getUid()); //Optional parameters
//                ClientsMainActivity.this.startActivity(myIntent);
            });
        }
    }
}