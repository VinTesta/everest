package com.climber.everest.fragment;

import static com.climber.everest.activity.LoginActivity.apiConfig;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.climber.everest.R;
import com.climber.everest.activity.MainActivity;
import com.climber.everest.adapter.AdapterEvento;
import com.climber.everest.config.RetrofitConfig;
import com.climber.everest.model.Evento;
import com.climber.everest.model.Resultado;
import com.climber.everest.services.ApiService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment
        implements SearchView.OnQueryTextListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerCardEvento;
    private List<Evento> eventos = new ArrayList<>();
    private AdapterEvento adapterEvento;
    private SearchView txtSearch;
    private AppCompatImageView btnOpenSearch;
    private Retrofit retrofit;
    private Resultado resReq;
    private DatabaseReference mDatabase;
    private ProgressBar cardViewProgressBar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cardViewProgressBar = view.findViewById(R.id.cardViewProgressBar);
        // region Instanciando o serchview
        txtSearch = (SearchView) view.findViewById(R.id.txtSearch);
        btnOpenSearch = (AppCompatImageView) view.findViewById(R.id.btnOpenSearch);
        //endregion

        // region Instanciando a retrofit
        retrofit = RetrofitConfig.getRetrofit();
        buscaEventos(view);
        // endregion
        return view;
    }


    // region Busca eventos
    private void buscaEventos(View view)
    {
        ApiService apiService = retrofit.create(ApiService.class);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();

            if(apiConfig.token != "")
            {
                apiService.buscaEventos(apiConfig.token)
                        .enqueue(new Callback<Resultado>() {
                            @Override
                            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                                if(response.isSuccessful())
                                {
                                    resReq = response.body();
                                    configRecyclerViewEventos(resReq.eventos, view);
                                }
                            }

                            @Override
                            public void onFailure(Call<Resultado> call, Throwable t) {
                                Log.e("Error", "O erro foi: " + t.toString());
                            }
                        });
            }
            else
            {
                Toast toast = Toast.makeText(getActivity(), "Houve um erro ao buscar os eventos", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
    // endregion

    // region CONFIGURAR RECYCLER VIEW
    public void configRecyclerViewEventos(ArrayList<Evento> eventos, View view)
    {
        adapterEvento = new AdapterEvento(eventos, getActivity());
        adapterEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        recyclerCardEvento = (RecyclerView) view.findViewById(R.id.recyclerCardEvento);
        recyclerCardEvento.setHasFixedSize(true);
        recyclerCardEvento.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerCardEvento.setAdapter(adapterEvento);
        recyclerCardEvento.setVisibility(View.VISIBLE);
        cardViewProgressBar.setVisibility(View.GONE);

        // region SEARCH VIEW EVENTOS
        btnOpenSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOpenSearch.setVisibility(View.GONE);
                txtSearch.setVisibility(View.VISIBLE);
            }
        });

        txtSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                btnOpenSearch.setVisibility(View.VISIBLE);
                txtSearch.setVisibility(View.GONE);
                return false;
            }
        });

        txtSearch.setOnQueryTextListener(this);
        // endregion
    }
    // endregion

    // region Funções do searchview
    public boolean onQueryTextSubmit(String s) {
        adapterEvento.filtrar(s);
        return false;
    }

    public boolean onQueryTextChange(String s) {
        adapterEvento.filtrar(s);
        return false;
    }
    // endregion
}