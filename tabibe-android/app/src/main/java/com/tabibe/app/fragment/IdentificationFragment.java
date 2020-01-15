package com.tabibe.app.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tabibe.app.R;
import com.tabibe.app.activity.DoctorProfileActivity;
import com.tabibe.app.model.Doctor;
import com.tabibe.app.model.SignUpResponse;
import com.tabibe.app.model.LoginResponse;
import com.tabibe.app.util.Constants;
import com.tabibe.app.util.DialogUtils;
import com.tabibe.app.util.PreferenceConnector;
import com.tabibe.app.util.Utils;
import com.tabibe.app.viewmodel.SignInViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class IdentificationFragment extends BaseFragment {

    @BindView(R.id.signInLayout)
    LinearLayout singInLayout;

    @BindView(R.id.signUpLayout)
    LinearLayout signUpLayout;

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

    @BindView(R.id.et_email_sign_in)
    TextInputEditText emailSignIn;

    @BindView(R.id.et_password_sing_in)
    TextInputEditText passwordSignIn;

    SignInViewModel signInViewModel;



    private Doctor doctor;
    private String cid;
    private String appointmentDate;
    private String timeSlot;
    private String consultationReason;

    public IdentificationFragment() {
        // Required empty public constructor
    }

    public static IdentificationFragment newInstance(Doctor doctor, String consultationId, String appointmentDate , String slot,String cr) {

        Bundle args = new Bundle();

        IdentificationFragment fragment = new IdentificationFragment();
        args.putSerializable("doctor",doctor);
        args.putString("consultationId",consultationId);
        args.putSerializable("appointmentDate",appointmentDate);
        args.putString("slot",slot);
        args.putString("consultationReason",cr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_identification, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);

        doctor = (Doctor) getArguments().getSerializable("doctor");
        cid = getArguments().getString("consultationId");
        appointmentDate = getArguments().getString("appointmentDate");
        timeSlot = getArguments().getString("slot");
        consultationReason = getArguments().getString("consultationReason");

        signInViewModel = ViewModelProviders.of(IdentificationFragment.this).get(SignInViewModel.class);
        signInViewModel.init();

        ((DoctorProfileActivity)getActivity()).toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ((DoctorProfileActivity)getActivity()).removeFragment();
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.signUpBtn)
    public void signUpBtnClicked(){

        if(signUpLayout.getVisibility()==View.GONE){
            signUpLayout.setVisibility(View.VISIBLE);
            singInLayout.setVisibility(View.GONE);
            return;
        }
        else {
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

    }

    private void observerData() {

        signInViewModel.getSignUpResponse().observe(this, new Observer<SignUpResponse>() {
            @Override
            public void onChanged(SignUpResponse signUpResponse) {
                closeLoadingProgressBar();
                if(signUpResponse.getStatus()==200){
                    firstName.setText("");
                    lastName.setText("");
                    phoneNumher.setText("");
                    password.setText("");
                    email.setText("");
                    confirmEmail.setText("");
                    DialogUtils.showDialogMessage(getContext(),
                            "Bienvenue sur Tabibe. Vous recevrez un E-mail de confirmation sous peu.Vérifiez votre boite mail pour activer votre compte en ligne.");
                    signUpLayout.setVisibility(View.GONE);
                    singInLayout.setVisibility(View.VISIBLE);
                }else if(signUpResponse.getStatus()==404){
                    DialogUtils.showDialogMessage(getContext(), signUpResponse.getMessage());
                }
                else {
                    DialogUtils.showDialogMessage(getContext(),"Quelque chose s'est mal passé.");
                }

            }
        });
    }

    private boolean validateData(){

        if(phoneNumher.getText().toString().isEmpty() ||email.getText().toString().isEmpty() || confirmEmail.getText().toString().isEmpty() || password.getText().toString().isEmpty() || firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty())
        {
            DialogUtils.showDialogMessage(getContext(),"S'il vous plaît entrer toutes les informations.");
            return false;
        }

        if(!email.getText().toString().equals(confirmEmail.getText().toString())){
            DialogUtils.showDialogMessage(getContext(),
                    "Email et confirmer l'email n'est pas la même. Veuillez entrer à nouveau.");
            return false;
        }

        if(phoneNumher.getText().toString().length()!=10){
            DialogUtils.showDialogMessage(getContext(),
                    "Entrez s'il vous plaît un numéro de téléphone valide.");
            return false;
        }

        if(!validatePassword(password.getText().toString())){
            DialogUtils.showDialogMessage(getContext()," Le mot de passe doit contenir au\n" +
                    "moins 8 caractères avec une lettre majuscule,\n" +
                    "minuscule, un caractère spécial (&,@,# ,$..) et des\n" +
                    "chiffres");
            return false;
        }

        return true;

    }


    public boolean validatePassword(final String password){
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&$%]).{8,20})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    @OnClick(R.id.signInBtn)
    public void signInBtnClicked(){

        if(singInLayout.getVisibility()==View.GONE) {
            signUpLayout.setVisibility(View.GONE);
            singInLayout.setVisibility(View.VISIBLE);
            return;
        }
        else {
            boolean error = false;
            if (emailSignIn.getText().toString().isEmpty()) {
                DialogUtils.showDialogMessage(getActivity(), "S'il vous plaît entrer email.");
                error = true;
            }

            if (passwordSignIn.getText().toString().isEmpty() && !error) {
                DialogUtils.showDialogMessage(getActivity(), "Veuillez entrer le mot de passe.");
                error = true;
            }

            if (Utils.checkInternetConnection(getActivity())) {
                if (!error) {

                    showProgressDialog("Se connecter...");
                    signInViewModel.login(Constants.API_ADMIN, Constants.API_PASSWORD, emailSignIn.getText().toString(), passwordSignIn.getText().toString());
                    observeData();
                }
            } else {
                DialogUtils.showDialogMessage(getActivity(), getString(R.string.no_network));
            }
        }
    }

    private void observeData() {

        signInViewModel.getLoginResponse().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                closeLoadingProgressBar();
                if (loginResponse.getStatus() == 200) {
                    Gson gson = new Gson();
                    String json = gson.toJson(loginResponse.getLoginUser().get(0));
                    PreferenceConnector.writeString(getActivity(), PreferenceConnector.USER_DATA, json);
                    passwordSignIn.setText("");
                    emailSignIn.setText("");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, PatientListFragment.newInstance(doctor,cid,appointmentDate,timeSlot,consultationReason)).addToBackStack("identification_frag").commit();

                } else if (loginResponse.getStatus() == 404) {
                    DialogUtils.showDialogMessage(getContext(), "Les informations d'identification invalides.");
                } else {
                    DialogUtils.showDialogMessage(getContext(), "Quelque chose s'est mal passé.");
                }
            }
        });

        }
    }
