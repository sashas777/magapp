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

import android.os.AsyncTask;

import java.net.MalformedURLException;
import java.net.URL;

import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;

public class LoginTask extends AsyncTask<Void, Void, Object> {

    FinishLoginInterface FinishLoginCallBack;
    String api_url;
    String api_username = null;
    String api_password = null;

    LoginTask(FinishLoginInterface callback, String username, String password, String url) {
        FinishLoginCallBack = callback;
        api_username = username;
        api_password = password;
        api_url = url;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        FinishLoginCallBack.onFinishLoginPreExecute();
    }

    protected Object doInBackground(Void... params) {
        String session = "";
        URL uri = null;
        try {
            uri = new URL(api_url);
        } catch (MalformedURLException e) {
            return e;
        }

        XMLRPCClient client = new XMLRPCClient(uri);
        try {
            session = (String) client.call("login", api_username, api_password);
            return session;
        } catch (XMLRPCException e) {
            return e;

        } catch (Exception e) {
            return e;
        }

    }

    @Override
    protected void onPostExecute(Object result) {
        FinishLoginCallBack.doFinishLoginPostExecute(result);
    }
}

