package com.kucharzyk.diabetictoolboxjetpack.ui_and_data.calculator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kucharzyk.diabetictoolboxjetpack.Globals;
import com.kucharzyk.diabetictoolboxjetpack.R;
import com.kucharzyk.diabetictoolboxjetpack.room_database.Meal;
import com.kucharzyk.diabetictoolboxjetpack.room_database.MealProductCrossRef;
import com.kucharzyk.diabetictoolboxjetpack.room_database.Product;

import java.time.Clock;
import java.util.List;

public class MealSummaryFragment extends Fragment {
    public static final String TAG = "MealSummaryFragment";

    private CalculatorViewModel calculatorViewModel;
    private NavController navController;
    private MealSummaryAdapter mMealSummaryAdapter;
    //private TextView mealName;

    FloatingActionButton mSaveMealButton, mDeleteMealButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        calculatorViewModel = new ViewModelProvider(requireActivity()).get(CalculatorViewModel.class);
        View child = inflater.inflate(R.layout.fragment_meal_summary, container, false);
        navController = NavHostFragment.findNavController(this);
        buildRecyclerView(child);

        mSaveMealButton = child.findViewById(R.id.button_save_meal);
        mDeleteMealButton = child.findViewById(R.id.button_delete_meal);
        //mealName = child.findViewById(R.id.text_meal_summary);
        

        final Observer<List<Product>> mealSummaryObserver = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                mMealSummaryAdapter.setProducts(products);
            }
        };

        calculatorViewModel.getMealSummary().observe(getViewLifecycleOwner(), mealSummaryObserver);
        return child;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mDeleteMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorViewModel.getMeal().clear();
                NavDirections action = MealSummaryFragmentDirections.actionMealSummaryToNavigationCalculator();
                navController.navigate(action);
            }
        });

        mSaveMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Meal testMeal = new Meal("Meal " + Globals.SIMPLE_DATE_TIME_FORMAT.format(System.currentTimeMillis()),
                        Globals.SIMPLE_DATE_FORMAT.format(System.currentTimeMillis()));
                Log.d(TAG, "testMeal id: " + testMeal.getMid());
                long mealId = calculatorViewModel.insertMeal(testMeal);



                for (Product product: calculatorViewModel.getMeal()
                     ) {
                    MealProductCrossRef mpcr =  new MealProductCrossRef();
                    mpcr.setMid((int) mealId);
                    mpcr.setPid(product.getPid());
                    calculatorViewModel.insertMealProductCrossRef(mpcr);
                }
            }
        });
    }

    private void buildRecyclerView(View rootView) {
        RecyclerView mRecyclerView = rootView.findViewById(R.id.recyclerViewMealSummary);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mMealSummaryAdapter = new MealSummaryAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mMealSummaryAdapter);

        mMealSummaryAdapter.setOnItemClickListener(new MealSummaryAdapter.OnItemClickListener() {
            @Override
            public void onEditProductClick(int position) {
                Product product = mMealSummaryAdapter.getProduct(position);

                NavDirections action = MealSummaryFragmentDirections.
                        actionMealSummaryToProductSummaryFragment(product, position);
                navController.navigate(action);
            }

            @Override
            public void onDeleteProductClick(int position) {
                calculatorViewModel.getMeal().remove(mMealSummaryAdapter.getProduct(position));
                calculatorViewModel.getMealSummary().setValue(calculatorViewModel.getMeal());
                //TODO If numof(products) == 0 then go back to the CalculatorFragment
            }
        });
    }
}