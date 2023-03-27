package com.example.abm_improved.Appointments.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm_improved.DataClasses.Appointment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.Interfaces;
import com.example.abm_improved.Utils.PopupDatePicker;
import com.example.abm_improved.Utils.PopupTimePicker;

import java.util.ArrayList;

public class AppointmentsRecyclerAdapter extends RecyclerView.Adapter<AppointmentsRecyclerAdapter.AppointmentViewHolder> {
    private ArrayList<Appointment> appointments;
    private OnItemClickListener clickListener; //instance of interface below

    //Interface for onclick listener
    public interface OnItemClickListener {
        void onItemClick (int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    //This is a static class that is a subclass of RecyclerView.ViewHolder
    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        public TextView appointmentType; // Parameters of the XML item
        public TextView appointmentDate;
        public TextView appointmentTime;

        public AppointmentViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            appointmentType = itemView.findViewById(R.id.appointmentType);
            appointmentDate = itemView.findViewById(R.id.date);
            appointmentTime = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(v -> {
                if (listener != null)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public AppointmentsRecyclerAdapter(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    //This method is called when the recycler view is created and it creates the view holder
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_appointment_item, parent, false);
        AppointmentViewHolder appointmentViewHolder = new AppointmentViewHolder(view, clickListener);
        return appointmentViewHolder;
    }

    @Override
    //This method is called when the recycler view is created and it binds the view holder to the data
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment currAppointment = this.appointments.get(position);
        if (DatabaseUtils.getAppointments().size() == 0)
            DatabaseUtils.getAllAppointmentTypesFromDatabase(new OnGetAppointmentTypes(holder, currAppointment));
        else
            holder.appointmentType.setText(DatabaseUtils.findAppointmentType(currAppointment.getAppointmentTypeUid()).getTypeName());
        holder.appointmentDate.setText(PopupDatePicker.intToString(Integer.parseInt(currAppointment.getDate())));
        holder.appointmentTime.setText(PopupTimePicker.getTimeString(Integer.parseInt(currAppointment.getTime())));
    }

    @Override
    public int getItemCount() {
        return this.appointments.size();
    }

    private class OnGetAppointmentTypes implements Interfaces.OnFinishQueryInterface {
        private AppointmentViewHolder holder;
        private Appointment currAppointment;
        public OnGetAppointmentTypes(AppointmentViewHolder holder, Appointment currAppointment) {
            this.holder = holder;
            this.currAppointment = currAppointment;
        }
        @Override
        public void onFinishQuery() {
            holder.appointmentType.setText(DatabaseUtils.findAppointmentType(currAppointment.getAppointmentTypeUid()).getTypeName());
        }
    }
}
