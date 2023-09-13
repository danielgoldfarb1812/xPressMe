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
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

public class ImageSelectionFragment extends DialogFragment {

    private ViewPager viewPager;
    private AppCompatButton btnOK;
    private int[] foodDrawables = {R.drawable.cake, R.drawable.chicken,
    R.drawable.chocolate, R.drawable.cookies, R.drawable.eggs, R.drawable.fries,
    R.drawable.hamburger, R.drawable.pancake, R.drawable.pizza, R.drawable.ramen, R.drawable.rice,
    R.drawable.schnitzel, R.drawable.spaghetti, R.drawable.steak, R.drawable.sushi};

    private int[] feelingsDrawables = {R.drawable.angry, R.drawable.confused, R.drawable.happy,
    R.drawable.sad, R.drawable.excited, R.drawable.frustrated, R.drawable.shy, R.drawable.silly,
    R.drawable.sick, R.drawable.tired, R.drawable.nervous, R.drawable.surprise, R.drawable.scared,
    R.drawable.curious, R.drawable.sick};

    private int[] needsDrawables = {R.drawable.eat, R.drawable.sleep, R.drawable.bathroom,
    R.drawable.shower, R.drawable.drink, R.drawable.dress_up, R.drawable.medicine};

    private int[] peopleDrawables = {R.drawable.dad, R.drawable.mom, R.drawable.brother,
    R.drawable.sister, R.drawable.grandma, R.drawable.grandpa};
    private int selectedImageResource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_selection, container, false);
        viewPager = rootView.findViewById(R.id.viewPager);
        btnOK = rootView.findViewById(R.id.btnOK);
        ImagePagerAdapter adapter = new ImagePagerAdapter(requireContext(), foodDrawables);
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Handle page scroll if needed
            }

            @Override
            public void onPageSelected(int position) {
                // Track the selected image
                selectedImageResource = foodDrawables[position];
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
