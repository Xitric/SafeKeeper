package dk.sdu.privacyenforcer.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import dk.sdu.privacyenforcer.R;
import dk.sdu.privacyenforcer.client.Privacy;

public class SendPermissionsModalFragment extends DialogFragment {

    private static final String BUNDLE_PERMISSIONS = "bundle_permissions";
    private static final String BUNDLE_EXPLANATIONS = "bundle_explanations";

    private SendPermissionsModalViewModel viewModel;
    private PermissionsModalListener listener;

    private TextView lblPermission;

    static SendPermissionsModalFragment newInstance(String[] permissions, String[] explanations) {
        SendPermissionsModalFragment fragment = new SendPermissionsModalFragment();

        Bundle bundle = new Bundle();
        bundle.putStringArray(BUNDLE_PERMISSIONS, permissions);
        bundle.putStringArray(BUNDLE_EXPLANATIONS, explanations);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PermissionsModalListener) {
            listener = (PermissionsModalListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(SendPermissionsModalViewModel.class);
        if (getArguments() != null) viewModel.init(
                getArguments().getStringArray(BUNDLE_PERMISSIONS),
                getArguments().getStringArray(BUNDLE_EXPLANATIONS));

        viewModel.isComplete().observe(this, complete -> {
            if (complete) {
                dismiss();
                listener.onPermissionSelectionResult(viewModel.getPermissions(), viewModel.getStates());
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_permissions_modal_fragment, container, false);
        view.findViewById(R.id.grantButton).setOnClickListener(v -> viewModel.grant());
        view.findViewById(R.id.denyButton).setOnClickListener(v -> viewModel.deny());
        view.findViewById(R.id.fakeButton).setOnClickListener(v -> viewModel.fake());

        lblPermission = view.findViewById(R.id.lbl_permission);
        TextView lblExplanation = view.findViewById(R.id.lbl_explanation);

        viewModel.getPermissionName().observe(this, this::setPermissionText);
        viewModel.getExplanation().observe(this, lblExplanation::setText);

        return view;
    }

    private void setPermissionText(String permission) {
        assert getContext() != null;
        int id = getResources().getIdentifier(permission, "string", getContext().getPackageName());
        String permissionLocalizedName = getString(id);

        lblPermission.setText(getString(R.string.permission_header, permissionLocalizedName));
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        listener.onPermissionSelectionCancelled(viewModel.getPermissions());
    }

    public interface PermissionsModalListener {
        void onPermissionSelectionCancelled(String[] permissions);

        void onPermissionSelectionResult(String[] permissions, Privacy.Mutation[] states);
    }
}
