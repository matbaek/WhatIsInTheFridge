package dk.offlines.whatisinthefridge.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import dk.offlines.whatisinthefridge.R;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private static final String TAG = "ProductListAdapter";

    private Context mContext;
    private int mResource;

    public ProductListAdapter(Context context, int resource, ArrayList<Product> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String expire = getItem(position).getExpire();
        String added = getItem(position).getAdded();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_firstLineFirst);
        TextView tvExpire = (TextView) convertView.findViewById(R.id.tv_firstLineSecond);
        TextView tvAdded = (TextView) convertView.findViewById(R.id.tv_secondLine);

        tvName.setText(name);
        tvExpire.setText(expire.toString());
        tvAdded.setText("Tilføjet: " + added);

        return convertView;
    }
}
