package com.tabibe.app.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tabibe.app.MainActivity;
import com.tabibe.app.R;
import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.LoginResponse;
import com.tabibe.app.util.Constants;
import com.tabibe.app.util.DialogUtils;
import com.tabibe.app.util.PreferenceConnector;
import com.tabibe.app.util.Utils;
import com.tabibe.app.viewmodel.SignInViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignFragment extends BaseFragment {

    @BindView(R.id.forgetPassword)
    TextView forgetPassword;

    @BindView(R.id.et_email)
    TextInputEditText email;

    @BindView(R.id.et_password)
    TextInputEditText password;

    SignInViewModel signInViewModel;

    private String recoveryEmail;



    public SignFragment() {
        // Required empty public constructor
    }

    public static SignFragment newInstance(){
        SignFragment signFragment = new SignFragment();
        return signFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        ButterKnife.bind(this,view);

        setHasOptionsMenu(true);

//        ((MainActivity)getActivity()).toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios);
        ((MainActivity)getActivity()).title.setText("Se connecter");

        String htmlString="<u>Mot de passe oublie ?</u>";
        forgetPassword.setText(Html.fromHtml(htmlString));

        signInViewModel = ViewModelProviders.of(SignFragment.this).get(SignInViewModel.class);
        signInViewModel.init();


        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_account).setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        clearFragment();
        return super.onOptionsItemSelected(item);
    }

    private void clearFragment() {

        ((MainActivity)getActivity()).toolbar.setNavigationIcon(null);
        ((MainActivity)getActivity()).title.setText(getResources().getString(R.string.app_name));
        ((MainActivity)getActivity()).removeFragment();
    }

    @OnClick(R.id.login)
    public void login(){
        hideKeyboard();
        boolean error = false;
        if(email.getText().toString().isEmpty()){
            DialogUtils.showDialogMessage(getActivity(), "S'il vous plaît entrer email.");
            error = true;
        }

        if(password.getText().toString().isEmpty() && !error){
            DialogUtils.showDialogMessage(getActivity(),"Veuillez entrer le mot de passe.");
            error = true;
        }

        if(Utils.checkInternetConnection(getActivity())){
            if(!error) {

                showProgressDialog("Se connecter...");
                signInViewModel.login(Constants.API_ADMIN, Constants.API_PASSWORD, email.getText().toString(), password.getText().toString());
                observeData();
            }
        }else {
            DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
        }

    }

    private void observeData() {

        signInViewModel.getLoginResponse().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                closeLoadingProgressBar();
                if(loginResponse.getStatus()==200){
                    Gson gson = new Gson();
                    String json = gson.toJson(loginResponse.getLoginUser().get(0));
                    PreferenceConnector.writeString(getActivity(),PreferenceConnector.USER_DATA,json);
                    hideKeyboard();
                    clearFragment();

                }else if(loginResponse.getStatus()==404){
                    DialogUtils.showDialogMessage(getContext(), "Les informations d'identification invalides.");
                }
                else {
                    DialogUtils.showDialogMessage(getContext(),"Quelque chose s'est mal passé.");
                }
            }
        });

    }



    @OnClick(R.id.forgetPassword)
    public void onForgotPassword(){

        showDialog();

    }

    private void observeForgotPassword() {

        signInViewModel.getForgotPassword().observe(this, new Observer<BaseResponse>() {
            @Override
            public void onChanged(BaseResponse baseResponse) {

                closeLoadingProgressBar();
                recoveryEmail="";
                if(baseResponse.getStatus()==200){
                    DialogUtils.showDialogMessage(getContext(),"Veuillez vérifier votre email pour définir un nouveau mot de passe");
                }else if(baseResponse.getStatus()==404){
                    DialogUtils.showDialogMessage(getContext(),
                            "Le courrier électronique fourni n'est pas enregistré. Veuillez créer\n" +
                            "nouveau compte");
                }else {
                    DialogUtils.showDialogMessage(getContext(),"Quelque chose s'est mal passé.");
                }
            }
        });

    }

    private void showDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final EditText edittext = new EditText(getActivity());
        alert.setMessage("Mot de passe oublié");
        alert.setTitle("Entrez l'adresse e-mail de récupération");

        alert.setView(edittext);

        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                recoveryEmail = edittext.getText().toString();
                if(recoveryEmail!=null && !recoveryEmail.isEmpty()){

                    if(Utils.checkInternetConnection(getActivity())){

                        showProgressDialog("Se te olvidó tu contraseña...");
                        signInViewModel.forgotPassword(Constants.API_ADMIN, Constants.API_PASSWORD, recoveryEmail);
                        observeForgotPassword();

                    }else {
                        DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
                    }

                }else {
                    DialogUtils.showDialogMessage(getContext(), "S'il vous plaît fournir un email de récupération");
                }
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
