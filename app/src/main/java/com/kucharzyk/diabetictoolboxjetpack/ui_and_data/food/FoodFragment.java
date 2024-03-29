package com.kucharzyk.diabetictoolboxjetpack.ui_and_data.food;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.kucharzyk.diabetictoolboxjetpack.Globals;
import com.kucharzyk.diabetictoolboxjetpack.R;
import com.kucharzyk.diabetictoolboxjetpack.room_database.Product;

import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends Fragment {
    public static final String TAG = "CalculatorFragment";

    private TextView mMealCarbsValue;
    private TextView mMealFatValue;
    private TextView mMealProteinValue;
    private TextView mMealCaloriesValue;
    private TextView mealCarbsExchangerValue;
    private TextView mealFatExchangerValue;

    private FoodViewModel foodViewModel;
    private FoodProductAdapter mFoodProductAdapter;

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodViewModel = new ViewModelProvider(requireActivity()).get(FoodViewModel.class);
        View root = inflater.inflate(R.layout.food_fragment, container, false);
        navController = NavHostFragment.findNavController(this);

        buildRecyclerView(root);

        mMealCarbsValue = root.findViewById(R.id.text_carbs_summary_value);
        mMealFatValue = root.findViewById(R.id.text_fat_summary_value);
        mMealProteinValue = root.findViewById(R.id.text_proteins_summary_value);
        mMealCaloriesValue = root.findViewById(R.id.text_calories_summary_value);
        mealCarbsExchangerValue = root.findViewById(R.id.text_carbs_exchanger_summary_value);
        mealFatExchangerValue = root.findViewById(R.id.text_protein_fat_exchanger_summary_value);
        TextInputEditText productSearchBar = root.findViewById(R.id.food_fragment_TextInputEditText_searched_product);
        CardView mMealSummaryCardView = root.findViewById(R.id.view_meal_summary_card_view);
        ConstraintLayout mMealSummaryConstraintLayout = root.findViewById(R.id.layout_meal_summary_constraint_layout);

        productSearchBar.addTextChangedListener(new TextWatcher() {
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

        final Observer<List<Product>> mealSummaryObserver = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> foodProducts) {
                Double sumMealCarbsValue = 0.0;
                Double sumMealFatValue = 0.0;
                Double sumMealProteinsValue = 0.0;
                Double sumMealCaloriesValue = 0.0;

                if (foodProducts != null) {
                    for (Product product : foodProducts) {
                        sumMealCarbsValue += product.getCarbohydrates();
                        sumMealFatValue += product.getFat();
                        sumMealProteinsValue += product.getProteins();
                        sumMealCaloriesValue += product.getCalories();
                    }
                }
                String mealCarbs = Globals.REAL_FORMATTER.format(sumMealCarbsValue) + " g";
                String mealFat = Globals.REAL_FORMATTER.format(sumMealFatValue) + " g";
                String mealProteins = Globals.REAL_FORMATTER.format(sumMealProteinsValue) + " g";
                String mealCalories = Globals.REAL_FORMATTER.format(sumMealCaloriesValue) + " kcal";
                String mealCarbsExchanger = Globals.REAL_FORMATTER.format
                        (sumMealCarbsValue / 12) + " units";
                String mealFatExchanger = Globals.REAL_FORMATTER.format
                        ((9 * sumMealFatValue + 4 * sumMealProteinsValue) / 100) + " units";

                mMealCarbsValue.setText(mealCarbs);
                mMealFatValue.setText(mealFat);
                mMealProteinValue.setText(mealProteins);
                mMealCaloriesValue.setText(mealCalories);
                mealCarbsExchangerValue.setText(mealCarbsExchanger);
                mealFatExchangerValue.setText(mealFatExchanger);
            }
        };

        final Observer<List<Product>> allProductsObserver = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                mFoodProductAdapter.setProducts(products);
            }
        };

        mMealSummaryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = FoodFragmentDirections.actionNavigationCalculatorToMealSummary();
                navController.navigate(action);
            }
        });

        foodViewModel.getMealSummary().observe(getViewLifecycleOwner(), mealSummaryObserver);
        foodViewModel.getAllProducts().observe(getViewLifecycleOwner(), allProductsObserver);
        return root;
    }


    private void filter(String productText) {
        List<Product> filteredList = new ArrayList<>();

        for (Product product : mFoodProductAdapter.getProductsList()) {
            if (product.getProductName().toLowerCase().contains(productText.toLowerCase())) {
                filteredList.add(product);
            }
        }
        mFoodProductAdapter.filterList(filteredList);
    }

/*    public void changeItem(int position, String mealName) {
        mAdapter.mFoodProducts.get(position).addMeal(mealName);
        mAdapter.notifyItemChanged(position);
    }*/


    private void buildRecyclerView(View rootView) {
        RecyclerView mRecyclerView = rootView.findViewById(R.id.recyclerViewFoodFragment);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mFoodProductAdapter = new FoodProductAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mFoodProductAdapter);

        mFoodProductAdapter.setOnItemClickListener(new FoodProductAdapter.OnItemClickListener() {
            @Override
            public void onAddProductClick(int position) {
                if (!foodViewModel.getMeal().contains(mFoodProductAdapter.getProduct(position))) {
                    foodViewModel.getMeal().add(mFoodProductAdapter.getProduct(position));
                    foodViewModel.getMealSummary().setValue(foodViewModel.getMeal());
                }
            }

            @Override
            public void onDeleteProductClick(int position) {
                foodViewModel.getMeal().remove(mFoodProductAdapter.getProduct(position));
                foodViewModel.getMealSummary().setValue(foodViewModel.getMeal());
            }

            @Override
            public void onItemClick(int position) {
                Product product = mFoodProductAdapter.getProduct(position);

                @NonNull NavDirections action = FoodFragmentDirections.
                        actionNavigationCalculatorToProductSummaryFragment(product, position);
                navController.navigate(action);
            }
        });
    }

}