package com.hogoworld.biryanihouse;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utility.UserVariables;

import static android.Manifest.permission.READ_CONTACTS;


public class SignInActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

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
    private AutoCompleteTextView mNameView;
    private AutoCompleteTextView mPhoneView;
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
    String conName = "", conEmailAddress = "", conPassword = "", conPhone = "";
    AQuery aq;
    HashMap<String, Object> post_map = new HashMap<>();
    String userID = "";
    String msg = "";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    JSONArray jarray = null;
    JSONObject obj = null;
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

        setContentView(R.layout.activity_sign_in);
        aq = new AQuery(getApplicationContext());
        mEmailView = (AutoCompleteTextView) findViewById(R.id.signup_email);
        mPasswordView = (AutoCompleteTextView) findViewById(R.id.signup_password);
        mNameView = (AutoCompleteTextView) findViewById(R.id.full_name);
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignInButton.setOnClickListener(SignInActivity.this);
        findViewById(R.id.sign_in_button).setOnClickListener(SignInActivity.this);
        findViewById(R.id.signin_button).setOnClickListener(SignInActivity.this);
        mSocialLogin = findViewById(R.id.social_signin);
        loginButton = (LoginButton) findViewById(R.id.signin_button);
        progressText = (TextView) findViewById(R.id.progress_text_signin);
        mLoginFormView = findViewById(R.id.signup_form);
        mSocialLogin = findViewById(R.id.social_signin);
        mProgressView = findViewById(R.id.progress_view_signin);

        //IME options listener for password//
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.signup || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;


            }
        });

        //Google sign in options//
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("user_birthday");

        //facebook login authentication//
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

//                String accToken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        String name = "";
                        String email = "";
                        try {
                            name = object.getString("name");
                            email = object.getString("email");

                            Log.d("Facebook", "Name : " + name + " Email : " + email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();

                Toast.makeText(SignInActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(SignInActivity.this, "Logged in cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SignInActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        //Google sign in button customization//
        SignInButton signIn = (SignInButton) findViewById(R.id.sign_in_button);
        signIn.setSize(SignInButton.SIZE_STANDARD);
        signIn.setScopes(gso.getScopeArray());
        setCustomGoogleSigninButton(signIn, "Sign in with Google");


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

    private void attemptLogin() {


        mNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        //Get the EditText values//
        conName = mNameView.getText().toString();
        conEmailAddress = mEmailView.getText().toString();
        conPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered on//
        if (!TextUtils.isEmpty(conPassword) && !isPasswordValid(conPassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address//
        if (TextUtils.isEmpty(conEmailAddress)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(conEmailAddress)) {
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
            UserSignUpTask(conName, conEmailAddress, conPhone, conPassword, "web");

        }

    }

    private void UserSignUpTask(String conName, String conEmailAddress, String conPhone, String conPassword, String cusType) {

        String mEmail = conEmailAddress;
        String mPassword = conPassword;
        String mName = conName;
        String mPhone = conPhone;
        final String mType = cusType;

        mProgressView.setVisibility(View.VISIBLE);
        mSocialLogin.setVisibility(View.GONE);
        mLoginFormView.setVisibility(View.GONE);

        post_map.put("full_name", mName);
        post_map.put("email", mEmail);
        post_map.put("phone", mPhone);
        post_map.put("password", mPassword);
//        post_map.put("cus_type", mType);

        String URL = UserVariables.KEY_BASE_URL + "insert_customer";

        aq.ajax(URL, post_map, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);

                try {

                    String message = object.getString("message");
                    Toast.makeText(SignInActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    SignInActivity.this.finish();

//                    if (object.getString("response").equals("success")) {
//
//                        jarray = object.getJSONArray("userdata");
//
//                        obj = jarray.getJSONObject(0);
//
//                        msg = obj.getString("message");
//                        userID = obj.getString("response");
//
//                        Log.d("consumer", "Id : " + userID + "Msg : " + msg);
//
//                        preferences = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
//                        editor = preferences.edit();
//                        editor.putString("userId", userID);
//                        editor.putString("registered", "Authorized");
//                        editor.putString("loginType", mType);
//                        editor.apply();
//
//                        Toast.makeText(SignInActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
//
//                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
//                        startActivity(intent);
//                        SignInActivity.this.finish();
//
//                    } else {
//
//                        JSONArray jarray = object.getJSONArray("userdata");
//
//                        JSONObject obj = jarray.getJSONObject(0);
//
//                        msg = obj.getString("msg");
//
//                        Log.d("consumer", "Msg : " + msg);
//
//                        mProgressView.setVisibility(View.GONE);
//                        mSocialLogin.setVisibility(View.VISIBLE);
//                        mLoginFormView.setVisibility(View.VISIBLE);
//
//                        Toast.makeText(SignInActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
//
//                        mEmailView.requestFocus();
//                        mEmailView.setError("This email address is already registered.");
//
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    //Validation for email address//
    private boolean isEmailValid(String emailAddress) {

        return emailAddress.contains("@");
    }

    //Validation for password//
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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

    //Check for contacts permisiion//
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
                    .setAction(android.R.string.ok, new View.OnClickListener() {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        List<String> emails = new ArrayList<>();
        data.moveToFirst();
        while (!data.isAfterLast()) {
            emails.add(data.getString(ProfileQuery.ADDRESS));
            data.moveToNext();
        }

        addEmailsToAutoComplete(emails);

    }

    private void addEmailsToAutoComplete(List<String> emails) {

        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignInActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emails);

        mEmailView.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.email_sign_up_button) {

            conName = mNameView.getText().toString();
            conEmailAddress = mEmailView.getText().toString();
            conPassword = mPasswordView.getText().toString();
            conPhone = mPhoneView.getText().toString();

            Log.d("Deatils", "Consumer Name: " + conName + "\n Email : " + conEmailAddress + "\n Password : " + conPassword + "\n Phone : " + conPhone);
            UserSignUpTask(conName, conEmailAddress, conPhone, conPassword, "web");

        }

        if (v.getId() == R.id.sign_in_button) {
            signup();
        }

    }

    private void signup() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);

    }

    //Pass the result back to activity//
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

            UserSignUpTask(userName, userEmail, "", "", "google");
            Log.d("Google details", "Name : " + userName + "Email : " + userEmail + "ID : " + userID);


        } else {

            Toast.makeText(SignInActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(SignInActivity.this, LoginActivity.class));
        finish();

    }
}
