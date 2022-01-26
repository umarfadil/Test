package app.android.versioningsample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    String versionCode = null;
    TextView tvVersion;
    String API_VERSION = "https://raw.githubusercontent.com/umarfadil/Test/master/app/src/main/assets/checkVersion.json";
    String urlApps = "https://play.google.com/store/apps/details?id=irs.mobile.app.mdnreload2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvVersion = findViewById(R.id.versionInfo);
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionCode = packageInfo.versionName;
        tvVersion.setText(versionCode);

        getVersionInfo();
    }

    private void getVersionInfo() {
        AndroidNetworking.get(API_VERSION)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        String androidVersion = Objects.requireNonNull(response.optJSONObject("data")).optString("versionName");
                        if (versionCode.equals(androidVersion) ) {
                            //Toast.makeText(getApplicationContext(), "Versi android sama", Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("Aplikasi Anda Uptodate.")
                                    .setCancelable(false)
                                    .setPositiveButton("Oke", null)
                                    .show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "Versi ANdroid tidak sama", Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("Silahkan Update aplikasi kamu ke Versi " + androidVersion.toString())
                                    .setCancelable(false)
                                    .setPositiveButton("Update Aplikasi", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int id) {
                                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(urlApps));
                                            startActivity(i);
                                            finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
                        //Toast.makeText(getApplicationContext(), "response -> " + response.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}