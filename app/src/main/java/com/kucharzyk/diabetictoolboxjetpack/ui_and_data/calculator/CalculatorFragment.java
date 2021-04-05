package com.kucharzyk.diabetictoolboxjetpack.ui_and_data.calculator;

import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;


import com.kucharzyk.diabetictoolboxjetpack.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CalculatorFragment extends Fragment {

    private static DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");

    private CalculatorViewModel calculatorViewModel;
    private RecyclerView mRecyclerView;
    private FoodProductAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<FoodProduct> mFoodProductList;
    private ArrayList<FoodProduct> mMeal;

    AutoCompleteTextView mealTextView;
    public TextView mMealCarbsValue;
    public TextView mMealFatValue;
    public TextView mMealProteinValue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calculatorViewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calculator, container, false);

        createExampleMealList();
        buildRecyclerView(root);

        mealTextView = root.findViewById(R.id.MealAutoCompleteTextView);
        mMealCarbsValue = root.findViewById(R.id.text_carbs_summary_value);
        mMealFatValue = root.findViewById(R.id.text_fat_summary_value);
        mMealProteinValue = root.findViewById(R.id.text_proteins_summary_value);
        mMeal = new ArrayList<>();

        mealTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        final Observer<ArrayList<FoodProduct>> mealSummaryObserver = new Observer<ArrayList<FoodProduct>>() {
            @Override
            public void onChanged(ArrayList<FoodProduct> foodProducts) {

                Double sumMealCarbsValue = 0.0;
                Double sumMealFatValue = 0.0;
                Double sumMealProteinsValue = 0.0;

                if (foodProducts == null){
                    mMealCarbsValue.setText(sumMealCarbsValue.toString());
                    mMealFatValue.setText(sumMealFatValue.toString());
                    mMealProteinValue.setText(sumMealProteinsValue.toString());
                }
                else {
                    for (FoodProduct product : foodProducts){
                        sumMealCarbsValue = sumMealCarbsValue + product.getCarbohydrates();
                        sumMealFatValue = sumMealFatValue + product.getFat();
                        sumMealProteinsValue = sumMealProteinsValue + product.getProteins();
                    }
                    mMealCarbsValue.setText(REAL_FORMATTER.format(sumMealCarbsValue));
                    mMealFatValue.setText(REAL_FORMATTER.format(sumMealFatValue));
                    mMealProteinValue.setText(REAL_FORMATTER.format(sumMealProteinsValue));
                }

            }
        };

        calculatorViewModel.getMealSummary().observe(getViewLifecycleOwner(), mealSummaryObserver);
/*        mealTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mealSelectionFragment = new MealSelection();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_calculator, mealSelectionFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });*/
        return root;
    }

    private void filter(String mealText){
        ArrayList<FoodProduct> filteredList = new ArrayList<>();

        for (FoodProduct meal : mFoodProductList){
            if(meal.getMealName().toLowerCase().contains(mealText.toLowerCase())) {
                filteredList.add(meal);
            }
        }
        mAdapter.filterList(filteredList);
    }

    public void changeItem(int position, String mealName){
        mFoodProductList.get(position).addMeal(mealName);
        mAdapter.notifyItemChanged(position);
    }


    private void createExampleMealList(){
        mFoodProductList = new ArrayList<>();
        mFoodProductList.add(new FoodProduct("banana", 20.24, 0.33, 1.09));
        mFoodProductList.add(new FoodProduct("apple", 10.1, 0.4, 0.4));
        mFoodProductList.add(new FoodProduct("pear", 12.3, 0.2, 0.6));
        mFoodProductList.add(new FoodProduct("cornflakes", 82.6, 7.4, 1.4));
        mFoodProductList.add(new FoodProduct("beer", 3.8, 0.0, 0.5));
        mFoodProductList.add(new FoodProduct("orange juice", 10.0, 0.2, 0.7));
        mFoodProductList.add(new FoodProduct("sandwich", 23.0, 11.0, 14.0));
        mFoodProductList.add(new FoodProduct("fish", 0.0, 12.0, 22.0));
        mFoodProductList.add(new FoodProduct("chicken wings", 8.9, 21.8, 19.6));
    }

    private void buildRecyclerView(View rootView){
        mRecyclerView = rootView.findViewById(R.id.recyclerView2);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new FoodProductAdapter(mFoodProductList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new FoodProductAdapter.OnItemClickListener() {
            @Override
            public void onAddMealClick(int position) {
                mMeal.add(mFoodProductList.get(position));
                calculatorViewModel.getMealSummary().setValue(mMeal);
                //mAdapter.notifyItemChanged(position);
            }

            @Override
            public void onItemClick(int position) {
                changeItem(position, "Selected");
            }
        });
    }
}