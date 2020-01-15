package com.tabibe.app.fragment;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tabibe.app.R;
import com.tabibe.app.adapter.SiblingAdapter;
import com.tabibe.app.adapter.SiblingEditClicked;
import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.LoginUser;
import com.tabibe.app.model.Patient;
import com.tabibe.app.model.UserResponse;
import com.tabibe.app.util.Constants;
import com.tabibe.app.util.DialogUtils;
import com.tabibe.app.util.PreferenceConnector;
import com.tabibe.app.util.Utils;
import com.tabibe.app.viewmodel.SiblingInformationViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SiblingsInformationFragment extends BaseFragment implements SiblingEditClicked {

    private SiblingAdapter siblingAdapter;

    private SiblingInformationViewModel siblingInformationViewModel;

    @BindView(R.id.recyclerView_siblings)
    RecyclerView recyclerView;

    @BindView(R.id.addUpdateSibling)
    LinearLayout linearLayout;

    @BindView(R.id.et_firstName)
    TextInputEditText firstName;

    @BindView(R.id.et_lastname)
    TextInputEditText lastName;

    @BindView(R.id.addUpdateSiblingBtn)
    Button addUpdate;

    LoginUser loginUser;

    Patient patient;

    public SiblingsInformationFragment() {
        // Required empty public constructor
    }

   public static SiblingsInformationFragment newInstance() {
        SiblingsInformationFragment fragment = new SiblingsInformationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_siblings_information, container, false);
        ButterKnife.bind(this,view);

        siblingInformationViewModel = ViewModelProviders.of(SiblingsInformationFragment.this).get(SiblingInformationViewModel.class);
        siblingInformationViewModel.init();

        setUpRecyclerView();
        fetchUsers();

        return view;
    }

    private void fetchUsers() {
        if(Utils.checkInternetConnection(getActivity())) {
            showProgressDialog("Trayendo hermanos...");
            String userData = PreferenceConnector.readString(getContext(),PreferenceConnector.USER_DATA,null);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userData, LoginUser.class);
            siblingInformationViewModel.fetchUsers(Constants.API_ADMIN, Constants.API_PASSWORD,String.valueOf(loginUser.getId()));
            observeData();
        }else {
            DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
        }

    }

    private void observeData() {

        siblingInformationViewModel.getUserResponse().observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {

                closeLoadingProgressBar();
                if(userResponse.getStatus()==200){

                    siblingAdapter.addItems(userResponse.getuResponses().get(0).getPatients());
                    recyclerView.setAdapter(siblingAdapter);
                }
                else {
                    DialogUtils.showDialogMessage(getContext(),"Quelque chose s'est mal passé.");
                }
            }
        });


    }


    private void setUpRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),0));
        siblingAdapter = new SiblingAdapter(new ArrayList<Patient>(),this);
    }


    @OnClick(R.id.fab)
    public void onClick(){

        linearLayout.setVisibility(View.VISIBLE);
        addUpdate.setText("Ajouter");

    }

    @OnClick(R.id.addUpdateSiblingBtn)
    public void addUpdate(){

        if(firstName.getText()==null && lastName.getText()==null && !firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty())
        {
            DialogUtils.showToast(getContext(),"Por favor complete toda la información");
        }else {

            if(addUpdate.getText()!=null && !addUpdate.getText().toString().isEmpty()){
                if(addUpdate.getText().toString().contains("Act")){
                    showProgressDialog("Agregar hermanos");
                    siblingInformationViewModel.updateMember(Constants.API_ADMIN, Constants.API_PASSWORD,String.valueOf(patient.getId()),firstName.getText().toString(),lastName.getText().toString());
                    observeDeleteData();
                }else {
                    showProgressDialog(
                            "Actualizando hermanos");
                    siblingInformationViewModel.addMember(Constants.API_ADMIN, Constants.API_PASSWORD,String.valueOf(loginUser.getId()),firstName.getText().toString(),lastName.getText().toString());
                    observeDeleteData();
                }
            }
        }
    }

    @Override
    public void editSibling(Patient uResponse) {

        this.patient = uResponse;
        linearLayout.setVisibility(View.VISIBLE);
        firstName.setText(uResponse.getFirst_name());
        lastName.setText(uResponse.getLast_name());
        addUpdate.setText("Actualizar");
    }

    @Override
    public void deleteSibling(Patient uResponse) {

        if(Utils.checkInternetConnection(getActivity())) {
            showProgressDialog("Eliminar hermano...");
            siblingInformationViewModel.deleteMember(Constants.API_ADMIN, Constants.API_PASSWORD,String.valueOf(uResponse.getId()));
            observeDeleteData();
        }else {
            DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
        }

    }

    private void observeDeleteData() {

        siblingInformationViewModel.getDeletedUserResponse().observe(this, new Observer<BaseResponse>() {
            @Override
            public void onChanged(BaseResponse baseResponse) {
                closeLoadingProgressBar();
                if(baseResponse.getStatus()==200){
                        addUpdate.setText("");
                        firstName.setText("");
                        lastName.setText("");
                        linearLayout.setVisibility(View.GONE);
                        fetchUsers();
                }
                else {
                    DialogUtils.showDialogMessage(getContext(),"Quelque chose s'est mal passé.");
                }
            }
        });

    }
}
