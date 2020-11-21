package za.co.dwindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONObject;

import za.co.dwindle.dialogs.PermissionCallback;
import za.co.dwindle.nav.CompressFrag;
import za.co.dwindle.policies.PrivacyPolicyFrag;
import za.co.dwindle.utils.ConstantUtils;
import za.co.dwindle.utils.DTUtils;
import za.co.dwindle.utils.DeviceMemoryUtils;
import za.co.dwindle.utils.DialogUtils;
import za.co.dwindle.utils.FragmentUtils;
import za.co.dwindle.utils.GeneralUtils;
import za.co.dwindle.utils.SharedPreferencesUtils;
import java.lang.Math;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity
{
    private final String TAG = "MainActivity";

    private TextView txtUsage;
    private ProgressBar progressBar;

    private Timer timer;
    private TimerTask timerTask;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireUI();

        displayPrivacyPolicy();

        this.handler = new Handler();
        this.timerTask = new TimerTask() {
            @Override
            public void run()
            {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        displayUsedStorage();
                    }
                });

            }
        };

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(this.timerTask, 0, ConstantUtils.STORAGE_REFRESH_TIME);
    }

    private void displayPrivacyPolicy()
    {
        try
        {
            JSONObject jsonObject = SharedPreferencesUtils.get(this, SharedPreferencesUtils.PRIVACY_POLICY_ACCEPTANCE);
            if(jsonObject == null)
            {
                setNavIcons(false, false);

                PrivacyPolicyFrag privacyPolicyFrag = new PrivacyPolicyFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), privacyPolicyFrag, R.id.fragContainer, getSupportActionBar(), "Privacy Policy", true, false, true, null);
            }else
            {
                DialogUtils.createAlertDialog(this, "Disclosure", "Dwindle compresses images and replaces the original image with the compressed one. Therefore, please save your original images in a safe please before compressing it.", false).show();

                CompressFrag compressFrag = new CompressFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), compressFrag, R.id.fragContainer, getSupportActionBar(), "Compress", true, false, true, null);
            }


        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: MainActivity - displayPrivacyPolicy"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.permissions:
            {
                DialogUtils.createAlertPermission(this, "Storage Permission", "Do you want to enable storage permission for Dwindle?", true, new PermissionCallback() {
                    @Override
                    public void checkPermission(boolean ischeckPermission) {
                        if(ischeckPermission)
                        {
                            GeneralUtils.openAppSettingsScreen(getApplicationContext());
                        }
                    }
                }).show();
                break;
            }
            default:
            {
                //unknown
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void wireUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        this.txtUsage = (TextView) findViewById(R.id.txtUsage);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void displayUsedStorage()
    {
        String storageUsed = "";
        if(DeviceMemoryUtils.getUsedStorageSize() != null)
        {
            storageUsed = DeviceMemoryUtils.formatSize(DeviceMemoryUtils.getUsedStorageSize());
        }else
        {
            storageUsed = "Unknown";
        }

        if(DeviceMemoryUtils.getTotalStorageSize() != null)
        {
            storageUsed = storageUsed + " / " + DeviceMemoryUtils.formatSize(DeviceMemoryUtils.getTotalStorageSize());
        }else
        {
            storageUsed = storageUsed + " / Unknown";
        }

        this.txtUsage.setText(storageUsed);

        if(DeviceMemoryUtils.getUsedStorageSize() != null && DeviceMemoryUtils.getTotalStorageSize() != null)
        {
            Double usedStorageSize = DeviceMemoryUtils.getUsedStorageSize().doubleValue();
            Double totalStorageSize = DeviceMemoryUtils.getTotalStorageSize().doubleValue();
            Double percentage = Math.ceil((usedStorageSize/totalStorageSize) * 100);
            this.progressBar.setProgress(percentage.intValue());
        }else
        {
            this.progressBar.setProgress(0);
        }
    }

    public void setNavIcons(boolean home, boolean menu)
    {
        if(home)
        {

        }else
        {

        }

        if(menu)
        {

        }else
        {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(this.timer != null)
        {
            this.timer.cancel();
            this.timer.purge();
        }
    }
}
