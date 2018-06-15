/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2018 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.connect;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Vector;

import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class RequestArrayTask extends AsyncTask<Vector, Void, Object[]> implements GetSession {


    RequestArrayInterface RequestCallBack;
    private Vector[] stored_params;
    private Context activity;
    private String desired_preferense_file = "magapp";
    private SharedPreferences settings;
    private String api_route;

    public RequestArrayTask(RequestArrayInterface callback, Context act, String api_point) {
        RequestCallBack = callback;
        activity = act;
        api_route = api_point;
        settings = activity.getSharedPreferences(desired_preferense_file, 0);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        RequestCallBack.onPreExecute();
    }

    protected Object[] doInBackground(Vector... params) {

        Object[] result_info;
        stored_params = params;
        String store_url = settings.getString("store_url", null);
        URL uri = null;
        try {
            uri = new URL(store_url);
        } catch (MalformedURLException e) {
            Log.e("Sashas", "RequestArrayTask::doInBackground:MalformedURLException " + e.getMessage());
        }
        XMLRPCClient client = new XMLRPCClient(uri, XMLRPCClient.FLAGS_NIL);

        try {
            if (!isOnline())
                throw new XMLRPCException("No internet connection");

            String session_id = settings.getString("session_id", null);
            Log.d("Sashas", session_id + " " + store_url + api_route + " " + Arrays.toString(params));
            result_info = (Object[]) client.call("call", session_id, api_route, params[0]);
            return result_info;
        } catch (XMLRPCServerException e) {
            Log.e("Sashas", "RequestArrayTask::doInBackground:XMLRPCServerException " + e.getMessage());
            return new Object[]{e};
        } catch (XMLRPCException e) {
            Log.e("Sashas", "RequestArrayTask::doInBackground:XMLRPCException " + e.getMessage());
            e.printStackTrace();
            return new Object[]{e};
        } catch (Exception e) {
            Log.e("Sashas", "RequestArrayTask::doInBackground:XMLRPCException " + e.getMessage());
            return new Object[]{e};
        }
    }

    @Override
    protected void onPostExecute(Object[] result) {

        if (result.length > 0 && result[0] instanceof XMLRPCException) {
            HandleError((XMLRPCException) result[0]);
        } else {

            RequestCallBack.doPostExecute(result, api_route);
        }
    }

    public void HandleError(XMLRPCException error_obj) {
         /* Set session null */
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("session_id", null);
        editor.commit();
		/* Set session null */
        if (!error_obj.getMessage().toString().equals("No internet connection")) {
            MagAuth auth = new MagAuth(this, activity, 1);
        } else {
            RequestCallBack.RequestFailed(error_obj.getMessage().toString());
        }
    }

    public void SessionReturned(String result, Boolean status) {
        if (status) {
            doInBackground(stored_params);
        } else {
            RequestCallBack.RequestFailed(result);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
