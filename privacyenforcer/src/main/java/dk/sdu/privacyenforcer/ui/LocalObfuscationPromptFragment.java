package dk.sdu.privacyenforcer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import dk.sdu.privacyenforcer.R;


public class LocalObfuscationPromptFragment extends DialogFragment {

    static LocalObfuscationPromptFragment newInstance() {
        return new LocalObfuscationPromptFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_obfuscation_prompt, container, false);
        Button okButton = view.findViewById(R.id.button_ok);
        okButton.setOnClickListener(v -> dismiss());

        return view;
    }
}
