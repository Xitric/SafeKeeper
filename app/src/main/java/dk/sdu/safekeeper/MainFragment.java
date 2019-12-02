package dk.sdu.safekeeper;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import dk.sdu.privacyenforcer.client.Privacy;
import dk.sdu.privacyenforcer.ui.SettingsFragment;

public class MainFragment extends Fragment {

    private OnMainFragmentInteractionListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnMainFragmentInteractionListener) {
            listener = (OnMainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnMainFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button settingsButton = view.findViewById(R.id.btn_settings);
        Button requestPermissionsButton = view.findViewById(R.id.btn_request_permissions);
        Button weatherButton = view.findViewById(R.id.btn_weather);
        Button obfuscationButton = view.findViewById(R.id.btn_local_obfuscation);

        settingsButton.setOnClickListener(v -> onSettingsAction());
        requestPermissionsButton.setOnClickListener(v -> onRequestPermissionsAction());
        weatherButton.setOnClickListener(v -> onWeatherAction());
        obfuscationButton.setOnClickListener(v -> onLocalObfuscationAction());

        return view;

    }

    public void onSettingsAction() {
        SettingsFragment settingsFragment = SettingsFragment.newInstance(R.id.fragment_containerx);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_containerx, settingsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void onWeatherAction() {
        Intent startWeather = new Intent(getActivity(), WeatherActivity.class);
        startActivity(startWeather);
    }

    public void onLocalObfuscationAction() {
        listener.onLocalObfuscation();
    }


    public void onRequestPermissionsAction() {
        //Request permissions for sending information over a network

        listener.onRequestPermissions(
                new String[]{
                        Privacy.Permission.SEND_LOCATION,
                        Privacy.Permission.SEND_ACCELERATION,
                        Privacy.Permission.SEND_CONTACTS
                }, new String[]{
                        "For informing analytics frameworks about where you are so we can stalk you!",
                        "For inferring your passwords so we can hack your bank account!",
                        "For sending spam e-mails to all of your friends!"
                });
    }

    public interface OnMainFragmentInteractionListener {
        void onLocalObfuscation();

        void onRequestPermissions(String[] permissions, String[] explanations);
    }

}
