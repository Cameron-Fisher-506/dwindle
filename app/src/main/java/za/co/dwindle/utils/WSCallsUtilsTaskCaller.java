package za.co.dwindle.utils;

import android.app.Activity;

public interface WSCallsUtilsTaskCaller
{
    public void taskCompleted(String response, int reqCode);
    public Activity getActivity();
}
