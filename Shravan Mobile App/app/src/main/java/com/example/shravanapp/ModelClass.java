package com.example.shravanapp;



public class ModelClass  {

    private int imageview1;
    private String txt_contact_name;
    private String txt_contact_num;


    ModelClass(int imageview1, String txt_contact_name, String txt_contact_num)
    {
        this.imageview1=imageview1;
        this.txt_contact_name=txt_contact_name;
        this.txt_contact_num=txt_contact_num;



    }

    public int getImageview1() {
        return imageview1;
    }

    public String getTxt_contact_name() {
        return txt_contact_name;
    }

    public String getTxt_contact_num() {
        return txt_contact_num;
    }
}
