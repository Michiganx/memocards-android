package com.example.memo3;

import java.util.ArrayList;

/**
 * Created by Eric on 31.05.2015.
 */
public class Deck {

    @Override
    public String toString() {
        return this._name;
    }

    public Deck(Integer _id, String _name, String _description) {
        this._id = _id;
        this._name = _name;
        this._description = _description;

        this.cards = new ArrayList<Card>();
    }

    private String _name;
    private String _description;
    private Integer _id;
    private Integer _user_id;

    public ArrayList<Card> cards  = null;

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer get_user_id() {
        return _user_id;
    }

    public void set_user_id(Integer _user_id) {
        this._user_id = _user_id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }
}
