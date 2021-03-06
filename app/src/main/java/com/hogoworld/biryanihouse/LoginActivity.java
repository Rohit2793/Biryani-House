package com.hogoworld.biryanihouse;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import utility.UserVariables;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int RC_SIGN_IN = 9001;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mSocialLogin;
    private TextView progressText, txtNotRegUser, txtForgotPaswd;
    private View mLoginFormView;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;
    private AccessTokenTracker tokenTracker;
    LoginButton loginButton;
    Button btnShwHidePaswd;
    Button mEmailSignInButton;
    String conEmailAddress = "", conPassword = "";
    AQuery aq;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String msgObj = "", IdObj = "";
    String email = "";
    String password = "";
    String fbemail = "";
    String fbname = "";
    String fbId = "";
    URL imgUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        aq = new AQuery(getApplicationContext());

        findViewById(R.id.log_in_button).setOnClickListener(LoginActivity.this);
        findViewById(R.id.login_button).setOnClickListener(LoginActivity.this);
        mSocialLogin = findViewById(R.id.social_login);
        txtNotRegUser = (TextView) findViewById(R.id.txt_no_account);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        btnShwHidePaswd = (Button) findViewById(R.id.btn_show_password);
        progressText = (TextView) findViewById(R.id.progress_text);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        txtForgotPaswd = (TextView) findViewById(R.id.txt_forgot_password);

        txtForgotPaswd.setPaintFlags(txtForgotPaswd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtForgotPaswd.setText("Forgot Password?");

        txtNotRegUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, SignInActivity.class));
                finish();

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {


                        try {
                            fbId = object.getString("id");
                            fbname = object.getString("name");
                            fbemail = object.getString("email");
                            imgUrl = new URL("https://graph.facebook.com/" + fbId + "/picture?type=large");

//                            Log.d("Facebook", "Name : " + fbname + " Email : " + fbemail + " Picture : " + imgUrl);
                        } catch (JSONException | MalformedURLException e) {
                            e.printStackTrace();
                        }
//                        Bundle facebookData = getFBData(object);

                    }
                });

                Bundle paramaters = new Bundle();
                paramaters.putString("fields", "id, name, email, picture");
                request.setParameters(paramaters);
                request.executeAsync();

                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Logged in cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        SignInButton signIn = (SignInButton) findViewById(R.id.log_in_button);
        signIn.setSize(SignInButton.SIZE_STANDARD);
        signIn.setScopes(gso.getScopeArray());
        setCustomGoogleSigninButton(signIn, "Log in with Google");

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (btnShwHidePaswd.getVisibility() == View.GONE) {

                    btnShwHidePaswd.setVisibility(View.VISIBLE);

                    btnShwHidePaswd.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (btnShwHidePaswd.getText().equals("SHOW")) {

                                btnShwHidePaswd.setText("HIDE");
                                mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                mPasswordView.setSelection(mPasswordView.getText().length());

                            } else if (btnShwHidePaswd.getText().equals("HIDE")) {

                                btnShwHidePaswd.setText("SHOW");
                                mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                mPasswordView.setSelection(mPasswordView.getText().length());

                            }

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setCustomGoogleSigninButton(SignInButton signIn, String btnText) {

        for (int i = 0; i < signIn.getChildCount(); i++) {

            View v = signIn.getChildAt(i);

            if (v instanceof TextView) {

                TextView tv = (TextView) v;
                tv.setText(btnText);
                return;
            }

        }

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            LoginUserTask(email, password);
        }
    }

    private void LoginUserTask(String memail, String mpassword) {

        HashMap<String, Object> post_map = new HashMap<>();
        String prefsUserId = "";

        mProgressView.setVisibility(View.VISIBLE);
        txtNotRegUser.setVisibility(View.GONE);
        mSocialLogin.setVisibility(View.GONE);
        mLoginFormView.setVisibility(View.GONE);

        post_map.put("email", memail);
        post_map.put("password", mpassword);

        String URL = UserVariables.KEY_BASE_URL + "login";

        aq.ajax(URL, post_map, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);

                try {

                    if (object.getString("response").equalsIgnoreCase("success")) {

                        JSONArray array = object.getJSONArray("userdata");

                        JSONObject jobj = array.getJSONObject(0);

                        msgObj = jobj.getString("msg");
//                        IdObj = jobj.getString("user_id");

                        Toast.makeText(LoginActivity.this, "" + msgObj, Toast.LENGTH_SHORT).show();

                        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        editor = preferences.edit();
                        editor.putString("registered", "Authorized");
//                        editor.putString("userId", IdObj);
                        editor.apply();

                        Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        finish();

                    } else {

                        JSONArray jarray = object.getJSONArray("userdata");

                        JSONObject obj = jarray.getJSONObject(0);

                        msgObj = obj.getString("msg");

                        Log.d("consumer", "Msg : " + msgObj);

                        mProgressView.setVisibility(View.GONE);
                        txtNotRegUser.setVisibility(View.VISIBLE);
                        mSocialLogin.setVisibility(View.VISIBLE);
                        mLoginFormView.setVisibility(View.VISIBLE);


                        Toast.makeText(LoginActivity.this, "" + msgObj, Toast.LENGTH_SHORT).show();
                        mPasswordView.requestFocus();
                        mPasswordView.setError("Please check your password");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        mGoogleApiClient.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Login Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.hogoworld.biryanihouse/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
//    }

//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Login Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.hogoworld.biryanihouse/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
//        mGoogleApiClient.disconnect();
//    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.email_sign_in_button) {

            email = mEmailView.getText().toString();
            password = mPasswordView.getText().toString();

            LoginUserTask(email, password);

        }

        if (v.getId() == R.id.sign_in_button) {
            signUp();
        }


    }

    private void signUp() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult gsr = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(gsr);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void handleSignInResult(GoogleSignInResult gsr) {

        if (gsr.isSuccess()) {

            GoogleSignInAccount gsa = gsr.getSignInAccount();
            String userName = gsa.getDisplayName();
            String userEmail = gsa.getEmail();
            String userID = gsa.getId();
            Uri photoUri = gsa.getPhotoUrl();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            String loginType = preferences.getString("loginType", "");

            Log.d("Google details", "Name : " + userName + " Email : " + userEmail + " ID : " + userID);

            if (loginType.equalsIgnoreCase("google")) {

//                Intent homeIntent = new Intent(LoginActivity.this, HomeScreen.class);
//                homeIntent.putExtra("userName", userName);
//                homeIntent.putExtra("userEmail", userEmail);
//                homeIntent.putExtra("userPhoto", photoUri);
//                startActivity(homeIntent);
//                finish();

            } else {

                Toast.makeText(LoginActivity.this, "This google Id is not registered with us.", Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

