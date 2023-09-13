package com.example.xpressme;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
            R.drawable.tired, R.drawable.nervous, R.drawable.surprise, R.drawable.scared,
    R.drawable.curious, R.drawable.sick};

    private int[] needsDrawables = {R.drawable.eat, R.drawable.sleep, R.drawable.bathroom,
    R.drawable.shower, R.drawable.drink, R.drawable.dress_up, R.drawable.medicine};

    private int[] peopleDrawables = {R.drawable.dad, R.drawable.mom, R.drawable.brother,
    R.drawable.sister, R.drawable.grandma, R.drawable.grandpa};
    private int selectedImageResource;
    Spinner categorySpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_selection, container, false);
        viewPager = rootView.findViewById(R.id.viewPager);
        btnOK = rootView.findViewById(R.id.btnOK);
        categorySpinner = rootView.findViewById(R.id.categorySpinner);
        // Set up the adapter for the category Spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        ImagePagerAdapter adapter = new ImagePagerAdapter(requireContext(), foodDrawables);
        viewPager.setAdapter(adapter);
        // Set an item selected listener for the category Spinner
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle category selection here
                String selectedCategory = parent.getItemAtPosition(position).toString();
                updateViewPagerImages(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected (if needed)
            }
        });

        // Set up the initial images (default category: "Food")
        updateViewPagerImages("Food");



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Handle page scroll if needed
            }

            @Override
            public void onPageSelected(int position) {
                // Track the selected image based on the current category
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                int[] selectedDrawables;

                if (selectedCategory.equals("Food")) {
                    selectedDrawables = foodDrawables;
                } else if (selectedCategory.equals("Feelings")) {
                    selectedDrawables = feelingsDrawables;
                } else if (selectedCategory.equals("Needs")) {
                    selectedDrawables = needsDrawables;
                } else if (selectedCategory.equals("People")) {
                    selectedDrawables = peopleDrawables;
                } else {
                    // Handle the case when an unknown category is selected
                    selectedDrawables = new int[0]; // Empty array
                }

                if (position >= 0 && position < selectedDrawables.length) {
                    selectedImageResource = selectedDrawables[position];
                } else {
                    // Handle an invalid position (out of bounds)
                    selectedImageResource = -1; // Set to a default value or handle the error as needed
                }
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
    private void updateViewPagerImages(String category) {
        // Depending on the selected category, update the images in the ViewPager
        int[] selectedDrawables;

        if (category.equals("Food")) {
            selectedDrawables = foodDrawables;
        } else if (category.equals("Feelings")) {
            selectedDrawables = feelingsDrawables;
        } else if (category.equals("Needs")) {
            selectedDrawables = needsDrawables;
        } else if (category.equals("People")) {
            selectedDrawables = peopleDrawables;
        } else {
            // Handle the case when an unknown category is selected
            selectedDrawables = new int[0]; // Empty array
        }

        ImagePagerAdapter adapter = new ImagePagerAdapter(requireContext(), selectedDrawables);
        viewPager.setAdapter(adapter);
    }

    public interface ImageSelectionListener {
        void onImageSelected(int drawableResourceId);
    }
}
