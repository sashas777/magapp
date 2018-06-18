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

public class RequestTask extends AsyncTask<Vector, Void, Object> implements GetSession {


    private RequestInterface RequestCallBack;
    private Vector[] stored_params;
    private Context activity;
    private String desired_preferense_file = "magapp";
    private SharedPreferences settings;
    private String api_route;

    public RequestTask(RequestInterface callback, Context act, String api_point) {
        RequestCallBack = callback;
        activity = act;
        api_route = api_point;
        settings = activity.getSharedPreferences(desired_preferense_file, 0);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        RequestCallBack.onPreExecute();
    }

    protected Object doInBackground(Vector... params) {

        Object result_info;
        stored_params = params;
        String store_url = settings.getString("store_url", null);
        URL uri;
        try {
            uri = new URL(store_url);
        } catch (MalformedURLException e) {
            Log.e("Sashas", this.getClass().getName() + ":doInBackground:MalformedURLException");
            return e;
        }

        XMLRPCClient client = new XMLRPCClient(uri, XMLRPCClient.FLAGS_NIL);

        try {
            if (!isOnline())
                throw new XMLRPCException("No internet connection");

            String session_id = settings.getString("session_id", null);
            if (session_id == null) {
                Log.d("Sashas", this.getClass().getName() + ":doInBackground:Session empty");
                MagAuth auth = new MagAuth(this, activity, 1);
            }
            Log.d("Sashas", session_id + " " + store_url + api_route + " " + Arrays.toString(params));
            result_info = client.call("call", session_id, api_route, params[0]);
            return result_info;
        } catch (XMLRPCServerException e) {
            Log.d("Sashas", "RequestTask::doInBackground::XMLRPCServerException");
            /*Session Expired*/
            if (e.getErrorNr() == 5) {
                Log.d("Sashas", e.getMessage());
                MagAuth auth = new MagAuth(this, activity, 1);
                //@todo check if it helps
                //cancel(true);
            }
            Log.e("Sashas", String.valueOf(e.getErrorNr()));
            Log.e("Sashas", e.getMessage());
            //cancel(true);
            return e;
        } catch (XMLRPCException e) {
            Log.d("Sashas", "RequestTask::doInBackground::XMLRPCException");
            Log.e("Sashas", e.getMessage());
            //cancel(true);
            return e;
        } catch (Exception e) {
            Log.d("Sashas", "RequestTask::doInBackground::Exception");
            Log.e("Sashas", e.getMessage());
            //cancel(true);
            return e;
        }
    }

    @Override
    protected void onCancelled(Object result) {
        Log.d("Sashas", "RequestTask::onCancelled");
        if (result instanceof XMLRPCException) {
            HandleError((XMLRPCException) result);
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        Log.d("Sashas", "RequestTask::onPostExecute");
        if (result instanceof XMLRPCException) {
            HandleError((XMLRPCException) result);
        } else if (result instanceof Exception) {
            Log.e("Sashas", "RequestTask::onPostExecute::Exception");
            HandleError((Exception) result);
        } else {
            Log.d("Sashas", "RequestTask::onPostExecute::doPostExecute");
            RequestCallBack.doPostExecute(result, api_route);
            //MagAuth auth = new MagAuth(this, activity,1);
        }
    }

    private void HandleError(Exception error_obj) {
        Log.e("Sashas", "RequestTask::HandleError");
        /* Set session null */
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("session_id", null);
        editor.apply();
        /* Set session null */
        if (error_obj.getMessage().indexOf("code") > 0) {
            Log.e("Sashas", this.getClass().getName() + ":HandleError:code");
            RequestCallBack.RequestFailed(error_obj.getMessage());
        } else if (error_obj.getMessage().equals("No internet connection")) {
            Log.e("Sashas", this.getClass().getName() + ":HandleError:No internet connection");
            RequestCallBack.RequestFailed("No internet connection");
        } else {
            Log.e("Sashas", this.getClass().getName() + ":HandleError:Other");
            //  MagAuth auth = new MagAuth(this, activity, 1);
            /*@todo new message when not authorized */
            RequestCallBack.RequestFailed(error_obj.getMessage());

        }

    }

    /*@todo check if need*/
    public void SessionReturned(String result, Boolean status) {
        if (status) {
            doInBackground(stored_params);
        } else {
            RequestCallBack.RequestFailed(result);
        }
    }


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo;
        try {
            netInfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {
            Log.d("Sashas", "RequestTask::isOnline::Exception");
            Log.e("Sashas", e.getMessage());
            netInfo = null;
        }

        Log.d("Sashas", "RequestTask::isOnline");
        Log.d("Sashas", netInfo.toString());
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
