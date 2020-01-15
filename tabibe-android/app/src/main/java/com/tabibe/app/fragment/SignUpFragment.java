package com.tabibe.app.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.tabibe.app.MainActivity;
import com.tabibe.app.R;
import com.tabibe.app.model.LoginResponse;
import com.tabibe.app.model.SignUpResponse;
import com.tabibe.app.util.ConstantValues;
import com.tabibe.app.util.Constants;
import com.tabibe.app.util.DialogUtils;
import com.tabibe.app.util.IEventlistener;
import com.tabibe.app.util.PreferenceConnector;
import com.tabibe.app.util.Utils;
import com.tabibe.app.viewmodel.SignInViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends BaseFragment implements IEventlistener {

    @BindView(R.id.et_phone)
    TextInputEditText phoneNumher;

    @BindView(R.id.et_email)
    TextInputEditText email;

    @BindView(R.id.et_confirmmail)
    TextInputEditText confirmEmail;

    @BindView(R.id.et_password)
    TextInputEditText password;

    @BindView(R.id.et_firstName)
    TextInputEditText firstName;

    @BindView(R.id.et_lastname)
    TextInputEditText lastName;
    TextView passwordText;

    SignInViewModel signInViewModel;

    com.google.android.material.textfield.TextInputLayout passFeild;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {

        Bundle args = new Bundle();

        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);

        signInViewModel = ViewModelProviders.of(SignUpFragment.this).get(SignInViewModel.class);
        signInViewModel.init();

        passwordText = view.findViewById(R.id.passwordText);
        passFeild = view.findViewById(R.id.passFeild);
        if (MainActivity.profile != null) {
            firstName.setText(MainActivity.profile.getFirstName());
            lastName.setText(MainActivity.profile.getLastName());
            email.setText(MainActivity.emailFromFB);
            confirmEmail.setText(MainActivity.emailFromFB);
            password.setText(MainActivity.profile.getId());
            passFeild.setVisibility(View.GONE);
            passwordText.setVisibility(View.GONE);
            ConstantValues.SOCIAL_REGISTER = "Y";
            DialogUtils.showDialogMessage(getActivity(), getString(R.string.socialMessage));



        }
        if (MainActivity.account != null) {
            firstName.setText(MainActivity.account.getDisplayName().split(" ")[0]);
            lastName.setText(MainActivity.account.getDisplayName().split(" ")[1]);
            email.setText(MainActivity.account.getEmail());
            confirmEmail.setText(MainActivity.account.getEmail());
            password.setText(MainActivity.account.getId());
            passFeild.setVisibility(View.GONE);
            passwordText.setVisibility(View.GONE);
            ConstantValues.SOCIAL_REGISTER = "Y";
            DialogUtils.showDialogMessage(getActivity(), getString(R.string.socialMessage));


        }


        return view;
    }

    private boolean validateData() {

        if (phoneNumher.getText().toString().isEmpty() || email.getText().toString().isEmpty() || confirmEmail.getText().toString().isEmpty() || password.getText().toString().isEmpty() || firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty()) {
            DialogUtils.showDialogMessage(getContext(), "S'il vous plaît entrer toutes les informations.");
            return false;
        }

        if (!email.getText().toString().equals(confirmEmail.getText().toString())) {
            DialogUtils.showDialogMessage(getContext(),
                    "Email et confirmer l'email n'est pas la même. Veuillez entrer à nouveau.");
            return false;
        }

        if (phoneNumher.getText().toString().length() != 10) {
            DialogUtils.showDialogMessage(getContext(),
                    "Entrez s'il vous plaît un numéro de téléphone valide.");
            return false;
        }

        if (ConstantValues.SOCIAL_REGISTER.equalsIgnoreCase("Y")) {

        } else {
            if (!validatePassword(password.getText().toString())) {
                DialogUtils.showDialogMessage(getContext(), " Le mot de passe doit contenir au\n" +
                        "moins 8 caractères avec une lettre majuscule,\n" +
                        "minuscule, un caractère spécial (&,@,# ,$..) et des\n" +
                        "chiffres");
                return false;
            }
        }

        return true;

    }

    public boolean validatePassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&$%]).{8,20})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    @OnClick(R.id.signUpBtn)
    public void signUpBtnClicked() {
        hideKeyboard();
        if (validateData()) {

            if (Utils.checkInternetConnection(getActivity())) {

                showProgressDialog("S'inscrire");
                signInViewModel.signUp(Constants.API_ADMIN, Constants.API_PASSWORD, firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), password.getText().toString(), phoneNumher.getText().toString());
                observerData();
            } else {
                DialogUtils.showDialogMessage(getActivity(), getString(R.string.no_network));
            }
        }

    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(new View(getContext()).getWindowToken(), InputMethodManager.RESULT_HIDDEN);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    private void observerData() {

        signInViewModel.getSignUpResponse().observe(this, new Observer<SignUpResponse>() {
            @Override
            public void onChanged(SignUpResponse signUpResponse) {
                closeLoadingProgressBar();
                if (signUpResponse.getStatus() == 200) {
                    if (ConstantValues.SOCIAL_REGISTER.equalsIgnoreCase("Y")) {
                        signInViewModel.login(Constants.API_ADMIN, Constants.API_PASSWORD, email.getText().toString(), password.getText().toString());
                        observeLoginData();
                    } else {
                        firstName.setText("");
                        lastName.setText("");
                        phoneNumher.setText("");
                        password.setText("");
                        email.setText("");
                        confirmEmail.setText("");
                        DialogUtils.showDialogWithCallBack(getContext(), "",
                                "Bienvenue sur Tabibe. Vous recevrez un E-mail de confirmation sous peu.Vérifiez votre boite mail pour activer votre compte en ligne.", SignUpFragment.this, "Oui", "");

                    }
                } else if (signUpResponse.getStatus() == 404) {
                    DialogUtils.showDialogMessage(getContext(), signUpResponse.getMessage());
                } else {
                    DialogUtils.showDialogMessage(getContext(), "Quelque chose s'est mal passé.");
                }

            }
        });
    }

    private void observeLoginData() {

        signInViewModel.getLoginResponse().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                closeLoadingProgressBar();
                if (loginResponse.getStatus() == 200) {
                    Gson gson = new Gson();
                    String json = gson.toJson(loginResponse.getLoginUser().get(0));
                    PreferenceConnector.writeString(getContext(), PreferenceConnector.USER_DATA, json);
                    password.setText("");
                    email.setText("");
//                    hideUserLogin();
                    clearFragment();


                } else if (loginResponse.getStatus() == 404) {
//                    signUp();
                    DialogUtils.showDialogMessage(getContext(), "Les informations d'identification invalides.");

                } else {
//                    signUp();
                    DialogUtils.showDialogMessage(getContext(), "Quelque chose s'est mal passé.");
                }
            }
        });


    }


    private void clearFragment() {

        ((MainActivity) getActivity()).toolbar.setNavigationIcon(null);
        ((MainActivity) getActivity()).title.setText(getResources().getString(R.string.app_name));
        ((MainActivity) getActivity()).removeFragment();
    }

    @Override
    public void onSuccess() {
        clearFragment();
    }

    @Override
    public void onCancel() {
        clearFragment();
    }
}
