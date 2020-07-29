package com.paradigmshift.fyp.FirebaseDatamodel;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.paradigmshift.fyp.R;

public class InitializeData {

    ImageView profilePic ;
    TextView name ;
    TextView title ;
    TextView description ;
    SmileRating smileRating ;
    TextView phoneNumber ;
    TextView email ;
    TextView location ;

    public InitializeData(ImageView profilePic, TextView name, TextView title, TextView description, SmileRating smileRating, TextView phoneNumber, TextView email, TextView location) {
        this.profilePic = profilePic;
        this.name = name;
        this.title = title;
        this.description = description;
        this.smileRating = smileRating;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.location = location;
    }


    public String initialize(CollectionReference profileReference) {

        final String[] documentId = {""};
        profileReference.document().getId();
        profileReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            ProfileDataModel model = documentSnapshot.toObject(ProfileDataModel.class);
                            model.setDocumentID(documentSnapshot.getId());

                             documentId[0] = model.getDocumentID();


                            name.setText(model.getName());
                            title.setText(model.getTitle());
                            smileRating.setSelectedSmile(BaseRating.TERRIBLE);
                            phoneNumber.setText(model.getPhoneNo());
                            email.setText(model.getEmail());
                            location.setText(model.getLocation());
                            description.setText(model.getDescruption());

                            //
                            switch (model.getRatings()) {
                                case 1:
                                    smileRating.setSelectedSmile(BaseRating.TERRIBLE,true);
                                    break;
                                case 2:
                                    smileRating.setSelectedSmile(BaseRating.BAD,true);
                                    break;
                                case 3:
                                    smileRating.setSelectedSmile(BaseRating.OKAY,true);
                                    break;
                                case 4:
                                    smileRating.setSelectedSmile(BaseRating.GOOD,true);
                                    break;
                                case 5:
                                    smileRating.setSelectedSmile(BaseRating.GREAT,true);
                                    break;
                                case 0:
                                    smileRating.setSelectedSmile(BaseRating.NONE);
                                    break;
                            }

                        }


                    }
                });
        return documentId[0];
    }
}
