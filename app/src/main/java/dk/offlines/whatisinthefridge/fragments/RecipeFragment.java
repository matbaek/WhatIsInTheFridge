package dk.offlines.whatisinthefridge.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import dk.offlines.whatisinthefridge.R;

public class RecipeFragment extends Fragment {
    private static final String TAG = "RecipeFragment";

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipe, container, false);

        return root;
    }
}
