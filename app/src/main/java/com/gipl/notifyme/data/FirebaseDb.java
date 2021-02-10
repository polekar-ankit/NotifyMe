package com.gipl.notifyme.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gipl.notifyme.data.model.db.TReason;
import com.gipl.notifyme.data.model.firebase.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.concurrent.Executors;

public class FirebaseDb {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    String empId;

    public FirebaseDb() {
    }

    public FirebaseDb(String empId) {
        this.empId = empId;
    }

    public void setEmployeeCheckInStatus(Employee employee, OnCompleteListener<Void> listener) {
        DatabaseReference databaseReference = db.getReference();
        databaseReference.child("TAttendance").child(empId).setValue(employee)
                .addOnCompleteListener(listener);
    }

    public void checkEmployeeData(ValueEventListener valueEventListener) {
        DatabaseReference databaseReference = db.getReference().child("TAttendance").child(empId);
        databaseReference.addValueEventListener(valueEventListener);
    }

    public void removeValueEvent(ValueEventListener valueEventListener) {
        DatabaseReference databaseReference = db.getReference().child("TAttendance").child(empId);
        databaseReference.removeEventListener(valueEventListener);
    }

    public void getReason(String type, DataManager dataManager) {
        Query databaseReference = db.getReference().child(type).orderByKey();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Executors.newSingleThreadExecutor().submit(() -> {
                    if (snapshot.getChildrenCount() > 0) {
                        int count = dataManager.clearReason(type);
                        for (DataSnapshot dataSnapshot :
                                snapshot.getChildren()) {
                            TReason reason = new TReason();
                            reason.setReason(dataSnapshot.getKey());
                            reason.setSuid(Integer.parseInt(dataSnapshot.getValue().toString()));
                            reason.setType(type);
                            Log.d("Notification", type + " : " + reason.getReason() + "");
                            dataManager.insertReason(reason);
                        }
                        dataManager.setReasonCacheDate(type, Calendar.getInstance().getTimeInMillis());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
