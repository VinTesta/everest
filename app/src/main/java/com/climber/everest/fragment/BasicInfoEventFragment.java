package com.climber.everest.fragment;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.climber.everest.R;
import com.climber.everest.config.RetrofitConfig;
import com.climber.everest.model.Evento;
import com.climber.everest.services.InterfaceComunicacaoFragment;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasicInfoEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicInfoEventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button selectInitHour;
    private Button selectFinishHour;
    private TextView dateInit;
    private Evento infoEvento;
    private TextView dateFinish;
    private EditText editTextTitulo;
    private EditText editTextDescricao;

    private InterfaceComunicacaoFragment listener;

    public BasicInfoEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasicInfoEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicInfoEventFragment newInstance(String param1, String param2) {
        BasicInfoEventFragment fragment = new BasicInfoEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        if (activity instanceof InterfaceComunicacaoFragment) {
            listener = (InterfaceComunicacaoFragment) activity;
        } else {
            throw new RuntimeException("Activity deve implementar AddressEventFragment.InterfaceComunicao");
        }
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
        View view = inflater.inflate(R.layout.fragment_basic_info_event, container, false);

        infoEvento = new Evento();

        selectInitHour = view.findViewById(R.id.selectInitHour);
        selectFinishHour = view.findViewById(R.id.selectFinishHour);
        dateInit = view.findViewById(R.id.dateInit);
        dateFinish = view.findViewById(R.id.dateFinish);

        editTextDescricao = (EditText) view.findViewById(R.id.editTextDescricao);
        editTextTitulo = (EditText) view.findViewById(R.id.editTextTitulo);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dataHoraAtual = df.format(new Date());
        Date date = null;
        try {
            date = df.parse(dataHoraAtual);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long epoch = date.getTime();

        editTextTitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                infoEvento.setTituloevento(editTextTitulo.getText().toString());
                listener.setEvento(infoEvento);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        editTextDescricao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                infoEvento.setDescevento(editTextDescricao.getText().toString());
                listener.setEvento(infoEvento);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        selectInitHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoEvento.selectDateTime(view, dateInit, "inicio");
                listener.setEvento(infoEvento);
            }
        });

        selectFinishHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoEvento.selectDateTime(view, dateFinish, "fim");
                listener.setEvento(infoEvento);
            }
        });

        return view;
    }
}