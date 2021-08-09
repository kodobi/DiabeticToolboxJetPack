package com.kucharzyk.diabetictoolboxjetpack.ui_and_data.diary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kucharzyk.diabetictoolboxjetpack.R;
import com.kucharzyk.diabetictoolboxjetpack.room_database.Product;

import java.util.ArrayList;
import java.util.List;

public class DiaryFragment extends Fragment {
    public static final String TAG = "DiaryFragment";

    private DiaryEntryViewModel diaryEntryViewModel;
    private DiaryEntryAdapter mAdapter;
    Product product1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        diaryEntryViewModel =
                new ViewModelProvider(this).get(DiaryEntryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_diary, container, false);

        buildRecyclerView(root);

        FloatingActionButton mAddProductButton = root.findViewById(R.id.diary_button_add_product);
        FloatingActionButton mSubProductButton = root.findViewById(R.id.diary_button_subtract_product);
        product1 = new Product("TestProduct2.1", 41.0, 16.0, 13.0, 100.0);

        final Observer<List<Product>> diaryEntriesObserver = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> diaryEntries) {
                mAdapter.setDiaryEntries(diaryEntries);
            }
        };

        mAddProductButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                diaryEntryViewModel.insert(product1);
            }
        });

        mSubProductButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                diaryEntryViewModel.deleteLastProduct(product1);
            }
        });

        diaryEntryViewModel.getAllProducts().observe(getViewLifecycleOwner(), diaryEntriesObserver);
        return root;
    }

    private void buildRecyclerView(View rootView){
        RecyclerView mDiaryRecyclerView = rootView.findViewById(R.id.diary_recycler_view);
        mDiaryRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new DiaryEntryAdapter();

        mDiaryRecyclerView.setLayoutManager(mLayoutManager);
        mDiaryRecyclerView.setAdapter(mAdapter);
    }

}