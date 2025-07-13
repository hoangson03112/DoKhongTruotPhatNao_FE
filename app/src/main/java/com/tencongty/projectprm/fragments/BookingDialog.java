package com.tencongty.projectprm.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.tencongty.projectprm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookingDialog {

    private Context context;
    private AlertDialog dialog;
    private BookingListener listener;

    private TextInputEditText etLicensePlate;
    private TextInputEditText etCheckInDateTime;
    private TextInputEditText etCheckOutDateTime;

    private Calendar checkInCalendar;
    private Calendar checkOutCalendar;
    private SimpleDateFormat dateTimeFormat;

    public interface BookingListener {
        void onBookingConfirmed(String licensePlate, Date checkInDateTime, Date checkOutDateTime);
    }

    public BookingDialog(Context context, BookingListener listener) {
        this.context = context;
        this.listener = listener;
        this.checkInCalendar = Calendar.getInstance();
        this.checkOutCalendar = Calendar.getInstance();
        this.dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        initDialog();
    }

    private void initDialog() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_booking, null);

        etLicensePlate = dialogView.findViewById(R.id.etLicensePlate);
        etCheckInDateTime = dialogView.findViewById(R.id.etCheckInDateTime);
        etCheckOutDateTime = dialogView.findViewById(R.id.etCheckOutDateTime);

        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        // Set up DateTime pickers
        etCheckInDateTime.setOnClickListener(v -> showDateTimePicker(checkInCalendar, etCheckInDateTime));
        etCheckOutDateTime.setOnClickListener(v -> showDateTimePicker(checkOutCalendar, etCheckOutDateTime));

        // Set up buttons
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> confirmBooking());

        // Create dialog
        dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(true)
                .create();
    }

    private void showDateTimePicker(Calendar calendar, TextInputEditText editText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // After selecting date, show time picker
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                            (TimePicker timeView, int hourOfDay, int minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);

                                // Update the EditText with selected datetime
                                editText.setText(dateTimeFormat.format(calendar.getTime()));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true);

                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void confirmBooking() {
        String licensePlate = etLicensePlate.getText().toString().trim();
        String checkInText = etCheckInDateTime.getText().toString().trim();
        String checkOutText = etCheckOutDateTime.getText().toString().trim();

        // Validate input
        if (licensePlate.isEmpty()) {
            etLicensePlate.setError("Vui lòng nhập biển số xe");
            return;
        }

        if (checkInText.isEmpty()) {
            etCheckInDateTime.setError("Vui lòng chọn thời gian check in");
            return;
        }

        if (checkOutText.isEmpty()) {
            etCheckOutDateTime.setError("Vui lòng chọn thời gian check out");
            return;
        }

        // Validate check out time is after check in time
        if (checkOutCalendar.getTimeInMillis() <= checkInCalendar.getTimeInMillis()) {
            Toast.makeText(context, "Thời gian check out phải sau thời gian check in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate check in time is not in the past
        if (checkInCalendar.getTimeInMillis() < System.currentTimeMillis()) {
            Toast.makeText(context, "Thời gian check in không thể là quá khứ", Toast.LENGTH_SHORT).show();
            return;
        }

        // All validations passed, call listener
        if (listener != null) {
            listener.onBookingConfirmed(licensePlate, checkInCalendar.getTime(), checkOutCalendar.getTime());
        }

        dialog.dismiss();
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}