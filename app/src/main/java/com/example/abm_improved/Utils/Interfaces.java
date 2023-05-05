package com.example.abm_improved.Utils;

import android.widget.ImageView;

import androidx.navigation.NavController;

public class Interfaces {

    public interface OnFinishQueryInterface {
        void onFinishQuery();
    }

    public interface OnChooseProfilePicListener {
        void onImageClick(ImageView imageView);
    }

    public static class DoNothing implements OnFinishQueryInterface {
        @Override
        public void onFinishQuery() {
        }
    }
}
