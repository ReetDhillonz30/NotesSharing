package com.inception.harmeetkaur.notessharing.datamodels;

/**
 * Created by Harmeet kaur on 02-05-2018.
 */

public class feedback_model {

   public String date,feedback,email;
   public feedback_model(){}

    public feedback_model(String date, String feedback, String email) {
        this.date = date;
        this.feedback = feedback;
        this.email = email;
    }
}
