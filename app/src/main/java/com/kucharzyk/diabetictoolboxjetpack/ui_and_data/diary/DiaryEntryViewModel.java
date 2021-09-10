package com.kucharzyk.diabetictoolboxjetpack.ui_and_data.diary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kucharzyk.diabetictoolboxjetpack.room_database.DiaryEntryWithMealsAndProducts;
import com.kucharzyk.diabetictoolboxjetpack.room_database.Meal;
import com.kucharzyk.diabetictoolboxjetpack.room_database.MealProductCrossRef;
import com.kucharzyk.diabetictoolboxjetpack.room_database.MealWithProducts;
import com.kucharzyk.diabetictoolboxjetpack.room_database.Product;
import com.kucharzyk.diabetictoolboxjetpack.ui_and_data.DiaryEntryWithMealsAndProductsRepository;
import com.kucharzyk.diabetictoolboxjetpack.ui_and_data.MealProductCrossRefRepository;
import com.kucharzyk.diabetictoolboxjetpack.ui_and_data.MealRepository;
import com.kucharzyk.diabetictoolboxjetpack.ui_and_data.MealWithProductsRepository;
import com.kucharzyk.diabetictoolboxjetpack.ui_and_data.ProductRepository;

import java.time.LocalDate;
import java.util.List;

public class DiaryEntryViewModel extends AndroidViewModel {
    public static final String TAG = "DiaryEntryViewModel";

    private final ProductRepository productRepository;
    private final MealRepository mealRepository;
    private final MealWithProductsRepository mealWithProductsRepository;
    private final MealProductCrossRefRepository mealProductCrossRefRepository;
    private final DiaryEntryWithMealsAndProductsRepository diaryEntryWithMealsAndProductsRepository;
    private final LiveData<List<Product>> allProducts;
    private final LiveData<List<Meal>> allMeals;
    private final LiveData<List<MealWithProducts>> mealWithProductsFromDate;
    private final LiveData<List<LocalDate>> allMealDates;
    private final LiveData<List<DiaryEntryWithMealsAndProducts>> allDiaryEntries;


    public DiaryEntryViewModel(@NonNull Application application) {
        super(application);

        productRepository = new ProductRepository(application);
        mealRepository = new MealRepository(application);
        mealWithProductsRepository = new MealWithProductsRepository(application);
        diaryEntryWithMealsAndProductsRepository = new DiaryEntryWithMealsAndProductsRepository(application);
        mealProductCrossRefRepository = new MealProductCrossRefRepository(application);

        allProducts = productRepository.getAllProducts();
        allMeals = mealRepository.getAllMeals();
        allMealDates = mealRepository.getAllMealDates();
        mealWithProductsFromDate = mealWithProductsRepository.getMealWithProductsFromDate(LocalDate.now());
        allDiaryEntries = diaryEntryWithMealsAndProductsRepository.getAllDiaryEntries();
    }

    public void insert(Product product){
        productRepository.insert(product);
    }
    public void delete(Product product) { productRepository.delete(product); }
    public LiveData<MealProductCrossRef> getMealRefByProductId(int productId) {
        return mealProductCrossRefRepository.getByProductId(productId);
    }
    public LiveData<List<MealProductCrossRef>> getAllCrossRefs() {
        return mealProductCrossRefRepository.getAllMealProductCrossRef();
    }
    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }
    public LiveData<List<Meal>> getAllMeals() {
        return allMeals;
    }
    public LiveData<List<LocalDate>> getAllMealDates() {return allMealDates; }
    public LiveData<List<MealWithProducts>> getMealWithProductsFromDate() { return mealWithProductsFromDate; }
    public LiveData<List<DiaryEntryWithMealsAndProducts>> getAllDiaryEntries() { return allDiaryEntries; }


}
