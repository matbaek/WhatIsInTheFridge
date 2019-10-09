package dk.offlines.whatisinthefridge;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dk.offlines.whatisinthefridge.Product.Product;
import dk.offlines.whatisinthefridge.Product.ProductListAdapter;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    DatabaseHelper mDatabaseHelper;
    private ListView mListView;
    private Button b_add;
    private TabLayout tab;
    private String type = "fridge";

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mDatabaseHelper = new DatabaseHelper(root.getContext());
        mListView = root.findViewById(R.id.main_listView);
        b_add = root.findViewById(R.id.main_button_add);
        tab = root.findViewById(R.id.main_tab);

        productListView("fridge");

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    type = "fridge";
                } else if(tab.getPosition() == 1) {
                    type = "freezer";
                }
                productListView(type);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductFragment fragment = new AddProductFragment();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Product tempProduct = (Product) parent.getItemAtPosition(position);
                removeData(tempProduct.getId());
                productListView(type);
                return false;
            }
        });

        return root;
    }

    private void productListView(String type) {
        Log.d(TAG, "productListView: Displaying data in the ListView.");

        Cursor data = mDatabaseHelper.getData(type);
        final ArrayList<Product> listData = new ArrayList<>();
        while(data.moveToNext()) {
            listData.add(new Product(Integer.parseInt(data.getString(0)), data.getString(1), data.getString(2), data.getString(3)));
        }

        final ProductListAdapter adapter = new ProductListAdapter(this.getContext(), R.layout.product_item_list, listData) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv_name = view.findViewById(R.id.tv_firstLineFirst);
                TextView tv_expire = view.findViewById(R.id.tv_firstLineSecond);
                TextView tv_added = view.findViewById(R.id.tv_secondLine);

                if(!listData.get(position).getExpire().matches("Ingen")) {
                    try {
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(listData.get(position).getExpire());
                        Date newDate = new SimpleDateFormat("dd/MM/yyyy").parse(df.format(new Date()));
                        if((date.getTime()) < newDate.getTime()) {
                            tv_name.setTextColor(Color.RED);
                            tv_expire.setTextColor(Color.RED);
                            tv_added.setTextColor(Color.RED);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                return view;
            }
        };

        mListView.setAdapter(adapter);
    }

    public void removeData(int id) {
        boolean insertData = mDatabaseHelper.removeData(id);

        if(insertData) toastMessage("Produktet blev slettet :)");
        else toastMessage("Noget gik galt :(");
    }

    private void toastMessage(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
