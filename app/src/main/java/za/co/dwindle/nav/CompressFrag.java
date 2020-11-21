package za.co.dwindle.nav;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.io.File;

import za.co.dwindle.R;
import za.co.dwindle.utils.ConstantUtils;
import za.co.dwindle.utils.DTUtils;
import za.co.dwindle.utils.DeviceMemoryUtils;
import za.co.dwindle.utils.GeneralUtils;
import za.co.dwindle.utils.ImageUtils;

import static android.app.Activity.RESULT_OK;

public class CompressFrag extends Fragment
{

    private final int SELECT_PHOTO = 1;

    private Button btnSelect;
    private ImageView imgSelected;
    private TextView txtPercentage;
    private SeekBar seekBar;

    private Uri uri;
    private File file;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_compress, container, false);

        wireUI(view);

        return view;
    }

    private void wireUI(View view)
    {
        this.imgSelected = (ImageView) view.findViewById(R.id.imgSelected);

        this.btnSelect = view.findViewById(R.id.btnSelect);
        setBtnSelectListener();

        this.txtPercentage = (TextView) view.findViewById(R.id.txtPercentage);
        this.seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        setSeekBarListener();
    }

    private void setBtnSelectListener()
    {
        this.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });
    }

    private void setSeekBarListener()
    {
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtPercentage.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        getActivity().startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            try
            {
                this.uri = data.getData();
                if(this.uri != null)
                {
                    String path = getPath(this.uri);
                    if(path != null)
                    {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), this.uri);
                        this.imgSelected.setImageBitmap(bitmap);

                        this.file = new File(path);
                        if(this.file != null && this.file.exists())
                        {

                            if(ImageUtils.getFileExtension(path) != null)
                            {
                                if(ImageUtils.getFileExtension(path).equals(ImageUtils.JPEG_EXTENSION))
                                {
                                    ImageUtils.compressJPEG(getContext(), this.file, this.seekBar.getProgress());
                                }else if(ImageUtils.getFileExtension(path).equals(ImageUtils.PNG_EXTENSION))
                                {
                                    ImageUtils.compressPNG(getContext(), this.file, this.seekBar.getProgress());
                                }else
                                {
                                    GeneralUtils.makeToast(getContext(), "Unable to compress image.\nUnknown file extension: "+ImageUtils.getFileExtension(path)+".\nPlease report to the developer!");
                                }

                            }else
                            {
                                GeneralUtils.makeToast(getContext(), "Unable to compress image.\nUnable to determine file extension.\nPlease report to the developer!");
                            }

                        }else
                        {
                            GeneralUtils.makeToast(getContext(), "Unable to compress image.\nFile not found.\nPlease report to the developer!");
                        }
                    }else
                    {
                        GeneralUtils.makeToast(getContext(), "Unable to load image.\nImage path not found.\nPlease report to the developer!");
                    }
                }else
                {
                    GeneralUtils.makeToast(getContext(), "Unable to load image.\n Please report to the developer!");
                }

            }catch (Exception e)
            {
                Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                        + "\nMethod: CompressFrag - onActivityResult"
                        + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
            }
        }
    }
}
