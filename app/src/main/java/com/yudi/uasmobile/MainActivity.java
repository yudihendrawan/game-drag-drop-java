package com.yudi.uasmobile;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<MieInstantStep> steps = new ArrayList<>();
    private MieInstantAdapter adapter;

    private static final int REQUEST_ACTIVITY_BERHASIL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MieInstantAdapter(steps);
        recyclerView.setAdapter(adapter);

        prepareData();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Collections.swap(steps, fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);

                if (isUrutanBenar()) {
                    Intent intent = new Intent(MainActivity.this, ActivityBerhasil.class);
                    startActivityForResult(intent, REQUEST_ACTIVITY_BERHASIL);
                    finish();
                }

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void prepareData() {
        steps.add(new MieInstantStep("1. Rebus air hingga mendidih"));
        steps.add(new MieInstantStep("2. Masukkan mie ke dalam air mendidih selama 3 menit"));
        steps.add(new MieInstantStep("3. Campurkan bumbu mie ke dalam mangkuk"));
        steps.add(new MieInstantStep("4. Tambahkan air panas ke dalam mangkuk"));
        steps.add(new MieInstantStep("5. Aduk hingga bumbu larut dan mie siap dinikmati"));
        Collections.shuffle(steps);
        adapter.notifyDataSetChanged();
    }

    private boolean isUrutanBenar() {
        for (int i = 0; i < steps.size(); i++) {
            String stepText = steps.get(i).getStepText();
            if (!stepText.matches("\\d+.*")) {
                return false;
            }
            int stepNumber = Integer.parseInt(stepText.split("\\.")[0]);
            if (i + 1 != stepNumber) {
                return false;
            }
        }
        return true;
    }

    public void resetGame() {
        steps.clear();
        prepareData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ACTIVITY_BERHASIL && resultCode == RESULT_OK) {
            resetGame();
        }
    }


}


