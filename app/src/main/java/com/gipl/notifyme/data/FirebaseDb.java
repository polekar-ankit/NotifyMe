package com.gipl.notifyme.data;

import androidx.annotation.NonNull;

import com.gipl.notifyme.data.model.firebase.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDb {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    String empId;

    public FirebaseDb(String empId) {
        this.empId = empId;
    }

    public void setEmployeeCheckInStatus(Employee employee, OnCompleteListener<Void> listener) {
        DatabaseReference databaseReference = db.getReference();
        databaseReference.child("TAttendance").child(empId).setValue(employee)
                .addOnCompleteListener(listener);
    }

    public void checkEmployeeData(ValueEventListener valueEventListener){
        DatabaseReference databaseReference = db.getReference().child("TAttendance").child(empId);
        databaseReference.addValueEventListener(valueEventListener);
    }
    public void removeValueEvent(ValueEventListener valueEventListener){
        DatabaseReference databaseReference = db.getReference().child("TAttendance").child(empId);
        databaseReference.removeEventListener(valueEventListener);
    }
}
