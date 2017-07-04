package com.abarska.customcontactapp;

import android.graphics.Bitmap;

/**
 * Created by Dell I5 on 28.06.2017.
 */

class Contact {

    private String name;
    private String surname;
    private String password;
    private String birthday;
    private String tel;
    private String email;
    private String address;
    private Bitmap largePic;
    private Bitmap thumbnailPic;


    Contact(String name, String surname, String password, String birthday, String tel, String email, String address, Bitmap largePic, Bitmap thumbnailPic) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.birthday = birthday;
        this.tel = tel;
        this.email = email;
        this.address = address;
        this.largePic = largePic;
        this.thumbnailPic = thumbnailPic;
    }

    String getName() {
        return name;
    }

    String getSurname() {
        return surname;
    }

    String getPassword() {
        return password;
    }

    String getBirthday() {
        return birthday;
    }

    String getTel() {
        return tel;
    }

    String getEmail() {
        return email;
    }

    String getAddress() {
        return address;
    }

    Bitmap getLargePic() {
        return largePic;
    }

    Bitmap getThumbnailPic() {
        return thumbnailPic;
    }
}
