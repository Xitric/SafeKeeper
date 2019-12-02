package dk.sdu.safekeeper;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Collections;
import java.util.Date;

import dk.sdu.safekeeper.repository.messenger.MessengerClient;
import dk.sdu.safekeeper.repository.messenger.MessengerService;
import dk.sdu.safekeeper.repository.messenger.vo.Attachment;
import dk.sdu.safekeeper.repository.messenger.vo.Coordinates;
import dk.sdu.safekeeper.repository.messenger.vo.Entry;
import dk.sdu.safekeeper.repository.messenger.vo.Message;
import dk.sdu.safekeeper.repository.messenger.vo.Messaging;
import dk.sdu.safekeeper.repository.messenger.vo.MessengerLocationPayload;
import dk.sdu.safekeeper.repository.messenger.vo.Payload;
import dk.sdu.safekeeper.repository.messenger.vo.Recipient;
import dk.sdu.safekeeper.repository.messenger.vo.Sender;
import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessengerActivity extends AppCompatActivity {

    private EditText textInput;
    private TextView messageLabel;
    private TextView latitudeLabel;
    private TextView longitudeLabel;

    private MessengerService messengerService;
    private FusedLocationProviderClient locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        textInput = findViewById(R.id.inp_message);
        messageLabel = findViewById(R.id.lbl_message);
        latitudeLabel = findViewById(R.id.lbl_lat);
        longitudeLabel = findViewById(R.id.lbl_lon);

        messengerService = MessengerClient.getService(this);
        locationManager = LocationServices.getFusedLocationProviderClient(this);
    }

    public void onSendAction(View sender) {
        String message = textInput.getText().toString();

        locationManager.getLastLocation().addOnSuccessListener(loc -> {
            if (loc != null) {
                //Constructs a dummy Messenger request
                MessengerLocationPayload payload = new MessengerLocationPayload("page", Collections.singletonList(
                        new Entry("123456789", new Date().getTime(), Collections.singletonList(
                                new Messaging(new Sender("987654321"), new Recipient("951378624"), new Date().getTime(), new Message(
                                        "mid.173962485", 1, Collections.singletonList(
                                        new Attachment(message, "http://", "location", new Payload(new Coordinates(
                                                loc.getLatitude(), loc.getLongitude()
                                        )))
                                )
                                ))
                        ))
                ));

                messengerService.sendLocationData(payload).enqueue(new Callback<MessengerLocationPayload>() {
                    @Override
                    @EverythingIsNonNull
                    public void onResponse(Call<MessengerLocationPayload> call, Response<MessengerLocationPayload> response) {

                    }

                    @Override
                    @EverythingIsNonNull
                    public void onFailure(Call<MessengerLocationPayload> call, Throwable t) {

                    }
                });
            }
        });
    }
}
