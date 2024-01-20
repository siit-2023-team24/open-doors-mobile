package com.bookingapptim24.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bookingapptim24.R;
import com.bookingapptim24.additionalAdapters.BitmapAdapter;
import com.bookingapptim24.additionalAdapters.ImageAdapter;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.PendingAccommodationService;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.ActivityCreateAccommodationBinding;
import com.bookingapptim24.models.DateRange;
import com.bookingapptim24.models.DateRangeDTO;
import com.bookingapptim24.models.PendingAccommodationWhole;
import com.bookingapptim24.models.PendingAccommodationWholeEdited;
import com.bookingapptim24.models.SeasonalRate;
import com.bookingapptim24.models.SeasonalRateDTO;
import com.bookingapptim24.models.enums.AccommodationType;
import com.bookingapptim24.models.enums.Amenity;
import com.bookingapptim24.models.enums.Country;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccommodationActivity extends AppCompatActivity {

    private ActivityCreateAccommodationBinding binding;
    private SessionManager sessionManager;

    private Long id = null;
    private Long accommodationId = null;
    private PendingAccommodationWhole accommodation;


    private List<String> selectedAmenities;
    private List<Long> oldImages = new ArrayList<>();
    private List<Long> toDeleteImages = new ArrayList<>();
    private List<Bitmap> newImages = new ArrayList<>();

    private RadioButton isAutomaticTrue;
    private RadioButton isAutomaticFalse;
    private RadioButton isPricePerGuestTrue;
    private RadioButton isPricePerGuestFalse;
    private NumberPicker deadlinePicker;
    private NumberPicker minGuestsPicker;
    private NumberPicker maxGuestsPicker;
    private NumberPicker numberPicker;
    private Spinner countrySpinner;
    private Spinner typeSpinner;
    private EditText nameET;
    private EditText descriptionET;
    private EditText defaultPriceET;
    private EditText cityET;
    private EditText streetET;
    private ViewPager2 viewPager;
    private ViewPager2 viewPagerNewImages;

    private TextView instructions;
    private DatePicker availabilityDatePicker;
    private boolean isSelecting = false;
    private Timestamp availableRangeStart;
    private List<Timestamp> availableDates = new ArrayList<>();
    private List<DateRange> availability = new ArrayList<>();
    private ListView availabilityListView;

    private TextView seasonalRatesError;
    private DatePicker seasonalRatesDatePicker;
    private ListView seasonalRateListView;
    private EditText seasonalRatePrice;
    private List<Timestamp> priceDates = new ArrayList<>();
    private Map<String, Double> priceValues = new HashMap<>();
    private List<SeasonalRate> seasonalRates = new ArrayList<>();

    private List<String> availabilityText = new ArrayList<>();
    private List<String> seasonalRatesText = new ArrayList<>();
    private ArrayAdapter<String> availabilityAdapter;
    private ArrayAdapter<String> seasonalRatesAdapter;

    //todo bind amenities


    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedAmenities = new ArrayList<>();
        sessionManager = new SessionManager(getApplicationContext());

        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccommodationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong("id");
            accommodationId = extras.getLong("accommodationId");
            getData();
        }

        nameET = binding.name;
        descriptionET = binding.description;
        defaultPriceET = binding.defaultPrice;
        cityET = binding.city;
        streetET = binding.street;

        viewPager = binding.viewPager;
        viewPagerNewImages = binding.viewPagerNewImages;


        isAutomaticFalse = binding.automaticFalse;
        isAutomaticTrue = binding.automaticTrue;

        isAutomaticTrue.setChecked(true);
        isAutomaticTrue.setTag(true);
        isAutomaticFalse.setTag(false);

        isPricePerGuestTrue = binding.pricePerGuestTrue;
        isPricePerGuestFalse = binding.pricePerGuestFalse;

        isPricePerGuestTrue.setChecked(true);
        isPricePerGuestTrue.setTag(true);
        isPricePerGuestFalse.setTag(false);

        binding.backButton.setOnClickListener(v -> {
            finish();
        });

        deadlinePicker = binding.deadline;
        deadlinePicker.setMinValue(1);
        deadlinePicker.setMaxValue(100);

        minGuestsPicker = binding.minGuests;
        minGuestsPicker.setMinValue(1);
        minGuestsPicker.setMaxValue(100);

        maxGuestsPicker = binding.maxGuests;
        maxGuestsPicker.setMinValue(1);
        maxGuestsPicker.setMaxValue(100);

        numberPicker = binding.number;
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(1000000);

        instructions = binding.instructions;
        availabilityDatePicker = binding.availabilityDatePicker;
        availabilityListView = binding.availabilityListView;

        availabilityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, availabilityText);
        availabilityListView.setAdapter(availabilityAdapter);

        String[] countryArray = new String[Country.values().length];
        for (int i = 0; i < Country.values().length; i++) {
            countryArray[i] = Country.values()[i].getCountryName();
        }

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                countryArray);

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner = binding.countrySpinner;
        countrySpinner.setAdapter(countryAdapter);

        String[] typeArray = new String[AccommodationType.values().length];
        for (int i=0; i < AccommodationType.values().length; i++){
            typeArray[i] = AccommodationType.values()[i].getValue();
        }
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                typeArray);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner = binding.typeSpinner;
        typeSpinner.setAdapter(typeAdapter);

        if (id == null && accommodationId == null)
            initiateAmenities();

        availabilityDatePicker.init(availabilityDatePicker.getYear(), availabilityDatePicker.getMonth(), availabilityDatePicker.getDayOfMonth(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                    Timestamp timestamp = new Timestamp(calendar.getTime().getTime());
                    timestamp.setNanos(0);
                    availableDateSelected(timestamp);
                });

        seasonalRatesError = binding.seasonalRatesError;
        seasonalRatesDatePicker = binding.seasonalRatesDatePicker;
        seasonalRateListView = binding.seasonalRatesListView;
        seasonalRatePrice = binding.seasonalRatePrice;

        seasonalRatesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, seasonalRatesText);
        seasonalRateListView.setAdapter(seasonalRatesAdapter);

        seasonalRatesDatePicker.init(seasonalRatesDatePicker.getYear(), seasonalRatesDatePicker.getMonth(), seasonalRatesDatePicker.getDayOfMonth(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                    Timestamp timestamp = new Timestamp(calendar.getTime().getTime());
                    timestamp.setNanos(0);
                    priceDateSelected(timestamp);
                });

        binding.createButton.setOnClickListener(v -> {
            boolean valid = true;

            String name = binding.name.getText().toString();
            if (name.isEmpty()) {
                binding.name.setError("Accommodation name is required.");
                valid = false;
            }

            String description = binding.description.getText().toString();


            int minGuests = minGuestsPicker.getValue();
            int maxGuests = maxGuestsPicker.getValue();
            if (maxGuests < minGuests) {
                maxGuests = minGuests;
            }

            Spinner spinnerType = binding.typeSpinner;
            String type = spinnerType.getSelectedItem().toString();

            EditText priceText = binding.defaultPrice;
            double price = 0;
            try {
                price = Double.parseDouble(priceText.getText().toString());
                if (price < 0) {
                    valid = false;
                    priceText.setError("Please enter a valid non-negative price.");
                }
            }
            catch (Exception e) {
                valid = false;
                priceText.setError("Please enter a valid non-negative price.");
            }

            RadioGroup pricePerGuest = binding.isPricePerGuest;
            int selectedPriceOption = pricePerGuest.getCheckedRadioButtonId();
            RadioButton selectedPriceButton = findViewById(selectedPriceOption);
            boolean isPricePerGuest = (boolean)selectedPriceButton.getTag();

            Spinner spinnerCountry = binding.countrySpinner;
            String country = spinnerCountry.getSelectedItem().toString();

            EditText editTextCity = binding.city;
            String city = editTextCity.getText().toString().trim();
            if (city.isEmpty()) {
                editTextCity.setError("City is required.");
                valid=false;
            }

            EditText editTextStreet = binding.street;
            String street = editTextStreet.getText().toString().trim();
            if (street.isEmpty()) {
                editTextStreet.setError("Street is required");
                valid=false;
            }


            int number = numberPicker.getValue();

            int deadline = deadlinePicker.getValue();

            RadioGroup automatic = binding.isAutomatic;
            int selectedAutomaticOption = automatic.getCheckedRadioButtonId();
            RadioButton selectedAutomaticButton = findViewById(selectedAutomaticOption);
            boolean isAutomatic = (boolean)selectedAutomaticButton.getTag();


            if(!valid) {
                System.out.println("THERE RIGHT THERE - Look at that condescending smirk, see it on every guy at work.");
                Toast.makeText(CreateAccommodationActivity.this, "Please enter the data according to the validations.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (id != null && id == 0) id = null;
            if (accommodationId != null && accommodationId == 0) accommodationId = null;

            PendingAccommodationWholeEdited dto = new PendingAccommodationWholeEdited(
                    id,
                    accommodationId,
                    name,
                    description,
                    "",
                    selectedAmenities,
                    oldImages,
                    minGuests,
                    maxGuests,
                    type,
                    formAvailabilityDTOs(),
                    price,
                    isPricePerGuest,
                    formSeasonalRateDTOs(),
                    city,
                    country,
                    street,
                    number,
                    deadline,
                    isAutomatic,
                    sessionManager.getUsername(),
                    toDeleteImages
            );

//            dto.setAvailability(formAvailabilityDTOs());
//            dto.setSeasonalRates(formSeasonalRateDTOs());

            PendingAccommodationService service = ClientUtils.pendingAccommodationService;

            Call<PendingAccommodationWholeEdited> call = service.add(dto);
            call.enqueue(new Callback<PendingAccommodationWholeEdited>() {
                @Override
                public void onResponse(Call<PendingAccommodationWholeEdited> call, Response<PendingAccommodationWholeEdited> response) {
                    if (response.isSuccessful()) {
                        sendImages(response.body().getId());
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_accommodation_created), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CreateAccommodationActivity.this, getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<PendingAccommodationWholeEdited> call, Throwable t) {
                    // Handle network failure or exception
                }
            });


        });

        binding.backButton.setOnClickListener(v -> this.finish());

        binding.selectImageButton.setOnClickListener(v -> checkPermissions());
    }

    private void initiateAmenities() {
        String[] amenityArray = new String[Amenity.values().length];
        for (int i = 0; i < Amenity.values().length; i++) {
            amenityArray[i] = Amenity.values()[i].getAmenityName();
        }

        for (String amenity : amenityArray) {
            System.out.println(amenity);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(amenity);

            if (accommodation != null) {
                selectedAmenities = accommodation.getAmenities();
                if (accommodation.getAmenities().contains(amenity))
                    checkBox.setChecked(true);
            }

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedAmenities.add(amenity);
                } else {
                    selectedAmenities.remove(amenity);
                }
            });
            binding.gridLayout.addView(checkBox);
        }
    }

    private List<SeasonalRateDTO> formSeasonalRateDTOs() {
        List<SeasonalRateDTO> dtos = new ArrayList<>();
        for (SeasonalRate seasonalRate : seasonalRates) {
            dtos.add(new SeasonalRateDTO(seasonalRate.getPrice(),
                    new DateRangeDTO(
                            formatDate(seasonalRate.getPeriod().getStartDate()),
                            formatDate(seasonalRate.getPeriod().getEndDate()))));
        }
        return dtos;
    }

    private List<DateRangeDTO> formAvailabilityDTOs() {
        List<DateRangeDTO> dtos = new ArrayList<>();
        for (DateRange dateRange : availability) {
            dtos.add(new DateRangeDTO(formatDate(dateRange.getStartDate()),
                    formatDate(dateRange.getEndDate())));
        }
        return dtos;
    }

    private int find(List<Timestamp> dates, Timestamp date) {
        for(int i=0; i<dates.size(); i++) {
            if(dates.get(i).getTime() == date.getTime()) return i;
        }
        return -1;
    }

    private void availableDateSelected(Timestamp date) {
        Log.d(date.toString(), date.toString());
        int dateIndex = find(availableDates, date);

        if (isSelecting) {
            Log.d("Is selecting", "Is selecting");
            if (date.getTime() < availableRangeStart.getTime()) {
                Log.d("Wrong time", "Wrong time");
                instructions.setText(getString(R.string.instruction_set_future_date) + formatDate(availableRangeStart));
                return;
            }

            if (dateIndex != -1) {
                Log.d("Occupied", "Occupied");
                instructions.setText(getString(R.string.instruction_date) + formatDate(date) + getString(R.string.instruction_date_available) + formatDate(availableRangeStart) + ".");
                return;
            }

            isSelecting = false;

            Log.d("New date range", "New date range");
            instructions.setText(getString(R.string.instruction_accommodation_available) + formatDate(availableRangeStart) + getString(R.string.instruction_to) + formatDate(date) + ".");
            populateWithDates(availableRangeStart, date);
        } else {
            if (dateIndex != -1) {
                Log.d("Deleted date", "Deleted date");
                instructions.setText(getString(R.string.instruction_date) + formatDate(date) + getString(R.string.instruction_deleted));
                availableDates.remove(dateIndex);
            } else {
                Log.d("Selected start", "Selected start");
                isSelecting = true;
                instructions.setText(getString(R.string.instruction_date) + formatDate(date) + getString(R.string.instruction_start_date_selected));
                availableRangeStart = date;
                return;
            }
        }

        generateAvailability();
        updateAvailabilityView();
        Log.d("amogus", availability.toString());
        Log.d("amogus2", availableDates.toString());
    }

    private void populateWithDates(Timestamp startDate, Timestamp endDate) {
        Calendar counterDate = Calendar.getInstance();
        counterDate.setTime(startDate);

        while (!counterDate.getTime().after(endDate)) {
            int dateIndex = find(availableDates, new Timestamp(counterDate.getTime().getTime()));
            if (dateIndex == -1) {
                availableDates.add(new Timestamp(counterDate.getTime().getTime()));
            }
            counterDate.add(Calendar.DATE, 1);
        }
    }

    private String formatDate(Timestamp date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private Timestamp getTimestamp(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new Timestamp(sdf.parse(date).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateAvailability() {
        availability.clear();

        if (availableDates.isEmpty()) return;

        availableDates.sort((a, b) -> a.compareTo(b));

        int i = 0;
        int j = 0;
        int k = 0;
        while (i < availableDates.size()) {
            while (j < availableDates.size() && areDatesWithinRange(availableDates, k, j)) {
                j++;
                if (j-1 != i) k++;
            }
            availability.add(new DateRange(availableDates.get(i), availableDates.get(j-1)));
            i = j;
            k = i;
        }
    }

    private void updateAvailabilityView() {
        availabilityText.clear();

        for (DateRange range : availability) {
            availabilityText.add("Available period: " +
                    formatDate(range.getStartDate()) +
                    " to " +
                    formatDate(range.getEndDate()));
        }
        Log.d("dateRangesText", availabilityText.toString());
        availabilityAdapter.notifyDataSetChanged();
    }

    private void priceDateSelected(Timestamp date) {
        double price = 0;
        try {
            price = Double.parseDouble(seasonalRatePrice.getText().toString());
            if (price < 0) {
                seasonalRatesError.setText("Please enter a valid non-negative price.");
                return;
            }
        }
        catch (Exception e) {
            seasonalRatesError.setText("Please enter a valid non-negative price.");
            return;
        }
        seasonalRatesError.setText("");

        int dateIndex = find(priceDates, date);
        if (dateIndex == -1) {
            priceValues.put(formatDate(date), price);
            priceDates.add(date);
        } else {
            priceValues.remove(formatDate(date));
            priceDates.remove(dateIndex);
        }
        generateSeasonalRates();
        updateSeasonalRatesView();
    }

    private void generateSeasonalRates(){
        seasonalRates.clear();
        if(this.priceDates.size()==0) return;

        priceDates.sort((a, b) -> a.compareTo(b));
        int i = 0;
        int j = 0;
        int k = 0;
        while (i<priceDates.size()) {
            while(j<priceDates.size() && areDatesWithinRange(priceDates, k, j) && areSamePrice(i,j)) {
                j++;
                if (j-1 != i) k++;
            }
            seasonalRates.add(new SeasonalRate(priceValues.get(formatDate(priceDates.get(i))), new DateRange(priceDates.get(i), priceDates.get(j-1))));
            i = j;
            k = i;
        }
    }

    private void updateSeasonalRatesView() {
        seasonalRatesText.clear();

        for (SeasonalRate seasonalRate : seasonalRates) {
            seasonalRatesText.add("A new price of" +
                    seasonalRate.getPrice() + "from" +
                    formatDate(seasonalRate.getPeriod().getStartDate()) + "to" +
                    formatDate(seasonalRate.getPeriod().getEndDate()) + ".");
        }

        seasonalRatesAdapter.notifyDataSetChanged();
    }

    private boolean areSamePrice(int i, int j) {
        Log.d("dict", priceValues.toString());
        boolean yes = priceValues.get(formatDate(priceDates.get(i))).equals(priceValues.get(formatDate(priceDates.get(j))));
        Log.d("yes", Boolean.toString(yes));
        return yes;
    }

    private boolean areDatesWithinRange(List<Timestamp> dates, int k, int j) {
        long range = 1000 * 60 * 60 * 24;
        return (dates.get(j).getTime() - dates.get(k).getTime() <= range);
    }



    private void getData() {
        Log.d("OpenDoors", "Getting accommodation details: " + id + ", " + accommodationId);
        if (id > 0) {
            Call<PendingAccommodationWhole> call = ClientUtils.pendingAccommodationService.getById(id);
            call.enqueue(new Callback<PendingAccommodationWhole>() {
                @Override
                public void onResponse(Call<PendingAccommodationWhole> call, Response<PendingAccommodationWhole> response) {
                    if (response.code() == 200) {
                        Log.d("OpenDoors", "Received pending accommodation: " + id);
                        accommodation = response.body();
                        if (accommodation.getImages() != null)
                            oldImages = accommodation.getImages();
                        setData();
                    }
                    else Log.d("OpenDoors","Message received: " + response.code());
                }
                @Override
                public void onFailure(Call<PendingAccommodationWhole> call, Throwable t) {
                    Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
                }
            });
        }
        else {
            Call<PendingAccommodationWhole> call = ClientUtils.accommodationService.getForEdit(accommodationId);
            call.enqueue(new Callback<PendingAccommodationWhole>() {
                @Override
                public void onResponse(Call<PendingAccommodationWhole> call, Response<PendingAccommodationWhole> response) {
                    if (response.code() == 200) {
                        Log.d("OpenDoors", "Received accommodation: " + accommodationId);
                        accommodation = response.body();
                        if (accommodation.getImages() != null)
                            oldImages = accommodation.getImages();
                        setData();
                    }
                    else Log.d("OpenDoors","Message received: " + response.code());
                }
                @Override
                public void onFailure(Call<PendingAccommodationWhole> call, Throwable t) {
                    Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
                }
            });
        }
    }

    private void setData() {
        if (accommodation.isAutomatic()) {
            isAutomaticTrue.setChecked(true);
            isAutomaticFalse.setChecked(false);
        } else {
            isAutomaticTrue.setChecked(false);
            isAutomaticFalse.setChecked(true);
        }

        if (accommodation.isPricePerGuest()) {
            isPricePerGuestTrue.setChecked(true);
            isPricePerGuestFalse.setChecked(false);
        } else {
            isPricePerGuestTrue.setChecked(false);
            isPricePerGuestFalse.setChecked(true);
        }
        deadlinePicker.setValue(accommodation.getDeadline());
        minGuestsPicker.setValue(accommodation.getMinGuests());
        maxGuestsPicker.setValue(accommodation.getMaxGuests());
        numberPicker.setValue(accommodation.getNumber());

        Country accommodationCountry = Country.fromString(accommodation.getCountry());
        countrySpinner.setSelection(Arrays.asList(Country.values()).indexOf(accommodationCountry));

        AccommodationType accommodationType = AccommodationType.fromString(accommodation.getType());
        typeSpinner.setSelection(Arrays.asList(AccommodationType.values()).indexOf(accommodationType));

        nameET.setText(accommodation.getName());
        descriptionET.setText(accommodation.getDescription());
        defaultPriceET.setText(String.valueOf(accommodation.getPrice()));
        cityET.setText(accommodation.getCity());
        streetET.setText(accommodation.getStreet());

        initiateAmenities();

        if (accommodation.getImages() != null && accommodation.getImages().size() > 0) {
            viewPager.setAdapter(new ImageAdapter(getApplicationContext(), accommodation.getImages()));
        }
        retrieveAvailability();
        updateAvailabilityView();

        retrieveSeasonalRates();
        updateSeasonalRatesView();
    }

    public void retrieveAvailability() {
        this.availability = accommodation.getAvailability();
        this.availableDates = new ArrayList<>();
        for (DateRange period : availability) {
            Timestamp startTimestamp = period.getStartDate();
            Timestamp endTimestamp = period.getEndDate();
            Timestamp counterTimestamp = new Timestamp(startTimestamp.getTime());

            while (counterTimestamp.getTime() <= endTimestamp.getTime()) {
                this.availableDates.add(new Timestamp(counterTimestamp.getTime()));
                counterTimestamp.setTime(counterTimestamp.getTime() + 24 * 60 * 60 * 1000); // Add one day
            }
        }
    }

    private void retrieveSeasonalRates() {
        this.seasonalRates = accommodation.getSeasonalRates();

        for (SeasonalRate seasonalRate: seasonalRates) {
            Timestamp start = seasonalRate.getPeriod().getStartDate();
            Timestamp end = seasonalRate.getPeriod().getEndDate();
            Timestamp counter = new Timestamp(start.getTime());

            while (counter.getTime() <= end.getTime()) {
                Timestamp newDate = new Timestamp(counter.getTime());

                this.priceDates.add(newDate);
                this.priceValues.put(formatDate(newDate), seasonalRate.getPrice());

                counter.setTime(counter.getTime() + 24 * 60 * 60 * 1000);
            }
        }
    }


    public void deleteImages(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getString(R.string.dialog_delete_images))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), (dialogInterface, id) -> {
                    toDeleteImages = oldImages;
                    newImages.clear();
                    viewPager.setAdapter(new ImageAdapter(getApplicationContext(), new ArrayList<>()));
                    Log.d("OpenDoors", "Images set to delete.");
                    Toast.makeText(this, getString(R.string.toast_delete_images), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(getString(R.string.no), (dialogInterface, id) -> dialogInterface.cancel());
        dialog.create().show();
    }

    private void checkPermissions() {
        Log.d("OpenDoors", "Checking permissions.");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, getString(R.string.toast_no_camera_permission), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectImage() {
        Intent imageChooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageChooserIntent.setType("image/*");

        imageChooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooserIntent = Intent.createChooser(imageChooserIntent, "Select image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null) {
            if (data.getClipData() != null) {
                int countSelected = data.getClipData().getItemCount();
                for (int i=0; i < countSelected; i++) {
                    Uri selectedImageUri = data.getClipData().getItemAt(i).getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        newImages.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                newImages.add(bitmap);
            }
            Toast.makeText(this, "New image added.", Toast.LENGTH_SHORT).show();
            viewPagerNewImages.setAdapter(new BitmapAdapter(getApplicationContext(), newImages));
        }
        else {
            Log.e("OpenDoors", "RESULT CODE: " + resultCode);
        }
    }

    private void sendImages(Long pendingId) {
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < newImages.size(); i++) {
            Bitmap bitmap = newImages.get(i);
            String filename = "image" + System.currentTimeMillis() + ".png";
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] bytes = out.toByteArray();

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), bytes);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("images", filename, requestBody);
            imageParts.add(imagePart);

            Call<ResponseBody> call = ClientUtils.pendingAccommodationService.sendImages(pendingId, imageParts);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(CreateAccommodationActivity.this, "Uploading images complete", Toast.LENGTH_SHORT).show();
                        Log.d("OpenDoors", "Sending accommodation images completed");
                    } else {
                        Toast.makeText(CreateAccommodationActivity.this, "Uploading images failed", Toast.LENGTH_SHORT).show();
                        Log.d("OpenDoors", "Sending accommodation images failed: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("OpenDoors", "Sending accommodation images failed");
                }
            });

        }
    }
}