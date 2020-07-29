package com.paradigmshift.fyp.EditItems;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DataSetUpload {



        String [] names = {"Aaron Bradley", "Barry Allen", "Bella Holmes", "Caroline Shaw", "Connor Graham", "Deann Hunt", "Ella Cole", "Jayden Shaw", "Jerry Carrol", "Lena Lucas", "Leonrd Kim", "Marc Baker"};
        String [] statuses ={"When the sensor experiments for deep space, all mermaids accelerate mysterious, vital moons.",
                "It is a cold powerdrain, sir.",
                "Particle of a calm shield, control the alignment!",
                "The human kahless quickly promises the phenomenan.",
                "Ionic cannon at the infinity room was the sensor of voyage, imitated to a dead pathway.",
                "Vital particles, to the port.",
                "Stars fly with hypnosis at the boldly infinity room!",
                "Hypnosis, definition, and powerdrain.",
                "When the queen experiments for nowhere, all particles control reliable, cold captains.",
                "When the c-beam experiments for astral city, all cosmonauts acquire remarkable, virtual lieutenant commanders.",
                "Starships walk with love at the cold parallel universe!",
                "Friendship at the bridge that is when quirky green people yell."};

        String [] profilePicUrls ={"https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Faaron_bradley.webp?alt=media&token=4baa405f-f087-4cbc-8ed3-bbe1ffe3281d",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Fbarry_allen.webp?alt=media&token=03268646-ca71-45f9-9811-06f774055cfd",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Fbella_holmes.webp?alt=media&token=44c3d636-4cc1-4d35-89b7-92c1c4d7c014",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Fcaroline_shaw.webp?alt=media&token=c66337f8-75f7-4bb5-aea0-6385a88338ef",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Fconnor_graham.webp?alt=media&token=1bafdfb5-c828-44f6-927a-d9eeb111dcfb",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Fdeann_hunt.webp?alt=media&token=65f42659-1aea-43cb-894c-200e25a8a09e",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Fella_cole.webp?alt=media&token=2afdd997-046e-4fb0-9b70-fcfd72132975",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Fjayden_shaw.webp?alt=media&token=b9cc7826-0f36-4c4c-8fea-253bbf405a14",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Fjerry_carrol.webp?alt=media&token=5d9f1720-a45e-4bf2-b5d8-7cd6ce7531ef",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Flena_lucas.webp?alt=media&token=3953f512-3e33-4811-92ea-1fd150e43593",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Fleonrd_kim.webp?alt=media&token=459f84d0-a892-4cce-a421-e29d5ece8874",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Fmarc_baker.webp?alt=media&token=e0e4c295-5c14-4c95-a976-66db98795d78",
        };
        String [] city = {"Lahore","Islamabad","karachi","Peshawar","Multan","pindi","Kashmir","Lahore","Lahore","Lahore","Lahore","Abtbad"};
        String [] AvailableFrom = {"3:00 pm","3:00 pm","3:00 pm","3:00 pm","3:00 pm","3:00 pm","3:00 pm","3:00 pm","3:00 pm","3:00 pm","3:00 pm","3:00 pm"};
        String [] AvailableTo = {"6:30 pm","6:30 pm","6:30 pm","6:30 pm","6:30 pm","6:30 pm","6:30 pm","6:30 pm","6:30 pm","6:30 pm","6:30 pm","6:30 pm"};
        String [] StreetAddress = {"Main Street","Main Street","Main Street","Main Street","Main Street","Main Street","Main Street","Main Street","Main Street","Main Street","Main Street","Main Street",};
        int [] StudentsTutored = {1,2,3,4,5,6,7,8,9,10,11,12};
        String [] TutorRequests = {"1","2","3","4","1","2","3","4","1","2","3","4"};
        String [] Rate = {"Rs.100","Rs.200","Rs.300","Rs.100","Rs.200","Rs.300","Rs.100","Rs.200","Rs.300","Rs.100","Rs.200","Rs.300",};
        int [] Review = {1,2,3,4,5,1,2,3,4,5,1,2};

        String Background[] = {"https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fcs.png?alt=media&token=cf7f5247-8f28-4ec6-80b5-f523157693a4",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fchem.jpg?alt=media&token=2e2ab1e8-72a7-446d-8929-6fc748e13804",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fphysics.jpg?alt=media&token=64d58b39-6b76-4ba9-807e-3568b97bd2d9",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fmath.jpg?alt=media&token=43df1398-187b-4895-9a2f-c6814ff161f5",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fchem.jpg?alt=media&token=2e2ab1e8-72a7-446d-8929-6fc748e13804",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fphysics.jpg?alt=media&token=64d58b39-6b76-4ba9-807e-3568b97bd2d9",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fmath.jpg?alt=media&token=43df1398-187b-4895-9a2f-c6814ff161f5",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fchem.jpg?alt=media&token=2e2ab1e8-72a7-446d-8929-6fc748e13804",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fphysics.jpg?alt=media&token=64d58b39-6b76-4ba9-807e-3568b97bd2d9",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fmath.jpg?alt=media&token=43df1398-187b-4895-9a2f-c6814ff161f5",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fcs.png?alt=media&token=cf7f5247-8f28-4ec6-80b5-f523157693a4",
                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fchem.jpg?alt=media&token=2e2ab1e8-72a7-446d-8929-6fc748e13804",};


       Context ctx;

        public DataSetUpload(Context context){
            this.ctx = context;
            Map<String, Object> note = new HashMap<>();
            for(int i=0;i<12;i++){
                note.put("name", names[i]);
                note.put("profilePic", profilePicUrls[i]);
                note.put("status", statuses[i]);
                note.put("city", city[i]);
                note.put("availableFrom", AvailableFrom[i]);
                note.put("availableTo", AvailableTo[i]);
                note.put("strretAddress", StreetAddress[i]);
                note.put("studentsTutored", StudentsTutored[i]);
                note.put("tutorRequests", TutorRequests[i]);
                note.put("rate", Rate[i]);
                note.put("review", Review[i]);
                note.put("backgroundImage", Background[i]);
                note.put("email", "Tutor@gmail.com");
                note.put("Title", "P.H.D");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("TutorData").document().set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ctx, "Note saved", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ctx, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });
            }
        }

    // myList.add(new FoldableDataModel("abc",3,"abc","abc","abc",3,"abc","abc","abc","abc","abc","abc"));

}
