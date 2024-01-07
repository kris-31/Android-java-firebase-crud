package com.kris.myfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    EditText Name, Regno, Marks;
    Button InsertData, button2;
    DatabaseReference ref;
    TextView textView; //in scroll view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        Name = findViewById(R.id.name);
        Regno = findViewById(R.id.edreg);
        Marks = findViewById(R.id.marks);
        InsertData = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        textView = findViewById(R.id.textviewResult);
        // Initialize Firebase
        FirebaseApp.initializeApp(MainActivity.this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("sample");

        // Show a toast message for activity creation
        Toast.makeText(MainActivity.this, "Activity created", Toast.LENGTH_SHORT).show();
    }

    // OnClickListener for the "Insert Data" button
    public void writeNewUser(View view) {
        String entered_name = Name.getText().toString();
        String entered_regno = Regno.getText().toString();
        int entered_mark = Integer.parseInt(Marks.getText().toString());
        Student user = new Student(entered_name, entered_regno, entered_mark);
        ref.push().setValue(user);  // Push to Firebase database
        Toast.makeText(MainActivity.this, "Data written to Firebase", Toast.LENGTH_SHORT).show();
    }

    // OnClickListener for the "Get Data" button
    //get method  not adivised
    public void getdatafromfb(View view) {
        textView.setText(""); // Clear existing text

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        // Attempt to convert the data to a Student object
                        Student stud = snapshot.getValue(Student.class);
                        if (stud != null) {
                            addTextToView(stud.getName() + " - " + stud.getRegno() + " - " + stud.getMarks());
                        }
                    } catch (DatabaseException ignored) {
                        // Handle the case where the data is not a Student object
                        String mString = String.valueOf(snapshot.getValue());
                        addTextToView(mString);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Can't get data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Helper method to add text to a TextView
    private void addTextToView(String text) {

        // Append the text to the existing text in the TextView
        textView.append(text + "\n");
    }

    public void call_update(View view) {  //update method
        String entered_regno = Regno.getText().toString();
        String entered_Name = Name.getText().toString();
        int entered_mark = Integer.parseInt(Marks.getText().toString());
        updateRecordByRegno(entered_regno,entered_Name,entered_mark);
    }

    public void call_delete(View view) {  //delete
        String entered_regno = Regno.getText().toString();
        deleteRecordByRegno(entered_regno);
    }



/* ############################################################################################################# */
//    delete based on regno

public void deleteRecordByRegno(String regnoToDelete) {
    Query query = ref.orderByChild("regno").equalTo(regnoToDelete);

    query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    childSnapshot.getRef().removeValue();
                }
                Toast.makeText(MainActivity.this, "Record with regno " + regnoToDelete + " deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Record with regno " + regnoToDelete + " not found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(MainActivity.this, "Deletion failed", Toast.LENGTH_SHORT).show();
        }
    });
}



/*  #############################################################################################################  */
/////update

public void updateRecordByRegno(String regnoToUpdate, String newName, int newMarks) {
    Query query = ref.orderByChild("regno").equalTo(regnoToUpdate);

    query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Update the values in the child node
                    childSnapshot.getRef().child("name").setValue(newName);
                    childSnapshot.getRef().child("marks").setValue(newMarks);
                }
                Toast.makeText(MainActivity.this, "Record with regno " + regnoToUpdate + " updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Record with regno " + regnoToUpdate + " not found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(MainActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    });
}


}