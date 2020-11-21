package za.co.dwindle.policies;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.json.JSONObject;

import za.co.dwindle.MainActivity;
import za.co.dwindle.R;
import za.co.dwindle.utils.ConstantUtils;
import za.co.dwindle.utils.DTUtils;
import za.co.dwindle.utils.SharedPreferencesUtils;


public class PrivacyPolicyFrag extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_privacy_policy, container, false);

        String privacyPolicy =
                "        <p style=\"text-align:left; margin:4%;\">Your privacy and legal rights are important to us. It is Dwindle&#39;s policy to respect your privacy regarding any images compressed on Dwindle.<br />\n" +
                "        <br />\n" +
                "        We do not collect any personal information and do not store any images compressed on Dwindle.<br />\n" +
                        "Dwindle compresses images and replaces the original image with the compressed one. Therefore, please save your original images in a safe please before compressing it.<br />\n" +
                "        Your continued use of our app will be regarded as acceptance of our practices around privacy and personal information. If you have any questions feel free to contact us.<br />\n" +
                "        <br />\n" +
                "        <b><u>Personal and Sensitive Information Disclosure</u></b><br />\n" +
                "        To make use of the Dwindle app please note that we require the following permissions:<br />\n" +
                "        1. Storage permission.<br />\n" +
                "        <br />\n" +
                "        This policy is effective as of 21 November 2020.<br />\n" +
                "        <br />\n" +
                "        By accepting these above terms for personal and sensitivity information disclosure as to how your images will be used, you give us permission to compress your images for the terms describe above.</p>";

        TextView txtPrivacyPolicy = (TextView) view.findViewById(R.id.txtPrivacyPolicy);
        txtPrivacyPolicy.setMovementMethod(new ScrollingMovementMethod());
        txtPrivacyPolicy.setText(Html.fromHtml(privacyPolicy));

        Button btnExit = (Button)view.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finishAffinity();
                System.exit(0);
            }
        });

        Button btnAccept = (Button) view.findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("isAccepted", true);

                    SharedPreferencesUtils.save(getContext(), SharedPreferencesUtils.PRIVACY_POLICY_ACCEPTANCE, jsonObject);

                    getContext().startActivity(new Intent(getActivity(), MainActivity.class));

                }catch(Exception e)
                {
                    Log.d(ConstantUtils.TAG, "\n\nClass: PrivacyPolicyFrag" +
                            "\nMethod: onCreateView" +
                            "\nError: " + e.getMessage() +
                            "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }
            }
        });

        return view;
    }
}
