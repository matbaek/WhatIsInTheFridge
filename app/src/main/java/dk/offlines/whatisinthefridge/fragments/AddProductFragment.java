package dk.offlines.whatisinthefridge.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import dk.offlines.whatisinthefridge.DatabaseHelper;
import dk.offlines.whatisinthefridge.R;

public class AddProductFragment extends Fragment {
    DatabaseHelper mDatabaseHelper;
    private EditText et_name, et_expire;
    private TabLayout tab;
    private Button b_save, b_back;
    private String type = "fridge";

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_add, container, false);

        // Show soft keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(this.getActivity().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

        mDatabaseHelper = new DatabaseHelper(root.getContext());

        et_name = root.findViewById(R.id.add_editText_name);
        et_expire = root.findViewById(R.id.add_editText_expire);
        tab = root.findViewById(R.id.add_tab);
        b_save = root.findViewById(R.id.add_button_save);
        b_back = root.findViewById(R.id.add_button_back);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    type = "fridge";
                    et_expire.setFocusable(true);
                } else if(tab.getPosition() == 1) {
                    type = "freezer";
                    et_expire.setFocusable(false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contextName = et_name.getText().toString().substring(0, 1).toUpperCase() + et_name.getText().toString().substring(1);
                String contextExpire = et_expire.getText().toString();
                if(type == "fridge") {
                    if(contextName.length() == 0 || contextExpire.length() == 0) {
                        toastMessage("Begge felter skal være udfyldt!");
                    } else {
                        addData(contextName, contextExpire, type);
                    }
                } else if(type == "freezer") {
                    if(contextName.length() == 0) {
                        toastMessage("Navn skal være udfyldt!");
                    } else {
                        addData(contextName, "", type);
                    }
                }
                back(container);
            }
        });

        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back(container);
            }
        });

        return root;
    }

    public void addData(String entryName, String entryExpireDays, String entryType) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String added = df.format(Calendar.getInstance().getTime());
        String expireDate = "Ingen";

        if(!entryExpireDays.matches("")) {
            int expireDays = Integer.parseInt(entryExpireDays);

            // Creating expire date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Calendar.getInstance().getTime());
            calendar.add(Calendar.DATE, expireDays);
            expireDate = df.format(calendar.getTime());
        }

        boolean insertData = mDatabaseHelper.addData(entryName, expireDate, added, entryType);

        if(insertData) toastMessage("Produktet blev tilføjet :)");
        else toastMessage("Noget gik galt :(");
    }

    private void back(ViewGroup container) {
        // Hide soft keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(this.getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(container.getWindowToken(), 0);

        HomeFragment fragment = new HomeFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
    }

    private void toastMessage(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
