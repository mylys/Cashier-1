package com.easygo.cashier.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-20
 */
public class EquipmentState implements Parcelable {
    private String equipment_name;
    private boolean equipment_state;
    private boolean equipment_request;

    public EquipmentState(String equipment_name, boolean equipment_state,boolean equipment_request) {
        this.equipment_name = equipment_name;
        this.equipment_state = equipment_state;
        this.equipment_request = equipment_request;
    }

    protected EquipmentState(Parcel in) {
        equipment_name = in.readString();
        equipment_state = in.readByte() != 0;
        equipment_request = in.readByte() != 0;
    }

    public static final Creator<EquipmentState> CREATOR = new Creator<EquipmentState>() {
        @Override
        public EquipmentState createFromParcel(Parcel in) {
            return new EquipmentState(in);
        }

        @Override
        public EquipmentState[] newArray(int size) {
            return new EquipmentState[size];
        }
    };

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public boolean isEquipment_state() {
        return equipment_state;
    }

    public void setEquipment_state(boolean equipment_state) {
        this.equipment_state = equipment_state;
    }

    public boolean isEquipment_request() {
        return equipment_request;
    }

    public void setEquipment_request(boolean equipment_request) {
        this.equipment_request = equipment_request;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(equipment_name);
        dest.writeByte((byte) (equipment_state ? 1 : 0));
        dest.writeByte((byte) (equipment_request ? 1 : 0));
    }
}
