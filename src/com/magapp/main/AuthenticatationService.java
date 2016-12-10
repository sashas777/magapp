/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2016 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.main;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class AuthenticatationService extends Service {

    private Authenticator mAuthenticator;

    /*
     * public static Account GetAccount(String accountName,String accountType) {
     * return new Account(accountName, accountType); }
     */
    @Override
    public void onCreate() {
        Log.i("Sashas", "Service created");
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public void onDestroy() {
        Log.i("Sashas", "Service destroyed");
    }

    /**
     * The bind method of the service.
     *
     * @param intent The intent used to invoke the service
     * @return The binder of the class which has implemented
     * |AbstractAccountAuthenticator|
     */
    @Override
    public IBinder onBind(Intent arg0) {
        return mAuthenticator.getIBinder();
    }

    public class Authenticator extends AbstractAccountAuthenticator {
        public Authenticator(Context context) {
            super(context);
        }

        @Override
        public Bundle editProperties(
                AccountAuthenticatorResponse accountAuthenticatorResponse,
                String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle addAccount(
                AccountAuthenticatorResponse accountAuthenticatorResponse,
                String s, String s2, String[] strings, Bundle bundle)
                throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle confirmCredentials(
                AccountAuthenticatorResponse accountAuthenticatorResponse,
                Account account, Bundle bundle) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(
                AccountAuthenticatorResponse accountAuthenticatorResponse,
                Account account, String s, Bundle bundle)
                throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAuthTokenLabel(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle updateCredentials(
                AccountAuthenticatorResponse accountAuthenticatorResponse,
                Account account, String s, Bundle bundle)
                throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle hasFeatures(
                AccountAuthenticatorResponse accountAuthenticatorResponse,
                Account account, String[] strings) throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }
    }

}
