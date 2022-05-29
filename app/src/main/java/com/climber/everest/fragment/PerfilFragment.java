package com.climber.everest.fragment;

import static android.content.ContentValues.TAG;
import static com.climber.everest.activity.LoginActivity._usuarioLogado;
import static com.climber.everest.activity.LoginActivity.apiConfig;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.climber.everest.activity.AlterarUsuarioActivity;
import com.climber.everest.R;
import com.climber.everest.model.Resultado;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;

import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btnAlterarSenha;
    private Button btnCancelarConta;
    private TextView emailUsuario;
    private Retrofit retrofit;
    private TextView diasAtivo;
    private TextView nomeUsuario;
    private ImageView alterarConta;
    private CircleImageView imagemPerfil;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Resultado resReq;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        nomeUsuario = view.findViewById(R.id.nomeUsuario);
        emailUsuario = view.findViewById(R.id.emailUsuario);
        alterarConta = view.findViewById(R.id.alterarConta);

        geraDadosUsuario(view);

        alterarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(  view.getContext(), AlterarUsuarioActivity.class);
                startActivity(i);

                FragmentManager fm = getActivity().getSupportFragmentManager();

                HomeFragment homeFrag = new HomeFragment();

                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.mainFrameLayout, homeFrag);
                ft.addToBackStack(null);

                ft.commit();
            }
        });

        return view;
    }

    private void geraDadosUsuario(View view)
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();

            if(apiConfig.token != "")
            {
                Log.e(TAG, apiConfig.token);
                nomeUsuario.setText(_usuarioLogado.nomeusuario);
                emailUsuario.setText(_usuarioLogado.emailusuario);
            }
            else
            {
                Toast toast = Toast.makeText(getActivity(), "Houve um erro ao buscar os eventos", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}