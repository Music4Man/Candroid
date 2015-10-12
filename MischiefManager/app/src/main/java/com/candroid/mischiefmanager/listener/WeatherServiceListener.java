package com.candroid.mischiefmanager.listener;

import com.candroid.mischiefmanager.data.Channel;

public interface WeatherServiceListener {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
