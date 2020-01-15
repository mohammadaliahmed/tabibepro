package com.tabibe.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginFragment;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.tabibe.app.fragment.InformationFragment;
import com.tabibe.app.fragment.LocationSearchFragment;
import com.tabibe.app.fragment.MyAppointments;
import com.tabibe.app.fragment.SearchFragment;
import com.tabibe.app.fragment.SignFragment;
import com.tabibe.app.fragment.SignUpFragment;
import com.tabibe.app.fragment.SpecialityFragment;

import com.tabibe.app.model.FacebookProfileModel;
import com.tabibe.app.model.LoginResponse;
import com.tabibe.app.model.LoginUser;
import com.tabibe.app.util.Constants;
import com.tabibe.app.util.DialogUtils;
import com.tabibe.app.util.PreferenceConnector;
import com.tabibe.app.util.Utils;
import com.tabibe.app.viewmodel.MainViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.my_toolbar)
    public Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    public TextView title;

    @BindView(R.id.fragmentContainer)
    FrameLayout frameLayout;

    @BindView(R.id.speciality_search)
    Button specialityBtn;

    @BindView(R.id.location_search)
    Button cityBtn;

    @BindView(R.id.signInLayout)
    ScrollView signInLayout;

    @BindView(R.id.et_email)
    TextInputEditText email;

    @BindView(R.id.et_password)
    TextInputEditText password;

    @BindView(R.id.bottomImage)
    ImageView bottomImage;

    private PrimaryDrawerItem appointment, information, document, logout;
    private ProfileDrawerItem profileDrawerItem;

    private Drawer mDrawer;
    private AccountHeader accountHeader;

    private String cityId = "-1";
    private String specialityId = "-1";
    private String speialityName = "";
    private String cityName = "";

    MainViewModel mainViewModel;


    LoginButton login_button;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    public static FacebookProfileModel profile;
    GoogleApiClient apiClient;
    SignInButton google;
    public static GoogleSignInAccount account;
    public static String emailFromFB;
    RelativeLayout fb_btn, google_btn;
    Button signup, login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        apiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        int arg = getIntent().getIntExtra("myappointments", 0);
        if (arg != 0) {

            replaceMyAppointment();
        }

        mainViewModel = ViewModelProviders.of(MainActivity.this).get(MainViewModel.class);
        mainViewModel.init();
        setUpToolbar();
        setUpDrawer();


        callbackManager = CallbackManager.Factory.create();
        signup = findViewById(R.id.signup);
        login_button = findViewById(R.id.login_button);
        google = findViewById(R.id.google);
        fb_btn = findViewById(R.id.fb_btn);
        google_btn = findViewById(R.id.google_btn);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFragment();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = null;
                profile = null;
                signUp();
            }
        });

        fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_button.performClick();
            }
        });

        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                google.performClick();
                hideKeyboard();
                Intent i = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                startActivityForResult(i, 100);
            }
        });


        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                Intent i = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                startActivityForResult(i, 100);
            }
        });

        login_button.setReadPermissions(Arrays.asList("public_profile", "email"));
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    getPackageName(),
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        }
//        catch (PackageManager.NameNotFoundException e) {
//
//        }
//        catch (NoSuchAlgorithmException e) {
//
//        }

        login_button.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
//                        profile = Profile.getCurrentProfile();

                        hideKeyboard();
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {
                                            MainActivity.account = null;
                                            emailFromFB = object.getString("email");
                                            String firstName = object.getString("name").split(" ")[0];
                                            String lastName = object.getString("name").split(" ")[1];
                                            String id = object.getString("id");

                                            profile = new FacebookProfileModel(id, firstName, lastName, emailFromFB);

//                                            String birthday = object.getString("birthday"); // 01/31/1980 format
                                            mainViewModel.login(Constants.API_ADMIN, Constants.API_PASSWORD, emailFromFB,
                                                    id);
                                            observeData();


                                        } catch (Exception e) {
                                            LoginManager.getInstance().logOut();
                                            Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            if (e.getMessage().equalsIgnoreCase("No value for email")) {
                                                DialogUtils.showDialogMessage(MainActivity.this,
                                                        "Vous avez aucune adresse mail configurée sur votre compte Face Book. Veuillez vous inscrire par Google ou votre adresse mail personnelle");
                                            }
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();


                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        DialogUtils.showDialogMessage(MainActivity.this,
                                "Vous avez aucune adresse mail configurée sur votre compte Face Book. Veuillez vous inscrire par Google ou votre adresse mail personnelle");

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(googleSignInResult);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void handleResult(GoogleSignInResult googleSignInResult) {
        if (googleSignInResult.isSuccess()) {
            account = googleSignInResult.getSignInAccount();
            MainActivity.profile = null;
            String userId = account.getEmail().replace("@", "").replace(".", "");
            String email = account.getEmail();
            mainViewModel.login(Constants.API_ADMIN, Constants.API_PASSWORD, account.getEmail(),
                    account.getId());
            observeData();
            Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {

                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkIfUserLogin()) {
            hideUserLogin();
        } else {
            showUserLogin();
        }

    }

    private boolean checkIfUserLogin() {
        String userData = PreferenceConnector.readString(this, PreferenceConnector.USER_DATA, null);
        if (userData != null) {
            return true;

        } else {
            return false;
        }
    }

    private void hideUserLogin() {
        signInLayout.setVisibility(View.GONE);
        bottomImage.setVisibility(View.VISIBLE);
    }

    private void showUserLogin() {
        bottomImage.setVisibility(View.GONE);
        signInLayout.setVisibility(View.VISIBLE);
    }

    private void setUpToolbar() {

        setSupportActionBar(toolbar);
        title.setText(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpDrawer() {

        initiatePrimaryDrwaerItem();
        setUpAccoundHeader();
        buildDrawer();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        menu.findItem(R.id.action_account).setIcon(R.drawable.drawer_icon);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_account) {

            String userData = PreferenceConnector.readString(this, PreferenceConnector.USER_DATA, null);

            if (userData == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, SignFragment.newInstance()).addToBackStack("search_fragment").commit();
            } else {
                Gson gson = new Gson();
                LoginUser loginUser = gson.fromJson(userData, LoginUser.class);
                if (profileDrawerItem.getIdentifier() == 5) {

                    profileDrawerItem.withName(loginUser.getFirst_name() + " " + loginUser.getLast_name());
                    profileDrawerItem.withEmail(loginUser.getPhone());
                    accountHeader.updateProfile(profileDrawerItem);
                }

                if (mDrawer.isDrawerOpen()) {
                    mDrawer.closeDrawer();
                } else {
                    mDrawer.openDrawer();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void initiatePrimaryDrwaerItem() {

        appointment = new PrimaryDrawerItem().withIdentifier(1).withName(getString(R.string.my_appointment_drawer)).withIcon(R.drawable.appointment_drawer);
        information = new PrimaryDrawerItem().withIdentifier(2).withName(getString(R.string.my_information_drawer)).withIcon(R.drawable.information_drawer);
        document = new PrimaryDrawerItem().withIdentifier(3).withName(getString(R.string.documents_drawer)).withIcon(R.drawable.document_drawer);
        profileDrawerItem = new ProfileDrawerItem().withIdentifier(5);
        logout = new PrimaryDrawerItem().withName(getString(R.string.logout_drawer)).withIcon(R.drawable.logout_drawer).withSelectable(false).withSelectedBackgroundAnimated(true);
    }


    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            title.setText(getResources().getString(R.string.app_name));
            toolbar.setNavigationIcon(null);
            getSupportFragmentManager().popBackStack();
        } else
            super.onBackPressed();
    }


    @OnClick(R.id.location_search)
    public void onLocationClicked() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, LocationSearchFragment.newInstance()).addToBackStack("location_fragment").commit();

    }

    @OnClick(R.id.speciality_search)
    public void onSpecialityClicked() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, SpecialityFragment.newInstance()).addToBackStack("speciality_fragment").commit();

    }

    @OnClick(R.id.doc_search_by_name)
    public void onDocSearchClicked() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, SearchFragment.newInstance(Constants.MAIN_SCREEN, "-1", "-1")).addToBackStack("search_fragment").commit();

    }

    @OnClick(R.id.doc_search)
    public void onSearchClicked() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, SearchFragment.newInstance(Constants.MAIN_SCREEN, getSpecialityId(), getCityId())).addToBackStack("search_fragment").commit();

        setSpecialityId("-1", "");
        setCityId("-1", "");
        specialityBtn.setText("Spécialité");
        cityBtn.setText("Ville");
    }

    private void buildDrawer() {

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withInnerShadow(true)
                .withAccountHeader(accountHeader).withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        appointment,
                        information,
                        document
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {

                            case -1:
                                logoutFromApp();
                                break;
                            case 1:
                                replaceMyAppointment();

                                break;
                            case 2:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragmentContainer, InformationFragment.newInstance()).addToBackStack("information_fragment").commit();
                                break;
                            case 3:

                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                }).build();


        mDrawer.addStickyFooterItem(logout);
        mDrawer.setGravity(Gravity.RIGHT);
        mDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        mDrawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void replaceMyAppointment() {
        if (checkIfUserLogin()) {
            hideUserLogin();
        } else {
            showUserLogin();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, MyAppointments.newInstance()).addToBackStack("my_appointments_fragment").commit();
    }

    private void logoutFromApp() {

        PreferenceConnector.writeString(this, PreferenceConnector.USER_DATA, null);
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        showUserLogin();
        DialogUtils.showDialogMessage(this, "Déconnecté avec succès");
    }

    private void setUpAccoundHeader() {
        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                .withSelectionListEnabled(false)
                .withDividerBelowHeader(true)
                .withProfileImagesVisible(false)
                .addProfiles(
                        profileDrawerItem
                )
                .build();
    }

    public String getCityId() {
        return cityId;
    }

    public String getSpecialityId() {
        return specialityId;
    }

    public void setCityId(String cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
        cityBtn.setText("Ville" + " (" + cityName + ")");
    }

    public void setSpecialityId(String specialityId, String speialityName) {
        this.specialityId = specialityId;
        this.speialityName = speialityName;
        specialityBtn.setText("Spécialité" + " (" + speialityName + ")");

    }

    @OnClick(R.id.login)
    public void login() {

        boolean error = false;
        if (email.getText().toString().isEmpty()) {
            DialogUtils.showDialogMessage(this, "S'il vous plaît entrer email.");
            error = true;
        }

        if (password.getText().toString().isEmpty() && !error) {
            DialogUtils.showDialogMessage(this, "Veuillez entrer le mot de passe.");
            error = true;
        }

        if (Utils.checkInternetConnection(this)) {
            if (!error) {

                showProgressDialog("Se connecter...");
                mainViewModel.login(Constants.API_ADMIN, Constants.API_PASSWORD, email.getText().toString(), password.getText().toString());
                observeData();
            }
        } else {
            DialogUtils.showDialogMessage(this, getString(R.string.no_network));
        }
    }

    private void observeData() {

        mainViewModel.getLoginResponse().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                closeLoadingProgressBar();
                LoginManager.getInstance().logOut();
                if (loginResponse.getStatus() == 200) {
                    Gson gson = new Gson();
                    String json = gson.toJson(loginResponse.getLoginUser().get(0));
                    PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.USER_DATA, json);
                    password.setText("");
                    email.setText("");
                    hideUserLogin();


                } else if (loginResponse.getStatus() == 404) {
                    if (profile != null || account != null) {
                        signUp();
                    } else {

                        DialogUtils.showDialogMessage(MainActivity.this, "Les informations d'identification invalides.");
                    }
                } else {
                    if (profile != null || account != null) {
                        signUp();
                    } else {
                        DialogUtils.showDialogMessage(MainActivity.this, "Quelque chose s'est mal passé.");
                    }
                }
            }
        });

    }


    public void signUp() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, SignUpFragment.newInstance()).addToBackStack("signup_fragment").commit();

    }

    public void loginFragment() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, SignFragment.newInstance()).addToBackStack("signin_fragment").commit();

    }

    public void removeFragment() {

        if (checkIfUserLogin()) {
            hideUserLogin();
        } else {
            showUserLogin();
        }
        getSupportFragmentManager().popBackStack();
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(new View(this).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
