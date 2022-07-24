package com.example.blood_point;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class SearchedResult extends Fragment {
    View view;
    RecyclerView recView;
    ImageView back;
    SearchAdapter search;
    ArrayList<donor_data> donor_Lists;
    DatabaseReference donor_ref;
    FirebaseAuth mAuth;
    FirebaseUser cur_user;
    SearchedResult() {

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_searched_result, container, false);
        mAuth = FirebaseAuth.getInstance();
        cur_user = mAuth.getCurrentUser();
        donor_ref = FirebaseDatabase.getInstance().getReference("DONORS");
        back = view.findViewById(R.id.backarrow);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchDonorActivity.class);
            startActivity(intent);
        });
        donor_Lists = new ArrayList<>();
        search = new SearchAdapter(donor_Lists);
        recView = view.findViewById(R.id.recycle_view_search);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.LayoutManager search_donor = new LinearLayoutManager(getContext());
        recView.setLayoutManager(search_donor);
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        recView.setAdapter(search);
        Query searched = donor_ref.child(SearchDonorActivity.getLoc()).child(SearchDonorActivity.getBg());
        searched.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot db : dataSnapshot.getChildren()) {
                        String NAME = Objects.requireNonNull(db.child("NAME").getValue()).toString();
                        String PHONE_NO = Objects.requireNonNull(db.child("PHONE_NO").getValue()).toString();
                        String ADDRESS = Objects.requireNonNull(db.child("ADDRESS").getValue()).toString();
                        donor_data data = new donor_data(NAME, PHONE_NO, ADDRESS);
                        donor_Lists.add(data);
                        search.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(), "No search results match with the database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
        return view;
    }
}
