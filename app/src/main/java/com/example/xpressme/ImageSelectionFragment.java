package com.example.xpressme;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

public class ImageSelectionFragment extends DialogFragment {

    private ViewPager viewPager;
    private Button btnOK;
    private int[] drawables = {R.drawable.brother, R.drawable.pizza, R.drawable.angry};
    private int selectedImageResource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_selection, container, false);
        viewPager = rootView.findViewById(R.id.viewPager);
        btnOK = rootView.findViewById(R.id.btnOK);

        ImagePagerAdapter adapter = new ImagePagerAdapter(requireContext(), drawables);
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Handle page scroll if needed
            }

            @Override
            public void onPageSelected(int position) {
                // Track the selected image
                selectedImageResource = drawables[position];
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Handle page scroll state change if needed
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the listener in the parent fragment (CreateButtonDialogFragment)
                if (getTargetFragment() instanceof ImageSelectionListener) {
                    ImageSelectionListener listener = (ImageSelectionListener) getTargetFragment();
                    listener.onImageSelected(selectedImageResource);
                }

                // Dismiss the dialog fragment
                dismiss();
            }
        });



        return rootView;
    }

    public interface ImageSelectionListener {
        void onImageSelected(int drawableResourceId);
    }
}
