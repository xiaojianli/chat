package com.xiaojianli.wechat.appUtil;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

public class MapParcelable implements Parcelable {

	private Map<String,String> mMap = new HashMap<String, String>();
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeMap(mMap);
	}
	
	public static final Parcelable.Creator<MapParcelable> CREATOR = new Creator<MapParcelable>() {

		@SuppressWarnings("unchecked")
		@Override
		public MapParcelable createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			MapParcelable mp = new MapParcelable();
			mp.setMap(source.readHashMap(HashMap.class.getClassLoader()));
			return mp;
		}

		@Override
		public MapParcelable[] newArray(int size) {
			// TODO Auto-generated method stub
			return new MapParcelable[size];
		}
	};


	public Map<String, String> getMap() {
		return mMap;
	}

	public void setMap(Map<String, String> mMap) {
		this.mMap = mMap;
	}
	
	

}
