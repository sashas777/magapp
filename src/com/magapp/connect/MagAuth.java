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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.magapp.main.NewOrderService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.timroes.axmlrpc.XMLRPCException;


public class MagAuth implements FinishLoginInterface {

    private String accountType = "com.magapp.main";
    private String desired_preferense_file = "magapp";
    private static String url;
    private String api_username = null;
    private String api_password = null;

    private Context activity;
    private LoginTask task;
    SharedPreferences settings;

    GetSession GetSessionCallBack;


    public MagAuth(GetSession callback, Context act, int cleansession) {
        Log.d("Sashas", this.getClass().getName() + ":MagAuth");
        GetSessionCallBack = callback;
        activity = act;
        settings = activity.getSharedPreferences(desired_preferense_file, 0);

        if (cleansession == 1) {
            Log.d("Sashas", this.getClass().getName() + ":MagAuth:cleansession=1");
            setSession(activity, null);
        }

        if (getSession() != null && isOnline()) {
            Log.d("Sashas", this.getClass().getName() + ":MagAuth:getSession!=null && isOnline");
            GetSessionCallBack.SessionReturned(getSession(), true);
        }
        else {
            Log.d("Sashas", this.getClass().getName() + ":MagAuth:getSession==null || !isOnline");
            setSession(activity, null);
            login();
        }
    }


    public String getCredentials() {
        String selected_account_name = settings.getString("selected_account_name", null);
        AccountManager manager = AccountManager.get(activity);
        Account[] accounts = manager.getAccountsByType(accountType);
        /* com.magapp.login with account specified */
        for (int i = 0; i < accounts.length; i++) {
            Account account = accounts[i];
            if (selected_account_name.equals(account.name)) {
                api_username = manager.getUserData(account, "username");
                api_password = manager.getPassword(account);
                url = manager.getUserData(account, "store_url");
                break;
            }
        }
        if (url != null) {
            url = url.concat("/index.php/api/xmlrpc/");
        }

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("store_url", url);
        editor.commit();
        return url;
    }

    public void login() {

        url = getCredentials();

        if (api_username != null && api_password != null && url != null && isOnline()) {
            task = new LoginTask(this, api_username, api_password, url);
            task.execute();
        } else if (!isOnline() && !(activity instanceof NewOrderService)) {
            Log.d("Sashas", "No Internet Connection.");
            makeToast("Oops. No network connection.");
        } else if ((api_password == null || url == null) && !(activity instanceof NewOrderService)) {
            Log.d("Sashas", "No accounts");
            task = new LoginTask(this, api_username, api_password, url);
            task.execute();
        } else if (!(activity instanceof NewOrderService)) {
            makeToast("Please select default account");
            Log.d("Sashas", "Default account not chosen.");
        }
    }

    public void onFinishLoginPreExecute() {

    }

    @Override
    public void doFinishLoginPostExecute(Object session) {
        Log.d("Sashas", this.getClass().getName() + ":doFinishLoginPostExecute");
        if (session instanceof XMLRPCException) {
            XMLRPCException exp = (XMLRPCException) session;
            String res = HandleError(exp);
            Log.e("Sashas", res);
            GetSessionCallBack.SessionReturned(res, false);
        } else if (session instanceof Exception) {
            Log.e("Sashas", ((Exception) session).getMessage().toString());
            GetSessionCallBack.SessionReturned(((Exception) session).getMessage().toString(), false);
        } else if (session instanceof String) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("session_id", session.toString());
            editor.commit();
            Log.d("Sashas", this.getClass().getName() + ":doFinishLoginPostExecute:session_saved");
			/* api_session=session.toString(); */
            GetSessionCallBack.SessionReturned(session.toString(), true);
        }

    }

    public String getApiUrl() {
        if (url != null && url.length() < 1)
            url = getCredentials();

        Log.e("Sashas", "url - " + url);
        return url;
    }

    public void setSession(Context act, String ses) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("session_id", ses);
        editor.commit();
        Log.d("Sashas", this.getClass().getName() + ":MagAuth:setSession " + ses);
    }


    public String getSession() {
        String session_id = settings.getString("session_id", null);
        return session_id;
    }


    public void makeToast(String text) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Log.d("Sashas", "Network type: " + String.valueOf(activeNetwork.getType()));
        return isConnected;
    }


    public String HandleError(XMLRPCException error_obj) {

        String error = error_obj.toString();

        String regex_script = "\\[code (.*?)\\]";
        Pattern p = Pattern.compile(regex_script);

        Matcher m = p.matcher(error);
        String error_code = "0";
        if (m.find()) {
            error_code = m.group(1).toString();
        }

        if (error_code.equals("5")) {
            error = "Session expired. Try to re-login.";
        } else if (error_code.equals("2")) {
            error = "Access denied. Please check credentials.";
        }

        return error;
    }
}
