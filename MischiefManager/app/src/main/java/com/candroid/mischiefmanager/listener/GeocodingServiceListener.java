package com.candroid.mischiefmanager.listener;

import com.candroid.mischiefmanager.data.LocationResult;

public interface GeocodingServiceListener {
    void geocodeSuccess(LocationResult location);

    void geocodeFailure(Exception exception);
}
