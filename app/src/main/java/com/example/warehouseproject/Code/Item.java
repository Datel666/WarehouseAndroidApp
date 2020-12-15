package com.example.warehouseproject.Code;



/**
 * item class
 *
 * Данный класс описывает структуру записи о товаре, хранящейся в базе данных
 */
public class Item {

    public Item(int _id, String _str, String _type, String _count, String _description, byte[] _photo) {
        id = _id;
        name = _str;
        count = _count;
        type = _type;
        description = _description;
        photo = _photo;
    }

    public byte[] photo;

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String count;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String type;


}
