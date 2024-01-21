package com.bookingapptim24.fragments.financialReports;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.AccommodationHost;
import com.bookingapptim24.models.AccommodationIdReport;
import com.bookingapptim24.models.DateRangeReport;
import com.bookingapptim24.models.DateRangeReportParams;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FinancialReportsFragment extends Fragment {
    private ArrayList<DateRangeReport> dateRangeReports;
    private ArrayList<AccommodationIdReport> accommodationIdReports;
    private View view;
    private SessionManager sessionManager;
    private Timestamp selectedStartDate;
    private Timestamp selectedEndDate;
    private PieChart reservationNumChart;
    private PieChart profitChart;
    private PieChart accommodationIdReservationNumChart;
    private PieChart accommodationIdProfitChart;
    private ArrayList<AccommodationHost> hostAccommodations;

    public FinancialReportsFragment() {}

    public static FinancialReportsFragment newInstance() {
        FinancialReportsFragment fragment = new FinancialReportsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_financial_reports, container, false);
        reservationNumChart = view.findViewById(R.id.dateRangeReservationsChart);
        profitChart = view.findViewById(R.id.dateRangeProfitChart);
        accommodationIdReservationNumChart = view.findViewById(R.id.accommodationIdReservationsChart);
        accommodationIdProfitChart = view.findViewById(R.id.accommodationIdProfitChart);

        loadAccommodationNames();

        Button getDateRangeReportButton = view.findViewById(R.id.getDateRangeReportButton);
        Button getAccommodationIdButton = view.findViewById(R.id.getAccommodationIdReportButton);

        makeStartDatePicker();
        makeEndDatePicker();
        getDateRangeReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateRangeReport();
            }
        });

        getAccommodationIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccommodationIdReport();
            }
        });

        return view;
    }

    private void getDateRangeReport() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(selectedStartDate == null || selectedEndDate == null) {
            showSnackbar("Please select start and end date!");
            return;
        }
        DateRangeReportParams params = new DateRangeReportParams(sessionManager.getUserId(), dateFormat.format(selectedStartDate), dateFormat.format(selectedEndDate));
        Call<ArrayList<DateRangeReport>> call = ClientUtils.financialReportService.getDateRangeReport(params);
        call.enqueue(new Callback<ArrayList<DateRangeReport>>() {
            @Override
            public void onResponse(Call<ArrayList<DateRangeReport>> call, Response<ArrayList<DateRangeReport>> response) {
                if (response.isSuccessful()) {
                    dateRangeReports = response.body();
                    loadDateRangeReportsIntoTable();
                    loadDateRangeReportsIntoCharts();
                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DateRangeReport>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });

    }

    private void getAccommodationIdReport() {
        Long accommodationId = getSelectedAccommodationId();
        Call<ArrayList<AccommodationIdReport>> call = ClientUtils.financialReportService.getAccommodationIdReport(accommodationId);
        call.enqueue(new Callback<ArrayList<AccommodationIdReport>>() {
            @Override
            public void onResponse(Call<ArrayList<AccommodationIdReport>> call, Response<ArrayList<AccommodationIdReport>> response) {
                if (response.isSuccessful()) {
                    accommodationIdReports = response.body();
                    loadAccommodationIdReportsIntoTable();
                    loadAccommodationIdReportsIntoCharts();
                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AccommodationIdReport>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private Long getSelectedAccommodationId() {
        Spinner spinner = (Spinner) view.findViewById(R.id.accommodationNameSpinner);
        String accommodationName = (String) spinner.getSelectedItem();
        for(AccommodationHost a: hostAccommodations) {
            if(a.getName().equals(accommodationName))
                return a.getId();
        }
        return null;
    }

    private void loadAccommodationNames() {
        Call<ArrayList<AccommodationHost>> call = ClientUtils.accommodationService.getForHost(sessionManager.getUserId());

        call.enqueue(new Callback<ArrayList<AccommodationHost>>() {
            @Override
            public void onResponse(Call<ArrayList<AccommodationHost>> call, Response<ArrayList<AccommodationHost>> response) {
                if (response.isSuccessful()) {
                    hostAccommodations = response.body();
                    addNamesToSpinner(hostAccommodations);
                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AccommodationHost>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void addNamesToSpinner(ArrayList<AccommodationHost> hostAccommodations) {
        Spinner dynamicSpinner = (Spinner) view.findViewById(R.id.accommodationNameSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dynamicSpinner.setAdapter(adapter);
        for(AccommodationHost a : hostAccommodations) {
            adapter.add(a.getName());
        }
        adapter.notifyDataSetChanged();
    }

    private void loadDateRangeReportsIntoTable() {
        DataTable dataTable = view.findViewById(R.id.data_table);

        DataTableHeader header = new DataTableHeader.Builder()
                .item("ID", 1)
                .item("Name", 3)
                .item("Number of reservations", 2)
                .item("Profit", 2)
        .build();

        ArrayList<DataTableRow> rows = new ArrayList<>();
        for(int i = 0; i < dateRangeReports.size(); i++) {
            DataTableRow row = new DataTableRow.Builder()
                    .value(dateRangeReports.get(i).getAccommodationId().toString())
                    .value(dateRangeReports.get(i).getAccommodationName())
                    .value(String.valueOf(dateRangeReports.get(i).getNumOfReservations()))
                    .value(String.valueOf(dateRangeReports.get(i).getProfit()))
            .build();
            rows.add(row);
        }
        dataTable.setHeaderTextSize(15);
        dataTable.setRowTextSize(15);
        dataTable.setHeader(header);
        dataTable.setRows(rows);
        dataTable.inflate(requireContext());
    }

    private void loadAccommodationIdReportsIntoTable() {
        DataTable dataTable = view.findViewById(R.id.data_table2);

        DataTableHeader header = new DataTableHeader.Builder()
                .item("Month", 3)
                .item("Number of reservations", 2)
                .item("Profit", 2)
                .build();

        ArrayList<DataTableRow> rows = new ArrayList<>();
        for(AccommodationIdReport report : accommodationIdReports) {
            DataTableRow row = new DataTableRow.Builder()
                    .value(report.getMonth())
                    .value(String.valueOf(report.getNumOfReservations()))
                    .value(String.valueOf(report.getProfit()))
                    .build();
            rows.add(row);
        }
        dataTable.setHeaderTextSize(15);
        dataTable.setRowTextSize(15);
        dataTable.setHeader(header);
        dataTable.setRows(rows);
        dataTable.inflate(requireContext());
    }

    private void loadDateRangeReportsIntoCharts() {
        ArrayList<PieEntry> reservationNumPieEntries = new ArrayList<>();
        ArrayList<PieEntry> profitPieEntries = new ArrayList<>();
        String label = "type";

        Map<String, Integer> reservationNum = new HashMap<>();
        Map<String, Double> profit = new HashMap<>();
        for(DateRangeReport report: dateRangeReports) {
            reservationNum.put(report.getAccommodationName(), report.getNumOfReservations());
            profit.put(report.getAccommodationName(), report.getProfit());
        }

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));

        for(String type: reservationNum.keySet()){
            reservationNumPieEntries.add(new PieEntry(reservationNum.get(type).floatValue(), type));
            profitPieEntries.add(new PieEntry(profit.get(type).floatValue(), type));
        }

        PieDataSet reservationNumPieDataSet = new PieDataSet(reservationNumPieEntries,label);
        reservationNumPieDataSet.setValueTextSize(12f);
        reservationNumPieDataSet.setColors(colors);

        PieDataSet profitDataSet = new PieDataSet(profitPieEntries, label);
        profitDataSet.setValueTextSize(12f);
        profitDataSet.setColors(colors);

        PieData pieDataReservationNum = new PieData(reservationNumPieDataSet);
        pieDataReservationNum.setDrawValues(true);
        pieDataReservationNum.setValueFormatter(new PercentFormatter());

        PieData pieDataProfit = new PieData(profitDataSet);
        pieDataProfit.setDrawValues(true);
        pieDataProfit.setValueFormatter(new PercentFormatter());

        reservationNumChart.setData(pieDataReservationNum);
        reservationNumChart.setUsePercentValues(true);
        reservationNumChart.getDescription().setEnabled(false);
        reservationNumChart.invalidate();

        profitChart.setData(pieDataProfit);
        profitChart.setUsePercentValues(true);
        profitChart.getDescription().setEnabled(false);
        profitChart.invalidate();
    }

    private void loadAccommodationIdReportsIntoCharts() {
        ArrayList<PieEntry> reservationNumPieEntries = new ArrayList<>();
        ArrayList<PieEntry> profitPieEntries = new ArrayList<>();
        String label = "type";

        Map<String, Integer> reservationNum = new HashMap<>();
        Map<String, Double> profit = new HashMap<>();
        for(AccommodationIdReport report: accommodationIdReports) {
            if(report.getNumOfReservations() == 0) continue;
            reservationNum.put(report.getMonth(), report.getNumOfReservations());
            profit.put(report.getMonth(), report.getProfit());
        }

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));

        for(String type: reservationNum.keySet()){
            reservationNumPieEntries.add(new PieEntry(reservationNum.get(type).floatValue(), type));
            profitPieEntries.add(new PieEntry(profit.get(type).floatValue(), type));
        }

        PieDataSet reservationNumPieDataSet = new PieDataSet(reservationNumPieEntries,label);
        reservationNumPieDataSet.setValueTextSize(12f);
        reservationNumPieDataSet.setColors(colors);

        PieDataSet profitDataSet = new PieDataSet(profitPieEntries, label);
        profitDataSet.setValueTextSize(12f);
        profitDataSet.setColors(colors);

        PieData pieDataReservationNum = new PieData(reservationNumPieDataSet);
        pieDataReservationNum.setDrawValues(true);
        pieDataReservationNum.setValueFormatter(new PercentFormatter());

        PieData pieDataProfit = new PieData(profitDataSet);
        pieDataProfit.setDrawValues(true);
        pieDataProfit.setValueFormatter(new PercentFormatter());

        accommodationIdReservationNumChart.setData(pieDataReservationNum);
        accommodationIdReservationNumChart.setUsePercentValues(true);
        accommodationIdReservationNumChart.getDescription().setEnabled(false);
        accommodationIdReservationNumChart.invalidate();

        accommodationIdProfitChart.setData(pieDataProfit);
        accommodationIdProfitChart.setUsePercentValues(true);
        accommodationIdProfitChart.getDescription().setEnabled(false);
        accommodationIdProfitChart.invalidate();
    }

    private void showSnackbar(String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public void makeStartDatePicker() {
        Button startDateButton = view.findViewById(R.id.startDateReportsButton);

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        CalendarConstraints.DateValidator dateValidator = new CalendarConstraints.DateValidator() {

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(@NonNull Parcel dest, int flags) {

            }

            @Override
            public boolean isValid(long date) {
                if(selectedEndDate != null) {
                    if(date > selectedEndDate.getTime()) return false;
                }

                return true;
            }
        };
        constraintsBuilder.setValidator(dateValidator);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A START DATE");
        materialDateBuilder.setCalendarConstraints(constraintsBuilder.build());

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        selectedStartDate = new Timestamp(selection);
                        TextView startDate = view.findViewById(R.id.selectedStartDateTextView);
                        startDate.setText(selectedStartDate.toString());
                    }
                }
        );
    }

    public void makeEndDatePicker() {
        Button endDateButton = view.findViewById(R.id.endDateReportsButton);

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        CalendarConstraints.DateValidator dateValidator = new CalendarConstraints.DateValidator() {

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(@NonNull Parcel dest, int flags) {

            }

            @Override
            public boolean isValid(long date) {
                if(selectedStartDate != null) {
                    if(date < selectedStartDate.getTime()) return false;
                }

                return true;
            }
        };
        constraintsBuilder.setValidator(dateValidator);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT AN END DATE");
        materialDateBuilder.setCalendarConstraints(constraintsBuilder.build());

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        selectedEndDate = new Timestamp(selection);
                        TextView endDate = view.findViewById(R.id.selectedEndDateTextView);
                        endDate.setText(selectedEndDate.toString());
                    }
                }
        );
    }
}