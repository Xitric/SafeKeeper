package dk.sdu.privacyenforcer.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Random;

import dk.sdu.privacyenforcer.R;
import dk.sdu.privacyenforcer.client.GeographicManager;


public class LocalObfuscationPromptFragment extends DialogFragment {

    private OnFragmentInteractionListener listener;

    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;

    private EditText editTextWidth;
    private EditText editTextHeight;
    private Button button;

    static LocalObfuscationPromptFragment newInstance() {
        return new LocalObfuscationPromptFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    currentLocation = location;
                    button.setEnabled(true);
                    Log.i("CurrentLocation", "Lat: " + location.getLatitude() + " Lon: " + location.getLongitude());
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_obfuscation_prompt, container, false);

        editTextWidth = view.findViewById(R.id.edit_text_width);
        editTextHeight = view.findViewById(R.id.edit_text_height);
        button = view.findViewById(R.id.button_ok);

        button.setOnClickListener(v -> okAction());

        return view;
    }

    private void okAction() {
        Random random = new Random();

        double areaWidth = getNumberFromEditTextView(editTextWidth);
        double areaHeight = getNumberFromEditTextView(editTextHeight);
        double randomAreaWidth = (random.nextDouble() * 2 * areaHeight - areaHeight) / 2;
        double randomAreaHeight = (random.nextDouble() * 2 * areaWidth - areaWidth) / 2;

        Location fakeLocation = GeographicManager.computeFakeLocation(currentLocation, randomAreaWidth, randomAreaHeight);

        if (listener != null && fakeLocation != null) {
            listener.onFragmentInteraction(fakeLocation);
        }
        dismiss();

    }

    private double getNumberFromEditTextView(EditText editText) {
        final double kmValue;

        try {
            kmValue = Double.valueOf(editText.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }

        return kmValue;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void checkPermission() {
        Activity context = getActivity();
        if (context != null) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Location fakeLocation);
    }

}
