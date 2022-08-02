package com.climber.everest.fragment;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

import static com.climber.everest.activity.LoginActivity.apiConfig;

import static java.lang.Double.parseDouble;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.climber.everest.R;
import com.climber.everest.activity.EventInfoActivity;
import com.climber.everest.activity.LoginActivity;
import com.climber.everest.config.RetrofitConfig;
import com.climber.everest.model.Endereco;
import com.climber.everest.model.Evento;
import com.climber.everest.model.RequestBody;
import com.climber.everest.model.Resultado;
import com.climber.everest.services.ApiService;
import com.climber.everest.services.InterfaceComunicacaoFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressEventFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public MapView mapView;
    public GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public Marker marcadorPrincipal;
    public EditText editTextLocalizacao;
    public LatLng localizacaoEvento;
    public Switch selectEnableLoc;
    private Retrofit retrofit;
    private Geocoder geocoder;
    private Endereco endereco;
    private Resultado resReq;
    private RequestBody req;
    private ConstraintLayout backloadAddress;

    private InterfaceComunicacaoFragment listener;

    // TODO: Rename and change types and number of parameters
    public static AddressEventFragment newInstance(String param1) {
        AddressEventFragment fragment = new AddressEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public AddressEventFragment() {
        // Required empty public constructor
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address_event, container, false);

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        req = new RequestBody();

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        editTextLocalizacao = view.findViewById(R.id.editTextLocalizacao);
        selectEnableLoc = view.findViewById(R.id.selectEnableLoc);
        backloadAddress = view.findViewById(R.id.backloadAddress);

        listener.setEnableEndereco(0);

//        LocalDateTime dataAtual = LocalDateTime.now();

        selectEnableLoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    listener.setEnableEndereco(1);
                    editTextLocalizacao.setEnabled(true);
                } else {
                    listener.setEnableEndereco(0);
                    editTextLocalizacao.setEnabled(false);
                }
            }
        });

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                marcadorPrincipal = mGoogleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .draggable(true));

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 90));

                listener.setLocalizacao(latLng);
                selecionaLocal(latLng);
            }
        };

        editTextLocalizacao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {

                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    try {
                        List<Address> listaEndereco = geocoder.getFromLocationName(editTextLocalizacao.getText().toString(), 1);
                        if(listaEndereco != null && listaEndereco.size() > 0)
                        {
                            Address endereco = listaEndereco.get(0);

                            if(marcadorPrincipal != null)
                            {
                                marcadorPrincipal.remove();
                            }

                            LatLng latLng = new LatLng(endereco.getLatitude(), endereco.getLongitude());

                            marcadorPrincipal = mGoogleMap.addMarker(
                                    new MarkerOptions()
                                            .position(latLng)
                                            .draggable(true));


                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 90));

                            listener.setLocalizacao(latLng);

                            editTextLocalizacao.setText(endereco.getAddressLine(0));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        // region Instanciando a retrofit
        retrofit = RetrofitConfig.getRetrofit();
        // endregion

        if(mParam1 != null)
        {
            backloadAddress.setVisibility(View.VISIBLE);
            ApiService apiService = retrofit.create(ApiService.class);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null){
                currentUser.reload();

                req.evento.setIdevento(Integer.valueOf(mParam1));

                apiService.buscaInfoEvento("application/json", apiConfig.token, req)
                        .enqueue(new Callback<Resultado>() {
                            @Override
                            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                                Log.e(TAG, response.toString());

                                if(response.isSuccessful())
                                {
                                    resReq = response.body();

                                    Log.e("O ID", "Id end: "+ resReq.evento.endereco.getIdeventoendereco());
                                    if(resReq.evento.endereco.getLatitude() != null && resReq.evento.endereco.getLongitude() != null)
                                    {
                                        Log.e("Tem end?", "Tem endereço");
                                        selecionaLocal(new LatLng(parseDouble(resReq.evento.endereco.getLatitude()), parseDouble(resReq.evento.endereco.getLongitude())));
                                    }
                                }

                                backloadAddress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<Resultado> call, Throwable t) {
                                Log.e("Error", "O erro foi: " + t.toString());
                                backloadAddress.setVisibility(View.GONE);
                            }
                        });
            }
        }

        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);

        if(ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mGoogleMap.setMyLocationEnabled(true);
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    10000,
                    locationListener
            );
        }

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if(marcadorPrincipal != null)
                {
                    marcadorPrincipal.remove();
                }

                marcadorPrincipal = mGoogleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .draggable(true));

                selecionaLocal(latLng);
            }
        });

        mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                LatLng latLng = marker.getPosition();

                selecionaLocal(latLng);
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void selecionaLocal(LatLng latLng)
    {
        try {
            List<Address> listaEndereco = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(listaEndereco != null && listaEndereco.size() > 0)
            {
                Address endereco = listaEndereco.get(0);
                editTextLocalizacao.setText(endereco.getAddressLine(0).toString());

                localizacaoEvento = new LatLng(endereco.getLatitude(), endereco.getLongitude());
                listener.setLocalizacao(localizacaoEvento);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}